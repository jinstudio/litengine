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

import com.paypal.litengine.BaseEngine;
import com.paypal.litengine.Processor;
import com.paypal.litengine.topo.Group;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TopologyEngine extends BaseEngine<TopoContext> {
	
	final Logger logger = LoggerFactory.getLogger(TopologyEngine.class);

    ThreadPoolExecutor processorExecutor = new ThreadPoolExecutor(100, 100, 100, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>());
   // long mainThread=-1;

    @Override
	public void execute(TopoContext context) {
		// TODO Auto-generated method stub
		this.execute(context, false);
	}

    private void execute(final TopoContext context,boolean once) {
        context.lock();
        List<Group> canRuns = context.getCanRunGroups();
        logger.debug("canRuns-----"+canRuns);
        if(Status.READY == context.getStatus())
            context.setStatus(Status.RUNNING);
        //execution finished
        if(context.isDone()){ 
            logger.debug("context done === Thread.currentThread().getId:"+Thread.currentThread().getId());
        	context.setStatus(Status.DONE);
        	context.unlock();
            return;
        }
        logger.debug("Thread.currentThread().getId:"+Thread.currentThread().getId());
        while(!once&&canRuns.size()==0){
            canRuns = context.getCanRunGroups();
        }
        context.unlock();
        Map<Group,OutputFieldsDeclarer> filedMapping= new HashMap<Group,OutputFieldsDeclarer>();
        Map<Group,List<Future>> futureMapping= new HashMap<Group,List<Future>>();
        
        for(Group group: canRuns) {
            group.lock();
            logger.debug("try to running group-------------------->"+group.getName());
            List<Task> tasks = group.getTasks();
            OutputFieldsDeclarer declarer = new OutputFieldsDeclarerImpl();
            List<Future> futures = new ArrayList<Future>();
            if(tasks.size() == 0)
                context.markDone(group);
            for(Task task: tasks) {
                task.setInput(context.getInput(group));
                Future future = processorExecutor.submit(new TopologyCallable(context, group, task, declarer));
                futures.add(future);
            }
            filedMapping.put(group, declarer);
            futureMapping.put(group,futures);
            group.unlock();
           
            new Thread(new Runnable(){

                @Override
                public void run() {
                    execute(context,true);
                }
        }).start();
        }
        
        for(Group group: canRuns){
            Values outputs = new Values();
            OutputFieldsDeclarer declarer =filedMapping.get(group);
            List<Future> futures=futureMapping.get(group);
            if(futures.size() > 0) {
                for(Future<?> future: futures) {
                    try {
                        outputs.add(future.get());
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }

            List<Group> kids = context.getChildren(group);
            for(Group kid: kids) {
                context.addInputMapping(group,kid, new TupleImpl(declarer.getFieldsDeclaration(), outputs));
            }
        }
        if(!once)
            execute(context);
    }

    class TopologyCallable implements Callable {

        Task task;
        OutputFieldsDeclarer declarer;
        TopoContext context;
        Group group;

        TopologyCallable(TopoContext context, Group group, Task task, OutputFieldsDeclarer declarer) {
            this.context = context;
            this.group = group;
            this.task = task;
            this.declarer = declarer;
        }

        @Override
        public Object call() throws Exception {
            logger.debug("start to run group-------------------->"+group.getName());
            Processor processor = this.task.getProcessor();
            processor.process();
            logger.debug("end processor process processor:"+this.task.getProcessor());
            processor.declareOutputFields(this.declarer);
            logger.debug("try to mark done processor:"+this.task.getProcessor());
            context.markDone(group, task);
            // execute(context);
            logger.debug("end to run group-------------------->"+group.getName());
            return this.task.getOutput();
        }

    }

	

}
