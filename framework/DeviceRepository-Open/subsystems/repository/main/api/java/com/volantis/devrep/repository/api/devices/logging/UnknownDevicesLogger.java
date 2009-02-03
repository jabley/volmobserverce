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

import com.volantis.devrep.localization.LocalizationFactory;
import com.volantis.synergetics.localization.ExceptionLocalizer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Logger class to acces unknown/abstract devices log entries.
 */
public class UnknownDevicesLogger {

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer EXCEPTION_LOCALIZER =
        LocalizationFactory.createExceptionLocalizer(UnknownDevicesLogger.class);

    private final EntryCache cache = new EntryCache();

    private static final Map LOGGERS = new HashMap();

    /**
     * Device type for unknown devices.
     */
    public final static String DEVICE_TYPE_UNKNOWN = "unknown";

    /**
     * Device type for abstract devices.
     */
    public final static String DEVICE_TYPE_ABSTRACT = "abstract";

    /**
     * The log file.
     */
    private final File file;

    /**
     * Semaphore to indicate if writing to the log file is safe.
     */
    private final Semaphore immediateWriting;

    /**
     * The list of appended entries that are waiting to be written out to the
     * log file.
     */
    private final List suspendedEntries;

    /**
     * Returns a logger object for the specified file. If no logger is created
     * for the specified file and the file does not exist or it exists, but its
     * size is 0 then a timestamp of the current date/time is written to the
     * file.
     *
     * @param file the file the logger is needed for
     * @return the logger to be used for the specified file
     */
    public static synchronized UnknownDevicesLogger getLogger(final File file) {
        return getLogger(file, new GregorianCalendar());
    }

    /**
     * Returns a logger object for the specified file.
     *
     * @param file the file the logger is requester for
     * @param baseTime the base timestamp to be used for new files.
     * @return the logger to be used for the file
     */
    private static UnknownDevicesLogger getLogger(final File file,
                                                  final Calendar baseTime) {
        final String path;
        try {
            path = file.getCanonicalPath();
        } catch (IOException e) {
            throw new UnknownDevicesLoggerException(EXCEPTION_LOCALIZER.format(
                "error-getting-path", file.getAbsolutePath()), e);
        }
        synchronized(LOGGERS) {
            // if file doesn't exist, write time stamp
            if (!file.exists() || file.length() == 0) {
                BufferedWriter writer = null;
                try {
                    if (!file.exists()) {
                        file.getParentFile().mkdirs();
                        file.createNewFile();
                    }
                    writer = new BufferedWriter(new FileWriter(file));
                    writer.write(Long.toString(baseTime.getTimeInMillis()));
                    writer.newLine();
                } catch (IOException e) {
                    throw new UnknownDevicesLoggerException(
                        EXCEPTION_LOCALIZER.format("cannot-write-to-file",
                            file.getAbsolutePath()), e);
                } finally {
                    if (writer != null) {
                        try {
                            writer.close();
                        } catch (IOException e) {
                            throw new UnknownDevicesLoggerException(
                                EXCEPTION_LOCALIZER.format("cannot-close-writer",
                                    file.getAbsolutePath()), e);
                        }
                    }
                }

            }
            UnknownDevicesLogger logger =
                (UnknownDevicesLogger) LOGGERS.get(path);
            if (logger == null) {
                logger = new UnknownDevicesLogger(file);
                LOGGERS.put(path, logger);
            }
            return logger;
        }
    }

    /**
     * Creates a logger for the specified file.
     * @param file
     */
    private UnknownDevicesLogger(final File file) {
        this.file = file;
        // initialize cache
        EntryIterator iter = null;
        try {
            for (iter = getEntries(); iter.hasNext(); ) {
                final Entry entry = (Entry) iter.next();
                cache.add(entry);
            }
        } finally {
            if (iter != null) {
                iter.close();
            }
        }

        immediateWriting = new Semaphore();
        immediateWriting.setEnabled(true);
        suspendedEntries = new LinkedList();
    }

    /**
     * Appends the specified entry to the logs.
     *
     * @param entry the entry to append
     * @throws IOException if there is a problem appending the entry
     */
    public void appendEntry(final Entry entry) throws IOException {
        // if it is new to the cache...
        if (cache.add(entry)) {
            // synchronized on immediateWriting to avoid conflicting with delete
            synchronized(immediateWriting) {
                if (immediateWriting.isEnabled()) {
                    internalAppendEntry(entry);
                } else {
                    suspendedEntries.add(entry);
                }
            }
        }
    }

    /**
     * Appends the entry to the log file.
     * @param entry the entry to append.
     * @throws IOException if there is a problem appending the entry
     */
    private synchronized void internalAppendEntry(Entry entry) throws IOException {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(file, true)));
            writer.write(asLogEntry(entry));
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    /**
     * Returns the given entry as a log file entry.
     * @return the string representation of the entry in the log file.
     */
    private String asLogEntry(Entry entry) {
        final StringBuffer buffer = new StringBuffer(50);
        buffer.append("name:");
        buffer.append(entry.getResolvedName());
        buffer.append("\r\n");
        buffer.append("type:");
        buffer.append(entry.getDeviceType());
        buffer.append("\r\n");
        buffer.append("query:");
        buffer.append(entry.getQuery());
        buffer.append("\r\n");
        buffer.append(entry.getValue());
        buffer.append("\r\n");
        buffer.append("===\r\n");
        return buffer.toString();
    }

    /**
     * Returns an iterator over the existing log entries.
     *
     * @return the iterator
     */
    public synchronized EntryIterator getEntries() {
        try {
            return new EntryIterator(file, this);
        } catch (IOException e) {
            throw new UnknownDevicesLoggerException(EXCEPTION_LOCALIZER.format(
                "error-creating-log-entry-iterator"), e);
        }
    }

    /**
     * Deletes the given number of entries from the beginning of the log file
     * and changes the timestamp to the specified one.
     *
     * @param number the number of entries to delete
     * @param timestamp the new timestamp
     * @throws IOException if an error occurrs during the delete process
     */
    public synchronized void deleteEntries(
            final int number, final Calendar timestamp) throws IOException {
        // wait the ongoing append (if there is any) and disable the writing to
        // the log file
        synchronized(immediateWriting) {
            immediateWriting.setEnabled(false);
        }
        BufferedReader reader = null;
        BufferedWriter writer = null;
        final File tempFile;
        try {
            reader = new BufferedReader(new InputStreamReader(
                new FileInputStream(file)));
            tempFile = File.createTempFile("_unknown-devices", "del", file.getParentFile());
            writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(tempFile)));
            reader.readLine(); // skip the timestamp line
            // ignore the first 'number' entries
            for (int i = 0; i < number; i++) {
                String line = reader.readLine();
                while (!line.equals("===")) {
                    line = reader.readLine();
                }
            }
            // append the new timestamp
            writer.write(Long.toString(timestamp.getTimeInMillis()));
            writer.newLine();
            // copy the remaining entries
            for (String line = reader.readLine(); line != null;
                 line = reader.readLine()) {
                writer.write(line);
                writer.newLine();
            }
        } finally {
            if (reader != null) {
                reader.close();
            }
            if (writer != null) {
                writer.close();
            }
        }
        file.delete();
        boolean renamed = tempFile.renameTo(file);
        if (!renamed) {
            throw new UnknownDevicesLoggerException(EXCEPTION_LOCALIZER.format(
            "file-cannot-be-renamed", new String[] {tempFile.getAbsolutePath(), file.getAbsolutePath()}), null);
        }
        // write out the entries that were appended during delete
        synchronized(immediateWriting) {
            immediateWriting.setEnabled(true);
            writeSuspendedEntries();
        }
    }

    /**
     * Writes out the suspended entries.
     * @throws IOException if there is a problem with writing the entries
     */
    private void writeSuspendedEntries() throws IOException {
        // use internal append because the these elements have already been
        // added to the cache.
        for (Iterator iter = suspendedEntries.iterator(); iter.hasNext(); ) {
            internalAppendEntry((Entry) iter.next());
        }
    }

    /**
     * Returns the timestamp from the log file.
     * @return the timestamp
     * @throws IOException if there is a problem reading the timestamp
     */
    public synchronized Calendar getTimestamp() throws IOException {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(
                new FileInputStream(file)));
            final String timestampStr = reader.readLine();
            final Calendar timestamp = new GregorianCalendar();
            timestamp.setTimeInMillis(Long.parseLong(timestampStr));
            return timestamp;
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }

    /**
     * Semaphore class to store a boolean value and to be used as a lock object.
     */
    private static class Semaphore {
        private boolean enabled;

        private Semaphore() {
            enabled = false;
        }

        private boolean isEnabled() {
            return enabled;
        }

        private void setEnabled(final boolean enable) {
            enabled = enable;
        }
    }
}
