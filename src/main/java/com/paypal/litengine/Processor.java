package com.paypal.litengine;

import com.paypal.litengine.engine.OutputFieldsDeclarer;

public interface Processor {
    
    public void process();
    
    public void declareOutputFields(OutputFieldsDeclarer declarer);

}
