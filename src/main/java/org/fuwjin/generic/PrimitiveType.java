package org.fuwjin.generic;

import org.fuwjin.util.FilterSet;


public class PrimitiveType implements Generic{
	private Class<?> cls;
	private WrapperType wrapper;
	private Generic supertype;

	public PrimitiveType(Class<?> cls) {
		this.cls = cls;
	}

	@Override
	public Class<?> getRawType() {
		return cls;
	}
	
	public WrapperType wrapper(){
		return wrapper;
	}

	@Override
	public Generic supertype() {
		return supertype;
	}

	@Override
	public Generic[] interfaces() {
		return Generics.NONE;
	}

	@Override
	public boolean isAssignableTo(Generic type) {
		if(this.equals(type)){
			return true;
		}
		if(type instanceof PrimitiveType){
            return supertype.isAssignableTo(type);
		}else{
			return wrapper.isAssignableTo(type);
		}
	}
	
	@Override
	public boolean isInstance(Object object) {
		return wrapper.isInstance(object);
	}
	
	@Override
	public boolean contains(Generic type) {
		return type instanceof PrimitiveType && getRawType().equals(type.getRawType());
	}
	
	@Override
	public FilterSet<GenericAction> actions() {
		return wrapper.actions();
	}

	void setWrapper(WrapperType wrapper) {
		this.wrapper = wrapper;
	}

	void setSuper(Generic supertype) {
		this.supertype = supertype;
	}
	
	@Override
	public String toString() {
		return cls.getCanonicalName();
	}
	
	@Override
	public GenericValue valueOf(final Object value) {
		if(!isInstance(value)){
			if(!void.class.equals(cls) || value != null){
				throw new IllegalArgumentException("Unexpected value: "+value);
			}
		}
		return new AbstractGenericValue(this, value){
			@Override
			public FilterSet<GenericAction> actions() {
				return wrapper.valueOf(value).actions();
			}
		};
	}
}
