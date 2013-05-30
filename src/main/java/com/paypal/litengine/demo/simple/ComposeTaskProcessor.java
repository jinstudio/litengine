package com.paypal.litengine.demo.simple;

import com.paypal.litengine.Tuple;
import com.paypal.litengine.engine.Fields;
import com.paypal.litengine.engine.TaskProcessor;
import com.paypal.litengine.engine.Values;

public class ComposeTaskProcessor extends TaskProcessor {

    @Override
    public Values doProcess(Tuple input) {
    	
      //  List<Object> outputs =( List<Object>)input.;
        for(String obj: input){
            System.out.println(obj+":"+input.getValueByField(obj));
        }
        System.out.println("Now i want to compose the result and return OmlSetTransactionContextRequest");
        return new Values(new OmlSetTransactionContextRequest());
    }

	@Override
	public Fields defineOutput() {
		return new Fields("omlRequest");
	}

}
