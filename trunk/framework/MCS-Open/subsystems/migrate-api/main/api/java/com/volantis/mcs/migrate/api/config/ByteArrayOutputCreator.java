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
/* ---------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2004. 
 * ---------------------------------------------------------------------------
 */

package com.volantis.mcs.migrate.api.config;

import com.volantis.mcs.migrate.api.framework.OutputCreator;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

/**
 * An output creator based on a simple byte-array.
 * <p/>
 * Typically the output stream will be used to create a simple string.
 * </p>
 *
 * @see //todo: migration-framework version of repository parser
 */
public class ByteArrayOutputCreator implements OutputCreator {

    private final ByteArrayOutputStream outputStream;

    /**
     * Create a byte-array Output Creator, with default values.
     * <p/>
     * </p>
     * @see ByteArrayOutputStream
     */
    public ByteArrayOutputCreator() {
        this.outputStream = new ByteArrayOutputStream();
    }

    /**
     * Generate a byte-array output stream.
     * <p>
     * If a buffer size was specified on creation of this object,
     * then this will have been used in creating the byte-array output
     * stream.
     * </p>
     * @return byte-array output stream.
     *
     * @see ByteArrayOutputStream
     */
    public OutputStream createOutputStream() {
        return getOutputStream();
    }

    // Javadoc inherited
    public String toString() {
        return "ByteArray output creator";
    }

    /**
     * Return the underlying output stream.
     * <p>
     * Post-processing accesss to the results of
     * a migration.
     * </p>
     * @return underlying byte-array output stream.
     *
     */
    public ByteArrayOutputStream getOutputStream() {
        return outputStream;
    }
}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 15-Nov-05	10098/2	phussain	VBM:2005110209 Migration Framework for Repository Parser - ready for review

 ===========================================================================
*/
