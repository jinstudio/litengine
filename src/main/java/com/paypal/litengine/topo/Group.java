package com.paypal.litengine.topo;

import java.util.ArrayList;
import java.util.List;

import com.paypal.litengine.engine.Task;

public class Group {
    
    private String name;
    
    private List<Task> tasks = new ArrayList<Task>();
    
    private Topology topology;

    //private List<Group> _groups= new ArrayList<Group>();
    
    private List<Group> parent=new ArrayList<Group>();
    
    public Group(String name) {
        this.name = name;
    }
    
    public Group(String name,Topology topo) {
        this.name = name;
        this.topology=topo;
    }

    public void addTask(Task task){
        tasks.add(task);
    }
    
    public List<Task> getTasks(){
        return this.tasks;
    }
    
    public boolean hasParent(){
        return this.parent.size()>0;
    }
    
    public List<Group> getParent() {
        return parent;
    }

    public void wait(String name){
        Group group=topology.get(name);
        if(group !=null){
            this.addParent(group);
        }
        else{
            Group parent= new Group(name);//create parent first in case parent not exists yet, later to update
            this.addParent(parent);
        }
    }
    
    public void addParent(Group parent){
        if(!this.parent.contains(parent)) {
            this.parent.add(parent);
        }
    }
    
    
    public void setTopology(Topology topology) {
        this.topology = topology;
    }

    public String getName() {
        return this.name;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.name == null) ? 0 : this.name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj)
            return true;
        if(obj == null)
            return false;
        if(getClass() != obj.getClass())
            return false;
        Group other = (Group) obj;
        if(this.name == null) {
            if(other.name != null)
                return false;
        } else if(!this.name.equals(other.name))
            return false;
        return true;
    }
    
}
