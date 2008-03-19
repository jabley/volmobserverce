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
package com.volantis.mcs.protocols.html;

import com.volantis.mcs.dom.Element;
import com.volantis.mcs.protocols.BodyAttributes;
import com.volantis.mcs.protocols.ProtocolConfiguration;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.ProtocolSupportFactory;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.styling.Styles;
import com.volantis.styling.properties.StyleProperty;

/**
 * This class is responsible for providing an HTML 3.2 protocol class with
 * specific behaviour for Pocket Internet Explorer.
 */
public class HTMLVersion3_2_PIE extends HTMLVersion3_2 {

    /**
     * Non standard HTML 3.2 body attribute introduced by Microsoft and
     * only recognised by IE.
     * <p>
     * Specifies the left margin in pixels for the entire body of the page.
     */
    private static final String LEFT_MARGIN = "leftmargin";

    /**
     * Non standard HTML 3.2 body attribute introduced by Microsoft and
     * only recognised by IE.
     * <p>
     * Specifies the right margin in pixels for the entire body of the page.
     */
    private static final String RIGHT_MARGIN = "rightmargin";

    /**
     * Non standard HTML 3.2 body attribute introduced by Microsoft and
     * only recognised by IE.
     * <p>
     * Specifies the top margin in pixels for the entire body of the page.
     */
    private static final String TOP_MARGIN = "topmargin";

    /**
     * Non standard HTML 3.2 body attribute introduced by Microsoft and
     * only recognised by IE.
     * <p>
     * Specifies the bottom margin in pixels for the entire body of the page.
     */
    private static final String BOTTOM_MARGIN = "bottommargin";

    /**
     * Initialises an instance.
     */
    public HTMLVersion3_2_PIE(ProtocolSupportFactory supportFactory,
            ProtocolConfiguration configuration) {
        super(supportFactory, configuration);
    }

    /**
     * Adds the supplied attributes to the supplied body element.
     * Additionally the following attributes will be added:
     * <ul>
     *  <li> leftmargin </li>
     *  <li> rightmargin </li>
     *  <li> topmargin </li>
     *  <li> bottommargin </li>
     * </ul>
     * The values for these attributes will be retrieved
     * from the Theme associated with the current canvas.  If no corresponding
     * values exists then the default of zero will be used.
     *
     * @param element the body element to be added to.
     * @param attributes the attributes to be added.
     * @throws ProtocolException
     */
    protected void addBodyAttributes(Element element,
            BodyAttributes attributes) throws ProtocolException {

        // Add the super class attributes.
        super.addBodyAttributes(element, attributes);
        setPageMargins(element, attributes);
    }

    /**
     * Sets the following attributes with the corresponding values found
     * in the Theme associated with the current canvas.  If no such values
     * exist then the default of zero is used.
     *
     * <ul>
     *  <li> leftmargin </li>
     *  <li> rightmargin </li>
     *  <li> topmargin </li>
     *  <li> bottommargin </li>
     * </ul>
     *
     * @param element The body element whose margins are to be set.
     * @param attributes attributes used to obtain styling information.
     */
    private void setPageMargins(Element element,
                                BodyAttributes attributes) {

        Styles styles = attributes.getStyles();

        // Have the margins been set by the styling associated
        // with the current canvas?  If so we want to preserve these
        // values.
        addMarginAttribute(element, TOP_MARGIN, styles,
                           StylePropertyDetails.MARGIN_TOP);
        addMarginAttribute(element, BOTTOM_MARGIN, styles,
                           StylePropertyDetails.MARGIN_BOTTOM);
        addMarginAttribute(element, LEFT_MARGIN, styles,
                           StylePropertyDetails.MARGIN_LEFT);
        addMarginAttribute(element, RIGHT_MARGIN, styles,
                           StylePropertyDetails.MARGIN_RIGHT);
    }

    private void addMarginAttribute(
            Element element, String attribute, Styles styles,
            StyleProperty property) {

        String value = getMargin(styles, property);
        addMarginAttribute(element, attribute, value);
    }


    /**
     * Helper method to assign a value to a margin attribute.
     * <p>
     * If value is null then the supplied attribute will be set to zero.
     *
     * @param element the element to have the attribute added.
     * @param attribute the attribute to add.
     * @param value the attribute value.
     */
    private void addMarginAttribute(Element element, String attribute, String value) {

        // If the value is null then default to 0.
        if (value == null) {
            value = "0";
        }
        element.setAttribute(attribute, value);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 01-Sep-05	9375/1	geoff	VBM:2005082301 XDIMECP: clean up protocol creation

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 27-May-05	8556/1	rgreenall	VBM:2005050503 Merge from 323

 26-May-05	8503/1	rgreenall	VBM:2005050503 Added HTML3.2 Protocol supporting additional behaviour required by Pocket Internet Explorer.

 ===========================================================================
*/
