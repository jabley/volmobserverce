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
 * $Header: /src/voyager/com/volantis/mcs/protocols/html/HTMLVersion4_01.java,v 1.3 2003/02/05 11:03:14 byron Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 15-Nov-02    Phil W-S        VBM:2002111303 - Created. New HTML 4.01
 *                              protocol. On creation this protocol was not
 *                              registered in its own right, but only as a base
 *                              class for other protocol(s).
 *                              Support for dissection has been added for use
 *                              by these devices.
 * 29-Jan-03    Byron           VBM:2003012803 - Modified constructor to set
 *                              protocolConfiguration value and any static
 *                              variables dependent on it.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols.html;

import com.volantis.mcs.protocols.ProtocolConfiguration;
import com.volantis.mcs.protocols.ProtocolSupportFactory;
import com.volantis.mcs.dom.Document;
import com.volantis.mcs.dom.DocType;
import com.volantis.mcs.dom.MarkupFamily;

/**
 * A protocol for HTML 4.01 devices. Since this implementation utilizes
 * presentational attributes, rather than CSS, to apply stylistic values,
 * the transitional DTD is referenced.
 *
 * @author <a href="phil.weighill-smith@volantis.com">Phil W-S</a>
 */
public class HTMLVersion4_01 extends HTMLVersion4_0 {

    protected HTMLVersion4_01(
            ProtocolSupportFactory supportFactory,
            ProtocolConfiguration configuration) {

        super(supportFactory, configuration);

        supportsDissectingPanes = true;
    }

    protected void doProtocolString(Document document) {
        DocType docType = domFactory.createDocType(
                "HTML", "-//W3C//DTD HTML 4.01 Transitional//EN",
                "http://www.w3.org/TR/html4/loose.dtd",
                null, MarkupFamily.SGML);
        document.setDocType(docType);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 22-Sep-05	9540/1	geoff	VBM:2005091906 Protocol Parameterisation: Basic Rendundant CSS Property Filtering

 01-Sep-05	9375/1	geoff	VBM:2005082301 XDIMECP: clean up protocol creation

 23-Jun-05	8833/1	pduffin	VBM:2005042901 Addressing review comments

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
