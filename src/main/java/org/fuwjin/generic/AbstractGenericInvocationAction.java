package org.fuwjin.generic;


public abstract class AbstractGenericInvocationAction extends AbstractGenericAction {
	public AbstractGenericInvocationAction(Generic type, boolean varArgs, Generic... parameters) {
		super(type, varArgs, parameters);
	}
	
	@Override
	public GenericValue rawValue(Object... arguments) throws Exception {
		Generic[] params = new Generic[arguments.length];
		for(int i=0;i<arguments.length;i++){
			if(arguments[i] instanceof GenericValue){
				GenericValue value = (GenericValue)arguments[i];
				params[i] = value.type();
				arguments[i] = value.value();
			}else{
				params[i] = Generics.genericOf(arguments[i] == null ? null : arguments[i].getClass());
			}
		}
		return call(params).doValue(arguments);
	}
	
	@Override
	public GenericValue value(GenericValue... arguments) throws Exception {
		Object[] args = new Object[arguments.length];
		for(int i=0;i<arguments.length;i++){
			args[i] = arguments[i].value();
		}
		Generic[] params = new Generic[arguments.length];
		for(int i=0;i<arguments.length;i++){
			params[i] = arguments[i].type();
		}
		return call(params).doValue(args);
	}
	
	@Override
	public abstract AbstractGenericAction call(Generic... parameters) throws Exception;
}
