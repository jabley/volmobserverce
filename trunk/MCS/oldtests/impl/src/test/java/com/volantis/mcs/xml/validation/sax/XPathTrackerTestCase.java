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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.xml.validation.sax;


import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.mcs.xml.xpath.XPath;
import com.volantis.xml.sax.ExtendedSAXException;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.InputSource;
import org.jdom.input.SAXHandler;
import org.jdom.Document;
import org.jdom.output.SAXOutputter;

import java.io.InputStream;
import java.io.ByteArrayInputStream;

/**
 * TestCase for the {@link XPathTracker} class and its supporting classes.
 */
public class XPathTrackerTestCase extends TestCaseAbstract {

    /**
     * This XML is used to test the {@link XPathTracker} class.
     */
    private static String testXML =
            "<countries expectedPath='' xmlns:x=\"http://www.mynamespace.com\">" +
                "<europe expectedPath='europe'>" +
                    "<country name='Britian' expectedPath='europe/country[1]'>" +
                        "<capital name='London' expectedPath='europe/country[1]/capital'>" +
                            "<attraction name='London Tower' expectedPath='europe/country[1]/capital/attraction[1]'/>" +
                            "<attraction name='London Eye' expectedPath='europe/country[1]/capital/attraction[2]'/>" +
                        "</capital>" +
                        "<majorCity name='Edinburgh' expectedPath='europe/country[1]/majorCity[1]'/>" +
                        "<majorCity name='Cardiff' expectedPath='europe/country[1]/majorCity[2]'/>" +
                        "<majorCity name='Birmingham' expectedPath='europe/country[1]/majorCity[3]'/>" +
                    "</country>" +
                    "<country name='France' expectedPath='europe/country[2]'>" +
                        "<capital name='Paris' expectedPath='europe/country[2]/capital'>" +
                            "<attraction name='Louvre' expectedPath='europe/country[2]/capital/attraction[1]'/>" +
                            "<attraction name='Euro Disney' expectedPath='europe/country[2]/capital/attraction[2]'/>" +
                        "</capital>" +
                        "<majorCity name='Lyon' expectedPath='europe/country[2]/majorCity[1]'/>" +
                        "<majorCity name='Lille' expectedPath='europe/country[2]/majorCity[2]'/>" +
                     "</country>" +
                     "<country name='Ireland' expectedPath='europe/country[3]'>" +
                        "<capital name='Dublin' expectedPath='europe/country[3]/capital'>" +
                            "<attraction name='Guiness' expectedPath='europe/country[3]/capital/attraction'/>" +
                        "</capital>" +
                        "<majorCity name='Cork' expectedPath='europe/country[3]/majorCity'/>" +
                     "</country>" +
                "</europe>" +
                "<northAmerica expectedPath='northAmerica'>" +
                    "<country name='USA' expectedPath='northAmerica/country'>" +
                        "<capital name='Washington' expectedPath='northAmerica/country/capital'>" +
                            "<attraction name='Whitehouse' expectedPath='northAmerica/country/capital/attraction[1]'/>" +
                            "<attraction name='Pentagon' expectedPath='northAmerica/country/capital/attraction[2]'/>" +
                        "</capital>" +
                        "<majorCity name='New York' expectedPath='northAmerica/country/majorCity'/>" +
                    "</country>" +
                    "<x:country name='USA' expectedPath='northAmerica/x:country'>" +
                        "<capital name='Washington' expectedPath='northAmerica/x:country/capital'>" +
                            "<attraction name='Whitehouse' expectedPath='northAmerica/x:country/capital/attraction[1]'/>" +
                            "<attraction name='Pentagon' expectedPath='northAmerica/x:country/capital/attraction[2]'/>" +
                        "</capital>" +
                        "<majorCity name='New York' expectedPath='northAmerica/x:country/majorCity'/>" +
                    "</x:country>" +
                "</northAmerica>" +
            "</countries>";


    /**
     * Method that parses some XML inorder to populate an
     * <code>XPathTracker</code> instance. After each has been processed
     * the trackers current xpath is tested against an expected xpath
     * @throws Exception if an error occurs
     */
    public void testTracker() throws Exception {
        // create a JDOM document from the textXML file
        XMLReader reader = XMLReaderFactory.createXMLReader();
        SAXHandler handler = new SAXHandler();
        reader.setContentHandler(handler);
        InputStream is = new ByteArrayInputStream(testXML.getBytes());
        reader.parse(new InputSource(is));
        Document document = handler.getDocument();

        // create an XPathTracker
        XPathTracker tracker = new XPathTracker(document.getRootElement());

        // create a SAXOutputter
        SAXOutputter outputter = new SAXOutputter();
        // Set the ContentHandler of the outputter to an instance of
        // the XPathTrakerFeeder class. The XPathTrackerFeeder class
        // ensures that the tracker receives all startElement and endElement
        // events. In addition, it also checks that the trackers current
        // xpath matches an expected xpath.
        outputter.setContentHandler(new XPathTrackerFeeder(tracker));
        // output the document
        outputter.output(document);

    }


    /**
     * A ContentHandler that updates a XPathTracker whenever a
     * {@link org.xml.sax.ContentHandler#startElement} or
     * {@link org.xml.sax.ContentHandler#endElement} is received.
     * For every startElement event this class will check that
     * the trackers current xpath is as expected. It does this
     * by looking for an "expectedPath" attribute and comparing
     * this againts the trackers actual xpath.
     */
    private class XPathTrackerFeeder extends DefaultHandler {

        /**
         * The tracker that this handler is "feeding"
         */
        private XPathTracker tracker;

        /**
         * Initializes a <code>XPathTrackerFeeder</code> instance with the
         * given parameters
         * @param tracker the {@link XPathTracker} to feed.
         */
        public XPathTrackerFeeder(XPathTracker tracker) {
            this.tracker = tracker;
        }

        // javadoc inherited
        public void startElement(String namespaceURI,
                                 String localName,
                                 String qName,
                                 Attributes attributes) throws SAXException {
            try {
                // feed the startElement to the tracker
                tracker.startElement(namespaceURI, localName, qName);
                // check that the trackers xpath is correct
                XPath path = tracker.currentXPath();
                // the expected path is provided via an "expectPath" attribute.
                String expectedPath = attributes.getValue("expectedPath");
                // get the actual path from the tracker.
                String actualPath = (path != null) ?
                        path.getExternalForm() : "";

                // check the actual path against the expected
                assertEquals("Tracker has incorrect path " + expectedPath + " " + actualPath,
                             expectedPath,
                             actualPath);
            } catch (Exception e) {
                throw new ExtendedSAXException("Unexpected xpath exception", e);
            }
        }

        // javadoc inherited
        public void endElement(String namespaceURI,
                               String localName,
                               String qName) throws SAXException {
            // inform the tracker of the endElement event.
            tracker.endElement(namespaceURI, localName, qName);
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 04-Mar-05	7294/1	geoff	VBM:2005022311 Remote Repository Exceptions

 04-Mar-05	7247/1	geoff	VBM:2005022311 Remote Repository Exceptions

 21-Dec-04	6524/1	allan	VBM:2004112610 Move xpath and xml validation out of eclipse

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 09-Dec-03	2057/1	doug	VBM:2003112803 Added SAX XSD Validation mechanism

 ===========================================================================
*/
