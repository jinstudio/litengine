package com.paypal.litengine.demo.complex;

import com.paypal.litengine.Tuple;
import com.paypal.litengine.engine.Fields;
import com.paypal.litengine.engine.OutputFieldsDeclarer;
import com.paypal.litengine.engine.TaskProcessor;
import com.paypal.litengine.engine.Values;

public class CountLengthProcessor extends TaskProcessor {

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("data"));
	}

	@Override
	public Values doProcess(Tuple input) {
		int len=0;
		Values values=(Values)input.getValue(0);
		for(Object str:values){
		   len+=((String)str).length();
		}
		 System.out.print("total length is:"+len);
		return new Values(len);
	}

}
