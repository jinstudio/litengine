package com.paypal.litengine.engine;

public class OutputFieldsDeclarerImpl implements OutputFieldsDeclarer {

	private Fields _fields;

	@Override
	public void declare(Fields fields) {
		if (_fields == null)
			_fields = fields;
		else
			_fields.add(fields);
	}

	@Override
	public Fields getFieldsDeclaration() {
		// TODO Auto-generated method stub
		return _fields;
	}

}
