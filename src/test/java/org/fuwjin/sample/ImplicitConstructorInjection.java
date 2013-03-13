package org.fuwjin.sample;


public class ImplicitConstructorInjection {
	private SimpleObject sample;
	
	public ImplicitConstructorInjection(SimpleObject sample){
		this.sample = sample;
	}
	
	@Override
	public String toString() {
		return "implicit constructor = "+sample;
	}

}
