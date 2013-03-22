package org.fuwjin.generic.action;

import java.lang.reflect.Array;

import org.fuwjin.generic.AbstractGenericAction;
import org.fuwjin.generic.GenericArray;
import org.fuwjin.generic.Generics;

public class ArrayInstanceLengthAction extends AbstractGenericAction {
	private Object value;

	public ArrayInstanceLengthAction(GenericArray array, Object value) {
		super(Generics.INT);
		this.value = value;
	}

	@Override
	public Object valueImpl(Object... arguments) throws Exception {
		return Array.getLength(value);
	}
}
