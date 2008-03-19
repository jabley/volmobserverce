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
 * $Header:$
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 01-Jun-03    Geoff           VBM:2003042906 - Implement shard link costing.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.wbdom.dissection.io;

import com.volantis.mcs.dissection.DissectionException;
import com.volantis.mcs.dissection.dom.Accumulator;
import com.volantis.mcs.dissection.links.ShardLinkDetails;
import com.volantis.mcs.wbdom.AttributesInternalIterator;
import com.volantis.mcs.wbdom.ChildrenInternalIterator;
import com.volantis.mcs.wbdom.EmptyElementType;
import com.volantis.mcs.wbdom.VisitorAttributesIterator;
import com.volantis.mcs.wbdom.VisitorChildrenIterator;
import com.volantis.mcs.wbdom.WBDOMElement;
import com.volantis.mcs.wbdom.WBDOMException;
import com.volantis.mcs.wbdom.WBDOMText;
import com.volantis.mcs.wbdom.WBDOMVisitor;
import com.volantis.mcs.wbsax.StringReferenceFactory;
import com.volantis.mcs.wbsax.WBSAXException;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.synergetics.localization.ExceptionLocalizer;

/**
 * A implementation of {@link WBDOMVisitor} which visits a section of
 * dissectable WBDOM in order to calculate its size when rendered into a WBXML
 * output stream.
 * <p>
 * NOTE: This is is only use for sizing the contents of shard links as
 * the dissector is responsible for visiting the rest of the DOM it manages.
 * <p>
 * Yes, this is inconsistent.
 */
public class WBSAXSizingVisitor extends AbtractSizer implements WBDOMVisitor {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    /**
     * Used for logging
     */
    private static final LogDispatcher logger = 
            LocalizationFactory.createLogger(WBSAXSizingVisitor.class);

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
                LocalizationFactory.createExceptionLocalizer(WBSAXSizingVisitor.class);

    /**
     * The dissector object which we collect the sizing information in.
     */
    protected Accumulator accumulator;

    //
    // Child objects which we delegate to in order to size the various
    // different components of a WBDOM. These are often visitors themselves.
    //

    /**
     * Iterating sizer for attributes.
     */
    protected AttributesInternalIterator attributesIterator;

    /**
     * Iterating sizer for element children.
     */
    protected ChildrenInternalIterator childrenIterator;

    /**
     * Visiting sizer for element and attribute names.
     */
    protected WBSAXNameSizer nameSizeSummer;

    /**
     * Visiting sizer for element values.
     */
    protected WBSAXValueSizeVisitor elementValueSummer;

    /**
     * Visiting sizer for attribute values.
     */
    protected WBSAXAttributeValueSizer attributeValueSummer;

    public WBSAXSizingVisitor(final Accumulator accumulator,
            ShardLinkDetails shardLinkDetails,
            StringReferenceFactory inputReferences) {

        super(accumulator);

        nameSizeSummer = new WBSAXNameSizer(accumulator);
        attributeValueSummer = new DissectionWBSAXAttributeValueSizer(
                accumulator, shardLinkDetails, inputReferences);
        elementValueSummer = new WBSAXValueSizeVisitor(accumulator);

        attributesIterator = new VisitorAttributesIterator(
                nameSizeSummer, attributeValueSummer) {
            public void after() throws WBDOMException {
                try {
                    // Cost the attributes END token.
                    costToken();
                } catch (WBSAXException e) {
                    throw new WBDOMException(e);
                }
            }
        };

        childrenIterator = new VisitorChildrenIterator(this) {
            public void after() throws WBDOMException {
                try {
                    // Cost the content END token.
                    costToken();
                } catch (WBSAXException e) {
                    throw new WBDOMException(e);
                }
            }
        };
    }

    // Inherit Javadoc.
    public void visitElement(WBDOMElement element) throws WBDOMException {
        boolean hasAttributes = element.hasAttributes();
        boolean hasContent = element.hasChildren() ||
                element.getEmptyType() == EmptyElementType.StartAndEndTag;
        // Size the start of the element.
        element.accept(nameSizeSummer);
        // Size the attributes.
        if (hasAttributes) {
            element.forEachAttribute(attributesIterator);
        }
        // Size the child content.
        if (hasContent) {
            // Note: it is legal to have 0 content elements, so we still
            // have to add one for the end content.
            element.forEachChild(childrenIterator);
        }
    }

    // Inherit Javadoc.
    public void visitText(WBDOMText text) throws WBDOMException {
        try {
            // Serialise the values inside the text.
            text.getBuffer().accept(elementValueSummer);
        } catch (WBSAXException e) {
            // MCSWD0002X="Error sizing element value"
            throw new WBDOMException(
                        exceptionLocalizer.format(
                                    "wbdom-attribute-value-sizing-error"),
                        e);
        }
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/5	doug	VBM:2004111702 Refactored Logging framework

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 09-Sep-03	1336/1	geoff	VBM:2003090301 Provide support for StringLiteral in WMLC

 27-Aug-03	1286/1	geoff	VBM:2003082101 Clean up wbdom.dissection (merge from Proteus)

 26-Aug-03	1284/1	geoff	VBM:2003082101 Clean up wbdom.dissection (merge from Mimas)

 26-Aug-03	1278/1	geoff	VBM:2003082101 Clean up wbdom.dissection (merge from Metis)

 26-Aug-03	1238/2	geoff	VBM:2003082101 Clean up wbdom.dissection

 15-Jul-03	804/2	geoff	VBM:2003070405 merge from metis with manual renames and copies

 15-Jul-03	798/6	geoff	VBM:2003070405 commit the clean up, athough I am not finished yet

 14-Jul-03	781/1	geoff	VBM:2003070404 clean up WBSAX

 10-Jul-03	751/2	geoff	VBM:2003070703 second go at cleaning up WBDOM test cases

 27-Jun-03	559/4	geoff	VBM:2003060607 make WML use protocol configuration again

 13-Jun-03	372/1	chrisw	VBM:2003060609 Implement wmlc url optimiser

 12-Jun-03	368/1	geoff	VBM:2003061006 Enhance WBDOM to support string references

 ===========================================================================
*/
