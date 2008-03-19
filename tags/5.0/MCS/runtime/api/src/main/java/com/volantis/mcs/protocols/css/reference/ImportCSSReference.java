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

package com.volantis.mcs.protocols.css.reference;

import com.volantis.mcs.protocols.OutputBuffer;

/**
 * A reference to some external CSS that is imported by some inline CSS.
 *
 * @todo This is not currently supported.
 */
public class ImportCSSReference
        implements CssReference {

    // Javadoc inherited.
    public void writePlaceHolderMarkup(OutputBuffer buffer) {
        throw new UnsupportedOperationException();
    }

    // Javadoc inherited.
    public void updateMarkup(String css) {
        throw new UnsupportedOperationException();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 22-Jul-05	9110/1	pduffin	VBM:2005072107 First stab at integrating new themes stuff together

 ===========================================================================
*/
