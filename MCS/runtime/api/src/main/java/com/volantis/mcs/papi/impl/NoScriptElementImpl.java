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
 * $Header: /src/voyager/com/volantis/mcs/papi/NoScriptElement.java,v 1.10 2003/04/17 10:21:06 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 19-Dec-01    Paul            VBM:2001120506 - Created.
 * 11-Mar-02    Ian             VBM:2002021904 - Fixed logic to write block 
 *                              without <noscript> tags when script asset is
 *                              invalid in a JavaScript enabled device.i Also
 *                              to always generate a <noscript> element in all
 *                              other circumstances.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from
 *                              class to string.
 * 22-Feb-02    Paul            VBM:2002021802 - Fixed ordering of call to
 *                              super.elementReset.
 * 20-May-02    Paul            VBM:2001122105 - Initialised general events.
 * 22-Oct-02    Geoff           VBM:2002102102 - Backed out the last change
 *                              for VBM:2002091203.
 * 11-Mar-03    Steve           VBM:2003022403 - Added public api doclet tags
 * 16-Apr-03    Geoff           VBM:2003041603 - Add catch clause for 
 *                              ProtocolException that throws PAPIException.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.papi.impl;

import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.papi.NoScriptAttributes;
import com.volantis.mcs.papi.PAPIAttributes;
import com.volantis.mcs.papi.PAPIException;
import com.volantis.mcs.protocols.MCSAttributes;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.SpanAttributes;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;

/**
 * The noscript element.
 */
public class NoScriptElementImpl
        extends AbstractExprElementImpl {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(NoScriptElementImpl.class);

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
            LocalizationFactory.createExceptionLocalizer(
                    NoScriptElementImpl.class);

    /**
     * Flag which indicates whether the elementStart method returned
     * SKIP_ELEMENT_BODY.
     */
    private boolean skipped;

    /**
     * The attributes to pass to the protocol methods.
     */
    private com.volantis.mcs.protocols.NoScriptAttributes pattributes;

    /**
     * Create a new <code>NoScriptElement</code>.
     */
    public NoScriptElementImpl() {
        pattributes = new com.volantis.mcs.protocols.NoScriptAttributes();
    }

    // Javadoc inherited.
    MCSAttributes getMCSAttributes() {
        return pattributes;
    }

    // Javadoc inherited.
    protected int exprElementStart(
            MarinerRequestContext context,
            PAPIAttributes papiAttributes) throws PAPIException {

        NoScriptAttributes attributes = (NoScriptAttributes) papiAttributes;

        MarinerPageContext pageContext
                = ContextInternals.getMarinerPageContext(context);

        VolantisProtocol protocol = pageContext.getProtocol();

        try {
            // Check to see whether the protocol supports java script, if it
            // does then check if we have a idRef to a script and if the script
            // asset is invalid, if so then put out plain markup without
            // <noscript></noscript> elements, but wrapped in span elements.
            //
            if (protocol.supportsJavaScript()) {
                // Is there and idref attribute?
                if (attributes.getIdref() != null) {
                    // Is the idref's asset valid?
                    if (pageContext.getIdValue(attributes.getIdref()) == null) {
                        // We need to skip the tags but still send the plain
                        // markup wrapped in a span element.
                        skipped = true;
                        SpanAttributes spanAttributes = new SpanAttributes();
                        spanAttributes.setStyles(pattributes.getStyles());
                        protocol.writeOpenSpan(spanAttributes);
                        return PROCESS_ELEMENT_BODY;
                    }
                }
            }

            // All other cases should put out <noscript></noscript> elements.
            pattributes.setTitle(attributes.getTitle());
            pattributes.setId(attributes.getId());

            // Initialise the general event attributes.
            PAPIInternals.initialiseGeneralEventAttributes(pageContext,
                    attributes, pattributes);

            protocol.writeOpenNoScript(pattributes);

            return PROCESS_ELEMENT_BODY;

        } catch (ProtocolException e) {

            logger.error("rendering-error", pattributes.getTagName(), e);
            throw new PAPIException(exceptionLocalizer.format(
                    "rendering-error", pattributes.getTagName()), e);
        }
    }

    // Javadoc inherited.
    protected int exprElementEnd(
            MarinerRequestContext context,
            PAPIAttributes papiAttributes)
            throws PAPIException {

        MarinerPageContext pageContext
                = ContextInternals.getMarinerPageContext(context);

        VolantisProtocol protocol = pageContext.getProtocol();

        if (skipped) {
            // span attributes are unused in the close method.
            protocol.writeCloseSpan(null);
        } else {
            protocol.writeCloseNoScript(pattributes);
        }

        return CONTINUE_PROCESSING;
    }

    // Javadoc inherited from super class.
    public void elementReset(MarinerRequestContext context) {
        skipped = false;

        pattributes.resetAttributes();

        super.elementReset(context);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 07-Nov-05	10173/1	emma	VBM:2005103107 Forward port: Fixes to correctly apply styles to various selectors

 07-Nov-05	10116/1	emma	VBM:2005103107 Fixes to correctly apply styles to various selectors

 30-Aug-05	9353/1	pduffin	VBM:2005081912 Removed style class from MCS Attributes

 24-Jun-05	8878/1	emma	VBM:2005062306 Building calls to the styling engine into the framework and fixing NPE

 20-Jun-05	8483/3	emma	VBM:2005052410 Migrate styling to use the new styling support framework (still using CSSEmulator to style underneath)

 18-May-05	8196/2	ianw	VBM:2005051203 Refactored PAPI to seperate out implementation

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/5	doug	VBM:2004111702 Refactored Logging framework

 18-Nov-04	6183/1	tom	VBM:2004101801 Changed PAPI to use the StyleEngine from the DeviceTheme for styling

 19-Feb-04	2789/5	tony	VBM:2004012601 refactored localised logging to synergetics

 18-Feb-04	2789/3	tony	VBM:2004012601 update localisation services

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 13-Aug-03	958/1	chrisw	VBM:2003070704 implemented expr attribute on papi elements

 ===========================================================================
*/
