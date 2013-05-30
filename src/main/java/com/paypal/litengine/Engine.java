package com.paypal.litengine;

public interface Engine<T> {
    
    public void trigger(T t,Tuple input);
}
