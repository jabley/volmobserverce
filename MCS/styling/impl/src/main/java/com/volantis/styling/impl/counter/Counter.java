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

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a CSS counter which understands nesting.
 * <p>
 * This handles nesting by maintaining a stack of elements, each of which
 * has a value. As startElement is called and we move down the stack, by
 * default each element shares the value of it's parent. Increments affect the
 * value of the current element, which may in turn affect it's parent value if
 * they are shared. Resets cause a new value to be used by the current element
 * which isolates any changes to its values from its parent value.
 */
class Counter {

    /**
     * The default value to reset to.
     */
    private static final int DEFAULT_RESET_VALUE = 0;

    /**
     * The default value to increment by.
     */
    private static final int DEFAULT_INCREMENT_VALUE = 1;

    /**
     * The stack of "elements" that this counter knows about.
     */
    private ElementStack elementStack = new ElementStack();

    /**
     * The name of the counter.
     */
    private String name;

    /**
     * Initialise.
     * <p>
     * This will be called when we have a first reference to a counter. As such
     * it performs an implicit startElement. This also means that start and end
     * element calls will be unbalanced, as we expect to have one extra end
     * before the counter goes out of scope.
     *
     * @param name the name of the counter.
     */
    public Counter(String name) {

        this.name = name;

        // Do the implicit start element.
        Element element = new Element();
        elementStack.push(element);
    }

    /**
     * Return the name of this counter.
     *
     * @return the name of the counter.
     */
    public String getName() {

        return name;
    }

    /**
     * Indicate to the counter that a start tag has been received.
     * <p>
     * <strong>NOTE:</strong> this should only be called if the element is not
     * display:none
     */
    public void startElement() {

        if (elementStack.empty()) {
            throw new IllegalStateException(
                    "Attempt to reuse counter illegally");
        }

        Element element = new Element();

        // Copy the parent's value down.
        Element parent = elementStack.peek();
        if (parent.getValue() != null) {
            element.setValue(parent.getValue());
        }

        elementStack.push(element);
    }

    /**
     * Indicate to the counter that an end tag has been received.
     * <p>
     * <strong>NOTE:</strong> this should only be called if the element is not
     * display:none
     *
     * @return true if the counter is now out of scope and should be discarded,
     *      false it is still in scope and should be kept.
     */
    public boolean endElement() {

        if (elementStack.empty()) {
            throw new IllegalStateException(
                    "Cannot end when not in an element");
        }

        elementStack.pop();

        return elementStack.empty();
    }

    /**
     * Reset the value of this counter to the default value (0).
     */
    public void reset() {
        reset(DEFAULT_RESET_VALUE);
    }

    /**
     * Reset the value of this counter to a new value.
     *
     * @param newValue the new value of the counter.
     */
    public void reset(int newValue) {

        if (elementStack.empty()) {
            throw new IllegalStateException(
                    "Cannot reset when not in an element");
        }

        Element element = elementStack.peek();
        Value elementValue = element.getValue();
        Element parent = null;
        Value parentValue = null;
        if (elementStack.depth() >= 2) {
            parent = elementStack.peek(1);
            parentValue = parent.getValue();
        }

        // If this element has no value or it has a value which is the same
        // as it's parent ...
        if (elementValue == null || parentValue == elementValue) {
            // ... then we need to create a new value which applies from this
            // point onwards.
            element.setValue(new Value(newValue));
        } else {
            // ... else we just reset the counter's existing value.
            element.getValue().reset(newValue);
        }
    }

    /**
     * Increment the current value of this counter by the default amount (1).
     */
    public void increment() {

        increment(DEFAULT_INCREMENT_VALUE);
    }

    /**
     * Increment the current value of this counter by the amount supplied.
     *
     * @param incrementValue the value to increment the counter by.
     */
    public void increment(int incrementValue) {

        if (elementStack.empty()) {
            throw new IllegalStateException(
                    "Cannot increment when not in an element");
        }

        // increment must do an implicit reset if required.
        Element element = elementStack.peek();
        Value value = element.getValue();
        // If the element has no existing value for this counter
        if (value == null) {
            // then do an implicit reset.
            value = new Value(DEFAULT_RESET_VALUE);
            element.setValue(value);
        }
        value.increment(incrementValue);
    }

    /**
     * Return the current value of the counter.
     *
     * @return the current value of the counter.
     */
    public int value() {

        if (elementStack.empty()) {
            throw new IllegalStateException(
                    "Cannot get when not in an element");
        }

        Value value = elementStack.peek().getValue();
        if (value == null) {
            throw new IllegalStateException(
                    "Must reset or increment before get");
        }

        return value.get();
    }

    /**
     * Return all current in-scope values of the counter from the top of the
     * document down.
     *
     * @return the current in-scope values of the counter
     */
    public int[] values() {
        List valueList = new ArrayList();
        Value currentValue = null;
        for (int i = elementStack.depth() - 1; i >= 0; i--) {
            Value stackValue = elementStack.peek(i).getValue();
            if (currentValue != stackValue) {
                valueList.add(stackValue);
                currentValue = stackValue;
            }
        }
        int[] values = new int[valueList.size()];
        for (int i = 0; i < values.length; i++) {
            values[i] = ((Value) valueList.get(i)).get();
        }
        return values;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 30-Sep-05	9635/2	adrianj	VBM:2005092817 Counter functions for CSS

 22-Sep-05	9578/1	adrianj	VBM:2005092102 Integrate counters into styling engine

 01-Aug-05	9114/3	geoff	VBM:2005072120 XDIMECP: Implement CSS Counters

 29-Jul-05	9114/1	geoff	VBM:2005072120 XDIMECP: Implement CSS Counters

 ===========================================================================
*/
