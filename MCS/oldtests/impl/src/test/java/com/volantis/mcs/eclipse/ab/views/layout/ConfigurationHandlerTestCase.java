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

package com.volantis.mcs.eclipse.ab.views.layout;

import com.volantis.mcs.eclipse.common.ControlType;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.xml.schema.W3CSchemata;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Test the configuration handler. This class tests that:
 * <ul><li>the xml is parsed correctly</li>
 * <li>the objects created as the result of parsing are as expected (effectively
 * also testing the methods on FormatAttributesViewDetails</li>
 * </ul>
 */
public class ConfigurationHandlerTestCase extends TestCaseAbstract {

    /**
     * Constant for the XSD filename.
     */
    private static final String XSD_FILENAME =
            "format-attributes-view.xsd";

    /**
     * Constant for the XSD location.
     */
    private static final String XSD_LOCATION =
            "http://www.volantis.com/schema/format-attributes-view/v1.0/" +
                    XSD_FILENAME;

    /**
     * Constant for the XML_HEADER
     */
    private static final String XML_HEADER =
            " xmlns:xsi=\"" + W3CSchemata.XSI_NAMESPACE + "\"" +
            " xsi:noNamespaceSchemaLocation=\"" + XSD_LOCATION + "\"";

    /**
     * XML String representing all the sections and values as currently expected.
     */
    private String xml =
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
        "<formatAttributesView" + XML_HEADER + ">" +
            "<section>" +
                "<attribute name=\"name\" type=\"Text\"/>" +
                "<attribute name=\"sourceFormatName\" type=\"Text\"/>" +
                "<attribute name=\"sourceFormatType\" type=\"ReadOnlyComboViewer\">" +
                    "<selection value=\"grid\"/>" +
                    "<selection value=\"pane\"/>" +
                    "<selection value=\"form\"/>" +
                    "<selection value=\"region\"/>" +
                "</attribute>" +
                "<attribute name=\"allowReset\" type=\"CheckBox\"/>" +
                "<attribute name=\"backgroundColor\" type=\"ColorSelector\"/>" +
                "<attribute name=\"backgroundComponent\" type=\"BackgroundComponent\" supplementary=\"backgroundComponentType\"/>" +
                "<attribute name=\"width\" type=\"LayoutNumberUnits\" supplementary=\"widthUnits\"/>" +
                "<attribute name=\"height\" type=\"Text\"/>" +
                "<attribute name=\"borderWidth\" type=\"Text\"/>" +
                "<attribute name=\"cellPadding\" type=\"Text\"/>" +
                "<attribute name=\"cellSpacing\" type=\"Text\"/>" +
                "<attribute name=\"horizontalAlignment\" type=\"ReadOnlyComboViewer\">" +
                    "<selection value=\"\"/>" +
                    "<selection value=\"left\"/>" +
                    "<selection value=\"center\"/>" +
                    "<selection value=\"right\"/>" +
                "</attribute>" +
                "<attribute name=\"verticalAlignment\" type=\"ReadOnlyComboViewer\">" +
                    "<selection value=\"\"/>" +
                    "<selection value=\"top\"/>" +
                    "<selection value=\"center\"/>" +
                    "<selection value=\"bottom\"/>" +
                "</attribute>" +
                "<attribute name=\"destinationArea\" type=\"Text\"/>" +
                "<attribute name=\"optimizationLevel\" type=\"ReadOnlyComboViewer\">" +
                    "<selection value=\"\"/>" +
                    "<selection value=\"never\"/>" +
                    "<selection value=\"little-impact\"/>" +
                    "<selection value=\"always\"/>" +
                "</attribute>" +
                "<attribute name=\"filterOnKeyboardUsability\" type=\"Text\"/>" +
                "<attribute name=\"maxContentSize\" type=\"Text\"/>" +
                "<attribute name=\"linkOrder\" type=\"ReadOnlyComboViewer\">"+
                    "<selection value=\"\"/>" +
                    "<selection value=\"next-first\"/>" +
                    "<selection value=\"prev-first\"/>" +
                "</attribute>" +
                "<attribute name=\"borderColor\" type=\"ColorSelector\"/>" +
                "<attribute name=\"scrolling\" type=\"ReadOnlyComboViewer\">" +
                    "<selection value=\"\"/>" +
                    "<selection value=\"auto\"/>" +
                    "<selection value=\"always\"/>" +
                    "<selection value=\"never\"/>" +
                "</attribute>" +
                "<attribute name=\"marginHeight\" type=\"Text\"/>" +
                "<attribute name=\"marginWidth\" type=\"Text\"/>" +
                "<attribute name=\"frameBorder\" type=\"CheckBox\"/>" +
                "<attribute name=\"resizeAllowed\" type=\"CheckBox\"/>" +
            "</section>" +
            "<section titleKey=\"nextLink\">" +
                "<attribute name=\"nextLinkText\" type=\"TextDefinition\"/>" +
                "<attribute name=\"nextLinkStyleClass\" type=\"TextDefinition\"/>" +
                "<attribute name=\"nextLinkShortcut\" type=\"Text\"/>" +
                "<attribute name=\"nextLinkPosition\" type=\"ReadOnlyComboViewer\">" +
                    "<selection value=\"\"/>" +
                    "<selection value=\"before\"/>" +
                    "<selection value=\"after\"/>" +
                "</attribute>" +
            "</section>" +
            "<section titleKey=\"previousLink\">" +
                "<attribute name=\"previousLinkText\" type=\"TextDefinition\"/>" +
                "<attribute name=\"previousLinkStyleClass\" type=\"TextDefinition\"/>" +
                "<attribute name=\"previousLinkShortcut\" type=\"Text\"/>" +
                "<attribute name=\"previousLinkPosition\" type=\"ReadOnlyComboViewer\">" +
                    "<selection value=\"\"/>" +
                    "<selection value=\"before\"/>" +
                    "<selection value=\"after\"/>" +
                "</attribute>" +
            "</section>" +
            "<section titleKey=\"iterationDetail\">" +
                "<attribute name=\"cells\" type=\"ReadOnlyComboViewer\">" +
                    "<selection value=\"fixed\"/>" +
                    "<selection value=\"variable\"/>" +
                "</attribute>" +
                "<attribute name=\"cellCount\" type=\"Text\"/>" +
                "<attribute name=\"clockValues\" type=\"TimeSelector\"/>" +
            "</section>" +
            "<section titleKey=\"iterationDetail\">" +
                "<attribute name=\"indexingDirection\" type=\"ReadOnlyComboViewer\">" +
                    "<selection value=\"\"/>" +
                    "<selection value=\"across-down\"/>" +
                    "<selection value=\"down-across\"/>" +
                "</attribute>" +
                "<attribute name=\"rowCount\" type=\"CellIterations\" supplementary=\"rows\"/>" +
                "<attribute name=\"columnCount\" type=\"CellIterations\" supplementary=\"columns\"/>" +
            "</section>" +
        "</formatAttributesView>";

    /**
     * Helper method for creating the XMLReader with the validation enabled.
     * @param sections the sections list
     * @return the newly created XMLReader.
     */
    private XMLReader createValidatingReader(List sections) throws Exception {
        XMLReader reader = new com.volantis.xml.xerces.parsers.SAXParser();

        reader.setContentHandler(new ConfigurationHandler(sections));
        reader.setErrorHandler(new ConfigurationErrorHandler());

        reader.setFeature("http://xml.org/sax/features/namespaces", true);
        reader.setFeature("http://xml.org/sax/features/validation", true);
        reader.setFeature("http://apache.org/xml/features/validation/schema", true);
        reader.setFeature("http://apache.org/xml/features/validation/schema-full-checking", true);

        URL url = getClass().getResource("format-attributes-view.xsd");
        reader.setProperty("http://apache.org/xml/properties/schema/" +
                "external-noNamespaceSchemaLocation",
                url.toExternalForm());

        return reader;
    }

    /**
     * Test the creation of the format attributes view objects from an xml string.
     */
    public void testHandlerWithXSDValidation() throws Exception {
        List sections = new ArrayList();
        XMLReader reader = createValidatingReader(sections);
        ConfigurationHandler handler = (ConfigurationHandler)reader.getContentHandler();

        // Do the parsing.
        reader.parse(new InputSource(new StringReader(xml)));
//        reader.parse(new InputSource(getClass().getResourceAsStream("format-attributes-view.xml")));

        final String sectionNames[] = {
            null, "nextLink", "previousLink", "iterationDetail", "iterationDetail"
        };

        Map nameType = new HashMap();
        nameType.put("name", ControlType.TEXT);
        nameType.put("sourceFormatName", ControlType.TEXT);
        nameType.put("sourceFormatType", ControlType.READ_ONLY_COMBO_VIEWER);
        nameType.put("allowReset",  ControlType.CHECK_BOX);
        nameType.put("backgroundColor", ControlType.COLOR_SELECTOR);
        nameType.put("backgroundComponent", ControlType.BACKGROUND_COMPONENT);
        nameType.put("width", ControlType.LAYOUT_NUMBER_UNITS);
        nameType.put("height", ControlType.TEXT);
        nameType.put("borderWidth", ControlType.TEXT);
        nameType.put("cellPadding", ControlType.TEXT);
        nameType.put("cellSpacing", ControlType.TEXT);
        nameType.put("horizontalAlignment", ControlType.READ_ONLY_COMBO_VIEWER);
        nameType.put("verticalAlignment", ControlType.READ_ONLY_COMBO_VIEWER);
        nameType.put("destinationArea", ControlType.TEXT);
        nameType.put("optimizationLevel", ControlType.READ_ONLY_COMBO_VIEWER);
        nameType.put("filterOnKeyboardUsability", ControlType.TEXT);
        nameType.put("maxContentSize", ControlType.TEXT);
        nameType.put("linkOrder", ControlType.READ_ONLY_COMBO_VIEWER);
        nameType.put("borderColor", ControlType.COLOR_SELECTOR);
        nameType.put("scrolling", ControlType.READ_ONLY_COMBO_VIEWER);
        nameType.put("marginHeight", ControlType.TEXT);
        nameType.put("marginWidth", ControlType.TEXT);
        nameType.put("frameBorder", ControlType.CHECK_BOX);
        nameType.put("resizeAllowed", ControlType.CHECK_BOX);
        nameType.put("nextLinkText", ControlType.TEXT_DEFINITION);
        nameType.put("nextLinkStyleClass", ControlType.TEXT_DEFINITION);
        nameType.put("nextLinkShortcut", ControlType.TEXT);
        nameType.put("nextLinkPosition", ControlType.READ_ONLY_COMBO_VIEWER);
        nameType.put("previousLinkText", ControlType.TEXT_DEFINITION);
        nameType.put("previousLinkStyleClass", ControlType.TEXT_DEFINITION);
        nameType.put("previousLinkShortcut", ControlType.TEXT);
        nameType.put("previousLinkPosition", ControlType.READ_ONLY_COMBO_VIEWER);
        nameType.put("cells", ControlType.READ_ONLY_COMBO_VIEWER);
        nameType.put("cellCount", ControlType.TEXT);
        nameType.put("clockValues", ControlType.TIME_SELECTOR);
        nameType.put("rowStyleClasses", ControlType.STYLE_SELECTOR);
        nameType.put("columnStyleClasses", ControlType.STYLE_SELECTOR);
        nameType.put("indexingDirection", ControlType.READ_ONLY_COMBO_VIEWER);
        nameType.put("rowCount", ControlType.CELL_ITERATIONS);
        nameType.put("columnCount", ControlType.CELL_ITERATIONS);


        assertEquals("Result should match", 5, sections.size());

        // Iterate through the objects that should've been created by the
        // SAX parser and verify that their contents is as expected.
        for (int i = 0; i < sections.size(); i++) {
            SectionDetails section = (SectionDetails) sections.get(i);

            // Section title should match each title in the xml file.
            assertEquals("Result should match",
                    sectionNames[i], section.getTitle());

            String[] actual = section.getDetails().getAttributes();
            for (int j = 0; j < actual.length; j++) {
                String s = actual[j];
                assertEquals("Attribute type should match: " + s,
                        nameType.get(s), section.getDetails().getAttributeControlType(s));
            }

            // Examine the first section's attributes' values.
            if (i == 0) {
                final String attributes[] = {
                    "name","sourceFormatName","sourceFormatType","allowReset",
                    "backgroundColor","backgroundComponent","width","height",
                    "borderWidth","cellPadding","cellSpacing","horizontalAlignment",
                    "verticalAlignment","destinationArea","optimizationLevel",
                    "filterOnKeyboardUsability","maxContentSize","linkOrder",
                    "borderColor","scrolling","marginHeight","marginWidth",
                    "frameBorder","resizeAllowed"
                };

                assertEquals("Size should match", attributes.length, actual.length);

                // Attribute name should match and should be in the same order
                // as expected.
                for (int attr = 0; attr < actual.length; attr++) {
                    String attributeName = attributes[attr];
                    assertEquals("Result should match",
                            attributeName, actual[attr]);

                    FormatAttributesViewDetails details = section.getDetails();
                    // Examine the sourceFormatType for 'selections' matches.
                    if (attributeName.equals("sourceFormatType")) {
                        verifyAttribute(details, "sourceFormatType",
                                new String[] {"grid","pane","form","region"},
                                ControlType.READ_ONLY_COMBO_VIEWER);

                    } else if (attributeName.equals("sourceFormatName")) {
                        verifyAttribute(details, "sourceFormatName",
                                null,
                                ControlType.TEXT);
                    } else if (attributeName.equals("horizontalAlignment")) {
                        verifyAttribute(details, "horizontalAlignment",
                                new String[] {"", "left","center","right"},
                                ControlType.READ_ONLY_COMBO_VIEWER);
                    }
                }
            }
        }
    }


    /**
     * Test the xml validation is actually working.
     */
    public void testXMLValidationInvalidXML() throws Exception {
        XMLReader reader = createValidatingReader(new ArrayList());

        String invalidElement = "invalidElementName";
        String invalidXML =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<" + invalidElement + XML_HEADER + ">" +
                    "<section>" +
                        "<attribute name=\"name\" type=\"Text\"/>" +
                        "<attribute name=\"sourceFormatName\" type=\"Text\"/>" +
                    "</section>" +
                "</" + invalidElement + ">";

        verifyValueIsInvalid(reader, invalidXML, invalidElement);

        invalidXML =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<formatAttributesView " + XML_HEADER + ">" +
                    "<section>" +
                        "<attribute name=\"name\" type=\"Text\"/>" +
                        "<attribute name=\"sourceFormatName\" type=\"Text\" rubbish=\"\"/>" +
                    "</section>" +
                "</formatAttributesView>";
        // Do the parsing.
        verifyValueIsInvalid(reader, invalidXML, "rubbish");
    }

    /**
     * Helper method for verifying the invalid value in the xml file.
     * @param reader the XMLReader
     * @param xml the xml string
     * @param invalidValue the invalid value
     */
    private void verifyValueIsInvalid(XMLReader reader, String xml, String invalidValue)
        throws Exception {
        // Do the parsing.
        try {
            reader.parse(new InputSource(new StringReader(xml)));
            fail("Expected a SAXException");
        } catch (SAXException e) {
            String message = e.getMessage();
            assertTrue("Error message should contain invalid value: " + message,
                    message.indexOf(invalidValue) > 0);
        }
    }

    /**
     * Test the xml validation is actually working for unique attribute names.
     */
    public void testXMLValidationUniqueAttributeName() throws Exception {
        XMLReader reader = createValidatingReader(new ArrayList());

        String xml =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<formatAttributesView" + XML_HEADER + ">" +
                    "<section>" +
                        "<attribute name=\"duplicate\" type=\"Text\"/>" +
                        "<attribute name=\"duplicate\" type=\"Text\"/>" +
                    "</section>" +
                "</formatAttributesView>";

        verifyValueIsInvalid(reader, xml, "duplicate");

        // Test the uniquesness across sections (duplicate attribute names
        // should be ignored.
        xml =  "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
               "<formatAttributesView " + XML_HEADER + ">" +
                    "<section>" +
                        "<attribute name=\"sourceFormatName\" type=\"Text\"/>" +
                    "</section>" +
                    "<section>" +
                        "<attribute name=\"sourceFormatName\" type=\"Text\"/>" +
                    "</section>" +
                "</formatAttributesView>";

        // Do the parsing.
        reader = createValidatingReader(new ArrayList());
        reader.parse(new InputSource(new StringReader(xml)));
    }

    /**
     * Helper method to verify the attribute values.
     *
     * @param details            the Format Attributes View Details.
     * @param attributeName      the attribute name
     * @param expectedSelections the expected selections.
     * @param expectedType       the expected type.
     * @throws Exception
     */
    private void verifyAttribute(FormatAttributesViewDetails details,
                                 String attributeName,
                                 String[] expectedSelections,
                                 ControlType expectedType) throws Exception {

        int expectedLength = expectedSelections == null ? 0 : expectedSelections.length;

        Object[] oSelections = details.getAttributeValueSelection(attributeName);
        assertEquals("Size should match",
                expectedLength, oSelections == null ? 0 : oSelections.length);

        for (int i = 0; i < expectedLength; i++) {
            Object oSelection = oSelections[i];
            assertEquals("Selection item should match",
                    expectedSelections[i], oSelection);
        }

        // Check the type.
        assertEquals("Type should match",
                expectedType,
                details.getAttributeControlType(attributeName));

    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 13-Oct-04	5771/1	byron	VBM:2004100806 Support style classes on grids and spatial format iterators: GUI

 22-Jan-04	2540/2	byron	VBM:2003121505 Added main formats attribute page

 13-Jan-04	2483/3	byron	VBM:2003121504 Corrected javadoc and updated xml and xsd file (unique validation and removed namespace declaration) and test cases

 13-Jan-04	2483/1	byron	VBM:2003121504 Eclipse PM Layout Editor: Format Attributes View: XML Config

 ===========================================================================
*/
