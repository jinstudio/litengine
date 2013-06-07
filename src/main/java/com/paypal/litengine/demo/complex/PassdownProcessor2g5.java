package com.paypal.litengine.demo.complex;

import com.paypal.litengine.Tuple;
import com.paypal.litengine.engine.Fields;
import com.paypal.litengine.engine.TaskProcessor;
import com.paypal.litengine.engine.Values;
import com.paypal.litengine.topo.config.Group;

@Group(name="group5", waits={"group2"})
public class PassdownProcessor2g5 extends TaskProcessor {

	@Override
	public Values doProcess(Tuple input) {
	    logger.debug("********:"+input);
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
