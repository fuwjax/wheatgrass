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

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URI;

import javax.tools.SimpleJavaFileObject;

/**
 * A JavaFileObject for storing bytecode and source in memory.
 */
public abstract class BaseFileObject extends SimpleJavaFileObject {
   private static final String URI_DELIMITER = ":///"; //$NON-NLS-1$
   private final String name;

   protected BaseFileObject(final String name, final Kind kind) {
      super(URI.create(kind + URI_DELIMITER + name.replace('.', '/') + kind.extension), kind);
      this.name = name;
   }

   @Override
   public String getName() {
      return name;
   }
   
   @Override
   public CharSequence getCharContent(final boolean ignoreEncodingErrors) throws IOException {
   	StringBuilder builder = new StringBuilder();
      BufferedReader reader = new BufferedReader(openReader(ignoreEncodingErrors));
      String line = reader.readLine();
      while(line != null){
      	builder.append(line).append('\n');
      	line = reader.readLine();
      }
      return builder;
   }
}
