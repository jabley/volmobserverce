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

package com.volantis.mcs.css.impl.parser.properties;

import com.volantis.mcs.css.impl.parser.CSSParserMessages;
import com.volantis.mcs.css.impl.parser.ParserContext;
import com.volantis.mcs.css.impl.parser.Separator;
import com.volantis.mcs.themes.Priority;
import com.volantis.mcs.themes.PropertyGroups;
import com.volantis.mcs.themes.PropertyValue;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.MutableStyleProperties;
import com.volantis.mcs.themes.ThemeFactory;
import com.volantis.mcs.themes.font.UnknownFontValue;
import com.volantis.styling.properties.MutableStylePropertySet;
import com.volantis.styling.properties.MutableStylePropertySetImpl;
import com.volantis.styling.properties.StyleProperty;

/**
 * Parser for the font shorthand property.
 *
 * <p>This is different to other shorthands in that it is not possible to
 * place the values in arbitrary order as there would be confusion as to which
 * value was for which property. Therefore, it has more structure and also
 * includes a special separator character. The following describes the syntax
 * of the property using the format in the CSS specification.</p>
 *
 * <pre>
 * [&lt;'font-style'> || &lt;'font-variant'> || &lt;'font-weight'>]?
 * [&lt;'font-size'> [ / &lt;'line-height'>]?]?
 * &lt;'font-family'>
 *
 * or
 *
 * &lt;'mcs-system-font'>
 * </pre>
 *
 * <p>This means that style, variant and weight have to occur at the start
 * but can occur in any order. They are then followed by an optional size
 * which if present may be followed by a / and the line-height. Finally, there
 * must be a font-family. Alternatively, it can specify an mcs-system-font.</p>
 */
public class FontParser
        extends AbstractPropertyParser {

    /**
     * The style, variant and weight properties.
     */
    private static final StyleProperty[] STYLE_VARIANT_WEIGHT_PROPERTIES =
            new StyleProperty[]{
                StylePropertyDetails.FONT_STYLE,
                StylePropertyDetails.FONT_VARIANT,
                StylePropertyDetails.FONT_WEIGHT,
            };

    /**
     * The handler for the mcs-system-font property.
     */
    private final ShorthandValueHandler systemFontConverter;

    /**
     * The handlers for the style, variant and weight properties.
     */
    private final ShorthandValueHandler[] styleVariantWeightHandlers;

    /**
     * The handler for the font-size property.
     */
    private final ShorthandValueHandler fontSizeHandler;

    /**
     * The handler for the line-height property.
     */
    private final ShorthandValueHandler lineHeightHandler;

    /**
     * The handler for the font-family property.
     */
    private final ShorthandValueHandler fontFamilyHandler;

    /**
     * The factory to use to access the properties.
     */
    private final PropertyParserFactory factory;

    /**
     * Initialise.
     *
     * @param factory The factory to use to retrieve parsers for the component
     *                properties.
     */
    public FontParser(PropertyParserFactory factory) {
        this.factory = factory;

        systemFontConverter = factory.getShorthandValueHandler(
                StylePropertyDetails.MCS_SYSTEM_FONT);

        // Get the handlers for style variant and weight.
        styleVariantWeightHandlers = new ShorthandValueHandler
                [STYLE_VARIANT_WEIGHT_PROPERTIES.length];
        for (int i = 0; i < STYLE_VARIANT_WEIGHT_PROPERTIES.length; i++) {
            StyleProperty property = STYLE_VARIANT_WEIGHT_PROPERTIES[i];
            styleVariantWeightHandlers[i] =
                    factory.getShorthandValueHandler(property);
        }

        fontSizeHandler = factory.getShorthandValueHandler(
                StylePropertyDetails.FONT_SIZE);

        lineHeightHandler = factory.getShorthandValueHandler(
                StylePropertyDetails.LINE_HEIGHT);

        fontFamilyHandler = factory.getShorthandValueHandler(
                StylePropertyDetails.FONT_FAMILY);
    }

    // Javadoc inherited.
    protected void setPropertyValue(
            MutableStyleProperties properties, StyleValue value,
            Priority priority) {
        for (int i = 0; i < PropertyGroups.FONT_PROPERTIES.length; i++) {
            StyleProperty property = PropertyGroups.FONT_PROPERTIES[i];
            PropertyValue propertyValue =
                ThemeFactory.getDefaultInstance().createPropertyValue(
                    property, value, priority);
            properties.setPropertyValue(propertyValue);
        }
    }

    // Javadoc inherited.
    protected void parseImpl(
            ParserContext context, StyleValueIterator iterator) {

        // Check for mcs-system-font first.
        MutableStyleProperties properties = context.getStyleProperties();
        StyleValue value = systemFontConverter.convert(context, iterator);
        Priority priority = context.getCurrentPriority();
        if (value != null) {

            // The mcs-system-font causes the other font properties to be set
            // to unknown value.
            StyleProperty[] fontGroup = PropertyGroups.FONT_PROPERTIES;
            for (int i = 0; i < fontGroup.length; i++) {
                StyleProperty property = fontGroup[i];
                properties.setPropertyValue(
                    ThemeFactory.getDefaultInstance().createPropertyValue(
                        property, UnknownFontValue.INSTANCE, priority));
            }

            // Set the mcs-system-font.
            systemFontConverter.setPropertyValue(properties, value, priority);
            return;
        }

        // The properties that have been set.
        MutableStylePropertySet propertySet = new MutableStylePropertySetImpl();

        // [<'font-style'> || <'font-variant'> || <'font-weight'>]?
        int shorthandPropertyCount = styleVariantWeightHandlers.length;
        while (iterator.hasMore()) {

            // Remember how many values were remaining before trying the
            // different handlers.
            int before = iterator.remaining();

            for (int j = 0; j < shorthandPropertyCount; j++) {
                ShorthandValueHandler handler = styleVariantWeightHandlers[j];
                StyleProperty property = STYLE_VARIANT_WEIGHT_PROPERTIES[j];

                // Try the different parsers in turn.
                value = handler.convert(context, iterator);
                if (value != null) {
                    if (propertySet.contains(property)) {
                        context.addDiagnostic(
                                CSSParserMessages.SHORTHAND_VALUE_ALREADY_SET,
                                new Object[]{
                                    context.getCurrentPropertyName(),
                                    handler.getShorthandReference()
                                });
                    } else {
                        handler.setPropertyValue(properties, value,
                                priority);
                        propertySet.add(property);
                    }

                    // Break out of the loop as the value has been consumed.
                    break;
                }
            }

            // If no values were consumed then it cannot be an allowable
            // value for these shorthands so break out.
            int after = iterator.remaining();
            if (after == before) {
                break;
            }
        }

        // 'font-size' [ / <'line-height'>]?
        if (iterator.hasMore()) {
            value = fontSizeHandler.convert(context, iterator);
            if (value != null) {
                fontSizeHandler.setPropertyValue(properties, value,
                        priority);
                propertySet.add(StylePropertyDetails.FONT_SIZE);

                if (iterator.hasMore()) {
                    // Try and see whether there is a line height following.
                    if (iterator.isSeparator(Separator.SLASH)) {
                        iterator.separator(Separator.SLASH);

                        value = lineHeightHandler.convert(context, iterator);
                        if (value != null) {
                            lineHeightHandler.setPropertyValue(properties, value,
                                    priority);
                            propertySet.add(StylePropertyDetails.LINE_HEIGHT);
                        }
                    }
                }
            }
        }

        // <'font-family'>
        if (iterator.hasMore()) {
            value = fontFamilyHandler.convert(context, iterator);
            fontFamilyHandler.setPropertyValue(properties, value,
                    priority);
            propertySet.add(StylePropertyDetails.FONT_FAMILY);
        } else {
            context.addDiagnostic(CSSParserMessages.MISSING_FONT_FAMILY,
                    new Object[]{
                        context.getCurrentPropertyName()
                    });
        }

        // If any of the properties were not set then default them.
        for (int i = 0; i < PropertyGroups.FONT_PROPERTIES.length; i += 1) {
            StyleProperty property = PropertyGroups.FONT_PROPERTIES[i];
            if (!propertySet.contains(property)) {
                ShorthandValueHandler handler =
                        factory.getShorthandValueHandler(property);
                value = handler.getInitial();
                if (value != null) {
                    handler.setPropertyValue(properties, value,
                            priority);
                }
            }
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 28-Sep-05	9487/3	pduffin	VBM:2005091203 Updated JavaDoc for CSS parser and refactored

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 ===========================================================================
*/
