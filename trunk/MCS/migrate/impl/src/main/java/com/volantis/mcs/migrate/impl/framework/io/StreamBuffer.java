/*
This file is part of Volantis Mobility Server. 

Volantis Mobility Server is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Volantis Mobility Server is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Volantis Mobility Server.  If not, see <http://www.gnu.org/licenses/>. 
*/
/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.migrate.impl.framework.io;

import com.volantis.synergetics.log.LogDispatcher;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * An object which buffers between an output and input stream.
 * <p>
 * Using this is similar to using a {@link java.io.ByteArrayOutputStream} and
 * {@link java.io.ByteArrayInputStream} together.
 *
 * @mock.generate
 */
public interface StreamBuffer {

    /**
     * Return an output stream that writes to the buffer.
     * <p>
     * This must be called, used and closed before {@link #getInput} is called.
     *
     * @return the output stream that writes to the buffer.
     */
    OutputStream getOutput();

    /**
     * Return an input stream that reads from the buffer.
     * <p>
     * This may only be called once the output stream has been obtained, used
     * and closed.
     *
     * @return the input stream that reads from the buffer.
     */
    InputStream getInput();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 18-May-05	8036/5	geoff	VBM:2005050505 XDIMECP: Migration Framework

 16-May-05	8036/3	geoff	VBM:2005050505 XDIMECP: Migration Framework

 16-May-05	8036/1	geoff	VBM:2005050505 XDIMECP: Migration Framework

 ===========================================================================
*/
