package com.paypal.litengine;

public interface Context<I,O> {
 
     void initTriggerSource(I input);
     
     O getFinalOutput();
}
