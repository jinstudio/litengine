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

public class TopologyEngine extends BaseEngine<TopoContext> {

    ThreadPoolExecutor processorExecutor = new ThreadPoolExecutor(100, 100, 100, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>());
    long mainThread=-1;

    @Override
    public void execute(final TopoContext context) {
        if(mainThread<0) {
            mainThread=Thread.currentThread().getId();
            System.out.println("amin thread id is:"+mainThread);
        }
        context.lock();
        List<Group> canRuns = context.getCanRunGroups();
        System.out.println("canRuns-----"+canRuns);
        if(Status.READY == context.getStatus())
            context.setStatus(Status.RUNNING);
        //execution finished
        if(context.isDone()){ 
            System.out.println("context done === Thread.currentThread().getId:"+Thread.currentThread().getId());
        	context.setStatus(Status.DONE);
        	context.unlock();
            return;
        }
        System.out.println("Thread.currentThread().getId:"+Thread.currentThread().getId());
        while(canRuns.size()==0&&mainThread==Thread.currentThread().getId()){
            canRuns = context.getCanRunGroups();
        }
        context.unlock();
        Map<Group,OutputFieldsDeclarer> filedMapping= new HashMap<Group,OutputFieldsDeclarer>();
        Map<Group,List<Future>> futureMapping= new HashMap<Group,List<Future>>();
        
        for(Group group: canRuns) {
            group.lock();
            System.out.println("try to running group-------------------->"+group.getName());
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
                    execute(context);
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
        if(mainThread==Thread.currentThread().getId())
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
            System.out.println("start to run group-------------------->"+group.getName());
            Processor processor = this.task.getProcessor();
            processor.process();
            System.out.println("end processor process processor:"+this.task.getProcessor());
            processor.declareOutputFields(this.declarer);
            System.out.println("try to mark done processor:"+this.task.getProcessor());
            context.markDone(group, task);
            // execute(context);
            System.out.println("end to run group-------------------->"+group.getName());
            return this.task.getOutput();
        }

    }

}
