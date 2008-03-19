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
 * $Header: /src/voyager/com/volantis/mcs/protocols/forms/ImplicitFieldType.java,v 1.3 2002/03/18 12:41:18 ianw Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 19-Feb-02    Paul            VBM:2001100102 - Created to encapsulate any
 *                              type specific behaviour of an implicit field.
 * 04-Mar-02    Paul            VBM:2001101803 - Removed the attributes
 *                              parameter from getFieldHandler method.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from class
                                to string.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.forms;

import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.XFFormFieldAttributes;
import com.volantis.mcs.protocols.XFImplicitAttributes;

/**
 * This class defines the behaviour of implicit fields.
 */
public final class ImplicitFieldType
        implements FieldType {

    /**
     * The reference to the single allowable instance of this class.
     */
    private static final ImplicitFieldType singleton;

    // Initialise the static fields.
    static {
        // Always initialise to prevent a synchronization problem if we do it
        // lazily.
        singleton = new ImplicitFieldType();
    }

    /**
     * Get the single allowable instance of this class.
     *
     * @return The single allowable instance of this class.
     */
    public static ImplicitFieldType getSingleton() {
        return singleton;
    }

    /**
     * Protect the constructor to prevent any other instances being created.
     */
    private ImplicitFieldType() {
    }

    // Javadoc inherited from super class.
    public void doField(
            VolantisProtocol protocol,
            XFFormFieldAttributes attributes) {

        protocol.doImplicitValue((XFImplicitAttributes) attributes);
    }

    // Javadoc inherited from super class.
    public FieldHandler getFieldHandler(VolantisProtocol protocol) {
        return protocol.getFieldHandler(this);
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
