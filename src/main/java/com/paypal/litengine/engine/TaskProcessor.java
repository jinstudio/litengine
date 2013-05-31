package com.paypal.litengine.engine;

import com.paypal.litengine.Processor;
import com.paypal.litengine.Tuple;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class TaskProcessor implements Processor {

	protected final Logger logger = LoggerFactory.getLogger(TaskProcessor.class);
    private Task task;
    
    @Override
    public void process() {
    	logger.debug("start to process "+this.getClass().getName());
        task.setOutput( doProcess(task.getInput()));
        logger.debug("end to process "+this.getClass().getName());
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
