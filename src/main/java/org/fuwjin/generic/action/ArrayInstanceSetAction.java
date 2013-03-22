package org.fuwjin.generic.action;

import java.lang.reflect.Array;

import org.fuwjin.generic.AbstractGenericAction;
import org.fuwjin.generic.GenericArray;
import org.fuwjin.generic.Generics;

public class ArrayInstanceSetAction extends AbstractGenericAction {
	private Object value;

	public ArrayInstanceSetAction(GenericArray array, Object value) {
		super(array.getGenericComponentType(), Generics.INT, array.getGenericComponentType());
		this.value = value;
	}

	@Override
	public Object valueImpl(Object... arguments) throws Exception {
		int index = ((Number)arguments[0]).intValue();
		Object result = Array.get(value, index);
		Array.set(value, index, arguments[1]);
		return result;
	}
}
