package org.fuwjin.generic;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Generics {
	public static final Generic[] NONE = new Generic[0];

	public static final GenericType TOP = new GenericType(null, null){
		public boolean isAssignableTo(Generic type) {
			return false;
		}
	};

	public static final GenericType OBJECT = new GenericType(Object.class);
	public static final PrimitiveType BOOLEAN = new PrimitiveType(boolean.class);
	public static final PrimitiveType BYTE = new PrimitiveType(byte.class);
	public static final PrimitiveType CHAR = new PrimitiveType(char.class);
	public static final PrimitiveType SHORT = new PrimitiveType(short.class);
	public static final PrimitiveType INT = new PrimitiveType(int.class);
	public static final PrimitiveType LONG = new PrimitiveType(long.class);
	public static final PrimitiveType FLOAT = new PrimitiveType(float.class);
	public static final PrimitiveType DOUBLE = new PrimitiveType(double.class);
	public static final PrimitiveType VOID = new PrimitiveType(void.class);
	
	private static Map<Class<?>, Generic> standardGenerics = new HashMap<Class<?>, Generic>();
	private static Map<Generic, GenericArray> standardArrays = new HashMap<Generic, GenericArray>();
	static{
		GenericType serializable = new GenericType(Serializable.class);
		GenericType cloneable = new GenericType(Cloneable.class);
		
		WrapperType booleanWrapper = new WrapperType(Boolean.class, BOOLEAN);
		WrapperType byteWrapper = new WrapperType(Byte.class, BYTE);
		WrapperType charWrapper = new WrapperType(Character.class, CHAR);
		WrapperType shortWrapper = new WrapperType(Short.class, SHORT);
		WrapperType intWrapper = new WrapperType(Integer.class, INT);
		WrapperType longWrapper = new WrapperType(Long.class, LONG);
		WrapperType floatWrapper = new WrapperType(Float.class, FLOAT);
		WrapperType doubleWrapper = new WrapperType(Double.class, DOUBLE);
		WrapperType voidWrapper = new WrapperType(Void.class, VOID);

		BOOLEAN.setWrapper(booleanWrapper);
		BYTE.setWrapper(byteWrapper);
		CHAR.setWrapper(charWrapper);
		SHORT.setWrapper(shortWrapper);
		INT.setWrapper(intWrapper);
		LONG.setWrapper(longWrapper);
		FLOAT.setWrapper(floatWrapper);
		DOUBLE.setWrapper(doubleWrapper);
		VOID.setWrapper(voidWrapper);

		BOOLEAN.setSuper(TOP);
		BYTE.setSuper(SHORT);
		CHAR.setSuper(INT);
		SHORT.setSuper(INT);
		INT.setSuper(LONG);
		LONG.setSuper(FLOAT);
		FLOAT.setSuper(DOUBLE);
		DOUBLE.setSuper(TOP);
		VOID.setSuper(TOP);

		for(Generic type: Arrays.asList(
				BOOLEAN, BYTE, CHAR, SHORT, INT, LONG, FLOAT, DOUBLE, VOID,
				booleanWrapper, byteWrapper, charWrapper, shortWrapper, intWrapper, longWrapper, floatWrapper, doubleWrapper, voidWrapper,
				TOP, OBJECT, serializable, cloneable
				)){
			standardGenerics.put(type.getRawType(), type);
		}

		GenericArray booleanArray = new GenericArray(BOOLEAN, OBJECT, serializable, cloneable);
		GenericArray byteArray = new GenericArray(BYTE, OBJECT, serializable, cloneable);
		GenericArray charArray = new GenericArray(CHAR, OBJECT, serializable, cloneable);
		GenericArray shortArray = new GenericArray(SHORT, OBJECT, serializable, cloneable);
		GenericArray intArray = new GenericArray(INT, OBJECT, serializable, cloneable);
		GenericArray longArray = new GenericArray(LONG, OBJECT, serializable, cloneable);
		GenericArray floatArray = new GenericArray(FLOAT, OBJECT, serializable, cloneable);
		GenericArray doubleArray = new GenericArray(DOUBLE, OBJECT, serializable, cloneable);
		GenericArray objectArray = new GenericArray(OBJECT, OBJECT, serializable, cloneable);
		
		for(GenericArray type: Arrays.asList(
				booleanArray, byteArray, charArray, shortArray, intArray, longArray, floatArray, doubleArray, objectArray
				)){
			standardGenerics.put(type.getRawType(), type);
			standardArrays.put(type.getGenericComponentType(), type);
		}
	}
	
	public static Generic genericOf(Type t){
		if(t instanceof Generic){
			return (Generic)t;
		}
		Generic type = standardGenerics.get(t);
		if(type != null){
			return type;
		}
		if(t instanceof ParameterizedType){
			ParameterizedType p = (ParameterizedType)t;
			return new GenericType(p);
		}
		if(t instanceof Class){
			Class<?> cls = (Class<?>)t;
			if(cls.isArray()){
				return arrayOf(genericOf(cls.getComponentType()));
			}
			return new GenericType(cls);
		}
		throw new UnsupportedOperationException("No generic for "+t.getClass());
	}

	public static GenericArray arrayOf(Generic componentType) {
		GenericArray type = standardArrays.get(componentType);
		if(type != null){
			return type;
		}
		return new GenericArray(componentType);
	}

	public static Generic[] genericsOf(Type[] ts) {
		Generic[] arr = new Generic[ts.length];
		for(int i=0;i<arr.length;i++){
			arr[i] = genericOf(ts[i]);
		}
		return arr;
	}

	public static GenericValue valueOf(Object value) {
		return genericOf(value.getClass()).valueOf(value);
	}
}
