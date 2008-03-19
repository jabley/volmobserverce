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
package com.volantis.mcs.eclipse.builder.common;

import com.volantis.mcs.policies.variants.selection.InternalTarget;

/**
 * An Eclipse Transfer for providing copy/paste of targets using JiBX.
 */
public class TargetTransfer extends JiBXTransfer {
    /**
     * The type name for this transfer.
     */
    private static final String TYPE_NAME =
            "com.volantis.mcs.eclipse.builder.common.TargetTransfer";

    /**
     * The type ID for this transfer.
     */
    private static final int TYPE_ID = registerType(TYPE_NAME);

    /**
     * A single instance of this transfer.
     */
    private static TargetTransfer _instance = new TargetTransfer();

    /**
     * Private constructor to prevent instantiation other than the single
     * shared instance.
     */
    private TargetTransfer() {
    }

    public static TargetTransfer getInstance() {
        return _instance;
    }

    protected boolean isValidArrayType(Object object) {
        return object instanceof InternalTarget[];
    }

    // Javadoc inherited
    protected String[] getTypeNames() {
        return new String[] { TYPE_NAME };
    }

    // Javadoc inherited
    protected int[] getTypeIds() {
        return new int[] { TYPE_ID };
    }
}
