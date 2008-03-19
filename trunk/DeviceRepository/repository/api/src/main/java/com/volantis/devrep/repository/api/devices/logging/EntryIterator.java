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
package com.volantis.devrep.repository.api.devices.logging;

import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.devrep.localization.LocalizationFactory;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.FileInputStream;

/**
 * Iterator for log entries.
 */
public class EntryIterator implements Iterator {
    /**
     * Used for logging
     */
    private static final LogDispatcher LOGGER =
        LocalizationFactory.createLogger(EntryIterator.class);

    private Entry nextEntry;
    private final BufferedReader reader;
    /**
     * UnknownDevicesLogger to synchronize on
     */
    private final UnknownDevicesLogger logger;

    EntryIterator(final File file, final UnknownDevicesLogger logger)
            throws IOException {
        this.logger = logger;
        reader = new BufferedReader(new InputStreamReader(
            new FileInputStream(file)));
        reader.readLine(); // skip the time stamp
        readNextEntry();
    }

    /**
     * Gets the next entry.
     */
    private void readNextEntry() {
        synchronized (logger) {
            try {
                final String firstLine = reader.readLine();
                if (firstLine == null) {
                    nextEntry = null;
                } else {
                    final String name = getValue(firstLine, "name:");
                    final String type = getValue(reader.readLine(), "type:");
                    final String query = getValue(reader.readLine(), "query:");
                    if (!query.equals("headers")) {
                        nextEntry = new SimpleValueEntry(
                            name, type, query, reader.readLine());
                        reader.readLine(); // "==="
                    } else {
                        nextEntry = getHeadersEntry(name, type);
                    }
                }
            } catch (Exception e) {
                LOGGER.error("cannot-read-unknown-devices-log-file", e);
                nextEntry = null;
            }
        }
    }

    /**
     * Read multi-line header names and values into a {@link HeadersEntry}.
     *
     * @param name      of the header to read
     * @param type      of the header to read
     * @return HeadersEntry
     * @throws IOException if there was a problem reading the header
     */
    private HeadersEntry getHeadersEntry(final String name, final String type)
            throws IOException {

        final HeadersEntry headerEntry =
            new HeadersEntry(name, type, null);

        String line = reader.readLine();
        // Keep reading header name value pairs until the
        // separator is found.
        while (!line.equals("===")) {
            // Create a buffer to pass back the next line
            // (which will have to be read in order to know
            // when to stop reading the multiline value).
            StringBuffer lineBuffer = new StringBuffer();
            final String headerName = getMultilineValue(line,
                    lineBuffer, "header-name:", "header-value");

            // Make sure the next and first lines are reset.
            line = lineBuffer.toString();
            lineBuffer.delete(0, lineBuffer.length());

            final String headerValue = getMultilineValue(line,
                    lineBuffer, "header-value:", "header-name:");
            headerEntry.addHeader(
                new Header(headerName, headerValue));

            // Make sure the next line is reset.
            line = lineBuffer.toString();
        }
        return headerEntry;
    }

    /**
     * Get value from single line
     */
    private String getValue(String line, final String key) {
        if(line == null) {
            return null;
        } else {
            return line.substring(key.length());
        }
    }
    
    /**
     * Get the potentially multiline value from key to nextKey.
     *
     * @param nextLine      from which to start reading the next value
     * @param key           from which to start reading the value
     * @param nextKey       the start of the next value (i.e. the point at
     *                      which to stop reading this one)
     * @return String value
     * @throws IOException if there was a problem retrieving the value
     */
    private String getMultilineValue(String firstLine,
                                     StringBuffer nextLine,
                                     final String key,
                                     final String nextKey) throws IOException {
        
        StringBuffer fullValue = new StringBuffer("");
        String line = firstLine;

        if (line != null && line.indexOf(key) == 0) {
            // If the key is present in the first line, then append the rest of
            // the the value to the buffer.
            fullValue.append(line.substring(key.length()));
        } else {
            // If not, return null.
            return null;
        }
        
        do{
            line = reader.readLine();
            if(line != null 
                && line.length() != 0 
                && (line.indexOf(nextKey) == -1) 
                && (line.indexOf("===") == -1)) {
                // If the next line is another chunk of the multiline value,
                // then append it to the buffer.
                fullValue.append(line);
            } else {
                // Otherwise it's part of the next value, so return it.
                nextLine.append(line);
                break;
            }
        } while (line!=null);
        
        return fullValue.toString();
    }
    
    // javadoc inherited
    public void remove() {
        throw new UnsupportedOperationException("Remove is not supported");
    }

    // javadoc inherited
    public boolean hasNext() {
        return nextEntry != null;
    }

    // javadoc inherited
    public Object next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        final Entry entry = nextEntry;
        readNextEntry();
        return entry;
    }

    /**
     * Closes the iterator releasing the associated resources.
     */
    public void close() {
        try {
            reader.close();
        } catch (IOException e) {
            throw new IllegalStateException("Cannot close log file.");
        }
    }
}
