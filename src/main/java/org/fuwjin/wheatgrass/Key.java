package org.fuwjin.wheatgrass;

import java.lang.annotation.Annotation;
import java.util.Collection;

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
public interface Key<T> {
	Generic<T> type();
	
	<D> Key<D> forType(Generic<D> other);
	
	boolean isAnnotationPresent(Class<? extends Annotation> annotationType);
	
	Collection<Annotation> qualifiers();
	
	Collection<Annotation> qualifiers(Class<? extends Annotation> annotatedWith);
	
	<A extends Annotation> A qualifier(Class<A> annotationType);

	boolean isQualified(Key<?> key);
}
