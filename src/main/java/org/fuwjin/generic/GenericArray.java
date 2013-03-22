package org.fuwjin.generic;

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;

import org.fuwjin.generic.action.ArrayGetAction;
import org.fuwjin.generic.action.ArrayInstanceGetAction;
import org.fuwjin.generic.action.ArrayInstanceLengthAction;
import org.fuwjin.generic.action.ArrayInstanceSetAction;
import org.fuwjin.generic.action.ArrayLengthAction;
import org.fuwjin.generic.action.ArrayNewAction;
import org.fuwjin.generic.action.ArraySetAction;
import org.fuwjin.util.FilterSet;

public class GenericArray implements Generic {
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
	public FilterSet<GenericAction> actions() {
		FilterSet<GenericAction> actions = new FilterSet<GenericAction>();
		actions.add(new ArrayLengthAction(this));
		actions.add(new ArrayNewAction(this));
		actions.add(new ArraySetAction(this));
		actions.add(new ArrayGetAction(this));
		return actions;
	}
	
	@Override
	public GenericValue valueOf(final Object value) {
		if(!isInstance(value)){
			throw new IllegalArgumentException("Unexpected value: "+value);
		}
		return new AbstractGenericValue(this, value){
			@Override
			public FilterSet<GenericAction> actions() {
				FilterSet<GenericAction> actions = new FilterSet<GenericAction>();
				actions.add(new ArrayInstanceLengthAction(GenericArray.this, value));
				actions.add(new ArrayInstanceSetAction(GenericArray.this, value));
				actions.add(new ArrayInstanceGetAction(GenericArray.this, value));
				return actions;
			}
		};
	}
	
	@Override
	public boolean isInstance(Object object) {
		return getRawType().isInstance(object);
	}
	
	@Override
	public boolean contains(Generic type) {
		return type instanceof GenericArray && getGenericComponentType().contains(((GenericArray)type).getGenericComponentType());
	}

	@Override
	public boolean isAssignableTo(Generic type) {
		if(type instanceof GenericArgument){
			return type.contains(this);
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
