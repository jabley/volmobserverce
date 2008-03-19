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

import org.apache.xml.serialize.XMLSerializer;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URL;

/**
 * Ensures that flow control works.
 */
public class FlowControlTestCase
        extends SAXRecorderTestAbstract {

    private TestFlow flow;

    protected void setUp() throws Exception {
        super.setUp();

        flow = new TestFlow();
    }

    public void testExitElement() throws Exception {

        checkFlowControl("xml-exit-element.xml",
                "<root>\n" +
                "    <a z=\"1\">Text1\n" +
                "        Text5\n" +
                "    </a>Text6\n" +
                "    <d v=\"5\">Text7</d>\n" +
                "</root>");

    }

    public void testExitTwoElements() throws Exception {

        checkFlowControl("xml-exit-two-elements.xml",
                "<root>\n" +
                "    <a>Text1\n" +
                "        Text5\n" +
                "    </a>Text6\n" +
                "    Text10\n" +
                "</root>");
    }

    public void testExitSiblingElements() throws Exception {

        checkFlowControl("xml-exit-sibling-elements.xml",
                "<root>\n" +
                "    <a>Text1\n" +
                "        <exit-sibling xmlns:a=\"a\">Text2\n" +
                "            <a:b>Text3\n" +
                "                <c/>\n" +
                "            </a:b>Text4\n" +
                "        </exit-sibling></a>\n" +
                "</root>");
    }

    public void testExitTwoLevels() throws Exception {

        checkFlowControl("xml-exit-two-levels.xml",
                "<root>\n" +
                "    <a>Text1\n" +
                "        <exit2 xmlns:a=\"a\">Text2\n" +
                "            <a:b>Text3\n" +
                "                <c/>\n" +
                "            </a:b>Text4\n" +
                "        </exit2></a></root>");
    }

    public void testPrefixMappings() throws Exception {

        checkFlowControl("xml-prefix-mappings.xml",
                "startDocument\n" +
                "startElement: {}root/root\n" +
                "characters: {\n" +
                "    }\n" +
                "startElement: {}a/a\n" +
                "characters: {Text1\n" +
                "        }\n" +
                "startPrefixMapping: a->a\n" +
                "startElement: {}exit-sibling/exit-sibling\n" +
                "characters: {Text2\n" +
                "            }\n" +
                "startElement: {a}b/a:b\n" +
                "characters: {Text3\n" +
                "                }\n" +
                "startElement: {}c/c\n" +
                "endElement: {}c/c\n" +
                "characters: {\n" +
                "            }\n" +
                "endElement: {a}b/a:b\n" +
                "characters: {Text4\n" +
                "        }\n" +
                "endElement: {}exit-sibling/exit-sibling\n" +
                "endPrefixMapping: a\n" +
                "endElement: {}a/a\n" +
                "characters: {\n" +
                "}\n" +
                "endElement: {}root/root\n" +
                "endDocument\n", new OutputterFactory() {
                    public ContentHandler asContentHandler(Writer writer) {
                        return new EventOutputter(writer);
                    }
                });
    }

    private void checkFlowControl(
            String resourceName, final String expectedOutput)
            throws IOException,
            SAXException {

        OutputterFactory factory = new OutputterFactory() {
            public ContentHandler asContentHandler(Writer writer)
                    throws IOException {
                XMLSerializer serializer = createSerializer(writer);

                return serializer.asContentHandler();
            }
        };
        checkFlowControl(resourceName, expectedOutput, factory);
    }

    private void checkFlowControl(
            String resourceName, final String expectedOutput,
            final OutputterFactory factory) throws IOException,
            SAXException {
        URL resource = getClass().getResource(resourceName);
        String input = readContent(resource);

        InputSource source = createInputSource(resource, input);

        SAXRecording recording = createRecording(source, false);

        final SAXPlayer player = recording.createPlayer();
        player.setFlowController(flow);

        final StringWriter writer = new StringWriter();
        ContentHandler outputter = factory.asContentHandler(writer);

        ContentHandler contentHandler = new DelegatingContentHandler(outputter) {
            public void startElement(
                    String uri, String localName, String qName,
                    Attributes attributes)
                    throws SAXException {
                if (localName.equals("exit")) {
                    flow.setLevelsToExit(1);
                } else {
                    super.startElement(uri, localName, qName, attributes);
                }
            }

            public void endElement(
                    String namespaceURI, String localName, String qName)
                    throws SAXException {
                if (localName.equals("exit")) {
                    // Nothing to do.
                } else if (localName.equals("exit-sibling")) {
                    super.endElement(namespaceURI, localName, qName);
                    flow.setLevelsToExit(1);
                } else if (localName.equals("exit2")) {
                    super.endElement(namespaceURI, localName, qName);
                    flow.setLevelsToExit(2);
                } else {
                    super.endElement(namespaceURI, localName, qName);
                }
            }
        };
        player.setContentHandler(contentHandler);

        player.play();

        assertEquals(expectedOutput, writer.getBuffer().toString());
    }

    private static class TestFlow
            implements FlowController {
        private int levelsToExit;

        public void setLevelsToExit(int levelsToExit) {
            this.levelsToExit = levelsToExit;
        }

        public boolean exitCurrentLevel() {
            if (levelsToExit > 0) {
                levelsToExit -= 1;
                return true;
            } else {
                return false;
            }
        }
    }
}
