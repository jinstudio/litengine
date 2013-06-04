package com.paypal.litengine.demo.complex;

import com.paypal.litengine.Tuple;
import com.paypal.litengine.engine.Fields;
import com.paypal.litengine.engine.TaskProcessor;
import com.paypal.litengine.engine.Values;
import com.paypal.litengine.topo.config.Group;
@Group(name="root")
public class GenerateDataProcessor extends TaskProcessor {

	@Override
	public Values doProcess(Tuple input) {
		 try {
		        logger.debug("GenerateDataProcessor Sleeping...");
	            Thread.sleep(1000);
	        } catch (InterruptedException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }
	        logger.debug("GenerateDataProcessor wakeup...");
		return new Values("paypal ebay","paypal jinstudio");
	}

	@Override
	public Fields defineOutput() {
		return new Fields("source1","source2");
	}

}
