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

package com.volantis.shared.metadata.type;

import com.volantis.shared.metadata.type.constraint.ConstraintFactory;
import com.volantis.shared.metadata.type.mutable.MutableBooleanType;
import com.volantis.shared.metadata.type.mutable.MutableChoiceDefinition;
import com.volantis.shared.metadata.type.mutable.MutableChoiceType;
import com.volantis.shared.metadata.type.mutable.MutableFieldDefinition;
import com.volantis.shared.metadata.type.mutable.MutableListType;
import com.volantis.shared.metadata.type.mutable.MutableNumberType;
import com.volantis.shared.metadata.type.mutable.MutableQuantityType;
import com.volantis.shared.metadata.type.mutable.MutableSetType;
import com.volantis.shared.metadata.type.mutable.MutableStringType;
import com.volantis.shared.metadata.type.mutable.MutableStructureType;
import com.volantis.shared.metadata.type.mutable.MutableUnitType;

/**
 * An object for creating instances of {@link MetaDataType} and related
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
public abstract class MetaDataTypeFactory {

    /**
     * Get the factory for creating <code>Constraint</code> related classes.
     *
     * @return The factory for creating <code>Constraint</code> related
     * classes.
     */
    public abstract ConstraintFactory getConstraintFactory();

    /**
     * Create a new instance of {@link MutableBooleanType}.
     *
     * @return A new instance of {@link MutableBooleanType}.
     */
    public abstract MutableBooleanType createBooleanType();

    /**
     * Create a new instance of {@link MutableNumberType}.
     *
     * @return A new instance of {@link MutableNumberType}.
     */
    public abstract MutableNumberType createNumberType();

    /**
     * Create a new instance of {@link MutableListType}.
     *
     * @return A new instance of {@link MutableListType}.
     */
    public abstract MutableListType createListType();

    /**
     * Create a new instance of {@link MutableQuantityType}.
     *
     * @return A new instance of {@link MutableQuantityType}.
     */
    public abstract MutableQuantityType createQuantityType();

    /**
     * Create a new instance of {@link MutableUnitType}.
     *
     * @return A new instance of {@link MutableUnitType}.
     */
    public abstract MutableUnitType createUnitType();

    /**
     * Create a new instance of {@link MutableSetType}.
     *
     * @return A new instance of {@link MutableSetType}.
     */
    public abstract MutableSetType createSetType();

    /**
     * Create a new instance of {@link MutableStringType}.
     *
     * @return A new instance of {@link MutableStringType}.
     */
    public abstract MutableStringType createStringType();

    /**
     * Create a new instance of {@link MutableStructureType}.
     *
     * @return A new instance of {@link MutableStructureType}.
     */
    public abstract MutableStructureType createStructureType();

    /**
     * Create a new instance of {@link MutableFieldDefinition}.
     *
     * @param name The name of the field.
     *
     * @return A new instance of {@link MutableFieldDefinition}.
     */
    public abstract MutableFieldDefinition createFieldDefinition(
            String name);

    /**
     * Create a new instance of {@link MutableChoiceType}.
     *
     * @return A new instance of {@link MutableChoiceType}.
     */
    public abstract MutableChoiceType createChoiceType();

    /**
     * Create a new instance of {@link MutableChoiceDefinition}.
     *
     * @param name The name of the choice.
     *
     * @return A new instance of {@link MutableChoiceDefinition}.
     */
    public abstract MutableChoiceDefinition createChoiceDefinition(
            String name);
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
