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

package com.volantis.osgi.cm;

import org.osgi.framework.Filter;
import org.osgi.framework.ServiceReference;

import java.util.Dictionary;

class FilterMatchesProperty
        implements Filter {

    private final String property;
    private final String value;

    public FilterMatchesProperty(String property, String value) {
        this.property = property;
        this.value = value;
    }

    public boolean match(ServiceReference reference) {
        String v = (String) reference.getProperty(property);
        return value.equals(v);
    }

    public boolean match(Dictionary dictionary) {
        String v = (String) dictionary.get(property);
        return value.equals(v);
    }

    public boolean matchCase(Dictionary dictionary) {
        String v = (String) dictionary.get(property);
        return value.equals(v);
    }
}
