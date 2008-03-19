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

import com.volantis.mcs.model.property.PropertyIdentifier;

import java.util.List;

/**
 * Describes a bean like class in the model underlying the interaction layer.
 *
 * @mock.generate base="ModifiableClassDescriptor"
 */
public interface BeanClassDescriptor
        extends ModifiableClassDescriptor {

    /**
     * Get the property descriptors as an immutable list.
     *
     * @return The property descriptors as an immutable list.
     */
    List getPropertyDescriptors();

    /**
     * Get the descriptor for the specified property.
     *
     * @param property The property.
     * @return The descriptor.
     */
    PropertyDescriptor getPropertyDescriptor(PropertyIdentifier property);

    /**
     * Get the identifier for the specified property.
     *
     * @param name The name of the property.
     * @return The identifier.
     */ 
    PropertyIdentifier getPropertyIdentifier(String name);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 31-Oct-05	9961/8	pduffin	VBM:2005101811 Committing restructuring

 26-Oct-05	9961/5	pduffin	VBM:2005101811 Added path support

 25-Oct-05	9961/3	pduffin	VBM:2005101811 Added diagnostic support and some commands

 21-Oct-05	9961/1	pduffin	VBM:2005101811 Committing first stab at interaction layer

 ===========================================================================
*/
