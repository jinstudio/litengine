package com.paypal.litengine.exception;

public class DuplicateFieldException extends IllegalArgumentException {
	
	private static final long serialVersionUID = 1L;
	
	private String field;
	
	public DuplicateFieldException(String field) {
		super(String.format("duplicate field '%s'", field));
		this.field = field;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}
	

}
