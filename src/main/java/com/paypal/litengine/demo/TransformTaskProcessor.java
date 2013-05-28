package com.paypal.litengine.demo;

import com.paypal.litengine.core.OutputFieldsDeclarer;
import com.paypal.litengine.core.TaskProcessor;
import com.paypal.litengine.core.Tuple;
import com.paypal.litengine.core.Values;

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
