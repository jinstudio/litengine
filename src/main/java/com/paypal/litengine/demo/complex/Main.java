package com.paypal.litengine.demo.complex;

import java.util.concurrent.ExecutionException;

import com.paypal.litengine.engine.Context;
import com.paypal.litengine.engine.Task;
import com.paypal.litengine.engine.TopologyEngine;
import com.paypal.litengine.topo.Topology;
import com.paypal.litengine.topo.TopologyBuilder;

public class Main {

    /**
     * @param args
     * @throws ExecutionException 
     * @throws InterruptedException 
     */
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        TopologyBuilder builder = new TopologyBuilder();
        builder.addTask("root", new Task().setProcessor(new GenerateDataProcessor()));
        builder.addTask("group1", new Task().setProcessor(new AppendStringProcessor())).wait("root");
        builder.addTask("group2", new Task().setProcessor(new PassdownProcessor())).wait("group1");
        builder.addTask("group3", new Task().setProcessor(new DummyProcessor())).wait("group2");
        builder.addTask("group4", new Task().setProcessor(new PassdownProcessor())).wait("group2");
        Topology topo= builder.createTopology();
        TopologyEngine engine= new TopologyEngine();
        engine.execute(new Context(topo));
    }

}
