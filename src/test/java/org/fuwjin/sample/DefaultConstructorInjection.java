package org.fuwjin.sample;


public class DefaultConstructorInjection {
	private SimpleObject sample;
	
	public DefaultConstructorInjection(){
		this(new SimpleObject("default constructor"));
	}
	
	public DefaultConstructorInjection(SimpleObject sample){
		this.sample = sample;
	}
	
	@Override
	public String toString() {
		return "default constructor = "+sample;
	}

}
