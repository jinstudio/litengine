package com.paypal.litengine.demo.complex;

import com.paypal.litengine.Tuple;
import com.paypal.litengine.engine.Fields;
import com.paypal.litengine.engine.TaskProcessor;
import com.paypal.litengine.engine.Values;

public class GenerateData2Processor extends TaskProcessor {

	@Override
	public Values doProcess(Tuple input) {
		return new Values("apple microsoft","intel ericsson");
	}

	@Override
	public Fields defineOutput() {
		return new Fields("source21","source22");
	}

}
