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

import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.properties.DisplayKeywords;
import com.volantis.styling.Styles;
import com.volantis.styling.values.PropertyValues;

import java.util.Iterator;

/**
 * Applies counter-* style properties values to a set of counters, making
 * the current value of each counter available.
 */
public class CounterEngine {

    /**
     * The counters that are currently in scope.
     */
    private Counters counters = new Counters();

    /**
     * Calls startElement on a collection of counters.
     */
    private CounterIteratee counterElementStarter = new CounterIteratee() {

        public void next(Counter counter) {
            counter.startElement();
        }
    };

    /**
     * Walks a style value containing a list of counter pairs, applying each
     * pair as a reset to the appropriate counter.
     */
    private CounterPairIterator counterResetter = new CounterPairIterator(
            new CounterPairIteratee() {

        public void next(String name, Integer value) {
            final Counter counter = counters.get(name);
            if (value == null) {
                counter.reset();
            } else {
                counter.reset(value.intValue());
            }
        }
    });

    /**
     * Walks a style value containing a list of counter pairs, applying each
     * pair as a increment to the appropriate counter.
     */
    private CounterPairIterator counterIncrementer = new CounterPairIterator(
            new CounterPairIteratee() {

        public void next(String name, Integer value) {
            final Counter counter = counters.get(name);
            if (value == null) {
                counter.increment();
            } else {
                counter.increment(value.intValue());
            }
        }
    });

    /**
     * Indicate to the counters engine that the start tag for a styled element
     * has been received. The styles associated with the element are passed in
     * so that any counter-* properties may be interpreted.
     * <p>
     * Note that currently only the properties on the topmost styles are
     * considered. In particular, :before and :after are ignored.
     *
     * @param styles the styles associated with the element, may not be null.
     *
     * todo: extend to support pseudo elements if necessary
     * todo: not sure if it actually makes sense for counter reset and increment properties on pseudo elements to affect the engine.
     */
    public void startElement(Styles styles) {

        if (styles == null) {
            throw new IllegalStateException("styles cannot be null");
        }

        counters.iterate(counterElementStarter);

        PropertyValues values = styles.getPropertyValues();

        if (values != null) {

            // If this styles has display:none ...
            StyleValue display = values.getComputedValue(
                    StylePropertyDetails.DISPLAY);
            if (display == DisplayKeywords.NONE) {
                // ... then it cannot increment or reset a counter.
            } else {
                // ... else it can increment and reset counters.
                // So process the counters.

                // Process any resets before any increments.
                StyleValue reset = values.getComputedValue(
                        StylePropertyDetails.COUNTER_RESET);
                if (reset != null) {
                    reset.visit(counterResetter, null);
                }
                StyleValue increment = values.getComputedValue(
                        StylePropertyDetails.COUNTER_INCREMENT);
                if (increment != null) {
                    increment.visit(counterIncrementer, null);
                }
            }
        }
    }

    /**
     * Indicate to the counters engine that the end tag for a styled element
     * has been received.
     */
    public void endElement() {
        Iterator it = counters.iterator();
        while (it.hasNext()) {
            Counter counter = (Counter) it.next();
            boolean done = counter.endElement();
            // If this counter has gone out of scope ...
            if (done) {
                // ... then remove it.
                it.remove();
            }
        }
    }

    /**
     * Return the current value of the named counter, or null if the counter
     * does not currently exist.
     *
     * @param name the name of the counter to query.
     * @param autoCreate True if the counter should be created if not found
     * @return the value of the counter or null.
     */
    public Integer getCounter(String name, boolean autoCreate) {

        Integer value = null;

        Counter counter = findCounter(name, autoCreate);

        if (counter != null) {
            value = new Integer(counter.value());
        }

        return value;
    }

    /**
     * Returns all current in-scope values of the named counter, or null if the
     * counter does not currently exist.
     *
     * @param name the name of the counter to query.
     * @param autoCreate True if the counter should be created if not found
     * @return An array of values of the counter
     */
    public int[] getCounterValues(String name, boolean autoCreate) {
        int[] values = null;

        Counter counter = findCounter(name, autoCreate);

        if (counter != null) {
            values = counter.values();
        }

        return values;
    }

    /**
     * Find a counter by name.
     *
     * @param name The name of the counter
     * @param autoCreate True if the counter should be created if not found
     * @return The named counter, or null if it was not found or created
     */
    private Counter findCounter(String name, boolean autoCreate) {
        Counter counter = counters.find(name);

        if (counter == null && autoCreate) {
            counter = counters.get(name);
            counter.reset();
        }

        return counter;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-Dec-05	10512/1	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 29-Nov-05	10347/3	pduffin	VBM:2005111405 Massive changes for performance

 21-Nov-05	10347/1	pduffin	VBM:2005111405 Cleaned up PropertyValues to remove synthesised properties and moved specified into an extended interface

 30-Sep-05	9635/1	adrianj	VBM:2005092817 Counter functions for CSS

 22-Sep-05	9578/1	adrianj	VBM:2005092102 Integrate counters into styling engine

 29-Jul-05	9114/1	geoff	VBM:2005072120 XDIMECP: Implement CSS Counters

 ===========================================================================
*/
