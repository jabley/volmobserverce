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
 * $Header: /src/voyager/com/volantis/mcs/protocols/XFActionAttributes.java,v 1.10 2003/01/21 09:04:28 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 28-Jun-01    Paul            VBM:2001062810 - Created.
 * 23-Jul-01    Paul            VBM:2001070507 - Added accessKey attribute.
 * 24-Jul-01    Paul            VBM:2001071103 - Added implementation of
 *                              doFormField method.
 * 26-Jul-01    Paul            VBM:2001071707 - Fixed problems with resetting
 *                              the state of the object.
 * 04-Sep-01    Paul            VBM:2001081707 - Changed accessKey's type to
 *                              Object to allow TextComponentNames to be
 *                              passed through to the protocol.
 * 20-Sep-01    Paul            VBM:2001091904 - Renamed accessKey to shortcut.
 * 19-Feb-02    Paul            VBM:2001100102 - Removed doFormField method and
 *                              added value attribute.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from class
                                to string.
 * 20-Jan-03    Geoff           VBM:2003011616 - Removed unused accesskey 
 *                              attribute which had already been replaced with 
 *                              a shortcut attribute on the parent class.                                  
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols;

/**
 * Encapsulate the attributes associated with an extended function form action.
 */
public class XFActionAttributes
        extends XFFormFieldAttributes {

    private String type;

    /**
     * The value of the field.
     */
    private String value;

    /**
     * This constructor delegates all its work to the initialise method,
     * no extra initialisation should be added here, instead it should be
     * added to the initialise method.
     */
    public XFActionAttributes() {
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
        setTagName("xfaction");

        type = null;
        value = null;
    }

    /**
     * Set the value of the type property.
     *
     * @param type The new value of the type property.
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Get the value of the type property.
     *
     * @return The value of the type property.
     */
    public String getType() {
        return type;
    }

    /**
     * Set the value of the value property.
     *
     * @param value The new value of the value property.
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Get the value of the value property.
     *
     * @return The value of the value property.
     */
    public String getValue() {
        return value;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
