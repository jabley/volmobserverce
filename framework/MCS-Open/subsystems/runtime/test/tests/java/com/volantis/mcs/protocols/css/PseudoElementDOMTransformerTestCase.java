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
package com.volantis.mcs.protocols.css;

import com.volantis.mcs.dom.Document;
import com.volantis.mcs.runtime.debug.StrictStyledDOMHelper;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

/**
 * Tests the transforms carried out by the PseudoElementDOMTransformer against
 * various pieces of XML.
 */
public class PseudoElementDOMTransformerTestCase
        extends DOMTransformerTestAbstract {

//    private static final DOMProtocol DOM_PROTOCOL;
//
//    static {
//        DOM_PROTOCOL = new DOMProtocolStub(new DefaultProtocolSupportFactory(),
//                new ProtocolConfigurationImpl());
//    }
//
    /**
     * Tests the transformer against a document containing only ::before
     * pseudo elements.
     *
     * @throws IOException                  if an IO exception occurs.
     * @throws SAXException                 if a SAX exception occurs.
     * @throws ParserConfigurationException
     */
    public void testBeforeTransform() throws IOException,
            SAXException,
            ParserConfigurationException {
        String inputXML =
                "<html>" +
                "<head/>" +
                "<body>" +
                "<div style=\"{} ::before {content:\'before__\'; color: red}\">" +
                "blah" +
                "</div>" +
                "</body>" +
                "</html>";

        String expectedXML =
                "<html>" +
                "<head/>" +
                "<body>" +
                "<div>" +
                "<span style='color: red'>before__</span>" +
                "blah" +
                "</div>" +
                "</body>" +
                "</html>";

        doTransform(inputXML,
                expectedXML);
    }

    /**
     * Tests the transformer against a document containing only :after
     * pseudo elements.
     *
     * @throws IOException                  if an IO exception occurs.
     * @throws SAXException                 if a SAX exception occurs.
     * @throws ParserConfigurationException
     */
    public void testAfterTransform() throws IOException,
            SAXException,
            ParserConfigurationException {
        String inputXML =
                "<html>" +
                "<head>" +
                "</head>" +
                "<body>" +
                "<div style='{} ::after {content:\"__after\"}'>" +
                "blah" +
                "</div>" +
                "</body>" +
                "</html>";

        String expectedXML =
                "<html>" +
                "<head>" +
                "</head>" +
                "<body>" +
                "<div>" +
                "blah" +
                "<span>__after</span>" +
                "</div>" +
                "</body>" +
                "</html>";

        doTransform(inputXML,
                expectedXML);
    }

    /**
     * Tests the transformer against a document containing ::before and :after
     * pseudo elements.
     *
     * @throws IOException                  if an IO exception occurs.
     * @throws SAXException                 if a SAX exception occurs.
     * @throws ParserConfigurationException
     */
    public void testBeforeAndAfterTransform()
            throws IOException,
            SAXException,
            ParserConfigurationException {
        String inputXML =
                "<html>" +
                "<head>" +
                "</head>" +
                "<body>" +
                "<div style='{}" +
                "            ::before {content:\"before__\"} " +
                "            ::after {content:\"__after\"}'>" +
                "blah" +
                "</div>" +
                "</body>" +
                "</html>";

        String expectedXML =
                "<html>" +
                "<head>" +
                "</head>" +
                "<body>" +
                "<div>" +
                "<span>before__</span>" +
                "blah" +
                "<span>__after</span>" +
                "</div>" +
                "</body>" +
                "</html>";

        doTransform(inputXML,
                expectedXML);
    }

    /**
     * Tests the transformer against a document containing an explicit display
     * value at block level.
     *
     * @throws IOException                  if an IO exception occurs.
     * @throws SAXException                 if a SAX exception occurs.
     * @throws ParserConfigurationException
     */
    public void testBlockDisplay() throws IOException,
            SAXException,
            ParserConfigurationException {
        String inputXML =
                "<html>" +
                "<head>" +
                "</head>" +
                "<body>" +
                "<div style='{} ::before {content:\"before__\"; " +
                "                         display:block}'>" +
                "blah" +
                "</div>" +
                "</body>" +
                "</html>";

        String expectedXML =
                "<html>" +
                "<head>" +
                "</head>" +
                "<body>" +
                "<div>" +
                "<div>before__</div>" +
                "blah" +
                "</div>" +
                "</body>" +
                "</html>";

        doTransform(inputXML,
                expectedXML);
    }

    /**
     * Tests the transformer against a document containing an explicit display
     * value at inline level.
     *
     * @throws IOException                  if an IO exception occurs.
     * @throws SAXException                 if a SAX exception occurs.
     * @throws ParserConfigurationException
     */
    public void testInlineDisplay() throws IOException,
            SAXException,
            ParserConfigurationException {
        String inputXML =
                "<html>" +
                "<head>" +
                "</head>" +
                "<body>" +
                "<div style='{} ::before {content:\"before__\"; " +
                "                         display:inline}'>" +
                "blah" +
                "</div>" +
                "</body>" +
                "</html>";

        String expectedXML =
                "<html>" +
                "<head>" +
                "</head>" +
                "<body>" +
                "<div>" +
                "<span>before__</span>" +
                "blah" +
                "</div>" +
                "</body>" +
                "</html>";

        doTransform(inputXML,
                expectedXML);
    }

    /**
     * Tests the transformer against a document containing an unsupported
     * display value.
     *
     * @throws IOException                  if an IO exception occurs.
     * @throws SAXException                 if a SAX exception occurs.
     * @throws ParserConfigurationException
     */
    public void testUnsupportedDisplayValue()
            throws IOException,
            SAXException,
            ParserConfigurationException {
        String inputXML =
                "<html>" +
                "<head>" +
                "</head>" +
                "<body>" +
                "<div style='{} ::before {content:\"before__\"; " +
                "                         display:none}'>" +
                "blah" +
                "</div>" +
                "</body>" +
                "</html>";


        try {
            doTransform(inputXML, "");
            fail("An UnsupportedOperationException should have been thrown");

        } catch (UnsupportedOperationException e) {
        }

    }

    /**
     * Do the transform using the input XML and check the results against the
     * expected XML.
     *
     * @param inputXML    the XML to build the document from.
     * @param expectedXML the XML the document should represent post-transform.
     * @throws IOException  if an IO exception occurs.
     * @throws SAXException if a SAX exception occurs.
     */
    private void doTransform(
            String inputXML,
            String expectedXML) throws
            IOException,
            SAXException,
            ParserConfigurationException {

        StrictStyledDOMHelper helper = new StrictStyledDOMHelper(null);

        Document document = helper.parse(inputXML);

        ReplacementPseudoElementFactory replacementPseudoElementFactory =
                new DefaultReplacementPseudoElementFactory(createDOMProtocol());
        PseudoElementDOMTransformer transformer =
                new PseudoElementDOMTransformer(
                        replacementPseudoElementFactory);
        transformer.transform(null,
                document);

        String actualXML = helper.render(document);
        String canonicalExpectedXML = helper.normalize(expectedXML);
        assertXMLEquals("Actual XML does not match expected XML",
                canonicalExpectedXML,
                actualXML);
    }

//    private static class DOMProtocolStub
//            extends DOMProtocol {
//
//        public DOMProtocolStub(
//                ProtocolSupportFactory supportFactory,
//                ProtocolConfiguration configuration) {
//            super(supportFactory, configuration);
//        }
//
//        public String defaultMimeType() {
//            return "application/xhtml+xml";
//        }
//    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-Dec-05	10527/3	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 05-Dec-05	10512/2	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 29-Nov-05	10505/3	pduffin	VBM:2005111405 Committing transactions from MCS 3.5.0 (7)

 29-Nov-05	10347/3	pduffin	VBM:2005111405 Massive changes for performance

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 29-Nov-05	10480/1	pduffin	VBM:2005070711 Merged changes from main trunk

 07-Nov-05	10166/1	geoff	VBM:2005102408 Backwards port: Pane style class renders layout rather than theme bgcolor

 29-Nov-05	10478/1	pduffin	VBM:2005070711 Fixed merge conflicts

 07-Nov-05	10046/1	geoff	VBM:2005102408 Post-review modifications
 05-Oct-05	9440/1	schaloner	VBM:2005070711 Added marker pseudo-element support

 29-Nov-05	10478/1	pduffin	VBM:2005070711 Fixed merge conflicts

 07-Nov-05	10046/1	geoff	VBM:2005102408 Post-review modifications
 05-Oct-05	9440/1	schaloner	VBM:2005070711 Added marker pseudo-element support

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 06-Sep-05	9413/1	schaloner	VBM:2005070406 Implemented before and after pseudo-element support

 ===========================================================================
*/
