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
package com.volantis.mcs.eclipse.common.odom;

import com.volantis.mcs.eclipse.ab.editors.dom.LPDMODOMFactory;
import com.volantis.synergetics.testtools.TestCaseAbstract;

import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.input.JDOMFactory;

/**
 * Test case for
 * {@link com.volantis.mcs.eclipse.ab.editors.dom.LPDMODOMFactory}.
 */
public class LPDMODOMFactoryTestCase extends TestCaseAbstract {
    /**
     * The factory being tested.
     */
    private static final JDOMFactory factory = new LPDMODOMFactory();

    /**
     * A namespace for various tests.
     */
    private static final Namespace namespace =
            Namespace.getNamespace(
                    "abc",
                    "http://www.volantis.com/xmlns/abc");

    // javadoc inherited
    public LPDMODOMFactoryTestCase(String name) {
        super(name);
    }

    /**
     * Tests the LPDMODOMElementFactory's attribute creation methods.
     */
    public void testAttributeCreation() throws Exception {
        final String attrName = "name";
        final String attrValue = "value";
        Attribute attr = factory.attribute(attrName, attrValue, namespace);
        assertSame("attribute(name, value, namespace) class not as",
                LPDMODOMAttribute.class,
                attr.getClass());
        assertSame("Namespaces should be the same",
                attr.getNamespace(), namespace);
        assertEquals("Attribute name should be the same",
                attrName, attr.getName());
        assertEquals("Attribute value should be the same",
                attrValue, attr.getValue());

        attr = factory.attribute("name", "value",
                Attribute.NMTOKEN_ATTRIBUTE,
                namespace);
        assertSame("attribute(name, value, type, namespace) class not " +
                "as expected",
                LPDMODOMAttribute.class,
                attr.getClass());
        assertSame("Namespaces should be the same",
                attr.getNamespace(), namespace);
        assertEquals("Attribute name should be the same",
                attrName, attr.getName());
        assertEquals("Attribute value should be the same",
                attrValue, attr.getValue());

        attr = factory.attribute("name", "value");
        assertSame("attribute(name, value) class not as expected",
                LPDMODOMAttribute.class,
                attr.getClass());
        assertEquals("Attribute name should be the same",
                attrName, attr.getName());
        assertEquals("Attribute value should be the same",
                attrValue, attr.getValue());

        attr = factory.attribute("name", "value",
                Attribute.NMTOKEN_ATTRIBUTE);
        assertSame("attribute(name, value, type) class not as expected",
                LPDMODOMAttribute.class,
                attr.getClass());
        assertEquals("Attribute name should be the same",
                attrName, attr.getName());
        assertEquals("Attribute value should be the same",
                attrValue, attr.getValue());
    }

    /**
     * Tests the LPDMODOMElementFactory's element creation methods.
     */
    public void testElementCreation() throws Exception {
        final String elemName = "name";
        Element elem = factory.element(elemName, namespace);
        assertSame("element(name, namespace) class not as expected",
                LPDMODOMElement.class,
                elem.getClass());
        assertSame("Namespaces should be the same",
                elem.getNamespace(), namespace);
        assertEquals("Element name should be the same",
                elemName, elem.getName());

        elem = factory.element("name");
        assertSame("element(name) class not as expected",
                LPDMODOMElement.class,
                elem.getClass());
        assertSame("Namespaces should be the same",
                elem.getNamespace(), MCSNamespace.LPDM);
        assertEquals("Element name should be the same",
                elemName, elem.getName());

        elem = factory.element("name",
                namespace.getURI());
        assertSame("element(name, uri) class not as expected",
                LPDMODOMElement.class,
                elem.getClass());
        assertEquals("Namespaces should be the same",
                elem.getNamespace().getURI(), namespace.getURI());
        assertEquals("Element name should be the same",
                elemName, elem.getName());

        elem = factory.element("name",
                namespace.getPrefix(),
                namespace.getURI());
        assertSame("element(name, prefix, uri) class not as expected",
                LPDMODOMElement.class,
                elem.getClass());
        assertEquals("Element name should be the same",
                elemName, elem.getName());
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 08-Sep-04	5451/4	pcameron	VBM:2004081609 Empty attribute values are deleted by editors

 08-Sep-04	5251/10	pcameron	VBM:2004081609 Empty attribute values are deleted by editors

 ===========================================================================
*/
