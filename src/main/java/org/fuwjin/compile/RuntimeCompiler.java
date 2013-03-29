package org.fuwjin.compile;

import java.io.InputStream;
import java.io.Reader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.tools.JavaFileObject;

public class RuntimeCompiler {
	private Set<JavaFileObject> files = new HashSet<>();
	
	public RuntimeCompiler include(String name, byte[] source){
		return include(new BinarySourceFileObject(name, source));
	}

	public RuntimeCompiler include(String name, CharSequence source){
		return include(new SourceFileObject(name, source));
	}
	
	public RuntimeCompiler include(String name, InputStream source){
		return include(new StreamSourceFileObject(name, source));
	}
	
	public RuntimeCompiler include(String name, Reader source){
		return include(new ReaderSourceFileObject(name, source));
	}

	public RuntimeCompiler include(JavaFileObject source) {
		files.add(source);
	   return this;
   }
	
	public Map<String,Class<?>> compile(){
		RuntimeClassLoader loader = new RuntimeClassLoader();
		if(!loader.compile(files)){
			throw new RuntimeException("could not compile files");
		}
		Map<String,Class<?>> classes = new HashMap<String,Class<?>>();
		for(JavaFileObject file: files){
			try{
				classes.put(file.getName(), loader.loadClass(file.getName()));
			}catch(ClassNotFoundException e){
				throw new RuntimeException(e);
			}
		}
		return classes;
	}
}
