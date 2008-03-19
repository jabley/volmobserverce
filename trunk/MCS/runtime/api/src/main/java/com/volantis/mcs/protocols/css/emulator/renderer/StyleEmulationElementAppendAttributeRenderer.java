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

import com.volantis.mcs.dom.Element;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.synergetics.log.LogDispatcher;

/**
 * This class is responsible for providing a style emulation renderer that
 * allows multiple style properties to be applied to the style attribute.
 */
public class StyleEmulationElementAppendAttributeRenderer
        extends StyleEmulationElementSetAttributeRenderer {

    /**
     * Constants for the style attribute name - "style".
     */
    private static final String STYLE_ATTRIBUTE = "style";

    /**
     * The logging object to use in this class for localised logging services.
     */
     private static final LogDispatcher logger =
            LocalizationFactory.createLogger(
                    StyleEmulationElementAttributeRenderer.class);

    /**
     * Initialises a new instance with the supplied parameters.
     *
     * @param elements the set of elements to be matched.
     * @param attributeName name of the attribute that will hold values
     * obtained from the {@link StyleEmulationAttributeValueRenderer}.
     * @param valueRenderer the renderer used to obtain a value to be written.
     */
    public StyleEmulationElementAppendAttributeRenderer(
            String [] elements,
            String attributeName,
            StyleEmulationAttributeValueRenderer valueRenderer) {

        super(elements, attributeName, valueRenderer);
    }

    /**
     * Sets the supplied <code>value</code> to the supplied
     * <code>attribute</code> on the supplied <code>element</code>.
     * <p>
     * If the specified attribute exists and has a value set then the value
     * supplied will be appended to the existing value.
     * <p>
     * Note that this class is intended only to be used for writing css
     * properties to the style attribute.  In this context, the style
     * attribute allows multiple values, e.g.
     * <p>
     * style="border-left-width:10px;border-top-color:red;"
     *
     *
     * @param element the element that contains the attribute to be set.
     * @param attribute the name of the attribute to set.
     * @param value the value to be set against the supplied attribute.
     */
    protected void setAttributeValue(Element element, String attribute,
                                     String value) {
        // Check that the supplied values are legal.
        if ((element != null) && (attribute != null) && (value != null)) {

            // Does the specified attribute exist?
            String styleAttributeValue =
                    element.getAttributeValue(STYLE_ATTRIBUTE);
            // If so, does it have a value?
            if (styleAttributeValue != null) {
                // The style attribute has already been set.
                // Lets append supplied value to the existing value.
                StringBuffer styleAttributeBuffer =
                        new StringBuffer(styleAttributeValue);
                styleAttributeBuffer.append(value);

                // Set the new value for the style attribute.
                element.setAttribute(STYLE_ATTRIBUTE,
                                     styleAttributeBuffer.toString());
            } else {
                // no existing value - simply set the supplied value.
                element.setAttribute(attribute, value);
            }
        } else {
            logger.warn("attribute-value-incorrect",
                        new Object[] {attribute, value});
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 21-Nov-05	10377/1	rgreenall	VBM:2005110710 Parameterize HTML 3.2 protocol to output border styling if supported by the requesting device.

 17-Nov-05	10251/1	rgreenall	VBM:2005110710 Parameterize HTML 3.2 protocol to output border styling if supported by the requesting device.

 ===========================================================================
*/
