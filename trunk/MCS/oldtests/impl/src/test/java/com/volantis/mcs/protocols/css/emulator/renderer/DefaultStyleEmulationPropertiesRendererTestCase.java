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
package com.volantis.mcs.protocols.css.emulator.renderer;

import com.volantis.mcs.dom.DOMFactory;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.debug.DOMUtilities;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.StyleValueFactory;
import com.volantis.mcs.themes.values.LengthUnit;
import com.volantis.styling.StylingFactory;
import com.volantis.styling.properties.StylePropertyDefinitions;
import com.volantis.styling.values.MutablePropertyValues;
import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * This class is responsible for testing the behaviour of
 * {@link DefaultStyleEmulationPropertiesRenderer}.
 *
 * @todo greater test coverage is required here.
 */
public class DefaultStyleEmulationPropertiesRendererTestCase
        extends TestCaseAbstract {

    public void testSingleAttributeOnlyStyleEmulationRenderer()
            throws Exception {

        DefaultStyleEmulationPropertiesRenderer propertiesRenderer =
                new DefaultStyleEmulationPropertiesRenderer();


        StyleEmulationPropertyRenderer renderer =
                new StyleEmulationElementAppendAttributeRenderer(
                        new String[]{"td"}, "style",
                        new CSSBorderWidthPropertyRenderer("border-left-width"));

        AttributeOnlyStyleEmulationPropertyRenderer borderLeftColorRenderer =
                new AttributeOnlyStyleEmulationPropertyRenderer(renderer);

        propertiesRenderer.register(StylePropertyDetails.BORDER_LEFT_WIDTH,
                borderLeftColorRenderer);

        Element tdElement = createElement("td");


        StyleValue width = StyleValueFactory.getDefaultInstance()
                .getLength(null, 15, LengthUnit.PX);

        MutablePropertyValues propertyValues = createPropertyValues();
        propertyValues.setComputedValue(StylePropertyDetails.BORDER_LEFT_WIDTH,
                width);

        propertiesRenderer.applyProperties(tdElement, propertyValues);

        assertEquals("Buffer should match",
                "<td style=\"border-left-width:15px;\"/>",
                DOMUtilities.toString(tdElement));


    }

    public void testEmulatingMultipleProperties()
            throws Exception {

        DefaultStyleEmulationPropertiesRenderer propertiesRenderer =
                new DefaultStyleEmulationPropertiesRenderer();


        StyleEmulationPropertyRenderer renderer =
                new StyleEmulationElementAppendAttributeRenderer(
                        new String[]{"td"}, "style",
                        new CSSBorderWidthPropertyRenderer("border-left-width"));

        AttributeOnlyStyleEmulationPropertyRenderer borderLeftWidthRenderer =
                new AttributeOnlyStyleEmulationPropertyRenderer(renderer);

        propertiesRenderer.register(StylePropertyDetails.BORDER_LEFT_WIDTH,
                borderLeftWidthRenderer);

        StyleEmulationPropertyRenderer renderer2 =
                new StyleEmulationElementAppendAttributeRenderer(
                        new String[]{"td"}, "style",
                        new CSSBorderWidthPropertyRenderer(
                                "border-right-width"));

        AttributeOnlyStyleEmulationPropertyRenderer borderRightWidthRenderer =
                new AttributeOnlyStyleEmulationPropertyRenderer(renderer2);

        propertiesRenderer.register(StylePropertyDetails.BORDER_RIGHT_WIDTH,
                borderRightWidthRenderer);


        Element tdElement = createElement("td");

        StyleValue width = StyleValueFactory.getDefaultInstance()
                .getLength(null, 15, LengthUnit.PX);

        MutablePropertyValues propertyValues = createPropertyValues();
        propertyValues.setComputedValue(
                StylePropertyDetails.BORDER_RIGHT_WIDTH,
                width);
        propertyValues.setComputedValue(StylePropertyDetails.BORDER_LEFT_WIDTH,
                width);


        propertiesRenderer.applyProperties(tdElement, propertyValues);

        assertEquals("Elements should match",
                "<td style=\"border-left-width:15px;border-right-width:15px;\"/>",
                DOMUtilities.toString(tdElement));
    }

    /**
     * Factory method to create a new element with the specified name.
     *
     * @param name the name of the new element.
     * @return an element initialised with the supplied name.
     */
    private Element createElement(String name) {
        return DOMFactory.getDefaultInstance().createElement(name);
    }

    /**
     * Create a set of mutable property values.
     *
     * @return A newly created set of mutable property values.
     */
    private MutablePropertyValues createPropertyValues() {
        StylePropertyDefinitions definitions =
                StylePropertyDetails.getDefinitions();
        return StylingFactory.getDefaultInstance()
                .createPropertyValues(definitions);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 22-Nov-05	10381/2	rgreenall	VBM:2005110710 Parameterize HTML 3.2 protocol to output border styling if supported by the requesting device.

 21-Nov-05	10377/1	rgreenall	VBM:2005110710 Parameterize HTML 3.2 protocol to output border styling if supported by the requesting device.

 17-Nov-05	10251/1	rgreenall	VBM:2005110710 Parameterize HTML 3.2 protocol to output border styling if supported by the requesting device.

 ===========================================================================
*/
