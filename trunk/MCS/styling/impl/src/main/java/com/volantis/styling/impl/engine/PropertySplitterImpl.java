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

package com.volantis.styling.impl.engine;

import com.volantis.mcs.themes.MutableStyleProperties;
import com.volantis.mcs.themes.Priority;
import com.volantis.mcs.themes.PropertyGroups;
import com.volantis.mcs.themes.PropertyValue;
import com.volantis.mcs.themes.PropertyValueArray;
import com.volantis.mcs.themes.SparsePropertyValueArray;
import com.volantis.mcs.themes.StyleProperties;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.ThemeFactory;
import com.volantis.mcs.themes.font.UnknownFontValue;
import com.volantis.styling.impl.compiler.ValueCompiler;
import com.volantis.styling.properties.StyleProperty;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Splits the supplied properties into important and normal.
 */
public class PropertySplitterImpl
        implements PropertySplitter {

    /**
     * The set of unknown font property values that must be set.
     */
    public static final PropertyValue[] UNKNOWN_FONT_PROPERTIES;
    static {
        StyleProperty[] fontGroup = PropertyGroups.FONT_PROPERTIES;
        UNKNOWN_FONT_PROPERTIES = new PropertyValue[fontGroup.length];
        for (int i = 0; i < fontGroup.length; i++) {
            StyleProperty fontProperty = fontGroup[i];
            PropertyValue value =
                ThemeFactory.getDefaultInstance().createPropertyValue(
                    fontProperty, UnknownFontValue.INSTANCE);
            UNKNOWN_FONT_PROPERTIES[i] = value;
        }
    }
    private static final StyleProperty MCS_SYSTEM_FONT =
            StylePropertyDetails.MCS_SYSTEM_FONT;

    /**
     * The object to compile all the values.
     */
    private final ValueCompiler compiler;

    /**
     * A map from {@link Priority} to {@link PropertyValueArray}.
     */
    private final Map priority2Values;

    /**
     * Initialise.
     *
     * @param compiler The compiler for individual values.
     */
    public PropertySplitterImpl(ValueCompiler compiler) {
        if (compiler == null) {
            throw new IllegalArgumentException("compiler cannot be null");
        }

        this.compiler = compiler;

        priority2Values = new HashMap();
    }

    // Javadoc inherited.
    public Prioritised[] split(StyleProperties properties) {

        // Reset the state.
        for (Iterator i = priority2Values.values().iterator(); i.hasNext();) {
            PropertyValueArray array = (PropertyValueArray) i.next();
            array.clear();
        }

        properties = addSystemFont(properties);

        for (Iterator i = properties.propertyValueIterator(); i.hasNext();) {
            PropertyValue propertyValue = (PropertyValue) i.next();

            StyleValue value = propertyValue.getValue();

            // Compile the value.
            StyleValue compiledValue = compiler.compile(value);

            // If compiling changed the value then create a new property value.
            Priority priority = propertyValue.getPriority();
            if (compiledValue != value) {
                propertyValue = ThemeFactory.getDefaultInstance().
                    createPropertyValue(propertyValue.getProperty(),
                        compiledValue, priority);
            }

            PropertyValueArray array = (PropertyValueArray)
                    priority2Values.get(priority);
            if (array == null) {
                array = new SparsePropertyValueArray();
                priority2Values.put(priority, array);
            }

            array.setPropertyValue(propertyValue);
        }

        Prioritised[] prioritised = new Prioritised[priority2Values.size()];
        int p = 0;
        for (Iterator i = priority2Values.entrySet().iterator(); i.hasNext();
             p += 1) {

            Map.Entry entry = (Map.Entry) i.next();
            Priority priority = (Priority) entry.getKey();
            PropertyValueArray array = (PropertyValueArray) entry.getValue();
            prioritised[p] = new Prioritised(priority,
                    array.toPropertyValueArray());
        }
        return prioritised;
    }

    private StyleProperties addSystemFont(StyleProperties properties) {
        // The mcs-system-font property is equivalent to setting all properties
        // set by the font shorthand to unknown user agent specific values.
        PropertyValue systemFont = properties.getPropertyValue(MCS_SYSTEM_FONT);
        if (systemFont != null) {
            Priority systemFontPriority = systemFont.getPriority();
            MutableStyleProperties copy =
                ThemeFactory.getDefaultInstance().createMutableStyleProperties(
                    properties);

            // The system font is set so make sure that all the other font
            // properties are set with equal or greater priority.
            for (int i = 0; i < UNKNOWN_FONT_PROPERTIES.length; i++) {
                PropertyValue value = UNKNOWN_FONT_PROPERTIES[i];
                StyleProperty property = value.getProperty();

                PropertyValue propertyValue = properties.getPropertyValue(
                        property);
                if (propertyValue == null ||
                        systemFontPriority.isGreaterThan(
                                propertyValue.getPriority())) {

                    propertyValue =
                        ThemeFactory.getDefaultInstance().createPropertyValue(
                            property, UnknownFontValue.INSTANCE,
                            systemFontPriority);
                    copy.setPropertyValue(propertyValue);
                }
            }

            properties = copy;
        }
        return properties;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/7	pduffin	VBM:2005111405 Massive changes for performance

 21-Nov-05	10347/5	pduffin	VBM:2005111405 Cleaned up PropertyValues to remove synthesised properties and moved specified into an extended interface

 18-Nov-05	10347/3	pduffin	VBM:2005111405 Stopped copying style values in order to change whether they were explicitly specified or not

 18-Nov-05	10347/1	pduffin	VBM:2005111405 Performance optimizations on the styling engine

 29-Sep-05	9654/1	pduffin	VBM:2005092817 Added support for expressions and functions in styles

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 21-Jun-05	8856/1	geoff	VBM:2005062005 Refactoring for XDIMECP: Generate optimised CSS for a DOM.

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 ===========================================================================
*/
