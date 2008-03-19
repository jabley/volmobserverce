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
 * $Header: /src/voyager/com/volantis/mcs/papi/PhoneNumberElement.java,v 1.2 2003/04/17 10:21:06 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 19-Mar-03    Phil W-S        VBM:2002111502 - Created. Must be hand-written
 *                              to handle the new attribute groupings due to
 *                              the introduction of the PhoneNumberElement.
 * 16-Apr-03    Geoff           VBM:2003041603 - Add catch clause for 
 *                              ProtocolException that throws PAPIException.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.papi.impl;

import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.papi.BlockAttributes;
import com.volantis.mcs.papi.PAPIException;
import com.volantis.mcs.papi.PhoneNumberAttributes;
import com.volantis.mcs.protocols.MCSAttributes;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.assets.TextAssetReference;
import com.volantis.mcs.runtime.policies.PolicyReferenceResolver;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;

/**
 * The phone number element.
 */
public final class PhoneNumberElementImpl
        extends AbstractAnchorBaseElementImpl {
    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(PhoneNumberElementImpl.class);

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
            LocalizationFactory
                    .createExceptionLocalizer(PhoneNumberElementImpl.class);

    /**
     * The attributes to pass to the protocol methods.
     */
    private com.volantis.mcs.protocols.PhoneNumberAttributes pattributes;

    /**
     * Create a new <code>PhoneNumberElement</code>.
     */
    public PhoneNumberElementImpl() {
        pattributes = new com.volantis.mcs.protocols.PhoneNumberAttributes();
    }

    // Javadoc inherited.
    MCSAttributes getMCSAttributes() {
        return pattributes;
    }

    // Javadoc inherited from super class.
    protected int elementStartImpl(
            MarinerRequestContext context,
            BlockAttributes blockAttributes)
            throws PAPIException {
        String value;

        MarinerPageContext pageContext =
                ContextInternals.getMarinerPageContext(context);

        PhoneNumberAttributes attributes =
                (PhoneNumberAttributes) blockAttributes;

        // Handle the common base attributes
        transferAttributes(context, attributes, pattributes);

        PolicyReferenceResolver resolver =
                pageContext.getPolicyReferenceResolver();

        // Resolve the fullNumber string to a mariner expression
        if ((value = attributes.getFullNumber()) != null) {
            TextAssetReference expression =
                    resolver.resolveQuotedTextExpression(value);
            pattributes.setFullNumber(expression);
        }

        VolantisProtocol protocol = pageContext.getProtocol();
        protocol.writeOpenPhoneNumber(pattributes);

        return PROCESS_ELEMENT_BODY;
    }

    // Javadoc inherited from super class.
    protected int elementEndImpl(
            MarinerRequestContext context,
            BlockAttributes blockAttributes)
            throws PAPIException {
        MarinerPageContext pageContext =
                ContextInternals.getMarinerPageContext(context);

        VolantisProtocol protocol = pageContext.getProtocol();

        try {
            protocol.writeClosePhoneNumber(pattributes);
        } catch (ProtocolException e) {

            logger.error("rendering-error", pattributes.getTagName(), e);
            new PAPIException(
                    exceptionLocalizer.format("rendering-error",
                            pattributes.getTagName()),
                    e);
        }

        return CONTINUE_PROCESSING;
    }

    // Javadoc inherited from super class.
    public void elementReset(MarinerRequestContext context) {

        pattributes.resetAttributes();

        super.elementReset(context);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-May-05	8196/2	ianw	VBM:2005051203 Refactored PAPI to seperate out implementation

 11-Feb-05	6931/1	geoff	VBM:2005020901 R821: Branding using Projects: Prerequisites: Fix remaining manual id creation

 09-Feb-05	6914/1	geoff	VBM:2005020707 R821: Branding using Projects: Prerequisites: Fix schema and related expressions

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/5	doug	VBM:2004111702 Refactored Logging framework

 18-Nov-04	6183/1	tom	VBM:2004101801 Changed PAPI to use the StyleEngine from the DeviceTheme for styling

 01-Nov-04	6068/1	tom	VBM:2004101508 renamed VolantisAttribute to MCSAttributes and added style properties container

 19-Feb-04	2789/5	tony	VBM:2004012601 refactored localised logging to synergetics

 18-Feb-04	2789/3	tony	VBM:2004012601 update localisation services

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 13-Aug-03	958/3	chrisw	VBM:2003070704 implemented expr attribute on papi elements

 08-Aug-03	958/1	chrisw	VBM:2003070704 half way through changes to existing PAPI

 31-Jul-03	868/1	mat	VBM:2003070704 Initial work on this task

 ===========================================================================
*/
