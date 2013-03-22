package org.fuwjin.generic;

import org.fuwjin.util.FilterSet;

public abstract class GenericTypeLiteral<T> implements Generic{
	private GenericType type;

	public GenericType type(){
		if(type == null){
			GenericType literal = (GenericType)Generics.genericOf(getClass());
			while(!GenericTypeLiteral.class.equals(literal.getRawType())){
				literal = (GenericType)literal.supertype();
			}
			type = (GenericType)literal.getActualTypeArguments()[0];
		}
		return type;
	}

	@Override
	public Class<?> getRawType() {
		return type().getRawType();
	}

	@Override
	public Generic supertype() {
		return type().supertype();
	}

	@Override
	public Generic[] interfaces() {
		return type().interfaces();
	}

	@Override
	public boolean isAssignableTo(Generic type) {
		return type().isAssignableTo(type);
	}
	
	@Override
	public boolean contains(Generic type) {
		return type().contains(type);
	}

	@Override
	public boolean isInstance(Object object) {
		return type().isInstance(object);
	}

	@Override
	public FilterSet<GenericAction> actions() {
		return type().actions();
	}

	@Override
	public GenericValue valueOf(Object value) {
		return type().valueOf(value);
	}
}
