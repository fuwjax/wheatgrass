package org.fuwjin.wheatgrass;

import static org.fuwjin.util.Types.access;
import static org.fuwjin.wheatgrass.Wheatgrass.argsOf;
import static org.fuwjin.wheatgrass.Wheatgrass.keyOf;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import javax.inject.Inject;
import javax.inject.Provider;

import org.fuwjin.wheatgrass.binding.Binding;

/**
 * The primary object for managing creation and wiring of classes.
 * 
 * <p>
 * The general use case for Injectors is the following: <code><pre>
 * //create a new injector with bindings based reflectively on someObject
 * Injector injector = Wheatgrass.newInjector().withMembers(someObject).build();
 * 
 * //get a Foo instance from the injector
 * Foo foo = injector.get(Foo.class);
 * 
 * //get a Provider instance from the injector
 * Provider<Bar> bar = injector.provide(Bar.class);
 * 
 * //build a scoped subInjector for SomeScope
 * Injector scopeInjector = injector.newInjector(SomeScope.class).build();
 * 
 * //build a Baz instance scoped to SomeScope
 * Baz baz = scopeInjector.inject(new Baz());
 * </pre></code>
 * <p>
 * Note that if generic types or annotations are required for the proper
 * construction of an object, the {@link Wheatgrass#keyOf(Class, Annotation...)}
 * and related methods should be used.
 * 
 * @author fuwjax
 * 
 */
public abstract class Injector {
	/**
	 * Returns an instance of type {@code type}. Equivalent to
	 * {@code bind(type).provide(this).get()}.
	 * 
	 * @param type
	 *            the type to retrieve
	 * @return the instance of type {@code type}
	 * @throws Exception
	 *             if the instance cannot be returned
	 */
	public <T> T get(Class<T> type) throws Exception {
		return get(keyOf(type));
	}

	/**
	 * Returns an instance of type {@code key.type()} subject to the type and
	 * annotations on the key. Equivalent to
	 * {@code bind(key).provide(this).get()}.
	 * 
	 * @param key
	 *            the key to retrieve
	 * @return an instance of type {@code key.type()}
	 * @throws Exception
	 *             if the instance cannot be returned
	 */
	public <T> T get(Key<T> key) throws Exception {
		return bind(key).get(this);
	}

	/**
	 * Returns a provider for the type {@code type}. Equivalent to
	 * {@code bind(type).provide(this)}.
	 * 
	 * @param type
	 *            the provided type
	 * @return the provider
	 * @throws Exception
	 *             if the provider cannot be returned
	 */
	public <T> Provider<T> provide(Class<T> type) throws Exception {
		return provide(keyOf(type));
	}

	/**
	 * Returns a provider for the type {@code key.type()}. Equivalent to
	 * {@code bind(key).provide(this)}.
	 * 
	 * @param key
	 *            the provided key
	 * @return the provider
	 * @throws Exception
	 *             if the provider cannot be returned
	 */
	public <T> Provider<T> provide(final Key<T> key) throws Exception {
		return new Provider<T>(){
			@Override
			public T get() {
				try {
					return Injector.this.get(key);
				} catch (Exception e) {
					throw new ProviderException("Could not provide instance for "+key, e);
				}
			}
		};
	}

	/**
	 * Returns a binding for the type {@code type}. Equivalent to
	 * {@code bind(Wheatgrass.keyOf(type))}.
	 * 
	 * @param type
	 *            the type to bind
	 * @return the binding
	 * @throws Exception
	 *             if the binding cannot be returned
	 */
	protected abstract <T> Binding<T> bind(Key<T> key);
	
	/**
	 * Injects fields and methods annotated with {@link Inject} on the
	 * {@code instance} with objects available through this injector.
	 * 
	 * @param instance
	 *            the instance to inject
	 * @return the injected instance
	 * @throws Exception
	 *             if the injection fails
	 */
	public <T> T inject(T instance) throws Exception {
		Class<?> type = instance.getClass();
		while (type != null) {
			for (Field field : type.getDeclaredFields()) {
				if (field.isAnnotationPresent(Inject.class)) {
					access(field).set(
							instance,
							get(keyOf(field.getType(), field.getGenericType(),
									field.getAnnotations())));
				}
			}
			for (Method method : type.getDeclaredMethods()) {
				if (method.isAnnotationPresent(Inject.class)) {
					access(method).invoke(instance, get(argsOf(method)));
				}
			}
			type = type.getSuperclass();
		}
		return instance;
	}

	/**
	 * Returns an array created for the {@code keys}.
	 * 
	 * @param keys
	 *            the target keys
	 * @return an array composed of elements retrieved by the keys
	 * @throws Exception
	 *             if the array cannot be returned
	 */
	public Object[] get(Key<?>[] keys) throws Exception {
		final Object[] array = new Object[keys.length];
		for (int i = 0; i < array.length; i++) {
			array[i] = get(keys[i]);
		}
		return array;
	}

	/**
	 * Creates a new injector builder with this as its parent and no scope.
	 * 
	 * @return the new injector builder
	 */
	public InjectorBuilder newInjector(){
		return newInjector(null);
	}

	/**
	 * Creates a new injector builder with this as its parent and scoped to
	 * {@code scope}.
	 * 
	 * @return the new injector builder
	 */
	public abstract InjectorBuilder newInjector(
			Class<? extends Annotation> scope);
}
