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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/protocols/voicexml/VoiceXMLVersion1_0TestCase.java,v 1.2 2003/04/30 08:35:40 byron Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 28-Apr-03    Adrian          VBM:2003042807 - Created this class to test 
 *                              VoiceXMLVersion1_0 
 * 30-Apr-03    Byron           VBM:2003042812 - Added getContentBuffer to
 *                              inner class
 * 27-May-03    Byron           VBM:2003051904 - Updated TestVoiceXMLVersion1_0
 *                              inner class to be able to get/set the style.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.voicexml;

import com.volantis.mcs.protocols.DOMProtocolTestable;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.VolantisProtocolTestable;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.builder.ProtocolBuilder;
import com.volantis.mcs.protocols.builder.TestProtocolRegistry;
import com.volantis.mcs.dom.Text;
import com.volantis.mcs.devices.InternalDevice;

/**
 * This class tests VoiceXMLVersion1_0
 */
public class VoiceXMLVersion1_0TestCase extends VoiceXMLRootTestAbstract {

    /**
     * The protocol to use in testing.
     */
    protected VoiceXMLVersion1_0 protocol;

    /**
     * The protocol to using in testing stored as a Testable to give us access
     * to the Testable interface methods.
     */
    protected DOMProtocolTestable testable;

    /**
     * Construct a new instance of VoiceXMLVersion1_0TestCase
     */
    public VoiceXMLVersion1_0TestCase(String name) {
        super(name);
    }

    // javadoc inherited from superclass
    protected VolantisProtocol createTestableProtocol(
            InternalDevice internalDevice) {
        ProtocolBuilder protocolBuilder = new ProtocolBuilder();
        VolantisProtocol protocol = protocolBuilder.build(
                new TestProtocolRegistry.TestVoiceXMLVersion1_0Factory(),
                internalDevice);
        return protocol;
    }

    // javadoc inherited from superclass
    protected void setTestableProtocol(VolantisProtocol protocol,
                                       VolantisProtocolTestable testable) {
        super.setTestableProtocol(protocol, testable);

        this.protocol = (VoiceXMLVersion1_0) protocol;
        this.testable = (DOMProtocolTestable) testable;
    }

    // javadoc inherited
    protected void checkResultForPre(final DOMOutputBuffer buffer) {
        final Text text = (Text) buffer.getRoot().getHead();
        assertEquals("     before          child     text          after     ",
            new String(text.getContents(), 0, text.getLength()));
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 01-Sep-05	9375/4	geoff	VBM:2005082301 XDIMECP: clean up protocol creation

 22-Aug-05	9363/5	emma	VBM:2005080405 Remove the 'supports multi class' properties

 22-Aug-05	9363/2	emma	VBM:2005080405 Remove the 'supports multi class' properties

 22-Aug-05	9298/3	geoff	VBM:2005080402 Style portlets and inclusions correctly.

 22-Aug-05	9324/1	ianw	VBM:2005080202 Move validation for WapCSS into styling

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 09-Aug-05	9151/4	pduffin	VBM:2005080205 Recommitted after super merge

 04-Aug-05	9151/1	pduffin	VBM:2005080205 Removing a lot of unnecessary styling code

 22-Jul-05	8859/1	emma	VBM:2005062006 Modify transformers to take account of Styles when flattening/optimizing tables

 14-Jul-05	9039/1	emma	VBM:2005071401 Adding get/setSynthesizedValues to PropertyValues

 12-Jul-05	9011/1	pduffin	VBM:2005071214 Refactored StyleValueFactory to change static methods to non static

 22-Jun-05	8483/3	emma	VBM:2005052410 Modifications after review

 20-Jun-05	8483/1	emma	VBM:2005052410 Migrate styling to use the new styling support framework (still using CSSEmulator to style underneath)

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 22-Nov-04	5733/4	geoff	VBM:2004093001 Support OMA WCSS subset of CSS2

 19-Nov-04	5733/2	geoff	VBM:2004093001 Support OMA WCSS subset of CSS2

 12-Nov-04	6135/2	byron	VBM:2004081726 Allow spatial format iterators within forms

 01-Nov-04	6068/1	tom	VBM:2004101508 renamed VolantisAttribute to MCSAttributes and added style properties container

 11-Oct-04	5744/1	claire	VBM:2004092801 mergevbm: Encoding of style class names for inclusions

 11-Oct-04	5742/1	claire	VBM:2004092801 Encoding of style class names for inclusions

 07-Oct-04	5729/3	claire	VBM:2004092801 Encoding of style class names for inclusions

 02-Sep-04	5354/3	tom	VBM:2004082008 Optimized imports and defuncted StandardStyleProperties

 02-Sep-04	5354/1	tom	VBM:2004082008 started device theme normalization

 20-Aug-03	1207/1	adrian	VBM:2003032804 removed suite and main methods from testcase classes

 07-Jul-03	728/1	adrian	VBM:2003052001 fixed pane attribute generation

 04-Jul-03	680/1	adrian	VBM:2003052001 Fixed bug in pane attribute and styleclass rendering

 ===========================================================================
*/
