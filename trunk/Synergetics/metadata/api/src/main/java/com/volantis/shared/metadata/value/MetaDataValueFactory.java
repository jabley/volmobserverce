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

package com.volantis.shared.metadata.value;

import com.volantis.shared.metadata.value.mutable.MutableBooleanValue;
import com.volantis.shared.metadata.value.mutable.MutableChoiceValue;
import com.volantis.shared.metadata.value.mutable.MutableListValue;
import com.volantis.shared.metadata.value.mutable.MutableNumberValue;
import com.volantis.shared.metadata.value.mutable.MutableQuantityValue;
import com.volantis.shared.metadata.value.mutable.MutableSetValue;
import com.volantis.shared.metadata.value.mutable.MutableStringValue;
import com.volantis.shared.metadata.value.mutable.MutableStructureValue;

/**
 * An object for creating instances of {@link MetaDataValue} and related
 * classes.
 * <p>
 * <strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User extensions of this class are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels. </strong>
 * </p>
 *
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 */
public abstract class MetaDataValueFactory {

    /**
     * Create a new instance of {@link MutableBooleanValue}.
     *
     * @return A new instance of {@link MutableBooleanValue}.
     */
    public abstract MutableBooleanValue createBooleanValue();

    /**
     * Create a new instance of {@link MutableListValue}.
     *
     * @return A new instance of {@link MutableListValue}.
     */
    public abstract MutableListValue createListValue();

    /**
     * Create a new instance of {@link MutableNumberValue}.
     *
     * @return A new instance of {@link MutableNumberValue}.
     */
    public abstract MutableNumberValue createNumberValue();

    /**
     * Create a new instance of {@link MutableQuantityValue}.
     *
     * @return A new instance of {@link MutableQuantityValue}.
     */
    public abstract MutableQuantityValue createQuantityValue();

    /**
     * Create a new instance of {@link MutableSetValue}.
     *
     * @return A new instance of {@link MutableSetValue}.
     */
    public abstract MutableSetValue createSetValue();

    /**
     * Create a new instance of {@link MutableStringValue}.
     *
     * @return A new instance of {@link MutableStringValue}.
     */
    public abstract MutableStringValue createStringValue();

    /**
     * Create a new instance of {@link MutableStructureValue}.
     *
     * @return A new instance of {@link MutableStructureValue}.
     */
    public abstract MutableStructureValue createStructureValue();

    /**
     * Create a new instance of {@link MutableChoiceValue}.
     *
     * @return A new instance of {@link MutableChoiceValue}.
     */
    public abstract MutableChoiceValue createChoiceValue();
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
