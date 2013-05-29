package com.paypal.litengine.demo.complex;

import com.paypal.litengine.engine.Task;
import com.paypal.litengine.topo.TopologyBuilder;

public class Main {

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        TopologyBuilder builder = new TopologyBuilder();
        builder.addTask("root", new Task());
        builder.addTask("group1", new Task()).wait("root");
        builder.addTask("group2", new Task()).wait("group1");
        builder.addTask("group3", new Task()).wait("group2");
        builder.addTask("group4", new Task()).wait("group2");
    }

}
