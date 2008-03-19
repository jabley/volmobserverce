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
 * A combination of pseudo classes that represent different states within the
 * user interface.
 *
 * @mock.generate
 */
public interface StatefulPseudoClass {

    /**
     * Get the set that contains this class.
     *
     * @return The set that contains this class.
     */
    StatefulPseudoClassSet getSet();

    /**
     * Get the CSS representation of the set of pseudo classes.
     *
     * <p>The order of the individual pseudo classes within the CSS
     * representation is irrelevant so code must not rely on it being in
     * any particular order.</p>
     */
    String getCSSRepresentation();

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
