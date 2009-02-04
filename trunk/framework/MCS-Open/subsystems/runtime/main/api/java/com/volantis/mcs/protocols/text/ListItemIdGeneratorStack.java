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
 * $Header: /src/voyager/com/volantis/mcs/protocols/text/ListItemIdGeneratorStack.java,v 1.2 2003/01/29 14:30:57 adrian Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 29-Jan-03    Adrian          VBM:2003012104 - Refactored from 
 *                              SMSListItemIdGeneratorStack inner class in SMS 
 *                              protocol - made methods public instead of 
 *                              package private. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.text;

import java.util.Stack;


/**
 * A class to manage the objects we use to emulate the formatting for OL 
 * and UL tags.
 * <p>
 * It is structured as a stack to reflect the nested nature of markup. 
 */
public final class ListItemIdGeneratorStack {
    private final Stack stack = new Stack();

    public ListItemIdGenerator peek() {
        if (stack.size() == 0) {
            throw new IllegalStateException("attempt to peek list item " +
                    "id generator when stack is empty");
        }
        return (ListItemIdGenerator) stack.peek();
    }

    public void pop() {
        if (stack.size() == 0) {
            throw new IllegalStateException("attempt to pop list item " +
                    "id generator when stack is empty");
        }
        stack.pop();
    }

    public void push(ListItemIdGenerator generator) {
        stack.push(generator);
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
