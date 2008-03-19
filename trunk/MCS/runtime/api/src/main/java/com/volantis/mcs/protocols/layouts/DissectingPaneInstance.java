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
 * $Header: /src/voyager/com/volantis/mcs/protocols/DissectingPaneInstance.java,v 1.4 2003/03/26 11:43:44 sfound Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 29-Oct-01    Paul            VBM:2001102901 - Created.
 * 22-Nov-01    Paul            VBM:2001110202 - Added attributes.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from class
 *                              to string.
 * 21-Mar-03    Byron           VBM:2003031907 - Added dissectingPane,
 *                               linkTo/From member fields and getters/setters.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.layouts;

import com.volantis.mcs.layouts.NDimensionalIndex;
import com.volantis.mcs.papi.DissectingPane;
import com.volantis.mcs.protocols.DissectingPaneAttributes;

/**
 * Contains all the state associated with a DissectingPane in a particular
 * MarinerPageContext.
 *
 * @mock.generate base="PaneInstance"
 */
public class DissectingPaneInstance
        extends PaneInstance {

    /**
     * The attributes which are passed to the DissectingPane related
     * protocol methods.
     */
    private final DissectingPaneAttributes attributes;

    /**
     * This is the PAPI dissecting pane
     */
    private DissectingPane dissectingPane;

    /**
     * Create a new <code>DissectingPaneInstance</code>.
     */
    public DissectingPaneInstance(NDimensionalIndex index) {
        super(index);
        attributes = new DissectingPaneAttributes();
    }

    public String getLinkFromText() {
        return attributes.getBackLinkText();
    }

    public void setLinkFromText(String linkFromText) {
        attributes.setBackLinkText(linkFromText);
    }

    public String getLinkToText() {
        return attributes.getLinkText();
    }

    public void setLinkToText(String linkToText) {
        attributes.setLinkText(linkToText);
    }

    public String getDissectingPaneName() {
        return format.getName();
    }

    public DissectingPane getDissectingPane() {
        return dissectingPane;
    }

    public void setDissectingPane(DissectingPane dissectingPane) {
        this.dissectingPane = dissectingPane;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 11-Jul-05	8862/1	pduffin	VBM:2005062108 Refactored layout rendering to make it more testable.

 18-May-05	8196/1	ianw	VBM:2005051203 Refactored PAPI to seperate out implementation

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 05-Nov-04	6112/6	byron	VBM:2004062909 Rename FormatContext et al to something more appropriate given recent changes.

 02-Nov-04	5882/1	ianw	VBM:2004102008 Split Code generators and move NDimensionalIndex for new build

 29-Jun-04	4713/1	geoff	VBM:2004061004 Support iterated Regions (make format contexts per format instance)

 ===========================================================================
*/
