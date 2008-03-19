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

package com.volantis.mcs.themes.impl.parsing;

import com.volantis.mcs.themes.parsing.ObjectParser;
import com.volantis.mcs.themes.parsing.ObjectParserFactory;

/**
 * An object parser factory implementation that provides implementations of
 * the various parsers.
 *
 * <p>Where possible a single shared instance of each parser type is used.</p>
 */
public class DefaultObjectParserFactory extends ObjectParserFactory {
    /**
     * A shared instance of an attribute selector parser.
     */
    private static final ObjectParser ATTRIBUTE_SELECTOR_PARSER =
            new AttributeSelectorParser();

    /**
     * A shared instance of a class selector parser.
     */
    private static final ObjectParser CLASS_SELECTOR_PARSER =
            new ClassSelectorParser();

    /**
     * A shared instance of a rule selector parser.
     */
    private static final ObjectParser RULE_SELECTOR_PARSER =
            new RuleSelectorParser();

    /**
     * A shared instance of a selector sequence parser.
     */
    private static final ObjectParser SELECTOR_SEQUENCE_PARSER =
            new SelectorSequenceParser();

    /**
     * A shared instance of a pseudo class selector parser.
     */
    private static final ObjectParser PSEUDO_CLASS_SELECTOR_PARSER =
            new PseudoClassSelectorParser();

    /**
     * A shared instance of a pseudo element selector parser.
     */
    private static final ObjectParser PSEUDO_ELEMENT_SELECTOR_PARSER =
            new PseudoElementSelectorParser();

    /**
     * A shared instance of a default parser.
     */
    private static final ObjectParser DEFAULT_PARSER =
            new DefaultObjectParser();

    // Javadoc inherited
    public ObjectParser createDefaultParser() {
        return DEFAULT_PARSER;
    }

    // Javadoc inherited
    public ObjectParser createAttributeSelectorParser() {
        return ATTRIBUTE_SELECTOR_PARSER;
    }

    // Javadoc inherited
    public ObjectParser createClassSelectorParser() {
        return CLASS_SELECTOR_PARSER;
    }

    // Javadoc inherited
    public ObjectParser createRuleSelectorParser() {
        return RULE_SELECTOR_PARSER;
    }

    // Javadoc inherited
    public ObjectParser createSelectorSequenceParser() {
        return SELECTOR_SEQUENCE_PARSER;
    }

    // Javadoc inherited
    public ObjectParser createPseudoClassSelectorParser() {
        return PSEUDO_CLASS_SELECTOR_PARSER;
    }

    // Javadoc inherited
    public ObjectParser createPseudoElementSelectorParser() {
        return PSEUDO_ELEMENT_SELECTOR_PARSER;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 12-Dec-05	10374/1	emma	VBM:2005111705 Interim commit

 14-Sep-05	9380/2	adrianj	VBM:2005082401 GUI support for nth-child

 05-Sep-05	9407/1	pduffin	VBM:2005083007 Removed old themes model

 21-Jul-05	8713/1	adrianj	VBM:2005060902 Enhanced GUI for creating new selectors

 21-Jul-05	8713/3	adrianj	VBM:2005060902 Enhanced GUI for creating new selectors

 ===========================================================================
*/
