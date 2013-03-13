package org.fuwjin.test;

import static org.fuwjin.wheatgrass.Wheatgrass.buildInjector;
import static org.fuwjin.wheatgrass.Wheatgrass.newInjector;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

import javax.inject.Singleton;

import org.fuwjin.sample.ScopedObject;
import org.fuwjin.sample.SimpleObject;
import org.fuwjin.sample.SimpleScope;
import org.fuwjin.sample.SimpleSingleton;
import org.fuwjin.wheatgrass.Injector;
import org.junit.Test;

public class SameTest {
	@Test
	public void testUniqueInstances() throws Exception{
		Injector injector = newInjector();
		SimpleObject first = injector.get(SimpleObject.class);
		SimpleObject second = injector.get(SimpleObject.class);
		assertNotSame(first, second);
	}
	
	@Test
	public void testSingletonInstance() throws Exception{
		Injector injector = newInjector();
		SimpleSingleton first = injector.get(SimpleSingleton.class);
		SimpleSingleton second = injector.get(SimpleSingleton.class);
		assertSame(first, second);
	}
	
	@Test
	public void testContextInstance() throws Exception{
		Injector injector = buildInjector().withConstants(new SimpleObject()).build();
		SimpleObject first = injector.get(SimpleObject.class);
		SimpleObject second = injector.get(SimpleObject.class);
		assertSame(first, second);
	}
	
	@Test
	public void testSingletonSubinjectorInstance() throws Exception{
		Injector injector = newInjector();
		SimpleSingleton first = injector.get(SimpleSingleton.class);
		SimpleSingleton second = injector.newInjector(Singleton.class).build().get(SimpleSingleton.class);
		assertSame(first, second);
	}
	
	@Test
	public void testSameSubinjectorInstance() throws Exception{
		Injector injector = newInjector().newInjector().withCreateConstants(SimpleObject.class).build();
		SimpleObject first = injector.get(SimpleObject.class);
		SimpleObject second = injector.get(SimpleObject.class);
		assertSame(first, second);
	}
	
	@Test
	public void testUnscopedInstances() throws Exception{
		Injector injector = newInjector();
		ScopedObject first = injector.get(ScopedObject.class);
		ScopedObject second = injector.get(ScopedObject.class);
		assertNotSame(first, second);
	}
	
	@Test
	public void testScopedInstances() throws Exception{
		Injector injector = newInjector().newInjector(SimpleScope.class).build();
		ScopedObject first = injector.get(ScopedObject.class);
		ScopedObject second = injector.get(ScopedObject.class);
		assertSame(first, second);
	}
}
