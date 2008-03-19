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

package com.volantis.styling;

/**
 * A representation of a pseudo entity that can have styles associated with
 * them.
 *
 * <p>A pseudo entity is either a pseudo element, or a pseudo class. All of the
 * former can have styles associated with them but of the latter some affect
 * whether a selector matches, cannot have styles associated with them and are
 * not included in this.</p>
 *
 * @mock.generate 
 */
public interface PseudoStyleEntity {

    /**
     * Get the CSS representation for this style entity.
     *
     * @return The CSS representation, may not be null.
     */
    String getCSSRepresentation();

    /**
     * Accept a visitor to visit this object.
     *
     * @param visitor the visitor to accept.
     */
    void accept(PseudoStyleEntityVisitor visitor);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 22-Jul-05	9110/2	pduffin	VBM:2005072107 First stab at integrating new themes stuff together

 15-Jul-05	9067/3	geoff	VBM:2005071415 More refactoring for: XDIMECP: Generate optimised CSS for a DOM.

 02-Jun-05	7997/1	pduffin	VBM:2005050324 Added styling API

 ===========================================================================
*/
