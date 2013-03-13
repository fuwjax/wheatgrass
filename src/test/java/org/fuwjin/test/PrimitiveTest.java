package org.fuwjin.test;

import static org.junit.Assert.assertEquals;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.fuwjin.generic.Generic;
import org.fuwjin.generic.Generics;
import org.fuwjin.util.RuntimeClassLoader;
import org.junit.Test;

public class PrimitiveTest {
	public boolean booleanValue;
	public byte byteValue;
	public char charValue;
	public short shortValue;
	public int intValue;
	public long longValue;
	public float floatValue;
	public double doubleValue;
	
	public Boolean booleanWrapper;
	public Byte byteWrapper;
	public Character charWrapper;
	public Short shortWrapper;
	public Integer intWrapper;
	public Long longWrapper;
	public Float floatWrapper;
	public Double doubleWrapper;
	
	public boolean[] booleanArray;
	public byte[] byteArray;
	public char[] charArray;
	public short[] shortArray;
	public int[] intArray;
	public long[] longArray;
	public float[] floatArray;
	public double[] doubleArray;
	public Object[] objectArray;
	
	public Boolean[] booleanWrapperArray;
	public Byte[] byteWrapperArray;
	public Character[] charWrapperArray;
	public Short[] shortWrapperArray;
	public Integer[] intWrapperArray;
	public Long[] longWrapperArray;
	public Float[] floatWrapperArray;
	public Double[] doubleWrapperArray;
	
	public Object object;
	public String string;
	public String[] strings;
	public CharSequence charSequence;
	public CharSequence[] charSequences;
	public Cloneable cloneable;
	public Serializable serializable;
	public Number number;
	public Number[] numbers;
	public Serializable[] serializables;
	
	public List<String> stringList;
	public ArrayList<String> stringArrayList;
	public Iterable<String> stringIterable;
	
	public List<? extends String> extendsStringList;
	public List<? extends CharSequence> extendsCharSequenceList;
	public List<? extends CharSequence> extendsCharSequenceAndSerializableList;
	
	@Test
	public void testFields(){
		for(Field from: PrimitiveTest.class.getFields()){
			for(Field to: PrimitiveTest.class.getFields()){
				Generic fromType = Generics.genericOf(from.getGenericType());
				Generic toType = Generics.genericOf(to.getGenericType());
				assertEquals(fromType+" to "+toType, isAssignable(from, to), fromType.isAssignableTo(toType));
			}
		}
	}

	private boolean isAssignable(Field from, Field to) {
		return new RuntimeClassLoader().compile("TestCompile","class TestCompile{"+toString(to.getGenericType())+" m("+toString(from.getGenericType())+" v){return v;}}", null);
	}

	private String toString(Type t) {
		if(t instanceof Class){
			return ((Class<?>)t).getCanonicalName();
		}
		return t.toString();
	}
}
