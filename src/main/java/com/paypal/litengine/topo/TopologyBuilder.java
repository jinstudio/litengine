package com.paypal.litengine.topo;

import com.paypal.litengine.engine.Task;


public class TopologyBuilder {
    
    private Topology topology= new Topology();
    
    public Topology createTopology(){
        return topology.init();
    }
    
    public Group addTask(String name,Task task){
        Group group=topology.get(name);
        if(group!=null){
            group.addTask(task);
        }
        else {
            group= new Group(name);
            topology.add(group);
        }
        
        return group;
    }
    
}
