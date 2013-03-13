package org.fuwjin.wheatgrass.type;

import static java.util.Arrays.asList;
import static org.fuwjin.wheatgrass.Wheatgrass.typeOf;
import static org.fuwjin.wheatgrass.Wheatgrass.typesOf;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class StandardGeneric<T> implements Generic<T> {
	private Class<T> type;
	private Type genericType;

	public StandardGeneric(Class<T> type) {
		this(type, type);
	}

	public StandardGeneric(Class<T> type, Type genericType) {
		this.type = type;
		this.genericType = genericType;
	}

	@Override
	public Class<T> type() {
		return type;
	}

	@Override
	public Type genericType() {
		return genericType;
	}

	@Override
	public Generic<?>[] parameters() {
		if(genericType instanceof ParameterizedType){
			return typesOf(((ParameterizedType)genericType).getActualTypeArguments());
		}
		return new Generic<?>[0];
	}

	@Override
	public Generic<?> superclass() {
		return substitute(typeOf(type.getGenericSuperclass()));
	}

	protected Generic<?> substitute(Generic<?> type) {
		//TODO
		return type;
	}

	@Override
	public Generic<?>[] interfaces() {
		return typesOf(type.getGenericInterfaces());
	}

	@Override
	public boolean contains(Generic<?> key) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean captures(Generic<?> key) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Generic<T> convertFrom(Generic<?> source) {
		StandardConversion.Builder<T> cast = new StandardConversion.Builder<T>(this);
		if(genericType.equals(source.genericType())){
			return cast.build();
		}
		if(Object.class.equals(type)){
			return null;
		}
		PrimitiveConverter primitive = PrimitiveConverter.valueOf(source.type());
		if(primitive != null){
			Generic<T> converter = primitive.convertTo(this);
			if(converter != null){
				return converter;
			}
		}
		if(!type.isAssignableFrom(source.type())){
			return null;
		}
		if(type.isInterface()){
			List<Generic<?>> interfaces = new ArrayList<Generic<?>>();
			interfaces.add(source);
			while(!interfaces.isEmpty()){
				Generic<?> iface = interfaces.remove(0);
				if(iface.type().isInterface()){
					if(type.equals(iface.type())){
						source = iface;
						break;
					}
					interfaces.addAll(asList(iface.interfaces()));
				}else{
					if(Object.class.equals(iface.type())){
						return null;
					}
					cast.superInterfaceConversion();
					interfaces.addAll(asList(iface.interfaces()));
					interfaces.add(iface.superclass());
				}
			}
		}else{
			while(!type.equals(source.type())){
				cast.superClassConversion();
				source = source.superclass();
			}
		}
		if(genericType.equals(source.genericType())){
			return cast.build();
		}
		if(source.type().equals(genericType)){
			return cast.rawTypeConversion().build();
		}
		if(type.equals(source.genericType())){
			return cast.uncheckedConversion().build();
		}
		if(source.captures(this)){
			return cast.captureConversion().build();
		}
		Generic<?>[] parameters = this.parameters();
		Generic<?>[] sourceParams = source.parameters();
		for(int i=0;i<parameters.length;i++){
			if(!parameters[i].contains(sourceParams[i])){
				return null;
			}
		}
		return cast.containsConversion().build();
	}

	@Override
	public boolean equals(Object obj) {
		try{
			Generic<?> o = (Generic<?>)obj;
			return genericType.equals(o.genericType());
		}catch(Exception e){
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		return genericType.hashCode();
	}
	
	@Override
	public String toString() {
		return genericType.toString();
	}

	@Override
	public String conversionId() {
		return "1";
	}

	@Override
	public T cast(Object instance) {
		return type.cast(instance);
	}
}
