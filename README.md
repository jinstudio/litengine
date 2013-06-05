This is a lite workflow engine
lite means it's easy to use, you feel the framework is lite, but the framework is powerful, <br>
for simple case you can use <a href="https://github.com/jinstudio/litengine/blob/master/src/main/java/com/paypal/litengine/engine/SimpleEngine.java">SimpleEngine</a>,
for complex case, you can use <a href="https://github.com/jinstudio/litengine/blob/master/src/main/java/com/paypal/litengine/engine/TopologyEngine.java">TopoEngine</a>.<br>
follow <a href="https://github.com/jinstudio/litengine/blob/master/src/main/java/com/paypal/litengine/demo/simple/SimpleDemo.java">SimpleDemo</a> to check how to play with simple engine,<br>
follow <a href="https://github.com/jinstudio/litengine/blob/master/src/main/java/com/paypal/litengine/demo/complex/WordsCountDemo.java">WordsCountDemo</a> to check how to play with complex engine,
and the WordsCount workflow like this:
group 2, group3 and group4 will running in parallel.
<pre>
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
</pre>
the workflow is created using the streaming api, like this 
<pre>
		TopologyBuilder builder = new TopologyBuilder();
        builder.addTask("root", new Task().setProcessor(new GenerateDataProcessor()));
        builder.addTask("group1", new Task().setProcessor(new AppendStringProcessor())).wait("root");
        builder.addTask("group2", new Task().setProcessor(new PassdownProcessor())).wait("group1");
        builder.addTask("group3", new Task().setProcessor(new PassdownProcessor())).wait("group1");
        builder.addTask("group4", new Task().setProcessor(new PassdownWithSleepProcessor())).wait("group1");
        builder.addTask("group5", new Task().setProcessor(new CountLengthProcessor())).wait("group2").wait("group3").wait("group4");
</pre>                        
also, you can check <a href="https://github.com/jinstudio/litengine/blob/master/src/main/java/com/paypal/litengine/demo/complex/WordsCountDemoAnno.java">WordsCountDemoAnno</a> to create the previous workflow with annotation,then code of creating workflow will be like this:
<pre>
 Tuple output=engine.trigger(new TopoContext(new Config(){

			@SuppressWarnings("unchecked")
			@Override
			public List<Class<? extends TaskProcessor>> processors() {
				return Arrays.asList(
				GenerateDataProcessor.class,
				AppendStringProcessor.class,
				PassdownProcessor.class,
				PassdownWithSleepProcessor.class,
				CountLengthProcessor.class);
			}
        	
        }), null);
</pre>

you can check <a href="https://github.com/jinstudio/litengine/blob/master/src/main/java/com/paypal/litengine/demo/complex/WordsCountDemoWithMultipleDS.java">WordsCountDemoWithMultipleDS</a> to create the following style workflow,
 <pre>
                +-----------+  +-----------+ 
                |   root1   |  |  root2    |
                +----+------+  +----+------+
                         \        /
                          \      /
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
</pre>
you can check <a href="https://github.com/jinstudio/litengine/blob/master/src/main/java/com/paypal/litengine/demo/complex/WordsCountDemoWithMultipleDS2.java">WordsCountDemoWithMultipleDS2</a> to create the following one group with multiple tasks workflow,

<pre>
                     +--------------+                                       
                     | root(2 tasks)|                                    
                     +----+---------+                             
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
</pre>

also a more complex one <a href="https://github.com/jinstudio/litengine/blob/master/src/main/java/com/paypal/litengine/demo/complex/WordsCountDemoAnno2.java">WordsCountDemoAnno2</a>  which do not have a single end node
<pre>
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
</pre>

please contact with <a href="mailto:jstudio.yao@gmail.com?subject=[litengine]">Jin Yao</a> for comments/support/improvements or bugs
