package org.fuwjin.generic;

import java.lang.reflect.AnnotatedElement;

public interface GenericAction {
	public GenericValue value(GenericValue... arguments) throws Exception;

	public GenericAction call(Generic... parameters) throws Exception;
	
	public Generic type();
	
	public Generic[] parameters();
	
	public AnnotatedElement element();
}
