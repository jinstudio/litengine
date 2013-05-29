package com.paypal.litengine.demo.complex;

import com.paypal.litengine.engine.Fields;
import com.paypal.litengine.engine.OutputFieldsDeclarer;
import com.paypal.litengine.engine.TaskProcessor;
import com.paypal.litengine.engine.Tuple;
import com.paypal.litengine.engine.Values;

public class AppendStringProcessor extends TaskProcessor {

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("result"));
	}

	@Override
	public Values doProcess(Tuple input) {
		System.out.println(this.getClass());
		StringBuilder sb= new StringBuilder();
		Values values=(Values) input.getValueByField("source");
		for(Object obj: values){
			sb.append(obj).append(" ");
		}
		return new Values(sb.append("!"));
	}

}
