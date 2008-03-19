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

package com.volantis.styling.compiler;

import com.volantis.mcs.themes.StyleSheet;
import com.volantis.styling.engine.StylingEngine;
import com.volantis.styling.sheet.CompiledStyleSheet;

/**
 * Compile a style sheet into a representation than can be efficiently applied
 * to a document.
 *
 * @mock.generate
 */
public interface StyleSheetCompiler {

    /**
     * Compile a style sheet into a format that can be used by a
     * {@link StylingEngine}.
     *
     * @param styleSheet The style sheet to be compiled.
     *
     * @return The newly compiled style sheet.
     */
    CompiledStyleSheet compileStyleSheet(StyleSheet styleSheet);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 06-Dec-05	10641/1	geoff	VBM:2005113024 Pagination page rendering issues

 06-Dec-05	10621/1	geoff	VBM:2005113024 Pagination page rendering issues

 29-Sep-05	9654/1	pduffin	VBM:2005092817 Added support for expressions and functions in styles

 18-Aug-05	9007/3	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 01-Aug-05	9110/1	pduffin	VBM:2005072107 First stab at integrating new themes stuff together

 18-Jul-05	9029/4	pduffin	VBM:2005071301 Adding ability for styling engine to support nested style sheets

 15-Jul-05	9067/4	geoff	VBM:2005071415 More refactoring for: XDIMECP: Generate optimised CSS for a DOM.

 02-Jun-05	7997/1	pduffin	VBM:2005050324 Added styling API

 ===========================================================================
*/
