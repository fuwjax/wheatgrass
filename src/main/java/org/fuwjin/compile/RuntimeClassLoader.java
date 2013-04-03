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
package org.fuwjin.compile;

import static javax.tools.ToolProvider.getSystemJavaCompiler;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collections;
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
	private static final JavaCompiler compiler = getSystemJavaCompiler();
   private final ConcurrentMap<String, ClassFileObject> files = new ConcurrentHashMap<String, ClassFileObject>();
   private final ForwardingJavaFileManager<StandardJavaFileManager> manager = new ForwardingJavaFileManager<StandardJavaFileManager>(
         compiler.getStandardFileManager(null, null, null)) {
      @Override
      public JavaFileObject getJavaFileForOutput(final Location location, final String className, final Kind kind,
            final FileObject sibling) throws IOException {
         final ClassFileObject file = new ClassFileObject(className, kind);
         ClassFileObject old = files.putIfAbsent(className, file);
         return old == null ? file : old;
      }
   };
   
   private Writer log;
   
   public RuntimeClassLoader(){
      log = new StringWriter();
   }
   
   public RuntimeClassLoader(OutputStream logStream){
   	 log = new OutputStreamWriter(logStream);
   }
   
   public boolean compile(final Path root) throws IOException{
      final Set<SourceFileObject> compUnit = new HashSet<SourceFileObject>();
   	Files.walkFileTree(root, new SimpleFileVisitor<Path>(){
			@Override
         public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				if(file.toString().endsWith(".java")){
					String name = root.relativize(file).toString();
					compUnit.add(new SourceFileObject(name.substring(0, name.length() - 5), new String(Files.readAllBytes(file))));
				}
	         return FileVisitResult.CONTINUE;
         }
   	});
   	return compile(compUnit);
   }

   /**
    * Compiles the {@code source} java code into the class {@code name}.
    * @param sources the java source code
    * @return true if the source code compiled, false otherwise
    */
   public boolean compile(final Map<String, String> sources) {
      final Set<SourceFileObject> compUnit = new HashSet<SourceFileObject>();
      for(final Map.Entry<String, String> entry: sources.entrySet()) {
         compUnit.add(new SourceFileObject(entry.getKey(), entry.getValue()));
      }
      return compile(compUnit);
   }

	public boolean compile(final Set<? extends JavaFileObject> compUnit) {
	   return compiler.getTask(log, manager, null, null, null, compUnit).call();
   }
   
   public boolean compile(String name, String source) {
   	return compile(Collections.singletonMap(name, source));
   }

   @Override
   protected Class<?> findClass(final String name) throws ClassNotFoundException {
      final ClassFileObject file = files.get(name);
      if(file == null) {
         throw new ClassNotFoundException(name);
      }
      final byte[] b = file.toBytes();
      return defineClass(name, b, 0, b.length);
   }
}
