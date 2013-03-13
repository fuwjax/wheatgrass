package org.fuwjin.wheatgrass.context;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.fuwjin.wheatgrass.Key;
import org.fuwjin.wheatgrass.binding.Binding;

public class ConstantContext implements Context{
	private List<Binding<?>> bindings;
	
	public ConstantContext(List<Binding<?>> objects){
		this.bindings = new ArrayList<Binding<?>>(objects);
	}
	
	@Override
	public Collection<? extends Binding<?>> bindings() {
		return bindings;
	}

	@Override
	public Collection<? extends Binding<?>> bindings(Key<?> key) {
		return Collections.emptySet();
	}
}
