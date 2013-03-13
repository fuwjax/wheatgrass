package org.fuwjin.test;

import static org.fuwjin.wheatgrass.Wheatgrass.buildInjector;
import static org.fuwjin.wheatgrass.Wheatgrass.newInjector;
import static org.junit.Assert.assertEquals;

import javax.inject.Named;
import javax.inject.Provider;
import javax.inject.Singleton;

import org.fuwjin.sample.DefaultConstructorInjection;
import org.fuwjin.sample.ImplicitConstructorInjection;
import org.fuwjin.sample.MultipleConstructorInjection;
import org.fuwjin.sample.NamedObject;
import org.fuwjin.sample.SimpleConstructorInjection;
import org.fuwjin.sample.SimpleFieldInjection;
import org.fuwjin.sample.SimpleInterface;
import org.fuwjin.sample.SimpleObject;
import org.fuwjin.sample.SimpleSetterInjection;
import org.fuwjin.sample.SimpleSingleton;
import org.fuwjin.wheatgrass.Provides;
import org.junit.Test;

public class InjectorTest {
	@Test
	public void testSimpleSample() throws Exception {
		SimpleObject obj = newInjector().get(SimpleObject.class);
		assertEquals("simple sample", obj.toString());
	}

	@Test
	public void testSimpleFieldInjection() throws Exception {
		SimpleFieldInjection obj = newInjector().get(SimpleFieldInjection.class);
		assertEquals("simple field = simple sample", obj.toString());
	}

	@Test
	public void testSimpleSetterInjection() throws Exception {
		SimpleSetterInjection obj = newInjector().get(SimpleSetterInjection.class);
		assertEquals("simple setter = simple sample", obj.toString());
	}

	@Test
	public void testSimpleConstructorInjection() throws Exception {
		SimpleConstructorInjection obj = newInjector().get(SimpleConstructorInjection.class);
		assertEquals("simple constructor = simple sample", obj.toString());
	}

	@Test
	public void testImplicitConstructorInjection() throws Exception {
		ImplicitConstructorInjection obj = newInjector().get(ImplicitConstructorInjection.class);
		assertEquals("implicit constructor = simple sample", obj.toString());
	}

	@Test
	public void testMultipleConstructorInjection() throws Exception {
		MultipleConstructorInjection obj = newInjector().get(MultipleConstructorInjection.class);
		assertEquals("multiple constructor = simple sample", obj.toString());
	}

	@Test
	public void testDefaultConstructorInjection() throws Exception {
		DefaultConstructorInjection obj = newInjector().get(DefaultConstructorInjection.class);
		assertEquals("default constructor = default constructor", obj.toString());
	}

	@Test
	public void testDirectSample() throws Exception {
		SimpleObject obj = buildInjector().withConstants(new SimpleObject("direct")).build().get(SimpleObject.class);
		assertEquals("direct", obj.toString());
	}

	@Test
	public void testDirectFieldInjection() throws Exception {
		SimpleFieldInjection obj = buildInjector().withConstants(new SimpleObject("direct")).build().get(SimpleFieldInjection.class);
		assertEquals("simple field = direct", obj.toString());
	}

	@Test
	public void testDirectSetterInjection() throws Exception {
		SimpleSetterInjection obj = buildInjector().withConstants(new SimpleObject("direct")).build().get(SimpleSetterInjection.class);
		assertEquals("simple setter = direct", obj.toString());
	}

	@Test
	public void testDirectConstructorInjection() throws Exception {
		SimpleConstructorInjection obj = buildInjector().withConstants(new SimpleObject("direct")).build().get(SimpleConstructorInjection.class);
		assertEquals("simple constructor = direct", obj.toString());
	}

	@Test
	public void testSingleton() throws Exception {
		SimpleObject obj = buildInjector().withMembers(new Object(){
			@Singleton SimpleObject sample = new SimpleObject("custom");
		}).build().get(SimpleObject.class);
		assertEquals("custom", obj.toString());
	}

	@Test
	public void testSingletonClass() throws Exception {
		SimpleSingleton obj = buildInjector().withMembers(new Object(){
			SimpleSingleton sample = new SimpleSingleton("singleton");
		}).build().get(SimpleSingleton.class);
		assertEquals("singleton", obj.toString());
	}

	@Test
	public void testSingletonInjection() throws Exception {
		SimpleSetterInjection obj = buildInjector().withMembers(new Object(){
			@Singleton SimpleObject sample = new SimpleObject("custom");
		}).build().get(SimpleSetterInjection.class);
		assertEquals("simple setter = custom", obj.toString());
	}

	@Test
	public void testNonSingletonInjection() throws Exception {
		SimpleSetterInjection obj = buildInjector().withMembers(new Object(){
			SimpleObject sample = new SimpleObject("custom");
		}).build().get(SimpleSetterInjection.class);
		assertEquals("simple setter = custom", obj.toString());
	}

	@Test
	public void testProvides() throws Exception {
		SimpleObject obj = buildInjector().withMembers(new Object(){
			@Provides SimpleObject sample(){
				return new SimpleObject("provided");
			}
		}).build().get(SimpleObject.class);
		assertEquals("provided", obj.toString());
	}

	@Test
	public void testProvidesInjection() throws Exception {
		SimpleFieldInjection obj = buildInjector().withMembers(new Object(){
			@Provides SimpleObject sample(){
				return new SimpleObject("provided");
			}
		}).build().get(SimpleFieldInjection.class);
		assertEquals("simple field = provided", obj.toString());
	}

	@Test
	public void testNonProvidesInjection() throws Exception {
		SimpleFieldInjection obj = buildInjector().withMembers(new Object(){
			SimpleObject sample(){
				return new SimpleObject("provided");
			}
		}).build().get(SimpleFieldInjection.class);
		assertEquals("simple field = simple sample", obj.toString());
	}

	@Test
	public void testInterfaceBindingInjection() throws Exception {
		SimpleInterface obj = buildInjector().withMembers(new Object(){
			native SimpleInterface inject(SimpleObject instead);
		}).build().get(SimpleInterface.class);
		assertEquals("simple sample", obj.toString());
	}

	@Test
	public void testProvider() throws Exception {
		SimpleObject obj = buildInjector().withMembers(new Object(){
			Provider<SimpleObject> provider = new Provider<SimpleObject>(){
				@Override
				public SimpleObject get() {
					return new SimpleObject("provider");
				}
			};
		}).build().get(SimpleObject.class);
		assertEquals("provider", obj.toString());
	}

	@Test
	public void testMethodProvider() throws Exception {
		SimpleObject obj = buildInjector().withMembers(new Object(){
			@Provides
			Provider<SimpleObject> provider(){
				return new Provider<SimpleObject>(){
					@Override
					public SimpleObject get() {
						return new SimpleObject("provider");
					}
				};
			}
		}).build().get(SimpleObject.class);
		assertEquals("provider", obj.toString());
	}

	@Test
	public void testProviderInjection() throws Exception {
		SimpleConstructorInjection obj = buildInjector().withMembers(new Object(){
			Provider<SimpleObject> provider = new Provider<SimpleObject>(){
				@Override
				public SimpleObject get() {
					return new SimpleObject("provider");
				}
			};
		}).build().get(SimpleConstructorInjection.class);
		assertEquals("simple constructor = provider", obj.toString());
	}

	@Test
	public void testNamedInjection() throws Exception {
		NamedObject obj = buildInjector().withMembers(new Object(){
			@Named("name") String objName = "explicit";
		}).build().get(NamedObject.class);
		assertEquals("name = explicit", obj.toString());
	}

	@Test
	public void testImplicitNamedInjection() throws Exception {
		NamedObject obj = buildInjector().withMembers(new Object(){
			@Named() String name = "explicit";
		}).build().get(NamedObject.class);
		assertEquals("name = explicit", obj.toString());
	}
}
