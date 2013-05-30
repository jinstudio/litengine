package com.paypal.litengine.engine;

import com.paypal.litengine.Processor;
import com.paypal.litengine.Tuple;

public abstract class TaskProcessor implements Processor {

    private Task task;
    
    @Override
    public void process() {
       task.setOutput( doProcess(task.getInput()));
    }
    
    @Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
    	Fields fields=this.defineOutput();
    	declarer.declare(fields);
    	task.setOutputFields(fields);
	}
    
    public abstract Values doProcess(Tuple input);
    
    public abstract Fields defineOutput();
    

	public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }
}
