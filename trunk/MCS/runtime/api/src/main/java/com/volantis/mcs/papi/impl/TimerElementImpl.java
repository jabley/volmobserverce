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
 * $Header: /src/voyager/com/volantis/mcs/papi/TimerElement.java,v 1.5 2003/03/12 16:10:43 sfound Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 06 Aug 02    sumit           VBM:2002080509 - Created for <timer>
 * 11-Mar-03    Steve           VBM:2003022403 - Added public api doclet tags
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.papi.impl;

import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.papi.PAPIAttributes;
import com.volantis.mcs.papi.PAPIException;
import com.volantis.mcs.papi.TimerAttributes;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.synergetics.log.LogDispatcher;

public class TimerElementImpl
        extends AbstractExprElementImpl {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(TimerElementImpl.class);

    private com.volantis.mcs.protocols.TimerAttributes pattributes;

    /**
     * Creates a new instance of TimerElement
     */
    public TimerElementImpl() {
        pattributes = new com.volantis.mcs.protocols.TimerAttributes();
    }

    /**
     * Called at the start of a PAPI element.
     *
     * @param context        The MarinerRequestContext within which this element is
     *                       being processed.
     * @param papiAttributes The implementation of PAPIAttributes which
     *                       contains the attributes specific to the implementation of PAPIElement.
     * @return PROCESS_ELEMENT_BODY or SKIP_ELEMENT_BODY.
     * @throws PAPIException If there was a problem processing the element.
     */
    protected int exprElementStart(
            MarinerRequestContext context,
            PAPIAttributes papiAttributes) throws PAPIException {

        TimerAttributes attributes = (TimerAttributes) papiAttributes;
        MarinerPageContext pageContext
                = ContextInternals.getMarinerPageContext(context);

        VolantisProtocol protocol = pageContext.getProtocol();
        pattributes.setDuration(attributes.getDuration());
        protocol.doTimer(pattributes);

        return SKIP_ELEMENT_BODY;
    }

    /**
     * Called at the end of a PAPI element.
     * <p>
     * If the elementStart method was called then this method will also be
     * called unless an Exception occurred during the processing of the
     * body.
     * </p>
     *
     * @param context        The MarinerRequestContext within which this element is
     *                       being processed.
     * @param papiAttributes The implementation of PAPIAttributes which
     *                       contains the attributes specific to the implementation of PAPIElement.
     * @return CONTINUE_PROCESSING or ABORT_PROCESSING.
     * @throws PAPIException If there was a problem processing the element.
     */
    protected int exprElementEnd(
            MarinerRequestContext context,
            PAPIAttributes papiAttributes) throws PAPIException {
        return CONTINUE_PROCESSING;
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

 18-May-05	8196/2	ianw	VBM:2005051203 Refactored PAPI to seperate out implementation

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/5	doug	VBM:2004111702 Refactored Logging framework

 18-Nov-04	6183/1	tom	VBM:2004101801 Changed PAPI to use the StyleEngine from the DeviceTheme for styling

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 13-Aug-03	958/1	chrisw	VBM:2003070704 implemented expr attribute on papi elements

 ===========================================================================
*/
