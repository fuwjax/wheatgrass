package org.fuwjin.generic;


public class WrapperType extends GenericType {
	private PrimitiveType primitive;

	public WrapperType(Class<?> cls, PrimitiveType primitive) {
		super(cls);
		this.primitive = primitive;
	}
	
	public PrimitiveType primitive(){
		return primitive;
	}

	@Override
	public boolean isAssignableTo(Generic type) {
		if(type instanceof PrimitiveType){
			return primitive.isAssignableTo(type);
		}
		return super.isAssignableTo(type);
	}
}
