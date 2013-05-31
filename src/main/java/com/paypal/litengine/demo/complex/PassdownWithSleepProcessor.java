package com.paypal.litengine.demo.complex;

import com.paypal.litengine.Tuple;
import com.paypal.litengine.engine.Fields;
import com.paypal.litengine.engine.TaskProcessor;
import com.paypal.litengine.engine.Values;

public class PassdownWithSleepProcessor extends TaskProcessor {

	@Override
	public Values doProcess(Tuple input) {
		logger.debug("input is:"+input);
	    try {
	        logger.debug("PassdownWithSleepProcessor Sleeping...");
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        logger.debug("PassdownWithSleepProcessor wakeup...");
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
