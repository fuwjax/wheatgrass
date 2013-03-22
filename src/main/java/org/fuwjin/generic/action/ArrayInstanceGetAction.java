package org.fuwjin.generic.action;

import java.lang.reflect.Array;

import org.fuwjin.generic.AbstractGenericAction;
import org.fuwjin.generic.GenericArray;
import org.fuwjin.generic.Generics;

public class ArrayInstanceGetAction extends AbstractGenericAction {
	private Object value;

	public ArrayInstanceGetAction(GenericArray array, Object value) {
		super(array.getGenericComponentType(), Generics.INT);
		this.value = value;
	}

	@Override
	public Object valueImpl(Object... arguments) throws Exception {
		int index = ((Number)arguments[0]).intValue();
		return Array.get(value, index);
	}
}
