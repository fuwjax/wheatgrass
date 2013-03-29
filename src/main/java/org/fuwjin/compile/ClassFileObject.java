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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * A JavaFileObject for storing bytecode and source in memory.
 */
public class ClassFileObject extends BaseFileObject {
   private ByteArrayOutputStream bytes = new ByteArrayOutputStream();

   /**
    * Creates a new instance.
    * @param name the class name
    */
   protected ClassFileObject(final String name, Kind kind) {
      super(name, kind);
   }

   public byte[] toBytes() {
      return bytes.toByteArray();
   }
   
   @Override
   public OutputStream openOutputStream() throws IOException {
      return bytes;
   }
}
