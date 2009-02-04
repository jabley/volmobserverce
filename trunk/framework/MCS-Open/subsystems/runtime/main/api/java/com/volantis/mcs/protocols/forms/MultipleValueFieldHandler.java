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
 * $Header: /src/voyager/com/volantis/mcs/protocols/forms/MultipleValueFieldHandler.java,v 1.2 2002/03/18 12:41:18 ianw Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 19-Feb-02    Paul            VBM:2001100102 - Created to provide default
 *                              behaviour for multiple value fields.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from class
                                to string.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.forms;

import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.utilities.MarinerURL;

/**
 * This class provides default behaviour for fields with multiple values.
 */
public final class MultipleValueFieldHandler
        implements FieldHandler {

    /**
     * The reference to the single allowable instance of this class.
     */
    private static final MultipleValueFieldHandler singleton;

    // Initialise the static fields.
    static {
        // Always initialise to prevent a synchronization problem if we do it
        // lazily.
        singleton = new MultipleValueFieldHandler();
    }

    /**
     * Get the single allowable instance of this class.
     *
     * @return The single allowable instance of this class.
     */
    public static MultipleValueFieldHandler getSingleton() {
        return singleton;
    }

    /**
     * Protect the constructor to prevent any other instances being created.
     */
    private MultipleValueFieldHandler() {
    }

    /**
     * This class simply copies a single value from the source to the destination
     * url and removes it from the source url.
     */
    public void updateParameterValue(
            MarinerPageContext context,
            FieldDescriptor descriptor,
            MarinerURL src,
            MarinerURL dst) {

        String name = descriptor.getName();
        if (name != null) {
            String[] values = src.getParameterValues(name);
            src.removeParameter(name);
            if (values != null) {
                dst.setParameterValues(name, values);
            }
        }
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
