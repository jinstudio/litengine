package com.paypal.litengine.demo.complex;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.paypal.litengine.Engine;
import com.paypal.litengine.Tuple;
import com.paypal.litengine.engine.TaskProcessor;
import com.paypal.litengine.engine.TopoContext;
import com.paypal.litengine.engine.TopologyEngine;
import com.paypal.litengine.topo.config.Config;
/**
 *  this demo create a workflow like this
 *  <pre>
                       +-----------+                                       
                       |   root    |                                    
                       +----+------+                             
                            |
                            |
                       +----+------+
                       |   group1  |
                       +-----+-----+
                      /      |       \
                     /       |        \
       +-----------+    +----+------+   +-----------+
       |   group2  |    |   group3  |   |   group4  |
       +-----------+    +-----------+   +-----------+
          /      \                            |
         /        \                           |
  +-----------+    +----+------+        +-----------+
  |   group5  |    |   group6  |        |   group7  |
  +-----------+    +-----------+        +-----------+ 
                             \           /
                              \         /
                             +-----------+                                       
                             |   group8  |                                    
                             +----+------+                 
 * </pre>
 * @author jyao1
 *
 */
public class WordsCountDemoAnno2 {
	
	static final Logger logger = LoggerFactory.getLogger(WordsCountDemoAnno2.class);
	
    /**
     * @param args
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        long start=System.currentTimeMillis();
        Engine<TopoContext> engine = new TopologyEngine();
        Tuple output=engine.trigger(new TopoContext(new Config(){

			@SuppressWarnings("unchecked")
			@Override
			public List<Class<? extends TaskProcessor>> processors() {
				return Arrays.asList(
				GenerateDataProcessor.class,
				AppendStringProcessor.class,
				PassdownProcessor2.class,
				PassdownProcessor2g5.class,
				PassdownWithSleepProcessor2.class);
			}
        	
        }), null,3400);
        logger.debug("##############################end##########################");
        if(output!=null)
        for(String o:output) 
        	logger.debug("final output is:"+o+":"+output.getValueByField(o));
        logger.debug("{}-{} time consumed:{}",Thread.currentThread().getName(),Thread.currentThread().getId(),System.currentTimeMillis()-start); 
    }

}
