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

package com.volantis.xml.sax.recorder;

import org.apache.xml.serialize.XMLSerializer;
import com.volantis.xml.sax.recorder.impl.SAXRecorderImpl;
import org.xml.sax.InputSource;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import java.io.StringWriter;
import java.io.ObjectOutputStream;
import java.io.ByteArrayOutputStream;
import java.net.URL;

public class SAXRecorderTestCase extends SAXRecorderTestAbstract {

    /**
     * Test a simple XML file without attributes.
     */
    public void testXMLWithoutAttributes()
            throws Exception {

        checkRecordAndPlaybackComplex("xml-without-attributes.xml");
    }

    /**
     * Test a simple XML file with attributes.
     */
    public void testXMLWithAttributes()
            throws Exception {

        checkRecordAndPlaybackComplex("xml-with-attributes.xml");
    }

    /**
     * Test a simple XML file with namespace attributes.
     */
    public void testXMLWithNamespaceAttributes()
            throws Exception {

        checkRecordAndPlaybackComplex("xml-with-namespace-attributes.xml");
    }

    /**
     * Test an XML file with a little bit of everything.
     */
    public void testXMLWithEverything()
            throws Exception {

        checkRecordAndPlaybackComplex("xml-with-everything.xml");
    }

    /**
     * Test an XML file with a little bit of everything treating characters as
     * ignoreable whitespace.
     */
    public void testXMLWithEverythingTreatingCharactersAsIgnorable()
            throws Exception {

        checkRecordAndPlayback("xml-with-everything.xml", true);
    }

    /**
     * Ensure an XML fragment (with no startDocument, endDocument or locator)
     * will record and play back properly.
     */
    public void testXMLFragment()
            throws Exception {

        checkRecordAndPlaybackFragment("xml-with-everything.xml");
    }

    private SAXRecording checkRecordAndPlaybackFragment(String resourceName)
            throws Exception {
        return checkRecordAndPlayback(resourceName, false, true);
    }

    private void checkRecordAndPlaybackComplex(String resourceName)
            throws Exception {
        SAXRecording recording = checkRecordAndPlayback(resourceName, false);
        assertTrue("Expected complex", recording.isComplex());
    }

    private SAXRecording checkRecordAndPlayback(
            String resourceName, final boolean treatCharactersAsIgnorable)
            throws Exception {
        return checkRecordAndPlayback(resourceName,
                treatCharactersAsIgnorable, false);
    }
    
    private SAXRecording checkRecordAndPlayback(
            String resourceName, final boolean treatCharactersAsIgnorable,
            boolean fragment)
            throws Exception {

        // Create the XML parser.
        URL resource = getClass().getResource(resourceName);
        String input = readContent(resource);

        InputSource source = createInputSource(resource, input);

        SAXRecording recording;
        if (fragment) {
            recording = createFragmentRecording(source);
        } else {
            recording = createRecording(source, treatCharactersAsIgnorable);
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(recording);
        oos.close();
        byte[] bytes = baos.toByteArray();
        System.out.println(bytes.length);

        // Create an XML serializer.
        StringWriter writer = new StringWriter();
        XMLSerializer serializer = createSerializer(writer);
        ContentHandler handler = serializer.asContentHandler();

        if (treatCharactersAsIgnorable) {
            handler = new DelegatingContentHandler(handler) {
                public void ignorableWhitespace(char ch[], int start, int length)
                        throws SAXException {
                    super.characters(ch, start, length);
                }
            };
        }

        // Attach the serializer to the player.
        SAXPlayer player = recording.createPlayer();
        player.setContentHandler(handler);

        // Play the recording.
        player.play();

        // Check to make sure that the resulting XML is the same as the
        // original.
        String output = writer.getBuffer().toString();

        boolean failed = true;
        try {
            assertXMLEquals("Expected output to match input",
                    input, output);
            failed = false;
        } finally {
            if (failed) {
                System.out.println("Input [" + input + "]");
                System.out.println("Output [" + output + "]");
            }
        }

        return recording;
    }

    /**
     * Test a simple real XML file.
     */
    public void testXMLWelcome()
            throws Exception {

        checkRecordAndPlaybackComplex("xml-welcome.xml");
    }

    public void testSimple()
        throws Exception {

        SAXRecorderFactory factory = SAXRecorderFactory.getDefaultInstance();
        SAXRecorderConfiguration configuration =
                factory.createSAXRecorderConfiguration();
        SAXRecorder recorder = new SAXRecorderImpl(configuration);
        char[] characters = "some text".toCharArray();
        recorder.getContentHandler().characters(
                characters, 0, characters.length);
        SAXRecording recording = recorder.getRecording();
        assertFalse(recording.isComplex());
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 14-Apr-05	1262/1	pduffin	VBM:2005041105 Added support for preparsing the pipeline template

 ===========================================================================
*/
