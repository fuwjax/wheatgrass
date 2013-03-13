package org.fuwjin.wheatgrass;

import static org.fuwjin.wheatgrass.Wheatgrass.keyOf;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import org.fuwjin.wheatgrass.binding.Binding;
import org.fuwjin.wheatgrass.binding.ConstantBinding;
import org.fuwjin.wheatgrass.context.ConstantContext;
import org.fuwjin.wheatgrass.context.Context;
import org.fuwjin.wheatgrass.context.ReflectiveContext;

/**
 * Creates new {@link StandardInjector} instances.
 * 
 * @author fuwjax
 * 
 */
public class InjectorBuilder implements RootInjectorBuilder {
	private Injector parent;
	private Class<? extends Annotation> scope;
	private List<Context> contexts = new ArrayList<Context>();
	private List<Binding<?>> bindings = new ArrayList<Binding<?>>();

	/**
	 * Creates a new builder.
	 * 
	 * @param scope
	 *            the scope, may be null
	 * @param parent
	 *            the parent, may be null
	 */
	public InjectorBuilder(Class<? extends Annotation> scope, Injector parent) {
		this.scope = scope;
		this.parent = parent;
	}

	@Override
	public Injector build() {
		assertBuilding();
		if (!bindings.isEmpty()) {
			withContext(new ConstantContext(bindings));
		}
		Context[] array = contexts.toArray(new Context[contexts.size()]);
		contexts = null;
		bindings = null;
		return createInjector(scope, parent, array);
	}

	@Override
	public <T> InjectorBuilder withConstant(Class<T> type, T constant) {
		addBinding(ConstantBinding.bind(constant, type));
		return this;
	}

	@Override
	public <T> InjectorBuilder withConstant(Key<T> key, T constant) {
		addBinding(ConstantBinding.bind(constant, key));
		return this;
	}

	@Override
	public InjectorBuilder withConstants(Object... constants) {
		for (Object constant : constants) {
			addBinding(ConstantBinding.bind(constant));
		}
		return this;
	}

	@Override
	public InjectorBuilder withContext(Context... contexts) {
		for (Context context : contexts) {
			addContext(context);
		}
		return this;
	}

	/**
	 * Creates constants from the parent. Similar behavior as
	 * {@code withConstants(parent.get(type))}.
	 * 
	 * @param types
	 *            the types to create
	 * @return this builder
	 * @throws Exception
	 *             if the constants cannot be created
	 */
	public InjectorBuilder withCreateConstants(Class<?>... types)
			throws Exception {
		for (Class<?> type : types) {
			addBinding(ConstantBinding.bind(get(keyOf(type))));
		}
		return this;
	}

	/**
	 * Creates constants from the parent. Similar behavior as
	 * {@code withConstants(parent.get(keys))}.
	 * 
	 * @param keys
	 *            the keys to create
	 * @return this builder
	 * @throws Exception
	 *             if the constants cannot be created
	 */
	public InjectorBuilder withCreateConstants(Key<?>... keys) throws Exception {
		for (Key<?> key : keys) {
			addBinding(ConstantBinding.bind(get(key)));
		}
		return this;
	}

	/**
	 * Creates member contexts from the parent. Similar behavior as
	 * {@code withMembers(parent.get(type))}.
	 * 
	 * @param types
	 *            the types to create
	 * @return this builder
	 * @throws Exception
	 *             if the member contexts cannot be created
	 */
	public InjectorBuilder withCreateMembers(Class<?>... types)
			throws Exception {
		for (Class<?> type : types) {
			addContext(new ReflectiveContext(get(keyOf(type))));
		}
		return this;
	}

	/**
	 * Creates member contexts from the parent. Similar behavior as
	 * {@code withMembers(parent.get(keys))}.
	 * 
	 * @param keys
	 *            the keys to create
	 * @return this builder
	 * @throws Exception
	 *             if the member contexts cannot be created
	 */
	public InjectorBuilder withCreateMembers(Key<?>... keys) throws Exception {
		for (Key<?> key : keys) {
			addContext(new ReflectiveContext(get(key)));
		}
		return this;
	}

	/**
	 * Creates constant from the parent. Similar behavior as
	 * {@code withConstant(type, parent.inject(constant))}.
	 * 
	 * @param type
	 *            the type to bind
	 * @param constant
	 *            the instance to inject
	 * @return this builder
	 * @throws Exception
	 *             if the constant cannot be injected
	 */
	public <T> InjectorBuilder withInjectConstant(Class<T> type, T constant)
			throws Exception {
		addBinding(ConstantBinding.bind(inject(constant), type));
		return this;
	}

	/**
	 * Creates constant from the parent. Similar behavior as
	 * {@code withConstant(key, parent.inject(constant))}.
	 * 
	 * @param key
	 *            the key to bind
	 * @param constant
	 *            the instance to inject
	 * @return this builder
	 * @throws Exception
	 *             if the constant cannot be injected
	 */
	public <T> InjectorBuilder withInjectConstant(Key<T> key, T constant)
			throws Exception {
		addBinding(ConstantBinding.bind(inject(constant), key));
		return this;
	}

	/**
	 * Creates constants from the parent. Similar behavior as
	 * {@code withConstants(parent.inject(constant))}.
	 * 
	 * @param constants
	 *            the instances to inject
	 * @return this builder
	 * @throws Exception
	 *             if the constants cannot be injected
	 */
	public InjectorBuilder withInjectConstants(Object... constants)
			throws Exception {
		for (Object constant : constants) {
			addBinding(ConstantBinding.bind(inject(constant)));
		}
		return this;
	}

	/**
	 * Creates member contexts from the parent. Similar behavior as
	 * {@code withMembers(parent.inject(context))}.
	 * 
	 * @param contexts
	 *            the instances to inject
	 * @return this builder
	 * @throws Exception
	 *             if the member contexts cannot be injected
	 */
	public InjectorBuilder withInjectMembers(Object... contexts)
			throws Exception {
		for (Object o : contexts) {
			addContext(new ReflectiveContext(inject(o)));
		}
		return this;
	}

	@Override
	public InjectorBuilder withMembers(Object... context) {
		for (Object o : context) {
			addContext(new ReflectiveContext(o));
		}
		return this;
	}

	protected void addBinding(Binding<?> binding) {
		assertBuilding();
		bindings.add(binding);
	}

	protected void assertBuilding() {
		if (bindings == null || contexts == null) {
			throw new IllegalStateException(
					"Injector has already been built from this builder");
		}
	}

	protected void addContext(Context context) {
		assertBuilding();
		contexts.add(context);
	}

	/**
	 * Creates a new injector.
	 * 
	 * @param scope
	 *            the scope, may be null
	 * @param contexts
	 *            the contexts
	 * @return the new injector
	 */
	protected Injector createInjector(Class<? extends Annotation> scope, Injector parent,
			Context[] contexts) {
		return new StandardInjector(scope, parent, contexts);
	}

	/**
	 * Returns an object from the parent.
	 * 
	 * @param key
	 *            the key to retrieve
	 * @return the object
	 * @throws Exception
	 *             if the object cannot be returned
	 */
	protected <T> T get(Key<T> key) throws Exception {
		assertParentInjector();
		return parent.get(key);
	}

	protected void assertParentInjector() {
		if (parent == null) {
			throw new IllegalStateException(
					"This builder does not support injection or creation");
		}
	}

	protected <T> T inject(T object) throws Exception {
		assertParentInjector();
		return parent.inject(object);
	}
}
