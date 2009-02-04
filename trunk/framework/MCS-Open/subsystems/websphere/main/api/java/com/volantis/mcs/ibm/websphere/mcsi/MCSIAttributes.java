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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.ibm.websphere.mcsi;

import com.volantis.mcs.papi.PAPIAttributes;

import java.util.Iterator;

/**
 * The base class of all the IAPI attribute classes.
 * 
 * @volantis-api-exclude-from PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 *
 */
public abstract class MCSIAttributes implements PAPIAttributes {
    
    /**
     * The Volantis copyright statement
     */
    static final String mark =
            "(c) Volantis Systems Ltd 2004. ";

    public String getElementName() {
        throw new UnsupportedOperationException("MCSI does not support this operation");

    }

    //Javadoc inherited
    public void reset () {
    }

    // javadoc inherited
    public PAPIAttributes getGenericAttributes() {
        throw new UnsupportedOperationException(
                "MCSIAttributes#getGenericAttributes is not supported");
    }

    // javadoc inherited
    public String getAttributeValue(String namespace, String localName) {
        throw new UnsupportedOperationException(
                "MCSIAttributes#getAttributeValue is not supported");
    }

    // javadoc inherited
    public void setAttributeValue(String namespace, String localName, String value) {
        throw new UnsupportedOperationException(
                "MCSIAttributes#setAttributeValue is not supported");
    }

    public Iterator getAttributeNames(String namespace) {
        throw new UnsupportedOperationException(
                "MCSIAttributes#getAttributeNames is not supported");
    }
}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Jun-05	8483/2	emma	VBM:2005052410 Migrate styling to use the new styling support framework (still using CSSEmulator to style underneath)

 18-May-05	8196/1	ianw	VBM:2005051203 Refactored PAPI to seperate out implementation

 08-Dec-04	6416/4	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/2	ianw	VBM:2004120703 New Build

 07-Dec-04	5800/2	ianw	VBM:2004090605 New Build system

 04-Feb-04	2828/1	ianw	VBM:2004011922 Added MCSI content handler

 ===========================================================================
*/
