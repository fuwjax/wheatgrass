package org.fuwjin.wheatgrass;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Marker annotation indicating that a method should be used as a binding in a
 * reflective member context.
 * 
 * @author fuwjax
 * @see RootInjectorBuilder#withMembers(Object...)
 */
@Target(METHOD)
@Retention(RUNTIME)
@Documented
public @interface Provides {

}
