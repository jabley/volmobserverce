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
 * $Header: /src/voyager/com/volantis/mcs/papi/AttrsElement.java,v 1.7 2003/04/22 10:59:03 aboyd Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 14-Oct-01    Paul            VBM:2001111402 - Created
 * 28-Nov-01    Paul            VBM:2001112202 - Updated to reflect changes
 *                              in papi classes.
 * 30-Nov-01    Paul            VBM:2001112909 - Added copyright statement and
 *                              pushed and popped the element.
 * 19-Dec-01    Paul            VBM:2001120506 - Renamed from InlineElement.
 * 31-Jan-02    Paul            VBM:2001122105 - Initialise event attributes.
 * 28-Feb-02    Paul            VBM:2002022804 - Made the elementEnd pop the
 *                              element rather than push it again.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from class
                                to string.
 * 10-Mar-02    Steve           VBM:2003022403 - Added API doclet tags
 * 17-Apr-03    Allan           VBM:2003041506 - Fixed javadoc bug in 
 *                              elementStart. Optimized imports.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.papi.impl;

import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.papi.AttrsAttributes;
import com.volantis.mcs.papi.PAPIAttributes;
import com.volantis.mcs.papi.PAPIException;
import com.volantis.mcs.protocols.MCSAttributes;
import com.volantis.mcs.protocols.VolantisProtocol;

/**
 * This abstract class implements functionality which is common across the
 * majority of PAPI inline elements.
 */
public abstract class AttrsElementImpl
        extends AbstractExprElementImpl {

    /**
     * Create a new <code>AttrsElementImpl</code>.
     */
    public AttrsElementImpl() {
    }

    // Javadoc inherited.
    abstract MCSAttributes getMCSAttributes();

    /**
     * Write the open markup for the element to the output buffer.
     * <p/>
     * It is package scope so that it does not appear in the public
     * documentation.
     * </p>
     *
     * @param protocol The protocol to use to generate the markup.
     */
    abstract void writeOpenMarkup(VolantisProtocol protocol)
            throws PAPIException;

    /**
     * Write the close markup for the element to the output buffer.
     * <p/>
     * It is package scope so that it does not appear in the public
     * documentation.
     * </p>
     *
     * @param protocol The protocol to use to generate the markup.
     */
    abstract void writeCloseMarkup(VolantisProtocol protocol)
            throws PAPIException;

    /**
     * Javadoc inherited from super class.
     */
    protected final int exprElementStart(
            MarinerRequestContext context,
            PAPIAttributes papiAttributes)
            throws PAPIException {

        MarinerPageContext pageContext
                = ContextInternals.getMarinerPageContext(context);

        AttrsAttributes attributes = (AttrsAttributes) papiAttributes;
        MCSAttributes pattributes = getMCSAttributes();

        // Copy the attributes into the protocol attributes class.
        pattributes.setId(attributes.getId());
        pattributes.setTitle(attributes.getTitle());

        // Initialise the event related attributes.
        PAPIInternals.initialiseGeneralEventAttributes(pageContext,
                attributes,
                pattributes);

        VolantisProtocol protocol = pageContext.getProtocol();
        writeOpenMarkup(protocol);

        // Push this element.
        pageContext.pushElement(this);

        return PROCESS_ELEMENT_BODY;
    }

    /**
     * Javadoc inherited from super class.
     */
    protected final int exprElementEnd(
            MarinerRequestContext context,
            PAPIAttributes papiAttributes)
            throws PAPIException {

        MarinerPageContext pageContext
                = ContextInternals.getMarinerPageContext(context);

        // Pop this element.
        pageContext.popElement(this);

        VolantisProtocol protocol = pageContext.getProtocol();
        writeCloseMarkup(protocol);

        return CONTINUE_PROCESSING;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 30-Aug-05	9353/1	pduffin	VBM:2005081912 Removed style class from MCS Attributes

 24-Jun-05	8878/1	emma	VBM:2005062306 Building calls to the styling engine into the framework and fixing NPE

 20-Jun-05	8483/3	emma	VBM:2005052410 Migrate styling to use the new styling support framework (still using CSSEmulator to style underneath)

 18-May-05	8196/2	ianw	VBM:2005051203 Refactored PAPI to seperate out implementation

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 22-Nov-04	6183/3	tom	VBM:2004101801 Changed PAPI to use the StyleEngine from the DeviceTheme for styling

 01-Nov-04	6068/1	tom	VBM:2004101508 renamed VolantisAttribute to MCSAttributes and added style properties container

 13-Aug-03	958/4	chrisw	VBM:2003070704 implemented expr attribute on papi elements

 08-Aug-03	958/1	chrisw	VBM:2003070704 half way through changes to existing PAPI

 31-Jul-03	868/1	mat	VBM:2003070704 Initial work on this task

 ===========================================================================
*/
