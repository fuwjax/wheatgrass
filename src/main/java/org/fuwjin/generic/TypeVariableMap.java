package org.fuwjin.generic;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.LinkedHashMap;
import java.util.Map;

public class TypeVariableMap {
	private Generic[] args;
	private Map<TypeVariable<?>, Generic> argMap;
	private Class<?> raw;

	public TypeVariableMap(Class<?> c) {
		raw = c;
	}

	public TypeVariableMap(ParameterizedType p) {
		raw = (Class<?>)p.getRawType();
	}

	public TypeVariableMap(Class<?> raw, Generic[] args) {
		this.raw =raw;
		this.args = new Generic[args.length];
		argMap = new LinkedHashMap<TypeVariable<?>, Generic>();
		if(args.length > 0){
			TypeVariable<?>[] vars = raw.getTypeParameters();
			for (int i = 0; i < vars.length; i++) {
				GenericArgument arg = new GenericArgument();
				argMap.put(vars[i], arg);
				Generic[] bounds = subst(vars[i].getBounds());
				this.args[i] = arg.init(bounds, args[i]).collapse();
				argMap.put(vars[i], this.args[i]);
			}
		}
	}

	private void initArgs() {
		if (argMap == null) {
			TypeVariable<?>[] vars = raw.getTypeParameters();
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

	private Generic get(TypeVariable<?> var) {
		return null;
	}

	public Generic[] toArray() {
		return null;
	}

	public Generic[] subst(Type[] ts) {
		Generic[] gs = new Generic[ts.length];
		for (int i = 0; i < ts.length; i++) {
			gs[i] = subst(ts[i]);
		}
		return gs;
	}

	public Generic subst(Type t) {
		if(t instanceof Generic){
			throw new UnsupportedOperationException();
		}
		if (t instanceof TypeVariable) {
			return get((TypeVariable<?>) t);
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


}
