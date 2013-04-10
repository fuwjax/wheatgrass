package org.fuwjin.wheatgrass;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Map;

import javax.inject.Named;
import javax.inject.Qualifier;
import javax.inject.Singleton;

import org.fuwjin.util.AnnotationBuilder;
import org.fuwjin.util.AnnotationHandler;
import org.fuwjin.util.Types;
import org.fuwjin.wheatgrass.context.ConstructorContext;
import org.fuwjin.wheatgrass.type.Generic;
import org.fuwjin.wheatgrass.type.StandardGeneric;

/**
 * Utility class for Wheatgrass. This class is primarily used to generate
 * {@link StandardInjector} instances through the {@link Wheatgrass#newInjector()}
 * method. Additionally there are utility methods for creating
 * {@link Annotation} instances as well as {@link Key}s.
 * 
 * @author fuwjax
 * 
 */
public final class Wheatgrass {
	private Wheatgrass() {
		throw new RuntimeException("Wheatgrass is a utility class only");
	}

	/**
	 * Creates a new builder for the creation of {@link Annotation} instances.
	 * Note that these instances are not intended to be used as surrogates for
	 * the real thing. This method exists primarily for producing
	 * equality-equivalent annotations for testing {@link Qualifier}s.
	 * 
	 * @param type
	 *            the annotation type
	 * @return the annotation builder
	 * @see AnnotationBuilder
	 */
	public static <A extends Annotation> AnnotationBuilder<A> buildAnnotation(Class<A> type) {
		assert type != null;
		return new AnnotationBuilder<A>(type);
	}

	public static <A extends Annotation> A newAnnotation(Class<A> type, Map<String, Object> values){
		assert type != null;
		assert values != null;
		return Types.proxy(type, new AnnotationHandler(type, values));
	}
	
	public static <A extends Annotation> A newAnnotation(Class<A> type){
		assert type != null;
		return Types.proxy(type, new AnnotationHandler(type, Collections.<String,Object>emptyMap()));
	}

	/**
	 * Creates a new builder for the creation of {@link StandardInjector} instances.
	 * This instance is bound to the {@link Singleton} scope and supports
	 * reflective constructor instantiation.
	 * 
	 * @return the injector builder
	 * @see RootInjectorBuilder
	 */
	public static Injector newInjector() {
		return new StandardInjector(Singleton.class, null, new ConstructorContext());
	}
	
	public static InjectorBuilder buildInjector(){
		return newInjector().newInjector();
	}

	public static InjectorBuilder buildInjector(Class<? extends Annotation> scope){
		return newInjector().newInjector(scope);
	}

	/**
	 * Creates a new {@link Key} with no generic type.
	 * 
	 * @param type
	 *            the key type
	 * @param annotations
	 *            the key annotations
	 * @return the key
	 */
	public static <T> Key<T> keyOf(Class<T> type, Annotation... annotations) {
		assert type != null;
		assert Types.nestedNotNull(annotations);
		return keyOf(typeOf(type), annotations);
	}
	
	public static <T> Key<T> keyOf(Generic<T> type, Annotation... annotations) {
		assert type != null;
		assert Types.nestedNotNull(annotations);
		return new StandardKey<T>(type, annotations);
	}
	
	public static <T> Generic<T> typeOf(Class<T> type){
		return new StandardGeneric<T>(type);
	}

	public static <T> Generic<T> typeOf(Class<T> type, Type genericType){
		return new StandardGeneric<T>(type, genericType);
	}

	public static Generic<?> typeOf(Type type){
		return typeOf(Types.resolve(type), type);
	}

	/**
	 * Creates a new {@link Key}.
	 * 
	 * @param type
	 *            the key type
	 * @param genericType
	 *            the key generic type
	 * @param annotations
	 *            the key annotations
	 * @return the key
	 */
	public static <T> Key<T> keyOf(Class<T> type, Type genericType,
			Annotation... annotations) {
		assert type != null;
		assert genericType != null;
		assert Types.nestedNotNull(annotations);
		return new StandardKey<T>(typeOf(type, genericType), annotations);
	}

	/**
	 * Creates a new {@link Key} with a {@code name}. This name is used to build
	 * a {@link Named} annotation if one was not supplied in the
	 * {@code annotations}.
	 * 
	 * @param name
	 *            the key name. This name will be used to create a new
	 *            {@link Named} annotation if none exists
	 * @param type
	 *            the key type
	 * @param genericType
	 *            the key generic type
	 * @param annotations
	 *            the key annotations
	 * @return the key
	 */
	public static <T> Key<T> keyOf(String name, Class<T> type,
			Type genericType, Annotation... annotations) {
		assert type != null;
		assert genericType != null;
		assert Types.nestedNotNull(annotations);
		for(int i=0;i<annotations.length;i++){
			if(annotations[i] instanceof Named && "".equals(((Named)annotations[i]).value())){
				annotations[i] = named(name);
			}
		}
		return keyOf(type, genericType, annotations);
	}

	/**
	 * Creates a {@link Key} for the {@code method}. This key will use the name,
	 * return type, generic return type, and annotations from the method.
	 * 
	 * @param method
	 *            the method
	 * @return the key
	 */
	public static Key<?> returnTypeOf(Method method) {
		assert method != null;
		return keyOf(method.getName(), method.getReturnType(),
				method.getGenericReturnType(), method.getAnnotations());
	}

	/**
	 * Creates a {@link Key} for the {@code field}. This key will use the name,
	 * type, generic type, and annotations from the field.
	 * 
	 * @param field
	 *            the field
	 * @return the key
	 */
	public static Key<?> typeOf(Field field) {
		assert field != null;
		return keyOf(field.getName(), field.getType(), field.getGenericType(),
				field.getAnnotations());
	}

	/**
	 * Creates a {@link Key} for the {@code constructor}. This key will use the
	 * declaring class and annotations from the constructor.
	 * 
	 * @param constructor
	 *            the constructor
	 * @return the key
	 */
	public static <T> Key<T> typeOf(Constructor<T> constructor) {
		assert constructor != null;
		return keyOf(constructor.getDeclaringClass(),
				constructor.getAnnotations());
	}

	/**
	 * Creates an array of {@link Key}s for the {@code method} arguments. This
	 * key will use the types, generic types, and annotations from the method
	 * parameters. Returns an empty array if there are no parameters.
	 * 
	 * @param method
	 *            the method
	 * @return the key array
	 */
	public static Key<?>[] argsOf(Method method) {
		assert method != null;
		return keysOf(method.getParameterTypes(),
				method.getGenericParameterTypes(),
				method.getParameterAnnotations());
	}

	/**
	 * Creates an array of {@link Key}s for the {@code constructor} arguments.
	 * This key will use the types, generic types, and annotations from the
	 * constructor parameters. Returns an empty array if there are no
	 * parameters.
	 * 
	 * @param constructor
	 *            the constructor
	 * @return the key array
	 */
	public static Key<?>[] argsOf(Constructor<?> constructor) {
		assert constructor != null;
		return keysOf(constructor.getParameterTypes(),
				constructor.getGenericParameterTypes(),
				constructor.getParameterAnnotations());
	}

	/**
	 * Creates an array of {@link Key}s. The types, genericTypes and annotations
	 * arrays must all be the same length, the keys array returned will be of
	 * the same length. Each element of the key array will be built from the
	 * corresponding element of the types, genericTypes and annotations arrays.
	 * 
	 * @param types the types
	 * @param genericTypes the generic types
	 * @param annotations the annotations
	 * @return the key array
	 */
	public static Key<?>[] keysOf(Class<?>[] types, Type[] genericTypes,
			Annotation[][] annotations) {
		assert Types.nestedNotNull(types);
		assert Types.nestedNotNull(genericTypes);
		assert Types.nestedNotNull(annotations);
		final Key<?>[] array = new Key<?>[types.length];
		for (int i = 0; i < array.length; i++) {
			array[i] = keyOf(types[i], genericTypes[i], annotations[i]);
		}
		return array;
	}

	public static Key<?> keyOf(Type type, Annotation... annotations) {
		return keyOf(Types.resolve(type), type, annotations);
	}

	public static Named named(String name) {
		return new AnnotationBuilder<Named>(Named.class).with("value", name).build();
	}

	public static Generic<?>[] typesOf(Type[] types) {
		assert Types.nestedNotNull(types);
		final Generic<?>[] array = new Generic<?>[types.length];
		for (int i = 0; i < array.length; i++) {
			array[i] = typeOf(types[i]);
		}
		return array;
	}

	public static <T> Key<T> keyOf(String name, Class<T> type) {
	   return keyOf(type, named(name));
   }
}
