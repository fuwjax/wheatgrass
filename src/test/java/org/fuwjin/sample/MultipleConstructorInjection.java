package org.fuwjin.sample;

import javax.inject.Inject;

public class MultipleConstructorInjection {
	private SimpleObject sample;
	
	public MultipleConstructorInjection(){
		this(new SimpleObject("default constructor"));
	}
	
	@Inject
	public MultipleConstructorInjection(SimpleObject sample){
		this.sample = sample;
	}
	
	@Override
	public String toString() {
		return "multiple constructor = "+sample;
	}

}
