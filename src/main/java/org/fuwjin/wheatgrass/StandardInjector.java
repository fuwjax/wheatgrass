package org.fuwjin.wheatgrass;

import java.lang.annotation.Annotation;
import java.util.Arrays;

import javax.inject.Scope;

import org.fuwjin.util.Types;
import org.fuwjin.wheatgrass.binding.Binding;
import org.fuwjin.wheatgrass.binding.SingletonBinding;
import org.fuwjin.wheatgrass.context.Context;
import org.fuwjin.wheatgrass.context.InjectorContext;

/**
 * An aggregator {@link Context} optionally bound to a scope. An injector is
 * primarily built by an {@link InjectorBuilder}, however this is not a
 * requirement.
 * <p>
 * Scopes are generally used to maintain a cached value for a particular scope
 * level. They are often custom annotations annotated by {@link Scope}, however
 * this is not a requirement. Scopes may potentially serve additional purposes,
 * see the extension information below for ways to customize StandardInjector is such
 * cases.
 * 
 * <p>
 * The general use case for Injectors is the following: <code><pre>
 * //create a new injector with bindings based reflectively on someObject
 * StandardInjector injector = Wheatgrass.newInjector().withMembers(someObject).build();
 * 
 * //get a Foo instance from the injector
 * Foo foo = injector.get(Foo.class);
 * 
 * //get a Provider instance from the injector
 * Provider<Bar> bar = injector.provide(Bar.class);
 * 
 * //build a scoped subInjector for SomeScope
 * StandardInjector scopeInjector = injector.newInjector(SomeScope.class).build();
 * 
 * //build a Baz instance scoped to SomeScope
 * Baz baz = scopeInjector.inject(new Baz());
 * </pre></code>
 * <p>
 * Note that if generic types or annotations are required for the proper
 * construction of an object, the {@link Wheatgrass#keyOf(Class, Annotation...)}
 * and related methods should be used.
 * 
 * <p>
 * There are two primary reasons to extend this class. If scopes require more
 * than simply existing, the {@link StandardInjector#hasScope(Key, Binding)} method
 * should be overridden. If a new {@link Binding} should be decorated, or if the
 * default caching behavior for scopes should be revised, then overload the
 * {@link StandardInjector#decorate(Key, Binding)} method.
 * 
 * @author fuwjax
 * 
 */
public class StandardInjector extends Injector {
	private final Class<? extends Annotation> scope;
	private Context[] contexts;
	private BindingMap bindings = new BindingMap();
	private Injector parent;

	/**
	 * Creates a new injector bound to {@code scope}.
	 * 
	 * @param scope
	 *            the bound scope
	 * @param contexts
	 *            the aggregated contexts
	 */
	public StandardInjector(Class<? extends Annotation> scope, Injector parent,
			Context... contexts) {
		this.parent = parent;
		assert Types.nestedNotNull(contexts);
		this.scope = scope;
		this.contexts = Arrays.copyOf(contexts, contexts.length + 1);
		this.contexts[contexts.length] = new InjectorContext(this);
		for(Context context: this.contexts){
			for(Binding<?> binding: context.bindings()){
				bindings.add(decorate(binding));
			}
		}
	}

	protected <T> Binding<T> decorate(Binding<T> binding) {
		if(binding != null && scope != null && binding.key().isAnnotationPresent(scope)){
			return SingletonBinding.bind(binding);
		}
		return binding;
	}
	
	@Override
	public InjectorBuilder newInjector(Class<? extends Annotation> scope) {
		return new InjectorBuilder(scope, this);
	}
	
	@Override
	protected <T> Binding<T> bind(Key<T> key) {
		Binding<T> binding = bindings.bind(key);
		if(binding != null){
			return binding;
		}
		for (Context context : contexts) {
			binding = bindings.bestOf(key, binding, context.bindings(key));
		}
		if(binding == null){
			if(parent == null){
				throw new RuntimeException("No binding found for "+key);
			}
			binding = parent.bind(key);
		}
		binding = decorate(binding);
		bindings.add(key.type(), binding);
		return binding;
	}
}

