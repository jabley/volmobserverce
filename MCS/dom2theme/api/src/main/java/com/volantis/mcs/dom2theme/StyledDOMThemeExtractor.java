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
package com.volantis.mcs.dom2theme;

import com.volantis.mcs.dom.Document;
import com.volantis.mcs.themes.StyleSheet;

/**
 * Extracts a style sheet from a styled DOM.
 * <p>
 * By styled DOM, we mean an MCS DOM with un-normalized styles attached to
 * elements where appropriate.
 */
public interface StyledDOMThemeExtractor {

    /**
     * Extracts a style sheet from a styled DOM.
     *
     * @param styledDom a styled DOM.
     * @return the extracted style sheet.
     */
    StyleSheet extract(Document styledDom);

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-Jul-05	8668/5	geoff	VBM:2005060302 XDIMECP: Generate optimised CSS for a DOM.

 ===========================================================================
*/
