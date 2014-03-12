package org.fuwjin.generic.action;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.fuwjin.generic.AbstractGenericAction;
import org.fuwjin.generic.Generic;
import org.fuwjin.generic.GenericType;

public class ConstructorAction extends AbstractGenericAction {
	private Constructor<?> cons;

	public ConstructorAction(GenericType type, Constructor<?> cons, Generic[] parameters) {
		super(type, cons.isVarArgs(), parameters);
		this.cons = cons;
	}

	@Override
	public Object valueImpl(Object... arguments) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		return access(cons).newInstance(arguments);
	}

	@Override
	public AnnotatedElement element() {
		return cons;
	}

}
