package org.fuwjin.generic;

import org.fuwjin.util.FilterSet;

public interface Generic {
	Class<?> getRawType();
	
	Generic supertype();
	
	Generic[] interfaces();
	
	boolean isAssignableTo(Generic type);
	
	boolean contains(Generic type);
	
	boolean isInstance(Object object);
	
	FilterSet<GenericAction> actions();
	
	GenericValue valueOf(Object value);
}
