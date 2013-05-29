package com.paypal.litengine.engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.paypal.litengine.Processor;
import com.paypal.litengine.topo.Group;

public class TopologyEngine {

	ThreadPoolExecutor processorExecutor = new ThreadPoolExecutor(100, 100,
			100, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());

	public void execute(Context context) throws InterruptedException,
			ExecutionException {

		List<Group> canRuns = context.getCanRunGroups();
		if (Status.READY == context.getStatus())
			context.setStatus(Status.RUNNING);
		//Map<Group,List<Future>> results = new HashMap<Group,List<Future>>();
		if(canRuns.size()==0)
			return;
		for (Group group : canRuns) {
			group.lock();
			List<Task> tasks = group.getTasks();
			OutputFieldsDeclarer declarer = new OutputFieldsDeclarerImpl();
			List<Future> futures= new ArrayList<Future>();
			if(tasks.size()==0)
				context.markDone(group);
			for (Task task : tasks) {
				task.setInput(context.getInput(group));
				Future future = processorExecutor.submit(new TopologyCallable(
						context, group, task, declarer));
				futures.add(future);
			}
			
			group.unlock();
			
			Values outputs = new Values();
			if (futures.size() > 0) {
				for (Future<?> future : futures) {
					outputs.add(future.get());
				}
			}
			
			List<Group> kids=context.getChildren(group);
			for(Group kid: kids){
				context.addInputMapping(kid, new TupleImpl(declarer.getFieldsDeclaration(),outputs));
			}
		}
//		Values outputs = new Values();
//		if (results.size() > 0) {
//			for (Future<?> future : results) {
//				outputs.add(future.get());
//			}
//		}
		
		this.execute(context);

	}

	class TopologyCallable implements Callable {

		Task task;
		OutputFieldsDeclarer declarer;
		Context context;
		Group group;

		TopologyCallable(Context context, Group group, Task task,
				OutputFieldsDeclarer declarer) {
			this.context = context;
			this.group = group;
			this.task = task;
			this.declarer = declarer;
		}

		@Override
		public Object call() throws Exception {
			Processor processor = this.task.getProcessor();
			processor.process();
			processor.declareOutputFields(this.declarer);
			context.markDone(group, task);
			// execute(context);
			return this.task.getOutput();
		}

	}

}
