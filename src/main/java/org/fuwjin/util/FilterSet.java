package org.fuwjin.util;

import java.util.AbstractSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;


public class FilterSet<T> extends AbstractSet<T>{
	private Set<T> set;
	private Constraint<T> constraint;
	public FilterSet(){
		this(Constraints.<T>anything(), new HashSet<T>());
	}
	
	public FilterSet(Constraint<T> constraint){
		this(constraint, new HashSet<T>());
	}
	
	public FilterSet(Constraint<T> constraint, Set<T> set){
		this.constraint = constraint;
		this.set = set;
	}

	public FilterSet<T> filter(Constraint<T> constraint){
		return new FilterSet<T>(constraint, set);
	}
	
	public T first(){
		Iterator<T> iter = iterator();
		if(iter.hasNext()){
			return iter.next();
		}
		return null;
	}
	
	@Override
	public boolean add(T e) {
		if(!constraint.satisfies(e)){
			return false;
		}
		return set.add(e);
	}

	@Override
	public Iterator<T> iterator() {
		return new Iterator<T>(){
			private Iterator<T> iter = set.iterator();
			@Override
			public boolean hasNext() {
				return iter.hasNext();
			}

			@Override
			public T next() {
				if(!hasNext()){
					throw new NoSuchElementException();
				}
				T t = iter.next();
				while(!constraint.satisfies(t)){
					if(!hasNext()){
						throw new NoSuchElementException();
					}
					t = iter.next();
				}
				return t;
			}

			@Override
			public void remove() {
				iter.remove();
			}
		};
	}

	@Override
	public int size() {
		int count = 0;
		for(T t: this){
			count++;
		}
		return count;
	}

	@Override
	public boolean isEmpty() {
		for(T t: this){
			return false;
		}
		return true;
	}
}
