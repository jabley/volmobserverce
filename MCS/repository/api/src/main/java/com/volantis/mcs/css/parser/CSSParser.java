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

import com.volantis.mcs.themes.Selector;
import com.volantis.mcs.themes.StyleSheet;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.PropertyValue;
import com.volantis.mcs.themes.MutableStyleProperties;
import com.volantis.styling.properties.StyleProperty;

import java.io.Reader;
import java.util.List;

/**
 * Creates object representations of various parts of a CSS document. 
 */
public interface CSSParser {

    /**
     * Parse the CSS specified in the source and return the constructed style
     * sheet.
     *
     * @param reader The CSS.
     * @param url    The location of the CSS, may be null.
     * @return The style sheet representing the CSS.
     */
    StyleSheet parseStyleSheet(Reader reader, String url);

    /**
     * Parse the value specified for a style attribute and return a constructed
     * style sheet.
     *
     * The grammer currently meets three of the four specified values of the
     * specification
     * <a href="http://www.w3.org/TR/2002/WD-css-style-attr-20020515">
     * Syntax of CSS Rules in HTML's "style" attribute</a>.
     * These values are declarations, declaration blocks, and inline rulesets.
     *
     * @param styleAttribute The style attribute value
     * @param url The base location of the page where the element containing the
     * attribute is specified
     * @param inlineStyleSelector The selector to be applied to rules, marking
     * them as style attribute styles.
     * @return The style sheet representing the style attribute value.
     */
    StyleSheet parseInlineStyleAttribute(String styleAttribute, String url, 
                                         Selector inlineStyleSelector);

    /**
     * Parse a block of CSS declarations, i.e. the contents of the { ... } block
     * for a rule.
     *
     * @param css The declaration.
     * @return The set of properties.
     */
    MutableStyleProperties parseDeclarations(String css);

    /**
     * Parse a group (comma separated) of selectors.
     *
     * @param css The CSS representation of the selectors.
     * @return The list of selectors.
     */
    List parseSelectorGroup(String css);

    /**
     * Parse a single selector (combined, sequence count as one).
     *
     * @param css The CSS representation of the selectors.
     * @return The selector.
     */
    Selector parseSelector(String css);

    /**
     * Parse a value for a single (not shorthand) property.
     *
     * @param property The property for which the CSS is being parsed.
     * @param css      The CSS to parser.
     * @return The value for the property.
     */
    StyleValue parseStyleValue(StyleProperty property, String css);

    /**
     * Parse a value for a single (not shorthand) property.
     *
     * @param property The property for which the CSS is being parsed.
     * @param css      The CSS to parser.
     * @return The value for the property.
     */
    PropertyValue parsePropertyValue(StyleProperty property, String css);
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
