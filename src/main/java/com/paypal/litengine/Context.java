package com.paypal.litengine;


public interface Context<I,O> {
 
     void initTriggerSource(I input);
     
     O getFinalOutput();
     
     Status getStatus();

     void setStatus(Status status);
}
