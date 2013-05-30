package com.paypal.litengine.demo.simple;

import com.paypal.litengine.Tuple;
import com.paypal.litengine.engine.OutputFieldsDeclarer;
import com.paypal.litengine.engine.TaskProcessor;
import com.paypal.litengine.engine.Values;

public class TransformTaskProcessor extends TaskProcessor {

    @Override
    public Values doProcess(Tuple input) {
        SetTransactionContextRequest request=(SetTransactionContextRequest) input.getValue(0);
        
        return new Values(new Object());
    }

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		// TODO Auto-generated method stub
		
	}

}
