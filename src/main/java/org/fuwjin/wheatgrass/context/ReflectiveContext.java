package org.fuwjin.wheatgrass.context;

import static java.util.Collections.emptySet;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Provider;

import org.fuwjin.wheatgrass.Key;
import org.fuwjin.wheatgrass.Provides;
import org.fuwjin.wheatgrass.binding.Binding;
import org.fuwjin.wheatgrass.binding.FieldBinding;
import org.fuwjin.wheatgrass.binding.MethodBinding;
import org.fuwjin.wheatgrass.binding.NativeMethodBinding;
import org.fuwjin.wheatgrass.binding.ProviderFieldBinding;
import org.fuwjin.wheatgrass.binding.ProviderMethodBinding;

public class ReflectiveContext implements Context {
	private Object context;

	public ReflectiveContext(Object context) {
		this.context = context;
	}
	
	@Override
	public Collection<? extends Binding<?>> bindings() {
		List<Binding<?>> bindings = new ArrayList<Binding<?>>();
		Class<?> cls = context.getClass();
		while(cls != null){
			for(final Field field: cls.getDeclaredFields()){
				if(Provider.class.isAssignableFrom(field.getType())){
					bindings.add(ProviderFieldBinding.bind(field, context));
				}else if(!field.isSynthetic()){
					bindings.add(FieldBinding.bind(field, context));
				}
			}
			for(final Method method: cls.getDeclaredMethods()){
				if(Modifier.isNative(method.getModifiers())){
					bindings.add(NativeMethodBinding.bind(method));
				}
				if(method.isAnnotationPresent(Provides.class)){
					if(Provider.class.isAssignableFrom(method.getReturnType())){
						bindings.add(ProviderMethodBinding.bind(method, context));
					}else if(!method.isSynthetic()){
						bindings.add(MethodBinding.bind(method, context));
					}
				}
			}
			cls = cls.getSuperclass();
		}
		return bindings;
	}
	
	@Override
	public Collection<? extends Binding<?>> bindings(Key<?> key) {
		return emptySet();
	}
}
