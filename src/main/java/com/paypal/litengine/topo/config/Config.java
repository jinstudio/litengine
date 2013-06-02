package com.paypal.litengine.topo.config;

import java.util.List;

import com.paypal.litengine.engine.TaskProcessor;

public interface Config {
	
	List<Class<? extends TaskProcessor>> processors();

}
