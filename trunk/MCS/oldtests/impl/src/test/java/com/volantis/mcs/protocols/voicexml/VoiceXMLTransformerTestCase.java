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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/protocols/voicexml/VoiceXMLTransformerTestCase.java,v 1.1 2003/04/28 16:14:55 aboyd Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 23-Apr-03    Allan           VBM:2003042302 - Created. Testcase for 
 *                              VoiceXMLTransformer. 
 * 24-Apr-03    Allan           VBM:2003042207 - Added tests for menu promotion
 *                              during transform. 
 * 28-Apr-03    Allan           VBM:2003042802 - Moved from protocols package. 
 * 03-Jun-03    Allan           VBM:2003060301 - TestCaseAbstract moved to 
 *                              Synergetics. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols.voicexml;

import com.volantis.mcs.devices.InternalDeviceTestHelper;
import com.volantis.mcs.devices.InternalDevice;
import com.volantis.mcs.dom.Document;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.RecursingDOMVisitor;
import com.volantis.mcs.dom.debug.DOMUtilities;
import com.volantis.mcs.protocols.builder.ProtocolBuilder;
import com.volantis.mcs.protocols.builder.TestProtocolRegistry;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import org.xml.sax.XMLReader;

import java.util.HashSet;
import java.util.Set;

/**
 * Test case for VoiceXMLTransformer.
 */ 
public class VoiceXMLTransformerTestCase extends TestCaseAbstract {

    /**
     * Test that nested prompts are removed.
     */
    public void testTransformNestedPrompts() throws Exception {
        String domString =
                "<form>" +
                "<prompt>" +
                "level 1 " +
                "<prompt>" +
                "level 2 " +
                "</prompt>" +
                "</prompt>" +
                "<prompt>" +
                "<prompt>" +
                "<prompt>" +
                "level 3 " +
                "</prompt>" +
                "level 2 " +
                "</prompt>" +
                "level 1 " +
                "</prompt>" +
                "</form>";

        XMLReader reader = DOMUtilities.getReader();
        Document dom = DOMUtilities.read(reader, domString);

        VoiceXMLTransformer transformer = new VoiceXMLTransformer();

        VoiceXMLVersion1_0 protocol = createProtocol();

        transformer.transform(createProtocol(), dom);
        
        String expected = "<form><prompt>level 1 " +
                "level 2 </prompt><prompt>level 3 " +
                "level 2 level 1 </prompt></form>";
        
        String normalizedExpected = 
                DOMUtilities.provideDOMNormalizedString(
                        expected, protocol.getCharacterEncoder());
        
        assertEquals(normalizedExpected, DOMUtilities.toString(
                dom, protocol.getCharacterEncoder()));
        
    }

    private VoiceXMLVersion1_0 createProtocol() {

        InternalDevice internalDevice = InternalDeviceTestHelper.createTestDevice();

        ProtocolBuilder protocolBuilder = new ProtocolBuilder();
        return (VoiceXMLVersion1_0) protocolBuilder.build(
                new TestProtocolRegistry.TestVoiceXMLVersion1_0Factory(),
                internalDevice);
    }

    /**
     * Test that prompts inside choice elements are removed.
     */
    public void testTransformPromptsInsideChoice() throws Exception {
        String domString =
                "<form>" +
                "<choice>" +
                "inside choice " +
                "<prompt>" +
                "level 1 " +
                "</prompt>" +
                "</choice>" +
                "<choice>" +
                "<prompt>" +
                "<prompt>" +
                "level 2 " +
                "</prompt>" +
                "level 1 " +
                "</prompt>" +
                "end of choice " +
                "</choice>" +
                "</form>";

        XMLReader reader = DOMUtilities.getReader();
        Document dom = DOMUtilities.read(reader, domString);

        VoiceXMLTransformer transformer = new VoiceXMLTransformer();

        VoiceXMLVersion1_0 protocol = createProtocol();

        transformer.transform(createProtocol(), dom);
        
        String expected = "<form><choice>inside choice level 1 " +
                "</choice><choice>" +
                "level 2 level 1 end of choice </choice></form>";
        
        String normalizedExpected = 
                DOMUtilities.provideDOMNormalizedString(
                        expected, protocol.getCharacterEncoder());
        
        assertEquals(normalizedExpected, DOMUtilities.toString(
                dom, protocol.getCharacterEncoder()));
    }

    /**
     * Test that unique ids are created for menus that have no ids.
     */ 
    public void testTransformPromoteMenuUniqueIds() throws Exception {
        String domString = 
                "<xml>" +
                "<form>" +
                "<menu>" +
                "inside a menu" +
                "</menu>" +
                "</form>" +
                "<form>" +
                "<menu>" +
                "inside a menu" +
                "</menu>" +
                "</form>" +
                "</xml>";
        
        XMLReader reader = DOMUtilities.getReader();
        Document dom = DOMUtilities.read(reader, domString);
            
        VoiceXMLTransformer transformer = new VoiceXMLTransformer();
        transformer.transform(createProtocol(), dom);
        
        final Set ids = new HashSet();
        
        RecursingDOMVisitor visitor = new RecursingDOMVisitor() {

            public void visit(Element element) {
                if("menu".equals(element.getName())) {
                    String id = element.getAttributeValue("id");
                    assertNotNull("menu id should not be null", id);
                    assertTrue("Found non-unique id: " + id,
                               ids.add(id));
                }
                element.forEachChild(this);
            }
        };
        dom.forEachChild(visitor);    
    }
    
    /**
     * Test that menus inside forms get promoted out.
     */ 
    public void testTransformPromoteMenu() throws Exception {
       String domString =
                "<xml>" +
               "<form>" +
               "<prompt>" +
               "inside a form" +
               "<menu id=\"menuid\">" +
               "<prompt>" +
               "inside a menu" +
               "</prompt>" +
               "</menu>" +
               "after the menu" +
               "</prompt>" +
               "<prompt>" +
               "a prompt after the menu" +
               "</prompt>" +
               "</form>" +
               "</xml>";         
                       
        XMLReader reader = DOMUtilities.getReader();
        Document dom = DOMUtilities.read(reader, domString);

        VoiceXMLTransformer transformer = new VoiceXMLTransformer();

        VoiceXMLVersion1_0 protocol = createProtocol();

        transformer.transform(createProtocol(), dom);
        
        String expected = "<xml><form><prompt>inside a form" +
                "</prompt><block><goto next=\"#menuid\"/>" +
                "</block></form>" +
                "<menu id=\"menuid\">" +
                "<prompt>inside a menu</prompt></menu></xml>";
        
        String normalizedExpected = 
                DOMUtilities.provideDOMNormalizedString(
                        expected, protocol.getCharacterEncoder());
        
        assertEquals(normalizedExpected, DOMUtilities.toString(
                dom, protocol.getCharacterEncoder()));
    }

    /**
     * Test that menus inside forms get promoted out and are positioned
     * correctly between forms.
     */ 
    public void testTransformPromoteMenuMultiForms() throws Exception {
       String domString =
                "<xml>" +
               "<form>" +
               "<prompt>" +
               "inside a form" +
               "<menu id=\"menuid\">" +
               "<prompt>" +
               "inside a menu" +
               "</prompt>" +
               "</menu>" +
               "after the menu" +
               "</prompt>" +
               "<prompt>" +
               "a prompt after the menu" +
               "</prompt>" +
               "<block><goto next=\"#form2\"/></block>" +
               "</form>" +
               "<form id=\"form2\">" +
               "<prompt>" +
               "another form" +
               "</prompt>" +
               "</form>" +
               "</xml>";         
                       
        XMLReader reader = DOMUtilities.getReader();
        Document dom = DOMUtilities.read(reader, domString);

        VoiceXMLTransformer transformer = new VoiceXMLTransformer();

        VoiceXMLVersion1_0 protocol = createProtocol();

        transformer.transform(createProtocol(), dom);
        
        String expected = "<xml><form><prompt>inside a form" +
                "</prompt><block><goto next=\"#menuid\"/>" +
                "</block></form>" +
                "<menu id=\"menuid\">" +
                "<prompt>inside a menu</prompt></menu>" +
                "<form id=\"form2\"><prompt>another form</prompt></form></xml>";
        
        String normalizedExpected = 
                DOMUtilities.provideDOMNormalizedString(
                        expected, protocol.getCharacterEncoder());
        
        assertEquals(normalizedExpected, DOMUtilities.toString(
                dom, protocol.getCharacterEncoder()));
    }
    /**
     * Construct a new VoiceXMLTransformerTestCase.
     * @param name
     */
    public VoiceXMLTransformerTestCase(String name) {
        super(name);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 01-Sep-05	9375/2	geoff	VBM:2005082301 XDIMECP: clean up protocol creation

 19-Aug-05	9289/1	emma	VBM:2005081602 Refactoring UnabridgedTransformers so they're not singletons

 21-Jun-05	8856/1	geoff	VBM:2005062005 Refactoring for XDIMECP: Generate optimised CSS for a DOM.

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 12-Oct-04	5778/1	adrianj	VBM:2004083106 Provide styling engine API

 ===========================================================================
*/
