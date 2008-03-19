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

import org.xml.sax.InputSource;
import org.xml.sax.ContentHandler;

import java.net.URL;
import java.io.StringWriter;

public class SAXPlayerTestCase
 extends SAXRecorderTestAbstract {

    public void testRepeatedPlayBack()
            throws Exception {

        URL resource = getClass().getResource("xml-with-everything.xml");
        String prefix = getParentPath(resource);
        String input = readContent(resource);

        InputSource source = createInputSource(resource, input);

        SAXRecording recording = createRecording(source, false);

        final SAXPlayer player = recording.createPlayer();

        StringWriter writer = new StringWriter();
        ContentHandler outputter = new EventOutputter(writer, true, prefix);

        player.setContentHandler(outputter);
        player.play();

        String output1 = writer.toString();

        // Now use the player again.
        writer.getBuffer().setLength(0);
        player.play();

        String output2 = writer.toString();

        assertEquals(output1, output2);
    }

}
