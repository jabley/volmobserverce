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
import com.volantis.mcs.css.parser.CSSParserFactory;
import com.volantis.mcs.runtime.RuntimeProject;
import com.volantis.mcs.runtime.policies.theme.StyleSheetActivatorImpl;
import com.volantis.mcs.utilities.MarinerURL;
import com.volantis.mcs.themes.CSSParserMode;
import com.volantis.styling.compiler.StyleSheetCompiler;
import com.volantis.styling.compiler.StyleSheetCompilerFactory;
import com.volantis.styling.compiler.StyleSheetCompilerFactory;
import com.volantis.styling.compiler.CSSCompiler;

/**
 * Class to build {@link com.volantis.styling.compiler.CSSCompiler}s.
 */
public class CSSCompilerBuilder {

    /**
     * Used to create style sheet compilers which transform a style sheet into
     * a compiled style sheet.
     */
    private StyleSheetCompilerFactory styleSheetCompilerFactory;

    /**
     * The project to resolve policy references relative to.
     * <p>
     * Optional only if the CSS is guaranteed not to contain policy references.
     */
    private RuntimeProject project;

    /**
     * The base URL to resolve policy references relative to.
     * <p>
     * Optional only if the CSS is guaranteed not to contain policy references.
     */
    private MarinerURL baseURL;

    /**
     * The mode of the parser, defaults to {@link CSSParserMode#STRICT}.
     */
    private CSSParserMode parserMode;

    /**
     * Initialise.
     */
    public CSSCompilerBuilder() {
    }

    /**
     * Set the style sheet compiler factory that this css compiler will use.
     * Mandatory.
     *
     * @param styleSheetCompilerFactory the style sheet compiler factory to use.
     */
    public void setStyleSheetCompilerFactory(StyleSheetCompilerFactory
            styleSheetCompilerFactory) {
        this.styleSheetCompilerFactory = styleSheetCompilerFactory;
    }

    /**
     * @see project
     */
    public void setProject(RuntimeProject project) {
        this.project = project;
    }

    /**
     * @see baseURL
     */
    public void setBaseURL(MarinerURL baseURL) {
        this.baseURL = baseURL;
    }

    /**
     * @see parserMode
     */
    public void setParserMode(CSSParserMode parserMode) {
        this.parserMode = parserMode;
    }

    /**
     * Get a CSS compiler using the objects provided earlier.
     *
     * @return a CSS compiler.
     */
    public CSSCompiler getCSSCompiler() {

        if (styleSheetCompilerFactory == null) {
            throw new IllegalStateException(
                    "style sheet compiler factory must be provided");
        }

        // Create a CSS parser to parse CSS into a(n uncompiled) style sheet.
        // We hardcode using the strict parser. This is primarily because
        // Paul said it's not much good in the other modes - no error recovery.
        // Each CSS compiler needs it's own CSS parser as the CSS parser is
        // not thread safe.
        CSSParserFactory parserFactory = CSSParserFactory.getDefaultInstance();

        CSSParser cssParser = parserFactory.createParser(parserMode);            

        // Create a style sheet compiler to compile style sheets into compiled
        // style sheets.
        // Each CSS compiler needs it's own style sheet compiler since the
        // style sheet compiler is not thread safe.
        final StyleSheetCompiler styleSheetCompiler =
                styleSheetCompilerFactory.createStyleSheetCompiler();

        // Create the activator which will activate any mcs specific values
        // such as mcs-component-uri(). If project and/or baseURL are null then
        // we can only compile css which can be guaranteed not to contain
        // values which require activation, eg the default style sheet. 
        StyleSheetActivatorImpl activator = new StyleSheetActivatorImpl(
                project, baseURL);

        // Create the compiler itself from it's constituent objects.
        final CSSCompilerImpl cssCompiler = new CSSCompilerImpl(cssParser,
                styleSheetCompiler, activator);

        return cssCompiler;
    }

}
