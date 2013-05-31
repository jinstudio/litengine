package com.paypal.litengine.engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.paypal.litengine.BaseContext;
import com.paypal.litengine.Tuple;
import com.paypal.litengine.topo.Group;
import com.paypal.litengine.topo.Topology;

public class TopoContext extends BaseContext{
    
    Status status=Status.READY;
    Topology topology;
    private volatile List<Group> doneGroups=new ArrayList<Group>();   
    private volatile Map<Group,List<Task>> doneGroupsMapping=new HashMap<Group,List<Task>>();
    private volatile Map<Group,Tuple> inputMapping= new HashMap<Group,Tuple>();
    private volatile List<Group> scheduledGroups=new ArrayList<Group>();
    private Lock lock = new ReentrantLock();
    
    public TopoContext(Topology topology) {
        super();
        this.topology = topology;
    }

    public void markDone(Group group){
        doneGroups.add(group);
    }
    
    public boolean isDone(Group group){
       return doneGroups.contains(group);
    }
    
    public boolean isDone(List<Group> groups){
        return doneGroups.containsAll(groups);
     }
    
    public synchronized void markDone(Group group,Task task){
        List<Task> tasks=doneGroupsMapping.get(group);
        if(tasks==null){
            List<Task> lists= new ArrayList<Task>(); 
            lists.add(task);
            doneGroupsMapping.put(group, lists);
            tasks=lists;
        }
        else{
            tasks.add(task);
        }
        if(tasks.containsAll(group.getTasks())){
            markDone(group);
        }
    }
    
    public List<Group> getChildren(Group group){
    	List<Group> kids= new ArrayList<Group>();
    	for(Group normal:topology.getNormal()){
    		if(normal.getParent().contains(group))
    			kids.add(normal);
    	}
    	return kids;
    }
    
    public Tuple getInput(Group group){
        while(this.topology.getNormal().contains(group)&&!inputMapping.containsKey(group)){
            
        }
        return inputMapping.get(group);
    }
    
    public synchronized void addInputMapping(Group parent,Group group,Tuple tuple){
    	Tuple tup=inputMapping.get(group);
    	if(tup!=null){
    		tup.add(tuple,parent.getName());
    	}
    	else
    		inputMapping.put(group, tuple);
    }
    
    public synchronized void addInputMapping(Group group,Fields fields){
        inputMapping.put(group, new TupleImpl(fields));
    }
    
    public synchronized List<Group> getCanRunGroups(){
       // System.out.println("status:"+status);
       // System.out.println("doneGroups:"+doneGroups);
      //  System.out.println("scheduledGroups:"+scheduledGroups);
        List<Group> canRuns= new ArrayList<Group>();
        if(Status.READY==status)
            return topology.getStarter();
        else if(Status.RUNNING==status){
            List<Group> normal=topology.getNormal();
            for(Group group: normal){
            //    System.out.println("getCanRunGroups:"+group);
                if(!isDone(group)&&isDone(group.getParent())&&!scheduledGroups.contains(group)){
                    canRuns.add(group);
                }
            }
        }
     //   System.out.println("canRuns:"+canRuns);
        scheduledGroups.addAll(canRuns);
        return canRuns;
    }

    public synchronized boolean canRun(Group group){
    	return getCanRunGroups().contains(group);
    }
    
    public Status getStatus() {
        return status;
    }

    public synchronized void setStatus(Status status) {
        this.status = status;
    }
    
    public synchronized boolean isDone(){
        if(Status.DONE==this.status)
            return true;
        System.out.println("this.getCanRunGroups:"+this.getCanRunGroups());
        System.out.println("this.topology.getAll:"+this.topology.getAll().size());
        System.out.println("this.doneGroups:"+this.doneGroups.size());
        return this.getCanRunGroups().size()==0&&(this.doneGroups.size()==this.topology.getAll().size());
    }

    @Override
    public void initTriggerSource(Tuple input) {
       List<Group> starters= this.topology.getStarter();
       for(Group group: starters){
           
          List<Task> tasks= group.getTasks();
          for(Task task:tasks){
              task.setInput(input);
          }
           
       }
    }
/**
 * TODO while status is not done, 
 * maybe throw exception is better, since this is a invalid case to get final output while the flow is still running
 * but in the future, if need to support partial return for SLA constraint, then should switch to another logic to support
 * return the done task output (the done level is based on task, not on the whole engine)
 */
	@Override
	public Tuple getFinalOutput() {
		if(this.status!=Status.DONE)
			return null;
		List<Group> all=this.topology.getAll();
		List<Group> nonleaf= this.topology.getNonleaf();
		List<Group> leaf=new ArrayList<Group>();
		for(Group g:all){
			if(!nonleaf.contains(g)){
				leaf.add(g);
			}
		}
		Fields fields=new Fields();
		Values values= new Values();
		for(Group g: leaf){
			for(Task task:g.getTasks()){
				fields.add(task.getOutputFields());
				values.add(task.getOutput());
			}
		}
		
		return new TupleImpl(fields,values);
	}
	
	 public void lock(){
	        this.lock.lock();
	 }
	    
	 public void unlock(){
	        this.lock.unlock();
	 }

}
