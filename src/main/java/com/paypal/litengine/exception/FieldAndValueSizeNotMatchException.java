package com.paypal.litengine.exception;

import com.paypal.litengine.engine.Fields;
import com.paypal.litengine.engine.Values;

public class FieldAndValueSizeNotMatchException extends IllegalArgumentException {
	
	private static final long serialVersionUID = 1L;
	
	
	public FieldAndValueSizeNotMatchException(Fields fields, Values values) {
		super(String.format("fileds size is '%s%', but values size is '%s'", fields.size(),values.size()));
	}


}
