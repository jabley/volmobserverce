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

package com.volantis.styling.properties;




/**
 * A collection of {@link StyleProperty} definitions.
 *
 * <p>{@link StyleProperty}s can be accessed either by name, or by an index.
 * The latter is provided for performance reasons and should not be used
 * outside the runtime as the index may change from build to build.</p>
 *
 * <p>The indeces for each {@link StyleProperty}, as returned by their
 * {@link StyleProperty#getIndex getIndex()} method must form a contiguous
 * sequence from <code>0</code> to <code>{@link #count()} - 1</code>.</p>
 *
 * <p>The names of each {@link StyleProperty} must be unique and can be stored
 * externally as they will not change from build to build.</p>
 *
 * @mock.generate
 */
public interface StylePropertyDefinitions
        extends StylePropertyIterator {

    /**
     * Query the number of properties.
     *
     * @return The number of properties.
     */
    int count();

    /**
     * Get the {@link StyleProperty} at the specified index.
     *
     * @param index The index of the property.
     *
     * @return The {@link StyleProperty} at the specified index, or null.
     */
    StyleProperty getStyleProperty(int index);

    /**
     * Get the {@link StyleProperty} with the specified name.
     *
     * @param name The name of the property.
     *
     * @return The {@link StyleProperty} awith the specified name.
     */
    StyleProperty getStyleProperty(String name);

    /**
     * Get the set of standard information for the properties.
     *
     * @return The set of standard information for the properties.
     */
    PropertyDetailsSet getStandardDetailsSet();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Jun-05	7997/4	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 02-Jun-05	7997/1	pduffin	VBM:2005050324 Added styling API

 ===========================================================================
*/
