package org.fuwjin.test;

import static org.fuwjin.wheatgrass.Wheatgrass.buildInjector;
import static org.junit.Assert.assertEquals;

import javax.inject.Provider;

import org.fuwjin.sample.InjectorObject;
import org.fuwjin.sample.ProvidedObject;
import org.fuwjin.wheatgrass.Injector;
import org.junit.Test;

public class ProviderInjectionTest {
	@Test
	public void testProviderInjection() throws Exception{
		Injector injector = buildInjector().withMembers(new Object(){
			Provider<String> names = new Provider<String>(){
				@Override
				public String get() {
					return "provided";
				}
			};
		}).build();
		assertEquals("provided name = provided", injector.get(ProvidedObject.class).toString());
	}

	@Test
	public void testImplicitProviderInjection() throws Exception{
		Injector injector = buildInjector().withMembers(new Object(){
			String name = "provided";
		}).build();
		assertEquals("provided name = provided", injector.get(ProvidedObject.class).toString());
	}

	@Test
	public void testInjectorInjection() throws Exception{
		Injector injector = buildInjector().withMembers(new Object(){
			String name = "inject me";
		}).build();
		assertEquals("injected name = inject me", injector.get(InjectorObject.class).toString());
	}
}
