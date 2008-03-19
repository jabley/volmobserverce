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
 * $Header: /src/voyager/com/volantis/mcs/protocols/ShardLinkAttributes.java,v 1.8 2002/03/18 12:41:17 ianw Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 07-Mar-01    Paul            Created.
 * 05-Jul-01    Paul            VBM:2001070509 - Force emacs to indent 2 spaces
 *                              when editing the file.
 * 26-Jul-01    Paul            VBM:2001071707 - Fixed problems with resetting
 *                              the state of the object.
 * 20-Sep-01    Paul            VBM:2001091904 - Renamed accessKey to shortcut.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from class
                                to string.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols;

/**
 * Encapsulate the attributes associated with a shard link.
 */
public class ShardLinkAttributes
        extends MCSAttributes {

    /**
     * The shortcut to associate with this menu item, or null if there is none.
     * If specified this may be either a TextComponentName, or a String.
     */
    private String shortcut;

    private String linkText;

    /**
     * This constructor delegates all its work to the initialise method,
     * no extra initialisation should be added here, instead it should be
     * added to the initialise method.
     */
    public ShardLinkAttributes() {
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
        shortcut = null;
        linkText = null;
    }

    /**
     * Set the shortcut property.
     *
     * @param shortcut The new value of the shortcut property.
     */
    public void setShortcut(String shortcut) {
        this.shortcut = shortcut;
    }

    /**
     * Get the value of the shortcut property.
     *
     * @return The value of the shortcut property.
     */
    public String getShortcut() {
        return shortcut;
    }

    /**
     * Set the linkText property.
     *
     * @param linkText The new value of the linkText property.
     */
    public void setLinkText(String linkText) {
        this.linkText = linkText;
    }

    /**
     * Get the value of the linkText property.
     *
     * @return The value of the linkText property.
     */
    public String getLinkText() {
        return linkText;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Sep-05	9128/1	pabbott	VBM:2005071114 Add XHTML 2 elements

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 01-Nov-04	6068/1	tom	VBM:2004101508 renamed VolantisAttribute to MCSAttributes and added style properties container

 ===========================================================================
*/
