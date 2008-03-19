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
import com.volantis.mcs.themes.StyleValue;
import com.volantis.synergetics.log.LogDispatcher;

import java.util.ArrayList;

/**
 * Abstract class containing all functionallity common to renderers
 * that use inline CSS in HTML 3.2 markup.
 */
public abstract class HTML3_2CSSPropertyRenderer implements StyleEmulationPropertyRenderer {

    private static final LogDispatcher logger =
        LocalizationFactory.createLogger(HTML3_2CSSPropertyRenderer.class);

    /**
     * CSS property name added to the inline CSS style
     */
    private final String cssPropertyName;

    /**
     * CSS property name given when this renderer is registered. May equal
     * the cssPropertyName.
     */
    private final String registeredPropertyName;



    private static final ArrayList validElementNames = new ArrayList();

    static {
        validElementNames.add("table");
        validElementNames.add("td");
    }

    /**
     * Initialise a new instance with the supplied parameters.
     *
     * @param propertyName the border width property name.
     */
    protected HTML3_2CSSPropertyRenderer(String propertyName, String registeredName) {
        cssPropertyName = propertyName;
        registeredPropertyName = registeredName;
    }

    /**
     * Apply the given style value to the given element.
     *
     * @param element the curent element
     * @param value the value that is to be renderered in an
     * appropriate manner.
     */
    public void apply(Element element, StyleValue value) {

        if (element == null) {
            throw new NullPointerException("Element can not be null");
        }

        if (validElementNames.contains(element.getName())) {

            if (logger.isDebugEnabled()) {
                logger.debug("Applying style " + registeredPropertyName +
                    ":" + value + " to " + element.getName() + " element");
            }

            String styleValueAsString = value.getStandardCSS();

            if (!styleValueAsString.equals("0")) {
                applyStyleToTableElement(element, styleValueAsString);
            }
        } else {
            if (logger.isDebugEnabled()) {
                logger.debug("Can not apply style " + registeredPropertyName +
                    ":" + value + " to " + element.getName() + " element");
            }

        }
    }

    /**
     * Apply the given style value to the table element, subclasses need to provide
     * and implementation of this method.
     *
     * @param element to apply style to
     * @param value to apply to table element
     */
    protected abstract void applyStyleToTableElement(Element element, String value);

    /**
     * Add the given StyleValue to the given <td> element.
     *
     * @param styleProperty to add to the element
     * @param td element to add the style to
     */
    protected void addStyleToTd(String styleProperty, Element td) {

        String existingStyle = td.getAttributeValue("style");
        if (existingStyle == null) {
            existingStyle = "";
        }

        if (styleProperty!=null && !styleProperty.equals("0")) {
            String styleValue = existingStyle + cssPropertyName + ":" +
                styleProperty + "; ";

            td.setAttribute("style", styleValue);
        }
    }

    /**
     * Return true if the given element has one child.
     *
     * @param element
     * @return true if element has one child
     */
    protected boolean hasOneChild(Element element) {
        return element.getHead() != null &&
            element.getHead() == element.getTail();
    }
}
