package org.fuwjin.wheatgrass.binding;

import static org.fuwjin.util.Types.access;
import static org.fuwjin.wheatgrass.Wheatgrass.keyOf;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import javax.inject.Provider;

import org.fuwjin.wheatgrass.Injector;
import org.fuwjin.wheatgrass.Key;

public class ProviderFieldBinding<T> implements Binding<T>{
	public static Binding<?> bind(Field field, Object target){
		Key<?> providerKey = keyOf(field.getName(), field.getType(), field.getGenericType(), field.getAnnotations());
		return bind(field, target, providerKey.forType(providerKey.type().parameters()[0]));
	}

	private static <T> Binding<T> bind(Field field, Object target, Key<T> key) {
		if(Modifier.isStatic(field.getModifiers())){
			return new ProviderFieldBinding<T>(field, null, key);
		}
		return new ProviderFieldBinding<T>(field, target, key);
	}

	private Key<T> key;
	private Field field;
	private Object target;

	protected ProviderFieldBinding(Field field, Object target, Key<T> key) {
		assert Provider.class.isAssignableFrom(field.getType());
		this.key = key;
		this.field = field;
		this.target = target;
	}
	
	@Override
	public T get(Injector root) throws Exception {
		return key.type().cast(((Provider<?>)access(field).get(target)).get());
	}

	@Override
	public Key<T> key() {
		return key;
	}
	
	@Override
	public String toString() {
		return key+": "+field;
	}
}
