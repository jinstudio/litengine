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
		//int valueSize=_values.size();
		//int filedSize=_fields.size();
		//if(this.values.size())
		this.values = _values;
		this.fields = _fields;
	}

	@Override
    public boolean contains(String field) {
        return fields.contains(field);
    }

    @Override
    public int fieldIndex(String field) {
        return 0;
    }

    @Override
    public Object getValue(int i) {
        return values.get(i);
    }

    @Override
    public Object getValueByField(String field) {
        return values.get(fields.fieldIndex(field));
    }

    @Override
    public int size() {
        return 0;
    }

	@Override
	public Iterator<String> iterator() {
		return fields.iterator();
	}

	@Override
	public void add(Tuple tuple) {
		for(String str: tuple){
			fields.add(str);
	        values.add(tuple.getValueByField(str));
		}
           
	}

	@Override
	public String toString() {
		StringBuilder sb= new StringBuilder();
		for(String str: this)
			sb.append(str).append("=").append(this.getValueByField(str)).append(";");
		return sb.toString();
	}

	@Override
	/**
	 * add with prefix, for internal handling input, the prefix will be auto added in case of conflicting
	 */
	public void add(Tuple tuple, String prefix) {
		
		for(String str: tuple){
			if(fields.contains(str))
				fields.add(prefix+str);
			else
				fields.add(str);
	        values.add(tuple.getValueByField(str));
		}
		
	}
	
	

}
