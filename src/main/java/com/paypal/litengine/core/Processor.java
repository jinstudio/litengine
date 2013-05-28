package com.paypal.litengine.core;

public interface Processor {
    
    public void process();
    
    public void declareOutputFields(OutputFieldsDeclarer declarer);

}
