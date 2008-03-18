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

import com.volantis.shared.metadata.type.immutable.ImmutableBooleanType;
import com.volantis.shared.metadata.type.immutable.ImmutableChoiceType;
import com.volantis.shared.metadata.type.immutable.ImmutableListType;
import com.volantis.shared.metadata.type.immutable.ImmutableNumberType;
import com.volantis.shared.metadata.type.immutable.ImmutableQuantityType;
import com.volantis.shared.metadata.type.immutable.ImmutableSetType;
import com.volantis.shared.metadata.type.immutable.ImmutableStringType;
import com.volantis.shared.metadata.type.immutable.ImmutableStructureType;

/**
 * Visitor interface for visiting immutable metadata types.
 */
public interface ImmutableMetaDataTypeVisitor {

    /**
     * Visit an ImmutableBooleanType.
     *
     * @param visitee The ImmutableBooleanType to visit
     */
    public void visit(ImmutableBooleanType visitee);

    /**
     * Visit an ImmutableListType.
     *
     * @param visitee The ImmutableListType to visit
     */
    public void visit(ImmutableListType visitee);

    /**
     * Visit an ImmutableNumberType.
     *
     * @param visitee The ImmutableNumberType to visit
     */
    public void visit(ImmutableNumberType visitee);

    /**
     * Visit an ImmutableQuantityType.
     *
     * @param visitee The ImmutableQuantityType to visit
     */
    public void visit(ImmutableQuantityType visitee);

    /**
     * Visit an ImmutableSetType.
     *
     * @param visitee The ImmutableSetType to visit
     */
    public void visit(ImmutableSetType visitee);

    /**
     * Visit an ImmutableStringType.
     *
     * @param visitee The ImmutableStringType to visit
     */
    public void visit(ImmutableStringType visitee);

    /**
     * Visit an ImmutableStructureType.
     *
     * @param visitee The ImmutableStructureType to visit
     */
    public void visit(ImmutableStructureType visitee);


    /**
     * Visit an ImmutableChoiceType.
     *
     * @param visitee The ImmutableChoiceType to visit
     */
    public void visit(ImmutableChoiceType visitee);
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
