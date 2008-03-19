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

import com.volantis.mcs.build.themes.definitions.values.ListValue;
import com.volantis.mcs.build.themes.definitions.values.Value;

import java.io.PrintStream;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Implemenation of a list value.
 */
public class ListValueImpl implements ListValue {
    /**
     * The first contained value.
     */
    private LinkedList values = new LinkedList();
    
    // Javadoc inherited
    public void setNext(Value next){
        values.add(next);
    }

    // Javadoc inherited
    public void writeConstructCode(String indent, PrintStream out) {
        Iterator i = values.iterator();
        out.print(indent);
        //Arrays.asList(new StyleValue[] {df, sd, sd});
        out.print("styleValueFactory.getList(java.util.Arrays.asList(new StyleValue[] {");
        while(i.hasNext()){
            Value v = (Value)i.next();
            v.writeConstructCode("",out);
            if (i.hasNext()){
                out.print(", ");
            }
        }
        out.print("}))");
    }
}
