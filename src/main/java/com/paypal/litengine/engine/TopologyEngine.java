package com.paypal.litengine.engine;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.paypal.litengine.Processor;
import com.paypal.litengine.topo.Group;

public class TopologyEngine {
    
    ThreadPoolExecutor processorExecutor=new ThreadPoolExecutor(100,100,100,TimeUnit.MILLISECONDS,new LinkedBlockingQueue<Runnable>());
    
    public void execute(Context context) throws InterruptedException, ExecutionException{
        
        List<Group> canRuns=context.getCanRunGroups();
        if(Status.READY==context.getStatus())
            context.setStatus(Status.RUNNING);
        List<Future> results= new ArrayList<Future>();
        OutputFieldsDeclarer declarer= new OutputFieldsDeclarerImpl();
        for(Group group: canRuns){
            List<Task> tasks=group.getTasks();
            for(Task task: tasks){
                task.setInput(context.getInput(group));
                Future future= processorExecutor.submit(new TopologyCallable(context,group,task,declarer));
                results.add(future);
            }
        }
        Values outputs= new Values();
        if(results.size()>0){
            for(Future<?> future:results){
                outputs.add(future.get());
            }
        }
        
        
    }
    
    class TopologyCallable implements Callable{
       
        Task task;
        OutputFieldsDeclarer declarer;
        Context context;
        Group group;
        
        TopologyCallable(Context context,Group group,Task task, OutputFieldsDeclarer declarer){
            this.context=context;
            this.group=group;
            this.task=task;
            this.declarer=declarer;
        }
        
        @Override
        public Object call() throws Exception {
            // TODO Auto-generated method stub
            Processor processor=this.task.getProcessor();
            processor.process();
            processor.declareOutputFields(this.declarer);
            context.markDone(group,task);
            execute(context);
            return this.task.getOutput();
        }
        
    }

}
