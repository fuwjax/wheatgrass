package org.fuwjin.wheatgrass.binding;

import org.fuwjin.wheatgrass.Key;

public abstract class AbstractBinding<T> implements Binding<T>{
	private Key<T> key;

	public AbstractBinding(Key<T> key){
		this.key = key;
	}
	
	@Override
	public Key<T> key() {
		return key;
	}
	
	@Override
	public String toString() {
		return key.toString();
	}
}
