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
package com.volantis.mcs.runtime.styling;

import com.volantis.mcs.css.parser.CSSParser;
import com.volantis.mcs.runtime.policies.theme.StyleSheetActivator;
import com.volantis.mcs.themes.StyleSheet;
import com.volantis.styling.compiler.StyleSheetCompiler;
import com.volantis.styling.compiler.CSSCompiler;
import com.volantis.styling.sheet.CompiledStyleSheet;

import java.io.Reader;

public class CSSCompilerImpl implements CSSCompiler {

    private CSSParser parser;

    private StyleSheetCompiler compiler;

    private StyleSheetActivator activator;

    public CSSCompilerImpl(CSSParser parser, StyleSheetCompiler compiler,
            StyleSheetActivator activator) {
        if (parser == null) {
            throw new IllegalArgumentException("parser cannot be null");
        }
        if (compiler == null) {
            throw new IllegalArgumentException("compiler cannot be null");
        }
        if (activator == null) {
            throw new IllegalArgumentException("activator cannot be null");
        }
        this.parser = parser;
        this.compiler = compiler;
        this.activator = activator;
    }

    // Javadoc inherited.
    public CompiledStyleSheet compile(Reader input, String url) {

        if (input == null) {
            throw new IllegalArgumentException("input cannot be null");
        }

        CompiledStyleSheet compiledStyleSheet;

        // Parse the CSS into a style sheet.
        StyleSheet styleSheet = parser.parseStyleSheet(input, url);
        if (styleSheet == null) {
            throw new IllegalStateException("No style sheet generated");
        }

        // Activate the style sheet.
        activator.activate(styleSheet);

        // Compile the style sheet into a compiled style sheet.
        compiledStyleSheet = compiler.compileStyleSheet(styleSheet);

        return compiledStyleSheet;
    }

}
