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
 * $Header: /src/voyager/com/volantis/mcs/papi/MessageElement.java,v 1.4 2003/03/20 15:15:31 sumit Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 11-Feb-03    Ian             VBM:2003020607 - Ported from Metis. 
 * 11-Mar-03    Steve           VBM:2003022403 - Added public api doclet tags
 * 20-Mar-03    sumit           VBM:2003031809 - Wrapped logger.debug 
 *                              statements in if(logger.isDebugEnabled()) block
 * 01-Nov-02    Ian             VBM:2002091806 - Created.
 * 29-Jan-03    Ian             VBM:2003011609 - Added processing to check if
 *                              canvas tag is supported here, if this is the 
 *                              case we are running under mcs so should not
 *                              allow the message tag.
 * 23-Apr-03    Steve           VBM:2003041606 - Override hasMixedContent() to
 *                              return false 
 * 19-May-03    Chris W         VBM:2003051902 - hasMixedContent() made package
 *                              protected.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.papi.impl;

import com.volantis.mcs.context.ApplicationContext;
import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.management.tracking.CanvasType;
import com.volantis.mcs.papi.PAPIAttributes;
import com.volantis.mcs.papi.PAPIException;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.synergetics.log.LogDispatcher;

/**
 * An initial implementation of a PAPI style element class.
 * It has no state, so only one instance needs to be created. It may be
 * advisable to add some state in which case it needs to be managed.
 */
public class MessageElementImpl
        extends PageElementImpl {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(MessageElementImpl.class);


    // Javadoc inherited from super class.
    public void elementReset(MarinerRequestContext context) {

        super.elementReset(context);
    }

    /**
     * End the element specific processing.
     *
     * @param context        The MarinerRequestContext within which this element is
     *                       being processed.
     * @param papiAttributes The implementation of PAPIAttributes which
     *                       contains the attributes specific to the implementation of PAPIElement.
     * @return PROCESS_ELEMENT_BODY or SKIP_ELEMENT_BODY.
     * @throws PAPIException If there was a problem processing the element.
     */
    protected int elementEndImpl(
            MarinerRequestContext context, PAPIAttributes papiAttributes)
            throws PAPIException {
        if (logger.isDebugEnabled()) {
            logger.debug("MessageElement end ");
        }

        ApplicationContext applicationContext =
                ContextInternals.getApplicationContext(context);

        // End of the message tag so we can't have any more canvas tags
        applicationContext.setCanvasTagSupported(false);

        return CONTINUE_PROCESSING;
    }

    /**
     * Start the element specific processing.
     *
     * @param context        The MarinerRequestContext within which this element is
     *                       being processed.
     * @param papiAttributes The implementation of PAPIAttributes which
     *                       contains the attributes specific to the implementation of PAPIElement.
     * @return PROCESS_ELEMENT_BODY or SKIP_ELEMENT_BODY.
     * @throws PAPIException If there was a problem processing the element.
     */
    protected int elementStartImpl(
            MarinerRequestContext context, PAPIAttributes papiAttributes)
            throws PAPIException {

        if (logger.isDebugEnabled()) {
            logger.debug("MessageElement start ");
        }
        VolantisProtocol protocol = pageContext.getProtocol();
        ApplicationContext applicationContext =
                ContextInternals.getApplicationContext(context);

        // We found a message tag so now allow canvas tags.
        applicationContext.setCanvasTagSupported(true);
        return PROCESS_ELEMENT_BODY;
    }

    /**
     * Return a canvas type of "message"
     *
     * @return
     */
    CanvasType getCanvasType() {
        return CanvasType.MESSAGE;
    }


    // Javadoc inherited from super class.
    boolean hasMixedContent() {
        return false;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Nov-05	9990/2	ibush	VBM:2005102516 Enable Local and Remote Project Loading

 18-May-05	8196/2	ianw	VBM:2005051203 Refactored PAPI to seperate out implementation

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/4	doug	VBM:2004111702 Refactored Logging framework

 15-Oct-04	5837/1	matthew	VBM:2004100706 allow <message> tag in MCS

 21-Jun-04	4702/1	matthew	VBM:2004061402 rework PageTracking

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 ===========================================================================
*/
