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
 * $Header: /src/voyager/com/volantis/mcs/protocols/XFImplicitAttributes.java,v 1.5 2003/04/23 12:51:25 chrisw Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 20-Sep-01    Paul            VBM:2001091202 - Created.
 * 19-Feb-02    Paul            VBM:2001100102 - Removed doFormField method.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from class
 *                              to string.
 * 17-Apr-03    Chris W         VBM:2003031909 - Added clientVariableName +
 *                              appropriate get & set methods.
 * 23-Apr-03    Chris W         VBM:2003031909 - getClientVariableName JavaDoc
 *                              corrected to return String not Object.                              
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols;

/**
 * Attributes for the xfimplicit tag.
 */
public class XFImplicitAttributes
        extends XFFormFieldAttributes {

    /**
     * The value to submit as part of the form data.
     */
    private String value;

    /**
     * The clientVariableName specifies the name of a client-side variable whose
     * value will be passed back to the server as the value of the xfimplicit
     * element. e.g. in wml, the clientVariableName myvar would be interpreted
     * as a variable reference of $myvar.
     */
    private String clientVariableName;

    /**
     * This constructor delegates all its work to the initialise method,
     * no extra initialisation should be added here, instead it should be
     * added to the initialise method.
     */
    public XFImplicitAttributes() {
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
        setTagName("xfimplicit");

        value = null;
        clientVariableName = null;
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

    /**
     * Get the value of the clientVariableName attribute
     *
     * @return String the value of the clientVariableName attribute
     */
    public String getClientVariableName() {
        return clientVariableName;
    }

    /**
     * Set the value of the clientVariableName attribute
     *
     * @param clientVariableName The value of the clientVariableName attribute
     */
    public void setClientVariableName(String clientVariableName) {
        this.clientVariableName = clientVariableName;
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
