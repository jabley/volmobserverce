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
package com.volantis.mcs.protocols.html;

import com.volantis.mcs.dom.Element;
import com.volantis.mcs.protocols.ColumnIteratorPaneAttributes;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.GridAttributes;
import com.volantis.mcs.protocols.GridChildAttributes;
import com.volantis.mcs.protocols.PaneAttributes;
import com.volantis.mcs.protocols.ProtocolConfiguration;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.ProtocolSupportFactory;
import com.volantis.mcs.protocols.SpatialFormatIteratorAttributes;
import com.volantis.mcs.protocols.TableAttributes;
import com.volantis.styling.Styles;

/**
 * Implementation of the XHTML Mobile Profile protocol for the Sony Ericsson
 * browsers.
 */
public class XHTMLMobile1_0_SE_P extends XHTMLMobile1_0 {

    /**
     * Initializes the new instance.
     */
    public XHTMLMobile1_0_SE_P(ProtocolSupportFactory supportFactory,
            ProtocolConfiguration configuration) {
        super(supportFactory, configuration);
    }

    // javadoc inherited
    protected void addTableAttributes(Element element,
                                      TableAttributes attributes)
            throws ProtocolException {
        super.addTableAttributes(element, attributes);
        
        addDefaultCellspacing(element);
    }

    // javadoc inherited
    protected void addGridAttributes(Element element,
                                     GridAttributes attributes) {
        super.addGridAttributes(element, attributes);

        addDefaultCellspacing(element);

        addWidthAttribute(element, attributes.getStyles());
    }

    // javadoc inherited
    protected void addGridChildAttributes(
            final Element element,
            final GridChildAttributes attributes) {
        int row = attributes.getRow();

        super.addGridChildAttributes(element, attributes);

        // Only add the width attribute on the cells in the first row to
        // avoid unnecessary page weight
        if (row == 0) {
            addWidthAttribute(element, attributes.getStyles());
        }
    }

    // javadoc inherited
    protected void addPaneTableAttributes(Element element,
                                          PaneAttributes attributes) {
        super.addPaneTableAttributes(element, attributes);

        addDefaultCellspacing(element);

        addWidthAttribute(element, attributes.getStyles());
    }

    // javadoc inherited
    protected void addColumnIteratorPaneAttributes(
            Element element, ColumnIteratorPaneAttributes attributes) {
        super.addColumnIteratorPaneAttributes(element, attributes);

        addDefaultCellspacing(element);

        addWidthAttribute(element, attributes.getStyles());
    }

    // javadoc inherited
    protected void openSpatialFormatIterator(
            DOMOutputBuffer dom,
            SpatialFormatIteratorAttributes attributes) {
        super.openSpatialFormatIterator(dom, attributes);

        // The current element is table.
        Element element = dom.getCurrentElement();

        addDefaultCellspacing(element);

        addWidthAttribute(element, attributes.getStyles());
    }

    /**
     * Helper method which adds a cellspacing attribute with a value of 0 if
     * the attribute does not already exist.
     *
     * @param table the table element onto which the default cellspacing
     *              attribute should be added if necessary
     */
    private void addDefaultCellspacing(Element table) {
        if (table.getAttributeValue("cellspacing") == null) {
            table.setAttribute("cellspacing", "0");
        }
    }

    /**
     * The width (accounting units) specified in the dimensional attributes
     * is added to the given element in order to work around the device
     * limitation whereby widths in CSS are not applied.
     *
     * @param element               the element rendered to represent the
     *                              format to which
     */
    private void addWidthAttribute(
            final Element element,
            final Styles styles) {

        String width = widthHandler.getAsString(styles);
        if (width != null) {
            element.setAttribute("width", width);
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 01-Sep-05	9375/1	geoff	VBM:2005082301 XDIMECP: clean up protocol creation

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 15-Jun-05	8784/1	philws	VBM:2005061402 Port SE P800/P900 width attribute from 3.3.1

 14-Jun-05	8779/2	philws	VBM:2005061402 Port SE P800/P900 width rendering from 3.2.3

 14-Jun-05	8756/1	philws	VBM:2005061402 Provide markup layout width rendering for SE P800/P900 phones to resolve device CSS deficiency

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/1	doug	VBM:2004111702 Refactored Logging framework

 12-Oct-04	5778/1	adrianj	VBM:2004083106 Provide styling engine API

 06-Sep-04	5361/4	pcameron	VBM:2004082517 New subprotocol for SE P800/P900 devices which adds cellspacing=0 to all tables

 03-Sep-04	5331/5	pcameron	VBM:2004082517 New subprotocol for SE P800/P900 devices which adds cellspacing=0 to all tables

 ===========================================================================
*/
