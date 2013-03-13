package org.fuwjin.wheatgrass.type;

import java.lang.reflect.Type;

public class GenericAdapter<T> implements Generic<T> {
	private Generic<T> type;
	public GenericAdapter(Generic<T> type){
		this.type = type;
	}
	
	protected Generic<T> getType() {
		return type;
	}

	@Override
	public Class<T> type() {
		return getType().type();
	}

	@Override
	public Type genericType() {
		return getType().genericType();
	}

	@Override
	public Generic<?>[] parameters() {
		return getType().parameters();
	}

	@Override
	public Generic<?> superclass() {
		return getType().superclass();
	}

	@Override
	public Generic<?>[] interfaces() {
		return getType().interfaces();
	}

	@Override
	public boolean contains(Generic<?> key) {
		return getType().contains(key);
	}

	@Override
	public boolean captures(Generic<?> key) {
		return getType().captures(key);
	}

	@Override
	public Generic<T> convertFrom(Generic<?> source) {
		return getType().convertFrom(source);
	}
	
	@Override
	public String conversionId() {
		return getType().conversionId();
	}
	
	@Override
	public T cast(Object instance) {
		return getType().cast(instance);
	}
}
