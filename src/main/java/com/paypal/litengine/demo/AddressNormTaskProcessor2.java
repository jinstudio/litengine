package com.paypal.litengine.demo;

import com.paypal.litengine.core.TaskProcessor;

public class AddressNormTaskProcessor2 extends TaskProcessor {

    @Override
    public Object doProcess(Object input) {
        // TODO Auto-generated method stub
        SetTransactionContextRequest request=(SetTransactionContextRequest) input;
        System.out.println("do address normalizations");
        request.setAddress2("normizalied address");
        return request;
    }

}
