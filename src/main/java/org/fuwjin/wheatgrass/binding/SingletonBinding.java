package org.fuwjin.wheatgrass.binding;

import org.fuwjin.wheatgrass.Injector;
import org.fuwjin.wheatgrass.Key;

public class SingletonBinding<T> implements Binding<T> {
	private Binding<T> binding;
	private T instance;

	public SingletonBinding(Binding<T> binding) {
		this.binding = binding;
	}

	public static <T> Binding<T> bind(Binding<T> binding){
		return new SingletonBinding<T>(binding);
	}
	
	@Override
	public T get(Injector root) throws Exception {
		if(instance == null){
			instance = binding.get(root);
		}
		return instance;
	}
	
	@Override
	public Key<T> key() {
		return binding.key();
	}
	
	@Override
	public String toString() {
		return "@Singleton "+binding;
	}
}

