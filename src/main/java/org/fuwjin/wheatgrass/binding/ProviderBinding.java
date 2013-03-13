package org.fuwjin.wheatgrass.binding;

import static org.fuwjin.wheatgrass.Wheatgrass.typeOf;

import javax.inject.Provider;

import org.fuwjin.wheatgrass.Injector;
import org.fuwjin.wheatgrass.Key;
import org.fuwjin.wheatgrass.type.Generic;

public class ProviderBinding<T> implements Binding<Provider<T>> {
	private static Generic<Provider> provider = typeOf(Provider.class);
	private Key<Provider<T>> key;
	private Key<?> provided;
	
	public static <T> Binding<? extends T> bind(Key<T> key){
		assert provider.convertFrom(key.type()) != null;
		return new ProviderBinding(key);
	}

	public ProviderBinding(Key<Provider<T>> key) {
		this.key = key;
		provided = key.forType(key.type().parameters()[0]);
	}
	
	@Override
	public Provider<T> get(Injector root) throws Exception {
		return key.type().cast(root.provide(provided));
	}

	@Override
	public Key<Provider<T>> key() {
		return key;
	}
	
	@Override
	public String toString() {
		return key+": Provider<"+provided+">";
	}
}
