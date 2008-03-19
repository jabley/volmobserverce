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
 * (c) Copyright Volantis Systems Ltd. 2007. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.xml.sax.recorder;

import org.apache.xml.serialize.XMLSerializer;

import java.io.IOException;
import java.io.StringWriter;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

public class MismatchedPrefixMappingsTestCase extends SAXRecorderTestAbstract {

    protected void doTestMismatchedNamespacePrefixTest(
        ContentHandlerDriver driver)
        throws IOException, SAXException, ParserConfigurationException {

        final SAXRecorder recorder = createSAXRecorder();
        ContentHandler handler = configureContentHandler(recorder,
                                                         false,
                                                         true);

        driver.driveContentHandler(handler);

        // Get the recording.
        SAXRecording recording = recorder.getRecording();
        SAXPlayer player = recording.createPlayer();
        StringWriter writer = new StringWriter();
        XMLSerializer resultSerializer = createSerializer(writer);
        ContentHandler result = resultSerializer.asContentHandler();
        player.setContentHandler(result);
        player.play();

        assertXMLEquals("XML should be equal", writer.getBuffer().toString(), driver.expectedXML());
    }


    public void testSinglePrefix() throws Exception {

        ContentHandlerDriver driver = new ContentHandlerDriver() {
            /**
             * Call this method to drive events into the specified content handler
             *
             * @param handler the handler to drive
             */
            public void driveContentHandler(ContentHandler handler)
                throws SAXException {
                handler.startPrefixMapping("a", "http://1");
                handler.startElement("http://topnamespace", "top", "top", new DummyAttributes());
                handler.characters(new char[]{' '}, 0, 1);
                handler.endElement("http://topnamespace", "top", "top");
            }

            public String expectedXML() {
                return "<top xmlns:a=\"http://1\"> </top>";
            }

        };
        doTestMismatchedNamespacePrefixTest(driver);


    }


    public interface ContentHandlerDriver {

        /**
         * Call this method to drive events into the specified content handler
         *
         * @param handler the handler to drive
         */
        public void driveContentHandler(ContentHandler handler) throws SAXException;

        /**
         * Return the XML expected
         * @return
         */
        public String expectedXML();

    }


}
