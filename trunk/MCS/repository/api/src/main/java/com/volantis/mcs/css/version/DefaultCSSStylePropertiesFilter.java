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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.css.version;

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.themes.MutableStyleProperties;
import com.volantis.mcs.themes.PropertyValue;
import com.volantis.mcs.themes.StyleKeyword;
import com.volantis.mcs.themes.StyleProperties;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.StyleValueType;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.ThemeFactory;
import com.volantis.mcs.themes.values.ShorthandValue;
import com.volantis.styling.properties.StyleProperty;
import com.volantis.synergetics.log.LogDispatcher;

import java.util.Iterator;

/**
 * A default implementation of CSSStylePropertiesFilter.
 * <p>
 * Currently this filters simple values and their value types and keywords,
 * and bitset keywords. Other compound types such as list and pair cannot be
 * filtered until the theme schema generation is improved to pass more data
 * through.
 */
public class DefaultCSSStylePropertiesFilter
        implements CSSStylePropertiesFilter {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger = 
            LocalizationFactory.createLogger(DefaultCSSStylePropertiesFilter.class);

    /**
     * The CSS version this filter is targetting.
     */
    private CSSVersion cssVersion;

    /**
     * Initialise.
     *
     * @param cssVersion the CSS version this filter is targetting.
     */
    public DefaultCSSStylePropertiesFilter(CSSVersion cssVersion) {
        this.cssVersion = cssVersion;
    }

    // Javadoc inherited.
    public StyleProperties filter(StyleProperties input) {
        MutableStyleProperties output =
            ThemeFactory.getDefaultInstance().createMutableStyleProperties();

        Iterator itr = input.propertyValueIterator();
        while (itr.hasNext()) {
            PropertyValue propertyValue = (PropertyValue) itr.next();
            StyleProperty property = propertyValue.getProperty();

            StyleValue inputValue = propertyValue.getValue();
            StyleValue outputValue = null;

            // So, if the CSS version supports that property...
            CSSProperty cssProperty = cssVersion.getProperty(property);
                       
            if (cssProperty != null) {
                // ... then check value type and content of the property.

                // Unfortunately, the theme generation of compound (meaning
                // pair and set) style values is currently broken.
                // This can be seen by examining the CSSVersion and
                // StylePropertyDetails classes that it generates - often times
                // meta data associated with compound values is usually missing.
                // Thus:
                // - Pair values (StylePair) and ordered set (StyleList) values
                // we currently IGNORE because none of the css versions we
                // support require filtering on these properties.
                // - Unordered set (StyleBitSet) values seeming have their meta
                // data supplied correctly (enough) by the generator, and both
                // CSSMobile and CSSWAP require filtering on keyword values for
                // text-decoration. By enough, we mean that the keywords
                // themselves are present, but there is no mention of bitset.
                // Thus we have implemented filtering for StyleBitSet keywords.
                //
                // TODO: later: fix the code generator to generate all value meta data
                // TODO: later: implement filtering for pairs and ordered sets using above
                // TODO: later: fix up filtering for unordered sets too

                // Check the value type...
                final StyleValueType valueType = inputValue.getStyleValueType();
                if (valueType == StyleValueType.PAIR ) {
                    // It's a StylePair (border-spacing, background-position).
                    // We deliberately do not filter ANY pair values since the
                    // value meta data for pairs is not generated correctly.
                    // See comment above.
                    outputValue = inputValue;
                } else if (valueType == StyleValueType.LIST) {
                    // It's a StyleList (font-family)
                    // We deliberately do not filter ANY list values since the
                    // value meta data for lists is not generated correctly.
                    // See comment above.
                    outputValue = inputValue;
                }  else if (valueType == StyleValueType.FRACTION) {
                    // It's a StyleFraction (mcs-marquee-speed)
                    // We deliberately do not filter ANY fraction values since
                    // the value meta data for composite values is not
                    // generated correctly. See comment above.
                    outputValue = inputValue;
                } else {
                    // It's a simple value (not pair, fraction or list)
                    // So filter by value type and keyword if appropriate.
                    outputValue = filterSimpleValue(cssProperty, inputValue);
                }

            // check also alias property saved in cssVersion,
            // like -moz-border-radius-topleft or -webkit-border-top-right-radius                
            } else if(cssVersion.getPropertyAlias(property) != null) {
                    outputValue = inputValue;
            } else {
                if (logger.isDebugEnabled()) {
                    logger.debug("Filtered property " + property.getName() +
                            " using " + cssVersion);
                }
            }

            if (outputValue != null) {
                output.setPropertyValue(
                    ThemeFactory.getDefaultInstance().createPropertyValue(
                        property, outputValue, propertyValue.getPriority()));
            }
        }

        // Copy all the shorthands across as the unsupported ones should
        // already have been filtered out.
        itr = input.shorthandValueIterator();
        while (itr.hasNext()) {
            ShorthandValue shorthandValue = (ShorthandValue) itr.next();
            output.setShorthandValue(shorthandValue);
        }

        return output;
    }

    /**
     * Filter a simple (non-compound) property value.
     * <p>
     * This filters by value type and keyword value.
     *
     * @param cssProperty the css property the value is for.
     * @param inputValue the value to filter.
     * @return the filtered value, may be null.
     */
    private StyleValue filterSimpleValue(CSSProperty cssProperty,
            StyleValue inputValue) {

        StyleValue outputValue = null;

        // If this CSS version supports this value type for this property...
        if (cssProperty.supportsValueType(inputValue.getStyleValueType())) {
            // then check the value content.
            // If this is a keyword...
            if (inputValue instanceof StyleKeyword) {
                // ... then,  we better check that the value is supported.

                // It is late, we are screwed, but PD's changes have made this
                // impossible for us mortals. @todo brain transplant.
                if (cssProperty.getStyleProperty() !=
                        StylePropertyDetails.VERTICAL_ALIGN) {
                    StyleKeyword keyword = (StyleKeyword) inputValue;
                    if (cssProperty.supportsKeyword(keyword)) {
                        outputValue = inputValue;
                    } else {
                        if (logger.isDebugEnabled()) {
                            logger.debug("Filtered keyword " + keyword +
                                    " using " + cssProperty);
                        }
                    }
                } else {
                    outputValue = inputValue;
                }
            } else {
                // ... else, we don't bother checking values for other types.
                outputValue = inputValue;
            }
        } else {
            if (logger.isDebugEnabled()) {
                logger.debug("Filtered " + inputValue + " using " +
                        cssProperty);
            }
        }

        return outputValue;
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 14-Dec-05	10833/2	ianw	VBM:2005121405 hack CSS filters

 29-Nov-05	10347/2	pduffin	VBM:2005111405 Massive changes for performance

 13-Sep-05	9496/1	pduffin	VBM:2005091211 Replaced text-decoration with individual properties

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 06-May-05	8090/1	emma	VBM:2005050411 Fixing broken css underline emulation for WML

 06-May-05	8048/1	emma	VBM:2005050411 Fixing broken css underline emulation for WML

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/3	doug	VBM:2004111702 Refactored Logging framework

 19-Nov-04	5733/4	geoff	VBM:2004093001 Support OMA WCSS subset of CSS2

 ===========================================================================
*/
