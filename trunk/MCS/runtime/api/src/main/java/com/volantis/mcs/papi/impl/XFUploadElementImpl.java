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

package com.volantis.mcs.papi.impl;

import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.papi.PAPIAttributes;
import com.volantis.mcs.papi.PAPIException;
import com.volantis.mcs.papi.XFUploadAttributes;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.assets.TextAssetReference;
import com.volantis.mcs.protocols.forms.FieldType;
import com.volantis.mcs.protocols.forms.UploadFieldType;
import com.volantis.mcs.runtime.policies.PolicyReferenceResolver;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;

/**
 * The xfupload element.
 */
public class XFUploadElementImpl
        extends XFFormFieldElementImpl {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(XFUploadElementImpl.class);

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
            LocalizationFactory
                    .createExceptionLocalizer(XFUploadElementImpl.class);

    /**
     * Create a new <code>XFUploadElement</code>.
     */
    public XFUploadElementImpl() {
        super(false);
    }

    // Javadoc inherited from super class.
    String getInitialValue(
            MarinerPageContext pageContext,
            PAPIAttributes papiAttributes)
            throws PAPIException {

        XFUploadAttributes attributes = (XFUploadAttributes) papiAttributes;

        return attributes.getInitial();
    }


    // Javadoc inherited from super class.
    FieldType getFieldType(PAPIAttributes papiAttributes) {

        return UploadFieldType.getSingleton();
    }

    protected int elementStartImpl(
            MarinerRequestContext context,
            PAPIAttributes papiAttributes)
            throws PAPIException {

        MarinerPageContext pageContext
                = ContextInternals.getMarinerPageContext(context);

        VolantisProtocol protocol = pageContext.getProtocol();
        if (protocol.getProtocolConfiguration().isFileUploadSupported()) {

            XFUploadAttributes attributes = (XFUploadAttributes) papiAttributes;

            // Create a new protocol attributes object every time, as this element
            // could be reused before the attributes have actually been finished with
            // by the protocol.
            com.volantis.mcs.protocols.XFUploadAttributes pattributes
                    = new com.volantis.mcs.protocols.XFUploadAttributes();

            // Initialise the attributes specific to this field.
            TextAssetReference object;
            String value;

            PolicyReferenceResolver resolver =
                    pageContext.getPolicyReferenceResolver();

            // Set the initial value attribute.
            object = resolver.resolveQuotedTextExpression(
                    attributes.getInitial());
            pattributes.setInitial(object);

            // Set the max length attribute.
            value = attributes.getMaxLength();
            if (value != null) {
                pattributes.setMaxLength(Integer.parseInt(value));
            }

            // Initialise form field event attributes.
            PAPIInternals
                    .initialiseFieldEventAttributes(pageContext, attributes,
                            pattributes);

            // Do the rest of the field processing.
            try {
                doField(pageContext, attributes, pattributes);
            } catch (ProtocolException e) {
                logger.error("rendering-error", pattributes.getTagName(), e);
                throw new PAPIException(exceptionLocalizer.format(
                        "rendering-error",
                        pattributes.getTagName()),
                        e);
            }
        }
        return PROCESS_ELEMENT_BODY;
    }
/*
    // Javadoc inherited from super class.
    boolean hasMixedContent() {
        return false;
    }
*/
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

*/
