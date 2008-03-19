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
package com.volantis.mcs.protocols;

import com.volantis.mcs.dom.Document;
import com.volantis.mcs.protocols.html.XHTMLBasicElementHelper;
import com.volantis.mcs.protocols.trans.NullRemovingDOMTransformer;
import com.volantis.mcs.protocols.trans.TransMapper;
import com.volantis.mcs.protocols.trans.TransformationConfiguration;
import com.volantis.mcs.runtime.debug.StrictStyledDOMHelper;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.synergetics.testtools.TestCaseAbstract;

public abstract class TransMapperTestAbstract extends TestCaseAbstract {

    private static final String original =
"<body>" +
    "<table style='text-align: center'>" +
        "<tr style='text-align: center; vertical-align: top'>" +
            "<td><p>first <strong>cell</strong></p></td>" +
            "<td>second cell</td>" +
        "</tr>" +
        "<tr>" +
            "<td id='qwerty'>" +
                "<table>" +
                    "<tr style='background-color: red'>" +
                        "<td>nested table</td>" +
                    "</tr>" +
                "</table>" +
                "trailing text" +
            "</td>" +
            "<td>" +
                "<table>" +
                    "<tr>" +
                        "<td>nested table 2</td>" +
                    "</tr>" +
                "</table>" +
            "</td>" +
            "<td>final cell</td>" +
        "</tr>" +
    "</table>" +
"</body>";
    private StrictStyledDOMHelper helper;

    protected void setUp() throws Exception {
        super.setUp();

        helper = new StrictStyledDOMHelper(null);
    }

    /**
     * This tests verifies that when remapping a document where all styled
     * elements have values for the style properties that have been deemed
     * 'important', these elements are not optimized away.
     *
     * @throws Exception if there was a problem parsing the XML.
     */
    public void testRemapWithImportantStyles() throws Exception {

        final String expected =
        "<body>" +
            "<div style=\"text-align: center; vertical-align: top\">" +
                "<p>first <strong>cell</strong></p>" +
            "</div>" +
            "<div style=\"text-align: center; vertical-align: top\">second cell</div>" +
            "<div id=\"qwerty\" style=\"text-align: center\">" +
                "<div style=\"background-color: red\">" +
                    "<div>nested table</div>" +
                "</div>trailing text" +
            "</div>" +
            "<div style=\"text-align: center\">nested table 2</div>" +
            "<div style=\"text-align: center\">final cell</div>" +
        "</body>";

        final TestTransformationConfiguration configuration =
                new TestTransformationConfiguration();
        configuration.addImportantProperties(StylePropertyDetails.TEXT_ALIGN);
        configuration.addImportantProperties(
                StylePropertyDetails.BACKGROUND_COLOR);

        doRemap(expected, original, configuration);
    }

    /**
     * This tests verifies that when remapping a document where none of the
     * styled elements have values for any of the 'important' style properties,
     * these elements are optimized away.
     *
     * @throws Exception if there was a problem parsing the XML.
     */
    public void testRemapWithUnimportantStyles() throws Exception {

        final String expected =
"<body>" +
    "<div style='display: block; text-align: center; vertical-align:top'>" +
        "<p>first " +
            "<strong>cell</strong>" +
        "</p>" +
    "</div>" +
    "<div style='display: block; text-align: center; vertical-align:top'>second cell</div>" +
    "<div style='display: block; text-align: center' id='qwerty'>" +
        "<div style='background-color: red; display: block'>nested table</div>" +
        "trailing text" +
    "</div>" +
    "<div style='display: block; text-align: center'>nested table 2</div>" +
    "<div style='display: block; text-align: center'>final cell</div>" +
"</body>";

        final TestTransformationConfiguration configuration =
                new TestTransformationConfiguration();
        doRemap(expected, original, configuration);
    }

    /**
     * This tests verifies that when remapping a document that has a mix of
     * styled elements (some have values for the 'important' style properties,
     * some do not), the styled elements are optimized away or not as
     * appropriate.
     *
     * @throws Exception if there was a problem parsing the XML.
     */
    public void testRemapWithMixedImportanceStyles() throws Exception {

        final String expected =
        "<body>" +
            "<div style=\"text-align: center; vertical-align: top\">" +
                "<p>first <strong>cell</strong></p>" +
            "</div>" +
            "<div style=\"text-align: center; vertical-align: top\">second cell</div>" +
            "<div id=\"qwerty\" style=\"text-align: center\">" +
                "<div style=\"background-color: red\">nested table</div>trailing text</div>" +
            "<div style=\"text-align: center\">nested table 2</div>" +
            "<div style=\"text-align: center\">final cell</div>" +
        "</body>";

        final TestTransformationConfiguration configuration =
                new TestTransformationConfiguration();
        configuration.addImportantProperties(StylePropertyDetails.VERTICAL_ALIGN);

        doRemap(expected, original, configuration);
    }

    /**
     * Convenience method which tests the remapping. Avoids code duplication.
     *
     * @param expected      String representing the expected remapped DOM
     * @param original      String representing the DOM to be remapped
     * @param configuration Used when remapping elements to determine if the
     *                      element to be remapped has any style information
     *                      that should be preserved.
     * @throws Exception if there was a problem parsing the XML
     */
    public void doRemap(String expected, String original,
            TransformationConfiguration configuration) throws Exception {

        Document dom = helper.parse(original);
        Document expectedDOM = helper.parse(expected);

        getTransMapper(configuration).remap(dom.getContentRoot(),
                XHTMLBasicElementHelper.getInstance());

        // Remove any null elements added by the remapping before rendering.
        // In normal operation this is done at the DOMTransformer level.
        // @todo I would rather pass a DOMProtocolMock than null, but mocks are
        // currently not accessible from integration tests, and StyledDOMTester
        // is not accessible from unit tests.... so move this to unit tests and
        // use a DOMProtocolMock when that becomes possible.
        NullRemovingDOMTransformer nullRemover = new NullRemovingDOMTransformer();
        nullRemover.transform(null, dom);


        String domAsString = helper.render(dom);
        String expectedDOMAsString = helper.render(expectedDOM);

        assertEquals("DOM not transformed as expected",
                expectedDOMAsString, domAsString);
    }

    /**
     * Returns the newly created {@link TransMapper} implementation that
     * should be used in this test.
     *
     * @param configuration the {@link TransformationConfiguration} to use
     * when creating the TransMapper.
     */
    public abstract TransMapper getTransMapper(
            TransformationConfiguration configuration);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 22-Aug-05	9223/1	emma	VBM:2005080403 Remove style class from within protocols and transformers

 ===========================================================================
*/
