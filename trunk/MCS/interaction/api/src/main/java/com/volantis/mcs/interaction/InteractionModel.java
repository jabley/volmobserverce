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

import com.volantis.mcs.interaction.Proxy;
import com.volantis.mcs.model.descriptor.TypeDescriptor;
import com.volantis.mcs.model.descriptor.ModelDescriptor;

/**
 * Defines an interaction model.
 *
 * @mock.generate
 */
public interface InteractionModel {

    /**
     * Get the underlying model description.
     *
     * @return The underlying model description.
     */
    ModelDescriptor getModelDescriptor();

    /**
     * Create a proxy for the specified model object.
     *
     * <p>The model object is stored within the returned proxy.</p>
     *
     * @param modelObject The model object to create.
     * @return The newly created proxy.
     */
    Proxy createProxyForModelObject(Object modelObject);

    /**
     * Create a proxy for the specified type descriptor.
     *
     * @param descriptor The type descriptor.
     * @return The newly created proxy.
     */
    Proxy createProxyForType(TypeDescriptor descriptor);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 16-Nov-05	10315/1	pduffin	VBM:2005111410 Added support for copying model objects

 ===========================================================================
*/
