package com.paypal.litengine.topo.config;

import java.util.List;

import com.paypal.litengine.engine.TaskProcessor;

public abstract class AnnoConfig implements Config {
	
	public List<Class<? extends TaskProcessor>> processors(){
		return null;
	}

}
