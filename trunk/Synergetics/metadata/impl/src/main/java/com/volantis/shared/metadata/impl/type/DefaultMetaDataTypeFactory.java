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

package com.volantis.shared.metadata.impl.type;

import com.volantis.shared.metadata.impl.type.constraint.DefaultConstraintFactory;
import com.volantis.shared.metadata.type.MetaDataTypeFactory;
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
 * Default implementation of {@link MetaDataTypeFactory}.
 */
public class DefaultMetaDataTypeFactory extends MetaDataTypeFactory {

    /**
     * Factory for creating <code>Constraint</code> objects.
     */
    private final ConstraintFactory constraintFactory;

    public DefaultMetaDataTypeFactory() {
        constraintFactory = new DefaultConstraintFactory();
    }

    public ConstraintFactory getConstraintFactory() {
        return constraintFactory;
    }

    public MutableBooleanType createBooleanType() {
        return new MutableBooleanTypeImpl();
    }

    public MutableNumberType createNumberType() {
        return new MutableNumberTypeImpl();
    }

    public MutableListType createListType() {
        return new MutableListTypeImpl();
    }

    public MutableQuantityType createQuantityType() {
        return new MutableQuantityTypeImpl();
    }

    public MutableUnitType createUnitType() {
        return new MutableUnitTypeImpl();
    }

    public MutableSetType createSetType() {
        return new MutableSetTypeImpl();
    }

    public MutableStringType createStringType() {
        return new MutableStringTypeImpl();
    }

    public MutableStructureType createStructureType() {
        return new MutableStructureTypeImpl();
    }

    public MutableFieldDefinition createFieldDefinition(
            String name) {
        return new MutableFieldDefinitionImpl(name);
    }

    /**
     * Only to be used by the persistence layer
     * @return an uninitialized mutable FieldDefinition
     */
    public MutableFieldDefinition createFieldDefinition() {
        return new MutableFieldDefinitionImpl();
    }

    public MutableChoiceType createChoiceType() {
        return new MutableChoiceTypeImpl();
    }

    public MutableChoiceDefinition createChoiceDefinition(
            String name) {
        return new MutableChoiceDefinitionImpl(name);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 17-Jan-05	6560/7	tom	VBM:2004122401 Changed Javadoc

 14-Jan-05	6560/5	tom	VBM:2004122401 Completed Metadata API Implementation

 13-Jan-05	6560/3	tom	VBM:2004122401 More Metadata API implementation

 10-Jan-05	6560/1	tom	VBM:2004122401 Began implementation of the new Metadata API

 ===========================================================================
*/
