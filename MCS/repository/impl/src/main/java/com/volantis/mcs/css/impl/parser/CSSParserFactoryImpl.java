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

package com.volantis.mcs.css.impl.parser;

import com.volantis.mcs.css.parser.CSSParser;
import com.volantis.mcs.css.parser.CSSParserFactory;
import com.volantis.mcs.css.parser.DiagnosticListener;
import com.volantis.mcs.css.parser.ExtensionHandler;
import com.volantis.mcs.themes.CSSParserMode;
import com.volantis.mcs.themes.Priority;
import com.volantis.mcs.themes.StyleSheetFactory;
import com.volantis.mcs.themes.StyleValue;

import java.util.Collection;

/**
 * Default implementation of {@link CSSParserFactory}.
 */
public class CSSParserFactoryImpl
        extends CSSParserFactory {

    private static final ExtensionHandler DEFAULT_EXTENSION_HANDLER =
            new ExtensionHandler() {
                public StyleValue customValue(String token) {
                    return null;
                }

                public Priority customPriority(String token) {
                    return null;
                }

                public Collection getCustomPriorities() {
                    return null;
                }

                public Collection getCustomValues() {
                    return null;
                }
            };

    // Javadoc inherited.
    public CSSParser createStrictParser() {
        return createParser(CSSParserMode.STRICT);
    }

    // Javadoc inherited.
    public CSSParser createLaxParser() {
        return createParser(CSSParserMode.LAX);
    }

    // Javadoc inherited.
    private DiagnosticListener createLaxDiagnosticListener() {
        return new LaxDiagnosticListener();
    }

    // Javadoc inherited.
    public CSSParser createParser(CSSParserMode parserMode) {
        return createExtensibleParser(parserMode, DEFAULT_EXTENSION_HANDLER);
    }

    // Javadoc inherited.
    public CSSParser createParser(
            StyleSheetFactory factory, DiagnosticListener diagnosticListener) {
        return new CSSParserImpl(factory, diagnosticListener,
                DEFAULT_EXTENSION_HANDLER);
    }

    // Javadoc inherited.
    public CSSParser createExtensibleParser(
            CSSParserMode parserMode,
            ExtensionHandler extensionHandler) {

        DiagnosticListener diagnosticListener;
        if (parserMode == null || parserMode == CSSParserMode.STRICT) {
            diagnosticListener = createStrictDiagnosticListener();
        } else if (parserMode == CSSParserMode.LAX) {
            diagnosticListener = createLaxDiagnosticListener();
        } else {
            throw new IllegalArgumentException(
                    "Unknown parser mode '" + parserMode + "'");
        }

        return new CSSParserImpl(
                StyleSheetFactory.getDefaultInstance(),
                diagnosticListener,
                extensionHandler);
    }

    /**
     * Create a listener that will throw an exception if any diagnostic messages
     * were received while parsing.
     * <p/>
     * <p>It aggregates all diagnostic messages together and includes them in
     * the exception message.</p>
     *
     * @return A strict diagnostic listener.
     */
    private DiagnosticListener createStrictDiagnosticListener() {
        return new StrictDiagnosticListener();
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 28-Sep-05	9487/3	pduffin	VBM:2005091203 Updated JavaDoc for CSS parser and refactored

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 ===========================================================================
*/
