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

package com.volantis.shared.metadata.impl.type;

import com.volantis.shared.metadata.impl.MetaDataObjectImpl;
import com.volantis.shared.metadata.type.MetaDataType;
import com.volantis.shared.metadata.type.VerificationError;
import com.volantis.shared.metadata.value.MetaDataValue;

import java.util.Collection;
import java.util.LinkedHashSet;

/**
 * Implementation of {@link MetaDataType}
 */
abstract class MetaDataTypeImpl extends MetaDataObjectImpl implements MetaDataType {

    // javadoc inherited
    public final Collection verify(final MetaDataValue value) {
        return verify(value, "");
    }

    /**
     * Verifies if the specified value violates any of the constraints of the
     * current type.
     *
     * <p>Subclasses should overwrite this method to verify meta data values
     * (instead of {@link #verify(MetaDataValue)}), as the recursive checks call
     * this method to check child types/values.</p>
     *
     * @param value the value to check
     * @param path the path to this value
     * @return the collection of verification errors or an empty collection if
     * the value is correct
     */
    protected Collection verify(final MetaDataValue value, final String path) {
        final Collection errors = new LinkedHashSet();
        if (value == null) {
            errors.add(new VerificationError(VerificationError.TYPE_NULL_VALUE,
                path, value, null, "Value cannot be null."));
        } else {
            // check if the value has the expected type
            final Class expectedValueType = getExpectedValueType();
            if (!expectedValueType.isAssignableFrom(value.getClass())) {
                errors.add(new VerificationError(
                    VerificationError.TYPE_INVALID_IMPLEMENTATION, path, value, null,
                    "Invalid value type. Expected: " + expectedValueType +
                        ", received: " + value.getClass().getName()));
            }
        }
        return errors;
    }

    /**
     * Returns the Class of the MetaDataValue expected of the current type.
     *
     * @return the class of the expected meta data value
     */
    protected abstract Class getExpectedValueType();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 17-Jan-05	6560/3	tom	VBM:2004122401 Changed Javadoc

 10-Jan-05	6560/1	tom	VBM:2004122401 Began implementation of the new Metadata API

 ===========================================================================
*/
