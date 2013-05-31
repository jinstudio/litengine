package com.paypal.litengine.demo.complex;

import com.paypal.litengine.Tuple;
import com.paypal.litengine.engine.Fields;
import com.paypal.litengine.engine.TaskProcessor;
import com.paypal.litengine.engine.Values;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppendStringProcessor extends TaskProcessor {
	
	final Logger logger = LoggerFactory.getLogger(AppendStringProcessor.class);

	@Override
	public Values doProcess(Tuple input) {
	    try {
	        logger.debug("AppendStringProcessor Sleeping...");
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        logger.debug("AppendStringProcessor wakeup...");
		StringBuilder sb= new StringBuilder();
		Values values=(Values) input.getValueByField("source");
		logger.debug("AppendStringProcessor values size:"+values.size());
		for(Object obj: values){
			sb.append(obj).append(" ");
		}
		logger.debug("AppendStringProcessor  almost end");
		return new Values(sb.append("!").toString());
	}

	@Override
	public Fields defineOutput() {
		return new Fields("result");
	}

}
