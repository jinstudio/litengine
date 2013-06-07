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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.paypal.litengine.BaseEngine;
import com.paypal.litengine.Processor;
import com.paypal.litengine.Status;
import com.paypal.litengine.topo.Group;

public class TopologyEngine extends BaseEngine<TopoContext> {
	
	final Logger logger = LoggerFactory.getLogger(TopologyEngine.class);

    ThreadPoolExecutor processorExecutor = new ThreadPoolExecutor(100, 200, 0, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>());
   // ThreadPoolExecutor subProcessorExecutor = new ThreadPoolExecutor(80, 600, 0, TimeUnit.MILLISECONDS,
   //         new LinkedBlockingQueue<Runnable>());

    @Override
	public void execute(TopoContext context) {
		this.execute(context, false);
	}

    private void execute(final TopoContext context,boolean once) {
        context.lock();
        List<Group> canRuns = context.getCanRunGroups();
        if(Status.READY == context.getStatus())
            context.setStatus(Status.RUNNING);
        //execution finished
        if(context.isDone()||context.isTimeout()){ 
            if(logger.isDebugEnabled())
                logger.debug("context done === current thread:{}",Thread.currentThread().getName());
        	context.setStatus(Status.DONE);
        	context.unlock();
            return;
        }
       
        while(!once&&canRuns.size()==0){
            if(context.isDone()||context.isTimeout()){
                logger.debug("context done === current thread:{}",Thread.currentThread().getName());
                context.setStatus(Status.DONE);
                context.unlock();
                return;
            }
            canRuns = context.getCanRunGroups();
        }
        
        final List<Group> clonedCanRuns=new ArrayList(canRuns);
        context.unlock();
        final Map<Group,OutputFieldsDeclarer> filedMapping= new HashMap<Group,OutputFieldsDeclarer>();
        final Map<Group,List<Future>> futureMapping= new HashMap<Group,List<Future>>();
        
        for(Group group: canRuns) {
            group.lock();
            logger.debug("try to running group-------------------->{}",group.getName());
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
           
            processorExecutor.execute(new Runnable(){
                @Override
                public void run() {
                    execute(context,true);
                }
        });
        }
        context.lock();
       
        processorExecutor.execute(new Runnable(){ 
            @Override
            public void run() {
                for(Group group: clonedCanRuns){
                    Values outputs = new Values();
                    OutputFieldsDeclarer declarer =filedMapping.get(group);
                    List<Future> futures=futureMapping.get(group);
                    if(futures.size() > 0) {
                        for(Future<?> future: futures) {
                            try {
                                Values values=(Values)future.get();
                                if(values!=null){
                                    outputs.addAll(values);
                                }
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    List<Group> kids = context.getChildren(group);
                    for(Group kid: kids) {
                        context.addInputMapping(group,kid, new TupleImpl(declarer.getFieldsDeclaration(), outputs));
                    }
                }
            }
    });
        context.unlock();
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
            if(context.isTimeout()){
                logger.debug("timeout-------->{}",group.getName());
                return null;
            }
            logger.debug("start to run group-------------------->{}",group.getName());
            Processor processor = this.task.getProcessor();
            if(processor!=null){
            	processor.process();
            	logger.debug("end processor process processor:"+this.task.getProcessor());
            	processor.declareOutputFields(this.declarer);
            	logger.debug("try to mark done processor:"+this.task.getProcessor());
            }
            context.markDone(group, task);
            logger.debug("end to run group-------------------->{}",group.getName());
            return this.task.getOutput();
        }

    }

}
