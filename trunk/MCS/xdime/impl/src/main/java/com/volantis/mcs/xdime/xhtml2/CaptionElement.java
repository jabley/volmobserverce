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
package com.volantis.mcs.xdime.xhtml2;

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.CaptionAttributes;
import com.volantis.mcs.protocols.DivAttributes;
import com.volantis.mcs.protocols.OutputBuffer;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.xdime.XDIMEAttributes;
import com.volantis.mcs.xdime.XDIMEContextInternal;
import com.volantis.mcs.xdime.XDIMEElementInternal;
import com.volantis.mcs.xdime.XDIMEException;
import com.volantis.mcs.xdime.XDIMEResult;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;

/**
 * XHTML V2 Caption element object.
 */
public class CaptionElement extends XHTML2Element {

    /**
     * Used for logging.
     */
    private static final LogDispatcher logger =
        LocalizationFactory.createLogger(CaptionElement.class);

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
        LocalizationFactory.createExceptionLocalizer(CaptionElement.class);

    /**
     * A filter that selects a table or object element.
     */
    private static final ElementUtils.Filter ELEMENTS_WITH_CAPTION_FILTER =
            new ElementUtils.Filter() {
                public boolean matches(XDIMEElementInternal element) {
                    return element instanceof TableElement ||
                            element instanceof ObjectElement;
                }
            };

    /**
     * Output buffer used if this is an object caption.
     */
    private OutputBuffer objectCaptionContent;

    public CaptionElement(XDIMEContextInternal context) {
        super(XHTML2Elements.CAPTION, context);

        protocolAttributes = new CaptionAttributes();
    }

    // Javadoc inherited
    protected XDIMEResult callOpenOnProtocol(
            XDIMEContextInternal context, XDIMEAttributes attributes)
        throws XDIMEException {

        XDIMEElementInternal foundElement1 = ElementUtils.getContainingElement(
                parent, ELEMENTS_WITH_CAPTION_FILTER);

        XDIMEElementInternal foundElement = foundElement1;

        if (foundElement instanceof TableElement) {
            openTableCaption(context, (TableElement)foundElement);

        } else if (foundElement instanceof ObjectElement) {
            openObjectCaption(context);

        } else {
            // This should never happen
            logger.error("internal-error-while-processing-element", getTagName());
            throw new XDIMEException(
                exceptionLocalizer.format(
                    "internal-error-while-processing-element", getTagName()));
        }
        return XDIMEResult.PROCESS_ELEMENT_BODY;

    }

    /**
     * Process a caption inside a table.
     *
     * @param element
     */
    private void openTableCaption(XDIMEContextInternal context, TableElement element)
        throws XDIMEException {

        VolantisProtocol protocol = getProtocol(context);

        try {
            protocol.writeOpenTableCaption((CaptionAttributes)protocolAttributes);
        } catch (ProtocolException e) {
            logger.error("rendering-error", getTagName(), e);

            throw new XDIMEException(exceptionLocalizer.format(
                "rendering-error", getTagName()), e);
        }
    }

    /**
     * Process a caption inside an object. Create a new output buffer to act
     * as a temporary store for the caption.
     *
     * @param context 
     */
    private void openObjectCaption(XDIMEContextInternal context)
            throws XDIMEException {

        objectCaptionContent = createOutputBuffer(context);

        getPageContext(context).pushOutputBuffer(objectCaptionContent);

        // Open the div in that represents the entire caption. We do this
        // because Paul says caption is a block element.
        DivAttributes divAttributes = new DivAttributes();
        divAttributes.copy(protocolAttributes);
        try {
            getProtocol(context).writeOpenDiv(divAttributes);
        } catch (ProtocolException e) {
            throw new XDIMEException(e);
        }
    }

    // Javadoc inherited
    protected void callCloseOnProtocol(XDIMEContextInternal context)
        throws XDIMEException {

        XDIMEElementInternal foundElement = ElementUtils.getContainingElement(
                parent, ELEMENTS_WITH_CAPTION_FILTER);

        XDIMEElementInternal object = foundElement;

        if (object instanceof TableElement) {
            closeTableCaption(context);

        } else if (object instanceof ObjectElement) {
            closeObjectCaption(context, (ObjectElement) object);

        } else {
            // This should never happen
            logger.error("internal-error-while-processing-element", getTagName());
            throw new XDIMEException(
                exceptionLocalizer.format(
                    "internal-error-while-processing-element", getTagName()));
        }
    }

    /**
     * Close an object caption.
     *
     * @param context
     */
    private void closeObjectCaption(XDIMEContextInternal context, ObjectElement element) {

        // Close the div in that represents the entire caption. We do this
        // because Paul says caption is a block element.
        DivAttributes divAttributes = new DivAttributes();
        divAttributes.copy(protocolAttributes);
        getProtocol(context).writeCloseDiv(divAttributes);

        getPageContext(context).popOutputBuffer(objectCaptionContent);

        element.setCaptionBuffer(objectCaptionContent);

    }

    /**
     * Close a table caption.
     *
     * @param context
     */
    private void closeTableCaption(XDIMEContextInternal context) {
        VolantisProtocol protocol = getProtocol(context);

        protocol.writeCloseTableCaption((CaptionAttributes)protocolAttributes);
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 01-Dec-05	9839/1	geoff	VBM:2005101702 Fix the XDIME2 Object element

 12-Oct-05	9673/5	pduffin	VBM:2005092906 Improved validation and fixed layout formatting

 10-Oct-05	9673/3	pduffin	VBM:2005092906 Improved validation and fixed layout formatting

 30-Sep-05	9562/4	pabbott	VBM:2005092011 Add XHTML2 Object element

 21-Sep-05	9128/4	pabbott	VBM:2005071114 Review feedback for XHTML2 elements

 20-Sep-05	9128/2	pabbott	VBM:2005071114 Add XHTML 2 elements

 ===========================================================================
*/
