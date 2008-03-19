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

package com.volantis.mcs.dom;

/**
 *
 * <p>
 * <strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels. </strong>
 * </p>
 */
public interface Attribute {
    /**
     * Get the next attribute.
     * @return The next attribute.
     */
    Attribute getNext ();

    /**
     * Get the name.
     * @return The name.
     */
    String getName ();

    /**
     * Get the value.
     * @return The value.
     */
    String getValue ();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-May-05	8005/2	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 ===========================================================================
*/
