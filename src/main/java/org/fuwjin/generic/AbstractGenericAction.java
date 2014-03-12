package org.fuwjin.generic;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Array;
import java.util.Arrays;

public abstract class AbstractGenericAction implements GenericAction {
	enum VarArgs{
		YES{
			@Override
			public Object[] fixArgs(Generic[] parameters, Object[] arguments) {
				if(arguments.length < parameters.length - 1){
					throw new IllegalArgumentException("Expected at least "+(parameters.length - 1)+" arguments, received "+arguments.length);
				}
				Object[] args = Arrays.copyOf(arguments, parameters.length);
				int varLength = arguments.length - parameters.length + 1;
				Object vars = Array.newInstance(parameters[parameters.length - 1].getRawType().getComponentType(), varLength);
				args[parameters.length - 1] = vars;
				System.arraycopy(arguments, parameters.length - 1, vars, 0, varLength);
				return args;
			}
		},
		NO{
			@Override
			public Object[] fixArgs(Generic[] parameters, Object[] arguments) {
				if(arguments.length != parameters.length){
					throw new IllegalArgumentException("Expected "+parameters.length+" arguments, received "+arguments.length);
				}
				return arguments;
			}
		};
		public abstract Object[] fixArgs(Generic[] parameters, Object[] arguments);
	}
	
	public static <T extends AccessibleObject> T access(T obj){
		if(!obj.isAccessible()){
			obj.setAccessible(true);
		}
		return obj;
	}

	private Generic type;
	private Generic[] parameters;
	private VarArgs varArgs;

	public AbstractGenericAction(Generic type, Generic... parameters) {
		this.type = type;
		this.parameters = parameters;
		varArgs = VarArgs.NO;
	}
	
	public AbstractGenericAction(Generic type, boolean varArgs, Generic... parameters) {
		this.type = type;
		this.parameters = parameters;
		this.varArgs = varArgs ? VarArgs.YES : VarArgs.NO;
	}
	
	@Override
	public Generic type() {
		return type;
	}

	@Override
	public Generic[] parameters() {
		return parameters;
	}

	@Override
	public AnnotatedElement element() {
		return null;
	}
	
	@Override
	public GenericValue value(GenericValue... arguments) throws Exception {
		Object[] args = new Object[arguments.length];
		for(int i=0;i<arguments.length;i++){
			args[i] = arguments[i].value();
		}
		return doValue(args);
	}
	
	protected GenericValue doValue(Object[] arguments) throws Exception {
		arguments = varArgs.fixArgs(parameters, arguments);
		for(int i=0;i<arguments.length;i++){
			if(!parameters[i].isInstance(arguments[i])){
				throw new IllegalArgumentException("Expected argument "+i+" to be of type "+parameters[i]);
			}
		}
		return type().valueOf(valueImpl(arguments));
	}
	
	protected abstract Object valueImpl(Object... arguments) throws Exception;

	@Override
	public AbstractGenericAction call(Generic... parameters) throws Exception{
		return this;
	}
}
