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
package com.volantis.mcs.dom2theme.integration.generator;

import com.volantis.mcs.dom2theme.impl.generator.DOMThemeGenerator;
import com.volantis.mcs.dom2theme.impl.generator.DefaultDOMThemeGenerator;
import com.volantis.mcs.dom2theme.impl.generator.rule.GroupRuleExtractor;
import com.volantis.mcs.dom2theme.impl.generator.rule.RuleExtractor;
import com.volantis.mcs.dom2theme.impl.generator.rule.builder.RuleBuilderFactory;
import com.volantis.mcs.dom2theme.impl.generator.rule.clazz.ClassRuleExtractor;
import com.volantis.mcs.dom2theme.impl.generator.rule.clazz.StylesClassRuleExtractorFactory;
import com.volantis.mcs.dom2theme.impl.generator.rule.clazz.StylesClassRuleExtractorFactoryImpl;
import com.volantis.mcs.dom2theme.impl.generator.rule.type.TypeRuleExtractor;
import org.apache.log4j.Category;
import org.apache.log4j.Level;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class DefaultDOMThemeGeneratorTestCase
        extends DOMThemeGeneratorTestAbstract {

    private static final Category category = Category.getInstance(
            "com.volantis.mcs.dom2theme");

    protected void setUp() throws Exception {

        super.setUp();

        // Turn up log4j *Threshold* to debug.
        // Each test still needs to set a *Level* for it's Category
        enableLog4jDebug();
    }

    // todo: resolve css parser problems?
    // - css parser cannot handle "::" at all? (can work around)

    /**
     * The standard input XML from AN004 5.9.3 Device Theme Generation
     */
    private String inputXml =
        "<body style='text-align: left'>" +
            "<p style='{font-family: sans-serif}" +
                ":first-letter {font-size: x-large; font-weight: bold; color: red}'>" +
                "<a style='{mcs-text-underline-style: solid}" +
                    ":link {color: blue}" +
                    ":visited {color: green}" +
                    ":hover:link {background-color: gray; color:purple}" +
                    ":hover:visited {background-color: gray; color:yellow}'>" +
                "</a>" +
                "<a style='mcs-text-underline-style: solid; font-size: large'/>" +
                "<a style='{mcs-text-underline-style: solid; font-size: medium}" +
                    ":link {color: blue}'>" +
                "</a>" +
                "<a style='{mcs-text-underline-style: solid; font-size: medium}" +
                    ":link {color: blue}'>" +
                "</a>" +
            "</p>" +
            "<p style='{font-family:fantasy}" +
                ":first-letter {font-size: x-large; color: green}'>" +
                "<span style='color: red'/>" +
                "<span style='color: red; font-size: large'/>" +
            "</p>" +
            "<p style='{font-family:fantasy}" +
                ":first-letter {font-size: x-large}'>" +
            "</p>" +
        "</body>";

    /**
     * The standard expected XML from AN004 5.9.3 Device Theme Generation
     */
    String expectedXml =
            "<body>" +
                "<p class='c1'>" +
                    "<a class='c2'/>" +
                    "<a class='c3'/>" +
                    "<a class='c4'/>" +
                    "<a class='c4'/>" +
                "</p>" +
                "<p class='c5'>" +
                    "<span/>" +
                    "<span class='c3'/>" +
                "</p>" +
                "<p class='c6'/>" +
            "</body>";


    public void testAN004TypeClassAndGroup() throws IOException,
            ParserConfigurationException, SAXException {

        /**
         * The standard expected theme from AN004 5.9.3 Device Theme Generation
         * note: differs by :hover:link vs :link:hover, paul says doesn't matter
         */
        String expectedTheme =
                "a {mcs-text-underline-style: solid}" +
                "body{text-align:left}" +
                "span {color: red}" +
                "p:first-letter {font-size: x-large}" +

                ".c1 {font-family: sans-serif}" +
                ".c1:first-letter {font-weight: bold; color: red}" +

                ".c2:link,.c4:link {color: blue}" +
                ".c2:visited,.c5:first-letter {color: green}" +

                ".c2:link:hover {background-color: gray; color: purple}" +
                ".c2:hover:visited {background-color: gray; color: yellow}" +

                ".c3 {font-size: large}" +

                ".c4 {font-size: medium}" +

                ".c5,.c6 {font-family: fantasy}";

        DOMThemeGenerator generator = createTypeClassRuleGenerator();
        RuleExtractor groupRuleExtractor = new GroupRuleExtractor();
        generator.addRuleExtractor(groupRuleExtractor);


        checkThemeGeneration(generator, "AN004 Type Class and Group",
                inputXml, expectedXml, expectedTheme);
    }

    public void testAN004TypeAndClassOnly() throws IOException,
            ParserConfigurationException, SAXException {

        DOMThemeGenerator generator = createTypeClassRuleGenerator();

        // We generate a theme without grouping.
        String expectedTheme =
                "a {mcs-text-underline-style: solid}" +
                "body{text-align:left}" +
                "span {color: red}" +
                "p:first-letter {font-size: x-large}" +

                ".c1 {font-family: sans-serif}" +
                ".c1:first-letter {font-weight: bold; color: red}" +

                ".c2:link {color: blue}" +
                ".c2:visited {color: green}" +
                ".c2:link:hover {background-color: gray; color: purple}" +
                ".c2:hover:visited {background-color: gray; color: yellow}" +

                ".c3 {font-size: large}" +

                ".c4 {font-size: medium}" +
                ".c4:link {color: blue}" +

                ".c5 {font-family: fantasy}" +
                ".c5:first-letter {color: green}" +

                ".c6 {font-family: fantasy}";

        checkThemeGeneration(generator, "AN004 Type and Class Only",
                inputXml, expectedXml, expectedTheme);
    }

    public void testAN004ClassOnly() throws IOException,
            ParserConfigurationException, SAXException {

        category.setLevel(Level.ALL);

        DOMThemeGenerator generator = new DefaultDOMThemeGenerator();
        RuleBuilderFactory ruleBuilderFactory = new RuleBuilderFactory();
        StylesClassRuleExtractorFactory stylesClassRuleExtractorFactory =
                new StylesClassRuleExtractorFactoryImpl(ruleBuilderFactory);
        RuleExtractor classRuleExtractor = new ClassRuleExtractor(
                stylesClassRuleExtractorFactory);
        generator.addRuleExtractor(classRuleExtractor);

        // We expect a DOM without types.
        String inputXml =
                "<body style='text-align: left'>" +
                    "<p style='{font-family: sans-serif}" +
                        ":first-letter {font-weight: bold; color: red}'>" +
                        "<a style='{text-align: left}" +
                            ":link {color: blue}" +
                            ":visited {color: green}" +
                            ":hover:link {background-color: gray; color:purple}" +
                            ":hover:visited {background-color: gray; color:yellow}'>" +
                        "</a>" +
                        "<a style='font-size: large'/>" +
                        "<a style='{font-size: medium}" +
                            ":link {color: blue}'>" +
                        "</a>" +
                        "<a style='{font-size: medium}" +
                            ":link {color: blue}'>" +
                        "</a>" +
                    "</p>" +
                    "<p style='{font-family:fantasy}" +
                        ":first-letter {color: green}'>" +
                        "<span style='text-align: left'/>" +
                        "<span style='font-size: large'/>" +
                    "</p>" +
                    "<p style='font-family:fantasy'/>" +
                "</body>";

        // We generate a theme without types or grouping.
        String expectedTheme =
                ".c1 {text-align: left}" +
                ".c2 {font-family: sans-serif}" +
                ".c2:first-letter {font-weight: bold; color: red}" +

                ".c3{text-align:left}" +
                ".c3:link {color: blue}" +
                ".c3:visited {color: green}" +
                ".c3:link:hover {background-color: gray; color: purple}" +
                ".c3:hover:visited {background-color: gray; color: yellow}" +

                ".c4 {font-size: large}" +

                ".c5 {font-size: medium}" +
                ".c5:link {color: blue}" +

                ".c6 {font-family: fantasy}" +
                ".c6:first-letter {color: green}" +

                ".c7 {font-family: fantasy}";

        /**
         * The standard expected XML from AN004 5.9.3 Device Theme Generation
         */
        String expectedXml =
                "<body class='c1'>" +
                    "<p class='c2'>" +
                        "<a class='c3'/>" +
                        "<a class='c4'/>" +
                        "<a class='c5'/>" +
                        "<a class='c5'/>" +
                    "</p>" +
                    "<p class='c6'>" +
                        "<span class='c1'/>" +
                        "<span class='c4'/>" +
                    "</p>" +
                    "<p class='c7'/>" +
                "</body>";

        checkThemeGeneration(generator, "AN004 Class Only",
                inputXml, expectedXml, expectedTheme);
    }
    /**
     * Ensure that psuedo links are processed correctly on elements for VBM
     * 2005072216.
     * @throws IOException
     * @throws ParserConfigurationException
     * @throws SAXException
     */
    public void testPseudoLinks() throws IOException,
            ParserConfigurationException, SAXException {

        DOMThemeGenerator generator = createTypeClassRuleGenerator();

        // We expect a DOM without types.
        String inputXml =
            "<body style='background-color: #ccc'>" +
                "<p  style='background-color: transparent'>" +
                        "<a  style='{background-color: transparent}" +
                            ":link {color: #30c}'/>" +
                        "<a  style='{background-color: #111}" +
                            ":link {color: #30c}'>" +
                        "</a>" +
                "</p>" +
            "</body>";

        // We generate a theme without types or grouping.
        String expectedTheme = "body{background-color:#ccc}" +
                "p{background-color:transparent}" +
                "a:link{color:#30c}" +
                ".c1{background-color:transparent}" +
                ".c2{background-color:#111}";

        String expectedXml =
                "<body><p><a class=\"c1\"/><a class=\"c2\"/></p></body>";
        checkThemeGeneration(generator, "Pseudo Links",
                inputXml, expectedXml, expectedTheme);
    }

    /**
     * Create a generator that will run the class and type rule extractors.
     * @return The DOMThemeGenerator
     */
    protected DOMThemeGenerator createTypeClassRuleGenerator() {
        category.setLevel(Level.ALL);

        DOMThemeGenerator generator = new DefaultDOMThemeGenerator();
        RuleBuilderFactory ruleBuilderFactory = new RuleBuilderFactory();
        RuleExtractor typeRuleExtractor = new TypeRuleExtractor(
                ruleBuilderFactory);
        generator.addRuleExtractor(typeRuleExtractor);
        StylesClassRuleExtractorFactory stylesClassRuleExtractorFactory =
                new StylesClassRuleExtractorFactoryImpl(ruleBuilderFactory);
        RuleExtractor classRuleExtractor = new ClassRuleExtractor(
                stylesClassRuleExtractorFactory);
        generator.addRuleExtractor(classRuleExtractor);
        return generator;
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 13-Sep-05	9496/1	pduffin	VBM:2005091211 Replaced text-decoration with individual properties

 16-Aug-05	9286/1	geoff	VBM:2005072208 Normalizing of inferrable properties does not work properly.

 09-Aug-05	9195/4	emma	VBM:2005080510 Refactoring to create StyledDOMTester

 09-Aug-05	9153/3	ianw	VBM:2005072216 Fixed rework issues

 05-Aug-05	9153/1	ianw	VBM:2005072216 Fix style normalizer issue with pseudo paths

 20-Jul-05	8668/14	geoff	VBM:2005060302 XDIMECP: Generate optimised CSS for a DOM.

 19-Jul-05	8668/12	geoff	VBM:2005060302 XDIMECP: Generate optimised CSS for a DOM.

 18-Jul-05	8668/10	geoff	VBM:2005060302 XDIMECP: Generate optimised CSS for a DOM.

 ===========================================================================
*/
