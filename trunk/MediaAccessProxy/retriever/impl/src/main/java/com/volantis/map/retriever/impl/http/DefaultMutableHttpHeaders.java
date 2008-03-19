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
 * (c) Volantis Systems Ltd 2007. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.map.retriever.impl.http;

import com.volantis.map.retriever.http.MutableHttpHeaders;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;

/**
 * Default implementation of {@link com.volantis.map.retriever.http.MutableHttpHeaders}.
 */
public class DefaultMutableHttpHeaders implements MutableHttpHeaders {

    /**
     * A case insensitive map of (header name -> List) for each of the
     * headers we are managing. Each list contains one or more header values.
     */
    private TreeMap headerMap = new TreeMap(String.CASE_INSENSITIVE_ORDER);

    // Javadoc inherited.
    public void addHeader(String name, String value) {

        List values = (List) headerMap.get(name);
        if (values == null) {
            values = new ArrayList();
            headerMap.put(name, values);
        }
        values.add(value);
    }

    // Javadoc inherited.
    public void removeHeader(String name) {

        headerMap.remove(name);
    }

    // Javadoc inherited.
    public String getHeader(String name) {

        String value = null;

        List values = (List) headerMap.get(name);
        if (values != null) {
            value = (String) values.get(0);
        }
        return value;
    }

    // Javadoc inherited.
    public Enumeration getHeaders(String name) {

        List values = (List) headerMap.get(name);
        return enumerate(values);
    }

    // Javadoc inherited.
    public Enumeration getHeaderNames() {

        Set names = headerMap.keySet();
        return enumerate(names);
    }

    /**
     * Helper method to create an enumeration for a collection.
     *
     * @param values the collection of values to enumerate over, may be null
     *      or empty.
     * @return the enumeration for the values provided.
     */
    private Enumeration enumerate(Collection values) {

        if (values == null) {
            values = Collections.EMPTY_LIST;
        }
        return new Vector(values).elements();
    }

}
