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
 * A Parameters instance contains a set of Name, Value pairs
 *
 * <p>
 * <strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels. </strong>
 * </p>
 */
public interface Parameters {

    /**
     * Set the specified parameter name to have the given value. The parameter
     * will be created if it does not already exist
     *
     * @param name the name
     * @param value the value
     */
    public void setParameterValue(String name, String value);

    /**
     * Return the value for the specified parameter.
     *
     * @param name the name of the parameter whose value should be returned
     * @return the value associated with the specified value, or null.
     */
    public String getParameterValue(String name);

    /**
     * Return true if the collection contains the specified name
     *
     * @param name the name to check for
     * @return true if the collection contains the specified name
     */
    public boolean containsName(String name);

    /**
     * Return an iterator over the entries in the Parameters instance. These
     * will be represented Parameter.Entry instances.
     *
     * @return an iterator over the Map.Entry contents of the Parameters
     * instance
     */
    public Iterator iterator();


    /**
     * An entry in the iterator
     */
    public static interface Entry {

        /**
         * The name of the parameter
         *
         * @return the name of the parameter
         */
        public String getName();

        /**
         * The value of the parameter
         *
         * @return the value of the parameter
         */
        public String getValue();
    }
}
