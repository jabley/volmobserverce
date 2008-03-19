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
 * $Header: /src/voyager/com/volantis/mcs/protocols/text/TextOutputBufferFactory.java,v 1.2 2002/11/28 11:56:32 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 27-Nov-02    Geoff           VBM:2002103005 - Created, mostly cloned from
 *                              StringOutputBufferFactory (yuck).
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols.text;

import com.volantis.mcs.protocols.OutputBuffer;
import com.volantis.mcs.protocols.OutputBufferFactory;

/**
 * Another impl of OutputBufferFactory, this time for TextOutputBuffers.
 */ 
public final class TextOutputBufferFactory
        implements OutputBufferFactory {

    /**
     * The reference to the single allowable instance of this class.
     */
    private static final TextOutputBufferFactory singleton;

    // Initialise the static fields.
    static {
        // Always initialise to prevent a synchronization problem if we do it
        // lazily.
        singleton = new TextOutputBufferFactory();
    }

    /**
     * Get the single allowable instance of this class.
     * @return The single allowable instance of this class.
     */
    public static TextOutputBufferFactory getSingleton() {
        return singleton;
    }

    /**
     * Protect the constructor to prevent any other instances being created.
     */
    private TextOutputBufferFactory() {
    }

    // Javadoc inherited.
    public OutputBuffer createOutputBuffer() {
        return new TextOutputBuffer();
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 22-Aug-05	9298/2	geoff	VBM:2005080402 Style portlets and inclusions correctly.

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/2	doug	VBM:2004111702 Refactored Logging framework

 26-Nov-04	6298/2	geoff	VBM:2004112405 MCS NullPointerException in wml code path

 ===========================================================================
*/
