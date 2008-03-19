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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.xdime;

import com.volantis.mcs.xdime.initialisation.ElementFactoryMap;
import com.volantis.mcs.xdime.initialisation.ElementFactoryMapBuilder;
import com.volantis.mcs.xdime.initialisation.ElementFactoryMock;
import com.volantis.mcs.xml.schema.model.ElementType;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import org.xml.sax.helpers.AttributesImpl;

/**
 * Verifies the behaviour of {@link XDIMEElementHandler}.
 */
public class XDIMEElementHandlerTestCase
        extends TestCaseAbstract {

    private static final String defaultNamespace = "mcs";


    private ElementFactoryMock factoryMock;
    private ElementType elementType;
    private XDIMEContextInternalMock contextMock;

    /**
     * Handler to use when testing. Will be recreated for each test.
     */
    XDIMEElementHandler handler;

    public void setUp() throws Exception {
        super.setUp();

        factoryMock = new ElementFactoryMock("factoryMock", expectations);

        ElementFactoryMapBuilder builder = new ElementFactoryMapBuilder();
        elementType = new ElementType("foo", "bar");
        builder.addMapping(elementType, factoryMock);
        ElementFactoryMap map = builder.buildFactoryMap();

        handler = new XDIMEElementHandler(map);

        contextMock =
                new XDIMEContextInternalMock("contextMock",
                        expectations);
    }

    public void tearDown() {
        handler = null;
    }

    public void testCreateXDIMEAttributesWithNamespace() throws XDIMEException {
        AttributesImpl saxAttributes = new AttributesImpl();

        for (int i = 0; i < 4; i++) {
            String index = Integer.toString(i);
            saxAttributes.addAttribute(defaultNamespace, index, "mcs:" + index,
                    "String", "value" + index);
        }

        XDIMEAttributesImpl attributes = (XDIMEAttributesImpl)handler.
                createXDIMEAttributes(elementType, saxAttributes);
        assertNotNull(attributes);
        int newLength = attributes.getLength();
        assertEquals(saxAttributes.getLength(), newLength);

        for (int i = 0; i < newLength; i++) {
            String index = Integer.toString(i);
            assertEquals("value" + index,
                    attributes.getValue(defaultNamespace, index));
        }
    }

    public void testCreateXDIMEAttributesWithoutNamespace() throws XDIMEException {
        AttributesImpl saxAttributes = new AttributesImpl();

        for (int i = 0; i < 4; i++) {
            String index = Integer.toString(i);
            saxAttributes.addAttribute("", index, index,
                    "String", "value" + index);
        }

        XDIMEAttributesImpl attributes = (XDIMEAttributesImpl)handler.
                createXDIMEAttributes(elementType, saxAttributes);
        assertNotNull(attributes);
        int newLength = attributes.getLength();
        assertEquals(saxAttributes.getLength(), newLength);

        for (int i = 0; i < newLength; i++) {
            String index = Integer.toString(i);
            assertEquals("value" + index, attributes.getValue("", index));
        }
    }

    public void testCreateXDIMEAttributesFromEmptySAXAttributes()
            throws XDIMEException {
        AttributesImpl saxAttributes = new AttributesImpl();


        XDIMEAttributesImpl attributes = (XDIMEAttributesImpl) handler.
                createXDIMEAttributes(elementType, saxAttributes);
        assertNotNull(attributes);
        int newLength = attributes.getLength();
        assertEquals(saxAttributes.getLength(), newLength);
        assertEquals(0, newLength);
    }

    public void testCreateXDIMEElement() throws XDIMEException {

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        final XDIMEElementInternalMock elementMock =
                new XDIMEElementInternalMock("elementMock",
                        expectations);

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        factoryMock.expects.createElement(contextMock).returns(elementMock);

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        XDIMEElement element = handler.createXDIMEElement(elementType, contextMock);
        assertNotNull(element);
    }

    public void testCreateXDIMEElementWithNullNamespace() {
        try {
            handler.createXDIMEElement(null, contextMock);
            fail("Namespaces must be present");
        } catch (XDIMEException e) {
            // do nothing, correct behaviour
        }
    }

    public void testCreateXDIMEUnknownElement() {
        try {
            ElementType unknownElementType = new ElementType("unknown", "abc");
            handler.createXDIMEElement(unknownElementType, contextMock);
            fail("Handler should recognise that element is not supported");
        } catch (XDIMEException e) {
            // do nothing - correct behaviour
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 10-Oct-05	9673/1	pduffin	VBM:2005092906 Improved validation and fixed layout formatting

 10-Oct-05	9637/3	emma	VBM:2005092807 Adding tests for XForms emulation

 30-Sep-05	9637/1	emma	VBM:2005092807 XForms in XDIME-CP (without tests)

 ===========================================================================
*/
