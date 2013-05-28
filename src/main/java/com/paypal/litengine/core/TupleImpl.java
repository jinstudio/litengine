package com.paypal.litengine.core;

import java.util.Iterator;
import java.util.List;


public class TupleImpl implements Tuple {
	
	private List<Object> _values;
	
	private Fields _fields;
	
    public TupleImpl(Fields _fields,List<Object> _values) {
		super();
		this._values = _values;
		this._fields = _fields;
	}

	@Override
    public boolean contains(String field) {
        // TODO Auto-generated method stub
        return _fields.contains(field);
    }

    @Override
    public int fieldIndex(String field) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public Object getValue(int i) {
        // TODO Auto-generated method stub
        return _values.get(i);
    }

    @Override
    public Object getValueByField(String field) {
        // TODO Auto-generated method stub
        return _values.get(_fields.fieldIndex(field));
    }

    @Override
    public int size() {
        // TODO Auto-generated method stub
        return 0;
    }

	@Override
	public Iterator<String> iterator() {
		// TODO Auto-generated method stub
		return _fields.iterator();
	}

}
