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
 * $Header: /src/voyager/com/volantis/mcs/papi/FragmentElement.java,v 1.2 2003/03/12 16:10:43 sfound Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 19-Mar-02    Steve           VBM:2002021105 - Created.
 *                              Element to override fragment link text
 * 11-Mar-03    Steve           VBM:2003022403 - Added public api doclet tags
 * 23-Apr-03    Steve           VBM:2003041606 - Override hasMixedContent() to
 *                              return false 
 * 19-May-03    Chris W         VBM:2003051902 - hasMixedContent() made package
 *                              protected.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.papi.impl;

import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.papi.Fragment;
import com.volantis.mcs.papi.FragmentAttributes;
import com.volantis.mcs.papi.PAPIAttributes;
import com.volantis.mcs.papi.PAPIException;

/**
 * The fragment element.
 */
public class FragmentElementImpl
        extends AbstractExprElementImpl {

    /**
     * Create a new <code>LineBreakElement</code>.
     */
    public FragmentElementImpl() {
    }

    // Javadoc inherited.
    protected int exprElementStart(
            MarinerRequestContext context,
            PAPIAttributes papiAttributes)
            throws PAPIException {

        return SKIP_ELEMENT_BODY;
    }

    // Javadoc inherited.
    protected int exprElementEnd(
            MarinerRequestContext context,
            PAPIAttributes papiAttributes)
            throws PAPIException {

        FragmentAttributes attributes = (FragmentAttributes) papiAttributes;

        // Get the current form fragment from the request context
        Fragment fragment = context.getFragment(attributes.getName());

        if (fragment != null) {
            String value = attributes.getLinkText();
            if (value != null) {
                fragment.overrideLinkToText(value);
            }

            value = attributes.getBackLinkText();
            if (value != null) {
                fragment.overrideLinkFromText(value);
            }
        }

        return CONTINUE_PROCESSING;
    }

    // Javadoc inherited from super class.
    public void elementReset(MarinerRequestContext context) {
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

 18-May-05	8196/2	ianw	VBM:2005051203 Refactored PAPI to seperate out implementation

 09-May-05	8132/1	philws	VBM:2005050510 Port format instance collection bug fix from 3.3

 09-May-05	8128/1	philws	VBM:2005050510 Ensure that format instances are collected for the entire layout format tree

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 13-Aug-03	958/1	chrisw	VBM:2003070704 implemented expr attribute on papi elements

 ===========================================================================
*/
