package org.fuwjin.generic.action;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;

import org.fuwjin.generic.AbstractGenericAction;
import org.fuwjin.generic.Generic;
import org.fuwjin.generic.GenericType;

public class FieldAccessAction extends AbstractGenericAction {
	private Field field;

	public FieldAccessAction(GenericType type, Field field, Generic value) {
		super(value, type);
		this.field = field;
	}

	@Override
	public Object valueImpl(Object... arguments) throws Exception {
		return access(field).get(arguments[0]);
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
