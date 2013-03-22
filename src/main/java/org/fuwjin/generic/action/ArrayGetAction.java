package org.fuwjin.generic.action;

import java.lang.reflect.Array;

import org.fuwjin.generic.AbstractGenericAction;
import org.fuwjin.generic.GenericArray;
import org.fuwjin.generic.Generics;

public class ArrayGetAction extends AbstractGenericAction {
	public ArrayGetAction(GenericArray array) {
		super(array.getGenericComponentType(), array, Generics.INT);
	}

	@Override
	public Object valueImpl(Object... arguments) throws Exception {
		int index = ((Number)arguments[1]).intValue();
		return Array.get(arguments[0], index);
	}
}
