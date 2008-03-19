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
package com.volantis.mcs.themes;

import com.volantis.synergetics.factory.MetaDefaultFactory;

import java.util.List;

/**
 * A factory interface for creating {@link StyleSheet} objects.
 *
 * @mock.generate
 */
public abstract class StyleSheetFactory {


    /**
     * Set up the meta default factory instance.
     */
    private static final MetaDefaultFactory metaDefaultFactory =
            new MetaDefaultFactory(
        "com.volantis.mcs.themes.impl.DefaultStyleSheetFactory",
        StyleSheetFactory.class.getClassLoader());

    /**
     * Returns the default instance of this factory.
     *
     * @return the default instance of this factory
     */
    public static StyleSheetFactory getDefaultInstance() {
        return (StyleSheetFactory) metaDefaultFactory.getDefaultFactoryInstance();
    }

    /**
     * Create an empty style sheet object.
     *
     * @return the style sheet created.
     */
    public abstract StyleSheet createStyleSheet();

    /**
     * Create an empty CSS style sheet object.
     *
     * @return the style sheet created.
     */
    public abstract CSSStyleSheet createCSSStyleSheet();

    /**
     * Creates an attribute selector.
     *
     * @return An attribute selector
     */
    public abstract AttributeSelector createAttributeSelector();

    /**
     * Creates a class selector.
     *
     * @param cssClass The CSS class.
     *
     * @return A class selector
     */
    public abstract ClassSelector createClassSelector(String cssClass);

    /**
     * Creates a combined selector.
     *
     * @return A combined selector
     */
    public abstract CombinedSelector createCombinedSelector();

    /**
     * Creates a selector sequence.
     *
     * @return A selector sequence
     */
    public abstract SelectorSequence createSelectorSequence();

    /**
     * Creates a contextual selector sequence from a list of selectors.
     *
     * @param selectors The list of selectors.
     *
     * @return A contextual selector sequence
     */
    public abstract SelectorSequence createSelectorSequence(
            List selectors);

    /**
     * Creates an ID selector.
     *
     * @param id The identifier.
     *
     * @return an ID selector
     */
    public abstract IdSelector createIdSelector(String id);

    /**
     * Creates an InlineStyleSelector selector.
     *
     * @param elementId The identifier.
     *
     * @return an InlineStyleSelector
     */
    public abstract InlineStyleSelector createInlineStyleSelector(int elementId);

    /**
     * Creates a selector group.
     *
     * @return A selector group
     */
    public abstract SelectorGroup createSelectorGroup();

    /**
     * Creates a type selector.
     *
     * @return A type selector
     */
    public abstract TypeSelector createTypeSelector();

    /**
     * Creates a type selector.
     *
     * @param namespacePrefix The namespace prefix, may be null.
     * @param type            The element type.
     * @return A type selector
     */
    public abstract TypeSelector createTypeSelector(
            String namespacePrefix, String type);

    /**
     * Creates a universal selector.
     *
     * @return A universal selector
     */
    public abstract UniversalSelector createUniversalSelector();

    /**
     * Creates a universal selector.
     *
     * @param namespacePrefix The namespace prefix, may be null.
     *
     * @return A universal selector
     */
    public abstract UniversalSelector createUniversalSelector(
            String namespacePrefix);

    /**
     * Creates a pseudo class selector of a specified type.
     *
     * @param type The type of pseudo class selector to create
     *
     * @return A pseudo class selector
     */
    public abstract PseudoClassSelector createPseudoClassSelector(String type);

    /**
     * Creates an nth child selector.
     *
     * @return An nth child selector.
     */
    public abstract NthChildSelector createNthChildSelector(int a, int b);

    /**
     * Creates an nth child selector.
     *
     * @return An nth child selector.
     */
    public abstract NthChildSelector  createNthChildSelector(String expression);

    /**
     * Creates a pseudo element selector of a specified type.
     *
     * @param type The type of pseudo element selector to create
     *
     * @return A pseudo element selector
     */
    public abstract PseudoElementSelector createPseudoElementSelector(String type);

    /**
     * Creates an invalid selector.
     *
     * @param text The text that is invalid.
     *
     * @return An invalid selector.
     */
    public abstract InvalidSelector createInvalidSelector(String text);


    /**
     * Creates a style properties object.
     *
     * @return A style properties object
     */
    public abstract MutableStyleProperties createStyleProperties();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 12-Dec-05	10374/2	emma	VBM:2005111705 Interim commit

 29-Nov-05	10505/3	pduffin	VBM:2005111405 Committing transactions from MCS 3.5.0 (7)

 29-Nov-05	10347/2	pduffin	VBM:2005111405 Massive changes for performance

 31-Oct-05	9965/1	ianw	VBM:2005101811 Fixed up invalid selectors

 01-Nov-05	9888/1	pduffin	VBM:2005101811 Committing new user interface changes that have been ported forward from 3.5

 31-Oct-05	9965/1	ianw	VBM:2005101811 Fixed up invalid selectors

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 15-Jul-05	9067/3	geoff	VBM:2005071415 More refactoring for: XDIMECP: Generate optimised CSS for a DOM.

 ===========================================================================
*/
