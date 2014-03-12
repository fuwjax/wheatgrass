package org.fuwjin.generic.action;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;

import org.fuwjin.generic.AbstractGenericAction;
import org.fuwjin.generic.Generic;
import org.fuwjin.generic.GenericType;

public class InstanceMethodAction extends AbstractGenericAction {
	private Method method;
	private Object value;

	public InstanceMethodAction(GenericType type, Object value, Method method, Generic returnType, Generic[] parameters) {
		super(returnType, method.isVarArgs(), parameters);
		this.value = value;
		this.method = method;
	}

	@Override
	public Object valueImpl(Object... arguments) throws Exception {
		return access(method).invoke(value, arguments);
	}

	@Override
	public AnnotatedElement element() {
		return method;
	}

}
