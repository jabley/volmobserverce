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
package com.volantis.mcs.dom2theme.integration.normalizer;

import com.volantis.mcs.dom.DOMFactory;
import com.volantis.mcs.dom.Document;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.StyledDOMTester;
import com.volantis.mcs.dom2theme.AssetResolverMock;
import com.volantis.mcs.dom2theme.ExtractorContextMock;
import com.volantis.mcs.dom2theme.extractor.PropertyDetailsSetHelper;
import com.volantis.mcs.dom2theme.impl.model.OutputStyledElement;
import com.volantis.mcs.dom2theme.impl.model.OutputStyledElementIteratee;
import com.volantis.mcs.dom2theme.impl.model.OutputStyledElementList;
import com.volantis.mcs.dom2theme.impl.model.OutputStyles;
import com.volantis.mcs.dom2theme.impl.model.PseudoStylePath;
import com.volantis.mcs.dom2theme.impl.model.PseudoStylePathIteratee;
import com.volantis.mcs.dom2theme.impl.optimizer.DefaultStyledDOMOptimizer;
import com.volantis.mcs.dom2theme.impl.optimizer.InputPropertiesOptimizer;
import com.volantis.mcs.dom2theme.impl.optimizer.PropertiesOptimizer;
import com.volantis.mcs.dom2theme.impl.optimizer.StyledDOMOptimizer;
import com.volantis.mcs.expression.PolicyExpression;
import com.volantis.mcs.expression.PolicyExpressionFactory;
import com.volantis.mcs.policies.PolicyReferenceMock;
import com.volantis.mcs.themes.MutableShorthandSet;
import com.volantis.mcs.themes.ShorthandSet;
import com.volantis.mcs.themes.StyleProperties;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.shared.iteration.IterationAction;
import com.volantis.styling.StylesBuilder;
import com.volantis.styling.StylingFactory;
import com.volantis.styling.compiler.CSSCompiler;
import com.volantis.styling.device.DeviceOutlook;
import com.volantis.styling.properties.PropertyDetailsSet;
import com.volantis.styling.properties.StyleProperty;
import com.volantis.styling.sheet.CompiledStyleSheet;
import com.volantis.testtools.mock.test.MockTestCaseAbstract;

import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.List;

import org.xml.sax.SAXException;

public class DefaultStyledDOMOptimizerTestCase
        extends MockTestCaseAbstract {

    private AssetResolverMock assetResolverMock;
    private ExtractorContextMock contextMock;

    protected void setUp() throws Exception {
        super.setUp();

        assetResolverMock = new AssetResolverMock(
                "assetResolverMock", expectations);

        contextMock = new ExtractorContextMock("contextMock", expectations);

        contextMock.expects.getAssetResolver().returns(assetResolverMock).any();
    }

    /**
     * Ensure that elements that have styles are ignored for styling purposes.
     */
    public void testIgnoreElementWithNoStyles() throws IOException,
            SAXException {

        // Create a document where e1 parent of e2 parent of e3 and where e2
        // has no styles set.
        DOMFactory factory = DOMFactory.getDefaultInstance();
        Document document = factory.createDocument();
        Element root = factory.createElement("root");
        document.addNode(root);
        Element e1 = factory.createElement("e1");
        e1.setStyles(StylesBuilder.getCompleteStyles("line-height: normal"));
        root.addTail(e1);
        Element e2 = factory.createElement("e2");
        e1.addTail(e2);
        Element e3 = factory.createElement("e3");
        e3.setStyles(StylesBuilder.getCompleteStyles("line-height: normal"));
        e2.addTail(e3);

        String expectedStyledElements =
                "[e3,]," + // has been cleared
                "[e2,]," + // no styles
                "[e1,]," + // has been cleared
                "[root,]," + // no styles
                "";

        checkNormalization(document, expectedStyledElements, Arrays.asList(
                new StyleProperty[]{
                    StylePropertyDetails.LINE_HEIGHT
                }), "");
    }

    /**
     * Ensure that elements that are for internal use only are completely
     * ignored for styling purposes.
     */
    public void testIgnoreInternalElements() throws IOException,
            SAXException {

        // line-height is inherited, and has a fixed initial value of normal.
        String inputXml =
                "<root>" +
                    "<e1 style='line-height: normal'>" +
                        "<SPECIAL>" +
                            "<e3 style='line-height: normal'/>" +
                        "</SPECIAL>" +
                    "</e1>" +
                "</root>";

        String expectedStyledElements =
                "[e3,]," + // has been cleared
                "[e1,]," + // has been cleared
                "[root,]," + // no styles
                "";

        checkNormalization(inputXml, expectedStyledElements, Arrays.asList(
                new StyleProperty[]{
                    StylePropertyDetails.LINE_HEIGHT
                }), "");
    }

    /**
     * Test that values which are inherited and are the same as initial values
     * are cleared.
     */
    public void testInheritedFixedInitial() throws IOException,
            SAXException {

        // line-height is inherited, and has a fixed initial value of normal.
        String inputXml =
                "<root>" +
                    "<e1 style='line-height: normal'/>" +
                "</root>";

        String expectedStyledElements =
                "[e1,]," + // has been cleared
                "[root,]," + // no styles
                "";

        checkNormalization(inputXml, expectedStyledElements, Arrays.asList(
                new StyleProperty[]{
                    StylePropertyDetails.LINE_HEIGHT
                }));
    }


    /**
     * Test that values which are not inherited and have been inferred and
     * are cleared if they equal their fixed initial value.
     */
    public void testInferredNotInheritedFixedInitial() throws IOException,
            SAXException {

        // clear is not inherited, and has a fixed initial value of none.
        String inputXml =
                "<root>" +
                    "<e1 style='clear: none'/>" +
                    "<e2 style='clear: left'/>" +
                "</root>";

        String expectedStyledElements =
                "[e1,]," + // clear:none has been cleared (confusing!)
                "[e2,[]{clear:left},]," +
                "[root,]," + // no styles
                "";

        checkNormalization(inputXml, expectedStyledElements, Arrays.asList(
                new StyleProperty[]{
                    StylePropertyDetails.CLEAR
                }));
    }

    // Test that values which are not inherited and have been inferred
    // are cleared if they equal their property initial value.
    public void testInferredNotInheritedPropertyInitial() throws IOException,
            SAXException {

        // border-top-color is not inherited, and has a initial value from
        // color.
        String inputXml =
                "<root>" +
                    "<e1 style='border-top-color:red; color:red'/>" +
                    "<e2 style='border-top-color:red; color:white'/>" +
                "</root>";

        String expectedStyledElements =
                "[e1,[]{color:red},]," +  // red border has been cleared
                "[e2,[]{color:white;border-top-color:red},]," +
                "[root,]," + // no styles
                "";

        checkNormalization(inputXml, expectedStyledElements, Arrays.asList(
                new StyleProperty[]{
                    StylePropertyDetails.BACKGROUND_COLOR,
                    StylePropertyDetails.COLOR,
                    StylePropertyDetails.BORDER_TOP_COLOR
                }));
    }

    /**
     * Ensure that if the device has a medium priority for a property on a
     * pseudo class that any values for that property on pseudo classes that
     * match it have an important priority.
     */
    public void testPseudoClassPropertyRequired() {

        String inputXml = "<e1 style='" +
                "{color:red} " +
                ":link {color:green} " +
                ":link:hover {color:blue}'/>";

        String expectedStyledElements =
                "[e1," +
                "[]{color:red}," +
                "[:link]{color:green !important}," +
                "[:link:hover]{color:blue !important}," +
                "],";

        checkNormalization(inputXml, expectedStyledElements, Arrays.asList(
                new StyleProperty[]{
                    StylePropertyDetails.COLOR,
                }), ":link {color: <unknown> !-medium}");
    }
    
    /**
     * Test that values which are inherited and have been inferred are cleared
     * if they match their parent's value.
     */
    public void testInferredInherited() throws IOException, SAXException {

        // Note: color is inherited, but has no (known) initial value.
        String inputXml =
                "<e1 style='color: black'>" +

                    "<e2></e2>" +

                    "<e3 style='color: black'>" +
                        "<e4 style='color: black'>" +
                        "</e4>" +
                    "</e3>" +

                    "<e5 style='color: purple'>" +
                        "<e6 style='color: purple'>" +
                        "</e6>" +
                    "</e5>" +

                    "<e7 style='color: red'>" +
                        "<e8 style='color: red'>" +
                            "<e9 style='color: yellow'>" +
                                "<e10 style='color: yellow'>" +
                                    "<e11 style='color: red'>" +
                                        "<e12 style='color: red'>" +
                                        "</e12>" +
                                    "</e11>" +
                                "</e10>" +
                            "</e9>" +
                        "</e8>" +
                    "</e7>" +

                "</e1>";

        String expectedStyledElements =

                "[e2,]," + // e2 has no styles

                "[e4,]," + // black is inferred
                "[e3,]," + // black is inferred

                "[e6,]," + // purple is inferred
                "[e5,[]{color:purple},]," +

                "[e12,]," + // red is inferred
                "[e11,[]{color:red},]," +
                "[e10,]," + // yellow is inferred
                "[e9,[]{color:yellow},]," +
                "[e8,]," + // red in inferred
                "[e7,[]{color:red},]," +

                "[e1,[]{color:black},]," +
                "";

        checkNormalization(inputXml, expectedStyledElements, Arrays.asList(
                new StyleProperty[]{
                    StylePropertyDetails.BACKGROUND_COLOR,
                    StylePropertyDetails.COLOR
                }));

    }

    /**
     * Pseudo Styleable Entities inherit values from their Styleable Entity
     * rather than their parent Pseudo Styleable Entities and that inferred
     * values are not cleared (clearing these values breaks the meaning, says
     * Paul).
     *
     * @throws IOException
     * @throws SAXException
     */
    public void testInferredInheritedPseudo() throws IOException, SAXException {

        // Note: color is inherited, but has no (known) initial value.
        String inputXml =
                "<e1 style='{color:red; font-size:small}" +
                        ":link {color:red; font-size:medium; background-image:none}" +
                        ":first-letter {color:red; font-size:large}'>" +
                    "<e2 style='{color:red; font-size:small}" +
                            ":link {color:red; font-size:medium; background-image:mcs-component-url(&apos;/fred.mimg&apos;)}" +
                            ":first-letter {color:red; font-size:large}" +
                            ":first-letter:first-line {color:green; font-size:large}'>" +
                        "<e3/>" +
                    "</e2>" +
                "</e1>";

        String expectedStyledElements =
                "[e3,]," +
                "[e2," +
                    // red and small are inferred from the parent and cleared
                    "[:first-letter]{font-size:large}," +
                    "[:first-letter:first-line]{color:green}," +
                    "[:link]{background-image:url(/fred.gif);color:red;font-size:medium}," +
                "]," +
                "[e1," +
                    "[]{color:red;font-size:small}," +
                    "[:first-letter]{font-size:large}," +
                    "[:link]{background-image:none;color:red;font-size:medium}," +
                "]," +
                "";

        final PolicyReferenceMock policyReferenceMock =
                new PolicyReferenceMock("policyReferenceMock", expectations);

        PolicyExpression policyExpression =
            PolicyExpressionFactory.getDefaultInstance().createExpression(
                "/fred.mimg");

        assetResolverMock.expects.evaluateExpression(policyExpression)
                .returns(policyReferenceMock).any();
        assetResolverMock.expects.resolveImage(policyReferenceMock)
                .returns("/fred.gif").any();

        checkNormalization(inputXml, expectedStyledElements, Arrays.asList(
                new StyleProperty[]{
                    StylePropertyDetails.BACKGROUND_IMAGE,
                    StylePropertyDetails.BACKGROUND_ATTACHMENT,
                    StylePropertyDetails.COLOR,
                    StylePropertyDetails.FONT_SIZE,
                    StylePropertyDetails.BORDER_TOP_COLOR,
                    StylePropertyDetails.BORDER_LEFT_STYLE,
                }));

    }

    private void checkNormalization(String inputXml,
            String expectedStyledElements, List indexedProperties) {
        
        checkNormalization(inputXml, expectedStyledElements,
                indexedProperties, "");
    }
    
    private void checkNormalization(
            String inputXml,
            String expectedStyledElements,
            List indexedProperties,
            final String deviceCSS) {

        StyledDOMTester styledDOMTester = new StyledDOMTester();
        Document actualXml = styledDOMTester.parseFull(inputXml);

        checkNormalization(actualXml, expectedStyledElements, indexedProperties, deviceCSS);

    }

    private void checkNormalization(
            Document actualXml, String expectedStyledElements,
            List indexedProperties,
            final String deviceCSS) {
        CSSCompiler compiler = StylingFactory.getDefaultInstance()
                .createDeviceCSSCompiler(DeviceOutlook.OPTIMISTIC);
        CompiledStyleSheet deviceStyleSheet = compiler.compile(
                new StringReader(deviceCSS), null);

        PropertyDetailsSet detailsSet =
                PropertyDetailsSetHelper.getDetailsSet(indexedProperties);

        ShorthandSet supportedShorthands = new MutableShorthandSet();

        InputPropertiesOptimizer propertiesOptimizer =
                new PropertiesOptimizer(detailsSet,
                        contextMock, supportedShorthands);

        StyledDOMOptimizer normalizer =
                new DefaultStyledDOMOptimizer(propertiesOptimizer,
                        detailsSet.getRootStyleValues(),
                        deviceStyleSheet);

        OutputStyledElementList elementList = normalizer.optimize(actualXml);
        String actualStyledElements = renderStyledElements(elementList);
        System.out.println(actualStyledElements);

        assertEquals("", expectedStyledElements, actualStyledElements);
    }

    private String renderStyledElements(OutputStyledElementList elementList) {

        final StringBuffer buffer = new StringBuffer();
        elementList.iterate(new OutputStyledElementIteratee() {
            public IterationAction next(OutputStyledElement element) {
                buffer.append(renderStyledElement(element));
                buffer.append(",");
                return IterationAction.CONTINUE;
            }
        });
        return buffer.toString();
    }

    private String renderStyledElement(OutputStyledElement element) {

        final StringBuffer buffer = new StringBuffer();
        buffer.append("[");
        buffer.append(element.getName());
        buffer.append(",");
        final OutputStyles styles = element.getStyles();
        if (styles != null) {
            styles.iterate(new PseudoStylePathIteratee() {
                public void next(PseudoStylePath pseudoPath) {
                    buffer.append(pseudoPath);
                    buffer.append("{");
                    StyleProperties properties =
                            styles.getPathProperties(pseudoPath);
                    buffer.append(properties.getStandardCSS());
                    buffer.append("},");
                }
            });
//        } else {
//            buffer.append("[STYLES=NULL]");
        }
        buffer.append("]");
        return buffer.toString();
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-Dec-05	10527/3	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 05-Dec-05	10512/1	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 29-Nov-05	10505/3	pduffin	VBM:2005111405 Committing transactions from MCS 3.5.0 (4)

 29-Nov-05	10370/2	pduffin	VBM:2005111405 Committing transactions from MCS 3.5.0 (4)

 18-Nov-05	10370/1	geoff	VBM:2005111405 interim commit

 01-Sep-05	9407/1	pduffin	VBM:2005083007 Changed MIB2_1 and Netfront3 configuration to remove device specific theme, and replaced it with a new initial value finder that is device aware

 18-Aug-05	9007/2	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 16-Aug-05	9286/1	geoff	VBM:2005072208 Normalizing of inferrable properties does not work properly.

 09-Aug-05	9195/2	emma	VBM:2005080510 Refactoring to create StyledDOMTester

 19-Jul-05	8668/8	geoff	VBM:2005060302 XDIMECP: Generate optimised CSS for a DOM.

 18-Jul-05	8668/6	geoff	VBM:2005060302 XDIMECP: Generate optimised CSS for a DOM.

 ===========================================================================
*/
