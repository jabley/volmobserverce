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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.xml.pipeline.sax.impl.template;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

/**
 * Values from parameter bindings or parameter defaults are always held in
 * a parameter block. Parameters must be uniquely named within a given
 * parameter block.
 */
public class ParameterBlock {

    /**
     * Map from {@link String} to {@link TValue}.
     */
    private final Map parameters;

    /**
     * Initializes the new instance.
     */
    public ParameterBlock() {
        // A hashtable is used to ensure that operations on it are
        // synchronized
        parameters = new Hashtable();
    }

    /**
     * The named parameter value is added to the parameter block.
     *
     * @param parameter the name of the parameter to add (cannot be null)
     * @param value     the value of the parameter to add (cannot be null)
     * @throws IllegalArgumentException if the named parameter already exists
     */
    public synchronized void add(String parameter, TValue value) {
        if (!parameters.containsKey(parameter)) {
            parameters.put(parameter, value);
        } else {
            throw new IllegalArgumentException("Parameter " + parameter +
                    " already exists in this " +
                    "parameter block");
        }
    }

    /**
     * The named parameter is queried and, if available, its value is
     * returned.
     *
     * @param parameter the name of the parameter to query
     * @return the parameter's value, or null if the parameter doesn't exist in
     *         the parameter block
     */
    public synchronized TValue query(String parameter) {
        return (TValue) parameters.get(parameter);
    }

    /**
     * Returns an interator to the set of parameter names registered with
     * the parameter block.
     *
     * @return an iterator to the set of parameter names
     */
    public Iterator iterator() {
        return parameters.keySet().iterator();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 31-Jul-03	222/1	philws	VBM:2003071802 New pipeline API and implementation of the equals and not equals expression feature

 10-Jun-03	13/2	philws	VBM:2003030610 Integrate with Template Model Expression facilities

 ===========================================================================
*/
