package com.paypal.litengine.demo.simple;

import java.util.concurrent.ExecutionException;

import com.paypal.litengine.Engine;
import com.paypal.litengine.Tuple;
import com.paypal.litengine.engine.Assemble;
import com.paypal.litengine.engine.SimpleEngine;
import com.paypal.litengine.engine.Task;

public class SimpleDemo {

    /**
     * @param args
     * @throws ExecutionException 
     * @throws InterruptedException 
     */
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        Assemble ass= new Assemble();
        ass.setStartPoint(new Task());
        SetTransactionContextRequest request= new SetTransactionContextRequest();
//        Task tmp=new Task().setInput(request).setProcessor(new AddressNormTaskProcessor1());
//        System.out.println(tmp.getInput());
        ass.addWorkingTask(new Task().setInput(request).setProcessor(new AddressNormTaskProcessor1()));
        ass.addWorkingTask(new Task().setInput(request).setProcessor(new TransformTaskProcessor()));
        ass.addWorkingTask(new Task().setInput(request).setProcessor(new AddressNormTaskProcessor2()));
        ass.setEndPoint(new Task().setProcessor(new ComposeTaskProcessor()));
        
        Engine<Assemble> engine= new SimpleEngine();
        //engine.execute(ass);
        Tuple output=engine.trigger(ass, null);
        System.out.println(output);
        for(Object o:output)
        	System.out.println(o);
        
    }

}
