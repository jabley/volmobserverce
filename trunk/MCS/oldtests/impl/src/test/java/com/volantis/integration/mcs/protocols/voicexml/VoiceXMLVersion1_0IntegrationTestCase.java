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
 * $Header: /src/voyager/testsuite/integration/com/volantis/integration/mcs/protocols/voicexml/VoiceXMLVersion1_0IntegrationTestCase.java,v 1.1 2003/04/28 16:14:55 aboyd Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 15-Apr-03    Byron           VBM:2003040302 - Created as per protocol
 *                              hierarchy.
 * 28-Apr-03    Allan           VBM:2003042802 - Moved from protocols package. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.integration.mcs.protocols.voicexml;

import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.builder.ProtocolBuilder;
import com.volantis.mcs.protocols.builder.ProtocolRegistry;

/**
 * This class provides the implement for the Abstract integration test case for
 * Voice XML.
 *
 * Note that the following tests:
 * <ul>
 * <li>testWriteOpenParagraph()</li>
 * <li>testWriteOpenRowIteratorPane()</li>
 * <li>testWriteOpenPane()</li>
 * <li>testWriteOpenLayout()</li>
 * </ul>
 *
 * did not complete successfully for the VoiceXML protocol (when it was a
 * StringProtocol.
 */
public class VoiceXMLVersion1_0IntegrationTestCase
        extends VoiceXMLRootIntegrationTestAbstract {


    // javadoc inherited
    protected VolantisProtocol getProtocol() {
        // todo: better: make this work with the test version
        ProtocolBuilder protocolBuilder = new ProtocolBuilder();
        VolantisProtocol protocol = protocolBuilder.build(
                new ProtocolRegistry.VoiceXMLVersion1_0Factory(),
                internalDevice);
        
        return protocol;
    }

    public void testWriteOpenInclusion() throws Exception {
        super.testWriteOpenInclusion();    //To change body of overridden methods use File | Settings | File Templates.
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 28-Nov-05	10467/1	ibush	VBM:2005111812 Styling Fixes for Orange Test Page

 28-Nov-05	10394/1	ibush	VBM:2005111812 Styling Fixes for Orange Test Page

 03-Oct-05	9673/1	pduffin	VBM:2005092906 Added support for targeting content at layout using styles

 12-Sep-05	9372/1	ianw	VBM:2005082221 Allow only one instance of MarinerPageContext for a page

 01-Sep-05	9375/2	geoff	VBM:2005082301 XDIMECP: clean up protocol creation

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
