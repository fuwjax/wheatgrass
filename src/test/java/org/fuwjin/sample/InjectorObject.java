package org.fuwjin.sample;

import javax.inject.Inject;

import org.fuwjin.wheatgrass.StandardInjector;

public class InjectorObject {
	@Inject
	private StandardInjector injector;
	
	@Override
	public String toString() {
		try {
			return "injected name = "+injector.get(String.class);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
