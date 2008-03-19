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

package com.volantis.styling;

import com.volantis.mcs.css.parser.CSSParser;
import com.volantis.mcs.css.parser.CSSParserFactory;
import com.volantis.mcs.themes.AttributeSelector;
import com.volantis.mcs.themes.ClassSelector;
import com.volantis.mcs.themes.CombinedSelector;
import com.volantis.mcs.themes.IdSelector;
import com.volantis.mcs.themes.InlineStyleSelector;
import com.volantis.mcs.themes.InvalidSelector;
import com.volantis.mcs.themes.NthChildSelector;
import com.volantis.mcs.themes.PropertyValue;
import com.volantis.mcs.themes.PseudoClassSelector;
import com.volantis.mcs.themes.PseudoClassTypeEnum;
import com.volantis.mcs.themes.PseudoElementSelector;
import com.volantis.mcs.themes.PseudoElementTypeEnum;
import com.volantis.mcs.themes.Selector;
import com.volantis.mcs.themes.SelectorSequence;
import com.volantis.mcs.themes.SelectorVisitor;
import com.volantis.mcs.themes.StyleProperties;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleSheet;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.TypeSelector;
import com.volantis.mcs.themes.UniversalSelector;
import com.volantis.mcs.themes.Rule;
import com.volantis.shared.iteration.IterationAction;
import com.volantis.styling.impl.StylesImpl;
import com.volantis.styling.impl.engine.sheet.InitialPropertyValues;
import com.volantis.styling.properties.StyleProperty;
import com.volantis.styling.properties.StylePropertyIteratee;
import com.volantis.styling.values.MutablePropertyValues;

import java.io.StringReader;
import java.util.List;

public class StylesBuilder {

    private static final CSSParserFactory CSS_PARSER_FACTORY =
            CSSParserFactory.getDefaultInstance();

    private static PseudoStyleEntities pseudoStyleEntities =
            new PseudoStyleEntitiesImpl();
    private static final InitialPropertyValues INITIAL_VALUES =
            new InitialPropertyValues(StylePropertyDetails.getDefinitions());

    /**
     * Get the style value associated with a specific property.
     * @param property The property whose value is required.
     * @param css The css representation of the value.
     * @return The style value.
     */
    public static StyleValue getStyleValue(StyleProperty property, String css) {
        CSSParser parser = CSS_PARSER_FACTORY.createStrictParser();
        return parser.parseStyleValue(property, css);
    }

    /**
     * Get the style value associated with a specific property.
     * @param property The property whose value is required.
     * @param css The css representation of the value.
     * @return The style value.
     */
    public static PropertyValue getPropertyValue(StyleProperty property, String css) {
        CSSParser parser = CSS_PARSER_FACTORY.createStrictParser();
        return parser.parsePropertyValue(property, css);
    }
    
    /**
     * Build a styles with initial values.
     *
     * @return the styles object.
     */
    public static Styles getInitialValueStyles() {
        return new StylesImpl(null, null, INITIAL_VALUES);
    }

    /**
     * Build a totally empty styles.
     * <p>
     * @return The styles object.
     *
     * @deprecated {@link #getInitialValueStyles()} should be used since most
     * styles used in the real world will have at least default values.
     *
     */
    public static Styles getEmptyStyles() {

       return new StylesImpl();
    }

    /**
     * @deprecated all usages of this method should be changed to either use
     *      {@link #getInitialValueStyles()} or {@link #getEmptyStyles()}.
     */
    public static Styles getDeprecatedStyles() {

        return getEmptyStyles();
    }

    /**
     * @deprecated Use {@link #getCompleteStyles(String)}
     */
    public static Styles getStyles(String cssValues) {
        return getStyles(cssValues, false);
    }

    public static Styles getSparseStyles(String cssValues) {
        return getStyles(cssValues, true); 
    }

    /**
     * Build a styles object from CSS representation.
     *
     * <p>The CSS can be in either of two forms.</p>
     * <pre>property: value; ...</pre>
     * <p>or</p>
     * <pre>{property: value; ...}
     * pseudo class/element {property: value; ...}
     *   :
     * </pre>
     *
     * @param cssValues The CSS.
     * @param specified Treats the style values in the input CSS as having
     * been specified.
     *
     * @return The styles object.
     *
     * @deprecated Use {@link #getCompleteStyles(String)}
     */
    public static Styles getStyles(String cssValues, boolean specified) {

        CSSParser parser = CSS_PARSER_FACTORY.createStrictParser();

        // Create a new styled element.
        Styles styles = new StylesImpl();
        if (cssValues != null) {

            String selectorBlock = null;
            String pseudoSelectorBlock = null;
            // Determine if there are multiple selector rules to be
            // parsed and what type they are (i.e. pseudo or standard)
            if (cssValues.indexOf('{') == -1) {
                // No pseudoselector rules were specified
                selectorBlock = cssValues;
            } else if ('{' == cssValues.charAt(0)) {
                // Both standard and pseudoselector rules were specified
                int index = cssValues.indexOf("}");
                selectorBlock = cssValues.substring(1, index);
                pseudoSelectorBlock = cssValues.substring(index + 1);
            } else {
                // No standard selector rules were specified
                pseudoSelectorBlock = cssValues;
            }

            // Put the standard selector rules onto the styled element
            if (selectorBlock != null) {
                // Parse the values into a properties object.
                StyleProperties properties = parser.parseDeclarations(
                        selectorBlock);

                // Translate that to a property values object.
                setComputedValuesFromProperties(styles, properties, specified);
            }

            // Put the pseudoselector rules onto the styled element
            if (pseudoSelectorBlock != null) {
                parsePseudoStyleSheetIntoNestedStyles(styles, parser,
                        pseudoSelectorBlock, specified);
            }
        }
        return styles;
    }

    public static Styles getCompleteStyles(String css) {
        return getCompleteStyles(css, false);
            }

    public static Styles getCompleteStyles(String css, boolean specified) {
        Styles styles = getStyles(css, specified);
        final MutablePropertyValues propertyValues = styles.getPropertyValues();
        StylePropertyDetails.getDefinitions().iterateStyleProperties(new StylePropertyIteratee() {
            public IterationAction next(StyleProperty property) {
                StyleValue value = propertyValues.getComputedValue(property);
                if (value == null) {
                    propertyValues.setComputedValue(property,
                            property.getStandardDetails().getInitialValue());
                }
                return IterationAction.CONTINUE;
        }
        });

        return styles;
    }

    /**
     * Parse style sheet data used for pseudo classes and elements into the
     * nested styles of the styles object provided.
     *
     * @param styles the styles to populate
     * @param parser
     * @param styleSheetString
     * @param specified
     */
    private static void parsePseudoStyleSheetIntoNestedStyles(
            Styles styles,
            CSSParser parser,
            String styleSheetString, boolean specified) {

        // Parse the style sheet.

        final StyleSheet styleSheet = parser.parseStyleSheet(
                new StringReader(styleSheetString), null);

        Visitor visitor = new Visitor(specified);

        // Iterate over the rules.
        List rules = styleSheet.getRules();
        for (int i=0; i< rules.size(); i++) {
            // Extract each rule.
            Rule rule = (Rule) rules.get(i);

            Selector selector = (Selector) rule.getSelectors().get(0);
            visitor.addStyles(styles, selector, rule.getProperties());
        }
    }

    private static void setComputedValuesFromProperties(
            final Styles styles,
            final StyleProperties properties, final boolean specified) {

        final MutablePropertyValues values =
                styles.getPropertyValues();
        if (properties != null) {
            values.iterateStyleProperties(new StylePropertyIteratee() {
                public IterationAction next(StyleProperty property) {
                    StyleValue value = properties.getStyleValue(property);
                    values.setComputedValue(property, value);
                    if (specified) {
                        values.setSpecifiedValue(property, value);
                    }
                    return IterationAction.CONTINUE;
                }
            });
        }
    }

    private static class Visitor implements SelectorVisitor {

        private final boolean specified;

        private StatefulPseudoClassSet aggregateClassSet;

        private Styles styles;

        public Visitor(boolean specified) {
            this.specified = specified;
        }

        // Javadoc inherited.
        public void visit(AttributeSelector selector) {
            throw new IllegalStateException("Unexpected selector " + selector);
        }

        // Javadoc inherited.
        public void visit(ClassSelector selector) {
            throw new IllegalStateException("Unexpected selector " + selector);
        }

        // Javadoc inherited.
        public void visit(CombinedSelector selector) {
            throw new IllegalStateException("Unexpected selector " + selector);
        }

        // Javadoc inherited.
        public void visit(IdSelector selector) {
            throw new IllegalStateException("Unexpected selector " + selector);
        }

        // Javadoc inherited.
        public void visit(InvalidSelector selector) {
            throw new IllegalStateException("Unexpected selector " + selector);
        }

        //javadoc inherited
        public void visit(InlineStyleSelector selector) {
            throw new IllegalStateException("Unexpected selector " + selector);
        }

        // Javadoc inherited.
        public void visit(NthChildSelector selector) {
            throw new IllegalStateException("Unexpected selector " + selector);
        }

        // Javadoc inherited.
        public void visit(PseudoClassSelector selector) {
            PseudoClassTypeEnum type = selector.getPseudoClassType();
            StatefulPseudoClass pseudoClass = pseudoStyleEntities.
                    getStatefulPseudoClass(type.getType());
            if (pseudoClass == null) {
                throw new IllegalStateException(
                        "Unexpected pseudo class " + type);
            }

            if (aggregateClassSet == null) {
                aggregateClassSet = pseudoClass.getSet();
            } else {
                aggregateClassSet = aggregateClassSet.add(pseudoClass);
            }
        }

        // Javadoc inherited.
        public void visit(PseudoElementSelector selector) {
            PseudoElementTypeEnum type = selector.getPseudoElementType();
            PseudoElement pseudoElement = pseudoStyleEntities.
                    getPseudoElement(type.getType());
            if (pseudoElement == null) {
                throw new IllegalStateException(
                        "Unexpected pseudo element " + type);
            }

            styles = styles.getNestedStyles(pseudoElement);
        }

        // Javadoc inherited.
        public void visit(TypeSelector selector) {
            throw new IllegalStateException("Unexpected selector " + selector);
        }

        // Javadoc inherited.
        public void visit(UniversalSelector selector) {
        }

        // Javadoc inherited.
        public void visit(SelectorSequence sequence) {
            sequence.visitChildren(this);
        }

        public void addStyles(Styles styles, Selector selector,
                              StyleProperties properties) {
            this.styles = styles;
            this.aggregateClassSet = null;
            selector.accept(this);
            styles = this.styles;

            // If any pseudo classes were found then use them.
            if (aggregateClassSet != null) {
                styles = styles.getNestedStyles(aggregateClassSet);
            }

            setComputedValuesFromProperties(styles, properties, specified);
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 12-Dec-05	10374/2	emma	VBM:2005111705 Interim commit

 05-Dec-05	10527/3	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 05-Dec-05	10512/2	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 29-Nov-05	10505/11	pduffin	VBM:2005111405 Committing transactions from MCS 3.5.0 (7)

 29-Nov-05	10347/5	pduffin	VBM:2005111405 Massive changes for performance

 29-Nov-05	10505/7	pduffin	VBM:2005111405 Committing transactions from MCS 3.5.0 (6)

 21-Nov-05	10347/3	pduffin	VBM:2005111405 Cleaned up PropertyValues to remove synthesised properties and moved specified into an extended interface

 29-Nov-05	10505/3	pduffin	VBM:2005111405 Committing transactions from MCS 3.5.0 (3)

 18-Nov-05	10347/1	pduffin	VBM:2005111405 Removed some unnecessary usages of setSpecifiedValue

 28-Oct-05	9965/1	ianw	VBM:2005101811 stabalise InvalidSelector code

 01-Nov-05	9888/1	pduffin	VBM:2005101811 Committing new user interface changes that have been ported forward from 3.5

 28-Oct-05	9965/1	ianw	VBM:2005101811 stabalise InvalidSelector code

 27-Sep-05	9487/2	pduffin	VBM:2005091203 Committing new CSS Parser

 05-Sep-05	9407/4	pduffin	VBM:2005083007 Removed old themes model

 02-Sep-05	9407/2	pduffin	VBM:2005083007 Committing resolved conflicts

 31-Aug-05	9409/1	geoff	VBM:2005083007 Move over to using the new themes model.

 22-Aug-05	9298/2	geoff	VBM:2005080402 Style portlets and inclusions correctly.

 22-Aug-05	9331/1	gkoch	VBM:2005081603 InputStream -> Reader

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 ===========================================================================
*/
