package org.fuwjin.wheatgrass.context;

import java.util.Collection;

import org.fuwjin.wheatgrass.Key;
import org.fuwjin.wheatgrass.binding.Binding;

public interface Context {
	Collection<? extends Binding<?>> bindings();
	
	Collection<? extends Binding<?>> bindings(Key<?> key); 
}
