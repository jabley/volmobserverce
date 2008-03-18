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
 * (c) Copyright Volantis Systems Ltd. 2007. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.synergetics.descriptorstore;

import java.util.Iterator;

/**
 * A collection of Parameter names that have been requested.
 *
 * <p>
 * <strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels. </strong>
 * </p>
 */
public interface ParameterNames {

    /**
     * Add a name to the collection of parameters
     *
     * @param name the name to add
     */
    public void setName(String name);

    /**
     * Return true if the collection contains the specified name.
     *
     * @param name the name to check for
     * @return true if the collection contains the specified name.
     */
    public boolean containsName(String name);

    /**
     * Return an iterator over the parameter names
     * @return an iterator over the parameter names
     */
    public Iterator iterator();

}
