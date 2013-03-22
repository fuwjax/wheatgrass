package org.fuwjin.generic.action;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;

import org.fuwjin.generic.AbstractGenericAction;
import org.fuwjin.generic.Generic;
import org.fuwjin.generic.GenericType;

public class InstanceFieldMutateAction extends AbstractGenericAction {
	private Field field;
	private Object value;

	public InstanceFieldMutateAction(GenericType type, Object value, Field field, Generic valueType) {
		super(valueType, valueType);
		this.value = value;
		this.field = field;
	}

	@Override
	public Object valueImpl(Object... arguments) throws Exception {
		Object result = field.get(value);
		field.set(value, arguments[0]);
		return result;
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
