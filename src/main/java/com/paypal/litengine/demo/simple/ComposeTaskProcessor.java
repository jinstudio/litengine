package com.paypal.litengine.demo.simple;

import java.util.List;

import com.paypal.litengine.engine.Fields;
import com.paypal.litengine.engine.OutputFieldsDeclarer;
import com.paypal.litengine.engine.TaskProcessor;
import com.paypal.litengine.engine.Tuple;
import com.paypal.litengine.engine.Values;

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
