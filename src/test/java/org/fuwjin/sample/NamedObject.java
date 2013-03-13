package org.fuwjin.sample;

import javax.inject.Inject;
import javax.inject.Named;

public class NamedObject {
	@Inject @Named("name") String name;
	
	@Override
	public String toString() {
		return "name = "+name;
	}
}
