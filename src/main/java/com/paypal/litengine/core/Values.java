package com.paypal.litengine.core;

import java.util.ArrayList;

@SuppressWarnings("serial")
public class Values extends ArrayList<Object>{
    public Values() {
        
    }
    
    public Values(Object... vals) {
        super(vals.length);
        for(Object o: vals) {
            add(o);
        }
    }
}
