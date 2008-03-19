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

package com.volantis.mcs.eclipse.ab.views.layout;

import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.mcs.eclipse.common.ControlType;
import com.volantis.mcs.eclipse.common.PresentableItem;

/**
 * Test the FormatAttributesViewDetails object.
 */
public class FormatAttributesViewDetailsTestCase extends TestCaseAbstract {

    /**
     * Test the class.
     */
    public void testAddAttributes() throws Exception {
        FormatAttributesViewDetails details = new FormatAttributesViewDetails();
        assertEquals("Value should match", null, details.getAttributes());

        try {
            details.addAttributes(null, null, null, null);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // do nothing
        }

        try {
            details.addAttributes("name", null, null, null);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // do nothing
        }

        String[] attributes = { "name" };
        details.addAttributes(attributes[0], "Text", null, null);
        assertEquals("Value should match",
                attributes.length,
                details.getAttributes().length);
        assertEquals("Value should match",
                1,
                details.getAttributes().length);
        assertEquals("Value should match",
                attributes[0],
                details.getAttributes()[0]);
        ControlType type = details.getAttributeControlType(attributes[0]);
        assertNotNull("Type shouldn't be null", type);
        assertEquals("Value should match", ControlType.TEXT, type);

        attributes = new String[] { "name", "attribute" };
        details.addAttributes(attributes[1], "UNKNOWN", null, null);
        assertEquals("Value should match",
                attributes.length,
                details.getAttributes().length);
        assertEquals("Value should match",
                2,
                details.getAttributes().length);
        assertEquals("Value should match",
                attributes[1],
                details.getAttributes()[1]);
        type = details.getAttributeControlType(attributes[1]);
        assertNull("Type should be null", type);
    }

    /**
     * Test the associating of an selection with an attribute.
     */
    public void testAssociateSelectionValue() throws Exception {
        FormatAttributesViewDetails details = new FormatAttributesViewDetails();

        try {
            details.associateSelectionValue(null, null);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // do nothing
        }

        String attributeName = "attribute";
        try {
            details.associateSelectionValue(attributeName, null);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // do nothing
        }

        try {
            details.associateSelectionValue(attributeName, "");
            fail("Expected an IllegalStateException");
        } catch (IllegalStateException e) {
            // do nothing
        }
        Object[] objects = details.getAttributeValueSelection(attributeName);
        assertNull("Objects should be null", objects);
        details.addAttributes(attributeName, "Text", null, null);
        details.associateSelectionValue(attributeName, "test");
        objects = details.getAttributeValueSelection(attributeName);
        assertNotNull("Objects shouldn't be null", objects);
        assertEquals("Value should match", "test", objects[0]);
    }

    /**
     * Test the getting of presentable items. Note that we override the
     * creation of the values deliberately to side-step the issue of not
     * finding the item in the resource bundle.
     */
    public void testGetAttributePresentableItems() throws Exception {
        FormatAttributesViewDetails details = new FormatAttributesViewDetails() {
            protected String[] createPresentableValues(String attribute,
                                                       Object[] realValues) {
                String [] values = new String[realValues.length];
                for (int i = 0; i < realValues.length; i++) {
                    values[i] = (String)realValues[i];
                }
                return values;
            }
        };

        PresentableItem[] items = details.getAttributePresentableItems(null);
        assertNull("Value should be null", items);

        details.addAttributes("attribute", "Text", null, null);
        items = details.getAttributePresentableItems(null);
        assertNull("Value should be null", items);

        details.associateSelectionValue("attribute", "selection");
        items = details.getAttributePresentableItems("attribute");
        assertNotNull("Value should NOT be null", items);
        assertEquals("Number of items should match", 1, items.length);
        assertEquals("Value should match", "selection", items[0].presentableValue);
        assertEquals("Value should match", "selection", items[0].realValue);

        details.associateSelectionValue("attribute", "selection2");
        items = details.getAttributePresentableItems("attribute");
        assertNotNull("Value should NOT be null", items);
        assertEquals("Number of items should match", 2, items.length);
        assertEquals("Value should match", "selection2", items[1].presentableValue);
        assertEquals("Value should match", "selection2", items[1].realValue);
        String value = details.getPresentableValue("attribute", "selection2");
        assertEquals("Value should match", "selection2", value);
        value = details.getPresentableValue("attribute", "not found");
        assertEquals("Value should match", "not found", value);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 13-Jan-04	2483/1	byron	VBM:2003121504 Eclipse PM Layout Editor: Format Attributes View: XML Config

 ===========================================================================
*/
