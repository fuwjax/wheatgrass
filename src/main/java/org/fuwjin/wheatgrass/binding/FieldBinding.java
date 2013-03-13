package org.fuwjin.wheatgrass.binding;

import static org.fuwjin.util.Types.access;
import static org.fuwjin.wheatgrass.Wheatgrass.typeOf;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.fuwjin.wheatgrass.Injector;
import org.fuwjin.wheatgrass.Key;

public class FieldBinding<T> implements Binding<T>{
	public static Binding<?> bind(Field field, Object target){
		return bind(field, target, typeOf(field));
	}

	private static <T> Binding<T> bind(Field field, Object target, Key<T> key) {
		if(Modifier.isStatic(field.getModifiers())){
			return new FieldBinding<T>(field, null, key);
		}
		return new FieldBinding<T>(field, target, key);
	}

	private Key<T> key;
	private Object target;
	private Field field;

	protected FieldBinding(Field field, Object target, Key<T> key) {
		this.target = target;
		this.field = field;
		this.key = key;
	}
	
	@Override
	public T get(Injector root) throws Exception {
		return key.type().cast(access(field).get(target));
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
