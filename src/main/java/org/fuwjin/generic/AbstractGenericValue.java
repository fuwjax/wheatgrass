package org.fuwjin.generic;


public abstract class AbstractGenericValue implements GenericValue {
	private Generic type;
	private Object value;

	public AbstractGenericValue(Generic type, Object value){
		this.type = type;
		this.value = value;
	}

	@Override
	public Object value() {
		return value;
	}

	@Override
	public Generic type() {
		return type;
	}
}
