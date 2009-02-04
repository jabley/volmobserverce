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
package com.volantis.mcs.dom.output.util;

/**
 * Command object for matching a character.
 */
public interface CharacterMatcher {

    /**
     * Returns true if the supplied character is deemed to be a match.
     * <p>
     * What constitutes a match is up to the individual implementations
     * of this method.
     *
     * @param c the character to match.
     *
     * @return true if <code>c</code> matches; otherwise false.
     */
    public boolean matches(char c);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 03-Oct-05	9681/1	rgreenall	VBM:2005092107 Added buffering to LineLengthRestrictingWriter and simplified implementation.

 ===========================================================================
*/
