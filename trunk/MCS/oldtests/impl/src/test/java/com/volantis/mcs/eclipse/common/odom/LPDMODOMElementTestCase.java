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
package com.volantis.mcs.eclipse.common.odom;

import com.volantis.synergetics.testtools.TestCaseAbstract;

import org.jdom.Namespace;

/**
 * Test case for {@link com.volantis.mcs.eclipse.common.odom.LPDMODOMElement}.
 */
public class LPDMODOMElementTestCase extends TestCaseAbstract {

    /**
     * Tests setting the attribute on an LPDMODOMElement which has no
     * namespace.
     */
    public void testSetAttributeWithNoNamespace() throws Exception {
        final String attrName = "myAttribute";
        final String attrValue = "myValue";

        LPDMODOMElement parent = new LPDMODOMElement("myElement");

        parent.setAttribute(attrName, attrValue);
        assertNotNull(parent.getAttribute(attrName));
        assertEquals("Attribute values should be the same",
                attrValue,
                parent.getAttributeValue(attrName));
    }

    /**
     * Tests setting the attribute on an LPDMODOMElement which has a
     * namespace.
     */
    public void testSetAttributeWithNamespace() throws Exception {
        final String attrName = "myAttribute";
        final String attrValue = "myValue";
        final Namespace namespace = Namespace.getNamespace("abc",
                "http://www.volantis.com/xmlns/abc");

        LPDMODOMElement parent = new LPDMODOMElement("myElement");

        parent.setAttribute(attrName, attrValue, namespace);
        assertNotNull(parent.getAttribute(attrName, namespace));
        assertEquals("Attribute values should be the same",
                attrValue,
                parent.getAttributeValue(attrName, namespace));

        // Sets an attribute with the same name but in a different (default)
        // namespace to some other value. This attribute should be left intact
        // after setting the namespaced one to the new value.
        parent.setAttribute(attrName, "anotherValue");
        assertEquals("Attribute value should not have changed",
                attrValue,
                parent.getAttributeValue(attrName, namespace));
    }

    /**
     * Tests changing the value of an existing attribute with no namespace.
     * @throws Exception
     */
    public void testChangeExistingAttributeWithNoNamespace()
            throws Exception {
        final String attrName = "myAttribute";
        final String attrValue = "myValue";

        LPDMODOMElement parent = new LPDMODOMElement("myElement");

        parent.setAttribute(attrName, attrValue);
        LPDMODOMAttribute attr =
                (LPDMODOMAttribute) parent.getAttribute(attrName);
        assertEquals("Attribute value should be the same",
                attrValue, attr.getValue());

        parent.setAttribute(attrName, "newValue");
        assertEquals("Attribute value should be the same",
                "newValue", attr.getValue());
    }

    /**
     * Tests changing the value of an existing attribute with a namespace.
     */
    public void testChangeExistingAttributeWithNamespace()
            throws Exception {
        final String attrName = "myAttribute";
        final String attrValue = "myValue";
        final Namespace namespace = Namespace.getNamespace("abc",
                "http://www.volantis.com/xmlns/abc");

        LPDMODOMElement parent = new LPDMODOMElement("myElement");

        parent.setAttribute(attrName, attrValue, namespace);
        LPDMODOMAttribute attr =
                (LPDMODOMAttribute) parent.getAttribute(attrName, namespace);
        assertEquals("Attribute value should be the same",
                attrValue, attr.getValue());

        parent.setAttribute(attrName, "newValue", namespace);
        assertEquals("Attribute value should be the same",
                "newValue", attr.getValue());
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 08-Sep-04	5251/16	pcameron	VBM:2004081609 Empty attribute values are deleted by editors

 08-Sep-04	5251/8	pcameron	VBM:2004081609 Empty attribute values are deleted by editors

 ===========================================================================
*/
