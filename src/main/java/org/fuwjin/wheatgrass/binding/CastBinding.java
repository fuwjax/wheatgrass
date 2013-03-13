package org.fuwjin.wheatgrass.binding;

import org.fuwjin.wheatgrass.Injector;
import org.fuwjin.wheatgrass.Key;
import org.fuwjin.wheatgrass.type.Generic;

public class CastBinding<T> implements Binding<T> {
	private Generic<T> cast;
	private Binding<?> binding;
	private Key<T> key;

	public static <T> Binding<T> bind(Generic<T> cast, Binding<?> binding){
		if("1".equals(cast.conversionId())){
			return (Binding<T>)binding;
		}
		while(binding instanceof CastBinding){
			binding = ((CastBinding<?>)binding).binding;
		}
		return new CastBinding<T>(cast, binding);
	}

	public CastBinding(Generic<T> cast, Binding<?> binding) {
		this.cast = cast;
		this.binding = binding;
		key = binding.key().forType(cast);
	}
	
	@Override
	public T get(Injector root) throws Exception {
		return cast.cast(binding.get(root));
	}

	@Override
	public Key<T> key() {
		return key;
	}
	
	@Override
	public String toString() {
		return key+": "+binding;
	}
}
