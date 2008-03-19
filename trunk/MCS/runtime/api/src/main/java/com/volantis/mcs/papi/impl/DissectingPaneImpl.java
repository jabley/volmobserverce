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
 * $Header: /src/voyager/com/volantis/mcs/papi/DissectingPane.java,v 1.3 2003/03/27 12:09:10 sfound Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 21-Mar-03    Byron           VBM:2003031907 - Created
 * 26-Mar-03    Steve           VBM:2003031907 - Javadoced and public API tags 
 *                              added
 * 27-Mar-03    Steve           VBM:2003031907 - Fixed constructor javadoc
 * 27-Mar-03    Steve           VBM:2003031907 - Fixed constructor javadoc MKII
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.papi.impl;

import com.volantis.mcs.papi.DissectingPane;
import com.volantis.mcs.protocols.layouts.DissectingPaneInstance;

/**
 * This class allows the overriding of link text used within dissecting panes
 * at runtime. The class can only be obtained via the MarinerRequestContext
 * class and for this reason it does not have a publicly accessible constructor.
 *
 * @see com.volantis.mcs.context.MarinerRequestContext#getDissectingPane
 */
public class DissectingPaneImpl
        implements DissectingPane {

    /**
     * The context for the dissecting pane
     */
    private DissectingPaneInstance dissectingPaneInstance;

    /**
     * Do not alter the access level of this constructor
     * It is package scope so that it is not availaible via the
     * public API.
     *
     * Creates a DissectingPane facade from a DissectingPaneInstance
     *
     * @param instance A DissectingPaneInstance object that is used
     *                 to access/modify the pane attributes.
     */
    DissectingPaneImpl(DissectingPaneInstance instance) {
        this.dissectingPaneInstance = instance;
    }

    public String getName() {
        return dissectingPaneInstance.getDissectingPaneName();
    }

    public String getLinkToText() {
        return dissectingPaneInstance.getLinkToText();
    }

    public String getLinkFromText() {
        return dissectingPaneInstance.getLinkFromText();
    }

    public void overrideLinkToText(String linkToText) {
        dissectingPaneInstance.setLinkToText(linkToText);
    }

    public void overrideLinkFromText(String linkFromText) {
        dissectingPaneInstance.setLinkFromText(linkFromText);
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
