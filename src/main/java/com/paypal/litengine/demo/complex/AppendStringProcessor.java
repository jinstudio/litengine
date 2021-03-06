package com.paypal.litengine.demo.complex;

import com.paypal.litengine.Tuple;
import com.paypal.litengine.engine.Fields;
import com.paypal.litengine.engine.TaskProcessor;
import com.paypal.litengine.engine.Values;
import com.paypal.litengine.topo.config.Group;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Group(name="group1", waits = { "root" })
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
		
		logger.debug("AppendStringProcessor values size:"+input.size());
		String value1=(String) input.getValueByField("source1");
		String value2=(String) input.getValueByField("source2");
		sb.append(value1).append(" ").append(value2);
		logger.debug("AppendStringProcessor  almost end");
		return new Values(sb.append("!").toString());
	}

	@Override
	public Fields defineOutput() {
		return new Fields("result");
	}

}
