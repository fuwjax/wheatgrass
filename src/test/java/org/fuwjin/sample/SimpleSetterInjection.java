package org.fuwjin.sample;

import javax.inject.Inject;

public class SimpleSetterInjection {
	private SimpleObject sample;
	
	@Inject
	public void setSimpleSample(SimpleObject sample){
		this.sample = sample;
	}
	
	@Override
	public String toString() {
		return "simple setter = "+sample;
	}

}
