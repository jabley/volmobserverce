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
 * $Header: /src/voyager/com/volantis/mcs/protocols/AnchorAttributes.java,v 1.25 2003/04/10 12:53:24 philws Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 05-Jul-01    Paul            VBM:2001070509 - Added this header and set the
 *                              default tag name.
 * 23-Jul-01    Paul            VBM:2001070507 - Added content attribute which
 *                              is used to store the body content of the
 *                              anchor tag.
 * 26-Jul-01    Paul            VBM:2001071707 - Fixed problems with resetting
 *                              the state of the object.
 * 04-Sep-01    Paul            VBM:2001081707 - Changed accessKey's type to
 *                              Object to allow TextComponentNames to be
 *                              passed through to the protocol.
 * 20-Sep-01    Paul            VBM:2001091904 - Renamed accessKey to shortcut.
 * 21-Sep-01    Doug            VBM:2001090302 - changed href's type to
 *                              Object to allow LinkComponentNames to be
 *                              passed through to the protocol.
 * 31-Jan-02    Paul            VBM:2001122105 - Added onBlur and onFocus.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from
 *                              class to string.
 * 23-May-02    Paul            VBM:2002042202 - Changed the type of the
 *                              content from String to Object and removed the
 *                              setOnBlur and setOnFocus methods.
 * 05-Jun-02    Byron           VBM:2002053002 - Added support for tabindex by
 *                              adding getTabindex and setTabindex methods
 * 17-Jun-02    Byron           VBM:2002061001 - Changed capitalisation for
 *                              xxxxtabindex
 * 06-Aug-02    Paul            VBM:2002080604 - Added target attribute.
 * 19-Mar-03    Phil W-S        VBM:2002111502 - Refactored to inherit common
 *                              attributes shared by Anchor and PhoneNumber
 *                              elements. Changed file indent.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols;

import com.volantis.mcs.protocols.MAPAttributes;

/**
 * Encapsulate the attributes associated with an anchor.
 */
public class AnchorAttributes
        extends AnchorBaseAttributes {

    private String segment;

    /**
     * The name of the region where the content referred to by the anchor will
     * be placed.
     */
    private String target;

    /**
     * This constructor delegates all its work to the initialise method,
     * no extra initialisation should be added here, instead it should be
     * added to the initialise method.
     */
    public AnchorAttributes() {
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
        // Set the default tag name, this is the name of the tag which makes
        // the most use of this class.
        setTagName("a");

        segment = null;
        target = null;
    }

    /**
     * Set the value of the segment property.
     *
     * @param segment The new value of the segment property.
     */
    public void setSegment(String segment) {
        this.segment = segment;
    }

    /**
     * Get the value of the segment property.
     *
     * @return The value of the segment property.
     */
    public String getSegment() {
        return segment;
    }

    /**
     * Set the value of the target property.
     *
     * @param target The new value of the target property.
     */
    public void setTarget(String target) {
        this.target = target;
    }

    /**
     * Get the value of the target property.
     *
     * @return The value of the target property.
     */
    public String getTarget() {
        return target;
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

 15-Mar-04	3422/1	geoff	VBM:2004030907 name attribute not rendered on a tag

 12-Mar-04	3403/1	geoff	VBM:2004030907 name attribute not rendered on a tag

 ===========================================================================
*/
