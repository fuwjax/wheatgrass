package org.fuwjin.util;

public interface Constraint<T> {
	boolean satisfies(T target);
}
