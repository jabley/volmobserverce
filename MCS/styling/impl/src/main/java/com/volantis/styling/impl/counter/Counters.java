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

import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

/**
 * A collection of counters.
 * <p>
 * Counters within the collection may managed by name, or iterated over.
 * <p>
 * todo: Currently creation is automagic but destruction is manual which is
 * confusing, we should either add an event to make destruction automagic as
 * well and make it a self managing collection or externalise get and make it
 * a simple collection.
 */
class Counters {

    /**
     * A map of counter name -> counter.
     */
    private Map counterMap = new HashMap();

    /**
     * Get the counter with the name specified.
     * <p>
     * If no such counter exists, a new one is created with the name specified
     * and returned.
     *
     * @param counterName the name of the counter.
     * @return the counter with the name specified, will not be null.
     */
    public Counter get(String counterName) {

        Counter counter = find(counterName);
        if (counter == null) {
            counter = add(counterName);
        }
        return counter;
    }

    /**
     * Find the counter with the name specified.
     * <p>
     * If no such counter exists, null will be returned.
     *
     * @param counterName the name of the counter.
     * @return the counter with the name specified, or null.
     */
    public Counter find(String counterName) {

        return (Counter) counterMap.get(toKey(counterName));
    }

    /**
     * Remove the counter with the name specified.
     * <p>
     * This should only be called when the counter indicates that it has gone
     * out of scope.
     *
     * @param name the name of the counter.
     */
    public void remove(String name) {

        counterMap.remove(toKey(name));
    }


    /**
     * Add a counter with the name specified to the collection.
     *
     * @param counterName the name of the counter.
     * @return the counter added.
     */
    private Counter add(String counterName) {

        Counter counter = new Counter(counterName);
        counterMap.put(toKey(counterName), counter);
        return counter;
    }

    /**
     * Retrieve an iterator for the counters within this {@link Counters}
     * instance.
     *
     * @return An iterator over the counters
     */
    public Iterator iterator() {
        return counterMap.values().iterator();
    }

    /**
     * Iterate over the counters in the collection.
     * <p>
     * Note that there is currently no guarantee of ordering.
     *
     * @param counterIteratee the iteratee to iterate with.
     */
    public void iterate(CounterIteratee counterIteratee) {
        Iterator counters = iterator();
        while (counters.hasNext()) {
            Counter counter = (Counter) counters.next();
            counterIteratee.next(counter);
        }
    }

    /**
     * Create a map key from a name. The key must be case insensitive.
     *
     * @param name the name to form the key from
     * @return the key created
     */
    private static Object toKey(String name) {

        // todo: surely there is a better way, I can't think of it now...
        // todo: ask paul about case insensitivity in css in general
        return name.toLowerCase();
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 22-Sep-05	9578/1	adrianj	VBM:2005092102 Integrate counters into styling engine

 29-Jul-05	9114/1	geoff	VBM:2005072120 XDIMECP: Implement CSS Counters

 ===========================================================================
*/
