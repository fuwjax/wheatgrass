package org.fuwjin.wheatgrass.binding;

import static org.fuwjin.util.Types.access;
import static org.fuwjin.wheatgrass.Wheatgrass.argsOf;
import static org.fuwjin.wheatgrass.Wheatgrass.keyOf;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import javax.inject.Provider;

import org.fuwjin.wheatgrass.Injector;
import org.fuwjin.wheatgrass.Key;

public class ProviderMethodBinding<T> implements Binding<T>{
	public static Binding<?> bind(Method method, Object target){
		Key<?> providerKey = keyOf(method.getName(), method.getReturnType(), method.getGenericReturnType(), method.getAnnotations());
		return bind(method, target, providerKey.forType(providerKey.type().parameters()[0]));
	}

	private static <T> Binding<T> bind(Method method, Object target, Key<T> key) {
		if(Modifier.isStatic(method.getModifiers())){
			return new ProviderMethodBinding<T>(method, null, key);
		}
		return new ProviderMethodBinding<T>(method, target, key);
	}

	private Key<T> key;
	private Method method;
	private Object target;
	private Key<?>[] params;

	protected ProviderMethodBinding(Method method, Object target, Key<T> key) {
		assert Provider.class.isAssignableFrom(method.getReturnType());
		this.key = key;
		this.method = method;
		this.target = target;
		params = argsOf(method);
	}
	
	@Override
	public T get(Injector root) throws Exception {
		Object[] args = root.get(params);
		return key.type().cast(((Provider<?>)access(method).invoke(target, args)).get());
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
