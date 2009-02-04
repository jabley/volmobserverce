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
package com.volantis.mcs.protocols;

/**
 * A fragment link renderer context for DOMProtocols.
 */
public class DOMFragmentLinkRendererContext
        extends FragmentLinkRendererContext {

    public DOMFragmentLinkRendererContext(DOMProtocol protocol) {

        super(protocol);

    }

    // Javadoc inherited.
    public void doAnchor(
            OutputBuffer outputBuffer,
            AnchorAttributes attributes) throws ProtocolException {

        ((DOMProtocol) protocol).doAnchor((DOMOutputBuffer) outputBuffer,
                attributes);

    }

    // Javadoc inherited.
    public void doLineBreak(
            OutputBuffer outputBuffer,
            LineBreakAttributes attributes) throws ProtocolException {

        ((DOMProtocol) protocol).doLineBreak((DOMOutputBuffer) outputBuffer,
                attributes);

    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 17-Sep-03	1412/3	geoff	VBM:2003091105 Modify WML Openwave protocols to render fragment links as numeric style links

 ===========================================================================
*/
