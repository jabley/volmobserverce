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
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.styling.values.MutablePropertyValues;

import java.util.Arrays;

public class ElementRuleContainerTestCase extends CSSRemappingTestCaseAbstract {

    public void testImgWithHeightAndWidthRules()
            throws InvalidExpressionException {

        final Element imgElement = createImgElement();

        assertNull(imgElement.getAttributeValue("width"));
        assertNull(imgElement.getAttributeValue("height"));

        final MutablePropertyValues propertyValues =
                imgElement.getStyles().findPropertyValues();

        assertEquals("53px", propertyValues.getSpecifiedValue(
                StylePropertyDetails.WIDTH).getStandardCSS());
        assertEquals("41px", propertyValues.getSpecifiedValue(
                StylePropertyDetails.HEIGHT).getStandardCSS());

        final CSSRemappingRule widthRule =
                createCSSRemappingRule("width", "width");
        final CSSRemappingRule heightRule =
                createCSSRemappingRule("height", "height");

        final ElementRuleContainer imgElementRuleContainer =
                new ElementRuleContainer("img",
                Arrays.asList(new CSSRemappingRule[]
                        {widthRule, heightRule}));
        imgElementRuleContainer.apply(imgElement);

        assertEquals("53px", imgElement.getAttributeValue("width"));
        assertEquals("41px", imgElement.getAttributeValue("height"));
    }

    public void testRulesAreAppliedToCorrectElement() throws Exception {

        final Element imgElement = createImgElement();

        assertNull(imgElement.getAttributeValue("width"));
        assertNull(imgElement.getAttributeValue("height"));

        final CSSRemappingRule widthRule =
                createCSSRemappingRule("width", "width");
        final CSSRemappingRule heightRule =
                createCSSRemappingRule("height", "height");

        final ElementRuleContainer objectElementRuleContainer =
                new ElementRuleContainer("object",
                Arrays.asList(new CSSRemappingRule[]
                        {widthRule, heightRule}));
        objectElementRuleContainer.apply(imgElement);

        assertNull("<object/> remapper has no effect on <img/> elements",
                imgElement.getAttributeValue("width"));
        assertNull("<object/> remapper has no effect on <img/> elements",
                imgElement.getAttributeValue("height"));
    }

}
