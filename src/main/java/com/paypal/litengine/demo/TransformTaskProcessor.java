package com.paypal.litengine.demo;

import com.paypal.litengine.core.TaskProcessor;;

public class TransformTaskProcessor extends TaskProcessor {

    @Override
    public Object doProcess(Object input) {
        SetTransactionContextRequest request=(SetTransactionContextRequest) input;
        
        return new Object();
    }

}
