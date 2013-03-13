package org.fuwjin.util;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.Map;

public class Types {
	public static <T extends AccessibleObject> T access(T obj) {
		if(!obj.isAccessible()){
			obj.setAccessible(true);
		}
		return obj;
	}

	public static Type parameter(Type generic, int index) {
		return ((ParameterizedType)generic).getActualTypeArguments()[index];
	}
	
	public static <T> T proxy(Class<T> type, InvocationHandler handler){
		return type.cast(Proxy.newProxyInstance(type.getClassLoader(), new Class<?>[]{type}, handler));
	}
	
	public static boolean moreSpecific(Class<?> target, Class<?> current, Class<?> test){
		if(target == null){
			return false;
		}
		if(test == null || !target.isAssignableFrom(test)){
			return false;
		}
		if(current == null || !target.isAssignableFrom(current)){
			return true;
		}
		if(current.isAssignableFrom(test)){
			return false;
		}
		if(test.isAssignableFrom(current)){
			return true;
		}
		if(current.isInterface() && !test.isInterface()){
			return true;
		}
		return false;
	}

	public static Class<?> resolve(Type type) {
		if(type instanceof Class){
			return (Class<?>)type;
		}
		throw new UnsupportedOperationException("resolve not supported for "+type.getClass());
	}

	public static boolean nestedNotNull(Object object) {
		if(object == null){
			return false;
		}
		if(object instanceof Object[]){
			for(Object o: (Object[])object){
				if(!nestedNotNull(o)){
					return false;
				}
			}
		}else if(object instanceof Iterable){
			for(Object o: (Iterable<?>)object){
				if(!nestedNotNull(o)){
					return false;
				}
			}
		}else if(object instanceof Map){
			for(Map.Entry<?,?> o: ((Map<?,?>)object).entrySet()){
				if(!nestedNotNull(o.getKey()) || !nestedNotNull(o.getValue())){
					return false;
				}
			}
		}
		return true;
	}
}
