package org.fuwjin.sample;

import javax.inject.Inject;
import javax.inject.Provider;

public class ProvidedObject {
	@Inject
	private Provider<String> name;
	
	@Override
	public String toString() {
		return "provided name = "+name.get();
	}
}
