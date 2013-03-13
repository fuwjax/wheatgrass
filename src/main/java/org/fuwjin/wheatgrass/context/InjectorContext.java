package org.fuwjin.wheatgrass.context;

import static java.util.Collections.emptyList;
import static java.util.Collections.singleton;
import static org.fuwjin.wheatgrass.Wheatgrass.typeOf;

import java.util.Collection;

import javax.inject.Provider;

import org.fuwjin.wheatgrass.Injector;
import org.fuwjin.wheatgrass.Key;
import org.fuwjin.wheatgrass.Wheatgrass;
import org.fuwjin.wheatgrass.binding.Binding;
import org.fuwjin.wheatgrass.binding.ConstantBinding;
import org.fuwjin.wheatgrass.binding.ProviderBinding;
import org.fuwjin.wheatgrass.type.Generic;

public class InjectorContext implements Context {
	private static Generic<Provider> provider = typeOf(Provider.class);
	private Injector injector;

	public InjectorContext(Injector injector){
		this.injector = injector;
	}
	
	@Override
	public Collection<? extends Binding<?>> bindings() {
		return singleton(ConstantBinding.bind(injector));
	}
	
	@Override
	public Collection<? extends Binding<?>> bindings(Key<?> key) {
		if(provider.convertFrom(key.type()) != null){
			return singleton(ProviderBinding.bind(key));
		}
		return emptyList();
	}
}
