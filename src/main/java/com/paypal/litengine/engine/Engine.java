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

public class Engine {
    
    ThreadPoolExecutor processorExecutor=new ThreadPoolExecutor(100,100,100,TimeUnit.MILLISECONDS,new LinkedBlockingQueue<Runnable>());
    
    public void execute(Assemble assemble) throws InterruptedException, ExecutionException{
        
        Task start=assemble.getStartPoint();
        Tuple obj=null;
        if(start!=null&&!(start.getProcessor() instanceof SpareTaskProcessor)){
        	Processor processor= start.getProcessor();
        	OutputFieldsDeclarer declarer= new OutputFieldsDeclarerImpl();
        	processor.declareOutputFields(declarer);
        	Fields fields=declarer.getFieldsDeclaration();
        	processor.process();
            Values values= start.getOutput();
            obj= new TupleImpl(fields,values);
        } 
       
        List<Task> tasks=assemble.getTasks();
        
        List<Future> results= new ArrayList<Future>();
        final OutputFieldsDeclarer declarer= new OutputFieldsDeclarerImpl();
        Fields fields=new Fields();
        for(final Task task: tasks){
            task.setInput(obj);
            Future future=processorExecutor.submit(new Callable(){

                @Override
                public Object call() throws Exception {
                	Processor processor=task.getProcessor();
                	processor.process();
                	processor.declareOutputFields(declarer);
                    return task.getOutput();
                }
            });
            
            results.add(future);
        }
        
        Values outputs= new Values();
        System.out.println(results.size());
        if(results.size()>0){
            for(Future<?> future:results){
                outputs.add(future.get());
            }
        }
        assemble.getEndPoint().setInput(new TupleImpl(declarer.getFieldsDeclaration(),outputs));
        assemble.getEndPoint().getProcessor().process();
        
    }
}
