package org.fuwjin.generic;

import org.fuwjin.util.FilterSet;

public interface GenericValue {
	Object value();
	
	Generic type();
	
	FilterSet<GenericAction> actions();
}
