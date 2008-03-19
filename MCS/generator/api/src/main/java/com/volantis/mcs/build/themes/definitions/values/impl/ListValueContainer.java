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
/* ---------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2007. 
 * ---------------------------------------------------------------------------
 */

package com.volantis.mcs.build.themes.definitions.values.impl;

import com.volantis.mcs.build.themes.definitions.values.Value;
import com.volantis.mcs.build.themes.definitions.values.ValueContainer;

import java.util.LinkedList;

/**
 * A container that can hold a list of values.
 */
public class ListValueContainer implements ValueContainer {
    /**
     * The list of values
     */
    private LinkedList values = new LinkedList();
    
    /**
     * Pointer pointing at next value in the list to return; 
     */
    private int nextToReturn = 0;
    
    // Javadoc inherited
    public void addValue(Value value) {
       values.add(value);
    }

    /**
     * Retrieve next contained value.
     * If there is no next value returns null, and move pointer to begin of
     * the list so new retrieving can be done.
     * Note that adding and geting values in the same moment
     * is not a good idea with this implementation
     * @return The next contained value
     */
    public Value getNext() {
        Value result = null;
        if (nextToReturn < values.size()){
            result = (Value) values.get(nextToReturn);
            nextToReturn++;
        }else{
            nextToReturn=0;
        }
        return result;
    }
}

