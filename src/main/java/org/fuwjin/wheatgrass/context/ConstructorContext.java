package org.fuwjin.wheatgrass.context;

import static java.lang.reflect.Modifier.isAbstract;
import static java.util.Collections.emptySet;
import static java.util.Collections.singleton;

import java.lang.reflect.Constructor;
import java.util.Collection;

import javax.inject.Inject;

import org.fuwjin.wheatgrass.Key;
import org.fuwjin.wheatgrass.binding.Binding;
import org.fuwjin.wheatgrass.binding.ConstructorBinding;

public class ConstructorContext implements Context {
	@Override
	public Collection<Binding<?>> bindings() {
		return emptySet();
	}
	
	@Override
	public Collection<? extends Binding<?>> bindings(Key<?> key) {
		Class<?> type = key.type().type();
		if(!isConcreteClass(type)){
			return emptySet();
		}
		Constructor<?>[] constructors = type.getDeclaredConstructors();
		if(constructors.length == 0){
			return emptySet();
		}
		if(constructors.length == 1){
			return singleton(ConstructorBinding.bind(constructors[0]));
		}
		Constructor<?> noArgs = null;
		for(Constructor<?> c: constructors){
			if(c.isAnnotationPresent(Inject.class)){
				return singleton(ConstructorBinding.bind(c));
			}
			if(c.getParameterTypes().length == 0){
				noArgs = c;
			}
		}
		if(noArgs != null){
			return singleton(ConstructorBinding.bind(noArgs));
		}
		return emptySet();
	}

	private <T> boolean isConcreteClass(Class<T> type) {
		return !(type.isInterface() || type.isAnnotation() || type.isInterface() || isAbstract(type.getModifiers()));
	}
}
