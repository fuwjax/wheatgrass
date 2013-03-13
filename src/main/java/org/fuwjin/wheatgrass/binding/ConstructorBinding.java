package org.fuwjin.wheatgrass.binding;

import static org.fuwjin.util.Types.access;
import static org.fuwjin.wheatgrass.Wheatgrass.argsOf;

import java.lang.reflect.Constructor;

import org.fuwjin.wheatgrass.Injector;
import org.fuwjin.wheatgrass.Key;
import org.fuwjin.wheatgrass.Wheatgrass;

public class ConstructorBinding<T> implements Binding<T> {
	public static <T> Binding<T> bind(Constructor<T> constructor){
		return new ConstructorBinding<T>(constructor);
	}
	
	private Constructor<T> constructor;
	private Key<T> key;
	private Key<?>[] arguments;

	protected ConstructorBinding(Constructor<T> constructor) {
		this.constructor = constructor;
		key = Wheatgrass.typeOf(constructor);
		arguments = argsOf(constructor);
	}
	
	@Override
	public T get(Injector root) throws Exception {
		Object[] args = root.get(arguments);
		return root.inject(access(constructor).newInstance(args));
	}

	@Override
	public Key<T> key() {
		return key;
	}
	
	@Override
	public String toString() {
		return key+": "+constructor;
	}
}
