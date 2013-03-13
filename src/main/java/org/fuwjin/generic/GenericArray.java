package org.fuwjin.generic;

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;

public class GenericArray implements Generic, GenericArrayType {
	private Generic component;
	private Generic supertype;
	private Generic[] interfaces;
	private Class<?> c;

	public GenericArray(Generic component){
		this.component = component;
		this.interfaces = Generics.NONE;
	}
	
	public GenericArray(Generic component, Generic supertype, Generic... interfaces){
		this.component = component;
		this.supertype = supertype;
		this.interfaces = interfaces;
	}
	
	@Override
	public Class<?> getRawType() {
		if(c == null){
			c = Array.newInstance(component.getRawType(), 0).getClass();
		}
		return c;
	}

	@Override
	public Generic supertype() {
		if(supertype == null){
			supertype = Generics.arrayOf(component.supertype());
		}
		return supertype;
	}
	
	@Override
	public Generic[] interfaces() {
		return interfaces;
	}

	@Override
	public boolean isAssignableTo(Generic type) {
		if(type instanceof GenericArgument){
			return ((GenericArgument)type).contains(this);
		}
		if(type instanceof GenericArray){
			GenericArray arr = (GenericArray)type;
			Generic comp = arr.getGenericComponentType();
			if(component instanceof PrimitiveType || comp instanceof PrimitiveType){
				return component.equals(comp);
			}
            return component.isAssignableTo(comp);
        }
		if(supertype().isAssignableTo(type)){
			return true;
		}
		for(Generic iface: interfaces()){
			if(iface.isAssignableTo(type)){
				return true;
			}
		}
		return false;
	}

	@Override
	public Generic getGenericComponentType() {
		return component;
	}

	@Override
	public String toString() {
		return component+"[]";
	}
	
	@Override
	public boolean equals(Object obj) {
		try{
			GenericArrayType o = (GenericArrayType)obj;
			return getGenericComponentType().equals(o.getGenericComponentType());
		}catch(Exception e){
			return false;
		}
	}
}
