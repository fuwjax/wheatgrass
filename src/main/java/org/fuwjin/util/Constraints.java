package org.fuwjin.util;

public class Constraints {
	public static <T> Constraint<T> anything(){
		return new Constraint<T>(){
			@Override
			public boolean satisfies(T target) {
				return true;
			}
		};
	}

	public static <T> Constraint<T> nothing(){
		return new Constraint<T>(){
			@Override
			public boolean satisfies(T target) {
				return false;
			}
		};
	}

	public static <T> Constraint<T> not(final Constraint<T> constraint){
		return new Constraint<T>(){
			@Override
			public boolean satisfies(T target) {
				return !constraint.satisfies(target);
			}
		};
	}

	// logical OR
	@SafeVarargs
	public static <T> Constraint<T> anyOf(final Constraint<T>... constraints){
		return new Constraint<T>(){
			@Override
			public boolean satisfies(T target) {
				for(Constraint<T> constraint: constraints){
					if(constraint.satisfies(target)){
						return true;
					}
				}
				return false;
			}
		};
	}

	// logical NOR
	@SafeVarargs
	public static <T> Constraint<T> noneOf(final Constraint<T>... constraints){
		return new Constraint<T>(){
			@Override
			public boolean satisfies(T target) {
				for(Constraint<T> constraint: constraints){
					if(constraint.satisfies(target)){
						return false;
					}
				}
				return true;
			}
		};
	}

	// logical AND
	@SafeVarargs
	public static <T> Constraint<T> allOf(final Constraint<T>... constraints){
		return new Constraint<T>(){
			@Override
			public boolean satisfies(T target) {
				for(Constraint<T> constraint: constraints){
					if(!constraint.satisfies(target)){
						return false;
					}
				}
				return true;
			}
		};
	}

	// logical XOR
	@SafeVarargs
	public static <T> Constraint<T> oneOf(final Constraint<T>... constraints){
		return new Constraint<T>(){
			@Override
			public boolean satisfies(T target) {
				boolean exactlyOne = false;
				for(Constraint<T> constraint: constraints){
					if(constraint.satisfies(target)){
						if(exactlyOne){
							return false;
						}
						exactlyOne = true;
					}
				}
				return exactlyOne;
			}
		};
	}
}
