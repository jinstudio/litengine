package com.paypal.litengine.engine;

import java.util.ArrayList;
import java.util.List;

import com.paypal.litengine.Context;
import com.paypal.litengine.Tuple;

public class Assemble implements Context{

    private Task start;
    private Task end;
    private List<Task> tasks = new ArrayList<Task>();

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

    @Override
    public void initTriggerSource(Tuple input) {
        start.setInput(input);
    }
}
