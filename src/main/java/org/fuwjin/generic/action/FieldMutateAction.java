package org.fuwjin.generic.action;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;

import org.fuwjin.generic.AbstractGenericAction;
import org.fuwjin.generic.Generic;
import org.fuwjin.generic.GenericType;
import org.fuwjin.generic.Generics;

public class FieldMutateAction extends AbstractGenericAction {
	private Field field;

	public FieldMutateAction(GenericType type, Field field, Generic value) {
		super(Generics.VOID, type, value);
		this.field = field;
	}

	@Override
	public Object valueImpl(Object... arguments) throws Exception {
		access(field).set(arguments[0], arguments[1]);
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
