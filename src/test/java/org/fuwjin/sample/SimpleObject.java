package org.fuwjin.sample;

public class SimpleObject implements SimpleInterface {
	private String message;
	
	public SimpleObject(){
		this("simple sample");
	}

	public SimpleObject(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return message;
	}
}
