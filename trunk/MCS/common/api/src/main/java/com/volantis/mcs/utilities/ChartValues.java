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
 * $Header: /src/voyager/com/volantis/mcs/utilities/ChartValues.java,v 1.4 2002/03/18 12:41:19 ianw Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 02-Jan-02    Paul            VBM:2002010201 - Removed unnecessary import.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from class
                                to string.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.utilities;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import java.util.Vector;

public class ChartValues {
    private static String mark = "(c) Volantis Systems Ltd 2001. ";
    private Vector values;
    private String sValues;

    // sValue is a list of space separated values. Where more than one
    // list is required, and comma is used to separate whole lists.
    public ChartValues(String sValues) {
	values = new Vector();
	StringTokenizer st = new StringTokenizer(sValues, ",");
	while(st.hasMoreTokens()) {
	    String s = st.nextToken().trim();
	    values.add(s);
	}
	this.sValues = sValues;
    }


    public ChartValues(Vector v) {
	this.values = v;
    }

    public Iterator iterator() {
	return values.iterator();
    }

    public Vector getValues() {
	return values;
    }

    public void setValues(Vector v) {
	this.values = v;
    }

    public String toString() {
	StringBuffer sb = new StringBuffer();
	Iterator i = values.iterator();
	while(i.hasNext()) {
	    sb.append((String)i.next());
	    if(i.hasNext()) {
		sb.append(", ");
	    }
	}
	return sb.toString();
    }

    public boolean equals(Object o) {
	return values.equals(o);
    }

    public int size() {
	return values.size();
    }
}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
