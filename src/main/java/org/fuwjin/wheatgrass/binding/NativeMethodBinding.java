package org.fuwjin.wheatgrass.binding;

import static org.fuwjin.wheatgrass.Wheatgrass.argsOf;
import static org.fuwjin.wheatgrass.Wheatgrass.returnTypeOf;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.fuwjin.wheatgrass.Injector;
import org.fuwjin.wheatgrass.Key;
import org.fuwjin.wheatgrass.type.Generic;

public class NativeMethodBinding<T> implements Binding<T> {
	public static Binding<?> bind(Method method){
		assert Modifier.isNative(method.getModifiers());
		Key<?> key = returnTypeOf(method);
		Key<?>[] args = argsOf(method);
		if(args.length != 1){
			return null;
		}
		Key<?> concrete = args[0];
		Generic<?> cast = key.type().convertFrom(concrete.type());
		if(cast == null){
			return null;
		}
		return bind(key.forType(cast), concrete);
	}
	
	public static <T> Binding<T> bind(Key<T> key, Key<?> concrete){
		return new NativeMethodBinding<T>(key, concrete);
	}

	private Key<T> key;
	private Key<?> concrete;

	public NativeMethodBinding(Key<T> key, Key<?> concrete) {
		this.key = key;
		this.concrete = concrete;
	}
	
	@Override
	public T get(Injector root) throws Exception {
		return key.type().cast(root.get(concrete));
	}

	@Override
	public Key<T> key() {
		return key;
	}
	
	@Override
	public String toString() {
		return key+": "+concrete;
	}
}
