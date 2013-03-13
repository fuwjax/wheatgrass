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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import javax.tools.SimpleJavaFileObject;

/**
 * A JavaFileObject for storing bytecode and source in memory.
 */
public class BufferedFileObject extends SimpleJavaFileObject {
   private static final String URI_DELIMITER = ":///"; //$NON-NLS-1$
   private ByteArrayOutputStream bytes;
   private final String name;
   private CharSequence source;

   /**
    * Creates a new instance.
    * @param name the class name
    */
   protected BufferedFileObject(final String name) {
      this(name, Kind.CLASS);
      bytes = new ByteArrayOutputStream();
   }

   /**
    * Creates a new instance.
    * @param name the name of the class
    * @param source the java source code
    */
   protected BufferedFileObject(final String name, final CharSequence source) {
      this(name, Kind.SOURCE);
      this.source = source;
   }

   private BufferedFileObject(final String name, final Kind kind) {
      super(URI.create(kind + URI_DELIMITER + name.replace('.', '/') + kind.extension), kind);
      this.name = name;
   }

   /**
    * Returns the bytecode or source bytes for this file object.
    * @return the bytes
    */
   public byte[] getBytes() {
      if(bytes == null) {
         return source.toString().getBytes();
      }
      return bytes.toByteArray();
   }

   @Override
   public CharSequence getCharContent(final boolean ignoreEncodingErrors) throws IOException {
      if(bytes == null) {
         return source;
      }
      return new String(getBytes());
   }

   @Override
   public String getName() {
      return name;
   }

   @Override
   public InputStream openInputStream() throws IOException {
      return new ByteArrayInputStream(getBytes());
   }

   @Override
   public OutputStream openOutputStream() throws IOException {
      return bytes;
   }
}
