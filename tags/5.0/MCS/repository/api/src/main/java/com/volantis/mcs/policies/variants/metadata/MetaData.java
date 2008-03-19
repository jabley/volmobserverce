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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.policies.variants.metadata;

import com.volantis.mcs.policies.variants.Variant;

/**
 * The meta data of a {@link Variant}.
 * <p/>
 * This is primarily used to determine how to use the variants content once a
 * variants has been selected. Some of the complex variants selections require
 * use of meta data as well.
 * <p/>
 *
 * <p>
 * <strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with current and future releases of the
 * product at binary and source levels.</strong>
 * </p>
 *
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 * @see Variant
 * @see MetaDataBuilder
 * @see MetaDataType
 * @since 3.5.1
 */
public interface MetaData {

    /**
     * Get a new builder instance for {@link MetaData}.
     *
     * <p>The returned builder has been initialised with the values of this
     * object and will return this object from its
     * {@link MetaDataBuilder#getMetaData()} until its state is
     * changed.</p>
     *
     * @return A new builder instance.
     */
    MetaDataBuilder getMetaDataBuilder();

    /**
     * Get the type of this meta data.
     *
     * @return The type of this meta data.
     */
    MetaDataType getMetaDataType();
}
