package com.paypal.litengine.demo.simple;

import com.paypal.litengine.engine.Fields;
import com.paypal.litengine.engine.OutputFieldsDeclarer;
import com.paypal.litengine.engine.TaskProcessor;
import com.paypal.litengine.engine.Tuple;
import com.paypal.litengine.engine.Values;

public class AddressNormTaskProcessor2 extends TaskProcessor {

    @Override
    public Values doProcess(Tuple input) {
        // TODO Auto-generated method stub
        SetTransactionContextRequest request=(SetTransactionContextRequest) input.getValue(0);
        System.out.println("do address normalizations");
        request.setAddress2("normizalied address");
        return new Values(request);
    }

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("address2"));
	}

}
