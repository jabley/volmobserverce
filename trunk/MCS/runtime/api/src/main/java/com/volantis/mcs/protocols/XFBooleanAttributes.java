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
 * $Header: /src/voyager/com/volantis/mcs/protocols/XFBooleanAttributes.java,v 1.7 2002/03/18 12:41:18 ianw Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 28-Jun-01    Paul            VBM:2001062810 - Created.
 * 24-Jul-01    Paul            VBM:2001071103 - Added implementation of
 *                              doFormField method.
 * 26-Jul-01    Paul            VBM:2001071707 - Fixed problems with resetting
 *                              the state of the object.
 * 04-Sep-01    Paul            VBM:2001081707 - Changed the types of
 *                              falseValues and trueValues to Object to allow
 *                              TextComponentNames to be passed through to the
 *                              protocol.
 * 07-Dec-01    Paul            VBM:2001120703 - Remove default false and
 *                              true values as this needs to be handled
 *                              elsewhere.
 * 19-Feb-02    Paul            VBM:2001100102 - Removed doFormField method.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from class
                                to string.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols;

import com.volantis.mcs.protocols.assets.TextAssetReference;

public class XFBooleanAttributes
        extends XFFormFieldAttributes {

    private TextAssetReference falseValues;
    private TextAssetReference trueValues;

    /**
     * This constructor delegates all its work to the initialise method,
     * no extra initialisation should be added here, instead it should be
     * added to the initialise method.
     */
    public XFBooleanAttributes() {
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
        setTagName("xfboolean");

        falseValues = null;
        trueValues = null;
    }

    /**
     * Set the value of the falseValues property.
     *
     * @param falseValues The new value of the falseValues property.
     */
    public void setFalseValues(TextAssetReference falseValues) {
        this.falseValues = falseValues;
    }

    /**
     * Get the value of the falseValues property.
     *
     * @return The value of the falseValues property.
     */
    public TextAssetReference getFalseValues() {
        return falseValues;
    }

    /**
     * Set the value of the trueValues property.
     *
     * @param trueValues The new value of the trueValues property.
     */
    public void setTrueValues(TextAssetReference trueValues) {
        this.trueValues = trueValues;
    }

    /**
     * Get the value of the trueValues property.
     *
     * @return The value of the trueValues property.
     */
    public TextAssetReference getTrueValues() {
        return trueValues;
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
