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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols;

/**
 * @mock.generate base="XFFormFieldAttributes"
 */
public class XFUploadAttributes
        extends XFFormFieldAttributes {

    /**
     * Maximum length of the input field in characters
     */
    private int maxLength;

    /**
     * This constructor delegates all its work to the initialise method,
     * no extra initialisation should be added here, instead it should be
     * added to the initialise method.
     */
    public XFUploadAttributes() {
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
        setTagName("xfupload");

        maxLength = -1;
    }

    /**
     * Set the value of the maxLength property.
     *
     * @param maxLength The new value of the maxLength property.
     */
    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    /**
     * Get the value of the maxLength property.
     *
     * @return The value of the maxLength property.
     */
    public int getMaxLength() {
        return maxLength;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 ===========================================================================
*/
