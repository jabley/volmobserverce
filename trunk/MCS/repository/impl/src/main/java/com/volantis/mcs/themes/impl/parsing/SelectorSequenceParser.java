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

package com.volantis.mcs.themes.impl.parsing;

import com.volantis.mcs.themes.AttributeSelector;
import com.volantis.mcs.themes.ClassSelector;
import com.volantis.mcs.themes.ElementSelector;
import com.volantis.mcs.themes.IdSelector;
import com.volantis.mcs.themes.PseudoClassSelector;
import com.volantis.mcs.themes.PseudoElementSelector;
import com.volantis.mcs.themes.Selector;
import com.volantis.mcs.themes.SelectorSequence;
import com.volantis.mcs.themes.InlineStyleSelector;
import com.volantis.mcs.themes.parsing.ObjectParser;

import java.util.Iterator;
import java.util.List;

public class SelectorSequenceParser implements ObjectParser {

    /**
     * A parser for attribute selectors.
     */
    private static final ObjectParser ATTRIBUTE_PARSER =
            new AttributeSelectorParser();

    /**
     * A parser for class selectors.
     */
    private static final ObjectParser CLASS_PARSER = new ClassSelectorParser();

    /**
     * A parser for element selectors.
     */
    private static final ObjectParser ELEMENT_PARSER =
            new ElementSelectorParser();

    /**
     * A parser for ID selectors.
     */
    private static final ObjectParser ID_PARSER = new IdSelectorParser();

    /**
     * A parser for pseudo class selectors.
     */
    protected static final ObjectParser PSEUDO_CLASS_PARSER =
            new PseudoClassSelectorParser();

    /**
     * Parser for pseudo-element selectors.
     */
    private static final ObjectParser PSEUDO_ELEMENT_PARSER =
            new PseudoElementSelectorParser();

    // Javadoc inherited
    public void objectToBuffer(StringBuffer output,
            SelectorSequence sequence) {

        List selectors = sequence.getSelectors();

        if (selectors != null && selectors.size() > 0) {
            Iterator it = selectors.iterator();
            while (it.hasNext()) {
                Selector selector = (Selector) it.next();
                ObjectParser parser = getParser(selector);
                output.append(parser.objectToText(selector));
            }
        }
    }

    // Javadoc inherited
    public Object textToObject(String text) {
        throw new UnsupportedOperationException();
    }

    // Javadoc inherited
    public String objectToText(Object object) {
        if (object instanceof SelectorSequence) {
            StringBuffer output = new StringBuffer();
            objectToBuffer(output, (SelectorSequence) object);
            return output.toString();
        } else {
            throw new IllegalArgumentException("Expected selector sequence");
        }
    }

    /**
     * Determine the appropriate parser to use to parse the supplied selector.
     * Will never return null, but will throw an IllegalArgumentException if
     * the supplied selector is not valid in a selector sequence.
     *
     * @param selector  for which to find a parser
     * @return ObjectParser to use to parse the selector
     * @throws IllegalArgumentException if the supplied selector is not valid
     * in a selector sequence
     */
    private ObjectParser getParser(Selector selector) {
        ObjectParser parser = null;
        if (selector instanceof ElementSelector) {
            parser = ELEMENT_PARSER;
        } else if (selector instanceof ClassSelector) {
            parser = CLASS_PARSER;
        } else if (selector instanceof IdSelector) {
            parser = ID_PARSER;
        } else if (selector instanceof AttributeSelector) {
            parser = ATTRIBUTE_PARSER;
        } else if (selector instanceof PseudoClassSelector) {
            parser = PSEUDO_CLASS_PARSER;
        } else if (selector instanceof PseudoElementSelector) {
            parser = PSEUDO_ELEMENT_PARSER;
        } else if (selector instanceof InlineStyleSelector) {
            parser = new ObjectParser() {
                public String objectToText(Object object) {
                    InlineStyleSelector selector = (InlineStyleSelector) object;

                    return "%" + selector.getElementId();
                }

                public Object textToObject(String text) {
                    return new UnsupportedOperationException();
                }
            };
        } else {
           throw new IllegalArgumentException("No ObjectParser specified " +
                   "for the selector sequence type: " + selector.getClass());
        }
        return parser;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 12-Dec-05	10374/1	emma	VBM:2005111705 Interim commit

 01-Nov-05	9961/1	pduffin	VBM:2005101811 Committing restructuring

 ===========================================================================
*/
