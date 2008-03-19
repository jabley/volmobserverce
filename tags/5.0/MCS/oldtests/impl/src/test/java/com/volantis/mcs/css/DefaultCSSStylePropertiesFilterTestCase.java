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
package com.volantis.mcs.css;

import com.volantis.mcs.css.version.DefaultCSSProperty;
import com.volantis.mcs.css.version.DefaultCSSStylePropertiesFilter;
import com.volantis.mcs.css.version.DefaultCSSVersion;
import com.volantis.mcs.themes.StyleKeyword;
import com.volantis.mcs.themes.StyleProperties;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.StyleValueFactory;
import com.volantis.mcs.themes.StyleValueType;
import com.volantis.mcs.themes.ThemeFactory;
import com.volantis.mcs.themes.MutableStyleProperties;
import com.volantis.mcs.themes.properties.FontSizeKeywords;
import com.volantis.styling.properties.StyleProperty;
import com.volantis.synergetics.testtools.TestCaseAbstract;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Category;
import org.apache.log4j.Level;

public class DefaultCSSStylePropertiesFilterTestCase
        extends TestCaseAbstract {
    
    private static final StyleValueFactory STYLE_VALUE_FACTORY =
            StyleValueFactory.getDefaultInstance();

    public void setUp() {
        BasicConfigurator.configure();
        // Tests by default have logging turned off
        Category.getRoot().setLevel(Level.OFF);
    }

    protected void tearDown() throws Exception {
        Category.shutdown();
    }

    public void testNullPropertyValue() {

        StyleProperty property = StylePropertyDetails.FONT_SIZE;

        DefaultCSSVersion cssVersion = new DefaultCSSVersion("test");
        DefaultCSSProperty propertyVersion = cssVersion.addProperty(
                property);
        propertyVersion.addValueTypes(new StyleValueType[]{
            StyleValueType.PERCENTAGE
        });
        cssVersion.markImmutable();

        DefaultCSSStylePropertiesFilter cssFilter =
                new DefaultCSSStylePropertiesFilter(cssVersion);

        checkSupported(cssFilter, property, null);
    }

    public void testSimpleProperty() {

        StyleProperty property = StylePropertyDetails.FONT_SIZE;

        /**
         public static StylePropertyDetails FONT_SIZE =
             new StylePropertyDetails("font-size", 25,
                 new StyleType[] {
                     StyleType.get("inherit"),
                     StyleType.get("keyword"),
                     StyleType.get("length"),
                     StyleType.get("percentage")},
                 new StyleKeyword(3),
                 true);

         public interface FontSizeEnumeration {
             public static final int XX_SMALL = 0;
             public static final int X_SMALL = 1;
             public static final int SMALL = 2;
             public static final int MEDIUM = 3;
             public static final int LARGE = 4;
             public static final int X_LARGE = 5;
             public static final int XX_LARGE = 6;
             public static final int LARGER = 7;
             public static final int SMALLER = 8;

         */

        DefaultCSSVersion cssVersion = new DefaultCSSVersion("test");
        DefaultCSSProperty propertyVersion = cssVersion.addProperty(
                property);
        propertyVersion.addValueTypes(new StyleValueType[]{
            // inherit not supported
            StyleValueType.KEYWORD,
            // length not supported
            StyleValueType.PERCENTAGE
        });
        propertyVersion.addKeywords(new StyleKeyword[]{
            FontSizeKeywords.LARGE,
            FontSizeKeywords.SMALL,
            // all others not supported.
        });
        cssVersion.markImmutable();

        DefaultCSSStylePropertiesFilter cssFilter =
                new DefaultCSSStylePropertiesFilter(cssVersion);

        checkRemoved(cssFilter, property, STYLE_VALUE_FACTORY.getInherit());
        checkRemoved(cssFilter, property, FontSizeKeywords.MEDIUM);
        checkSupported(cssFilter, property,
            STYLE_VALUE_FACTORY.getPercentage(null, 99.0));
        checkSupported(cssFilter, property, FontSizeKeywords.LARGE);
    }

    private void checkSupported(DefaultCSSStylePropertiesFilter cssFilter,
            StyleProperty property, StyleValue value) {

        MutableStyleProperties input =
            ThemeFactory.getDefaultInstance().createMutableStyleProperties();
        input.setStyleValue(property, value);
        StyleProperties output = cssFilter.filter(input);
        assertEquals("", value, output.getStyleValue(property));
    }

    private void checkRemoved(DefaultCSSStylePropertiesFilter cssFilter,
            StyleProperty property, StyleValue value) {

        MutableStyleProperties input =
            ThemeFactory.getDefaultInstance().createMutableStyleProperties();
        input.setStyleValue(property, value);
        StyleProperties output = cssFilter.filter(input);
        assertNull("", output.getStyleValue(property));
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10505/3	pduffin	VBM:2005111405 Committing transactions from MCS 3.5.0 (7)

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 26-Oct-05	9965/1	ianw	VBM:2005101811 Interim commit

 01-Nov-05	9888/1	pduffin	VBM:2005101811 Committing new user interface changes that have been ported forward from 3.5

 26-Oct-05	9965/1	ianw	VBM:2005101811 Interim commit

 13-Sep-05	9496/1	pduffin	VBM:2005091211 Replaced text-decoration with individual properties

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 06-May-05	8090/1	emma	VBM:2005050411 Fixing broken css underline emulation for WML

 06-May-05	8048/1	emma	VBM:2005050411 Fixing broken css underline emulation for WML

 09-Dec-04	6417/1	philws	VBM:2004120703 Committing tidy up

 19-Nov-04	5733/3	geoff	VBM:2004093001 Support OMA WCSS subset of CSS2

 ===========================================================================
*/
