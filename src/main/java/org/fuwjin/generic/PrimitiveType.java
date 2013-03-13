package org.fuwjin.generic;


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
}
