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
import com.volantis.styling.impl.sheet.Styler;

/**
 * Encapsulates information needed by {@link Styler}s.
 *
 * @mock.generate base="StylerContext"
 */
public interface StandardStylerContext extends StylerContext {

    /**
     * Get the resulting styles that will be returned by the engine and should
     * be updated by the styler if its nested matcher succeeds.
     *
     * @return The resulting styles.
     */
    Styles getStyles();

    /**
     * Set the styles that will be updated by the matchers.
     *
     * @param styles The styles to update.
     */
    void setStyles(Styles styles);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-Jul-05	9029/1	pduffin	VBM:2005071301 Adding ability for styling engine to support nested style sheets

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 ===========================================================================
*/
