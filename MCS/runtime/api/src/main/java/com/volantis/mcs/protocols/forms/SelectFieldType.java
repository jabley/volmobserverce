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
 * $Header: /src/voyager/com/volantis/mcs/protocols/forms/SelectFieldType.java,v 1.4 2003/04/17 10:21:07 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 19-Feb-02    Paul            VBM:2001100102 - Created to encapsulate any
 *                              type specific behaviour of a select field.
 * 04-Mar-02    Paul            VBM:2001101803 - Made this an abstract base
 *                              class for multiple and single select fields
 *                              as it is necessary to be able to distinguish
 *                              between multiple and single select fields by
 *                              their type.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from class
                                to string.
 * 16-Apr-03    Geoff           VBM:2003041603 - Add declaration for  
 *                              ProtocolException where necessary.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.forms;

import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.XFFormFieldAttributes;
import com.volantis.mcs.protocols.XFSelectAttributes;

/**
 * This class defines the behaviour of select fields.
 */
public abstract class SelectFieldType
        implements FieldType {

    /**
     * Protect the constructor to prevent any other instances being created.
     */
    protected SelectFieldType() {
    }

    // Javadoc inherited from super class.
    public void doField(
            VolantisProtocol protocol,
            XFFormFieldAttributes attributes) throws ProtocolException {

        protocol.doSelectInput((XFSelectAttributes) attributes);
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
