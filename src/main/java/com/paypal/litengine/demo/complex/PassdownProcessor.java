package com.paypal.litengine.demo.complex;

import com.paypal.litengine.Tuple;
import com.paypal.litengine.engine.Fields;
import com.paypal.litengine.engine.TaskProcessor;
import com.paypal.litengine.engine.Values;

public class PassdownProcessor extends TaskProcessor {

	@Override
	public Values doProcess(Tuple input) {
	    
		System.out.println(this.getClass());
		System.out.println("input is:"+input);
		if (input.getValue(0) instanceof Values)
			return (Values) input.getValue(0);
		else
			return new Values(input.getValue(0));
	}

	@Override
	public Fields defineOutput() {
		// TODO Auto-generated method stub
		return new Fields("data");
	}

}
