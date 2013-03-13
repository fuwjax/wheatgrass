/*******************************************************************************
 * Copyright (c) 2010 Michael Doberenz.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Michael Doberenz - initial API and implementation
 ******************************************************************************/
package org.fuwjin.util;

import static javax.tools.ToolProvider.getSystemJavaCompiler;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.JavaFileObject.Kind;
import javax.tools.StandardJavaFileManager;

/**
 * A ClassLoader which compiles source code at runtime in memory.
 */
public class RuntimeClassLoader extends ClassLoader {
   private final ConcurrentMap<String, BufferedFileObject> files = new ConcurrentHashMap<String, BufferedFileObject>();
   private final ForwardingJavaFileManager<StandardJavaFileManager> manager = new ForwardingJavaFileManager<StandardJavaFileManager>(
         getStandardFileManager()) {
      @Override
      public JavaFileObject getJavaFileForOutput(final Location location, final String className, final Kind kind,
            final FileObject sibling) throws IOException {
         return getFile(className);
      }
   };

   /**
    * Compiles the {@code source} java code into the class {@code name}.
    * @param sources the java source code
    * @return true if the source code compiled, false otherwise
    */
   public boolean compile(final Map<String, String> sources) {
      final Set<BufferedFileObject> compUnit = new HashSet<BufferedFileObject>();
      for(final Map.Entry<String, String> entry: sources.entrySet()) {
         compUnit.add(new BufferedFileObject(entry.getKey(), entry.getValue()));
      }
      final JavaCompiler compiler = getSystemJavaCompiler();
      final OutputStreamWriter log = new OutputStreamWriter(System.err);
      return compiler.getTask(log, manager, null, null, null, compUnit).call();
   }

   @Override
   protected Class<?> findClass(final String name) throws ClassNotFoundException {
      final BufferedFileObject file = files.get(name);
      if(file == null) {
         throw new ClassNotFoundException(name);
      }
      final byte[] b = file.getBytes();
      return defineClass(name, b, 0, b.length);
   }

   /**
    * Returns the file object for the {@code className}.
    * @param className the name of the file object
    * @return the file object
    */
   JavaFileObject getFile(final String className) {
      final BufferedFileObject file = new BufferedFileObject(className);
      files.put(className, file);
      return file;
   }

   private StandardJavaFileManager getStandardFileManager() {
      return getSystemJavaCompiler().getStandardFileManager(null, null, null);
   }

	public boolean compile(String name, String source) {
		return compile(name, source, System.err);
	}

	public boolean compile(String name, String source, OutputStream logStream) {
	      final Set<BufferedFileObject> compUnit = new HashSet<BufferedFileObject>();
       compUnit.add(new BufferedFileObject(name, source));
	      final JavaCompiler compiler = getSystemJavaCompiler();
	      final Writer log = logStream == null ? new StringWriter() : new OutputStreamWriter(logStream);
	      return compiler.getTask(log, manager, null, null, null, compUnit).call();
	}
}
