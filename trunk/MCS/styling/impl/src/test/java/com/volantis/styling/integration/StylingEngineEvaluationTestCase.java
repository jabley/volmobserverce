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

package com.volantis.styling.integration;

import com.volantis.mcs.css.parser.CSSParser;
import com.volantis.mcs.css.parser.CSSParserFactory;
import com.volantis.mcs.themes.StyleList;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleSheet;
import com.volantis.mcs.themes.StyleString;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.StyleValueFactory;
import com.volantis.mcs.xdime.XDIMESchemata;
import com.volantis.styling.Styles;
import com.volantis.styling.StylingFactory;
import com.volantis.styling.compiler.CompilerConfiguration;
import com.volantis.styling.compiler.FunctionResolver;
import com.volantis.styling.compiler.FunctionResolverMock;
import com.volantis.styling.compiler.StyleSheetCompiler;
import com.volantis.styling.compiler.StyleSheetSource;
import com.volantis.styling.engine.AttributesMock;
import com.volantis.styling.engine.StylingEngine;
import com.volantis.styling.expressions.EvaluationContext;
import com.volantis.styling.expressions.StylingFunctionMock;
import com.volantis.styling.properties.StyleProperty;
import com.volantis.styling.sheet.CompiledStyleSheet;
import com.volantis.styling.values.MutablePropertyValues;
import com.volantis.testtools.mock.expectations.OrderedExpectations;
import com.volantis.synergetics.testtools.TestCaseAbstract;

import java.io.Reader;
import java.io.StringReader;
import java.util.Arrays;

/**
 * Test the evaluation function of the styling engine.
 * <p/>
 * <p>These tests go from CSS through the parser to the styling engine and
 * applies it to a stream of elements.</p>
 */
public class StylingEngineEvaluationTestCase
        extends TestCaseAbstract {

    private static final String NS = XDIMESchemata.CDM_NAMESPACE;

    private static final StyleValueFactory STYLE_VALUE_FACTORY =
            StyleValueFactory.getDefaultInstance();
    private static final StyleString ABC_STRING =
            STYLE_VALUE_FACTORY.getString(null, "abc");

    private static final StyleString XYZ_STRING =
            STYLE_VALUE_FACTORY.getString(null, "xyz");

    private FunctionResolverMock functionResolverMock;
    private StylingFunctionMock stylingFunctionMock;
    private AttributesMock attributesMock;

    protected void setUp() throws Exception {
        super.setUp();

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        functionResolverMock = new FunctionResolverMock("functionResolverMock",
                                                        expectations);

        stylingFunctionMock = new StylingFunctionMock("stylingFunctionMock",
                                                      expectations);

        attributesMock = new AttributesMock("attributesMock", expectations);

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        functionResolverMock.expects
                .resolve("foo")
                .returns(stylingFunctionMock).any();

        attributesMock.expects
                .getAttributeValue(null, "style")
                .returns(null).any();
    }

    /**
     * Test that the evaluation mechanism works for a single value.
     */
    public void testSimpleFunction() throws Exception {

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        String css = "body {mcs-container: foo('xyz')}" +
                "div {mcs-container: foo('abc')}";
        StringReader reader = new StringReader(css);

        StylingEngine engine = createStylingEngine(reader,
                                                   functionResolverMock);

        final EvaluationContext context = engine.getEvaluationContext();

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        expectations.add(new OrderedExpectations() {
            public void add() {

                stylingFunctionMock.expects
                        .evaluate(context, "foo",
                                  Arrays.asList(new Object[]{
                                      XYZ_STRING
                                  }))
                        .returns(ABC_STRING);

                stylingFunctionMock.expects
                        .evaluate(context, "foo",
                                  Arrays.asList(new Object[]{
                                      ABC_STRING
                                  }))
                        .returns(XYZ_STRING);
            }
        });

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        StyleValue value;

        engine.startElement(NS, "body", attributesMock);
        value = getContainerValue(engine, StylePropertyDetails.MCS_CONTAINER);
        assertEquals(ABC_STRING, value);

        {
            engine.startElement(NS, "div", attributesMock);

            value = getContainerValue(engine, StylePropertyDetails.MCS_CONTAINER);
            assertEquals(XYZ_STRING, value);

            engine.endElement(NS, "div");
        }

        engine.endElement(NS, "body");
    }

    /**
     * Test that the evaluation mechanism works for a single value.
     */
    public void testListFunction() throws Exception {

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        String css = "body {content: foo('abc') 'abc' foo('abc')}";
        StringReader reader = new StringReader(css);

        StylingEngine engine = createStylingEngine(reader,
                                                   functionResolverMock);

        final EvaluationContext context = engine.getEvaluationContext();

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        expectations.add(new OrderedExpectations() {
            public void add() {

                stylingFunctionMock.expects
                        .evaluate(context, "foo",
                                  Arrays.asList(new Object[]{
                                      ABC_STRING
                                  }))
                        .returns(XYZ_STRING);

                stylingFunctionMock.expects
                        .evaluate(context, "foo",
                                  Arrays.asList(new Object[]{
                                      ABC_STRING
                                  }))
                        .returns(XYZ_STRING);
            }
        });

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        StyleValue value;

        StyleList expected = STYLE_VALUE_FACTORY.getList(Arrays.asList(
                new Object[]{
                    XYZ_STRING,
                    ABC_STRING,
                    XYZ_STRING
                }));

        engine.startElement(NS, "body", attributesMock);
        value = getContainerValue(engine, StylePropertyDetails.CONTENT);
        assertEquals(expected, value);

        engine.endElement(NS, "body");
    }

    private StylingEngine createStylingEngine(
            Reader reader,
            final FunctionResolver functionResolverMock) {

        CSSParserFactory factory = CSSParserFactory.getDefaultInstance();
        CSSParser parser = factory.createStrictParser();
        StyleSheet styleSheet = parser.parseStyleSheet(reader, null);

        StylingFactory stylingFactory = StylingFactory.getDefaultInstance();
        CompilerConfiguration configuration =
                stylingFactory.createCompilerConfiguration();
        configuration.setSource(StyleSheetSource.THEME);
        configuration.addFunctionResolver(functionResolverMock);
        StyleSheetCompiler compiler =
                stylingFactory.createStyleSheetCompiler(configuration);

        CompiledStyleSheet compiledStyleSheet =
                compiler.compileStyleSheet(styleSheet);

        StylingEngine engine = stylingFactory.createStylingEngine();

        engine.pushStyleSheet(compiledStyleSheet);
        return engine;
    }

    private StyleValue getContainerValue(
            StylingEngine engine, final StyleProperty property) {
        Styles styles = engine.getStyles();
        MutablePropertyValues propertyValues = styles.getPropertyValues();
        return propertyValues.getComputedValue(
                property);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 06-Dec-05      10641/1 geoff   VBM:2005113024 Pagination page rendering issues

 06-Dec-05      10621/1 geoff   VBM:2005113024 Pagination page rendering issues

 05-Dec-05      10512/1 pduffin VBM:2005112927 Fixed markers, before, after, hr, using images in content

 29-Sep-05      9654/1  pduffin VBM:2005092817 Added support for expressions and functions in styles

 ===========================================================================
*/
