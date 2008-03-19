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
 * Collection of pseudo style entities.
 *
 * @mock.generate
 */
public interface PseudoStyleEntities {

    /**
     * Get the stateful pseudo class of the specified name.
     *
     * @param cssName The CSS name of the stateful pseudo class.
     *
     * @return The stateful pseudo class with the specified name, or null if
     * there is none.
     */
    public StatefulPseudoClass getStatefulPseudoClass(String cssName);

    /**
     * Get the pseudo element of the specified name.
     *
     * @param cssName The CSS name of the pseudo element.
     *
     * @return The pseudo element with the specified name, or null.
     */
    public PseudoElement getPseudoElement(String cssName);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 22-Jul-05	9110/2	pduffin	VBM:2005072107 First stab at integrating new themes stuff together

 02-Jun-05	7997/1	pduffin	VBM:2005050324 Added styling API

 ===========================================================================
*/
