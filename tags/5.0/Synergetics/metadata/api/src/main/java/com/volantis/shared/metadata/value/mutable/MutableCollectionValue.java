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

package com.volantis.shared.metadata.value.mutable;

import com.volantis.shared.metadata.value.CollectionValue;
import com.volantis.shared.metadata.value.immutable.ImmutableMetaDataValue;
import com.volantis.shared.metadata.value.MetaDataValue;
import java.util.Collection;

/**
 * A mutable {@link CollectionValue}.
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
public interface MutableCollectionValue
        extends CollectionValue, MutableCompositeValue {

    /**
     * Get a mutable collection of {@link ImmutableMetaDataValue} instances.
     *
     * <p>Modifications made to the returned collection will change the state of
     * this object. The objects contained within the collection are not themselves
     * mutable so if their state needs to be changed then a mutable copy will
     * need to be obtained, changed and then used to replace the original
     * object.</p>
     *
     * <p>The returned collection only stores immutable instances of
     * {@link MetaDataValue}. If a user attempts to add a mutable instance to
     * it then the collection will obtain an immutable instance and store that
     * instead.</p>
     *
     * @return a mutable collection of {@link ImmutableMetaDataValue} instances.
     */
    public Collection getContentsAsMutableCollection();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 17-Jan-05	6707/1	pduffin	VBM:2005011710 Refactored device repository API to fix couple of performance and code duplication issues. Added support for retrieving device policy values as meta data

 10-Jan-05	6560/3	tom	VBM:2004122401 Began implementation of the new Metadata API

 ===========================================================================
*/
