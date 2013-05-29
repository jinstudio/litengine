package com.paypal.litengine.demo.simple;

import java.util.concurrent.ExecutionException;

import com.paypal.litengine.engine.Assemble;
import com.paypal.litengine.engine.Engine;
import com.paypal.litengine.engine.Task;

public class Main {

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
        
        Engine engine= new Engine();
        engine.execute(ass);
        ass.getEndPoint().getOutput();
    }

}
