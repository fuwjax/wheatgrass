package org.fuwjin.util;

import java.lang.annotation.Annotation;
import java.lang.annotation.IncompleteAnnotationException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;

public class AnnotationHandler implements InvocationHandler, Annotation {
	private Class<? extends Annotation> type;
	private Map<String, Object> values;

	public AnnotationHandler(Class<? extends Annotation> type, Map<String, Object> values) {
		this.type = type;
		this.values = values;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		if(method.getDeclaringClass().isAssignableFrom(AnnotationHandler.class)){
			return method.invoke(this, args);
		}
		return valueOf(method);
	}

	private Object valueOf(Method method) {
		Object value = method.getDefaultValue();
		if(values.containsKey(method.getName())){
			return values.get(method.getName());
		}
		if(value == null){
			throw new IncompleteAnnotationException(type, method.getName());
		}
		if(!value.getClass().isArray()){
			return value;
		}
		if(!value.getClass().getComponentType().isPrimitive()){
			return ((Object[])value).clone();
		}
		try{
			return value.getClass().getMethod("clone").invoke(value);
		}catch(Exception e){
			throw new RuntimeException("IMPOSSIBLE: all primitive array types have a clone method", e);
		}
	}

	@Override
	public Class<? extends Annotation> annotationType() {
		return type;
	}
	
	@Override
	public String toString() {
	     StringBuilder builder = new StringBuilder().append('@').append(type.getName()).append('(');
        boolean first = true;
        for (Method method: type.getDeclaredMethods()) {
            if (first){
                first = false;
            }else{
                builder.append(", ");
            }
            builder.append(method.getName()).append('=').append(toString(valueOf(method)));
        }
        return builder.append(')').toString();
  	}
	
	private static String toString(Object value) {
		if(!value.getClass().isArray()){
			return value.toString();
		}
		if(!value.getClass().getComponentType().isPrimitive()){
			return Arrays.toString((Object[])value);
		}
		try{
			return (String)Arrays.class.getMethod("toString", value.getClass()).invoke(null, value);
		}catch(Exception e){
			throw new RuntimeException("IMPOSSIBLE: Arrays.toString() exists for all primitive arrays", e);
		}
	}

	private static boolean equals(Object value, Object other) {
		if(!value.getClass().isArray()){
			return value.equals(other);
		}
		if(!value.getClass().getComponentType().isPrimitive()){
			return Arrays.equals((Object[])value, (Object[])other);
		}
		if(!value.getClass().isInstance(other)){
			return false;
		}
		try{
			return (Boolean)Arrays.class.getMethod("equals", value.getClass(), value.getClass()).invoke(null, value, other);
		}catch(Exception e){
			throw new RuntimeException("IMPOSSIBLE: Arrays.equals() exists for all primitive arrays", e);
		}
	}

	private static int hashCode(Object value) {
		if(!value.getClass().isArray()){
			return value.hashCode();
		}
		if(!value.getClass().getComponentType().isPrimitive()){
			return Arrays.hashCode((Object[])value);
		}
		try{
			return (Integer)Arrays.class.getMethod("hashCode", value.getClass()).invoke(null, value);
		}catch(Exception e){
			throw new RuntimeException("IMPOSSIBLE: Arrays.hashCode() exists for all primitive arrays", e);
		}
	}

	@Override
	public boolean equals(Object obj) {
		if(!type.isInstance(obj)){
			return false;
		}
		for(Method method: type.getDeclaredMethods()){
			try{
				if(!equals(valueOf(method), method.invoke(obj))){
					return false;
				}
			}catch(Exception e){
				return false;
			}
		}
		return true;
	}
	
	@Override
	public int hashCode() {
		int hash = 0;
		for(Method method: type.getDeclaredMethods()){
			hash += 127 * method.getName().hashCode() ^ hashCode(valueOf(method));
		}
		return hash;
	}
}
