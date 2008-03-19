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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.map.agent;

import com.volantis.synergetics.descriptorstore.Parameters;

import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

/**
 */
public class ParametersImpl implements Parameters {
    private final Map entries;
    public ParametersImpl() {
        entries = new HashMap();
    }

    public void setParameterValue(final String name, final String value) {
        entries.put(name, new Entry(name, value));
    }

    public String getParameterValue(final String name) {
        final Entry entry = (Entry) entries.get(name);
        return entry.getValue();
    }

    public boolean containsName(final String name) {
        return entries.containsKey(name);
    }

    public Iterator iterator() {
        return entries.values().iterator();
    }

    private static class Entry implements Parameters.Entry {
        private final String name;
        private final String value;

        public Entry(final String name, final String value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public String getValue() {
            return value;
        }
    }
}
