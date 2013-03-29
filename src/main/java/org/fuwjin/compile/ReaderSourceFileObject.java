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

import java.io.IOException;
import java.io.Reader;

/**
 * A JavaFileObject for storing bytecode and source in memory.
 */
public class ReaderSourceFileObject extends BaseFileObject {
	private Reader reader;

   protected ReaderSourceFileObject(final String name, final Reader reader) {
      super(name, Kind.SOURCE);
		this.reader = reader;
   }

   @Override
   public Reader openReader(boolean ignoreEncodingErrors) throws IOException {
      return reader;
   }
}
