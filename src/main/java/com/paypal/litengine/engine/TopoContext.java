package com.paypal.litengine.engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.paypal.litengine.BaseContext;
import com.paypal.litengine.Status;
import com.paypal.litengine.Tuple;
import com.paypal.litengine.topo.Group;
import com.paypal.litengine.topo.Topology;
import com.paypal.litengine.topo.TopologyBuilder;
import com.paypal.litengine.topo.config.Config;

public class TopoContext extends BaseContext {

    private volatile Status status = Status.READY;
	Topology topology;
	private volatile List<Group> doneGroups = new ArrayList<Group>();
	private volatile Map<Group, List<Task>> doneGroupsMapping = new HashMap<Group, List<Task>>();
	private volatile Map<Group, Tuple> inputMapping = new HashMap<Group, Tuple>();
	private volatile List<Group> scheduledGroups = new ArrayList<Group>();
	private Lock lock = new ReentrantLock();

	public TopoContext(Topology topology) {
		super();
		this.topology = topology;
		//clear task references to make sure the correct final output
		for(Group group:topology.getAll()){
		    group.clear();
		}
	}

	public TopoContext(Config config) {
		super();
		TopologyBuilder builder = new TopologyBuilder();
		for (Class<? extends TaskProcessor> cls : config.processors()) {
			com.paypal.litengine.topo.config.Groups groups = cls
					.getAnnotation(com.paypal.litengine.topo.config.Groups.class);
			
			com.paypal.litengine.topo.config.Group group1 = cls
					.getAnnotation(com.paypal.litengine.topo.config.Group.class);
			
			if (groups != null) {
				for (com.paypal.litengine.topo.config.Group group : groups
						.group()) {
					fromConfigGroup(builder, cls, group);
				}
			}
			if(group1!=null){
				fromConfigGroup(builder,cls,group1);
			}
		}
		Topology topo = builder.createTopology();
		this.topology = topo;

	}

	private void fromConfigGroup(TopologyBuilder builder,
			Class<? extends TaskProcessor> cls,
			com.paypal.litengine.topo.config.Group group) {
		String name = group.name();
		String[] waits = group.waits();
		try {
			Group tmp = builder.addTask(name,
					new Task().setProcessor(cls.newInstance()));
			if (waits != null) {
				for (String wait : waits)
					tmp.wait(wait);
			}
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	public void markDone(Group group) {
		doneGroups.add(group);
	}

	public boolean isDone(Group group) {
		return doneGroups.contains(group);
	}

	public boolean isDone(List<Group> groups) {
		return doneGroups.containsAll(groups);
	}

	public void markDone(Group group, Task task) {
		List<Task> tasks = doneGroupsMapping.get(group);
		if (tasks == null) {
			List<Task> lists = new ArrayList<Task>();
			lists.add(task);
			doneGroupsMapping.put(group, lists);
			tasks = lists;
		} else {
			tasks.add(task);
		}
		if (tasks.containsAll(group.getTasks())) {
			markDone(group);
		}
	}

	public List<Group> getChildren(Group group) {
		List<Group> kids = new ArrayList<Group>();
		for (Group normal : topology.getNormal()) {
			if (normal.getParent().contains(group))
				kids.add(normal);
		}
		return kids;
	}

	public Tuple getInput(Group group) {
		while (this.topology.getNormal().contains(group)
				&& !inputMapping.containsKey(group)) {

		}
		return inputMapping.get(group);
	}

	public void addInputMapping(Group parent, Group group, Tuple tuple) {
		Tuple tup = inputMapping.get(group);
		if (tup != null) {
			tup.add(tuple, parent.getName());
		} else
			inputMapping.put(group, tuple);
	}

	public void addInputMapping(Group group, Fields fields) {
		inputMapping.put(group, new TupleImpl(fields));
	}

	public List<Group> getCanRunGroups() {
		List<Group> canRuns = new ArrayList<Group>();
		if (Status.READY == status)
			return topology.getStarter();
		else if (Status.RUNNING == status) {
			List<Group> normal = topology.getNormal();
			for (Group group : normal) {
				if (!isDone(group) && isDone(group.getParent())
						&& !scheduledGroups.contains(group)) {
					canRuns.add(group);
				}
			}
		}
		scheduledGroups.addAll(canRuns);
		return canRuns;
	}

	public boolean canRun(Group group) {
		return getCanRunGroups().contains(group);
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public boolean isDone() {
		if (Status.DONE == this.status)
			return true;
		//System.out.println("doneGroups:"+doneGroups);
		return this.doneGroups.size() == this.topology.getAll().size();
		
	}
	
	public boolean isTimeout(){
	    return Status.TIME_OUT==this.status;
	}

	@Override
	public void initTriggerSource(Tuple input) {
		List<Group> starters = this.topology.getStarter();
		for (Group group : starters) {

			List<Task> tasks = group.getTasks();
			for (Task task : tasks) {
				task.setInput(input);
			}

		}
	}

	/**
	 * TODO while status is not done, maybe throw exception is better, since
	 * this is a invalid case to get final output while the flow is still
	 * running but in the future, if need to support partial return for SLA
	 * constraint, then should switch to another logic to support return the
	 * done task output (the done level is based on task, not on the whole
	 * engine)
	 */
	@Override
	public Tuple getFinalOutput() {
		if (Status.DONE!=this.status && Status.TIME_OUT!=this.status)
			return null;
		List<Group> all = this.topology.getAll();
		List<Group> nonleaf = this.topology.getNonleaf();
		List<Group> leaf = new ArrayList<Group>();
		for (Group g : all) {
			if (!nonleaf.contains(g)) {
				leaf.add(g);
			}
		}
		Fields fields = new Fields();
		Values values = new Values();
		for (Group g : leaf) {
			for (Task task : g.getTasks()) {
			    Fields outputFields = task.getOutputFields();
                if(outputFields!=null){
			        fields.add(outputFields,g.getName());
			        values.add(task.getOutput());
			    }
			}
		}

		return new TupleImpl(fields, values);
	}

	public void lock() {
		this.lock.lock();
	}

	public void unlock() {
		this.lock.unlock();
	}

}
