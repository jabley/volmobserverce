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
package com.volantis.mcs.dom2theme.impl.model;

/**
 * An internal iterator interface for {@link PseudoStylePath}s.
 *
 * @mock.generate
 */
public interface PseudoStylePathIteratee {

    /**
     * Called for each path in the collection.
     *
     * @param pseudoPath the path to be processed.
     */
    void next(PseudoStylePath pseudoPath);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-Jul-05	8668/7	geoff	VBM:2005060302 XDIMECP: Generate optimised CSS for a DOM.

 ===========================================================================
*/
