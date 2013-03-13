package org.fuwjin.wheatgrass;

import static org.fuwjin.wheatgrass.Wheatgrass.keyOf;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Qualifier;

import org.fuwjin.wheatgrass.binding.Binding;
import org.fuwjin.wheatgrass.type.Generic;

/**
 * A union of type, generic type, and qualifiers used to both search for a
 * {@link Binding} as well as record a binding's key information.
 * 
 * @author fuwjax
 * 
 * @param <T> the type
 */
public class StandardKey<T> implements Key<T>{
	private Generic<T> type;
	private Annotation[] qualifiers;

	/**
	 * Creates a new instance.
	 * @param type the key type
	 * @param genericType the key generic type
	 * @param qualifiers the qualifiers
	 */
	public StandardKey(Generic<T> type, Annotation... qualifiers) {
		this.type = type;
		this.qualifiers = qualifiers;
	}

	public <A extends Annotation> A annotation(Class<A> type) {
		for (Annotation annotation : qualifiers) {
			if (type.isInstance(annotation)) {
				return type.cast(annotation);
			}
		}
		return this.type.type().getAnnotation(type);
	}
	
	public <A extends Annotation> A qualifier(Class<A> type) {
		for (Annotation annotation : qualifiers) {
			if (type.isInstance(annotation)) {
				return type.cast(annotation);
			}
		}
		return null;
	}


	/**
	 * Returns the key type.
	 * @return the key type
	 */
	public Generic<T> type() {
		return type;
	}

	/**
	 * Returns the set of qualifiers.
	 * @return the qualifiers
	 */
	public Collection<Annotation> qualifiers() {
		return Arrays.asList(qualifiers);
	}

	public static boolean isQualifier(Annotation a) {
		return a.annotationType().isAnnotationPresent(Qualifier.class);
	}

	public Key<?> minimal(){
		List<Annotation> annotations = new ArrayList<Annotation>();
		for(Annotation a: qualifiers){
			if(isQualifier(a)){
				annotations.add(a);
			}
		}
		return keyOf(type, annotations.toArray(new Annotation[annotations.size()]));
	}
	
	@Override
	public <D> Key<D> forType(Generic<D> other) {
		return new StandardKey<D>(other, qualifiers);
	}
	
	public boolean isQualified(Key<?> test) {
		int count = 0;
		for(Annotation a: qualifiers){
			if(isQualifier(a)){
				count++;
				if(!a.equals(test.qualifier(a.annotationType()))){
					return false;
				}
			}
		}
		for(Annotation a: test.qualifiers()){
			if(isQualifier(a)){
				count--;
				
			}
		}
		return count == 0;
	}

	@Override
	public Set<Annotation> qualifiers(Class<? extends Annotation> annotatedWith){
		Set<Annotation> set = new HashSet<Annotation>();
		for(Annotation qualifier: qualifiers){
			if(qualifier.annotationType().isAnnotationPresent(annotatedWith)){
				set.add(qualifier);
			}
		}
		return set;
	}
	
	@Override
	public boolean isAnnotationPresent(Class<? extends Annotation> annotationType) {
		for(Annotation qualifier: qualifiers){
			if(annotationType.isInstance(qualifier)){
				return true;
			}
		}
		return type.type().isAnnotationPresent(annotationType);
	}
	
	@Override
	public String toString() {
		return type+Arrays.toString(qualifiers);
	}
}
