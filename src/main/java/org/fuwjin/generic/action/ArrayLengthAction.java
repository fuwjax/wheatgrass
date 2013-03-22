package org.fuwjin.generic.action;

import java.lang.reflect.Array;

import org.fuwjin.generic.AbstractGenericAction;
import org.fuwjin.generic.GenericArray;
import org.fuwjin.generic.Generics;

public class ArrayLengthAction extends AbstractGenericAction {
	public ArrayLengthAction(GenericArray array) {
		super(Generics.INT, array);
	}

	@Override
	public Object valueImpl(Object... arguments) throws Exception {
		return Array.getLength(arguments[0]);
	}
}
