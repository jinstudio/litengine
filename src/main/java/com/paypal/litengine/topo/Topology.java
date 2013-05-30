package com.paypal.litengine.topo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Topology {
    
    private List<Group> starter=new ArrayList<Group>();
    
    private List<Group> all=new ArrayList<Group>();
    
    private List<Group> normal=new ArrayList<Group>();
    
    private List<Group> nonleaf=new ArrayList<Group>();
    
    public boolean contains(Group group){
        return this.all.contains(group);
    }
    
    public boolean contains(String group){
        return this.all.contains(new Group(group));
    }
    
    public Group get(Group group){
        return get(group.getName());
    }
    
    public Group get(String group){
        for(Group g: this.all){
            if(g.getName().equals(group))
                return g;
        }
        return null;
    }
    
    public void add(Group group){
        if(!this.all.contains(group)){
            this.all.add(group);
            group.setTopology(this);
        }
    }
    
    protected void addNonleaf(Group group){
    	this.nonleaf.add(group);
    }
    
    protected Topology init(){
        for(Group group: all){
            if(!group.hasParent()){
                this.starter.add(group);
            }
            else
                normal.add(group);
        }
        return this;
    }
    
    public List<Group> getAll(){
    	return Collections.unmodifiableList(this.all);
    }
    
    public List<Group> getStarter(){
        return Collections.unmodifiableList(this.starter);
    }
    public List<Group> getNormal(){
       
        return Collections.unmodifiableList(this.normal);
    }
    
    public List<Group> getNonleaf(){
    	return Collections.unmodifiableList(this.nonleaf);
    }
    
}
