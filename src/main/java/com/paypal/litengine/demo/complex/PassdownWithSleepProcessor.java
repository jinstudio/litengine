package com.paypal.litengine.demo.complex;

import com.paypal.litengine.Tuple;
import com.paypal.litengine.engine.Fields;
import com.paypal.litengine.engine.TaskProcessor;
import com.paypal.litengine.engine.Values;

public class PassdownWithSleepProcessor extends TaskProcessor {

	@Override
	public Values doProcess(Tuple input) {
		System.out.println("input is:"+input);
	    try {
	        System.out.println("PassdownWithSleepProcessor Sleeping...");
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("PassdownWithSleepProcessor wakeup...");
		System.out.println(this.getClass());
		if (input.getValue(0) instanceof Values)
			return (Values) input.getValue(0);
		else
			return new Values(input.getValue(0));
	}

	@Override
	public Fields defineOutput() {
		return new Fields("data");
	}

}
