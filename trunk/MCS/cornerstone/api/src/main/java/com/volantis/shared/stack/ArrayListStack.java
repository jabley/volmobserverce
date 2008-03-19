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

package com.volantis.shared.stack;

import java.util.EmptyStackException;

public class ArrayListStack
        extends java.util.ArrayList
        implements Stack {

    public ArrayListStack() {
    }

    public ArrayListStack(int initialCapacity) {
        super(initialCapacity);
    }

    public int depth() {
        return size();
    }

    public Object peek() {
        int last = size() - 1;
        if (last < 0) {
            throw new EmptyStackException();
        }
        return get(last);
    }

    public Object peek(int depth) {
        int offset = size() - depth - 1;
        if (offset == -1) {
            throw new EmptyStackException();
        } else if (offset < -1) {
            throw new IndexOutOfBoundsException();
        }

        return get(offset);
    }

    public Object pop() {
        int last = size() - 1;
        if (last < 0) {
            throw new EmptyStackException();
        }
        return remove(last);
    }

    public Object push(Object item) {
        add(item);
        return item;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 10-Oct-05	9673/1	pduffin	VBM:2005092906 Improved validation and fixed layout formatting

 28-Jun-05	8856/2	geoff	VBM:2005062005 Refactoring for XDIMECP: Generate optimised CSS for a DOM.

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 ===========================================================================
*/
