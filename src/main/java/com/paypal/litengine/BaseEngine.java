package com.paypal.litengine;

public abstract class BaseEngine<T extends Context> implements Engine<T>{

    @Override
    public void trigger(T context, Tuple input) {
        context.initTriggerSource(input);
        this.execute(context);
    }
    
    public abstract void execute(T context);

}
