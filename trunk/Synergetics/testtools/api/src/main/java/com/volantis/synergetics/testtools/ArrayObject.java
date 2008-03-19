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
 * $Header:$
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 18-May-03    Geoff           VBM:2003042904 - Created; a wrapper for arrays 
 *                              to make them into first class Objects, with 
 *                              appropriate common methods. 
 * 03-Jun-03    Allan           VBM:2003060301 - This class moved to 
 *                              Synergetics. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.synergetics.testtools;

import java.lang.reflect.Array;
import java.util.Iterator;

/**
 * A wrapper for arrays to make them into first class Objects, with appropriate
 * common methods.
 *
 * Implements <code>hashCode()</code> and <code>equals()</code> functionality
 * for an array. Useful when keying a <code>Map</code> with arrays, comparing
 * two arrays for equality, or implementing toString() for an array.
 *
 * <p>Does not make a defensive copy; behavior changes with associated array.
 * Does not account for circularly linked arrays.
 *
 * @author Bob Lee (crazybob@crazybob.org)
 */
public class ArrayObject {

    // Could use java.text.NumberFormat here instead...
    // But there is no HexNumberFormat, they do exist out there in
    // the wilds though.
    // http://cvs.sourceforge.net/cgi-bin/viewcvs.cgi/jreceiver/jreceiver/src/jreceiver/util/text/HexNumberFormat.java
    public static interface Format {

        String format(Object o);
    }

    private final Object array;

    private Format printer;

    /**
     * Constructs a key for a given array.
     */
    public ArrayObject(Object array) {
        if (array == null) {
            throw new NullPointerException();
        }
        this.array = array;
    }

    public ArrayObject(Object array, Format printer) {
        this(array);
        this.printer = printer;
    }

    public int hashCode() {
        int hashCode = 0;
        for (Iterator i = this.iterator(); i.hasNext();) {
            Object element = i.next();
            if (element != null) {
                hashCode = hashCode * 37 + element.hashCode();
            }
        }
        return hashCode;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ArrayObject)) {
            return false;
        }
        ArrayObject arrayObject = (ArrayObject) o;
        if (this.length() != arrayObject.length()) {
            return false;
        }
        Iterator i1 = this.iterator();
        Iterator i2 = arrayObject.iterator();
        while (i1.hasNext()) {
            Object o1 = i1.next();
            Object o2 = i2.next();
            if (!(o1 == null ? o2 == null : o1.equals(o2))) {
                return false;
            }
        }
        return true;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append('[');
        for (Iterator i = this.iterator(); i.hasNext();) {
            buffer.append(i.next());
            if (i.hasNext()) {
                buffer.append(", ");
            }
        }
        buffer.append(']');
        return buffer.toString();
    }

    /**
     * Constructs element iterator. Recursively wraps nested arrays.
     */
    private Iterator iterator() {
        final int length = this.length();
        return new Iterator() {

            private int index = 0;

            public boolean hasNext() {
                return index < length;
            }

            public Object next() {
                Object element = Array.get(ArrayObject.this.array, index++);
                return (element == null) ? null :
                    (element.getClass().isArray()) ?
                    new ArrayObject(element, printer) :
                    (printer == null) ? element :
                    printer.format(element);
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }

        };
    }

    /**
     * Gets length of enclosed array.
     */
    private int length() {
        return Array.getLength(this.array);
    }

}
