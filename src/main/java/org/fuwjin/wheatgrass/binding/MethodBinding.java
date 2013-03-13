package org.fuwjin.wheatgrass.binding;

import static org.fuwjin.util.Types.access;
import static org.fuwjin.wheatgrass.Wheatgrass.argsOf;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.fuwjin.wheatgrass.Injector;
import org.fuwjin.wheatgrass.Key;
import org.fuwjin.wheatgrass.Wheatgrass;

public class MethodBinding<T> implements Binding<T> {
	public static Binding<?> bind(Method method, Object target){
		return bind(method, target, Wheatgrass.returnTypeOf(method));
	}

	private static <T> Binding<T> bind(Method method, Object target, Key<T> key) {
		if(Modifier.isStatic(method.getModifiers())){
			return new MethodBinding<T>(method, null, key);
		}
		return new MethodBinding<T>(method, target, key);
	}

	private Method method;
	private Object target;
	private Key<T> key;
	private Key<?>[] params;

	protected MethodBinding(Method method, Object target, Key<T> key) {
		this.method = method;
		this.target = target;
		this.key = key;
		params = argsOf(method);
	}
	
	@Override
	public T get(Injector root) throws Exception {
		final Object[] args = root.get(params);
		return key.type().cast(access(method).invoke(target, args));
	}

	@Override
	public Key<T> key() {
		return key;
	}
	
	@Override
	public String toString() {
		return key+": "+method;
	}
}
