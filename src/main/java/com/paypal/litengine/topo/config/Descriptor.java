package com.paypal.litengine.topo.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.paypal.litengine.engine.TaskProcessor;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Descriptor {
	Class<? extends TaskProcessor>[] value();
}
