package org.fuwjin.generic;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.fuwjin.util.Constraint;

public class ActionConstraint {
	public static Constraint<GenericAction> constructor(){
		return new Constraint<GenericAction>(){
			public boolean satisfies(GenericAction target) {
				return target.element() instanceof Constructor;
			}
		};
	}

	public static Constraint<GenericAction> field() {
		return new Constraint<GenericAction>(){
			public boolean satisfies(GenericAction target) {
				return target.element() instanceof Field;
			}
		};
	}

	public static Constraint<GenericAction> method() {
		return new Constraint<GenericAction>(){
			public boolean satisfies(GenericAction target) {
				return target.element() instanceof Method;
			}
		};
	}

	public static Constraint<GenericAction> args(final int count) {
		return new Constraint<GenericAction>(){
			@Override
			public boolean satisfies(GenericAction target) {
				return target.parameters().length == count;
			}
		};
	}
}
