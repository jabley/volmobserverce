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
 * (c) Volantis Systems Ltd 2007. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.css;

import com.volantis.mcs.dom.Element;

public class CSSRemappingRuleTestCase extends CSSRemappingTestCaseAbstract {

    public void testBasicRuleEvaluation() throws Exception {

        final Element imgElement = createImgElement();

        final String attributeName = "width";
        assertNull(imgElement.getAttributeValue(attributeName));

        final CSSRemappingRule widthRule =
                createCSSRemappingRule(attributeName, "width");

        widthRule.apply(imgElement);

        assertEquals("53px", imgElement.getAttributeValue(attributeName));
    }

    public void testDifferentAttributeNameCreatedFromExpression()
            throws InvalidExpressionException {

        final Element imgElement = createImgElement();

        final String attributeName = "width-x";
        assertNull(imgElement.getAttributeValue(attributeName));

        final CSSRemappingRule widthRule =
                createCSSRemappingRule(attributeName, "width");
        widthRule.apply(imgElement);

        assertEquals("53px", imgElement.getAttributeValue(attributeName));
    }

    public void testDoesNotAlterExistingAttribute()  throws Exception {
        final Element imgElement = createImgElement();

        final String attributeName = "width";

        imgElement.setAttribute(attributeName, "123px");

        assertEquals("123px", imgElement.getAttributeValue(attributeName));

        final CSSRemappingRule widthRule =
                createCSSRemappingRule(attributeName, "width");

        widthRule.apply(imgElement);

        assertEquals("123px", imgElement.getAttributeValue(attributeName));
    }

    public void testIsIdempotent()  throws Exception {

        // This is a nice effect of not overwriting existing properties. This
        // test is intended to document that fact. If it gets removed at a
        // later date, presumably we have good reason for doing so.

        final Element imgElement = createImgElement();

        final String attributeName = "width";
        assertNull(imgElement.getAttributeValue(attributeName));

        final CSSRemappingRule widthRule =
                createCSSRemappingRule(attributeName, "width");
        widthRule.apply(imgElement);

        assertEquals("53px", imgElement.getAttributeValue(attributeName));

        widthRule.apply(imgElement);

        assertEquals("53px", imgElement.getAttributeValue(attributeName));
    }
}
