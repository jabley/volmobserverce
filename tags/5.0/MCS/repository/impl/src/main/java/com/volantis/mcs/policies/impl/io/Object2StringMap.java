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

package com.volantis.mcs.policies.impl.io;

import java.util.HashMap;
import java.util.Map;

/**
 * A mapping between an object and a string.
 */
public class Object2StringMap {

    private Map object2String;
    private Map string2Object;

    public Object2StringMap() {
        object2String = new HashMap();
        string2Object = new HashMap();
    }

    public void map(Object object, String string) {
        object2String.put(object, string);
        string2Object.put(string, object);
    }

    public String object2String(Object object) {
        String string = (String) object2String.get(object);
        if (string == null) {
            throw new IllegalArgumentException("Unknown object " + object);
        }

        return string;
    }

    public Object string2Object(String string) {
        if (string == null) {
            return null;
        }
        
        Object object = string2Object.get(string);
        if (object == null) {
            throw new IllegalArgumentException("Unknown string " + string);
        }

        return object;
    }
}
