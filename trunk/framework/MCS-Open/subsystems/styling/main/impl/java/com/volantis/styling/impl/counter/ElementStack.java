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
package com.volantis.styling.impl.counter;

import com.volantis.synergetics.cornerstone.stack.ArrayListStack;
import com.volantis.synergetics.cornerstone.stack.Stack;


/**
 * A stack of counter {@link Element}s.
 */
class ElementStack {

    private Stack stack = new ArrayListStack();

    /**
     * @see com.volantis.synergetics.cornerstone.stack.Stack#peek
     */
    public Element peek() {

        return (Element) stack.peek();
    }

    /**
     * @see com.volantis.synergetics.cornerstone.stack.Stack#push
     */
    public void push(Element element) {

        stack.push(element);
    }

    /**
     * @see com.volantis.synergetics.cornerstone.stack.Stack#pop
     */
    public Element pop() {

        return (Element) stack.pop();
    }

    /**
     * @see com.volantis.synergetics.cornerstone.stack.Stack#depth
     */
    public int depth() {

        return stack.depth();
    }

    /**
     * @see com.volantis.synergetics.cornerstone.stack.Stack#peek(int)
     */
    public Element peek(int i) {

        return (Element) stack.peek(i);
    }

    /**
     * @see com.volantis.synergetics.cornerstone.stack.Stack#isEmpty
     */
    public boolean empty() {
        
        return stack.isEmpty();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 10-Oct-05	9673/1	pduffin	VBM:2005092906 Improved validation and fixed layout formatting

 22-Sep-05	9578/1	adrianj	VBM:2005092102 Integrate counters into styling engine

 29-Jul-05	9114/1	geoff	VBM:2005072120 XDIMECP: Implement CSS Counters

 ===========================================================================
*/
