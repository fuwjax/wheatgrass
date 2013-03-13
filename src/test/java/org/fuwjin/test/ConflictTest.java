package org.fuwjin.test;

import static org.fuwjin.wheatgrass.Wheatgrass.keyOf;
import static org.fuwjin.wheatgrass.Wheatgrass.named;
import static org.fuwjin.wheatgrass.Wheatgrass.buildInjector;
import static org.junit.Assert.assertEquals;

import javax.inject.Named;
import javax.inject.Provider;

import org.fuwjin.sample.SimpleInterface;
import org.fuwjin.sample.SimpleObject;
import org.fuwjin.sample.SimpleQualifier;
import org.fuwjin.wheatgrass.Provides;
import org.junit.Test;

public class ConflictTest {
	private static final Named NAMED_SAMPLE1 = named("sample1");

	@Test
	public void testTypeSpecificity() throws Exception {
		SimpleInterface obj = buildInjector().withMembers(new Object(){
			SimpleObject sample1 = new SimpleObject("sample1");
			SimpleInterface sample2 = new SimpleObject("sample2");
		}).build().get(SimpleInterface.class);
		assertEquals("sample2", obj.toString());
	}
	
	@Test
	public void testExplicitNaming() throws Exception {
		SimpleInterface obj = buildInjector().withMembers(new Object(){
			SimpleObject sample1 = new SimpleObject("sample1");
			@Named("sample1")
			SimpleInterface sample2 = new SimpleObject("sample2");
		}).build().get(keyOf(SimpleInterface.class, NAMED_SAMPLE1));
		assertEquals("sample2", obj.toString());
	}
	
	@Test
	public void testExtraQualifier() throws Exception {
		SimpleInterface obj = buildInjector().withMembers(new Object(){
			@SimpleQualifier
			SimpleInterface sample1 = new SimpleObject("sample1");
			@Named("sample1")
			SimpleInterface sample2 = new SimpleObject("sample2");
		}).build().get(keyOf(SimpleInterface.class, NAMED_SAMPLE1));
		assertEquals("sample2", obj.toString());
	}

	@Test
	public void testFieldMethodSpecificityConflict() throws Exception {
		SimpleInterface obj = buildInjector().withMembers(new Object(){
			SimpleObject sample1 = new SimpleObject("sample1");
			@Provides
			SimpleInterface sample2(){
				return new SimpleObject("sample2");
			}
		}).build().get(SimpleInterface.class);
		assertEquals("sample2", obj.toString());
	}
	
	@Test(expected=RuntimeException.class)
	public void testFieldMethodConflict() throws Exception {
		SimpleInterface obj = buildInjector().withMembers(new Object(){
			SimpleInterface sample1 = new SimpleObject("sample1");
			@Provides
			SimpleInterface sample2(){
				return new SimpleObject("sample2");
			}
		}).build().get(SimpleInterface.class);
	}
	
	@Test
	public void testFieldProviderSpecificityConflict() throws Exception {
		SimpleInterface obj = buildInjector().withMembers(new Object(){
			SimpleObject sample1 = new SimpleObject("sample1");
			Provider<SimpleInterface> sample2 = new Provider<SimpleInterface>(){
				@Override
				public SimpleInterface get() {
					return new SimpleObject("sample2");
				}
			};
		}).build().get(SimpleInterface.class);
		assertEquals("sample2", obj.toString());
	}
	
	@Test(expected=RuntimeException.class)
	public void testFieldProviderConflict() throws Exception {
		SimpleInterface obj = buildInjector().withMembers(new Object(){
			SimpleInterface sample1 = new SimpleObject("sample1");
			Provider<SimpleInterface> sample2 = new Provider<SimpleInterface>(){
				@Override
				public SimpleInterface get() {
					return new SimpleObject("sample2");
				}
			};
		}).build().get(SimpleInterface.class);
	}
}
