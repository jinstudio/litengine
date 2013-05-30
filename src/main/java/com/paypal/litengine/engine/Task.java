package com.paypal.litengine.engine;

import com.paypal.litengine.Processor;
import com.paypal.litengine.Tuple;

public class Task {

    private TaskEnum type;

    private Tuple input;

    private Values output;

    private TaskProcessor processor = new SpareTaskProcessor();

    public TaskEnum getType() {
        return type;
    }

    public void setType(TaskEnum type) {
        this.type = type;
    }

    public Tuple getInput() {
        return input;
    }

    public Task setInput(Tuple input) {
        if(this.input == null)
            this.input = input;
        return this;
    }
    
    public Task setInput(Object input) {
        if(this.input == null)
            this.input = new TupleImpl(Fields.DEFAULT,new Values(input));
        return this;
    }

    public Values getOutput() {
        return output;
    }

    public void setOutput(Values output) {
        this.output = output;
    }

    public Processor getProcessor() {
        return processor;
    }

    public Task setProcessor(TaskProcessor processor) {
        this.processor = processor;
        processor.setTask(this);
        return this;
    }
    
}
