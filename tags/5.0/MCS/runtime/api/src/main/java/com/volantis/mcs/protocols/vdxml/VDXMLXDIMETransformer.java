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
package com.volantis.mcs.protocols.vdxml;

import com.volantis.mcs.dom.DOMFactory;
import com.volantis.mcs.dom.DOMVisitor;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.RecursingDOMVisitor;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.synergetics.log.LogDispatcher;

/**
 * Performs the conversion of XDIME generated pseudo markup into valid VDXML.
 * The {@link #transform} method will be invoked against each container of
 * relevant markup within the document to be transformed. Note that each
 * container must have already had geometry applied to it and must still be
 * defined in terms of the original generated markup (be it pseudo or real).
 */
public final class VDXMLXDIMETransformer {
    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(VDXMLXDIMETransformer.class);

    /**
     * A special key set as the associated object on any panes that are
     * effectively deleted from the DOM. Allows callers to find out if this
     * transformer has removed the specified pane from the DOM
     */
    public static final Object PANE_REMOVED = new Object();

    /**
     * Ripped off version of the WMLDOMTransformer which just promotes all
     * the pseudo block elements to the top level within each display context.
     * The document writer will then add BRs after each block.
     */
    private static final VDXMLBlockTransformer BLOCK_TRANSFORMER =
            new VDXMLBlockTransformer();

    /**
     * Actually does the processing of transformation.
     *
     * <p>The transformation involves:</p>
     *
     * <ul>
     *
     * <li>finding {@link VDXMLConstants#INPUT_FIELD_ELEMENT input fields} and
     * merging them into the containing {@link
     * VDXMLConstants#PSEUDO_PANE_ELEMENT pane} such that the pane's ({@link
     * VDXMLConstants#X_ATTRIBUTE X}, {@link VDXMLConstants#Y_ATTRIBUTE Y})
     * position is copied straight onto the input field and the pane's
     * {@link VDXMLConstants#WIDTH_ATTRIBUTE width} becomes the input field's
     * {@link VDXMLConstants#SIZE_ATTRIBUTE width size}. The input field must
     * be the only child of the pane. Colour and other presentational
     * properties are not inherited by the input field from the pane
     * (consistently with CSS).</li>
     *
     * </ul>
     */
    private static class XDIMEVisitor
            extends RecursingDOMVisitor {

        public void visit(Element element) {
            if (VDXMLConstants.INPUT_FIELD_ELEMENT.equals(element.getName())) {
                // Input fields must be in a pane of their own (there should be
                // no other content)
                if ((element.getNext() != null) ||
                        (element.getPrevious() != null)) {
                    logger.error("form-input-fields-must-target-unused-pane");
                    throw new IllegalStateException(
                            "Form input fields must be targeted at " +
                                    "otherwise unused panes");
                } else if (!VDXMLConstants.PSEUDO_PANE_ELEMENT.equals(
                        element.getParent().getName())) {
                    logger.error("form-input-field-not-in-pane");
                    throw new IllegalStateException(
                            "Form input fields must appear directly " +
                                    "within a pane");
                } else {
                    // Copy the required attribute values across.
                    // Assumes the geometry has been added to the pane
                    Element p = element.getParent();

                    String x = p.getAttributeValue(VDXMLConstants.X_ATTRIBUTE);
                    String y = p.getAttributeValue(VDXMLConstants.Y_ATTRIBUTE);
                    String w =
                            p.getAttributeValue(VDXMLConstants.WIDTH_ATTRIBUTE);
                    int width = Integer.parseInt(w);

                    // Input fields always have a width of 2 greater than that
                    // specified so account for that here.
                    width -= VDXMLConstants.INPUT_FIELD_SIZE_OFFSET;

                    if (width < 0) {
                        logger.error("form-input-field-too-small");
                        throw new IllegalStateException(
                                "Input fields must always be at least " +
                                        VDXMLConstants.INPUT_FIELD_SIZE_OFFSET +
                                        " characters in length");
                    }

                    w = Integer.toString(width);
                    String bc = p.getAttributeValue(VDXMLConstants.
                            BACKGROUND_COLOUR_ATTRIBUTE);

                    element.setAttribute(VDXMLConstants.X_ATTRIBUTE, x);
                    element.setAttribute(VDXMLConstants.Y_ATTRIBUTE, y);
                    element.setAttribute(VDXMLConstants.SIZE_ATTRIBUTE, w);

                    if (bc != null) {
                        element.setAttribute(VDXMLConstants.
                                BACKGROUND_COLOUR_ATTRIBUTE,
                                bc);
                    }

                    // Move the input field up to replace the pane in the DOM
                    element.replace(p);

                    // Now indicate to the caller that the pane is no longer
                    // used in the DOM
                    p.setObject(PANE_REMOVED);

                    // Visit the content of the current element in case other
                    // processing is needed
                    element.forEachChild(this);
                }
            } else {
                // Visit the content of the current element in case other
                // processing is needed
                element.forEachChild(this);
            }
        }
    }

    /**
     * This method should be invoked with the elements that contain the XDIME
     * generated pseudo markup that needs to be transformed into valid VDXML.
     *
     * @param factory the factory to use to create nodes in the document
     * @param element the element containing the XDIME generation pseudo
     *                markup that needs transforming; always a {@link
     *                VDXMLConstants#PSEUDO_PANE_ELEMENT}
     */
    public void transform( DOMFactory factory, Element element) {
        DOMVisitor transformer = new XDIMEVisitor();

        BLOCK_TRANSFORMER.transform(factory, element);

        transformer.visit(element);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 21-Jun-05	8856/1	geoff	VBM:2005062005 Refactoring for XDIMECP: Generate optimised CSS for a DOM.

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/2	doug	VBM:2004111702 Refactored Logging framework

 12-Oct-04	5778/1	adrianj	VBM:2004083106 Provide styling engine API

 23-Sep-04	5599/1	geoff	VBM:2004092214 Port VDXML to MCS: port existing protocol code

 07-Jun-04	4483/12	philws	VBM:2004051807 Tidy up Claire's code (standards)

 07-Jun-04	4483/10	philws	VBM:2004051807 Input field resizing to account for Minitel behaviour

 04-Jun-04	4483/8	philws	VBM:2004051807 Help zone and input field colour fixes

 28-May-04	4575/1	geoff	VBM:2004051807 Minitel VDXML protocol support (block element support)

 28-May-04	4483/6	philws	VBM:2004051807 Fix form text block handling

 28-May-04	4483/4	philws	VBM:2004051807 Fix pane <-> input field mapping

 27-May-04	4495/1	claire	VBM:2004051807 Very basic VDXML form handling

 25-May-04	4483/1	philws	VBM:2004051807 Initial VDXML transformer implementation

 ===========================================================================
*/
