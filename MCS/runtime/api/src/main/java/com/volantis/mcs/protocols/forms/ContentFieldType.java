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
 * $Header: /src/voyager/com/volantis/mcs/protocols/forms/ContentFieldType.java,v 1.2 2002/09/09 10:23:41 mat Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 09-Sep-02    Mat             VBM:2002090502 - Created to handle the contents
 *                              of the xfcontent tag.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.forms;

import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.XFContentAttributes;
import com.volantis.mcs.protocols.XFFormFieldAttributes;

/**
 * This class defines the behaviour of content fields.
 */
public final class ContentFieldType implements FieldType {

    /**
     * The reference to the single allowable instance of this class.
     */
    private static final ContentFieldType singleton;

    // Initialise the static fields.
    static {
        // Always initialise to prevent a synchronization problem if we do it
        // lazily.
        singleton = new ContentFieldType();
    }

    /**
     * Get the single allowable instance of this class.
     *
     * @return The single allowable instance of this class.
     */
    public static ContentFieldType getSingleton() {
        return singleton;
    }

    /**
     * Protect the constructor to prevent any other instances being created.
     */
    private ContentFieldType() {
    }

    // Javadoc inherited from super class.
    public void doField(VolantisProtocol protocol,
            XFFormFieldAttributes attributes) throws ProtocolException {

        protocol.doContent((XFContentAttributes) attributes);
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

 15-Nov-05	10321/1	emma	VBM:2005103109 Forward port: Styling not applied correctly to some xf selectors

 14-Nov-05	10300/1	emma	VBM:2005103109 Styling not applied correctly to some xf selectors

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
