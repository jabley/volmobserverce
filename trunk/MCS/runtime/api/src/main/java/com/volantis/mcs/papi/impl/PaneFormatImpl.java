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
 * $Header: /src/voyager/com/volantis/mcs/papi/PaneFormat.java,v 1.2 2003/03/12 16:10:43 sfound Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 02-Sep-02    Sumit           VBM:2002030703 Created. Handles the in page
 *                              formatting of a Pane
 * 11-Mar-03    Steve           VBM:2003022403 - Added public api doclet tags
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.papi.impl;

import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.layouts.Pane;
import com.volantis.mcs.papi.PaneFormat;
import com.volantis.mcs.protocols.layouts.FormatInstance;

/**
 * This class allows the editing of certain properties of a Pane in the current
 * layout. A PaneFormat for a Pane can be obtained by calling the
 * marinerRequestContext.getPaneFormat("pane name") or by calling
 * marinerRequestContext.getPaneFormats() to get a list of Panes as PaneFormats.
 */
public class PaneFormatImpl
        implements PaneFormat {

    Pane mPane = null;

    /**
     * This Contructor is used by MarinerRequestContext to create a
     * PaneFormat in the marinerRequestContext.getPaneFormat(..) or the
     * getPaneFormats() methods
     */
    public PaneFormatImpl(FormatInstance instance) {
        mPane = (Pane) instance.getFormat();
    }

    /**
     * This Constructor can be used to obtain a PaneFormat for the current pane.
     * It should only be used after an opening <pane> element.
     */

    public PaneFormatImpl(MarinerRequestContext req) {
        MarinerPageContext pageContext =
                ContextInternals.getMarinerPageContext(req);
        mPane = pageContext.getCurrentPane();
    }

    public String getName() {
        if (mPane != null)
            return mPane.getName();
        return null;
    }

    public String getBackgroundColor() {
        if (mPane != null)
            return mPane.getBackgroundColour();
        return null;
    }

    public void setBackgroundColor(String bgColor) {
        if (mPane != null)
            mPane.setBackgroundColour(bgColor);
    }

    public String getBackgroundComponentName() {
        if (mPane != null)
            return mPane.getBackgroundComponent();
        return null;
    }

    public String getBackgroundComponentType() {
        if (mPane != null)
            return mPane.getBackgroundComponentType();
        return null;
    }

    public void setBackgroundComponentName(String name) {
        if (mPane != null)
            mPane.setBackgroundComponent(name);
    }

    public void setBackgroundComponentType(String type) {
        if (mPane != null)
            mPane.setBackgroundComponentType(type);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-May-05	8196/1	ianw	VBM:2005051203 Refactored PAPI to seperate out implementation

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 05-Nov-04	6112/2	byron	VBM:2004062909 Rename FormatContext et al to something more appropriate given recent changes.

 ===========================================================================
*/
