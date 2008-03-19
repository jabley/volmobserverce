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

package com.volantis.mcs.css.parser;

import com.volantis.mcs.themes.StyleSheetFactory;
import com.volantis.mcs.themes.CSSParserMode;
import com.volantis.synergetics.factory.MetaDefaultFactory;

/**
 * Factory for {@link CSSParser} related classes.
 */
public abstract class CSSParserFactory {

    /**
     * Obtain a reference to the default factory implementation.
     */
    protected static final MetaDefaultFactory metaDefaultFactory;

    static {
        metaDefaultFactory =
                new MetaDefaultFactory(
                        "com.volantis.mcs.css.impl.parser.CSSParserFactoryImpl",
                        CSSParserFactory.class.getClassLoader());
    }

    /**
     * Get the default instance of this factory.
     *
     * @return The default instance of this factory.
     */
    public static CSSParserFactory getDefaultInstance() {
        return (CSSParserFactory) metaDefaultFactory.getDefaultFactoryInstance();
    }

    /**
     * Create a parser that fails if any diagnostic messages were received
     * while parsing.
     *
     * @return A CSSParser that fails on any diagnostic message.
     */
    public abstract CSSParser createStrictParser();

    /**
     * Create a parser that ignores all diagnostic messages.
     *
     * @return A CSSParser that ignores all diagnostic messages.
     */
    public abstract CSSParser createLaxParser();

    /**
     * Create either a strict or lax parser depending on the mode.
     *
     * @param parserMode The mode of the parser.
     * @return The newly created parser.
     */
    public abstract CSSParser createParser(CSSParserMode parserMode);

    /**
     * Create a parser.
     *
     * @param factory            The factory to use to create style sheet related objects.
     * @param diagnosticListener The listener to which diagnostic messages may
     *                           be sent.
     * @return A CSSParser.
     */
    public abstract CSSParser createParser(
            StyleSheetFactory factory, DiagnosticListener diagnosticListener);

    /**
     * Create a strict yet extensible parser.
     *
     * @param parserMode
     * @param extensionHandler The handler for the extension.
     *
     * @return The extension.
     */
    public abstract CSSParser createExtensibleParser(
            CSSParserMode parserMode,
            ExtensionHandler extensionHandler);

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
