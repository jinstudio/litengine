package com.paypal.litengine;

import java.util.HashMap;
import java.util.Map;

import com.paypal.litengine.engine.Assemble;
import com.paypal.litengine.engine.SimpleEngine;
import com.paypal.litengine.engine.TopoContext;
import com.paypal.litengine.engine.TopologyEngine;

public class EngineFactory {
	
	private Map<Class,BaseEngine> mapping=new HashMap<Class,BaseEngine>();
	
	private EngineFactory(){
		mapping.put(Assemble.class, new SimpleEngine());
		mapping.put(TopoContext.class, new TopologyEngine());
	}

	public static EngineFactory getInstance(){
		return FactoryHolder.instance;
	}
	
	private static class FactoryHolder{
		static EngineFactory instance= new EngineFactory();
	}
	
	public BaseEngine<?> getEngine(BaseContext context){
		return mapping.get(context.getClass()).setContext(context);
	}
	
	public void registerEngine(Class<? extends BaseContext> cls, BaseEngine<BaseContext> engine){
		this.mapping.put(cls,engine);
	}
}
