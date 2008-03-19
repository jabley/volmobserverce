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
 * $Header: /src/voyager/com/volantis/mcs/protocols/XFTextInputAttributes.java,v 1.9 2002/07/26 12:13:10 sfound Exp $
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
 * 04-Sep-01    Paul            VBM:2001081707 - Changed validate's type to
 *                              Object to allow TextComponentNames to be
 *                              passed through to the protocol.
 * 07-Dec-01    Paul            VBM:2001120703 - Remove default type value
 *                              as this needs to be handled elsewhere.
 * 19-Feb-02    Paul            VBM:2001100102 - Removed doFormField method.
 * 14-Mar-02    Steve           VBM:2002021119 - Added inputMode attribute for
 *                              imode-HTML and mml
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from class
                                to string.
 * 26-Jul-02    Steve           VBM:2002072301 - Added emptyok attribute. This
 *                              is not set by the xftextinput element but may
 *                              be set by a protocol to signal the output of
 *                              the emptyok attribute on <input> fields.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols;

/**
 * @mock.generate base="XFFormFieldAttributes"
 */
public class XFTextInputAttributes
        extends XFFormFieldAttributes {

    /**
     * Maximum length of the input field in characters
     */
    private int maxLength;

    /**
     * Type of the input field, text..password..etc
     */
    private String type;

    private String inputMode;

    /**
     * This constructor delegates all its work to the initialise method,
     * no extra initialisation should be added here, instead it should be
     * added to the initialise method.
     */
    public XFTextInputAttributes() {
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
        setTagName("xftextinput");

        maxLength = -1;
        type = null;
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
     * Set the keyboard input mode
     *
     * @param mode The name of the mode
     */
    public void setInputMode(String mode) {
        inputMode = mode;
    }

    /**
     * Get the keyboard input mode
     *
     * @return The name of the input mode
     */
    public String getInputMode() {
        return inputMode;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 21-Jun-05	8833/1	pduffin	VBM:2005042901 Merged changes from MCS 3.3.1, improved testability of the protocols

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
