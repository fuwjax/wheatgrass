package org.fuwjin.wheatgrass.type;

import java.lang.reflect.Type;

public interface Generic<T> {
	Class<T> type();
	
	Type genericType();

	Generic<?>[] parameters();
	
	Generic<?> superclass();
	
	Generic<?>[] interfaces();
	
	boolean contains(Generic<?> key);
	
	boolean captures(Generic<?> key);

	Generic<T> convertFrom(Generic<?> source);
	
	String conversionId();
	
	T cast(Object instance);
}
