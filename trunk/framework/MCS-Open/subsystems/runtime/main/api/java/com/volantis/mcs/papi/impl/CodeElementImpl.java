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
 * 30-Apr-03    Steve           VBM:2003041606 Moved from auto generated code
 *                              so that the isPreFormatted method can be 
 *                              overridden with the minimum of fuss.
 * ----------------------------------------------------------------------------
 */


package com.volantis.mcs.papi.impl;

import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.papi.PAPIException;
import com.volantis.mcs.protocols.MCSAttributes;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;

/**
 * The code element.
 */
public class CodeElementImpl
        extends AttrsElementImpl {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(CodeElementImpl.class);

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
            LocalizationFactory.createExceptionLocalizer(CodeElementImpl.class);

    /**
     * The attributes to pass to the protocol methods.
     */
    private com.volantis.mcs.protocols.CodeAttributes pattributes;

    /**
     * Create a new <code>CodeElement</code>.
     */
    public CodeElementImpl() {
        pattributes = new com.volantis.mcs.protocols.CodeAttributes();
    }

    // Javadoc inherited.
    MCSAttributes getMCSAttributes() {
        return pattributes;
    }

    // Javadoc inherited from super class.
    void writeOpenMarkup(VolantisProtocol protocol) throws PAPIException {
        // @todo This dodgy code should be fixed. Do not copy!
        // See ParseMarinerSchema.generateProtocolCallBlock()
        // for an explanation of why we make the catch this way.
        try {
            protocol.writeOpenCode(pattributes);
        } catch (Exception e) {
            if (e instanceof RuntimeException) {
                // Just a normal RuntimeException, pass it up the stack.
                throw (RuntimeException) e;
            } else if (e instanceof ProtocolException) {
                // The non-runtime exception we are expecting.
                // Create a PAPI exception to wrap it and pass up the stack.

                logger.error("rendering-error", pattributes.getTagName(), e);
                throw new PAPIException(
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
    }

    // Javadoc inherited from super class.
    void writeCloseMarkup(VolantisProtocol protocol) {
        protocol.writeCloseCode(pattributes);
    }

    // Javadoc inherited from super class.
    public void elementReset(MarinerRequestContext context) {

        pattributes.resetAttributes();

        super.elementReset(context);
    }

    // Javadoc inherited from super class.
    public boolean isPreFormatted() {
        return true;
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

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/5	doug	VBM:2004111702 Refactored Logging framework

 22-Nov-04	6183/1	tom	VBM:2004101801 Changed PAPI to use the StyleEngine from the DeviceTheme for styling

 19-Feb-04	2789/5	tony	VBM:2004012601 refactored localised logging to synergetics

 18-Feb-04	2789/3	tony	VBM:2004012601 update localisation services

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 14-Aug-03	958/3	chrisw	VBM:2003070704 Fixed SpanElementTestCase

 08-Aug-03	958/1	chrisw	VBM:2003070704 half way through changes to existing PAPI

 31-Jul-03	868/1	mat	VBM:2003070704 Initial work on this task

 ===========================================================================
*/
