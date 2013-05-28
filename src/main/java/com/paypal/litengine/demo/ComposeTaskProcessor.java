package com.paypal.litengine.demo;

import java.util.List;

import com.paypal.litengine.core.TaskProcessor;;

public class ComposeTaskProcessor extends TaskProcessor {

    @Override
    public Object doProcess(Object input) {
        // TODO Auto-generated method stub
        List<Object> outputs =( List<Object>)input;
        for(Object obj: outputs){
            System.out.println(obj.getClass().getName());
        }
        System.out.println("Now i want to compose the result and return OmlSetTransactionContextRequest");
        return new OmlSetTransactionContextRequest();
    }

}
