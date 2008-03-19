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
package com.volantis.mcs.devices.logging;

import com.volantis.mcs.http.HttpHeaders;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.devrep.repository.api.devices.logging.UnknownDevicesLogger;
import com.volantis.devrep.repository.api.devices.logging.EntryIterator;
import com.volantis.devrep.repository.api.devices.logging.UAProfileEntry;
import com.volantis.devrep.repository.api.devices.logging.TACFACEntry;
import com.volantis.devrep.repository.api.devices.logging.TACEntry;
import com.volantis.devrep.repository.api.devices.logging.HeadersEntry;
import com.volantis.devrep.repository.api.devices.logging.Header;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Collections;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * This class is responsible for testing the behaviour of
 * {@link UnknownDevicesLogger}
 */
public class UnknownDevicesLoggerTestCase extends TestCaseAbstract {

    public void testAddAndGetEntries() throws IOException {
        final File tempFile = File.createTempFile("unknowndevices", "test.tmp");
        final UnknownDevicesLogger logger =
            UnknownDevicesLogger.getLogger(tempFile);

        final TACEntry tacEntry = new TACEntry("123456");
        logger.appendEntry(tacEntry);

        EntryIterator entries = logger.getEntries();
        assertTrue(entries.hasNext());
        assertEquals(tacEntry, entries.next());
        assertFalse(entries.hasNext());
        entries.close();

        final TACFACEntry tacfacEntry = new TACFACEntry("12345678");
        logger.appendEntry(tacfacEntry);

        entries = logger.getEntries();
        assertTrue(entries.hasNext());
        assertEquals(tacEntry, entries.next());
        assertTrue(entries.hasNext());
        assertEquals(tacfacEntry, entries.next());
        assertFalse(entries.hasNext());
        entries.close();

        final UAProfileEntry uaprofileEntry =
            new UAProfileEntry("http://test.volantis.com");
        logger.appendEntry(uaprofileEntry);

        entries = logger.getEntries();
        assertTrue(entries.hasNext());
        assertEquals(tacEntry, entries.next());
        assertTrue(entries.hasNext());
        assertEquals(tacfacEntry, entries.next());
        assertTrue(entries.hasNext());
        assertEquals(uaprofileEntry, entries.next());
        assertFalse(entries.hasNext());
        entries.close();

        final Map headers = new HashMap();
        headers.put("user-agent", Collections.singletonList("test user agent"));
        final HeadersEntry headersEntry =
            new HeadersEntry("PC", "abstract", new MapHttpHeaders(headers));
        logger.appendEntry(headersEntry);

        entries = logger.getEntries();
        assertTrue(entries.hasNext());
        assertEquals(tacEntry, entries.next());
        assertTrue(entries.hasNext());
        assertEquals(tacfacEntry, entries.next());
        assertTrue(entries.hasNext());
        assertEquals(uaprofileEntry, entries.next());
        assertTrue(entries.hasNext());
        assertEquals(headersEntry, entries.next());
        assertFalse(entries.hasNext());
        entries.close();

        final Map multiLineHeaders = new HashMap();
        multiLineHeaders.put("user-agent",
                Collections.singletonList("A test user agent header\n" +
                " which should span much more than one line \n" +
                "in order to test that multiline headers are properly supported. "));
        final HeadersEntry multiLineHeadersEntry = new HeadersEntry("PC",
                "abstract", new MapHttpHeaders(multiLineHeaders));
        logger.appendEntry(multiLineHeadersEntry);

        entries = logger.getEntries();
        assertTrue(entries.hasNext());
        assertEquals(tacEntry, entries.next());
        assertTrue(entries.hasNext());
        assertEquals(tacfacEntry, entries.next());
        assertTrue(entries.hasNext());
        assertEquals(uaprofileEntry, entries.next());
        assertTrue(entries.hasNext());
        assertEquals(headersEntry, entries.next());
        assertTrue(entries.hasNext());

        // The headers will not contain the line breaks when retrieved from
        // the log file, so we have to compare the actual text.
        Iterator inputHeaders = multiLineHeadersEntry.getHeaders();
        Header inputHeader = (Header) inputHeaders.next();
        assertFalse(inputHeaders.hasNext());

        HeadersEntry outputMultiLineHeaders = (HeadersEntry) entries.next();
        Iterator outputHeaders = outputMultiLineHeaders.getHeaders();
        Header outputHeader = (Header) outputHeaders.next();
        assertFalse(outputHeaders.hasNext());

        assertEquals(inputHeader.getName(), outputHeader.getName());

        // Remove the newline characters from the value.
        String[] inputHeaderChunks = inputHeader.getValue().split("\n");
        StringBuffer inputHeaderWithoutBreaks = new StringBuffer();
        for (int i=0; i<inputHeaderChunks.length; i++) {
            inputHeaderWithoutBreaks.append(inputHeaderChunks[i]);
        }
        assertEquals(inputHeaderWithoutBreaks.toString(),
                outputHeader.getValue());

        assertFalse(entries.hasNext());
        entries.close();
    }

    public void testDelete() throws IOException {
        final File tempFile = File.createTempFile("unknowndevices", "test.tmp");
        final UnknownDevicesLogger logger =
            UnknownDevicesLogger.getLogger(tempFile);

        final TACEntry tacEntry = new TACEntry("123456");
        logger.appendEntry(tacEntry);
        final TACFACEntry tacfacEntry = new TACFACEntry("12345678");
        logger.appendEntry(tacfacEntry);
        final UAProfileEntry uaprofileEntry =
            new UAProfileEntry("http://test.volantis.com");
        logger.appendEntry(uaprofileEntry);
        final Map headers = new HashMap();
        headers.put("user-agent", Collections.singletonList("test user agent"));
        final HeadersEntry headersEntry =
            new HeadersEntry("PC", "abstract", new MapHttpHeaders(headers));
        logger.appendEntry(headersEntry);
        final GregorianCalendar timestamp1 = new GregorianCalendar();
        logger.deleteEntries(2, timestamp1);

        EntryIterator entries = logger.getEntries();
        assertTrue(entries.hasNext());
        assertEquals(uaprofileEntry, entries.next());
        assertTrue(entries.hasNext());
        assertEquals(headersEntry, entries.next());
        assertFalse(entries.hasNext());
        entries.close();
        assertEquals(timestamp1, logger.getTimestamp());

        final GregorianCalendar timestamp2 = new GregorianCalendar();
        logger.deleteEntries(0, timestamp2);

        entries = logger.getEntries();
        assertTrue(entries.hasNext());
        assertEquals(uaprofileEntry, entries.next());
        assertTrue(entries.hasNext());
        assertEquals(headersEntry, entries.next());
        assertFalse(entries.hasNext());
        entries.close();
        assertEquals(timestamp2, logger.getTimestamp());

        final GregorianCalendar timestamp3 = new GregorianCalendar();
        logger.deleteEntries(2, timestamp3);

        entries = logger.getEntries();
        assertFalse(entries.hasNext());
        entries.close();
        assertEquals(timestamp3, logger.getTimestamp());
    }

    public void testSimultaneousReadWrite() throws IOException {
        final File tempFile = File.createTempFile("unknowndevices", "test.tmp");
        final UnknownDevicesLogger logger =
            UnknownDevicesLogger.getLogger(tempFile);

        final TACEntry tacEntry = new TACEntry("123456");
        logger.appendEntry(tacEntry);
        final TACFACEntry tacfacEntry = new TACFACEntry("12345678");
        logger.appendEntry(tacfacEntry);

        EntryIterator entries = logger.getEntries();
        assertTrue(entries.hasNext());
        assertEquals(tacEntry, entries.next());
        assertTrue(entries.hasNext());

        final UAProfileEntry uaprofileEntry =
            new UAProfileEntry("http://test.volantis.com");
        logger.appendEntry(uaprofileEntry);

        assertEquals(tacfacEntry, entries.next());
        assertTrue(entries.hasNext());
        assertEquals(uaprofileEntry, entries.next());
        assertFalse(entries.hasNext());
        entries.close();
    }

    public void testInitialTimeStamp() throws IOException {
        final GregorianCalendar start = new GregorianCalendar();
        final File tempFile = File.createTempFile("unknowndevices", "test.tmp");
        final UnknownDevicesLogger logger =
            UnknownDevicesLogger.getLogger(tempFile);
        final GregorianCalendar end = new GregorianCalendar();
        final Calendar timestamp = logger.getTimestamp();
        assertFalse(start.after(timestamp));
        assertFalse(end.before(timestamp));
    }

    private static class MapHttpHeaders implements HttpHeaders {
        private final Map baseMap;

        private MapHttpHeaders(final Map baseMap) {
            this.baseMap = baseMap;
        }

        public String getHeader(final String name) {
            final List values = (List) baseMap.get(name.toLowerCase());
            if (values == null || values.size() != 1) {
                return null;
            }
            return (String) values.get(0);
        }

        public Enumeration getHeaderNames() {
            return Collections.enumeration(baseMap.keySet());
        }

        public Enumeration getHeaders(final String name) {
            List values = (List) baseMap.get(name.toLowerCase());
            if (values == null)  {
                values = Collections.EMPTY_LIST;
            }
            return Collections.enumeration(values);
        }
    }
}
