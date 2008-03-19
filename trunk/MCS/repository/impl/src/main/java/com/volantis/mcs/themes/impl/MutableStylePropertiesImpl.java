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
package com.volantis.mcs.themes.impl;

import com.volantis.mcs.themes.values.ShorthandValue;
import com.volantis.mcs.themes.values.StyleColorNames;
import com.volantis.mcs.themes.values.StyleKeywords;
import com.volantis.mcs.themes.MutableStyleProperties;
import com.volantis.mcs.themes.StyleValueFactory;
import com.volantis.mcs.themes.PropertyValueArray;
import com.volantis.mcs.themes.SparsePropertyValueArray;
import com.volantis.mcs.themes.PropertyValue;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.StyleProperties;
import com.volantis.mcs.themes.ThemeVisitor;
import com.volantis.mcs.themes.PropertyValueIteratee;
import com.volantis.mcs.themes.StyleShorthand;
import com.volantis.mcs.themes.ShorthandValueIteratee;
import com.volantis.mcs.themes.StyleValueVisitorStub;
import com.volantis.mcs.themes.StyleColorName;
import com.volantis.mcs.themes.StyleInherit;
import com.volantis.mcs.themes.StyleKeyword;
import com.volantis.mcs.themes.StylePair;
import com.volantis.mcs.themes.StyleList;
import com.volantis.mcs.themes.StyleFraction;
import com.volantis.mcs.themes.ThemeFactory;
import com.volantis.shared.iteration.IterationAction;
import com.volantis.styling.properties.StyleProperty;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * This class provides an implementation of StyleProperties.  It acts as a
 * wrapper to an existing instance of StyleProperties by taking those values
 * and including them but also providing a means of setting new values.
 */
public class MutableStylePropertiesImpl
    implements MutableStyleProperties {

    /**
     * The factory for {@link com.volantis.mcs.themes.StyleValue}s.
     */
    private static final StyleValueFactory STYLE_VALUE_FACTORY =
            StyleValueFactory.getDefaultInstance();

    /**
     * The array of properties.
     */
    private PropertyValueArray propertyValues;

    /**
     * The map of shorthands.
     */
    private Map shorthandValues;

    /**
     * Creates a new instance of this class, creating an empty array of style values
     */
    public MutableStylePropertiesImpl() {
        propertyValues = new SparsePropertyValueArray();
    }

    /**
     * Perform post JIBX initialisation.
     */
    void jibxPostSet() {
        ConvertStyleValues converter = new ConvertStyleValues();
        Iterator iterator = propertyValueIterator();
        while (iterator.hasNext()) {
            PropertyValue propertyValue = (PropertyValue) iterator.next();
            StyleValue value = propertyValue.getValue();

            // Convert the value.
            StyleValue converted = converter.convertValue(value);

            // If the value changed then update the property value. 
            if (value != converted) {
                propertyValue =
                    ThemeFactory.getDefaultInstance().createPropertyValue(
                        propertyValue.getProperty(), converted,
                        propertyValue.getPriority());
                setPropertyValue(propertyValue);
            }
        }
    }

    /**
     * Create a new instance of this class, initialising for values, followed
     * by population of these with existing values.  If a null value is passed
     * into this constructor then it will throw an
     * {@link IllegalArgumentException}.
     *
     * @param properties The style properties that should be added into
     *                       this object at creation time.  May not be null.
     */
    public MutableStylePropertiesImpl(StyleProperties properties) {
        if (properties == null) {
            throw new IllegalArgumentException("properties cannot be null");
        }
        if (properties instanceof MutableStylePropertiesImpl) {
            MutableStylePropertiesImpl mutable = (MutableStylePropertiesImpl) properties;
            this.propertyValues = mutable.propertyValues.copyPropertyValueArray();
            if (mutable.shorthandValues != null) {
                shorthandValues = new HashMap(mutable.shorthandValues);
            }
        } else {
            throw new IllegalArgumentException(
                    "Unknown style properties type " + properties);
        }
    }

    public MutableStylePropertiesImpl(PropertyValueArray array) {
        this.propertyValues = array;
    }

    // Javadoc inherited.
    public StyleValue getStyleValue(StyleProperty property) {
        return propertyValues.getStyleValue(property);
    }

    // Javadoc inherited.
    public PropertyValue getPropertyValue(StyleProperty property) {
        return propertyValues.getPropertyValue(property);
    }

    // JavaDoc inherited
    public void setStyleValue(StyleProperty property, StyleValue value) {
        propertyValues.setStyleValue(property, value);
    }

    public void setPropertyValue(PropertyValue propertyValue) {
        propertyValues.setPropertyValue(propertyValue);
    }

    public PropertyValue clearPropertyValue(StyleProperty property) {
        return propertyValues.clearPropertyValue(property);
    }

    // Javadoc inherited.
    public boolean isEmpty() {
        return propertyValues.isEmpty() &&
                (shorthandValues == null || shorthandValues.isEmpty());
    }

    // Javadoc inherited from interface
    public void accept(ThemeVisitor visitor) {
        visitor.visit(this);
    }

    // Javadoc inherited from super class.
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof MutableStylePropertiesImpl)) {
            return false;
        }

        MutableStylePropertiesImpl properties = (MutableStylePropertiesImpl) o;

        boolean thisEmpty = (shorthandValues == null || shorthandValues.isEmpty());
        boolean otherEmpty = (properties.shorthandValues == null || properties.shorthandValues.isEmpty());
        if (thisEmpty) {
            if (!otherEmpty) {
                return false;
            }
        } else if (!shorthandValues.equals(properties.shorthandValues)) {
            return false;
        }

        return propertyValues.equals(properties.propertyValues);
    }

    // Javadoc inherited from super class.
    public int hashCode() {
        return propertyValues.hashCode();
    }

    // Javadoc inherited.
    public IterationAction iteratePropertyValues(
            PropertyValueIteratee iteratee) {
        return propertyValues.iteratePropertyValues(iteratee);
    }

    // Javadoc inherited.
    public Iterator propertyValueIterator() {
        return propertyValues.propertyValueIterator();
    }

    public void setShorthandValue(ShorthandValue shorthandValue) {
        if (shorthandValues == null) {
            // Make the shorthand values ordered so tests will work properly.
            shorthandValues = new TreeMap();
        }
        shorthandValues.put(shorthandValue.getShorthand().getName(),
                shorthandValue);
    }

    // Javadoc inherited.
    public ShorthandValue getShorthandValue(StyleShorthand shorthand) {
        if (shorthandValues == null) {
            return null;
        }
        return (ShorthandValue) shorthandValues.get(shorthand.getName());
    }

    // Javadoc inherited.
    public IterationAction iterateShorthandValues(ShorthandValueIteratee iteratee) {

        IterationAction action = IterationAction.CONTINUE;
        if (shorthandValues != null && !shorthandValues.isEmpty()) {
            Iterator i = new ArrayList(shorthandValues.values()).iterator();
            while (i.hasNext() && action != IterationAction.BREAK) {
                ShorthandValue shorthandValue = (ShorthandValue) i.next();
                action = iteratee.next(shorthandValue);
            }
        }
        return action;
    }

    // Javadoc inherited.
    public Iterator shorthandValueIterator() {
        if (shorthandValues == null) {
            return Collections.EMPTY_LIST.iterator();
        } else {
            return shorthandValues.values().iterator();
        }
    }

    // Javadoc inherited.
    public String getStandardCSS() {
        final StringBuffer buffer = new StringBuffer();
        propertyValues.appendStandardCSS(buffer);
        iterateShorthandValues(new ShorthandValueIteratee() {
            public IterationAction next(ShorthandValue shorthandValue) {
                if (buffer.length() != 0) {
                    buffer.append(";");
                }
                buffer.append(shorthandValue.getStandardCSS());
                return IterationAction.CONTINUE;
            }
        });
        return buffer.toString();
    }

    // Javadoc inherited.
    public String toString() {
        return getStandardCSS();
    }

    public void clearShorthandValue(StyleShorthand shorthand) {
        if (shorthandValues != null) {
            shorthandValues.remove(shorthand.getName());
        }
    }

    /**
     * Fixes up problems caused by jibx unmarshalling, i.e. multiple
     * StyleKeyword instances for the same keyword.
     */
    private static class ConvertStyleValues
            extends StyleValueVisitorStub {

        private StyleValue converted;

        public StyleValue convertValue(StyleValue value) {
            StyleValue oldConverted = converted;
            converted = value;
            if (value != null) {
                value.visit(this, null);
            }
            StyleValue result = converted;
            converted = oldConverted;
            return result;
        }

        public void visit(StyleColorName value, Object object) {
            // Get the shared color name.
            converted = StyleColorNames.getColorByName(value.getName());
        }

        public void visit(StyleInherit value, Object object) {
            converted = STYLE_VALUE_FACTORY.getInherit();
        }

        public void visit(StyleKeyword value, Object object) {
            // Get the shared keyword.
            converted = StyleKeywords.getKeywordByName(value.getName());
        }

        public void visit(StylePair value, Object object) {

            StyleValue first = value.getFirst();
            StyleValue firstConverted = convertValue(first);

            StyleValue second = value.getSecond();
            StyleValue secondConverted = convertValue(second);

            if (first != firstConverted || second != secondConverted) {
                converted =
                    STYLE_VALUE_FACTORY.getPair(firstConverted, secondConverted);
            }
        }

        public void visit(StyleList value, Object object) {
            List list = value.getList();
            List convertedList = new ArrayList();
            boolean changed = false;
            for (int i = 0; i < list.size(); i++) {
                StyleValue item = (StyleValue) list.get(i);
                StyleValue convertedItem = convertValue(item);
                convertedList.add(convertedItem);
                if (item != convertedItem) {
                    changed = true;
                }
            }

            if (changed) {
                converted =
                    STYLE_VALUE_FACTORY.getList(convertedList, value.isUnique());
            }
        }

        public void visit(StyleFraction value, Object object) {
            StyleValue numerator = value.getNumerator();
            StyleValue numeratorConverted = convertValue(numerator);

            StyleValue denominator = value.getDenominator();
            StyleValue denominatorConverted = convertValue(denominator);

            if (numerator != numeratorConverted || denominator != denominatorConverted) {
                converted = STYLE_VALUE_FACTORY.getFraction(
                    numeratorConverted, denominatorConverted);
            } 
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/4	pduffin	VBM:2005111405 Massive changes for performance

 18-Nov-05	10347/1	pduffin	VBM:2005111405 Performance optimizations on the styling engine

 05-Sep-05	9407/1	pduffin	VBM:2005083007 Removed old themes model

 15-Jul-05	9067/3	geoff	VBM:2005071415 More refactoring for: XDIMECP: Generate optimised CSS for a DOM.

 12-Jul-05	8996/1	pduffin	VBM:2005071103 Enhanced mock generation to support methods defined on Object better

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 25-May-05	8329/1	pabbott	VBM:2005051901 New visitor pattern for Themes v3

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 02-Sep-04	5354/1	tom	VBM:2004082008 started device theme normalization

 06-Apr-04	3641/3	claire	VBM:2004032602 Enhancements and updating testcase coverage

 30-Mar-04	3641/1	claire	VBM:2004032602 Using menu types and styles in PAPI

 ===========================================================================
*/
