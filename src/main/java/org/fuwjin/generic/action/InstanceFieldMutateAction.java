package org.fuwjin.generic.action;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;

import org.fuwjin.generic.AbstractGenericAction;
import org.fuwjin.generic.Generic;
import org.fuwjin.generic.GenericType;
import org.fuwjin.generic.Generics;

public class InstanceFieldMutateAction extends AbstractGenericAction {
	private Field field;
	private Object value;

	public InstanceFieldMutateAction(GenericType type, Object value, Field field, Generic valueType) {
		super(Generics.VOID, valueType);
		this.value = value;
		this.field = field;
	}

	@Override
	public Object valueImpl(Object... arguments) throws Exception {
		access(field).set(value, arguments[0]);
		return null;
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
