package com.paypal.litengine.demo.complex;

import com.paypal.litengine.Tuple;
import com.paypal.litengine.engine.Fields;
import com.paypal.litengine.engine.TaskProcessor;
import com.paypal.litengine.engine.Values;

public class AppendStringProcessor extends TaskProcessor {

	@Override
	public Values doProcess(Tuple input) {
	    try {
	        System.out.println("AppendStringProcessor Sleeping...");
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("AppendStringProcessor wakeup...");
		System.out.println("current processor:"+this.getClass());
		StringBuilder sb= new StringBuilder();
		Values values=(Values) input.getValueByField("source");
		System.out.println("AppendStringProcessor values size:"+values.size());
		for(Object obj: values){
			sb.append(obj).append(" ");
		}
		System.out.println("AppendStringProcessor  almost end");
		return new Values(sb.append("!").toString());
	}

	@Override
	public Fields defineOutput() {
		return new Fields("result");
	}

}
