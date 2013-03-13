package org.fuwjin.sample;

import javax.inject.Inject;

public class SimpleFieldInjection {
	@Inject
	private SimpleObject sample;
	
	@Override
	public String toString() {
		return "simple field = "+sample;
	}
}
