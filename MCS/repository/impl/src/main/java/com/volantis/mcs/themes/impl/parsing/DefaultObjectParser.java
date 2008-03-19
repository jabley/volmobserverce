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
/* ---------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2004. 
 * ---------------------------------------------------------------------------
 */

package com.volantis.mcs.themes.impl.parsing;

import com.volantis.mcs.themes.parsing.ObjectParser;


/**
 * Default parser for converting objects to and from text.
 *
 * <p>Works by converting objects to text using their {@link Object#toString}
 * method, and converting text to String objects.</p>
 */
public class DefaultObjectParser implements ObjectParser {
    // Javadoc inherited
    public String objectToText(Object object) {
        return String.valueOf(object);
    }

    // Javadoc inherited
    public Object textToObject(String text) {
        return text;
    }
}
/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-Sep-05	9407/3	pduffin	VBM:2005083007 Removed old themes model

 31-Aug-05	9407/1	pduffin	VBM:2005083007 Changed layout style sheet builder over to using the new model, added support for nth child

 21-Jul-05	8713/1	adrianj	VBM:2005060902 Enhanced GUI for creating new selectors

 21-Jul-05	8713/2	adrianj	VBM:2005060902 Enhanced GUI for creating new selectors

 ===========================================================================
*/
