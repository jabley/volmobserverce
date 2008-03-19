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
 * $Header: /src/voyager/com/volantis/mcs/papi/LineBreakElement.java,v 1.6 2003/03/12 16:10:43 sfound Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 19-Dec-01    Paul            VBM:2001120506 - Created.
 * 31-Jan-02    Paul            VBM:2001122105 - Updated to reflect changes to
 *                              protocols.
 * 28-Feb-02    Paul            VBM:2002022804 - Made the elementEnd pop the
 *                              element rather than push it again.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from
 *                              class to string.
 * 22-Feb-02    Paul            VBM:2002021802 - Fixed ordering of call to
 *                              super.elementReset.
 * 11-Mar-03    Steve           VBM:2003022403 - Added public api doclet tags
 * 23-Apr-03    Steve           VBM:2003041606 - Override hasMixedContent() to
 *                              return false 
 * 19-May-03    Chris W         VBM:2003051902 - hasMixedContent() made package
 *                              protected.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.papi.impl;

import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.papi.LineBreakAttributes;
import com.volantis.mcs.papi.PAPIAttributes;
import com.volantis.mcs.papi.PAPIException;
import com.volantis.mcs.protocols.MCSAttributes;
import com.volantis.mcs.protocols.VolantisProtocol;

/**
 * The br element.
 */
public class LineBreakElementImpl
        extends AbstractExprElementImpl {

    /**
     * The attributes to pass to the protocol methods.
     */
    private com.volantis.mcs.protocols.LineBreakAttributes pattributes;

    /**
     * Create a new <code>LineBreakElement</code>.
     */
    public LineBreakElementImpl() {
        pattributes = new com.volantis.mcs.protocols.LineBreakAttributes();
    }

    // Javadoc inherited.
    MCSAttributes getMCSAttributes() {
        return pattributes;
    }

    // Javadoc inherited.
    protected int exprElementStart(
            MarinerRequestContext context,
            PAPIAttributes papiAttributes)
            throws PAPIException {

        LineBreakAttributes attributes = (LineBreakAttributes) papiAttributes;

        // Copy the attributes into the protocol attributes class.
        pattributes.setId(attributes.getId());
        pattributes.setTitle(attributes.getTitle());

        MarinerPageContext pageContext
                = ContextInternals.getMarinerPageContext(context);

        // Push this element.
        pageContext.pushElement(this);

        return PROCESS_ELEMENT_BODY;
    }

    // Javadoc inherited.
    protected int exprElementEnd(
            MarinerRequestContext context,
            PAPIAttributes papiAttributes)
            throws PAPIException {

        MarinerPageContext pageContext
                = ContextInternals.getMarinerPageContext(context);

        // Pop this element.
        pageContext.popElement(this);

        VolantisProtocol protocol = pageContext.getProtocol();
        protocol.writeLineBreak(pattributes);

        return CONTINUE_PROCESSING;
    }

    // Javadoc inherited from super class.
    public void elementReset(MarinerRequestContext context) {
        pattributes.resetAttributes();

        super.elementReset(context);
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

 30-Aug-05	9353/1	pduffin	VBM:2005081912 Removed style class from MCS Attributes

 24-Jun-05	8878/1	emma	VBM:2005062306 Building calls to the styling engine into the framework and fixing NPE

 20-Jun-05	8483/3	emma	VBM:2005052410 Migrate styling to use the new styling support framework (still using CSSEmulator to style underneath)

 18-May-05	8196/2	ianw	VBM:2005051203 Refactored PAPI to seperate out implementation

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 18-Nov-04	6183/1	tom	VBM:2004101801 Changed PAPI to use the StyleEngine from the DeviceTheme for styling

 13-Aug-03	958/1	chrisw	VBM:2003070704 implemented expr attribute on papi elements

 ===========================================================================
*/
