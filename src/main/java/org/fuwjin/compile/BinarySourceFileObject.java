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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * A JavaFileObject for storing bytecode and source in memory.
 */
public class BinarySourceFileObject extends BaseFileObject {
   private byte[] bytes;

   protected BinarySourceFileObject(final String name, final byte[] bytes) {
      super(name, Kind.SOURCE);
      this.bytes = bytes;
   }

   @Override
   public InputStream openInputStream() throws IOException {
      return new ByteArrayInputStream(bytes);
   }
}
