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
 * $Header: /src/voyager/com/volantis/mcs/papi/DissectingPaneElement.java,v 1.1 2003/03/26 11:43:44 sfound Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 21-Mar-03    Byron           VBM:2003031907 - Created
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.papi.impl;

import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.papi.DissectingPane;
import com.volantis.mcs.papi.DissectingPaneAttributes;
import com.volantis.mcs.papi.PAPIAttributes;
import com.volantis.mcs.papi.PAPIException;

/**
 * The dissecting pane PAPI element
 */
public class DissectingPaneElementImpl
        extends AbstractExprElementImpl {


    /**
     * The pane itself
     */
    private DissectingPane dissectingPane;

    /**
     * Javadoc inherited from super class.
     */
    protected int exprElementStart(
            MarinerRequestContext context,
            PAPIAttributes papiAttributes)
            throws PAPIException {

        return SKIP_ELEMENT_BODY;
    }

    /**
     * Javadoc inherited from super class.
     */
    protected int exprElementEnd(
            MarinerRequestContext context,
            PAPIAttributes papiAttributes)
            throws PAPIException {

        DissectingPaneAttributes attributes =
                (DissectingPaneAttributes) papiAttributes;

        // Get the current form fragment from the request context
        DissectingPane dissectingPane =
                context.getDissectingPane(attributes.getName());
        if (dissectingPane != null) {

            String value = attributes.getLinkText();
            if (value != null) {
                dissectingPane.overrideLinkToText(value);
            }

            value = attributes.getBackLinkText();
            if (value != null) {
                dissectingPane.overrideLinkFromText(value);
            }
        }
        return CONTINUE_PROCESSING;
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

 13-Aug-03	958/1	chrisw	VBM:2003070704 implemented expr attribute on papi elements

 ===========================================================================
*/
