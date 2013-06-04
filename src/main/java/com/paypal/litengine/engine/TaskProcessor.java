package com.paypal.litengine.engine;

import com.paypal.litengine.Processor;
import com.paypal.litengine.Tuple;
import com.paypal.litengine.exception.FieldAndValueSizeNotMatchException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class TaskProcessor implements Processor {

	protected final Logger logger = LoggerFactory.getLogger(TaskProcessor.class);
    private Task task;
    
    @Override
    public void process() {
    	logger.debug("start to process "+this.getClass().getName());
    	Values values=doProcess(task.getInput());
    	Fields definedOutput = this.defineOutput();
		if(values!=null&&definedOutput==null){
    		throw new IllegalArgumentException(String.format("'%s': declared fields is null but values is not null",this.getClass().getCanonicalName()));
    	}
    	if(values==null&&definedOutput==null){
    		return;
    	}
    	if(values!=null&&values.size()!=definedOutput.size()){
    		throw new FieldAndValueSizeNotMatchException(definedOutput, values);
    	}
        task.setOutput(values);
        logger.debug("end to process "+this.getClass().getName());
    }
    
    @Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
    	Fields fields=this.defineOutput();
    	//skip append output fields if not declaring output
    	if(fields==null)
    		return;
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
