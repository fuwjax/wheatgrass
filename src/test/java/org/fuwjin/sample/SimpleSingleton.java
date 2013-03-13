package org.fuwjin.sample;

import javax.inject.Singleton;

@Singleton
public class SimpleSingleton {
	private String message;
	
	public SimpleSingleton(){
		this("simple sample");
	}

	public SimpleSingleton(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return message;
	}
}
