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

package com.volantis.styling.impl.engine;

import com.volantis.styling.Styles;

/**
 * A container for {@link Styles}.
 *
 * <p>This is used to allow the {@link Styles} to be changed without objects
 * that reference this from having to know when they are changed.</p>
 *
 * @mock.generate
 */
public class StylesContainer {

    /**
     * The contained styles.
     */
    private Styles styles;

    /**
     * Get the styles.
     *
     * @return The styles.
     */
    public Styles getStyles() {
        return styles;
    }

    /**
     * Set the styles.
     *
     * @param styles The styles.
     */
    public void setStyles(Styles styles) {
        this.styles = styles;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-Jul-05	9029/1	pduffin	VBM:2005071301 Adding ability for styling engine to support nested style sheets

 ===========================================================================
*/
