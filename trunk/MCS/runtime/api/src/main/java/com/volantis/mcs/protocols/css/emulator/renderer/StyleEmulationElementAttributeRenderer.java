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
package com.volantis.mcs.protocols.css.emulator.renderer;

import com.volantis.mcs.dom.Element;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.DOMHelper;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.synergetics.log.LogDispatcher;

/**
 * A renderer for an element that has attributes. If the element name is null
 * the element cannot and will not be opened or closed.
 */
public class StyleEmulationElementAttributeRenderer
        implements StyleEmulationPropertyRenderer {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(
                    StyleEmulationElementAttributeRenderer.class);

    /**
     * The element to open if necessary, or null if one should not be opened.
     */
    protected final String elementName;

    /**
     * The attribute name to set (may be null).
     */
    protected final String attributeName;

    /**
     * Renderer for the attribute value.
     */
    protected final StyleEmulationAttributeValueRenderer attributeValueRenderer;

    /**
     * Construct the rule with the element and attributes.
     *
     * @param elementName   the element name.
     * @param attributeName the set of attributes.
     */
    public StyleEmulationElementAttributeRenderer(
            String elementName, String attributeName,
            StyleEmulationAttributeValueRenderer valueRenderer) {
        this.elementName = elementName;
        this.attributeName = attributeName;
        this.attributeValueRenderer = valueRenderer;
    }

    /**
     * We would like to add a new element to the current element, but we do
     * this if and only if the element matching the elementName hasn't been
     * added already.
     */
    public void apply(Element element, StyleValue value) {

        Element currentElement = element;

        if ((elementName != null) &&
                !elementName.equals(currentElement.getName())) {
            if (elementName != null) {
                currentElement = DOMHelper
                        .insertChildElement(currentElement, elementName);
            }
        }

        processAttribute(currentElement, attributeName, value);
    }


    /**
     * Processs the attribute name and set the attribute value as required.
     *
     * @param element       the element to set the attributes value on.
     * @param attributeName the attribute name to set.
     * @param value         the style value to set.
     */
    protected void processAttribute(
            Element element, String attributeName,
            StyleValue value) {

        String renderedValue = attributeValueRenderer.render(value);

        if (renderedValue != null) {
            setAttributeValue(element, attributeName, renderedValue);
        }
    }

    /**
     * Set the attribute value form the attribute.
     *
     * @param element   the element to set the attributes value on.
     * @param attribute the attribute.
     * @param value     the value to set.
     */
    protected void setAttributeValue(
            Element element, String attribute,
            String value) {

        if ((element != null) && (attribute != null) && (value != null)) {
            element.setAttribute(attribute, value);
        } else {
            logger.warn("attribute-value-incorrect",
                    new Object[]{attribute, value});
        }
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 14-Oct-05	9825/1	pduffin	VBM:2005091502 Corrected device name and made use of new stylistic property.

 20-Sep-05	9128/1	pabbott	VBM:2005071114 Add XHTML 2 elements

 22-Aug-05	9184/2	pabbott	VBM:2005080508 Move CSS eumlator to post process step

 10-Aug-05	9211/1	pabbott	VBM:2005080902 End to End CSS emulation test

 03-Aug-05	8923/1	pabbott	VBM:2005063010 End to End CSS emulation test

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 02-Mar-05	7240/1	emma	VBM:2005022812 mergevbm from MCS 3.3

 02-Mar-05	7214/1	emma	VBM:2005022812 Fixing leftover localization logging problems

 09-Dec-04	6417/1	philws	VBM:2004120703 Committing tidy up

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6232/6	doug	VBM:2004111702 refactored logging framework

 29-Nov-04	6332/1	doug	VBM:2004112913 Refactored logging framework

 29-Nov-04	6232/4	doug	VBM:2004111702 Refactored Logging framework

 12-Oct-04	5778/1	adrianj	VBM:2004083106 Provide styling engine API

 20-Jul-04	4897/4	geoff	VBM:2004071407 Implementation of theme style options: finalise rule processing

 29-Jun-04	4720/9	byron	VBM:2004061604 Core Emulation Facilities - rename and move classes

 29-Jun-04	4720/7	byron	VBM:2004061604 Core Emulation Facilities - rename and move classes

 28-Jun-04	4720/3	byron	VBM:2004061604 Core Emulation Facilities - rework issues

 25-Jun-04	4720/1	byron	VBM:2004061604 Core Emulation Facilities

 ===========================================================================
*/
