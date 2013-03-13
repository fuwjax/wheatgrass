package org.fuwjin.generic;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class GenericType implements Generic, ParameterizedType {
	private ParameterizedType p;
	private Class<?> c;
	private Generic s;
	private Generic[] is;
	private GenericType owner;
	private Generic[] args;
	private Map<TypeVariable<?>, Generic> argMap;

	public GenericType(Class<?> c) {
		this.c = c;
		this.owner = c.getDeclaringClass() == null ? null : new GenericType(
				c.getDeclaringClass());
	}

	public GenericType(ParameterizedType p) {
		this.p = p;
		c = (Class<?>) p.getRawType();
		if (p.getOwnerType() instanceof Class) {
			owner = new GenericType((Class<?>) p.getOwnerType());
		} else if (p.getOwnerType() instanceof ParameterizedType) {
			owner = new GenericType((ParameterizedType) p.getOwnerType());
		} else if (p.getOwnerType() != null) {
			throw new UnsupportedOperationException();
		}
	}

	public GenericType(Class<?> raw, GenericType owner, Generic... args) {
		this.c = raw;
		this.owner = owner;
		this.args = new Generic[args.length];
		argMap = new LinkedHashMap<TypeVariable<?>, Generic>();
		if(args.length > 0){
			TypeVariable<?>[] vars = c.getTypeParameters();
			for (int i = 0; i < vars.length; i++) {
				GenericArgument arg = new GenericArgument();
				argMap.put(vars[i], arg);
				Generic[] bounds = subst(vars[i].getBounds());
				this.args[i] = arg.init(bounds, args[i]).collapse();
				argMap.put(vars[i], this.args[i]);
			}
		}
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
				s = subst(c.getGenericSuperclass());
			}
		}
		return s;
	}

	private Generic[] subst(Type[] ts) {
		Generic[] gs = new Generic[ts.length];
		for (int i = 0; i < ts.length; i++) {
			gs[i] = subst(ts[i]);
		}
		return gs;
	}

	protected Generic getArgument(TypeVariable<?> var) {
		initArgs();
		Generic arg = argMap.get(var);
		if (arg == null) {
			if(getOwnerType() != null){
				return getOwnerType().getArgument(var);
			}
			throw new UnsupportedOperationException(var.toString());
		}
		return arg;
	}

	private void initArgs() {
		if (argMap == null) {
			TypeVariable<?>[] vars = getRawType().getTypeParameters();
			argMap = new LinkedHashMap<TypeVariable<?>, Generic>();
			args = new Generic[vars.length];
			for (int i = 0; i < vars.length; i++) {
				GenericArgument arg = new GenericArgument();
				argMap.put(vars[i], arg);
				Generic[] bounds = subst(vars[i].getBounds());
				args[i] = arg.init(bounds, p == null ? null : subst(p.getActualTypeArguments()[i])).collapse();
				argMap.put(vars[i], args[i]);
			}
		}
	}

	private Generic subst(Type t) {
		if(t instanceof Generic){
			throw new UnsupportedOperationException();
		}
		if (t instanceof TypeVariable) {
			return getArgument((TypeVariable<?>) t);
		}
		if (t instanceof Class) {
			return Generics.genericOf(t);
		}
		if (t instanceof ParameterizedType) {
			ParameterizedType pt = (ParameterizedType)t;
			Generic[] st = subst(pt.getActualTypeArguments());
			GenericType outer = pt.getOwnerType() == null ? null : (GenericType) subst(pt.getOwnerType());
			return new GenericType((Class<?>) pt.getRawType(), outer, st);
		}
		if (t instanceof WildcardType) {
			WildcardType w = (WildcardType) t;
			Generic[] lower = subst(w.getLowerBounds());
			Generic[] upper = subst(w.getUpperBounds());
			return new GenericArgument(upper, lower).collapse();
		}
		if (t instanceof GenericArrayType) {
			GenericArrayType a = (GenericArrayType) t;
			return Generics.arrayOf(subst(a.getGenericComponentType()));
		}
		throw new UnsupportedOperationException("Unknown type: " + t.getClass());
	}

	@Override
	public Generic[] interfaces() {
		if (is == null) {
			is = subst(getRawType().getGenericInterfaces());
		}
		return is;
	}

	@Override
	public boolean isAssignableTo(Generic type) {
		if (this.equals(type)) {
			return true;
		}
		if(type instanceof GenericArgument){
			return ((GenericArgument)type).contains(this);
		}
		if (type instanceof GenericType) {
			GenericType pt = (GenericType) type;
			if (getRawType().equals(pt.getRawType())) {
				for (int i = 0; i < pt.getActualTypeArguments().length; i++) {
					if (!getActualTypeArguments()[i].isAssignableTo(pt
							.getActualTypeArguments()[i])) {
						return false;
					}
				}
				return true;
			}
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
	public GenericType getOwnerType() {
		return owner;
	}

	@Override
	public Generic[] getActualTypeArguments() {
		initArgs();
		return args;
	}

	@Override
	public String toString() {
		if (p != null) {
			return p.toString();
		}
		if (getActualTypeArguments().length == 0) {
			return getRawType().getCanonicalName();
		}
		String argString = Arrays.toString(getActualTypeArguments());
		return getRawType().getCanonicalName() + "<"
				+ argString.substring(1, argString.length() - 1) + ">";
	}

	@Override
	public boolean equals(Object obj) {
		try {
			ParameterizedType o = (ParameterizedType) obj;
			return getRawType().equals(o.getRawType())
					&& getOwnerType().equals(o.getOwnerType())
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
