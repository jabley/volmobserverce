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
 * $Header: /src/voyager/com/volantis/mcs/protocols/html/HTMLVersion4_0_IE6.java,v 1.1.4.3 2003/04/17 08:23:51 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 29-May-02    Chris W         VBM:2002082907 - Added this protocol in order
 *                              to correct IE6 whose default text alignment is
 *                              centred not left.
 * 11-Oct-02    Chris W         VBM:2002082907 - All methods modified to only
 *                              add alignment attributes to the td tag when the
 *                              VolantisAttribute contains no stylesheet info
 * 20-Mar-03    sumit           VBM:2003031809 - Wrapped logger.debug
 *                              statements in if(logger.isDebugEnabled()) block
 * 16-Apr-03    Geoff           VBM:2003041603 - Add declaration for
 *                              ProtocolException where necessary.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols.html;

import com.volantis.mcs.dom.Element;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.GridChildAttributes;
import com.volantis.mcs.protocols.ProtocolConfiguration;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.ProtocolSupportFactory;
import com.volantis.mcs.protocols.TableCellAttributes;
import com.volantis.mcs.themes.properties.DisplayKeywords;
import com.volantis.styling.Styles;
import com.volantis.synergetics.log.LogDispatcher;

/**
 * This is a sub-class of the HTML v4.0 protocol class to cater for
 * IE6. By default IE6 aligns text in the centre of a table cell, not on the left.
 */
public class HTMLVersion4_0_IE6
        extends HTMLVersion4_0_IE {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
                LocalizationFactory.createLogger(HTMLVersion4_0_IE6.class);


    private final String ALIGN_HORIZONTAL = "align";
    private final String ALIGN_VERTICAL = "valign";

    public HTMLVersion4_0_IE6(ProtocolSupportFactory supportFactory,
            ProtocolConfiguration configuration) {
        super(supportFactory, configuration);
    }

    public String toString() {
        return "HTMLVersion4_0_IE6";
    }

    // Javadoc inherited from super class
    protected void addPaneCellAttributes(Element element, Styles styles) {

        super.addPaneCellAttributes(element, styles);

        updateAlignAttributes(element, styles);
    }

    // Javadoc inherited from super class.
    protected void openGridChild(DOMOutputBuffer dom,
                                 GridChildAttributes attributes) {

        Element element = dom.openStyledElement("td", attributes,
                DisplayKeywords.TABLE_CELL);
        updateAlignAttributes(element, attributes.getStyles());

        // allow subclasses to specify additional attributes.
        addGridChildAttributes(element, attributes);
    }


    // JavaDoc inherited from super class
    protected void addTableCellAttributes(Element element,
                                          TableCellAttributes attributes)
            throws ProtocolException {

        // Add the super classes attributes first.
        super.addTableCellAttributes(element, attributes);

        updateAlignAttributes(element, attributes.getStyles());
    }

    /**
     * Update the align attributes for the particular element in the following
     * manner:
     * o If there is a style class on the element that has an alignment
     *   set or the alignment attribute has been set already then do not set
     *   the default alignment attribute.
     * o Otherwise set the value of the alignment as necessary (default to
     *   valign=center and align=left if nothing has been specified).
     *
     * @param element    the element that will be used to set the alignment's
     *                   attribute value.
     * @param styles     the styles used to determine the value of the
     *                   alignment.
     */
    private void updateAlignAttributes(final Element element,
                                       final Styles styles) {

        String value;

        if ((value = horizontalAlignHandler.getAsString(styles)) != null) {
            element.setAttribute ("align", value);
        }
        if ((value = verticalAlignHandler.getAsString(styles)) != null) {
            element.setAttribute ("valign", value);
        }

        // Update the horizontal alignment attribute.
        value = horizontalAlignHandler.getAsString(styles);
        updateAlignment(element, ALIGN_HORIZONTAL, "left", value);

        // Update the vertical alignment attribute.
        value = verticalAlignHandler.getAsString(styles);
        updateAlignment(element, ALIGN_VERTICAL, "center", value);

    }

    /**
     * Update the element alignment attribute value with the specified
     * criteria. This is a helper method used by {@link
     * #updateAlignAttributes}.
     *
     * @param element       the element to remove or set attribute values on.
     * @param attributeName the attributeName of the attribute to set update.
     * @param defaultValue  the default alignment value (center or left)
     * @param actualValue   the actual alignment value (middle or right or ...)
     */
    protected void updateAlignment(Element element,
                                   final String attributeName,
                                   final String defaultValue,
                                   final String actualValue) {

        String currentValue = element.getAttributeValue(attributeName);
        if (currentValue == null) {
            // No value set for attribute.
            if (actualValue != null) {
                // We have an actual value so use that value.
                element.setAttribute(attributeName, actualValue);
            } else {
                // We have no attribute and no actual value, so set the
                // attribute value to the default.
                if (logger.isDebugEnabled()) {
                    logger.debug("Using default attribute value '" +
                                 defaultValue + "' for element: " + element);
                }
                element.setAttribute(attributeName, defaultValue);
            }
        }
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

 09-Jun-05	8665/2	emma	VBM:2005060204 Refactoring in order to annotate DOM with style info

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/2	doug	VBM:2004111702 Refactored Logging framework

 01-Nov-04	6068/1	tom	VBM:2004101508 renamed VolantisAttribute to MCSAttributes and added style properties container

 12-Oct-04	5778/1	adrianj	VBM:2004083106 Provide styling engine API

 16-Sep-03	1416/1	byron	VBM:2003090306 Default valign and align lost on IE6 protocol

 21-Aug-03	1240/1	chrisw	VBM:2003070811 Ported emulation of CSS2 border-spacing from mimas to proteus

 21-Aug-03	1219/3	chrisw	VBM:2003070811 Ported emulation of CSS2 border-spacing from metis to mimas

 20-Aug-03	1152/1	chrisw	VBM:2003070811 Emulate CSS2 border-spacing using cellspacing on table element

 ===========================================================================
*/
