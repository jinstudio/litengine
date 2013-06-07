package com.paypal.litengine.demo.complex;

import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.paypal.litengine.Engine;
import com.paypal.litengine.Tuple;
import com.paypal.litengine.engine.Task;
import com.paypal.litengine.engine.TopoContext;
import com.paypal.litengine.engine.TopologyEngine;
import com.paypal.litengine.topo.Topology;
import com.paypal.litengine.topo.TopologyBuilder;
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
public class WordsCountDemo {
	
	static final Logger logger = LoggerFactory.getLogger(WordsCountDemo.class);
	
    /**
     * @param args
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        long start=System.currentTimeMillis();
        TopologyBuilder builder = new TopologyBuilder();
        builder.addTask("root", new Task().setProcessor(new GenerateDataProcessor()));
        builder.addTask("group1", new Task().setProcessor(new AppendStringProcessor())).wait("root");
        builder.addTask("group2", new Task().setProcessor(new PassdownProcessor())).wait("group1");
        builder.addTask("group3", new Task().setProcessor(new PassdownProcessor())).wait("group1");
        builder.addTask("group4", new Task().setProcessor(new PassdownWithSleepProcessor())).wait("group1");
        builder.addTask("group5", new Task().setProcessor(new CountLengthProcessor())).wait("group2").wait("group3").wait("group4");
        Topology topo = builder.createTopology();
        Engine<TopoContext> engine = new TopologyEngine();
        Tuple output=engine.trigger(new TopoContext(topo), null);
        logger.debug("##############################end##########################");
        for(String o:output) 
        	logger.debug(o+":"+output.getValueByField(o));
        logger.debug("{}-{} time consumed:{}",Thread.currentThread().getName(),Thread.currentThread().getId(),System.currentTimeMillis()-start);
        //you can execute the flow twice with the same result!
        /*output=engine.trigger(new TopoContext(topo), null,2000);
        logger.debug("##############################end##########################");
        for(String o:output) 
        	logger.debug(o+":"+output.getValueByField(o));*/
        
    }

}
