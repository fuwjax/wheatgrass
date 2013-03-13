package org.fuwjin.wheatgrass.type;

import java.util.HashMap;
import java.util.Map;


public enum PrimitiveConverter {
	PrimitiveByte(byte.class, Byte.class, short.class, char.class){
		@Override
		protected Object convert(Object value) {
			// value is a byte
			return value;
		}
	},
	PrimitiveShort(short.class, Short.class, int.class){
		@Override
		protected Object convert(Object value) {
			// value is a byte or short
			return ((Number)value).shortValue();
		}
	},
	PrimitiveChar(char.class, Character.class, int.class){
		@Override
		protected Object convert(Object value) {
			// value is a byte or char
			if(value instanceof Number){
				return (char)((Number)value).byteValue();
			}
			return value;
		}
	},
	PrimitiveInt(int.class, Integer.class, long.class){
		@Override
		protected Object convert(Object value) {
			// value is a byte, short, char, or int
			if(value instanceof Character){
				return (int)((Character)value).charValue();
			}
			return ((Number)value).intValue();
		}
	},
	PrimitiveLong(long.class, Long.class, float.class){
		@Override
		protected Object convert(Object value) {
			// value is a byte, short, char, int, or long
			if(value instanceof Character){
				return (long)((Character)value).charValue();
			}
			return ((Number)value).longValue();
		}
	},
	PrimitiveFloat(float.class, Float.class, double.class){
		@Override
		protected Object convert(Object value) {
			// value is a byte, short, char, int, long, or float
			if(value instanceof Character){
				return (float)((Character)value).charValue();
			}
			return ((Number)value).floatValue();
		}
	},
	PrimitiveDouble(double.class, Double.class, null){
		@Override
		protected Object convert(Object value) {
			// value is a byte, short, char, int, long, float, or double
			if(value instanceof Character){
				return (double)((Character)value).charValue();
			}
			return ((Number)value).doubleValue();
		}
	},
	PrimitiveBoolean(boolean.class, Boolean.class, null){
		@Override
		protected Object convert(Object value) {
			// value is a boolean
			return value;
		}
	},
	ByteWrapper(Byte.class, byte.class, Short.class, Character.class){
		@Override
		protected Object convert(Object value) {
			// value is a null or byte
			return value;
		}
	},
	ShortWrapper(Short.class, short.class, Integer.class){
		@Override
		protected Object convert(Object value) {
			// value is a null, byte or short
			return value == null ? null : ((Number)value).shortValue();
		}
	},
	CharacterWrapper(Character.class, char.class, Integer.class){
		@Override
		protected Object convert(Object value) {
			// value is a null, byte or char
			if(value instanceof Number){
				return (char)((Number)value).byteValue();
			}
			return value;
		}
	},
	IntegerWrapper(Integer.class, int.class, Long.class){
		@Override
		protected Object convert(Object value) {
			// value is a null, byte, short, char, or int
			if(value instanceof Character){
				return (int)((Character)value).charValue();
			}
			return value == null ? null : ((Number)value).intValue();
		}
	},
	LongWrapper(Long.class, long.class, Float.class){
		@Override
		protected Object convert(Object value) {
			// value is a null, byte, short, char, int, or long
			if(value instanceof Character){
				return (long)((Character)value).charValue();
			}
			return value == null ? null : ((Number)value).longValue();
		}
	},
	FloatWrapper(Float.class, float.class, Double.class){
		@Override
		protected Object convert(Object value) {
			// value is a null, byte, short, char, int, long or float
			if(value instanceof Character){
				return (float)((Character)value).charValue();
			}
			return value == null ? null : ((Number)value).floatValue();
		}
	},
	DoubleWrapper(Double.class, double.class, null){
		@Override
		protected Object convert(Object value) {
			// value is a null, byte, short, char, int, long, float or double
			if(value instanceof Character){
				return (double)((Character)value).charValue();
			}
			return value == null ? null : ((Number)value).doubleValue();
		}
	},
	BooleanWrapper(Boolean.class, boolean.class, null){
		@Override
		protected Object convert(Object value) {
			// value is a null or boolean
			return value;
		}
	};
	private static final Map<Class<?>, PrimitiveConverter> converters = new HashMap<Class<?>, PrimitiveConverter>();
	static{
		for(PrimitiveConverter c: values()){
			converters.put(c.cls, c);
		}
		for(PrimitiveConverter c: values()){
			c.boxerC = converters.get(c.boxer);
			c.superC = converters.get(c.supertype);
			c.narrowC = converters.get(c.narrow);
		}
	}
	
	private Class<?> cls;
	private Class<?> boxer;
	private Class<?> supertype;
	private Class<?> narrow;
	private PrimitiveConverter boxerC;
	private PrimitiveConverter superC;
	private PrimitiveConverter narrowC;

	private PrimitiveConverter(Class<?> cls, Class<?> boxer, Class<?> supertype){
		this.cls = cls;
		this.boxer = boxer;
		this.supertype = supertype;
	}
	
	private PrimitiveConverter(Class<?> cls, Class<?> boxer, Class<?> supertype, Class<?> narrow){
		this.cls = cls;
		this.boxer = boxer;
		this.supertype = supertype;
		this.narrow = narrow;
	}
	
	public static PrimitiveConverter valueOf(Class<?> cls){
		return converters.get(cls);
	}
	
	protected abstract Object convert(Object value);
	
	public <T> Generic<T> convertTo(final Generic<T> key) {
		final Class<T> type = key.type();
		final StandardConversion.Builder<T> cast = new StandardConversion.Builder<T>(key);
		PrimitiveConverter c = this;
		while(c != null && !c.cls.equals(type)){
			if(c.boxer.equals(type)){
				c = c.boxerC;
				cast.boxConversion();
			}else if(c.narrow != null && c.narrow.equals(type)){
				c = c.narrowC;
				cast.narrowingConversion();
			}else if(c.narrow != null && c.narrowC.boxer.equals(type)){
				c = c.narrowC.boxerC;
				cast.narrowingConversion().boxConversion();
			}else{
				cast.superClassConversion();
				c = c.superC;
			}
		}
		if(c == null){
			return null;
		}
		final PrimitiveConverter conversion = c;
		return new GenericAdapter<T>(cast.build()){
			public T cast(Object object) {
				return super.cast(conversion.convert(object));
			}
		};
	}
}
