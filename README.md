This is a lite workflow engine
lite means it's easy to use, you feel the framework is lite, but the framework is powerful, <br>
for simple case you can use <a href="https://github.com/jinstudio/litengine/blob/master/src/main/java/com/paypal/litengine/engine/SimpleEngine.java">SimpleEngine</a>,
for complex case, you can use <a href="https://github.com/jinstudio/litengine/blob/master/src/main/java/com/paypal/litengine/engine/TopologyEngine.java">TopoEngine</a>.<br>
follow <a href="https://github.com/jinstudio/litengine/blob/master/src/main/java/com/paypal/litengine/demo/simple/SimpleDemo.java">SimpleDemo</a> to check how to play with simple engine,<br>
follow <a href="https://github.com/jinstudio/litengine/blob/master/src/main/java/com/paypal/litengine/demo/complex/WordsCountDemo.java">WordsCountDemo</a> to check how to play with complex engine,
and the WordsCount workflow like this:
group 2, group3 and group4 will running in parallel.<pre>
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

