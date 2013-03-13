package org.fuwjin.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public class AnnotationBuilder<A extends Annotation> {
	private Class<A> type;
	private Map<String, Object> values;

	public AnnotationBuilder(Class<A> type) {
		this.type = type;
	}

	public AnnotationBuilder<A> with(String name, Object value) {
		if(values == null){
			values = new HashMap<String,Object>();
		}
		values.put(name, value);
		return this;
	}
	
	public A build(){
		InvocationHandler handler = new AnnotationHandler(type, values == null ? Collections.<String,Object>emptyMap(): values);
		values = null;
		return Types.proxy(type, handler);
	}
}
