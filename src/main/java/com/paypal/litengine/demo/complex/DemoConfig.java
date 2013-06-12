package com.paypal.litengine.demo.complex;

import com.paypal.litengine.topo.config.AnnoConfig;
import com.paypal.litengine.topo.config.Descriptor;
@Descriptor({
	GenerateDataProcessor.class,
	AppendStringProcessor.class,
	PassdownProcessor2.class, 
	PassdownWithSleepProcessor.class,  
	CountLengthProcessor.class
})  
public class DemoConfig extends AnnoConfig{
	 
}
