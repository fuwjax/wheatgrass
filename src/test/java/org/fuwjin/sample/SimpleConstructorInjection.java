package org.fuwjin.sample;

import javax.inject.Inject;

public class SimpleConstructorInjection {
	private SimpleObject sample;
	
	@Inject
	public SimpleConstructorInjection(SimpleObject sample){
		this.sample = sample;
	}
	
	@Override
	public String toString() {
		return "simple constructor = "+sample;
	}

}
