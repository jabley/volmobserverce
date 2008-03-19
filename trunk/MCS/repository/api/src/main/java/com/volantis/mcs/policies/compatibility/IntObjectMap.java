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

package com.volantis.mcs.policies.compatibility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IntObjectMap {

    private final List int2Object;

    private final Map object2Int;

    public IntObjectMap() {
        int2Object = new ArrayList();
        object2Int = new HashMap();
    }

    public void put(int i, Object object) {
        while (int2Object.size() <= i) {
            int2Object.add(null);
        }
        int2Object.set(i, object);
        object2Int.put(object, new Integer(i));
    }

    public Object get(int index) {
        return int2Object.get(index);
    }

    public int get(Object object) {
        Integer i = (Integer) object2Int.get(object);
        if (i == null) {
            return -1;
        } else {
            return i.intValue();
        }
    }

}
