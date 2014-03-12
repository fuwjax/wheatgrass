package org.fuwjin.test;

import static org.fuwjin.generic.ActionConstraint.args;
import static org.fuwjin.generic.ActionConstraint.constructor;
import static org.fuwjin.generic.ActionConstraint.field;
import static org.fuwjin.generic.ActionConstraint.method;
import static org.fuwjin.util.Constraints.allOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.fuwjin.generic.ActionConstraint;
import org.fuwjin.generic.Generic;
import org.fuwjin.generic.GenericAction;
import org.fuwjin.generic.GenericTypeLiteral;
import org.fuwjin.generic.GenericValue;
import org.fuwjin.generic.Generics;
import org.junit.Test;

public class ActionTest {
	static class TestAction{
		String name;
	}
	
	static class TestParameter<A>{
		A value;
	}

	static class TestMethodParameter{
		static <A> A echo(A value){
			return value;
		}
	}

	@Test
	public void testActions() throws Exception{
		Generic generic = Generics.genericOf(TestAction.class);
		GenericAction constructor = generic.actions().filter(constructor()).first();
		GenericValue instance = constructor.value();
		GenericAction getField = generic.actions().filter(allOf(field(), args(1))).first();
		GenericAction setField = generic.actions().filter(allOf(field(), args(2))).first();
		assertNull(getField.value(instance).value());
		assertNull(setField.value(instance, Generics.valueOf("bob")).value());
		assertEquals("bob",getField.value(instance).value());
		assertEquals("bob",((TestAction)instance.value()).name);
	}
	
	@Test
	public void testParameterActions() throws Exception{
		Generic generic = new GenericTypeLiteral<TestParameter<String>>(){};
		GenericAction constructor = generic.actions().filter(constructor()).first();
		GenericValue instance = constructor.value();
		GenericAction getField = generic.actions().filter(allOf(field(), args(1))).first();
		GenericAction setField = generic.actions().filter(allOf(field(), args(2))).first();
		assertNull(getField.value(instance).value());
		assertNull(setField.value(instance, Generics.valueOf("bob")).value());
		assertEquals("bob",getField.value(instance).value());
		assertEquals("bob",((TestParameter<String>)instance.value()).value);
	}
	
	@Test
	public void testMethodParameterActions() throws Exception{
		Generic generic = Generics.genericOf(TestMethodParameter.class);
		GenericAction method = generic.actions().filter(method()).first();
		assertEquals("bob",method.value(Generics.valueOf("bob")).value());
	}
}
