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

package com.volantis.mcs.runtime.styling;

import com.volantis.mcs.context.FormatReferenceFinder;
import com.volantis.mcs.context.FormatReferenceFinderMock;
import com.volantis.mcs.css.parser.CSSParser;
import com.volantis.mcs.css.parser.CSSParserFactory;
import com.volantis.mcs.protocols.FormatReference;
import com.volantis.mcs.protocols.FormatReferenceMock;
import com.volantis.mcs.runtime.layouts.StyleFormatReference;
import com.volantis.mcs.runtime.layouts.styling.SingleNamespaceAttributes;
import com.volantis.mcs.runtime.themes.ThemeStyleSheetCompilerFactory;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleSheet;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.xdime.XDIMESchemata;
import com.volantis.styling.Styles;
import com.volantis.styling.StylingFactory;
import com.volantis.styling.compiler.StyleSheetCompiler;
import com.volantis.styling.compiler.StyleSheetCompilerFactory;
import com.volantis.styling.engine.Attributes;
import com.volantis.styling.engine.StylingEngine;
import com.volantis.styling.expressions.EvaluationContext;
import com.volantis.styling.sheet.CompiledStyleSheet;
import com.volantis.styling.values.MutablePropertyValues;
import com.volantis.synergetics.testtools.TestCaseAbstract;

import java.io.StringReader;

/**
 * Styling integration test cases.
 */
public class StylingIntegrationTestCase
        extends TestCaseAbstract {

    private CSSParser parser;
    private StyleSheetCompiler compiler;
    private StylingEngine engine;
    private FormatReferenceFinderMock formatReferenceFinderMock;
    private Attributes attributes;

    protected void setUp() throws Exception {
        super.setUp();

        // =====================================================================
        //   Create Mocks
        // =====================================================================
        formatReferenceFinderMock = new FormatReferenceFinderMock(
                "formatReferenceFinderMock", expectations);

        CSSParserFactory parserFactory = CSSParserFactory.getDefaultInstance();
        parser = parserFactory.createStrictParser();
        
        StyleSheetCompilerFactory compilerFactory =
                ThemeStyleSheetCompilerFactory.getDefaultInstance();
        compiler = compilerFactory.createStyleSheetCompiler();

        StylingFactory stylingFactory = StylingFactory.getDefaultInstance();
        engine = stylingFactory.createStylingEngine();

        EvaluationContext evaluationContext = engine.getEvaluationContext();
        evaluationContext.setProperty(FormatReferenceFinder.class,
                formatReferenceFinderMock);

        attributes = new SingleNamespaceAttributes(null);
    }

    /**
     * Test that container instance works properly.
     */
    public void testContainerInstance() throws Exception {

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        final FormatReferenceMock formatReferenceMock =
                new FormatReferenceMock("formatReferenceMock", expectations);

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        formatReferenceFinderMock.expects
                .getFormatReference("alpha", new int[]{1})
                .returns(formatReferenceMock);

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        StyleSheet styleSheet = parseStyleSheet(
                "p {mcs-container: mcs-container-instance('alpha', 1)}");

        CompiledStyleSheet compiledStyleSheet =
                compiler.compileStyleSheet(styleSheet);

        engine.pushStyleSheet(compiledStyleSheet);

        engine.startElement(XDIMESchemata.XHTML2_NAMESPACE, "p", attributes);
        Styles styles = engine.getStyles();

        MutablePropertyValues propertyValues = styles.getPropertyValues();
        StyleValue value = propertyValues.getComputedValue(
                StylePropertyDetails.MCS_CONTAINER);

        StyleFormatReference styleFormatReference =
                (StyleFormatReference) value;
        FormatReference formatReference = styleFormatReference.getReference();
        assertSame(formatReferenceMock, formatReference);
    }

    private StyleSheet parseStyleSheet(final String css) {
        StringReader reader = new StringReader(css);
        StyleSheet styleSheet = parser.parseStyleSheet(reader, null);
        return styleSheet;
    }

    /**
     * Test that container instance works properly with counter function.
     */
    public void testContainerInstanceWithCounter() throws Exception {

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        final FormatReferenceMock formatReferenceMock =
                new FormatReferenceMock("formatReferenceMock", expectations);

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        formatReferenceFinderMock.expects
                .getFormatReference("alpha", new int[]{1})
                .returns(formatReferenceMock);

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        StyleSheet styleSheet = parseStyleSheet(
                "p {counter-reset: alpha 1}\n" +
                "p {mcs-container: mcs-container-instance('alpha', counter(alpha))}");

        CompiledStyleSheet compiledStyleSheet =
                compiler.compileStyleSheet(styleSheet);

        engine.pushStyleSheet(compiledStyleSheet);

        engine.startElement(XDIMESchemata.XHTML2_NAMESPACE, "p", attributes);
        Styles styles = engine.getStyles();

        MutablePropertyValues propertyValues = styles.getPropertyValues();
        StyleValue value = propertyValues.getComputedValue(
                StylePropertyDetails.MCS_CONTAINER);

        StyleFormatReference styleFormatReference =
                (StyleFormatReference) value;
        FormatReference formatReference = styleFormatReference.getReference();
        assertSame(formatReferenceMock, formatReference);
    }
}
