package org.fuwjin.generic;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.Objects;

import org.fuwjin.generic.action.ConstructorAction;
import org.fuwjin.generic.action.FieldAccessAction;
import org.fuwjin.generic.action.FieldMutateAction;
import org.fuwjin.generic.action.InstanceFieldAccessAction;
import org.fuwjin.generic.action.InstanceFieldMutateAction;
import org.fuwjin.generic.action.InstanceMethodAction;
import org.fuwjin.generic.action.MethodAction;
import org.fuwjin.generic.action.StaticFieldAccessAction;
import org.fuwjin.generic.action.StaticFieldMutateAction;
import org.fuwjin.generic.action.StaticMethodAction;
import org.fuwjin.util.FilterSet;

public class GenericType implements Generic {
	private ParameterizedType p;
	private Class<?> c;
	private Generic s;
	private Generic[] is;
	private GenericType owner;
	private TypeVariableMap vars;
	private FilterSet<GenericAction> actions;
	private boolean raw;

	public GenericType(Class<?> c) {
		this.c = c;
		this.owner = c.getDeclaringClass() == null ? null : new GenericType(
				c.getDeclaringClass());
		this.raw = true;
		vars = new TypeVariableMap(c);
	}

	public GenericType(ParameterizedType p) {
		this.p = p;
		c = (Class<?>) p.getRawType();
		owner = (GenericType)Generics.genericOf(p.getOwnerType());
		vars = new TypeVariableMap(p);
	}

	public GenericType(Class<?> raw, GenericType owner, Generic... args) {
		this.c = raw;
		this.owner = owner;
		vars = new TypeVariableMap(raw, args);
	}

	@Override
	public Class<?> getRawType() {
		return c;
	}

	@Override
	public Generic supertype() {
		if (s == null) {
			if (c.isInterface()) {
				s = Generics.OBJECT;
			} else if(Object.class.equals(c)){
				s = Generics.TOP;
			}else{
				s = vars.subst(c.getGenericSuperclass());
			}
		}
		return s;
	}

	@Override
	public Generic[] interfaces() {
		if (is == null) {
			is = vars.subst(getRawType().getGenericInterfaces());
		}
		return is;
	}

	@Override
	public boolean isAssignableTo(Generic type) {
		if (this.equals(type)) {
			return true;
		}
		if(type instanceof GenericArgument){
			return type.contains(this);
		}
		if (Objects.equals(getRawType(),type.getRawType())) {
			return type.contains(this);
		}
		if (supertype().isAssignableTo(type)) {
			return true;
		}
		for (Generic iface : interfaces()) {
			if (iface.isAssignableTo(type)) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean isInstance(Object object) {
		return c.isInstance(object);
	}
	
	@Override
	public FilterSet<GenericAction> actions() {
		if(actions == null){
			actions = new FilterSet<GenericAction>();
			for(Constructor<?> cons: c.getDeclaredConstructors()){
				actions.add(new ConstructorAction(this, cons, vars.subst(cons.getGenericParameterTypes())));
			}
			for(Field field: c.getDeclaredFields()){
				if(Modifier.isStatic(field.getModifiers())){
					actions.add(new StaticFieldAccessAction(this, field, vars.subst(field.getGenericType())));
					actions.add(new StaticFieldMutateAction(this, field, vars.subst(field.getGenericType())));
				}else{
					actions.add(new FieldAccessAction(this, field, vars.subst(field.getGenericType())));
					actions.add(new FieldMutateAction(this, field, vars.subst(field.getGenericType())));
				}
			}
			for(Method method: c.getDeclaredMethods()){
				if(Modifier.isStatic(method.getModifiers())){
					actions.add(new StaticMethodAction(this, method, vars.subst(method.getGenericReturnType()), vars.subst(method.getGenericParameterTypes())));
				}else{
					actions.add(new MethodAction(this, method, vars.subst(method.getGenericReturnType()), vars.subst(method.getGenericParameterTypes())));
				}
			}
		}
		return actions;
	}
	
	@Override
	public GenericValue valueOf(final Object value) {
		if(value != null && !isInstance(value)){
			throw new IllegalArgumentException("Unexpected value: "+value);
		}
		return new AbstractGenericValue(this, value){
			@Override
			public FilterSet<GenericAction> actions() {
				FilterSet<GenericAction> actions = new FilterSet<GenericAction>();
				for(Field field: c.getDeclaredFields()){
					if(!Modifier.isStatic(field.getModifiers())){
						actions.add(new InstanceFieldAccessAction(GenericType.this, value, field, vars.subst(field.getGenericType())));
						actions.add(new InstanceFieldMutateAction(GenericType.this, value, field, vars.subst(field.getGenericType())));
					}
				}
				for(Method method: c.getDeclaredMethods()){
					if(!Modifier.isStatic(method.getModifiers())){
						actions.add(new InstanceMethodAction(GenericType.this, value, method, vars.subst(method.getGenericReturnType()), vars.subst(method.getGenericParameterTypes())));
					}
				}
				return actions;
			}
		};
	}

	public GenericType getOwnerType() {
		return owner;
	}

	public Generic[] getActualTypeArguments() {
		return vars.toArray();
	}
	
	@Override
	public boolean contains(Generic type) {
		GenericType pt = (GenericType) type;
		if (getRawType().equals(pt.getRawType())) {
			for (int i = 0; i < pt.getActualTypeArguments().length; i++) {
				if (!pt.getActualTypeArguments()[i].isAssignableTo(getActualTypeArguments()[i])) {
					return false;
				}
			}
			return true;
		}
		return false;
	}
	
	@Override
	public String toString() {
		if (p != null) {
			return p.toString();
		}
		if (getActualTypeArguments().length == 0 || raw) {
			return getRawType().getCanonicalName();
		}
		String argString = Arrays.toString(getActualTypeArguments());
		return getRawType().getCanonicalName() + "<"
				+ argString.substring(1, argString.length() - 1) + ">";
	}

	@Override
	public boolean equals(Object obj) {
		try {
			GenericType o = (GenericType) obj;
			return Objects.equals(getRawType(),o.getRawType())
					&& Objects.equals(getOwnerType(),o.getOwnerType())
					&& Arrays.equals(getActualTypeArguments(),
							o.getActualTypeArguments());
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return Objects.hash(getRawType(), getOwnerType(),
				Arrays.hashCode(getActualTypeArguments()));
	}
}
