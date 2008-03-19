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
/* ---------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2004. 
 * ---------------------------------------------------------------------------
 */

package com.volantis.mcs.themes.parsing;

import com.volantis.synergetics.factory.MetaDefaultFactory;

/**
 * A factory class for object parsers used by the themes API.
 */
public abstract class ObjectParserFactory {
    /**
     * Set up the meta default factory instance.
     */
    private static final MetaDefaultFactory metaDefaultFactory =
            new MetaDefaultFactory(
           "com.volantis.mcs.themes.impl.parsing.DefaultObjectParserFactory",
           ObjectParserFactory.class.getClassLoader());

    /**
     * Returns the default instance of this factory.
     *
     * @return the default instance of this factory
     */
    public static ObjectParserFactory getDefaultInstance() {
        return (ObjectParserFactory)
                metaDefaultFactory.getDefaultFactoryInstance();
    }

    /**
     * Creates a default parser that converts objects to strings using their
     * {@link Object#toString} method.
     *
     * @return A default parser
     */
    public abstract ObjectParser createDefaultParser();

    /**
     * Creates a parser for attribute selectors.
     *
     * @return A parser for attribute selectors
     */
    public abstract ObjectParser createAttributeSelectorParser();

    /**
     * Creates a parser for class selectors.
     *
     * @return A parser for class selectors
     */
    public abstract ObjectParser createClassSelectorParser();

    /**
     * Creates a parser for all selectors supported by a rule (combined
     * selectors or selector sequences).
     *
     * @return A parser for all selectors supported by a rule
     */
    public abstract ObjectParser createRuleSelectorParser();

    /**
     * Creates a parser for selector sequences.
     *
     * @return A parser for selector sequences
     */
    public abstract ObjectParser createSelectorSequenceParser();

    /**
     * Creates a parser for pseudo class selectors.
     *
     * @return A parser for pseudo class selectors
     */
    public abstract ObjectParser createPseudoClassSelectorParser();

    /**
     * Creates a parser for pseudo element selectors.
     *
     * @return A parser for pseudo element selectors
     */
    public abstract ObjectParser createPseudoElementSelectorParser();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 12-Dec-05	10374/1	emma	VBM:2005111705 Interim commit

 20-Sep-05	9380/5	adrianj	VBM:2005082401 Tidy up and javadoc nth-child support

 14-Sep-05	9380/2	adrianj	VBM:2005082401 GUI support for nth-child

 13-Sep-05	9496/1	pduffin	VBM:2005091211 Replaced text-decoration with individual properties

 05-Sep-05	9407/3	pduffin	VBM:2005083007 Removed old themes model

 21-Jul-05	8713/1	adrianj	VBM:2005060902 Enhanced GUI for creating new selectors

 ===========================================================================
*/
