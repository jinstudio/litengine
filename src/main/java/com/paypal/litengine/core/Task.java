package com.paypal.litengine.core;

public class Task {

    private TaskEnum type;

    private Object input;

    private Object output;

    private TaskProcessor processor = new SpareTaskProcessor();

    public TaskEnum getType() {
        return type;
    }

    public void setType(TaskEnum type) {
        this.type = type;
    }

    public Object getInput() {
        return input;
    }

    public Task setInput(Object input) {
        if(this.input == null)
            this.input = input;
        return this;
    }

    public Object getOutput() {
        return output;
    }

    public void setOutput(Object output) {
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
