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
 * (c) Volantis Systems Ltd 2007. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.shared.metadata.type.mutable;

import com.volantis.shared.metadata.mutable.MutableMetaDataObject;
import com.volantis.shared.metadata.type.ChoiceDefinition;
import com.volantis.shared.metadata.type.MetaDataType;

/**
 * A mutable {@link ChoiceDefinition}.
 * 
 * <p>
 * <strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels. </strong>
 * </p>
 * 
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 */
public interface MutableChoiceDefinition extends ChoiceDefinition,
        MutableMetaDataObject {
    /**
     * Set the type of the choice.
     * 
     * <p>
     * An immutable instance of {@link MetaDataType} will be stored. If a user
     * attempts to set a mutable instance, then an immutable instance will be
     * obtained first and stored instead.
     * </p>
     * 
     * @param type The type of the choice, may not be null.
     */
    void setType(MetaDataType type);
}
