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
 * $Header: /src/voyager/com/volantis/mcs/protocols/DissectingPaneAttributes.java,v 1.5 2003/03/26 11:43:44 sfound Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 22-Nov-01    Paul            VBM:2001110202 - Created.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from
 *                              class to string.
 * 03-May-02    Paul            VBM:2002042203 - Removed the attributes which
 *                              were retrieved from the dissecting pane and
 *                              added the dissecting pane itself.
 * 14-Mar-03    Doug            VBM:2003030409 - Added the isNextLinkFirst 
 *                              member and corresponding getters and setters.
 * 21-Mar-03    Byron           VBM:2003031907 - Added linkTo/From member 
 *                              fields and getters/setters.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols;

import com.volantis.mcs.layouts.DissectingPane;

/**
 * This encapsulates the attributes which are needed when dissecting the
 * contents of this pane.
 *
 * @mock.generate base="MCSAttributes"
 */
public class DissectingPaneAttributes
        extends MCSAttributes {

    /**
     * The DissectingPane.
     */
    private DissectingPane dissectingPane;

    /**
     * The path to the inclusion.
     */
    private String inclusionPath;


    /**
     * Varible used to determine whether or not the next shard link should come
     * before the previous link or vice versa.
     */
    private boolean isNextLinkFirst;

    /**
     * The backLinkText attribute of the part element
     */
    private String backLinkText;

    /**
     * The linkText attribute of the part element
     */
    private String linkText;

    /**
     * This constructor delegates all its work to the initialise method,
     * no extra initialisation should be added here, instead it should be
     * added to the initialise method.
     */
    public DissectingPaneAttributes() {
        initialise();
    }

    /**
     * This method should reset the state of this object back to its
     * state immediately after it was constructed.
     */
    public void resetAttributes() {
        super.resetAttributes();

        // Call this after calling super.resetAttributes to allow initialise to
        // override any inherited attributes.
        initialise();
    }

    /**
     * Initialise all the data members. This is called from the constructor
     * and also from resetAttributes.
     */
    private void initialise() {
        setTagName("pane");
        dissectingPane = null;
        inclusionPath = null;
    }

    /**
     * Set the value of the dissectingPane property.
     *
     * @param dissectingPane The new value of the dissectingPane property.
     */
    public void setDissectingPane(DissectingPane dissectingPane) {
        this.dissectingPane = dissectingPane;
    }

    /**
     * Get the value of the dissectingPane property.
     *
     * @return The value of the dissectingPane property.
     */
    public DissectingPane getDissectingPane() {
        return dissectingPane;
    }

    /**
     * Set the value of the inclusionPath property.
     *
     * @param inclusionPath The new value of the inclusionPath property.
     */
    public void setInclusionPath(String inclusionPath) {
        this.inclusionPath = inclusionPath;
    }

    /**
     * Get the value of the inclusionPath property.
     *
     * @return The value of the inclusionPath property.
     */
    public String getInclusionPath() {
        return inclusionPath;
    }

    /**
     * Setter for the isNextLinkFirst property
     *
     * @param isNextLinkFirst the new value for the property
     */
    public void setIsNextLinkFirst(boolean isNextLinkFirst) {
        this.isNextLinkFirst = isNextLinkFirst;
    }


    /**
     * Getter for the isNextLinkFirst property
     * +  * @return the isNextLinkFirst property
     */
    public boolean isNextLinkFirst() {
        return isNextLinkFirst;
    }

    public String getLinkText() {
        return linkText;
    }

    public void setLinkText(String linkText) {
        this.linkText = linkText;
    }

    public String getBackLinkText() {
        return backLinkText;
    }

    public void setBackLinkText(String backLinkText) {
        this.backLinkText = backLinkText;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 11-Jul-05	8862/1	pduffin	VBM:2005062108 Refactored layout rendering to make it more testable.

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 01-Nov-04	6068/1	tom	VBM:2004101508 renamed VolantisAttribute to MCSAttributes and added style properties container

 ===========================================================================
*/
