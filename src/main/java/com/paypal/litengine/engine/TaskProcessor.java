package com.paypal.litengine.engine;

import com.paypal.litengine.Processor;

public abstract class TaskProcessor implements Processor {

    private Task task;
    
    @Override
    public void process() {
       task.setOutput( doProcess(task.getInput()));
    }
    
    public abstract Values doProcess(Tuple input);

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }
}
