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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.xdime.response;

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.SpanAttributes;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.response.attributes.ResponseMessageAttributes;
import com.volantis.mcs.themes.properties.DisplayKeywords;
import com.volantis.mcs.xdime.XDIMEAttributes;
import com.volantis.mcs.xdime.XDIMEContextInternal;
import com.volantis.mcs.xdime.XDIMEException;
import com.volantis.mcs.xdime.XDIMEResult;
import com.volantis.mcs.xdime.widgets.ResponseElements;
import com.volantis.styling.StylingFactory;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;

/**
 * Implementation of AJAX resposne for Progress widget.
 */
public class ResponseMessageElement extends ResponseElement {
    /**
     * Used for logging.
     */
    private static final LogDispatcher logger =
        LocalizationFactory.createLogger(ResponseMessageElement.class);

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
        LocalizationFactory.createExceptionLocalizer(ResponseMessageElement.class);

    private SpanAttributes messageSpanAttributes;

    private SpanAttributes contentSpanAttributes;

    public ResponseMessageElement(XDIMEContextInternal context) {
        super(ResponseElements.MESSAGE, context);

        protocolAttributes = new ResponseMessageAttributes();
    }

    // Javadoc inherited
    public XDIMEResult callOpenOnProtocol(
        XDIMEContextInternal context, XDIMEAttributes attributes)
        throws XDIMEException {

        VolantisProtocol protocol = getProtocol(context);

        // If 'id' attribute is not specified by the user,
        // generate it now.
        if (protocolAttributes.getId() == null) {
            String id = protocol.getMarinerPageContext().generateUniqueFCID();

            protocolAttributes.setId(id);
        }

        // Render 'span' element, which will enclose content of
        // the 'response:message' element. Remember to propagate
        // the ID attribute and styles.
        try {
            messageSpanAttributes = new SpanAttributes();

            messageSpanAttributes.setId(protocolAttributes.getId());

            messageSpanAttributes.setStyles(StylingFactory.getDefaultInstance()
                    .createInheritedStyles(
                            protocol.getMarinerPageContext().getStylingEngine()
                            .getStyles(), DisplayKeywords.INLINE));

            protocol.writeOpenSpan(messageSpanAttributes);

            contentSpanAttributes = new SpanAttributes();

            contentSpanAttributes.setStyles(protocolAttributes.getStyles());

            protocol.writeOpenSpan(contentSpanAttributes);

        } catch (ProtocolException e) {
            logger.error("rendering-error", getTagName(), e);

            throw new XDIMEException(exceptionLocalizer.format(
                "rendering-error", getTagName()), e);
        }

        // Store this element in parent, so that it
        // can be accessed when parent is ended.
        if (parent instanceof ResponseValidationElement) {
            ResponseValidationElement validationElement =
                    (ResponseValidationElement) parent;

            validationElement.setMessageElement(this);

        } else if (parent instanceof ResponseFieldElement) {
            ResponseFieldElement fieldElement =
                    (ResponseFieldElement) parent;

            fieldElement.setMessageElement(this);
        }

        return XDIMEResult.PROCESS_ELEMENT_BODY;
    }

    // Javadoc inherited
    public void callCloseOnProtocol(XDIMEContextInternal context) {
        // Close the 'span' element.
        VolantisProtocol protocol = getProtocol(context);

        protocol.writeCloseSpan(contentSpanAttributes);

        protocol.writeCloseSpan(messageSpanAttributes);
    }

    /**
     * Returns message attributes.
     *
     * @return message attributes.
     */
    protected ResponseMessageAttributes getMessageAttributes() {
        return (ResponseMessageAttributes)protocolAttributes;
    }
}
