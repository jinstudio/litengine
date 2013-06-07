package com.paypal.litengine;

interface Lite<T,I,O> {
    
    public O trigger(T t,I input);
    
    public O trigger(I input);
     
    public O trigger(T t,I input,int timeout);
    
    public O trigger(I input,int timeout);
}
