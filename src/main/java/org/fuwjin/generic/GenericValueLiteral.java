package org.fuwjin.generic;

import org.fuwjin.util.FilterSet;

public abstract class GenericValueLiteral<T> implements GenericValue{
	private GenericValue value;
	
	public GenericValueLiteral(T value){
		GenericType literal = (GenericType)Generics.genericOf(getClass());
		while(!GenericValueLiteral.class.equals(literal.getRawType())){
			literal = (GenericType)literal.supertype();
		}
		Generic type = literal.getActualTypeArguments()[0];
		this.value = type.valueOf(value);
	}

	public Generic type(){
		return value.type();
	}

	@Override
	public FilterSet<GenericAction> actions() {
		return value.actions();
	}
	
	@Override
	public Object value() {
		return value.value();
	}
}
