package com.paypal.litengine.core;

import java.util.ArrayList;
import java.util.List;

public class Assemble {

    private Task start;
    private Task end;
    private List<Task> tasks = new ArrayList();

    public void setStartPoint(Task task) {
        if(start == null)
            start = task;
    }
    
    public final Task getStartPoint(){
        return start;
    }

    public void addWorkingTask(Task task) {
        tasks.add(task);
    }

    public void setEndPoint(Task task) {
        if(end == null)
            end = task;
    }
    
    public final Task getEndPoint(){
        return end;
    }
    
    public final List<Task> getTasks(){
        return this.tasks;
    }
}
