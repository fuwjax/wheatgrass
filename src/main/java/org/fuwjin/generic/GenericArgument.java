package org.fuwjin.generic;

import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GenericArgument implements Generic, WildcardType {
	private Generic[] upper;
	private Generic[] lower;
	
	public GenericArgument(){
		// must call init
	}
	
	public GenericArgument(Generic[] upper, Generic[] lower){
		this.lower = lower;
		this.upper = upper;
	}
	
	@Override
	public Generic[] getUpperBounds() {
		return upper;
	}

	@Override
	public Generic[] getLowerBounds() {
		return lower;
	}

	@Override
	public Class<?> getRawType() {
		return getUpperBounds()[0].getRawType();
	}

	@Override
	public Generic supertype() {
		if(getRawType().isInterface()){
			return Generics.OBJECT;
		}
		return getUpperBounds()[0];
	}

	@Override
	public Generic[] interfaces() {
		Generic[] ifaces = getUpperBounds();
		if(getRawType().isInterface()){
			return ifaces;
		}
		return Arrays.copyOfRange(ifaces, 1, ifaces.length);
	}

	@Override
	public boolean isAssignableTo(Generic type) {
		if(type instanceof GenericArgument){
			GenericArgument arg = (GenericArgument)type;
			for(Generic b: getUpperBounds()){
				if(!arg.contains(b)){
					return false;
				}
			}
			if(getLowerBounds().length == 0){
				return arg.getLowerBounds().length == 0;
			}
			for(Generic b: getLowerBounds()){
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
		}else if(actual instanceof GenericArgument){
			GenericArgument argument = (GenericArgument)actual;
			this.lower = argument.getLowerBounds();
			List<Generic> uppers = new ArrayList<Generic>(Arrays.asList(argument.getUpperBounds()));
			bounds: for(Generic b: bounds){
				for(Generic u: uppers){
					if(u.isAssignableTo(b)){
						continue bounds;
					}
				}
				uppers.add(b);
			}
			this.upper = uppers.toArray(Generics.NONE);
		}else{
			this.upper = new Generic[]{actual};
			this.lower = upper;
		}
		return this;
	}

	public boolean contains(Generic type) {
		for(Generic b: getUpperBounds()){
			if(!type.isAssignableTo(b)){
				return false;
			}
		}
		for(Generic b: getLowerBounds()){
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
}
