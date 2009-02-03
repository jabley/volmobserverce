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

package com.volantis.synergetics.cornerstone.stack;

/**
 *
 * <p>
 * <strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels. </strong>
 * </p>
 *
 * @volantis-api-include-in InternalAPI
 */
public interface Stack {

    /**
     * Pushes an item onto the top of this stack.
     *
     * @param   item   the item to be pushed onto this stack.
     * @return  the <code>item</code> argument.
     */
    public Object push(Object item);

    /**
     * Removes the object at the top of this stack and returns that
     * object as the value of this function.
     *
     * @return     The object at the top of this stack.
     * @exception  java.util.EmptyStackException  if this stack is empty.
     */
    public Object pop();

    /**
     * Looks at the object at the top of this stack without removing it
     * from the stack.
     *
     * @return     the object at the top of this stack.
     * @exception  java.util.EmptyStackException  if this stack is empty.
     */
    public Object peek();

    /**
     * Looks at the object at the specified depth without removing it from
     * the stack.
     *
     * @param depth The depth of the object to look at, 0 is the top.
     *
     * @return The object at the specified depth.
     */
    public Object peek(int depth);

    /**
     * Tests if this stack is empty.
     *
     * @return  <code>true</code> if and only if this stack contains
     *          no items; <code>false</code> otherwise.
     */
    public boolean isEmpty();

    /**
     * The depth of this stack.
     *
     * @return The depth of this stack, 0 if the stack is empty.
     */
    public int depth();
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
