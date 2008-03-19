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

import com.volantis.synergetics.testtools.TestCaseAbstract;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.net.URL;

import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

public abstract class SAXRecorderTestAbstract extends TestCaseAbstract {

    protected SAXRecording createFragmentRecording(InputSource source)
            throws IOException, SAXException {

        return createRecordingImpl(source, false, true);
    }

    protected SAXRecording createRecording(
            InputSource source, boolean treatCharactersAsIgnorable)
            throws IOException, SAXException {

        return createRecordingImpl(source, treatCharactersAsIgnorable, false);
    }

    protected SAXRecording createRecordingImpl(
            InputSource source, boolean treatCharactersAsIgnorable,
            boolean fragment)
            throws IOException, SAXException {

        final SAXRecorder recorder = createSAXRecorder();
        ContentHandler handler = configureContentHandler(recorder, treatCharactersAsIgnorable, fragment);

        // Attach the recorder to the parser.
        XMLReader reader = new org.apache.xerces.parsers.SAXParser();
        reader.setContentHandler(handler);

        // Parse the XML document.
        reader.parse(source);

        // Get the recording.
        SAXRecording recording = recorder.getRecording();
        return recording;
    }

    /**
     * This method wraps the content handler obtained from the recorder
     * instance in a number of delegating content handlers that allow it to
     * behave as specified with the fragment and ignorable characters
     * parameters
     *
     * @param recorder the recorder instance.
     * @param treatCharactersAsIgnorable treat whitespace characters as
     * ignorable
     * @param fragment treat the XML as a fragment.
     * @return the content handler that should be written to
     */
    protected ContentHandler configureContentHandler(
        final SAXRecorder recorder,
        boolean treatCharactersAsIgnorable,
        boolean fragment) {
        ContentHandler handler = recorder.getContentHandler();

        if (treatCharactersAsIgnorable) {
            handler = new DelegatingContentHandler(handler) {
                public void characters(char ch[], int start, int length)
                        throws SAXException {
                    super.ignorableWhitespace(ch, start, length);
                }
            };
        }

        if (fragment) {
            handler = new DelegatingContentHandler(handler) {
                public void startDocument() {
                }

                public void endDocument() {
                }
            };
        }
        return handler;
    }

    /**
     * Create a SAX recorder
     *
     * @return the SAX recorder
     */
    protected SAXRecorder createSAXRecorder() {
        // Create the recorder.
        SAXRecorderFactory factory = SAXRecorderFactory.getDefaultInstance();
        SAXRecorderConfiguration configuration
                = factory.createSAXRecorderConfiguration();
        initialiseConfiguration(configuration);
        final SAXRecorder recorder = factory.createSAXRecorder(configuration);
        return recorder;
    }


    /**
     * Initialise the configuration.
     *
     * @param configuration The configuration to initialise.
     */
    protected void initialiseConfiguration(
            SAXRecorderConfiguration configuration) {
    }

    protected String readContent(URL resource) throws IOException {
        Reader ioReader = new InputStreamReader(resource.openStream(), "utf-8");
        StringBuffer buffer = new StringBuffer();
        int c;
        while ((c = ioReader.read()) != -1) {
            buffer.append((char) c);
        }

        String input = buffer.toString();
        return input;
    }

    protected InputSource createInputSource(URL resource, String input) {
        Reader stringReader = new StringReader(input);
        InputSource source = new InputSource(resource.toExternalForm());
        source.setCharacterStream(stringReader);
        return source;
    }

    protected XMLSerializer createSerializer(Writer writer) {
        OutputFormat format = new OutputFormat();
        format.setOmitXMLDeclaration(true);
        format.setLineSeparator("\n");
        format.setPreserveSpace(true);
        XMLSerializer serializer = new XMLSerializer(format);
        serializer.setOutputCharStream(writer);
        return serializer;
    }

    protected String getParentPath(URL resource) {
        String prefix = resource.toExternalForm();
        prefix = prefix.substring(0, prefix.lastIndexOf('/') + 1);
        return prefix;
    }
}
