package com.paypal.litengine.demo.complex;

import com.paypal.litengine.Tuple;
import com.paypal.litengine.engine.Fields;
import com.paypal.litengine.engine.OutputFieldsDeclarer;
import com.paypal.litengine.engine.TaskProcessor;
import com.paypal.litengine.engine.Values;

public class GenerateDataProcessor extends TaskProcessor {

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("source"));
	}

	@Override
	public Values doProcess(Tuple input) {
		System.out.println(this.getClass());
		return new Values("paypal","ebay","hello","world!");
	}

}
