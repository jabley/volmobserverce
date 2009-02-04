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

package com.volantis.mcs.dom2theme.integration.renderer;

import com.volantis.mcs.css.parser.CSSParser;
import com.volantis.mcs.css.parser.CSSParserFactory;
import com.volantis.mcs.css.renderer.CSSStyleSheetRenderer;
import com.volantis.mcs.css.renderer.PropertyRenderer;
import com.volantis.mcs.css.renderer.RendererContext;
import com.volantis.mcs.css.renderer.StyleSheetRenderer;
import com.volantis.mcs.dom2theme.impl.normalizer.PropertiesNormalizer;
import com.volantis.mcs.dom2theme.impl.optimizer.ShorthandOptimizer;
import com.volantis.mcs.dom2theme.impl.optimizer.TargetEntity;
import com.volantis.mcs.dom2theme.integration.optimizer.TestPropertyClearerChecker;
import com.volantis.mcs.themes.MutableStyleProperties;
import com.volantis.mcs.themes.PropertyValue;
import com.volantis.mcs.themes.PropertyValueIteratee;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleValuesMock;
import com.volantis.mcs.themes.ThemeFactory;
import com.volantis.shared.iteration.IterationAction;
import com.volantis.styling.device.DeviceStylingTestHelper;
import com.volantis.styling.device.DeviceValues;
import com.volantis.styling.device.DeviceValuesMock;
import com.volantis.styling.values.MutablePropertyValues;
import com.volantis.styling.StylingFactory;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import junit.framework.Test;
import junit.framework.TestSuite;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ShorthandRendererTestSuiteBuilder {

    private List tests = new ArrayList();

    private final PropertiesNormalizer normalizer;
    private final PropertyRenderer renderer;
    private final ShorthandOptimizer allShorthandsOptimizer;
    private final ShorthandOptimizer noShorthandOptimizer;
    private final TestPropertyClearerChecker checker;

    public ShorthandRendererTestSuiteBuilder(
            PropertiesNormalizer normalizer, PropertyRenderer renderer) {
        this(normalizer, renderer, null, null, null);
    }

    public ShorthandRendererTestSuiteBuilder(
            PropertiesNormalizer normalizer,
            PropertyRenderer renderer,
            ShorthandOptimizer allShorthandsOptimizer,
            ShorthandOptimizer noShorthandOptimizer,
            TestPropertyClearerChecker checker) {

        this.normalizer = normalizer;
        this.renderer = renderer;
        this.allShorthandsOptimizer = allShorthandsOptimizer;
        this.noShorthandOptimizer = noShorthandOptimizer;
        this.checker = checker;
    }

    public void addTest(
            String inputCSS,
            String expectedCSS) {

        tests.add(new ShorthandTest(inputCSS, expectedCSS, "", allShorthandsOptimizer));
    }

    public void addWithAndWithout(
            String inputCSS,
            String expectedWithoutShorthandsCSS) {

        tests.add(
                new ShorthandTest(inputCSS, inputCSS, "with shorthands",
                        allShorthandsOptimizer));
        tests.add(
                new ShorthandTest(inputCSS, expectedWithoutShorthandsCSS,
                        "without shorthands", noShorthandOptimizer));
    }

    public void addWithAndWithout(
            String inputCSS,
            String expectedWithShorthandsCSS,
            String expectedWithoutShorthandsCSS) {

        tests.add(
                new ShorthandTest(inputCSS, expectedWithShorthandsCSS,
                        "with shorthands", allShorthandsOptimizer));
        tests.add(
                new ShorthandTest(inputCSS, expectedWithoutShorthandsCSS,
                        "without shorthands", noShorthandOptimizer));
    }

    public void addSameWithAndWithout(String inputCSS) {

        tests.add(
                new ShorthandTest(inputCSS, inputCSS, "with shorthands",
                        allShorthandsOptimizer));
        tests.add(
                new ShorthandTest(inputCSS, inputCSS, "without shorthands",
                        noShorthandOptimizer));
    }

    public Test getSuite() {
        TestSuite suite = new TestSuite();
        for (Iterator i = tests.iterator(); i.hasNext();) {
            Test test = (Test) i.next();
            suite.addTest(test);
        }
        return suite;
    }

    private class ShorthandTest
            extends TestCaseAbstract {

        private CSSParser parser;

        private final String inputCSS;
        private final String expectedCSS;
        private final String description;
        private final ShorthandOptimizer optimizer;

        public ShorthandTest(
                String inputCSS, String expectedCSS,
                String description,
                ShorthandOptimizer optimizer) {

            this.inputCSS = inputCSS;
            this.expectedCSS = expectedCSS;
            this.description = description;
            this.optimizer = optimizer;
        }

        public String getName() {
            return "ShorthandTest - " + inputCSS + "(" + description + ")";
        }

        protected void setUp() throws Exception {
            super.setUp();

            parser =
                    CSSParserFactory.getDefaultInstance().createStrictParser();
        }

        protected void runTest()
                throws Throwable {

            doRenderTest(renderer, inputCSS, expectedCSS);
        }

        private void doRenderTest(
                PropertyRenderer renderer, String inputCSS,
                String expectedCSS)
                throws Exception {

            StringWriter writer = new StringWriter();
            StyleSheetRenderer styleSheetRenderer = CSSStyleSheetRenderer.getSingleton();
            RendererContext context = new RendererContext(writer,
                    styleSheetRenderer);

            final StyleValuesMock parentValuesMock =
                    new StyleValuesMock("parentValuesMock", expectations);

            StylingFactory stylingFactory = StylingFactory.getDefaultInstance();

            final MutableStyleProperties inputProperties =
                    parser.parseDeclarations(inputCSS);
            final MutablePropertyValues inputValues = stylingFactory.createPropertyValues(
                    StylePropertyDetails.getDefinitions());
            inputProperties.iteratePropertyValues(new PropertyValueIteratee() {
                public IterationAction next(PropertyValue propertyValue) {
                    inputValues.setComputedValue(propertyValue.getProperty(),
                            propertyValue.getValue());
                    return IterationAction.CONTINUE;
                }
            });

            final MutableStyleProperties outputValues =
                ThemeFactory.getDefaultInstance().createMutableStyleProperties();

            checker.setInputValues(inputValues);

            if (normalizer != null) {
                normalizer.normalize(inputValues);
            }

//            // Create output properties and populate with the computed values.
//            PropertyDetailsSet detailsSet = StylePropertyDetails
//                    .getDefinitions().getStandardDetailsSet();
//            detailsSet.iterateStyleProperties(new StylePropertyIteratee() {
//                public IterationAction next(StyleProperty property) {
//                    StyleValue value = inputValues.getComputedValue(property);
//
//                    outputValues.setStyleValue(property, value);
//                    return IterationAction.CONTINUE;
//                }
//            });
//
            final DeviceValuesMock deviceValuesMock =
                    DeviceStylingTestHelper.createDeviceValuesMock(mockFactory,
                            expectations, DeviceValues.UNKNOWN);

            optimizer.optimize(TargetEntity.ELEMENT, inputValues, outputValues,
                    deviceValuesMock);

            renderer.render(outputValues, context);
            context.flushStyleSheet();

            String actualCSS = writer.getBuffer().toString();

            assertEquals(expectedCSS, actualCSS);
        }
    }
}
