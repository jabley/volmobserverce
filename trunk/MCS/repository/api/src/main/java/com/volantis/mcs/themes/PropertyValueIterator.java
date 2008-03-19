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

package com.volantis.mcs.themes;

import com.volantis.shared.iteration.IterationAction;
import com.volantis.mcs.themes.PropertyValue;
import com.volantis.mcs.themes.PropertyValueIteratee;

import java.util.Iterator;

/**
 * Iterates over a contained sequence of {@link PropertyValue}s.
 */
public interface PropertyValueIterator {

    /**
     * Iterate over the contained sequence of {@link PropertyValue}.
     *
     * @param iteratee The object that will be invoked for each
     * {@link PropertyValue}.
     */
    IterationAction iteratePropertyValues(PropertyValueIteratee iteratee);

    /**
     * Get an external iterator over the contained sequence of
     * {@link PropertyValue}s.
     *
     * @return An iterator over a sequence of {@link PropertyValue}.
     */
    Iterator propertyValueIterator();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 15-Jul-05	9067/3	geoff	VBM:2005071415 More refactoring for: XDIMECP: Generate optimised CSS for a DOM.

 08-Jun-05	7997/4	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 02-Jun-05	7997/1	pduffin	VBM:2005050324 Added styling API

 ===========================================================================
*/
