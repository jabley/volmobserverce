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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols.html;

import com.volantis.mcs.protocols.ProtocolConfiguration;
import com.volantis.mcs.protocols.ProtocolSupportFactory;

/**
 * This is the Netscape 4 specialisation of the HTMLVersion4_0. It only needs to
 * set the CSS emulation properties up correctly.
 */
public class HTMLVersion4_0_NS4 extends HTMLVersion4_0 {

    public HTMLVersion4_0_NS4(ProtocolSupportFactory supportFactory,
            ProtocolConfiguration configuration) {
        super(supportFactory, configuration);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 01-Sep-05	9375/1	geoff	VBM:2005082301 XDIMECP: clean up protocol creation

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 23-Jun-04	4739/1	steve	VBM:2004042912 Move border spacing out of IE and NS4

 23-Jun-04	4730/1	steve	VBM:2004042912 Move border spacing out of IE and NS4

 20-Aug-03	1152/1	chrisw	VBM:2003070811 Emulate CSS2 border-spacing using cellspacing on table element

 ===========================================================================
*/
