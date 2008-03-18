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
package com.volantis.synergetics.utilities;

import java.util.EmptyStackException;

/**
 * A stack implementation based on a ArrayList instead of the standard Vector
 * that java.util.Stack is based on. An ArrayList is faster than a Vector.
 * <p/>
 * This class was originally copied from the MCS class of the same name.
 */
public class ArrayListStack extends java.util.ArrayList {

    /**
     * Construct a new ArrayListStack with a specified initial capacity
     *
     * @param initialCapacity the initial capacity
     */
    public ArrayListStack(int initialCapacity) {
        super(initialCapacity);
    }

    /**
     * Provide the depth of the stack
     *
     * @return the stack depth
     */
    public int depth() {
        return size();
    }

    /**
     * Get the Object at the top of the stack without removing the Object
     *
     * @return the Object at the top of the stack
     */
    public Object peek() {
        int last = size() - 1;
        if (last < 0) {
            throw new EmptyStackException();
        }
        return get(last);
    }

    /**
     * Get the Object at a specified depth in the stack withot removing the
     * Object
     *
     * @param depth stack depth at which to get the Object
     * @return the Object at the specified stack depth
     */
    public Object peek(int depth) {
        int offset = size() - depth - 1;
        if (offset == -1) {
            throw new EmptyStackException();
        } else if (offset < -1) {
            throw new IndexOutOfBoundsException();
        }

        return get(offset);
    }

    /**
     * Pop the top Object off the stack.
     *
     * @return the popped Object
     */
    public Object pop() {
        int last = size() - 1;
        if (last < 0) {
            throw new EmptyStackException();
        }
        return remove(last);
    }

    /**
     * Push an Object onto the top of the stack.
     *
     * @param item the Object to push.
     */
    public void push(Object item) {
        add(item);
    }


}
