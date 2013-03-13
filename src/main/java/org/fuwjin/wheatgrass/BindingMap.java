package org.fuwjin.wheatgrass;

import static org.fuwjin.wheatgrass.Wheatgrass.typeOf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.fuwjin.wheatgrass.binding.AbstractBinding;
import org.fuwjin.wheatgrass.binding.Binding;
import org.fuwjin.wheatgrass.binding.CastBinding;
import org.fuwjin.wheatgrass.type.Generic;

public class BindingMap {
	private Map<Generic<?>, List<Binding<?>>> bindings = new HashMap<Generic<?>, List<Binding<?>>>();
	
	public <T> boolean add(Generic<T> key, Binding<? extends T> binding){
		if(binding == null){
			addNull(key);
			return false;
		}
		return add(binding);
	}
	
	public void addNull(Generic<?> key){
		listFor(key);
	}
	
	public void addNull(Class<?> type){
		addNull(typeOf(type));
	}

	public boolean add(Binding<?> binding){
		if(binding == null){
			return false;
		}
		List<Binding<?>> list = listFor(binding.key().type());
		if(!list.contains(binding)){
			list.add(binding);
			return true;
		}
		return false;
	}

	protected List<Binding<?>> listFor(Generic<?> type) {
		List<Binding<?>> list = bindings.get(type);
		if(list == null){
			list = new ArrayList<Binding<?>>();
			bindings.put(type, list);
		}
		return list;
	}
	
	public <T> Binding<T> bind(Key<T> key){
		Binding<T> best = null;
		Generic<T> cast = key.type();
		List<Binding<?>> list = bindings.get(key.type());
		if(list != null){
			for(Binding<?> binding: list){
				best = betterOf(key, best, cast, binding);
			}
		}
		if(best != null){
			return best;
		}
		for(Map.Entry<Generic<?>, List<Binding<?>>> entry: bindings.entrySet()){
			cast = key.type().convertFrom(entry.getKey());
			if(cast != null){
				for(Binding<?> binding: entry.getValue()){
					best = betterOf(key, best, cast, binding);
				}
			}
		}
		add(key.type(), best);
		return best;
	}
	
	protected <T> Binding<T> betterOf(Key<T> key, final Binding<T> current, Generic<T> cast, Binding<?> binding){
		if(!key.isQualified(binding.key())){
			return current;
		}
		final Binding<T> test = CastBinding.bind(cast, binding);
		if(current == null){
			return test;
		}
		int c = cast.conversionId().compareTo(current.key().type().conversionId());
		if(c < 0){
			return test;
		}
		if(c > 0){
			return current;
		}
		return new AbstractBinding<T>(current.key()){
			@Override
			public T get(Injector root) throws Exception {
				throw new RuntimeException("Could not differentiate between "+current+" and "+test);
			}
		};
	}
	
	protected <T> Binding<T> bestOf(Key<T> key, Binding<T> current, Iterable<? extends Binding<?>> bindings){
		for(Binding<?> binding: bindings){
			Generic<T> cast = key.type().convertFrom(binding.key().type());
			if(cast != null){
				return betterOf(key, current, cast, binding);
			}
		}
		return current;
	}
	
	public boolean isBound(Class<?> type){
		return bindings.containsKey(type);
	}
	
	public boolean isEmpty() {
		return bindings.isEmpty();
	}

}
