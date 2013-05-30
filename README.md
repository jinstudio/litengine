This is a lite workflow engine
lite means it's easy to use, you feel the framework is lite, but the framework is powerful, for simple case you can use SimpleEngine, for complex case, you can use TopoEngine
follow com.paypal.litengine.demo.simple.SimpleDemo to check how to play with simple engine
follow com.paypal.litengine.demo.complex.WordsCountDemo to check how to play with complex engine
the workflow like this:
group 2, group3 and group4 will running in parallel<pre>
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

