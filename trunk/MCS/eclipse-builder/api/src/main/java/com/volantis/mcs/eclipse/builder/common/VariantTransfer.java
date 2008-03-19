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

import com.volantis.mcs.accessors.xml.jibx.JiBXWriter;
import com.volantis.mcs.accessors.xml.jibx.JiBXReader;
import com.volantis.mcs.policies.variants.InternalVariantBuilder;
import com.volantis.mcs.policies.variants.VariantBuilder;
import com.volantis.mcs.repository.RepositoryException;

import java.io.IOException;

/**
 * An Eclipse Transfer for providing copy/paste of variants using JiBX.
 */
public class VariantTransfer extends JiBXTransfer {
    /**
     * The type name for this transfer.
     */
    private static final String TYPE_NAME =
            "com.volantis.mcs.eclipse.builder.common.VariantTransfer";

    /**
     * The type ID for this transfer.
     */
    private static final int TYPE_ID = registerType(TYPE_NAME);

    /**
     * A single instance of this transfer.
     */
    private static VariantTransfer _instance = new VariantTransfer();

    /**
     * Private constructor to prevent instantiation other than the single
     * shared instance.
     */
    private VariantTransfer() {
    }

    protected Object byteArrayToObject(JiBXReader reader, byte[] buffer)
            throws IOException {

        final Object object = super.byteArrayToObject(reader, buffer);

        // Ensure that the object that we retrieve from the clipboard does not
        // have any colloborative only state copied.
        // Probably we should do this when the object is put into the clipboard
        // but this would require another object copy and this is simpler.
        ((InternalVariantBuilder)object).setVariantIdentifier(null);

        return object;
    }

    public static VariantTransfer getInstance() {
        return _instance;
    }

    protected boolean isValidArrayType(Object object) {
        return object instanceof VariantBuilder[];
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
