package org.fuwjin.wheatgrass.type;


public class StandardConversion<T> extends GenericAdapter<T>{
	private String priority;

	public static class Builder<T>{
		private Generic<T> type;
		private StringBuilder priority = new StringBuilder();

		public Builder(Generic<T> type) {
			this.type = type;
		}

		public Generic<T> build() {
			if(priority.length() == 0){
				return type;
			}
			return new StandardConversion<T>(type, priority.toString());
		}

		public Builder<T> superInterfaceConversion() {
			priority.append(6);
			return this;
		}

		public Builder<T> superClassConversion() {
			priority.append(5);			
			return this;
		}

		public Builder<T> rawTypeConversion() {
			priority.append(4);
			return this;
		}

		public Builder<T> uncheckedConversion() {
			priority = new StringBuilder(priority.append(8).toString().replaceAll(".", "8"));
			return this;
		}

		public Builder<T> captureConversion() {
			priority.append(3);
			return this;
		}

		public Builder<T> containsConversion() {
			priority.append(3);
			return this;
		}

		public Builder<T> boxConversion() {
			priority.append(2);
			return this;
		}

		public Builder<T> narrowingConversion() {
			priority = new StringBuilder(priority.append(7).toString().replaceAll(".", "7"));
			return this;
		}
	}
	
	public StandardConversion(Generic<T> key, String priority) {
		super(key);
		this.priority = priority;
	}
	
	@Override
	public String conversionId() {
		return priority;
	}

	@Override
	public T cast(Object object) {
		return type().cast(object);
	}
}
