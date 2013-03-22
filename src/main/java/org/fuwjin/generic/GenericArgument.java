package org.fuwjin.generic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.fuwjin.util.FilterSet;

public class GenericArgument implements Generic {
	private Generic[] upper;
	private Generic[] lower;
	private boolean raw;
	
	public GenericArgument(){
		// must call init
	}
	
	public GenericArgument(Generic[] upper, Generic[] lower){
		this.lower = lower;
		this.upper = upper;
	}
	
	@Override
	public Class<?> getRawType() {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public Generic supertype() {
		if(getRawType().isInterface()){
			return Generics.OBJECT;
		}
		return upper[0];
	}
	
	@Override
	public FilterSet<GenericAction> actions() {
		return new FilterSet<GenericAction>();
	}
	
	@Override
	public boolean isInstance(Object object) {
		for(Generic bound: upper){
			if(!bound.isInstance(object)){
				return false;
			}
		}
		//TODO handle lower bounds?
		return true;
	}

	@Override
	public Generic[] interfaces() {
		Generic[] ifaces = upper;
		if(getRawType().isInterface()){
			return ifaces;
		}
		return Arrays.copyOfRange(ifaces, 1, ifaces.length);
	}

	@Override
	public boolean isAssignableTo(Generic type) {
		if(raw){
			return true;
		}
		if(type instanceof GenericArgument){
			GenericArgument arg = (GenericArgument)type;
			for(Generic b: upper){
				if(!arg.contains(b)){
					return false;
				}
			}
			if(lower.length == 0){
				return arg.lower.length == 0;
			}
			for(Generic b: lower){
				if(!arg.contains(b)){
					return false;
				}
			}
			return true;
        }
		return false;
	}
	
	public GenericArgument init(Generic[] bounds, Generic actual) {
		if(actual == null){
			this.lower = Generics.NONE;
			this.upper = bounds;
			raw = true;
		}else if(actual instanceof GenericArgument){
			GenericArgument argument = (GenericArgument)actual;
			this.lower = argument.lower;
			List<Generic> uppers = new ArrayList<Generic>(Arrays.asList(argument.upper));
			bounds: for(Generic b: bounds){
				for(Generic u: uppers){
					if(u.isAssignableTo(b)){
						continue bounds;
					}
				}
				uppers.add(b);
			}
			this.upper = uppers.toArray(Generics.NONE);
			raw = argument.raw;
		}else{
			this.upper = new Generic[]{actual};
			this.lower = upper;
		}
		return this;
	}

	@Override
	public boolean contains(Generic type) {
		for(Generic b: upper){
			if(!type.isAssignableTo(b)){
				return false;
			}
		}
		for(Generic b: lower){
			if(!b.isAssignableTo(type)){
				return false;
			}
		}
		return true;
	}

	public Generic collapse() {
		if(upper.length == 1 && Arrays.equals(upper, lower)){
			return upper[0];
		}
		return this;
	}
	
	@Override
	public GenericValue valueOf(Object value) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("?");
		if(upper.length > 1 || !Object.class.equals(upper[0].getRawType())){
			String delim = " extends ";
			for(Generic b: upper){
				builder.append(delim).append(b);
				delim = " & ";
			}
		}
		if(lower.length > 0){
			String delim = " super ";
			for(Generic b: lower){
				builder.append(delim).append(b);
				delim = " & ";
			}
		}
		return builder.toString();
	}
}
