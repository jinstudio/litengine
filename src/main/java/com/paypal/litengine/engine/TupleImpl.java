package com.paypal.litengine.engine;

import java.util.Iterator;
import java.util.List;

import com.paypal.litengine.Tuple;


public class TupleImpl implements Tuple {
	
	private List<Object> values;
	
	private Fields fields;
	
	public TupleImpl(Fields _fields) {
		super();
		this.fields = _fields;
	}
	
    public TupleImpl(Fields _fields,List<Object> _values) {
		super();
		this.values = _values;
		this.fields = _fields;
	}

	@Override
    public boolean contains(String field) {
        // TODO Auto-generated method stub
        return fields.contains(field);
    }

    @Override
    public int fieldIndex(String field) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public Object getValue(int i) {
        // TODO Auto-generated method stub
        return values.get(i);
    }

    @Override
    public Object getValueByField(String field) {
        // TODO Auto-generated method stub
        return values.get(fields.fieldIndex(field));
    }

    @Override
    public int size() {
        // TODO Auto-generated method stub
        return 0;
    }

	@Override
	public Iterator<String> iterator() {
		// TODO Auto-generated method stub
		return fields.iterator();
	}

}
