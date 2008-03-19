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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols.vdxml;

import com.volantis.mcs.dom.Element;
import com.volantis.mcs.protocols.DOMFragmentLinkRendererContext;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.DOMProtocol;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.styling.values.MutablePropertyValues;

/**
 * A fragment link renderer context for VDXML Protocols.
 */
public class VDXMLFragmentLinkRendererContext
        extends DOMFragmentLinkRendererContext {

    /**
     * Initialise.
     *
     * @param protocol the protocol to delegate to.
     */
    public VDXMLFragmentLinkRendererContext(DOMProtocol protocol) {

        super(protocol);
    }

    /**
     * @see VDXMLVersion2_0#outputExternalLink
     */
    public void outputExternalLink(String shortcut, String url) {

        ((VDXMLVersion2_0) protocol).outputExternalLink(shortcut, url);
    }

    /**
     * @see VDXMLVersion2_0#getFragmentLinksBuffer
     */
    public DOMOutputBuffer getFragmentLinksBuffer(String fragmentName)
            throws ProtocolException {

        return ((VDXMLVersion2_0) protocol).getFragmentLinksBuffer(fragmentName);
    }

    /**
     * @see VDXMLVersion2_0#addFakePaneAttributes
     */
    public void addFakePaneAttributes(Element element, String formatName,
            MutablePropertyValues propertyValues) {

        ((VDXMLVersion2_0) protocol).addFakePaneAttributes(element, formatName,
                propertyValues);
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 30-Jun-05	8893/2	emma	VBM:2005062406 Annotate DOM elements generated from VDXML with styles

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 01-Oct-04	5635/2	geoff	VBM:2004092216 Port VDXML to MCS: update menu support

 24-Sep-04	5613/2	geoff	VBM:2004092215 Port VDXML to MCS: update fragment link support

 ===========================================================================
*/
