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
 * $Header: /src/voyager/com/volantis/mcs/papi/SpanElement.java,v 1.3 2003/04/25 10:26:08 aboyd Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 17-Apr-03    Allan           VBM:2003041506 - Manually create this element 
 *                              so that the new src attribute can be handled as
 *                              archictected. 
 * 22-Apr-03    Allan           VBM:2003041710 - Update elementStart() to 
 *                              handle a mariner expression on the src 
 *                              attribute and defer to the protocol for the 
 *                              return value. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.papi.impl;

import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.papi.PAPIAttributes;
import com.volantis.mcs.papi.PAPIException;
import com.volantis.mcs.papi.SpanAttributes;
import com.volantis.mcs.protocols.MCSAttributes;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.assets.TextAssetReference;
import com.volantis.mcs.runtime.policies.PolicyReferenceResolver;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;

/**
 * The span element.
 */
public final class SpanElementImpl
        extends AttrsElementImpl {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(SpanElementImpl.class);

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
            LocalizationFactory.createExceptionLocalizer(SpanElementImpl.class);

    /**
     * The attributes to pass to the protocol methods.
     */
    com.volantis.mcs.protocols.SpanAttributes pattributes;

    /**
     * Create a new <code>SpanElement</code>.
     */
    public SpanElementImpl() {
        pattributes = new com.volantis.mcs.protocols.SpanAttributes();
    }

    // Javadoc inherited.
    MCSAttributes getMCSAttributes() {
        return pattributes;
    }

    // Javadoc inherited from super class.
    public int styleElementStart(
            MarinerRequestContext context,
            PAPIAttributes papiAttributes)
            throws PAPIException {

        MarinerPageContext pageContext
                = ContextInternals.getMarinerPageContext(context);

        SpanAttributes attributes = (SpanAttributes) papiAttributes;

        PolicyReferenceResolver resolver =
                pageContext.getPolicyReferenceResolver();

        TextAssetReference srcReference =
                resolver.resolveQuotedTextExpression(attributes.getSrc());
        if (srcReference != null && srcReference.isPolicy()) {
            pattributes.setSrc(srcReference);
        }

        int result = super.styleElementStart(context,
                (SpanAttributes) papiAttributes);

        if (result != SKIP_ELEMENT_BODY) {
            // Check to see if the protocol would like us to skip the element
            // body. If it does then return SKIP_ELEMENT_BODY otherwise return
            // result.        
            VolantisProtocol protocol = pageContext.getProtocol();
            result = protocol.skipElementBody() ? SKIP_ELEMENT_BODY : result;
        }

        return result;
    }

    // Javadoc inherited from super class.
    void writeOpenMarkup(VolantisProtocol protocol) throws PAPIException {
        try {
            protocol.writeOpenSpan(pattributes);
        } catch (ProtocolException e) {
            logger.error("open-span-error", e);
            throw new PAPIException(
                    exceptionLocalizer.format("open-span-error"), e);
        }
    }

    // Javadoc inherited from super class.
    void writeCloseMarkup(VolantisProtocol protocol) {
        protocol.writeCloseSpan(pattributes);
    }

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

 27-Jun-05	8878/3	emma	VBM:2005062306 rework

 24-Jun-05	8878/1	emma	VBM:2005062306 Building calls to the styling engine into the framework and fixing NPE

 18-May-05	8196/2	ianw	VBM:2005051203 Refactored PAPI to seperate out implementation

 17-Feb-05	6957/1	geoff	VBM:2005021103 R821: Branding using Projects: Prerequisites: use current project in PAPI phase

 11-Feb-05	6931/1	geoff	VBM:2005020901 R821: Branding using Projects: Prerequisites: Fix remaining manual id creation

 09-Feb-05	6914/1	geoff	VBM:2005020707 R821: Branding using Projects: Prerequisites: Fix schema and related expressions

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/5	doug	VBM:2004111702 Refactored Logging framework

 22-Nov-04	6183/1	tom	VBM:2004101801 Changed PAPI to use the StyleEngine from the DeviceTheme for styling

 01-Nov-04	6068/1	tom	VBM:2004101508 renamed VolantisAttribute to MCSAttributes and added style properties container

 19-Feb-04	2789/5	tony	VBM:2004012601 refactored localised logging to synergetics

 18-Feb-04	2789/3	tony	VBM:2004012601 update localisation services

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 14-Aug-03	958/3	chrisw	VBM:2003070704 Fixed SpanElementTestCase

 08-Aug-03	958/1	chrisw	VBM:2003070704 half way through changes to existing PAPI

 ===========================================================================
*/
