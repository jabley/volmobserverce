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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/protocols/voicexml/NuanceGrammarTestCase.java,v 1.1 2003/04/30 07:42:01 adrian Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 30-Apr-03    Adrian          VBM:2003042903 - Created this testcase class to
 *                              test NuanceGrammar 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.voicexml;

import com.volantis.mcs.context.TestMarinerPageContext;
import com.volantis.mcs.devices.InternalDeviceTestHelper;
import com.volantis.mcs.devices.InternalDevice;
import com.volantis.mcs.dom.debug.DOMUtilities;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.assets.TextAssetReference;
import com.volantis.mcs.protocols.assets.implementation.LiteralTextAssetReference;
import com.volantis.mcs.protocols.builder.ProtocolBuilder;
import com.volantis.mcs.protocols.builder.TestProtocolRegistry;
import junit.framework.TestCase;

/**
 * This class tests NuanceGrammar.
 */
public class NuanceGrammarTestCase extends TestCase {

    private InternalDevice internalDevice;

    protected void setUp() throws Exception {
        super.setUp();

        internalDevice = InternalDeviceTestHelper.createTestDevice();
    }

    /**
     * This test the method generateBooleanGrammar     
     */ 
    public void testGenerateBooleanGrammar() throws Exception {

        final TextAssetReference trueReference =
                new LiteralTextAssetReference("yes, yep, yeah");
        
        final TextAssetReference falseReference =
                new LiteralTextAssetReference("no, nope");
        
        TestMarinerPageContext context = new TestMarinerPageContext();
        
        ProtocolBuilder protocolBuilder = new ProtocolBuilder();
        VoiceXMLRoot protocol = (VoiceXMLRoot) protocolBuilder.build(
                new TestProtocolRegistry.TestVoiceXMLVersion1_0Factory(),
                internalDevice);
        protocol.setMarinerPageContext(context);
        
        DOMOutputBuffer buffer = new DOMOutputBuffer();
        buffer.initialise();
        
        NuanceGrammar grammar = new NuanceGrammar();
        grammar.generateBooleanGrammar(buffer, "sugar",
                falseReference, trueReference);
        
        String result = DOMUtilities.toString(
                buffer.getCurrentElement(), protocol.getCharacterEncoder());
        
        String expected = "Sugar [ " +
                "(no) {&lt;sugar 0&gt;} " +
                "(nope) {&lt;sugar 0&gt;} " +
                "(yes) {&lt;sugar 1&gt;} " +
                "(yep) {&lt;sugar 1&gt;} " +
                "(yeah) {&lt;sugar 1&gt;} " +
                "]";
        
        assertEquals("Incorrect boolean grammer generated.", expected, result);
    }
    
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 01-Sep-05	9375/2	geoff	VBM:2005082301 XDIMECP: clean up protocol creation

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 ===========================================================================
*/
