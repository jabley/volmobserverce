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

package com.volantis.mcs.model.descriptor;



/**
 * Describes a list type.
 *
 * @mock.generate base="ModifiableClassDescriptor"
 */
public interface ListClassDescriptor
        extends ModifiableClassDescriptor {

    /**
     * If the property is of type List then get the type of the objects that
     * can be inserted into that collection.
     *
     * @return The type of an item in a collection.
     */
    ClassDescriptor getItemClassDescriptor();

    /**
     * Get the accessor for the list referenced by this property.
     *
     * @return The list accessor.
     */
    ListAccessor getListAccessor();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 31-Oct-05	9961/5	pduffin	VBM:2005101811 Committing restructuring

 21-Oct-05	9961/1	pduffin	VBM:2005101811 Committing first stab at interaction layer

 ===========================================================================
*/
