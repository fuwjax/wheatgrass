package org.fuwjin.wheatgrass.binding;

import org.fuwjin.wheatgrass.Injector;
import org.fuwjin.wheatgrass.Key;

public interface Binding<T> {
	T get(Injector root) throws Exception;

	Key<T> key();
}
