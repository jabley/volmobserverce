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
package com.volantis.synergetics.descriptorstore.impl;

import com.volantis.synergetics.descriptorstore.Parameters;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Default implementation of the Parameters imterface.
 */
public class DefaultParameters implements Parameters {

    /**
     * Delegate to an underlying SortedMap. Using a sorted map allows us to
     * compare contents without thier order making a difference.
     */
    private HashMap map = new HashMap();

    // javadoc inherited
    public void setParameterValue(String name, String value) {
        map.put(name, value);
    }

    // javadoc inherited
    public String getParameterValue(String name) {
        return (String) map.get(name);
    }

    // javadoc inherited
    public boolean containsName(String name) {
        return map.containsKey(name);
    }

    // javadoc inherited
    public Iterator iterator() {
        final Iterator delegate = map.entrySet().iterator();
        // implement a trivial iterator
        return new Iterator() {
            public boolean hasNext() {
                return delegate.hasNext();
            }

            public Object next() {
                Map.Entry entry = (Map.Entry) delegate.next();
                return new DefaultEntry((String)entry.getKey(),
                                        (String)entry.getValue());
            }

            public void remove() {
                delegate.remove();
            }
        };
    }

    // javadoc inherited
    public boolean equals(Object obj) {
        boolean result = false;
        if (getClass() == obj.getClass()) {
            DefaultParameters other = (DefaultParameters) obj;
            result = map.equals(other.map);
        }
        return result;
    }

    // javadoc inherited
    public int hashCode() {
        return map.hashCode();
    }

    /**
     * Default implementation of the Parameters.Entry interface
     */
    public static class DefaultEntry implements Parameters.Entry{

        /**
         * The name of the parameter
         */
        private String name;

        /**
         * the value of the parameter
         */
        private String value;

        public DefaultEntry(String name, String value) {
            this.name = name;
            this.value = value;
        }

        // javadoc inherited
        public String getName() {
            return name;
        }

        // javadoc inherited
        public String getValue() {
            return value;
        }
    }

}
