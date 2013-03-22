package org.fuwjin.generic.action;

import java.lang.reflect.Array;

import org.fuwjin.generic.AbstractGenericAction;
import org.fuwjin.generic.GenericArray;
import org.fuwjin.generic.Generics;

public class ArrayNewAction extends AbstractGenericAction {
	private Class<?> component;

	public ArrayNewAction(GenericArray array) {
		super(array, Generics.INT);
		this.component = array.getGenericComponentType().getRawType();
	}

	@Override
	public Object valueImpl(Object... arguments) throws Exception {
		int size = ((Number)arguments[0]).intValue();
		return Array.newInstance(component, size);
	}
}
