package com.paypal.litengine.demo.simple;

import java.util.concurrent.ExecutionException;

import com.paypal.litengine.Engine;
import com.paypal.litengine.EngineFactory;
import com.paypal.litengine.Tuple;
import com.paypal.litengine.engine.Assemble;
import com.paypal.litengine.engine.SimpleEngine;
import com.paypal.litengine.engine.Task;
import com.paypal.litengine.engine.Values;

/**
 * example using EngineFactory
 * @author jyao1
 *
 */
public class SimpleDemo2 {

    /**
     * @param args
     * @throws ExecutionException 
     * @throws InterruptedException 
     */
    public static void main(String[] args) throws InterruptedException, ExecutionException {
    	
        Assemble ass= new Assemble();
        ass.setStartPoint(new Task());
        SetTransactionContextRequest request= new SetTransactionContextRequest();
        ass.addWorkingTask(new Task().setInput(request).setProcessor(new AddressNormTaskProcessor1()));
        ass.addWorkingTask(new Task().setInput(request).setProcessor(new TransformTaskProcessor()));
        ass.addWorkingTask(new Task().setInput(request).setProcessor(new AddressNormTaskProcessor2()));
        ass.setEndPoint(new Task().setProcessor(new ComposeTaskProcessor()));
        
        //trigger like this
        Tuple output=EngineFactory.getInstance().getEngine(ass).trigger(null);
        System.out.println(output);
        for(Object o:output)
        	System.out.println(o);
        
    }

}
