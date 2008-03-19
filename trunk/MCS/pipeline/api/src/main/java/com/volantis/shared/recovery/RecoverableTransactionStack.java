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

package com.volantis.shared.recovery;

import org.xml.sax.Locator;

import java.util.Stack;
import java.util.Iterator;

/**
 * The <code>RecoverableTransactonStack</code> class represents a LIFO stack of
 * Objects.  It wraps a java.util.Stack and delegates the usual push, pop,
 * peek, and empty methods.  Additionally it provides a method to search the
 * stack from the top down to find the first instance of a Class.
 *
 * This Stack implements the {@link RecoverableTransaction} interface and as
 * such provides the means to:
 * <ul>
 * <li> Capture the Stack state at a specified instant.
 * <li> Restore the captured state under error conditions.
 * </ul>
 *
 * Clients of RecoverableTransactionStack may define the start of a transaction
 * by calling the {@link #startTransaction()} method.  The transaction may be
 * completed successfully by a call to the {@link #commitTransaction()} method.
 * The transaction may be reverted under error conditions by a call to the
 * {@link #rollbackTransaction()} method.
 */
public class RecoverableTransactionStack
        extends AbstractRecoverableTransaction {

    /**
     * The Volantis copyright statement.
     */
    private static final String mark =
            "(c) Volantis Systems Ltd 2003. ";

    /**
     * The java.util.Stack to which this class will delegate.
     */
    protected Stack stack;

    /**
     * Create a new instance of RecoverableTransactionStack.
     */
    public RecoverableTransactionStack() {
        stack = new Stack();
    }

    /**
     * Pushes an item onto the top of this stack.
     * @param item - the item to be pushed onto this stack.
     * @return the <code>item</code> argument.
     */
    public Object push(Object item) {
        return stack.push(item);
    }

    /**
     * Removes the Object at the top of this Stack and returns that Object as
     * the value of this function.
     * @return The Object at the top of this Stack
     * @throws java.util.EmptyStackException if this stack is empty.
     */
    public Object pop() {
        return stack.pop();
    }

    /**
     * Returns the Object at the top of this Stack without removing it.
     * @return The Object at the top of this Stack
     * @throws java.util.EmptyStackException if this stack is empty.
     */
    public Object peek() {
        return stack.peek();
    }

    /**
     * Returns the element at the specified position in the stack.
     * @param index index of element to return.
     */
    public Object get(int index) {
        return stack.get(index);
    }

    /**
     * Tests if this stack is empty.
     * @return <code>true</code> if and only if this stack contains no items;
     *         <code>false</code> otherwise.
     */
    public boolean empty() {
        return stack.empty();
    }

    /**
     * Returns the number of items in the Stack.
     * @return the number of items in this Stack.
     */
    public int size() {
        return stack.size();
    }

    /**
     * Returns an iterator over the elements in this list in proper sequence.
     * @return an iterator over the elements in this list in proper sequence.
     */
    public Iterator iterator() {
        return stack.iterator();
    }

    /**
     * Returns the 1-based position where an object is on this stack.
     * If the object <tt>o</tt> occurs as an item in this stack, this method
     * returns the distance from the top of the stack of the occurrence nearest
     * the top of the stack; the topmost item on the stack is considered to be
     * at distance <tt>1</tt>. The <tt>equals</tt> method is used to compare
     * <tt>o</tt> to the items in this stack.
     *
     * @param object the desired object.
     * @return the 1-based position from the top of the stack where the object
     *         is located; the return value <code>-1</code> indicates that the
     *         object is not on the stack.
     */
    public int search(Object object) {
        return stack.search(object);
    }

    /**
     * Searches from the top of the stack down looking for the
     * first object that is an instance of the class passed in.
     * @param clazz the Class of object that we are searching for.
     * @return the first instance of the class located or null if no matches
     *         were found.
     */
    public Object find(Class clazz) {
        if (clazz == null) {
            throw new NullPointerException("Cannot find a null class");
        }
        Object lookup = null;
        Object matched = null;
        for (int i = stack.size() - 1; i >= 0 && matched == null; i--) {
            lookup = stack.get(i);
            if (clazz.isInstance(lookup)) {
                matched = lookup;
            }
        }
        return matched;
    }

    // Javadoc inherited from Cloneable interface.
    protected Object clone() throws CloneNotSupportedException {
        RecoverableTransactionStack clone =
                (RecoverableTransactionStack)super.clone();
        clone.stack = (Stack)stack.clone();
        return clone;
    }

    // Javadoc inherited from superclass
    protected void startTransactionImpl() {
        // nothing to do.
    }

    // Javadoc inherited from superclass
    protected void commitTransactionImpl(
            AbstractRecoverableTransaction poppedState) {
        // nothing to do.
    }

    // Javadoc inherited from superclass
    protected void rollbackTransactionImpl(
            AbstractRecoverableTransaction poppedState) {
        // restore the stack back to the state at the time of the last call
        // to the startTransaction method.
        RecoverableTransactionStack popped =
                (RecoverableTransactionStack)poppedState;
        stack = popped.stack;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 27-Oct-03	421/1	doug	VBM:2003101601 Added support for sql update statements

 10-Aug-03	264/2	adrian	VBM:2003072807 added error recovery support to pipeline context and associated object hierarchy

 ===========================================================================
*/
