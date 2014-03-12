package org.fuwjin.generic.action;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;

import org.fuwjin.generic.AbstractGenericAction;
import org.fuwjin.generic.Generic;
import org.fuwjin.generic.GenericType;

public class StaticFieldAccessAction extends AbstractGenericAction {
	private Field field;

	public StaticFieldAccessAction(GenericType type, Field field, Generic value) {
		super(value);
		this.field = field;
	}

	@Override
	public Object valueImpl(Object... arguments) throws Exception {
		return access(field).get(null);
	}

	@Override
	public AnnotatedElement element() {
		return field;
	}

	@Override
	public String toString() {
		return field.getName();
	}
}
