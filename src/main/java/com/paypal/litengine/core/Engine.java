package com.paypal.litengine.core;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Engine {
    
    ThreadPoolExecutor processorExecutor=new ThreadPoolExecutor(100,100,100,TimeUnit.MILLISECONDS,new LinkedBlockingQueue<Runnable>());
    
    public void execute(Assemble assemble) throws InterruptedException, ExecutionException{
        
        Task start=assemble.getStartPoint();
        Object obj=null;
        if(start!=null&&!(start.getProcessor() instanceof SpareTaskProcessor)){
            start.getProcessor().process();
            obj= start.getOutput();
        } 
       
        List<Task> tasks=assemble.getTasks();
        
        List<Future> results= new ArrayList<Future>();
        for(final Task task: tasks){
            task.setInput(obj);
            Future future=processorExecutor.submit(new Callable(){

                @Override
                public Object call() throws Exception {
                    task.getProcessor().process();
                    return task.getOutput();
                }
            });
            
            results.add(future);
        }
        
        List<Object> outputs= new ArrayList<Object>();
        System.out.println(results.size());
        if(results.size()>0){
            for(Future<?> future:results){
                outputs.add(future.get());
            }
        }
        assemble.getEndPoint().setInput(outputs);
        assemble.getEndPoint().getProcessor().process();
        
    }
}
