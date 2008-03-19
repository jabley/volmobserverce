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
 * $Header: $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 30-Apr-03    Steve      		VBM:2003041606 Moved from auto generated code
 *                              so that the isPreFormatted method can be
 *                              overridden with the minimum of fuss.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.papi.impl;

import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.papi.BlockAttributes;
import com.volantis.mcs.papi.PAPIException;
import com.volantis.mcs.papi.PreAttributes;
import com.volantis.mcs.protocols.MCSAttributes;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;


/**
 * PreElement
 *
 * @author steve
 */
public class PreElementImpl
        extends BlockElementImpl {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(PreElementImpl.class);

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
            LocalizationFactory.createExceptionLocalizer(PreElementImpl.class);

    /**
     * The attributes to pass to the protocol methods.
     */
    private com.volantis.mcs.protocols.PreAttributes pattributes;

    /**
     * Create a new <code>PreElement</code>.
     */
    public PreElementImpl() {
        pattributes = new com.volantis.mcs.protocols.PreAttributes();
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

        MarinerPageContext pageContext
                = ContextInternals.getMarinerPageContext(context);

        PreAttributes attributes
                = (PreAttributes) blockAttributes;

        pattributes.setId(attributes.getId());
        pattributes.setTitle(attributes.getTitle());

        // Initialise the general event attributes
        PAPIInternals.initialiseGeneralEventAttributes(pageContext,
                attributes,
                pattributes);

        VolantisProtocol protocol = pageContext.getProtocol();
        try {
            protocol.writeOpenPre(pattributes);
        } catch (Exception e) {
            if (e instanceof RuntimeException) {
                // Just a normal RuntimeException, pass it up the stack.
                throw (RuntimeException) e;
            } else if (e instanceof ProtocolException) {
                // The non-runtime exception we are expecting.
                // Create a PAPI exception to wrap it and pass up the stack.
                logger.error("rendering-error", pattributes.getTagName(), e);
                new PAPIException(
                        exceptionLocalizer.format("rendering-error",
                                pattributes.getTagName()),
                        e);
            } else {
                // Any other non-runtime exception is an error.

                logger.error("illegal-protocol-error", e);
                throw new IllegalStateException("Illegal Protocol Exception:" +
                        e.getClass().getName() + ": " +
                        e.getLocalizedMessage());
            }
        }
        return PROCESS_ELEMENT_BODY;
    }

    // Javadoc inherited from super class.
    protected int elementEndImpl(
            MarinerRequestContext context,
            BlockAttributes blockAttributes)
            throws PAPIException {

        MarinerPageContext pageContext
                = ContextInternals.getMarinerPageContext(context);

        VolantisProtocol protocol = pageContext.getProtocol();

        try {
            protocol.writeClosePre(pattributes);
        } catch (Exception e) {
            if (e instanceof RuntimeException) {
                // Just a normal RuntimeException, pass it up the stack.
                throw (RuntimeException) e;
            } else if (e instanceof ProtocolException) {
                // The non-runtime exception we are expecting.
                // Create a PAPI exception to wrap it and pass up the stack.
                logger.error("rendering-error", pattributes.getTagName(), e);
                new PAPIException(
                        exceptionLocalizer.format("rendering-error",
                                pattributes.getTagName()),
                        e);
            } else {
                // Any other non-runtime exception is an error.

                logger.error("illegal-protocol-error", e);
                throw new IllegalStateException("Illegal Protocol Exception:" +
                        e.getClass().getName() + ": " +
                        e.getLocalizedMessage());
            }
        }

        return CONTINUE_PROCESSING;
    }

    // Javadoc inherited from super class.
    public void elementReset(MarinerRequestContext context) {
        pattributes.resetAttributes();
        super.elementReset(context);
    }

    // Javadoc inherited from super class
    public boolean isPreFormatted() {
        return true;
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 30-Aug-05	9353/1	pduffin	VBM:2005081912 Removed style class from MCS Attributes

 18-May-05	8196/2	ianw	VBM:2005051203 Refactored PAPI to seperate out implementation

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/5	doug	VBM:2004111702 Refactored Logging framework

 18-Nov-04	6183/1	tom	VBM:2004101801 Changed PAPI to use the StyleEngine from the DeviceTheme for styling

 19-Feb-04	2789/5	tony	VBM:2004012601 refactored localised logging to synergetics

 18-Feb-04	2789/3	tony	VBM:2004012601 update localisation services

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 13-Aug-03	958/3	chrisw	VBM:2003070704 implemented expr attribute on papi elements

 08-Aug-03	958/1	chrisw	VBM:2003070704 half way through changes to existing PAPI

 31-Jul-03	868/1	mat	VBM:2003070704 Initial work on this task

 ===========================================================================
*/
