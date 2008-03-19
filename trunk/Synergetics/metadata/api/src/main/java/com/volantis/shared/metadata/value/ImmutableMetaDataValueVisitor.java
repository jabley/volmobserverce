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
/* ---------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2004. 
 * ---------------------------------------------------------------------------
 */

package com.volantis.shared.metadata.value;

import com.volantis.shared.metadata.value.immutable.ImmutableBooleanValue;
import com.volantis.shared.metadata.value.immutable.ImmutableChoiceValue;
import com.volantis.shared.metadata.value.immutable.ImmutableListValue;
import com.volantis.shared.metadata.value.immutable.ImmutableNumberValue;
import com.volantis.shared.metadata.value.immutable.ImmutableQuantityValue;
import com.volantis.shared.metadata.value.immutable.ImmutableSetValue;
import com.volantis.shared.metadata.value.immutable.ImmutableStringValue;
import com.volantis.shared.metadata.value.immutable.ImmutableStructureValue;
import com.volantis.shared.metadata.value.immutable.ImmutableUnitValue;

/**
 * Visitor interface for visiting immutable metadata values.
 */
public interface ImmutableMetaDataValueVisitor {
    /**
     * Visit an ImmutableBooleanValueImpl.
     *
     * @param visitee The ImmutableBooleanValueImpl to visit
     */
    public void visit(ImmutableBooleanValue visitee);

    /**
     * Visit an ImmutableListValueImpl.
     *
     * @param visitee The ImmutableListValueImpl to visit
     */
    public void visit(ImmutableListValue visitee);

    /**
     * Visit an IMMUTABLE_NUMBER_VALUE.
     *
     * @param visitee The IMMUTABLE_NUMBER_VALUE to visit
     */
    public void visit(ImmutableNumberValue visitee);

    /**
     * Visit an ImmutableQuantityValue.
     *
     * @param visitee The ImmutableQuantityValue to visit
     */
    public void visit(ImmutableQuantityValue visitee);

    /**
     * Visit an ImmutableSetValueImpl.
     *
     * @param visitee The ImmutableSetValueImpl to visit
     */
    public void visit(ImmutableSetValue visitee);

    /**
     * Visit an ImmutableStructureValue.
     *
     * @param visitee The ImmutableStructureValue to visit
     */
    public void visit(ImmutableStructureValue visitee);

    /**
     * Visit an ImmutableStringValue.
     *
     * @param visitee The ImmutableStringValue to visit
     */
    public void visit(ImmutableStringValue visitee);

    /**
     * Visit an IMMUTABLE_UNIT_VALUE.
     *
     * @param visitee The IMMUTABLE_UNIT_VALUE to visit
     */
    public void visit(ImmutableUnitValue visitee);


    /**
     * Visit an ImmutableChoiceValue.
     *
     * @param visitee The ImmutableChoiceValue to visit
     */
    public void visit(ImmutableChoiceValue visitee);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 19-Jan-05	6686/1	pduffin	VBM:2005010506 Completed code to read and write XML versions of the resource components, asset and templates. Also, fixed a few problems with the implementation of the MetaData API

 17-Jan-05	6670/1	adrianj	VBM:2005010506 Implementation of resource asset continued

 ===========================================================================
*/
