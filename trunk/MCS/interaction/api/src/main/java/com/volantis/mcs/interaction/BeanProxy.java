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

package com.volantis.mcs.interaction;

import com.volantis.mcs.model.descriptor.BeanClassDescriptor;
import com.volantis.mcs.model.property.PropertyIdentifier;

/**
 * A proxy for a bean like object in the model.
 *
 * <p>As far as the interaction layer is concerned a bean object is an object
 * that simply has a set of properties of various different types.</p>
 *
 * <p>This has a reference to a proxy object for every property in the model
 * object, irrespective of whether that property is a simple type, or another
 * model object.</p>
 */
public interface BeanProxy
        extends ParentProxy {

    /**
     * Get the descriptor of the underlying model object.
     *
     * @return The descriptor of the underlying model object.
     */
    BeanClassDescriptor getBeanClassDescriptor();

    /**
     * Get the value of the proxy's property.
     *
     * <p>If no proxy has been associated with the property then this will
     * automatically create one based on the description of that property.</p>
     *
     * @param property The property whose proxy should be returned.
     * @return The proxy for the property.
     */
    Proxy getPropertyProxy(PropertyIdentifier property);

    /**
     * Set the value of the proxy's property.
     *
     * @param property The property whose proxy should be set.
     * @param newProxy The value of the proxy's property.
     * @return The old value of the proxy's property.
     */
    Object setPropertyProxy(PropertyIdentifier property, Proxy newProxy);

    /**
     * Get the property with which the associated proxy is associated.
     *
     * @param proxy A child proxy.
     * @return The property with which the proxy is associated.
     * @throws IllegalStateException if the proxy is not associated with any
     *                               property.
     */
    PropertyIdentifier getPropertyForProxy(Proxy proxy);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 31-Oct-05	9961/7	pduffin	VBM:2005101811 Committing restructuring

 26-Oct-05	9961/5	pduffin	VBM:2005101811 Improved validation, checked for duplicate devices, added support for validation in the runtime

 25-Oct-05	9961/3	pduffin	VBM:2005101811 Added diagnostic support and some commands

 21-Oct-05	9961/1	pduffin	VBM:2005101811 Committing first stab at interaction layer

 ===========================================================================
*/
