package com.paypal.litengine;

public abstract class BaseEngine<T extends Context<Tuple, Tuple>> implements Engine<T> {

    @Override
    public Tuple trigger(T context, Tuple input) {
        context.initTriggerSource(input);
        this.execute(context);
        return context.getFinalOutput();
    }
    
    public abstract void execute(T context);

}
