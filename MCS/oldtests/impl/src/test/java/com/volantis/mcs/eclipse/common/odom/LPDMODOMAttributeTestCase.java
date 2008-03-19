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

/**
 * Test case for {@link LPDMODOMAttribute}.
 */
public class LPDMODOMAttributeTestCase extends TestCaseAbstract {

    /**
     * Tests setting the value of an attribute to a non-empty value.
     */
    public void testSetValueNonEmpty() throws Exception {
        final String attrName = "myAttribute";
        final String attrValue = "myValue";

        LPDMODOMAttribute attribute =
                new LPDMODOMAttribute(attrName, attrValue);

        assertEquals("Attribute value not as expected",
                attrValue, attribute.getValue());

        attribute.setValue("newValue");
        assertEquals("Attribute value not as expected",
                "newValue", attribute.getValue());
    }

    /**
     * Tests setting the value of an attribute to the empty string.
     */
    public void testSetValueEmpty() throws Exception {
        final String attrName = "myAttribute";
        final String attrValue = "myValue";

        LPDMODOMElement parent = new LPDMODOMElement();

        LPDMODOMAttribute attribute =
                new LPDMODOMAttribute(attrName, attrValue);
        assertEquals("Attribute value not as expected",
                attrValue, attribute.getValue());

        parent.setAttribute(attribute);
        assertSame("Attribute should have a parent",
                parent, attribute.getParent());

        attribute.setValue("");
        assertEquals("Attribute value should not have changed", attrValue,
                attribute.getValue());
        assertNull("Attribute should have detached",
                attribute.getParent());
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 08-Sep-04	5251/9	pcameron	VBM:2004081609 Empty attribute values are deleted by editors

 ===========================================================================
*/
