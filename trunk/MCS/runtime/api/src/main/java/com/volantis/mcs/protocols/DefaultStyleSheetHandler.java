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
package com.volantis.mcs.protocols;

import com.volantis.mcs.runtime.styling.CSSCompilerBuilder;
import com.volantis.mcs.runtime.styling.DefaultStyleSheetCompilerFactory;
import com.volantis.styling.compiler.CSSCompiler;
import com.volantis.styling.sheet.CompiledStyleSheet;

import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Handler for default style sheets.
 */
public class DefaultStyleSheetHandler {

    /**
     * Compile a Style Sheet.
     *
     * @param cssInputResourceStream - InputSteam of the style sheet to compile
     * @return CompiledStyleSheet
     */
    public static CompiledStyleSheet compileStyleSheet(
            InputStream cssInputResourceStream) {

        CompiledStyleSheet compiledStyleSheet = null;

        try {
            // Create a CSS compiler.
            CSSCompilerBuilder builder = new CSSCompilerBuilder();
            builder.setStyleSheetCompilerFactory(
                    DefaultStyleSheetCompilerFactory.getDefaultInstance());
            CSSCompiler cssCompiler = builder.getCSSCompiler();

            // convert the css into StyleSheet
            final InputStreamReader reader = new InputStreamReader(
                    cssInputResourceStream);
            compiledStyleSheet = cssCompiler.compile(reader, null);

        } catch (ExceptionInInitializerError e) {
        }

        return compiledStyleSheet;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 06-Dec-05	10641/1	geoff	VBM:2005113024 Pagination page rendering issues

 06-Dec-05	10621/1	geoff	VBM:2005113024 Pagination page rendering issues

 29-Sep-05	9654/1	pduffin	VBM:2005092817 Added support for expressions and functions in styles

 27-Sep-05	9487/2	pduffin	VBM:2005091203 Committing new CSS Parser

 08-Sep-05	9447/4	ibush	VBM:2005090604 Create default style sheet and load in style engine

 06-Sep-05	9447/1	ibush	VBM:2005090604 Create default style sheet and load in style engine

 ===========================================================================
*/
