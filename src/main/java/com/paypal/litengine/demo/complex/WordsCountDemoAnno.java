package com.paypal.litengine.demo.complex;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.paypal.litengine.Engine;
import com.paypal.litengine.Tuple;
import com.paypal.litengine.engine.TaskProcessor;
import com.paypal.litengine.engine.TopoContext;
import com.paypal.litengine.engine.TopologyEngine;
import com.paypal.litengine.topo.config.AnnoConfig;
import com.paypal.litengine.topo.config.Config;
import com.paypal.litengine.topo.config.Descriptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
                     \        |        /
                      \       |       /
                        +----+------+
                        |   group5  |
                        +-----+-----+
 * </pre>
 * @author jyao1
 *
 */
public class WordsCountDemoAnno {
	
	static final Logger logger = LoggerFactory.getLogger(WordsCountDemoAnno.class);
	
    /**
     * @param args
     * @throws ExecutionException
     * @throws InterruptedException
     */
	
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        
        Engine<TopoContext> engine = new TopologyEngine();
        Tuple output=engine.trigger(new TopoContext(
        		new DemoConfig()), null);
        logger.info("##############################end##########################");
        for(String o:output) 
        	logger.info(o+":"+output.getValueByField(o));
    }
    
}

