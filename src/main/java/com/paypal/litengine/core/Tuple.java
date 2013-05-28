package com.paypal.litengine.core;

public interface Tuple extends Iterable<String>{
    /**
     * Returns the number of fields in this tuple.
     */
    public int size();
    
    /**
     * Returns the position of the specified field in this tuple.
     */
    public int fieldIndex(String field);
    
    /**
     * Returns true if this tuple contains the specified name of the field.
     */
    public boolean contains(String field);
    
    /**
     * Gets the field at position i in the tuple. Returns object since tuples are dynamically typed.
     */
    public Object getValue(int i);
    
    public Object getValueByField(String field);

}
