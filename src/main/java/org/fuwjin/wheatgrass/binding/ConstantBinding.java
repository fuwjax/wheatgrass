package org.fuwjin.wheatgrass.binding;

import static org.fuwjin.wheatgrass.Wheatgrass.keyOf;

import org.fuwjin.wheatgrass.Injector;
import org.fuwjin.wheatgrass.Key;

public class ConstantBinding<T> implements Binding<T> {
	public static Binding<?> bind(Object constant){
		return bind(constant, (Class)constant.getClass());
	}
	
	public static <T> Binding<T> bind(T constant, Class<T> type){
		return new ConstantBinding<T>(constant, keyOf(type));
	}

	public static <T> Binding<T> bind(T constant, Key<T> key){
		return new ConstantBinding<T>(constant, key);
	}

	private T constant;
	private Key<T> key;

	protected ConstantBinding(T constant, Key<T> key) {
		this.constant = constant;
		this.key = key;
	}
	
	@Override
	public T get(Injector root) throws Exception {
		return constant;
	}

	@Override
	public Key<T> key() {
		return key;
	}
	
	@Override
	public String toString() {
		return key+": "+constant;
	}
}
