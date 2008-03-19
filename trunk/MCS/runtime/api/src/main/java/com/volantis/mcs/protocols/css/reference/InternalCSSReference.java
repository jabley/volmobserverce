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
import com.volantis.mcs.protocols.css.CSSModule;
import com.volantis.mcs.protocols.css.PlaceHolder;

/**
 * A reference to some internal CSS.
 */
public class InternalCSSReference
        implements CssReference {

    /**
     * The CSS protocol module.
     */
    private final CSSModule cssModule;

    /**
     * The place holder for the reference to the CSS.
     */
    private PlaceHolder placeHolder;

    /**
     * Initialise.
     *
     * @param cssModule The CSS protocol module.
     */
    public InternalCSSReference(CSSModule cssModule) {
        this.cssModule = cssModule;
    }

    // Javadoc inherited.
    public void writePlaceHolderMarkup(OutputBuffer buffer) {
        placeHolder = cssModule.addInlinePlaceHolder(buffer);
    }

    // Javadoc inherited.
    public void updateMarkup(String css) {
        cssModule.updateInlinePlaceHolder(placeHolder, css);
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
