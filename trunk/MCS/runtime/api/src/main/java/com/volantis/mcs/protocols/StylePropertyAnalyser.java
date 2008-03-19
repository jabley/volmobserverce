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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols;

import com.volantis.mcs.dom.WalkingDOMVisitorStub;
import com.volantis.mcs.dom.DOMWalker;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.Node;
import com.volantis.mcs.dom.Text;
import com.volantis.mcs.dom.WalkingDOMVisitor;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.themes.StyleKeyword;
import com.volantis.mcs.themes.StyleLength;
import com.volantis.mcs.themes.StylePercentage;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.StyleValueFactory;
import com.volantis.mcs.themes.properties.BackgroundColorKeywords;
import com.volantis.mcs.themes.properties.BackgroundImageKeywords;
import com.volantis.mcs.themes.properties.BorderStyleKeywords;
import com.volantis.mcs.themes.properties.DisplayKeywords;
import com.volantis.mcs.themes.properties.HeightKeywords;
import com.volantis.mcs.themes.properties.TextAlignKeywords;
import com.volantis.mcs.themes.properties.VerticalAlignKeywords;
import com.volantis.mcs.themes.properties.WhiteSpaceKeywords;
import com.volantis.mcs.themes.properties.WidthKeywords;
import com.volantis.mcs.protocols.html.HTMLConstants;
import com.volantis.styling.Styles;
import com.volantis.styling.properties.StyleProperty;
import com.volantis.styling.values.PropertyValues;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Helper class to determine if a visually important style property has visual
 * effect for a given element (analysing its environment).
 *
 * <strong>Note: This class can only be used for style properties that have
 * analysers assigned to them. It is the responsibility of the developer to add
 * more analysers if a new style property is considered to be important.</strong>
 */
public class StylePropertyAnalyser {
    /**
     * Used for logging
     */
    private static final LogDispatcher LOGGER =
            LocalizationFactory.createLogger(StylePropertyAnalyser.class);

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer EXCEPTION_LOCALIZER =
        LocalizationFactory.createExceptionLocalizer(StylePropertyAnalyser.class);

    private static final StyleValueFactory STYLE_VALUE_FACTORY =
        StyleValueFactory.getDefaultInstance();

    /**
     * Map from style properties to the analysers to be used for them
     */
    private static final Map ANALYSERS;

    /**
     * Singleton instance.
     */
    private static final StylePropertyAnalyser INSTANCE =
        new StylePropertyAnalyser();

    /**
     * Display style checking part of the margin analysers
     */
    private static final Analyser MARGIN_DISPLAY_STYLE_ANALYSER =
        new DisplayStyleDependentAnalyser(new StyleKeyword[] {
            DisplayKeywords.TABLE_ROW_GROUP,
            DisplayKeywords.TABLE_HEADER_GROUP,
            DisplayKeywords.TABLE_FOOTER_GROUP,
            DisplayKeywords.TABLE_ROW,
            DisplayKeywords.TABLE_COLUMN_GROUP,
            DisplayKeywords.TABLE_COLUMN,
            DisplayKeywords.TABLE_CELL
        }, false);

    /**
     * Display style checking part of the padding analysers
     */
    private static final Analyser PADDING_DISPLAY_STYLE_ANALYSER =
        new DisplayStyleDependentAnalyser(new StyleKeyword[] {
            DisplayKeywords.TABLE_ROW_GROUP,
            DisplayKeywords.TABLE_HEADER_GROUP,
            DisplayKeywords.TABLE_FOOTER_GROUP,
            DisplayKeywords.TABLE_ROW,
            DisplayKeywords.TABLE_COLUMN_GROUP,
            DisplayKeywords.TABLE_COLUMN
        }, false);

    static {
        ANALYSERS = new HashMap();
        ANALYSERS.put(StylePropertyDetails.BACKGROUND_COLOR,
            new BackgroundColorAnalyser());
        // background image is not none (and not null)
        ANALYSERS.put(StylePropertyDetails.BACKGROUND_IMAGE,
            new ExcludedValueAnalyser(StylePropertyDetails.BACKGROUND_IMAGE,
                BackgroundImageKeywords.NONE, true));
        ANALYSERS.put(StylePropertyDetails.BORDER_BOTTOM_WIDTH,
            new BorderWidthAnalyser(StylePropertyDetails.BORDER_BOTTOM_WIDTH,
                StylePropertyDetails.BORDER_BOTTOM_STYLE));
        ANALYSERS.put(StylePropertyDetails.BORDER_LEFT_WIDTH,
            new BorderWidthAnalyser(StylePropertyDetails.BORDER_LEFT_WIDTH,
                StylePropertyDetails.BORDER_LEFT_STYLE));
        ANALYSERS.put(StylePropertyDetails.BORDER_RIGHT_WIDTH,
            new BorderWidthAnalyser(StylePropertyDetails.BORDER_RIGHT_WIDTH,
                StylePropertyDetails.BORDER_RIGHT_STYLE));
        ANALYSERS.put(StylePropertyDetails.BORDER_TOP_WIDTH,
            new BorderWidthAnalyser(StylePropertyDetails.BORDER_TOP_WIDTH,
                StylePropertyDetails.BORDER_TOP_STYLE));
        // height is not 100% and height is not auto and height is not null and
        // display is neither 'table-column' nor 'table-column-group'
        ANALYSERS.put(StylePropertyDetails.HEIGHT,
            new CompoundAndAnalyser(
                new CompoundAndAnalyser(
                    new DisplayStyleDependentAnalyser(new StyleKeyword[]{
                        DisplayKeywords.TABLE_COLUMN,
                        DisplayKeywords.TABLE_COLUMN_GROUP}, false),
                    new ExcludedValueAnalyser(StylePropertyDetails.HEIGHT,
                        HeightKeywords.AUTO, true)),
                new ExcludedValueAnalyser(StylePropertyDetails.HEIGHT,
                    STYLE_VALUE_FACTORY.getPercentage(null, 100), false)));
        // display style is not 'table-row-group', 'table-header-group',
        // 'table-footer-group', 'table-row', 'table-column-group',
        // 'table-column', 'table-cell' and value is not 0 (in any unit)
        ANALYSERS.put(StylePropertyDetails.MARGIN_BOTTOM,
            new CompoundAndAnalyser(
                MARGIN_DISPLAY_STYLE_ANALYSER,
                new NonZeroValueAnalyser(StylePropertyDetails.MARGIN_BOTTOM)));
        ANALYSERS.put(StylePropertyDetails.MARGIN_LEFT,
            new CompoundAndAnalyser(
                MARGIN_DISPLAY_STYLE_ANALYSER,
                new NonZeroValueAnalyser(StylePropertyDetails.MARGIN_LEFT)));
        ANALYSERS.put(StylePropertyDetails.MARGIN_RIGHT,
            new CompoundAndAnalyser(
                MARGIN_DISPLAY_STYLE_ANALYSER,
                new NonZeroValueAnalyser(StylePropertyDetails.MARGIN_RIGHT)));
        ANALYSERS.put(StylePropertyDetails.MARGIN_TOP,
            new CompoundAndAnalyser(
                MARGIN_DISPLAY_STYLE_ANALYSER,
                new NonZeroValueAnalyser(StylePropertyDetails.MARGIN_TOP)));
        // display style is not 'table-row-group', 'table-header-group',
        // 'table-footer-group', 'table-row', 'table-column-group',
        // 'table-column' and value is not 0 (in any unit)
        ANALYSERS.put(StylePropertyDetails.PADDING_BOTTOM,
            new CompoundAndAnalyser(
                PADDING_DISPLAY_STYLE_ANALYSER,
                new NonZeroValueAnalyser(StylePropertyDetails.PADDING_BOTTOM)));
        ANALYSERS.put(StylePropertyDetails.PADDING_LEFT,
            new CompoundAndAnalyser(
                PADDING_DISPLAY_STYLE_ANALYSER,
                new NonZeroValueAnalyser(StylePropertyDetails.PADDING_LEFT)));
        ANALYSERS.put(StylePropertyDetails.PADDING_RIGHT,
            new CompoundAndAnalyser(
                PADDING_DISPLAY_STYLE_ANALYSER,
                new NonZeroValueAnalyser(StylePropertyDetails.PADDING_RIGHT)));
        ANALYSERS.put(StylePropertyDetails.PADDING_TOP,
            new CompoundAndAnalyser(
                PADDING_DISPLAY_STYLE_ANALYSER,
                new NonZeroValueAnalyser(StylePropertyDetails.PADDING_TOP)));
        ANALYSERS.put(StylePropertyDetails.TEXT_ALIGN,
            new TextAlignAnalyser());
        // display style is 'inline', 'inline-table', 'run-in' or 'table-cell'
        // and vertical align style property value is not 'baseline'
        ANALYSERS.put(StylePropertyDetails.VERTICAL_ALIGN,
            new CompoundAndAnalyser(
                new DisplayStyleDependentAnalyser(new StyleKeyword[]{
                    DisplayKeywords.INLINE, DisplayKeywords.INLINE_TABLE,
                    DisplayKeywords.RUN_IN, DisplayKeywords.TABLE_CELL}, true),
                new ExcludedValueAnalyser(StylePropertyDetails.VERTICAL_ALIGN,
                    VerticalAlignKeywords.BASELINE, true)));
        ANALYSERS.put(StylePropertyDetails.WHITE_SPACE,
            new WhitespaceAnalyser());
        ANALYSERS.put(StylePropertyDetails.WIDTH,
            new WidthAnalyser());
        ANALYSERS.put(StylePropertyDetails.COLOR, new ColorAnalyser());
    }

    /**
     * Returns the singleton instance.
     *
     * @return the singleton instance
     */
    public static StylePropertyAnalyser getInstance() {
        return INSTANCE;
    }

    /**
     * Returns true if the given collection of important properties contains at
     * least one style property that has visual effect on the specified element.
     *
     * <p>The important style properties in the specified collection must have
     * registered analysers in the underlying map of analysers.</p>
     *
     * @param properties the properties to check
     * @param element the element to perform the checks on
     * @return true if at least one of the specified style properties has
     * visual effect on the given element
     * @throws IllegalArgumentException if the specified style properties
     * collection contains a style property that doesn't have an associated
     * analyser
     */
    public boolean hasVisuallyImportantProperty(final Collection properties,
                                                final Element element) {
        for (Iterator iter = properties.iterator(); iter.hasNext(); ) {
            final StyleProperty property = (StyleProperty) iter.next();
            final Analyser analyser = (Analyser) ANALYSERS.get(property);
            if (analyser == null) {
                throw new IllegalArgumentException(EXCEPTION_LOCALIZER.format(
                    "unknown-style-property", property.getName()));
            }
            if (analyser.hasVisualEffect(element)) {
                return true;
            }
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(element.getName() + " doesn't have visually " +
                "important style properties.");
        }
        return false;
    }

    /**
     * Returns the first non-null (direct/indirect) parent of the specified
     * element or null, if no such parent can be found.
     *
     * @param element the element whose parent to be returned
     * @return the parent or null
     */
    private static Element getParent(final Element element) {
        Element parent = element.getParent();
        while (parent != null && parent.getName() == null) {
            parent = parent.getParent();
        }
        return parent;
    }

    /**
     * Base interface for all of the analysers.
     *
     * <p>The implementations are responsible to decide, if a specific style
     * property has visual effect on a given element.</p>
     */
    private interface Analyser {
        /**
         * Returns true if the style property associated to the implemented
         * analyser has visual effect on the specified element.
         *
         * @param element the element to check
         * @return true if the style property is
         */
        boolean hasVisualEffect(Element element);
    }

    /**
     * Analyser for background color style property.
     *
     * <p>The background color style property has visual effect if
     * <ul>
     * <li>style value is not null and</li>
     * <li>style value is not transparent and</li>
     * <li>style value is not the same as the parent value or if the same, the
     * parent has background image.</li>
     * </ul>
     * </p>
     */
    private static class BackgroundColorAnalyser implements Analyser{
        // javadoc inherited
        public boolean hasVisualEffect(final Element element) {
            final PropertyValues propertyValues =
                element.getStyles().getPropertyValues();
            boolean result = true;
            final StyleValue bgColorValue = propertyValues.getComputedValue(
                StylePropertyDetails.BACKGROUND_COLOR);
            if (bgColorValue == null ||
                    BackgroundColorKeywords.TRANSPARENT.equals(bgColorValue)) {
                // value is 'transparent' or missing
                result = false;
            } else {
                final Element parent = getParent(element);
                if (parent != null) {
                    final Styles parentStyles = parent.getStyles();
                    if (parentStyles != null) {
                        final PropertyValues parentValues =
                            parentStyles.getPropertyValues();
                        // value is the same as the parent...
                        if (bgColorValue.equals(parentValues.getComputedValue(
                                StylePropertyDetails.BACKGROUND_COLOR))) {
                            // ...and the parent doesn't have a background image
                            final StyleValue bgImageValue =
                                parentValues.getComputedValue(
                                    StylePropertyDetails.BACKGROUND_IMAGE);
                            result = bgImageValue != null &&
                                !bgImageValue.equals(
                                    BackgroundImageKeywords.NONE);
                        }
                    }
                }
            }
            return result;
        }
    }

    /**
     * Analyser for border width properties.
     *
     * <p>A border width style property (bottom, left, right, top) has visual
     * effect, if
     * <ul>
     * <li>the border style style property for the same side is not none and</li>
     * <li>the value is not specified as a length value or the number part of
     * the length value is 0 (in any kind of unit).</li>
     * </ul>
     * </p>
     */
    private static class BorderWidthAnalyser implements Analyser{
        private final StyleProperty widthProperty;
        private final StyleProperty styleProperty;

        public BorderWidthAnalyser(final StyleProperty widthProperty,
                                   final StyleProperty styleProperty) {
            this.widthProperty = widthProperty;
            this.styleProperty = styleProperty;
        }

        // javadoc inherited
        public boolean hasVisualEffect(final Element element) {
            final PropertyValues propertyValues =
                element.getStyles().getPropertyValues();
            final StyleValue borderWidthValue =
                propertyValues.getComputedValue(widthProperty);
            boolean result = true;
            if (borderWidthValue != null) {
                if (borderWidthValue instanceof StyleLength) {
                    final StyleLength length = (StyleLength) borderWidthValue;
                    if (length.getNumber() == 0.0) {
                        result = false;
                    }
                }
            }
            if (result) {
                final StyleValue borderStyleValue =
                    propertyValues.getComputedValue(styleProperty);
                if (borderStyleValue == null ||
                        borderStyleValue.equals(BorderStyleKeywords.NONE)) {
                    result = false;
                }
            }
            return result;
        }
    }

    /**
     * Analyser that compares the color style value. A color style property has
     * a visual effect if:
     *
     * <ol>
     * <li>The color is different to the parent; i.e. it can't inherit from the
     * parent</li>
     * AND
     * <li>The element contains child Nodes which may be affected by a color
     * style property, such as Text Nodes.</li>
     * </ol>
     *
     * See also the tests for this, which talk about the possible states.
     */
    private static class ColorAnalyser implements Analyser {

        // javadoc inherited
        public boolean hasVisualEffect(Element element) {
            boolean result;

            // From the STM defined in the TestCase, it can be seen that a
            // limiting factor in returning true is whether the context element
            // has any child (Text) nodes. There is scope for playing around
            // with the order that checks are made in this method; currently
            // the test for whether there are Text child nodes is done last,
            // since this is expected to be a more expensive operation than

            // looking at parent Element and calculating property values.
            // THIS ASSUMPTION HAS NOT BEEN VERIFIED BY MEASURING AGAINST A
            // SUITABLE CORPUS OF TEST DOCUMENTS AND MAY BE WRONG. The methods
            // are all intentionally small, both from a readability
            // perspective, and to allow re-ordering for a more performant
            // implementation if the current one proves expensive.

            final PropertyValues propertyValues =
                element.getStyles().getPropertyValues();
            final StyleValue colorValue = propertyValues.getComputedValue(
                StylePropertyDetails.COLOR);

            final Element parent = getParent(element);

            if (colorIsInheritableFromParent(colorValue, parent)) {
                result = false;
            } else {
                result = isValidColorValue(colorValue)
                        && hasChildNodesAffectedByColor(element);
            }

            return result;
        }

        /**
         * Return <code>true</code> if the color style can be inherited from
         * the parent, otherwise return <code>false</code>.
         *
         * @param colorValue the effective color value of the context
         *                   <code>Element</code>.
         * @param parent     the parent <code>Element</code>. May be null.
         * @return true or false
         */
        private boolean colorIsInheritableFromParent(StyleValue colorValue,
                                                     Element parent) {

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Testing colorIsInheritableFromParent: " +
                        "colorValue=" + colorValue + ", parent=" + parent);
            }

            // guard conditions - order of these is important in current
            // implementation.
            if (hasNoEffect(parent)) {

                // No parent to inherit from
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("No parent - returning false.");
                }
                return false;
            }

            if (!isValidColorValue(colorValue)) {

                // The context element does not override the parent, so just
                // take what the parent has already.
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("No override by context element " +
                            "- returning true.");
                }
                return true;
            }

            // We need to compare the parent and context color values.
            final PropertyValues parentPropertyValues =
                    parent.getStyles().getPropertyValues();

            final StyleValue parentColorValue =
                    parentPropertyValues.getComputedValue(
                            StylePropertyDetails.COLOR);

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("parent colorValue=" + parentColorValue);
            }

            // @todo StyleColorName always returns false, which may be a false
            // positive (sic). The other StyleColor subclasses have more
            // 'reasonable' equals implementations.
            return colorValue.equals(parentColorValue);
        }

        /**
         * Return true if the parent can affect the context element, otherwise
         * false. This is a Composed Method to show whether the specified
         * parent element is important.
         *
         * Equivalent to the test <code>null == parent</code>.
         *
         * @param parent the parent element obtained from
         *                {@link StylePropertyAnalyser#getParent(Element)}
         * @return true if the parent has no effect on the context element,
         *         otherwise false.
         */
        private boolean hasNoEffect(Element parent) {
            return null == parent;
        }

        /**
         * Return true if the colorValue is a StyleValue that will have an
         * effect, otherwise false. This is a Composed Method to show whether
         * the specified StyleValue is important.
         *
         * Equivalent to the test <code>null != colorValue</code>.
         *
         * @param colorValue
         * @return true if the StyleValue has a visible effect.
         */
        private boolean isValidColorValue(StyleValue colorValue) {
            return null != colorValue;
        }

        /**
         * Return <code>true</code> if the Element contains children that are
         * affected by setting the color style property on the Element.
         *
         * Currently, this is restricted to just checking for the presence of
         * <code>Text</code> <code>Node</code>s. Potentially, this should be
         * expanded in the future.
         *
         * @param element    the context <code>Element</code>.
         * @return true if the <code>colorValue</code> will have a visual
         *              effect on the child <code>Node</code>s, otherwise
         *              false.
         */
        private boolean hasChildNodesAffectedByColor(Element element) {

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Checked hasChildNodesAffectedByColor: " +
                        "element=" + element);
            }

            // Illustration of local class to collect results from anonymous
            // inner classes.

//            class BooleanResultCollector {
//                // public mutable field
//                boolean result = false;
//            }
//
//            final BooleanResultCollector result = new BooleanResultCollector();
//
//            ...

            // Horrible Java hack to work around getting results back from
            // anonymous inner classes. Another way would be to declare a local
            // class, or more usefully, a utility class that contains a mutable
            // field, but with the class reference declared final, as
            // illustrated above.

            final boolean [] result = new boolean[] { false };

            WalkingDOMVisitor visitor = new WalkingDOMVisitorStub() {

                // Javadoc inherited.
                public void visit(Text text) {
                    // Would be nice to stop visiting all the other nodes in
                    // the hierarchy, but we can't.

                    result[0] = true;
                }
            };

            new DOMWalker(visitor) {

                // javadoc inherited
                protected void assertElementProperties(Element element) {
                    // no-op

                    // This allows Elements with name == null to be processed
                    // too.
                }

            }.walk(element);

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Exiting hasChildNodesAffectedByColor: "
                        + result[0]);
            }

            return result[0];
        }
    }

    /**
     * Analyser that returns true if the display style value is from a given
     * set of keyword values.
     */
    private static class DisplayStyleDependentAnalyser
            implements Analyser {

        /**
         * Display keywords to check.
         */
        private final StyleKeyword[] keywords;
        /**
         * If true, {@link #hasVisualEffect(com.volantis.mcs.dom.Element)}
         * returns true if the display style value of the specified element is
         * among the stored keywords.
         * If false, {@link #hasVisualEffect(com.volantis.mcs.dom.Element)}
         * returns true if the display style value of the specified element is
         * NOT among the stored keywords.
         */
        private final boolean include;

        /**
         * @param keywords the array of keywords to check.
         * @param include true if the style value should be among the keywords,
         * false otherwise.
         */
        private DisplayStyleDependentAnalyser(final StyleKeyword[] keywords,
                                              final boolean include) {
            this.keywords = keywords;
            this.include = include;
        }

        // javadoc inherited
        public boolean hasVisualEffect(final Element element) {
            final PropertyValues propertyValues =
                element.getStyles().getPropertyValues();
            final StyleValue styleValue =
                propertyValues.getComputedValue(StylePropertyDetails.DISPLAY);
            boolean found = false;
            for (int i = 0; i < keywords.length && !found; i++) {
                found = keywords[i].equals(styleValue);
            }
            return include && found || !include && !found;
        }
    }

    /**
     * Compound analyser that takes two analysers and only returns true if both
     * of the analysers returned true.
     */
    private static class CompoundAndAnalyser implements Analyser {
        private final Analyser left;
        private final Analyser right;

        public CompoundAndAnalyser(final Analyser left, final Analyser right) {
            this.left = left;
            this.right = right;
        }

        // javadoc inherited
        public boolean hasVisualEffect(final Element element) {
            final boolean leftValue = left.hasVisualEffect(element);
            return leftValue && right.hasVisualEffect(element);
        }
    }

    /**
     * Analyser that returns true, if the style value is NOT equal to the
     * stored value.
     *
     * <p>The analyser can be configured to accept null values or reject them.
     * </p>
     */
    private static class ExcludedValueAnalyser implements Analyser {
        /**
         * The style property whose value to check.
         */
        private final StyleProperty property;
        /**
         * The value to check against.
         */
        private final StyleValue value;
        /**
         * If true, null style values on the element are considered to be the
         * same as the excluded value.
         */
        private final boolean nullIsSame;

        public ExcludedValueAnalyser(final StyleProperty property,
                                     final StyleValue value,
                                     final boolean nullIsSame) {
            this.property = property;
            this.value = value;
            this.nullIsSame = nullIsSame;
        }

        // javadoc inherited
        public boolean hasVisualEffect(final Element element) {
            final PropertyValues propertyValues =
                element.getStyles().getPropertyValues();
            final StyleValue value = propertyValues.getComputedValue(property);
            boolean same = value == null && nullIsSame ||
                value != null && value.equals(this.value);
            return !same;
        }
    }

    /**
     * Analyzer that only accepts length or percentage style values, if the
     * number part is not null.
     */
    private static class NonZeroValueAnalyser implements Analyser {
        private final StyleProperty property;

        public NonZeroValueAnalyser(final StyleProperty property) {
            this.property = property;
        }

        // javadoc inherited
        public boolean hasVisualEffect(final Element element) {
            final PropertyValues propertyValues =
                element.getStyles().getPropertyValues();
            final StyleValue value = propertyValues.getComputedValue(property);
            boolean result = true;
            if (value != null) {
                if (value instanceof StyleLength) {
                    final StyleLength length = (StyleLength) value;
                    if (length.getNumber() == 0.0) {
                        result = false;
                    }
                } else if (value instanceof StylePercentage) {
                    final StylePercentage percentage = (StylePercentage) value;
                    if (percentage.getPercentage() == 0.0) {
                        result = false;
                    }
                }
            }
            return result;
        }
    }

    /**
     * Analyser for the text align style property.
     *
     * <p>A text align style property is visually important if
     * <ul>
     * <li>the value is not null and</li>
     * <li>the display style value is 'block', 'list-item', 'run-in', 'table' or
     * 'table-cell' and</li>
     * <li>the value is different from the parent value and</li>
     * <li>the element has at least one text node or an element child node that
     * doesn't have a value for this property.</li>
     * </ul>
     * </p>
     */
    private static class TextAlignAnalyser implements Analyser {
        // javadoc inherited
        public boolean hasVisualEffect(final Element element) {
            final PropertyValues propertyValues =
                element.getStyles().getPropertyValues();
            boolean important = true;
            final StyleValue textAlignValue =
                propertyValues.getComputedValue(
                    StylePropertyDetails.TEXT_ALIGN);
            if (textAlignValue == null ||
                    textAlignValue == TextAlignKeywords._INTERNAL_DEFERRED_INHERIT) {
                important = false;
            }
            if (important) {
                final StyleValue displayValue = propertyValues.getComputedValue(
                    StylePropertyDetails.DISPLAY);
                if ((displayValue == null ||
                        !displayValue.equals(DisplayKeywords.BLOCK) &&
                        !displayValue.equals(DisplayKeywords.LIST_ITEM) &&
                        !displayValue.equals(DisplayKeywords.RUN_IN) &&
                        !displayValue.equals(DisplayKeywords.TABLE) &&
                        !displayValue.equals(DisplayKeywords.TABLE_CELL))) {
                    important = false;
                }
            }
            if (important) {
                final Element parent = getParent(element);
                if (parent != null) {
                    final Styles parentStyles = parent.getStyles();
                    if (parentStyles != null) {
                        final StyleValue parentValue =
                            parentStyles.getPropertyValues().getComputedValue(
                                StylePropertyDetails.TEXT_ALIGN);
                        if (textAlignValue.equals(parentValue)) {
                            important = false;
                        }
                    }
                }
            }

            if (important) {
                important = false;
                for (Node child = element.getHead();
                     child != null && !important;
                     child = child.getNext()) {
                    if (child instanceof Text) {
                        final Text text = (Text) child;
                        important = text.getLength() > 0;
                    } else if (child instanceof Element){
                        final Element childElement = (Element)child;
                        if(isInlineElement(childElement.getName())){
                            // inline element can not have text-align defined
                            // so parent can not be removed because we property value
                            // will be lost
                            important = true;
                        } else {
                            final Styles styles = childElement.getStyles();
                            StyleValue childValue = null;
                            if(styles != null && styles.getPropertyValues() != null){
                                childValue = childElement.getStyles().getPropertyValues().
                                    getSpecifiedValue(StylePropertyDetails.TEXT_ALIGN);
                            }
                            // if child is specified then from this child poind of view
                            // parent is not important
                            // if for all - then not important at all
                            important = (childValue != null) ? false  : important;

                        }
                    }
                }
            }
            return important;
        }
    }

    /**
     * check if element with specified element name is inline element
     * if so return true otherwise false
     *
     * @param tagName
     * @return true if element is inline false otherwise
     * @return
     */
    private static boolean isInlineElement(String tagName) {
        for (int i = 0; i < HTMLConstants.INLINE_ELEMENTS.length; i++) {
            if (HTMLConstants.INLINE_ELEMENTS[i].equalsIgnoreCase(tagName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Analyser for the whitespace style property.
     *
     * <p>A whitespace style property is visually important if
     * <ul>
     * <li>the value is not null and</li>
     * <li>the value is different from the parent value or the element doesn't
     * have a parent element and the value is the default value (normal) and</li>
     * <li>the element has at least one text node or an element child node that
     * doesn't have a value for this property.</li>
     * </ul>
     * </p>
     */
    private static class WhitespaceAnalyser implements Analyser {
        // javadoc inherited
        public boolean hasVisualEffect(final Element element) {
            final PropertyValues propertyValues =
                element.getStyles().getPropertyValues();
            boolean important = true;
            final StyleValue whitespaceValue = propertyValues.getComputedValue(
                StylePropertyDetails.WHITE_SPACE);
            // if no value is set, it is not important
            if (whitespaceValue == null) {
                important = false;
            }
            // if it is the same as the parent value, then it is not important,
            // as it is an inherited property
            if (important) {
                final Element parent = getParent(element);
                if (parent != null) {
                    final Styles parentStyles = parent.getStyles();
                    if (parentStyles != null) {
                        final StyleValue parentValue =
                            parentStyles.getPropertyValues().getComputedValue(
                                StylePropertyDetails.WHITE_SPACE);
                        if (whitespaceValue.equals(parentValue)) {
                            important = false;
                        }
                    }
                } else {
                    important =
                        !whitespaceValue.equals(WhiteSpaceKeywords.NORMAL);
                }
            }
            // check if there is an element we can apply this on
            if (important) {
                important = false;
                for (Node child = element.getHead();
                     child != null && !important;
                     child = child.getNext()) {

                    if (child instanceof Text) {
                        final Text text = (Text) child;
                        important = text.getLength() > 0;
                    } else {
                        final Element childElement = (Element) child;
                        final Styles childStyles = childElement.getStyles();
                        if (childStyles != null) {
                            final PropertyValues childValues =
                                childStyles.getPropertyValues();
                            final StyleValue childValue =
                                childValues.getComputedValue(
                                    StylePropertyDetails.WHITE_SPACE);
                            important = childValue == null;
                        } else {
                            important = true;
                        }
                    }
                }
            }
            return important;
        }
    }

    /**
     * Analyser for the width style property.
     *
     * <p>The width style property has visual effect if
     * <ul>
     * <li>value is not 100% and</li>
     * <li>value is not auto or missing, if the display style is 'block'.
     * </ul>
     * </p>
     */
    private static class WidthAnalyser implements Analyser {
        // javadoc inherited
        public boolean hasVisualEffect(Element element) {
            final PropertyValues propertyValues =
                element.getStyles().getPropertyValues();
            final StyleValue value = propertyValues.getComputedValue(
                StylePropertyDetails.WIDTH);
            boolean result = true;
            if (value instanceof StylePercentage) {
                final StylePercentage percentage = (StylePercentage) value;
                if (percentage.getPercentage() == 100.0) {
                    result = false;
                }
            }
            if (result) {
                if (value == null || value.equals(WidthKeywords.AUTO)) {
                    final StyleValue displayValue =
                        propertyValues.getComputedValue(
                            StylePropertyDetails.WIDTH);
                    result = DisplayKeywords.BLOCK.equals(displayValue);
                }
            }
            return result;
        }
    }
}
