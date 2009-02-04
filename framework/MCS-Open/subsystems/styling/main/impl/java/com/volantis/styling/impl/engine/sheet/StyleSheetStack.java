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

package com.volantis.styling.impl.engine.sheet;

import com.volantis.styling.sheet.CompiledStyleSheet;
import com.volantis.synergetics.cornerstone.stack.Stack;
import com.volantis.synergetics.cornerstone.stack.ArrayListStack;

/**
 * A stack of {@link CompiledStyleSheet}s.
 */
public class StyleSheetStack {

    /**
     * The underlying type unsafe stack.
     */
    private final Stack stack;

    /**
     * Initialise.
     */
    public StyleSheetStack() {
        stack = new ArrayListStack();
    }

    /**
     * Push the style sheet on the stack.
     *
     * @param styleSheet The style sheet to push.
     */
    public void push(CompiledStyleSheet styleSheet) {

        if (styleSheet == null) {
            throw new IllegalArgumentException("styleSheet cannot be null");
        }

        stack.push(styleSheet);
    }

    /**
     * Pop the style sheet off the stack.
     *
     * @param styleSheet The style sheet that is expected to be on the top of
     * the stack.
     */
    public void pop(CompiledStyleSheet styleSheet) {
        if (styleSheet == null) {
            throw new IllegalArgumentException("styleSheet cannot be null");
        }

        CompiledStyleSheet poppedStyleSheet = (CompiledStyleSheet) stack.pop();
        if (poppedStyleSheet != styleSheet) {
            throw new IllegalStateException(
                    "Popped style sheet " + poppedStyleSheet +
                    " does not match expected style sheet " + styleSheet);
        }
    }

    /**
     * Get the current depth of the stack.
     *
     * @return The depth of the stack.
     */
    public int depth() {
        return stack.depth();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-Jul-05	9029/1	pduffin	VBM:2005071301 Adding ability for styling engine to support nested style sheets

 ===========================================================================
*/
