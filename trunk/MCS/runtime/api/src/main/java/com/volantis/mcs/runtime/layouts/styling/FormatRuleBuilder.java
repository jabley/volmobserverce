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

package com.volantis.mcs.runtime.layouts.styling;

import com.volantis.mcs.layouts.Format;
import com.volantis.mcs.themes.StyleSheet;

/**
 * Updates a style sheet with rules that associate a format with its styles.
 */
public interface FormatRuleBuilder {

    /**
     * Add rules to the style sheet that associate the format with its style
     * sheet.
     *
     * @param styleSheet The style sheet that is updated.
     * @param format The format for which the rule(s) is(are) built.
     */
    public void addRules(StyleSheet styleSheet, Format format);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 ===========================================================================
*/
