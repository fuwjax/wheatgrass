package org.fuwjin.generic;

import java.lang.reflect.Type;

public interface Generic extends Type{
	Class<?> getRawType();
	
	Generic supertype();
	
	Generic[] interfaces();
	
	boolean isAssignableTo(Generic type);
	
	@Override
	public boolean equals(Object obj);
}
