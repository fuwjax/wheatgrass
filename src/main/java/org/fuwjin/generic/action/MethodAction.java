package org.fuwjin.generic.action;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.Arrays;

import org.fuwjin.generic.AbstractGenericAction;
import org.fuwjin.generic.Generic;
import org.fuwjin.generic.GenericType;

public class MethodAction extends AbstractGenericAction {
	private static Generic[] prepend(GenericType type, Generic[] args){
		Generic[] parameters = new Generic[args.length+1];
		parameters[0] = type;
		System.arraycopy(args, 0, parameters, 1, parameters.length - 1);
		return parameters;
	}
	
	private Method method;

	public MethodAction(GenericType type, Method method, Generic returnType, Generic[] parameters) {
		super(returnType, method.isVarArgs(), prepend(type, parameters));
		this.method = method;
	}

	@Override
	public Object valueImpl(Object... arguments) throws Exception {
		Object self = arguments[0];
		Object[] args = Arrays.copyOfRange(arguments, 1, arguments.length);
		return access(method).invoke(self, args);
	}

	@Override
	public AnnotatedElement element() {
		return method;
	}

}
