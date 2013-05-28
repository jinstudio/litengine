package com.paypal.litengine.demo;

import java.util.List;

import com.paypal.litengine.core.Fields;
import com.paypal.litengine.core.OutputFieldsDeclarer;
import com.paypal.litengine.core.TaskProcessor;
import com.paypal.litengine.core.Tuple;
import com.paypal.litengine.core.Values;

public class ComposeTaskProcessor extends TaskProcessor {

    @Override
    public Values doProcess(Tuple input) {
        // TODO Auto-generated method stub
    	
      //  List<Object> outputs =( List<Object>)input.;
        for(String obj: input){
            System.out.println(obj+":"+input.getValueByField(obj));
        }
        System.out.println("Now i want to compose the result and return OmlSetTransactionContextRequest");
        return new Values(new OmlSetTransactionContextRequest());
    }

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		// TODO Auto-generated method stub
		declarer.declare(new Fields("omlRequest"));
	}

}
