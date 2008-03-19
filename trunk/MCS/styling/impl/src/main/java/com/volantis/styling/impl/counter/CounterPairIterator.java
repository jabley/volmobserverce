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

import com.volantis.mcs.themes.StyleIdentifier;
import com.volantis.mcs.themes.StyleInteger;
import com.volantis.mcs.themes.StyleList;
import com.volantis.mcs.themes.StylePair;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.StyleValueVisitorStub;

import java.util.List;

/**
 * Iterates over a counter-* StyleValue, which is a StyleList of StylePairs,
 * calling the iteratee for each pair encountered.
 */
class CounterPairIterator extends StyleValueVisitorStub {

    /**
     * The iteratee to process each pair.
     */
    private CounterPairIteratee iteratee;

    /**
     * Initialise.
     *
     * @param iteratee used to process each pair that is found.
     */
    public CounterPairIterator(CounterPairIteratee iteratee) {
        this.iteratee = iteratee;
    }

    // Javadoc inherited.
    public void visit(StyleList value, Object object) {

        // Iterate over the elements of the list.
        List list = value.getList();
        for (int i = 0; i < list.size(); i++) {
            StyleValue styleValue = (StyleValue) list.get(i);
            styleValue.visit(this, null);
        }
    }

    // Javadoc inherited.
    public void visit(StylePair value, Object object) {

        // Suck "string, int" out of the pair
        StyleValue first = value.getFirst();
        if (first instanceof StyleIdentifier) {
            StyleIdentifier styleIdentifier = (StyleIdentifier) first;
            String name = styleIdentifier.getName();
            Integer integer = null;

            StyleValue second = value.getSecond();
            if (second instanceof StyleInteger) {
                StyleInteger styleInteger = (StyleInteger) second;
                integer = new Integer(styleInteger.getInteger());
            }

            // Send them off to the iteratee
            iteratee.next(name, integer);
        }
    }

    public void visit(StyleIdentifier value, Object object) {
        super.visit(value, object);
    }

    public void visit(StyleInteger value, Object object) {
        super.visit(value, object);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 28-Sep-05	9487/5	pduffin	VBM:2005091203 Resolved merge conflicts

 28-Sep-05	9487/3	pduffin	VBM:2005091203 Updated JavaDoc for CSS parser and refactored

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 22-Sep-05	9578/1	adrianj	VBM:2005092102 Integrate counters into styling engine

 29-Jul-05	9114/1	geoff	VBM:2005072120 XDIMECP: Implement CSS Counters

 ===========================================================================
*/
