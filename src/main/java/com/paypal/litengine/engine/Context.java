package com.paypal.litengine.engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.paypal.litengine.topo.Group;
import com.paypal.litengine.topo.Topology;

public class Context {
    
    Status status=Status.READY;
    Topology topology;
    private List<Group> doneGroups=new ArrayList<Group>();   
    private Map<Group,List<Task>> doneGroupsMapping=new HashMap<Group,List<Task>>();
    private Map<Group,Tuple> inputMapping= new HashMap<Group,Tuple>();
    
    public Context(Topology topology) {
        super();
        this.topology = topology;
    }

    public void markDone(Group group){
        doneGroups.add(group);
    }
    
    public boolean isDone(Group group){
       return doneGroups.contains(group);
    }
    
    public boolean isDone(List<Group> groups){
        return doneGroups.contains(groups);
     }
    
    public void markDone(Group group,Task task){
        List<Task> tasks=doneGroupsMapping.get(group);
        if(tasks==null){
            List lists= new ArrayList<Task>(); 
            lists.add(task);
            doneGroupsMapping.put(group, lists);
            tasks=lists;
        }
        else{
            tasks.add(task);
        }
        if(tasks.containsAll(group.getTasks())){
            markDone(group);
        }
    }
    
    public Tuple getInput(Group group){
        return inputMapping.get(group);
    }
    
    public void addInputMapping(Group group,Tuple tuple){
        inputMapping.put(group, tuple);
    }
    
    public List<Group> getCanRunGroups(){
        List<Group> canRuns= new ArrayList<Group>();
        if(Status.READY==status)
            return topology.getStarter();
        else if(Status.RUNNING==status){
            List<Group> normal=topology.getNormal();
            
            for(Group group: normal){
                if(!isDone(group)&&isDone(group.getParent())){
                    canRuns.add(group);
                }
            }
        }
        return canRuns;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

}
