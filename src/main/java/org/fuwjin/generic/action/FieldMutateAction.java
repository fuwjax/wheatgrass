package org.fuwjin.generic.action;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;

import org.fuwjin.generic.AbstractGenericAction;
import org.fuwjin.generic.Generic;
import org.fuwjin.generic.GenericType;

public class FieldMutateAction extends AbstractGenericAction {
	private Field field;

	public FieldMutateAction(GenericType type, Field field, Generic value) {
		super(value, type, value);
		this.field = field;
	}

	@Override
	public Object valueImpl(Object... arguments) throws Exception {
		Object value = field.get(arguments[0]);
		field.set(arguments[0], arguments[1]);
		return value;
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
