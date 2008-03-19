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
package com.volantis.mcs.dissection.string;

import com.volantis.mcs.dissection.DissectionException;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * A concrete subclass of {@link CompositeDissectableString} which makes it
 * suitable for easy testing. 
 * <p>
 * {@link CompositeDissectableString} is designed to "leech" off a host which
 * owns the children that it iterates over, so this implementation simply
 * implements child ownership and iteration. 
 */ 
public class TestCompositeString extends CompositeDissectableString {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    private int totalChars;
    private int totalCost;
    
    private ArrayList children = new ArrayList();
    
    public void add(DissectableString string) throws DissectionException {
        children.add(string);
        totalChars += string.getLength();
        totalCost += string.getCost();
    }
    
    public void initialise() {
        initialise(totalChars, totalCost, children.size());
    }

    public void forEachChild(InternalIterator iterator)
            throws DissectionException {
        Iterator itr = children.iterator();
        while (itr.hasNext()) {
            DissectableString string = (DissectableString)itr.next();
            iterator.next(string);
        }
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 10-Jul-03	774/1	geoff	VBM:2003070703 merge from mimas and fix renames manually

 10-Jul-03	770/1	geoff	VBM:2003070703 merge from metis and rename files manually

 10-Jul-03	751/1	geoff	VBM:2003070703 second go at cleaning up WBDOM test cases

 24-Jun-03	365/1	geoff	VBM:2003061005 first go at long string dissection; still needs cleanup and more testing.

 ===========================================================================
*/
