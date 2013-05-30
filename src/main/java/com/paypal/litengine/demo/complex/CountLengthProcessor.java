package com.paypal.litengine.demo.complex;

import com.paypal.litengine.Tuple;
import com.paypal.litengine.engine.Fields;
import com.paypal.litengine.engine.TaskProcessor;
import com.paypal.litengine.engine.Values;

public class CountLengthProcessor extends TaskProcessor {

	@Override
	public Values doProcess(Tuple input) {
		System.out.println("input is:" + input);
		int len = 0;
		for (String str : input) {
			Values values = (Values) input.getValueByField(str);
			for (Object val : values) {
				len += ((String) val).length();
			}
		}
		System.out.print("total length is:" + len);
		return new Values(len);
	}

	@Override
	public Fields defineOutput() {
		return new Fields("length");
	}

}
