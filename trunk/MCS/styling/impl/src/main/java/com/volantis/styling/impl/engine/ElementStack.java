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

package com.volantis.styling.impl.engine;

import com.volantis.mcs.themes.StyleValues;
import com.volantis.shared.stack.ArrayListStack;
import com.volantis.shared.stack.Stack;
import com.volantis.styling.sheet.CompiledStyleSheet;

/**
 * A stack of the information relating to the element.
 *
 * @todo Write tests for this.
 *
 * @mock.generate
 */
public class ElementStack {

    /**
     * The underlying type unsafe stack.
     */
    protected final Stack stack;

    /**
     * Initialise.
     */
    public ElementStack() {
        stack = new ArrayListStack();
    }

    /**
     * Get the containing element's stack frame.
     *
     * @return The containing element's stack frame.
     */
    public ElementStackFrame getContainingElementStackFrame() {
        return (ElementStackFrame) stack.peek(1);
    }

    /**
     * Push the specified frame onto the stack.
     *
     * @param frame The frame to push.
     */
    public void pushElementStackFrame(ElementStackFrame frame) {
        stack.push(frame);
    }

    /**
     * Get the current element's stack frame.
     *
     * @return The current element's stack frame, or null if the stack is
     * empty.
     */
    public ElementStackFrame getCurrentElementStackFrame() {
        if (stack.isEmpty()) {
            return null;
        } else {
            return (ElementStackFrame) stack.peek();
        }
    }

    /**
     * Pop the top most frame off the stack and return it.
     *
     * @return The top most frame.
     */
    public ElementStackFrame popElementStackFrame() {
        return (ElementStackFrame) stack.pop();
    }

    /**
     * Push the element information onto the stack.
     *
     * @param namespace The namespace of the element.
     * @param localName The local name of the element.
     * @param elementId The integer identifier for the element
     * @param inlineStyleSheet The style sheet representing any styling applied
     * through the style attribute.
     */
    public void push(String namespace, String localName, int elementId,
                     CompiledStyleSheet inlineStyleSheet) {
        if (namespace == null) {
            throw new IllegalArgumentException("namespace cannot be null");
        }
        if (localName == null) {
            throw new IllegalArgumentException("localName cannot be null");
        }

        ElementStackFrame containing = getCurrentElementStackFrame();
        if (containing != null) {
            containing.incrementChildCount();
        }

        ElementStackFrame frame = new ElementStackFrameImpl();
        frame.setNamespace(namespace);
        frame.setLocalName(localName);
        frame.setChildCount(0);
        frame.setElementId(elementId);
        frame.setInlineStyleSheet(inlineStyleSheet);
        pushElementStackFrame(frame);
    }

    /**
     * Pop the element information from the stack.
     *
     * @param namespace The expected namespace of the popped element.
     * @param localName The expected local name of the popped element.
     * @return ElementStackFrame Return the element stack from which is popped.
     */
    public ElementStackFrame pop(String namespace, String localName) {
        if (namespace == null) {
            throw new IllegalArgumentException("namespace cannot be null");
        }
        if (localName == null) {
            throw new IllegalArgumentException("localName cannot be null");
        }
        ElementStackFrame frame = popElementStackFrame();
        String poppedNamespace = frame.getNamespace();
        String poppedLocalName = frame.getLocalName();

        if (!poppedNamespace.equals(namespace)
                || !poppedLocalName.equals(localName)) {
            throw new IllegalStateException(
                    "Popped element " +
                    "{" + poppedNamespace + "}" + poppedLocalName +
                    " does not match expected element " +
                    "{" + namespace + "}" + localName);
        }

        return frame;
    }

    /**
     * Set the property values associated with the current stack frame.
     *
     * @param values The property values.
     */
    public void setPropertyValues(StyleValues values) {
        ElementStackFrame frame = getCurrentElementStackFrame();
        frame.setValues(values);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 10-Oct-05	9673/1	pduffin	VBM:2005092906 Improved validation and fixed layout formatting

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 18-Jul-05	9029/1	pduffin	VBM:2005071301 Adding ability for styling engine to support nested style sheets

 28-Jun-05	8856/1	geoff	VBM:2005062005 Refactoring for XDIMECP: Generate optimised CSS for a DOM.

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 ===========================================================================
*/
