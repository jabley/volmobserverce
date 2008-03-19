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
 * $Header: /src/voyager/com/volantis/mcs/protocols/mms/MMS_DOMOutputBufferFactory.java,v 1.2 2003/01/29 14:30:57 adrian Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 29-Jan-03    Adrian          VBM:2003012104 - Created this 
 *                              OutputBufferFactory to allocate 
 *                              MMS_DOMOutputBuffers for MMS_SMIL_2_0 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.mms;

import com.volantis.mcs.protocols.DOMOutputBufferFactory;
import com.volantis.mcs.protocols.OutputBuffer;
import com.volantis.mcs.dom.DOMFactory;

/**
 * This class constructs MMS_DOMOutputBuffers.
 */
public class MMS_DOMOutputBufferFactory extends DOMOutputBufferFactory {

    /**
     * Initialise.
     *
     * @param factory the DOM factory to use.
     */
    public MMS_DOMOutputBufferFactory(DOMFactory factory) {
        super(factory);
    }

    // Javadoc inherited.
    public OutputBuffer createOutputBuffer() {
        MMS_DOMOutputBuffer buffer = new MMS_DOMOutputBuffer();
        buffer.initialise();
        return buffer;
    }
}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 22-Aug-05	9298/1	geoff	VBM:2005080402 Style portlets and inclusions correctly.

 09-Jun-05	8665/1	emma	VBM:2005060204 Refactoring in order to annotate DOM with style info

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 26-Nov-04	6298/2	geoff	VBM:2004112405 MCS NullPointerException in wml code path

 ===========================================================================
*/
