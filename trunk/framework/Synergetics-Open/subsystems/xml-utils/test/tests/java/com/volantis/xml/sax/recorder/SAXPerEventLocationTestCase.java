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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.xml.sax.recorder;

import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;

import java.io.StringWriter;
import java.net.URL;

/**
 * Test that the recording of per event location information works correctly.
 */
public class SAXPerEventLocationTestCase
        extends SAXRecorderTestCase {

    /**
     * Override to make sure that all the tests in the parent class run ok
     * with per event recording turned on.
     */
    protected void initialiseConfiguration(
            SAXRecorderConfiguration configuration) {
        configuration.setRecordPerEventLocation(true);
    }

    public void testEventRecording()
        throws Exception {

        URL resource = getClass().getResource("xml-with-everything.xml");
        String prefix = getParentPath(resource);
        String input = readContent(resource);

        InputSource source = createInputSource(resource, input);

        SAXRecording recording = createRecording(source, false);

        final SAXPlayer player = recording.createPlayer();

        final StringWriter writer = new StringWriter();
        ContentHandler outputter = new EventOutputter(writer, true, prefix);

        player.setContentHandler(outputter);

        player.play();

        assertEquals("xml-with-everything.xml,1,1: startDocument\n" +
                "xml-with-everything.xml,1,54: startPrefixMapping: ->hello\n" +
                "xml-with-everything.xml,1,54: startPrefixMapping: abc->goodbye\n" +
                "xml-with-everything.xml,1,54: startElement: {hello}root/root\n" +
                "xml-with-everything.xml,2,5: characters: {\n" +
                "    }\n" +
                "xml-with-everything.xml,2,32: startElement: {hello}element/element\n" +
                "xml-with-everything.xml,2,44: characters: {some content}\n" +
                "xml-with-everything.xml,2,54: endElement: {hello}element/element\n" +
                "xml-with-everything.xml,3,5: characters: {\n" +
                "    }\n" +
                "xml-with-everything.xml,3,18: startElement: {goodbye}element/abc:element\n" +
                "xml-with-everything.xml,3,33: characters: {another element}\n" +
                "xml-with-everything.xml,3,47: endElement: {goodbye}element/abc:element\n" +
                "xml-with-everything.xml,4,5: characters: {\n" +
                "    }\n" +
                "xml-with-everything.xml,4,36: processingInstruction: processing-instruction = data\n" +
                "xml-with-everything.xml,5,1: characters: {\n" +
                "}\n" +
                "xml-with-everything.xml,5,8: endElement: {hello}root/root\n" +
                "xml-with-everything.xml,5,8: endPrefixMapping: \n" +
                "xml-with-everything.xml,5,8: endPrefixMapping: abc\n" +
                "xml-with-everything.xml,6,1: endDocument\n", writer.getBuffer().toString());
    }

    public void testEventRecordingOfFragment()
        throws Exception {

        URL resource = getClass().getResource("xml-with-everything.xml");
        String prefix = getParentPath(resource);
        String input = readContent(resource);

        InputSource source = createInputSource(resource, input);

        SAXRecording recording = createFragmentRecording(source);

        final SAXPlayer player = recording.createPlayer();

        final StringWriter writer = new StringWriter();
        ContentHandler outputter = new EventOutputter(writer, true, prefix);
        outputter.setDocumentLocator(player.getLocator());

        player.setContentHandler(outputter);

        player.play();

        assertEquals("xml-with-everything.xml,1,54: startPrefixMapping: ->hello\n" +
                "xml-with-everything.xml,1,54: startPrefixMapping: abc->goodbye\n" +
                "xml-with-everything.xml,1,54: startElement: {hello}root/root\n" +
                "xml-with-everything.xml,2,5: characters: {\n" +
                "    }\n" +
                "xml-with-everything.xml,2,32: startElement: {hello}element/element\n" +
                "xml-with-everything.xml,2,44: characters: {some content}\n" +
                "xml-with-everything.xml,2,54: endElement: {hello}element/element\n" +
                "xml-with-everything.xml,3,5: characters: {\n" +
                "    }\n" +
                "xml-with-everything.xml,3,18: startElement: {goodbye}element/abc:element\n" +
                "xml-with-everything.xml,3,33: characters: {another element}\n" +
                "xml-with-everything.xml,3,47: endElement: {goodbye}element/abc:element\n" +
                "xml-with-everything.xml,4,5: characters: {\n" +
                "    }\n" +
                "xml-with-everything.xml,4,36: processingInstruction: processing-instruction = data\n" +
                "xml-with-everything.xml,5,1: characters: {\n" +
                "}\n" +
                "xml-with-everything.xml,5,8: endElement: {hello}root/root\n" +
                "xml-with-everything.xml,5,8: endPrefixMapping: \n" +
                "xml-with-everything.xml,5,8: endPrefixMapping: abc\n",
                writer.getBuffer().toString());
    }
}
