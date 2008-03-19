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
 * $Header: /src/voyager/com/volantis/mcs/protocols/CustomMarkupAttributes.java,v 1.2 2002/03/22 17:56:43 pduffin Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 21-Mar-03    Paul            VBM:2002032105 - Created to encapsulate custom
 *                              markup attributes.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols;

import java.util.Map;

/**
 * This class encapsulates attributes needed for generating custom markup.
 */
public class CustomMarkupAttributes
        extends MCSAttributes {

    /**
     * The attributes.
     */
    private Map attributes;

    /**
     * The name of the element.
     */
    private String elementName;

    /**
     * This constructor delegates all its work to the initialise method,
     * no extra initialisation should be added here, instead it should be
     * added to the initialise method.
     */
    public CustomMarkupAttributes() {
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
        attributes = null;
        elementName = null;
    }

    /**
     * Set the value of the attributes property.
     *
     * @param attributes The new value of the attributes property.
     */
    public void setAttributes(Map attributes) {
        this.attributes = attributes;
    }

    /**
     * Get the value of the attributes property.
     *
     * @return The value of the attributes property.
     */
    public Map getAttributes() {
        return attributes;
    }

    /**
     * Set the value of the elementName property.
     *
     * @param elementName The new value of the elementName property.
     */
    public void setElementName(String elementName) {
        this.elementName = elementName;
    }

    /**
     * Get the value of the elementName property.
     *
     * @return The value of the elementName property.
     */
    public String getElementName() {
        return elementName;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 01-Nov-04	6068/1	tom	VBM:2004101508 renamed VolantisAttribute to MCSAttributes and added style properties container

 ===========================================================================
*/
