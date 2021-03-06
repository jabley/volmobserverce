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

/**
 * An internal iterator interface for {@link Counter}s.
 */
interface CounterIteratee {

    /**
     * Called for each instance of a {@link Counter} within a containing
     * collection.
     *
     * @param counter the counter to process.
     */
    void next(Counter counter);
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
