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

import com.volantis.mcs.css.impl.parser.ParsingMockTestCaseAbstract;
import com.volantis.mcs.themes.MutableStylePropertiesMock;
import com.volantis.mcs.themes.PropertyValue;
import com.volantis.mcs.themes.StyleKeyword;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.StyleValueType;
import com.volantis.mcs.themes.StyleValueTypeVisitor;
import com.volantis.mcs.themes.ThemeFactory;
import com.volantis.mcs.themes.StyleValueFactory;
import com.volantis.mcs.themes.properties.AllowableKeywords;
import com.volantis.styling.properties.StyleProperty;
import com.volantis.styling.properties.StylePropertyDefinitions;
import junit.framework.Test;
import junit.framework.TestSuite;

import java.util.Iterator;
import java.util.Set;

/**
 * Test that the parameterized property parser works properly.
 */
public class ParameterizedPropertyParserTestCase
        extends ParsingMockTestCaseAbstract
        implements StyleValueTypeVisitor {

    private static final StyleValueFactory STYLE_VALUE_FACTORY =
        StyleValueFactory.getDefaultInstance();

    private final Set supportedTypes;

    private final StyleProperty property;
    private final StyleValueType type;

    /**
     * Dynamically create a test suite that contains tests for all the
     * properties that use a parameterized parser.
     *
     * @return A test suite.
     */
    public static Test suite() {
        
        TestSuite suite = new TestSuite();
        
        PropertyParserFactory factory = new DefaultPropertyParserFactory(
                "PropertyParsers.properties");
        
        StylePropertyDefinitions definitions = 
                StylePropertyDetails.getDefinitions();
        for (Iterator i = definitions.stylePropertyIterator(); i.hasNext();) {
            StyleProperty property = (StyleProperty) i.next();
            PropertyParser parser = factory.getPropertyParser(property.getName());
            if (parser instanceof ParameterizedPropertyParser) {
                Set supportedTypes = property.getStandardDetails().getSupportedTypes();
                TestSuite propertySuite = new TestSuite(
                        "test '" + property.getName() + "'");
                for (Iterator t = supportedTypes.iterator(); t.hasNext();) {
                    StyleValueType type = (StyleValueType) t.next();
                    propertySuite.addTest(new ParameterizedPropertyParserTestCase(
                            property, supportedTypes, type));
                }
                suite.addTest(propertySuite);
            }
        }
        
        return suite;
    }

    public ParameterizedPropertyParserTestCase(
            StyleProperty property,
            Set supportedTypes, StyleValueType type) {

        this.property = property;
        this.supportedTypes = supportedTypes;
        this.type = type;

        setName(type.getType());
    }

    protected void runTest() throws Throwable {

        type.accept(this);
//        for (int i = 0; i < property.getTypes().length; i++) {
//            StyleType type = property.getTypes()[i];
//            type.accept(this);
//        }
    }

    private void parseValue(String css, StyleValue expected) {

        final MutableStylePropertiesMock mutableStylePropertiesMock =
                new MutableStylePropertiesMock("mutableStylePropertiesMock",
                                               expectations);
        styleSheetFactoryMock.expects.createStyleProperties()
                .returns(mutableStylePropertiesMock);

        PropertyValue propertyValue =
            ThemeFactory.getDefaultInstance().createPropertyValue(
                property, expected);

        mutableStylePropertiesMock.expects.setPropertyValue(propertyValue);

        parseDeclarations(property.getName() + ":" + css);
    }

    public void visitAngle() {
        parseValue("10deg", ANGLE_10DEG);
        parseValue("-10deg", ANGLE_NEGATIVE_10DEG);
        parseValue("20rad", ANGLE_20RAD);
        parseValue("30grad", ANGLE_30GRAD);
    }

    public void visitColor() {
        parseValue("green", COLOR_GREEN);
        parseValue("rgb(10%,20%,30%)", COLOR_10PC_20PC_30PC);
        parseValue("rgb(10,20,30)", COLOR_3_7_11);
        parseValue("#fff", COLOR_FFF);
        parseValue("#123456", COLOR_123456);
    }

    public void visitFunction() {
        // Nothing to do.
    }

    public void visitComponentURI() {
        parseValue("mcs-component-url(/image.mimg)", IMAGE_MIMG);
        parseValue("mcs-component-url(\"/image.mimg\")", IMAGE_MIMG);
    }

    public void visitTranscodableURI() {
        parseValue("mcs-transcodable-url(\"http://localhost:8080/tomcat.gif\")",
            STYLE_VALUE_FACTORY.getTranscodableURI(
                null, "http://localhost:8080/tomcat.gif"));
        parseValue("mcs-transcodable-url('http://localhost:8080/tomcat.gif')",
            STYLE_VALUE_FACTORY.getTranscodableURI(
                null, "http://localhost:8080/tomcat.gif"));
    }

    public void visitIdentifier() {
        throw new UnsupportedOperationException();
    }

    public void visitInherit() {
        parseValue("inherit", INHERIT);
    }

    public void visitInteger() {
        parseValue("10", INTEGER_10);
        parseValue("-10", INTEGER_NEGATIVE_10);
    }

    public void visitKeyword() {
        AllowableKeywords allowableKeywords =
                property.getStandardDetails().getAllowableKeywords();
        StyleKeyword keyword = (StyleKeyword) allowableKeywords.getKeywords().get(0);
        parseValue(keyword.getName(), keyword);
    }

    public void visitLength() {
        if (!supportedTypes.contains(StyleValueType.NUMBER)) {
            parseValue("0", LENGTH_0PX);
        }
        parseValue("1px", LENGTH_1PX);
        parseValue("2cm", LENGTH_2CM);
        parseValue("-2pt", LENGTH_NEGATIVE_2PT);
    }

    public void visitList() {
        throw new UnsupportedOperationException();
    }

    public void visitNumber() {
        parseValue("10", NUMBER_10);
        parseValue("1.5", NUMBER_1_5);
        parseValue("-1.5", NUMBER_NEGATIVE_1_5);
    }

    public void visitPair() {
        throw new UnsupportedOperationException();
    }

    public void visitPercentage() {
        parseValue("25%", PERCENTAGE_25);
    }

    public void visitString() {
        parseValue("\"string\"", STRING_STRING);
        parseValue("'string'", STRING_STRING);
    }

    public void visitTime() {
        parseValue("10s", TIME_10S);
        parseValue("-10ms", TIME_NEGATIVE_10MS);
    }

    public void visitURI() {
        parseValue("url(/image.png)", IMAGE_PNG);
        parseValue("url(\"/image.png\")", IMAGE_PNG);
    }

    public void visitFrequency() {
        parseValue("10hz", FREQ_10HZ);
        parseValue("10khz", FREQ_10KHZ);
    }

    public void visitFraction() {
        parseValue("2cm/10s", FRACTION_FIFTH_CM_PER_SEC);
        parseValue("-2pt/10s", FRACTION_NEG_FIFTH_PT_PER_SEC);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10505/3	pduffin	VBM:2005111405 Committing transactions from MCS 3.5.0 (7)

 29-Nov-05	10347/2	pduffin	VBM:2005111405 Massive changes for performance

 26-Oct-05	9965/1	ianw	VBM:2005101811 Interim commit

 01-Nov-05	9888/1	pduffin	VBM:2005101811 Committing new user interface changes that have been ported forward from 3.5

 26-Oct-05	9965/1	ianw	VBM:2005101811 Interim commit

 29-Sep-05	9654/1	pduffin	VBM:2005092817 Added support for expressions and functions in styles

 28-Sep-05	9487/3	pduffin	VBM:2005091203 Updated JavaDoc for CSS parser and refactored

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 ===========================================================================
*/
