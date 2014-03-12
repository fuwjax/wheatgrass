package org.fuwjin.generic;

import org.fuwjin.util.FilterSet;

public interface Generic {
	Class<?> getRawType();
	
	Generic[] arguments();
	
	Generic component();
	
	Generic box();
	
	Generic array();
	
	Generic supertype();
	
	Generic[] interfaces();
	
	boolean isAssignableTo(Generic type);
	
	boolean contains(Generic type);
	
	FilterSet<GenericAction> actions();
	
	GenericValue valueOf(Object value);
}
