package org.fuwjin.generic.action;

import java.lang.reflect.Array;

import org.fuwjin.generic.AbstractGenericAction;
import org.fuwjin.generic.GenericArray;
import org.fuwjin.generic.Generics;

public class ArraySetAction extends AbstractGenericAction {

	public ArraySetAction(GenericArray array) {
		super(array.getGenericComponentType(), array, Generics.INT, array.getGenericComponentType());
	}

	@Override
	public Object valueImpl(Object... arguments) throws Exception {
		int index = ((Number)arguments[1]).intValue();
		Object value = Array.get(arguments[0], index);
		Array.set(arguments[0], index, arguments[2]);
		return value;
	}
}
