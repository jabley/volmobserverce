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
package com.volantis.devrep.repository.api.devices;

import com.volantis.devrep.device.api.xml.identification.HeaderPattern;
import com.volantis.devrep.device.api.xml.identification.Identification;
import com.volantis.devrep.device.api.xml.identification.IdentificationEntry;
import com.volantis.devrep.device.api.xml.identification.UserAgentPattern;
import com.volantis.devrep.localization.LocalizationFactory;
import com.volantis.devrep.repository.api.accessors.xml.DeviceRepositoryConstants;
import com.volantis.mcs.accessors.xml.ZipArchive;
import com.volantis.mcs.accessors.xml.jibx.JiBXReader;
import com.volantis.mcs.devices.Device;
import com.volantis.mcs.devices.DeviceRepository;
import com.volantis.mcs.devices.DeviceRepositoryException;
import com.volantis.mcs.http.HttpHeaders;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.shared.content.BinaryContentInput;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Tool that iterates through the identification entries and checks if the XML
 * device repository returns the correct device for the user agent and other
 * secondary header attributes.
 *
 * <p>For each of the identification entries in the device repository, it
 * generates a sample user agent string and a sample secondary header, if needed
 * and queries the device repository to see if the returned device name matches
 * the one in the identification entry. If not, and error is added to the
 * result object.</p>
 *
 * <p>Note: not thread safe</p>
 */
public class DeviceIdentificationAnalyser {
    /**
     * Used for logging.
     */
    private static final LogDispatcher LOGGER =
        LocalizationFactory.createLogger(DeviceIdentificationAnalyser.class);
    /**
     * Used for localizing exceptions
     */
    private static final ExceptionLocalizer EXCEPTION_LOCALIZER =
        LocalizationFactory.createExceptionLocalizer(
            DeviceIdentificationAnalyser.class);

    /**
     * The result of the analisys
     */
    private AnalyserResult result;

    /**
     * Runs the tests for the given mdpr file.
     *
     * @param fileName the file name for the XML repository
     * @param deviceName
     * @return the result of the tests
     * @throws RepositoryException if there are problems with loading the
     * identification entries
     * @throws com.volantis.mcs.devices.DeviceRepositoryException if the repository cannot be created or
     * read
     */
    private DeviceIdentificationAnalyserResult testDevicePatterns(
                final String fileName, final String deviceName)
            throws RepositoryException, DeviceRepositoryException {

        // load the identification object
        final Identification identification = loadIdentification(fileName);

        if (identification == null) {
            throw new RepositoryException(EXCEPTION_LOCALIZER.format(
                "device-repository-file-missing",
                DeviceRepositoryConstants.IDENTIFICATION_XML));
        }

        // create the repository
        final SimpleDeviceRepositoryFactory factory =
            new SimpleDeviceRepositoryFactory();
        final DeviceRepository repository =
            factory.createDeviceRepository("file://" + fileName, null);

        final Iterator entriesIter;
        if (deviceName == null) {
            // iterate throuogh the identification entries
            entriesIter = identification.entries();
        } else {
            // only check one entry
            final IdentificationEntry entry = identification.find(deviceName);
            entriesIter = Collections.singleton(entry).iterator();
        }
        return testDevicePatterns(repository, entriesIter);
    }

    /**
     * Runs the tests defined by the given iterator over IdentificationEntry
     * objects to test the repository.
     *
     * @param repository the repository to test
     * @param entriesIter iterator over the identification entries that will be
     * used as a basis for tests
     * @return the result of the tests
     * @throws DeviceRepositoryException if the repository cannot be read
     */
    public DeviceIdentificationAnalyserResult testDevicePatterns(
                final DeviceRepository repository, final Iterator entriesIter)
            throws DeviceRepositoryException {

        result = new AnalyserResult(repository);
        // iterate throuogh the identification entries
        while (entriesIter.hasNext()) {
            testIdentificationEntry((IdentificationEntry) entriesIter.next());
        }
        return result;
    }

    /**
     * Tests one identification entry.
     *
     * @param entry the entry to test, must not be null.
     * @throws DeviceRepositoryException if there is a problem reading the
     * device repository
     */
    private void testIdentificationEntry(final IdentificationEntry entry)
            throws DeviceRepositoryException {

        result.startDevice(entry.getDeviceName());
        // check all of the user agent patterns
        for (Iterator userAgentsIter = entry.userAgentPatterns();
             userAgentsIter.hasNext();) {
            final String userAgentPattern =
                ((UserAgentPattern) userAgentsIter.next()).getRegularExpression();
            result.startUserAgentPattern(userAgentPattern);
            // replace .* with empty string, "###" and "4"
            checkRecognisedDeviceName(entry.headerPatterns(), "");
            checkRecognisedDeviceName(entry.headerPatterns(), "###");
            checkRecognisedDeviceName(entry.headerPatterns(), "4");
            result.endUserAgentPattern();
        }
        result.endDevice();
    }

    /**
     * Checks if the recognised device name matches the current device name
     * stored in the result field.
     *
     * <p>.*'s in the user agent pattern will be resolved using the given
     * replacement string. If header patterns are present, separate tests will
     * be performed using all of the secondary headers one by one.</p>
     *
     * @param headerPatternsIter the iterator over the secondary header patterns
     * @param replacementStr the replacement string to use
     * @throws DeviceRepositoryException if there is a problem reading the
     * device repository
     */
    private void checkRecognisedDeviceName(final Iterator headerPatternsIter,
                                           final String replacementStr)
            throws DeviceRepositoryException {

        final String userAgent =
            generateSample(result.getUserAgentPattern(), replacementStr).trim();
        if (headerPatternsIter.hasNext()) {
            while (headerPatternsIter.hasNext()) {
                // try all of the header patterns
                final Map headers = new HashMap();
                headers.put("user-agent", Collections.singletonList(userAgent));
                final HeaderPattern headerPattern =
                    (HeaderPattern) headerPatternsIter.next();
                final String value = generateSample(
                    headerPattern.getRegularExpression(), replacementStr).trim();
                final String key = headerPattern.getName().toLowerCase();
                headers.put(key, Collections.singletonList(value));
                result.testWithHeaders(headers);
            }
        } else {
            final Map headers = new HashMap();
            headers.put("user-agent", Collections.singletonList(userAgent));
            result.testWithHeaders(headers);
        }
    }

    /**
     * Takes a regular expression and converts it into an example string.
     *
     * @param regExp the pattern returned from the configuration
     * @return a user agent that can be used to resolve to a device.
     */
    private String generateSample(final String regExp,
                                  final String replacementStr) {
        final StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < regExp.length(); i++) {
            final char ch = regExp.charAt(i);
            if (ch == '\\') {
                i++;
                buffer.append(regExp.charAt(i));
            } else if (ch == '.') {
                if (i < (regExp.length() - 1) && (regExp.charAt(i + 1) == '*')) {
                    i++;
                    buffer.append(replacementStr);
                } else {
                    buffer.append('X');
                }
            } else if (ch == '*') {
                // do nothing
                LOGGER.warn("asterisk-without-dot-in-pattern", regExp);
            } else if (ch == '[') {
                i++;
                char nextChar = regExp.charAt(i);
                boolean hasNextChar = true;
                if (nextChar == '\\') {
                    i++;
                    nextChar = regExp.charAt(i);
                } else if (nextChar == ']') {
                    hasNextChar = false;
                }
                if (hasNextChar) {
                    buffer.append(nextChar);
                    i++;
                    while (regExp.charAt(i) != ']') {
                        if (regExp.charAt(i) == '\\') {
                            i++;
                        }
                        i++;
                    }
                }
            } else {
                buffer.append(ch);
            }
        }
        return buffer.toString();
    }

    /**
     * Reads the identification data from the specified XML repository.
     *
     * @param zipFileName the file name for the mdpr file
     * @return the created Identification object
     * @throws RepositoryException if there is an error reading the repository
     */
    private Identification loadIdentification(final String zipFileName)
            throws RepositoryException {

        try {
            final ZipArchive archive = new ZipArchive(zipFileName);
            final InputStream stream = archive.getInputFrom(
                DeviceRepositoryConstants.IDENTIFICATION_XML);
            Object object = null;
            if (stream != null) {
                final BinaryContentInput content =
                    new BinaryContentInput(stream);
                final JiBXReader jibxReader =
                    new JiBXReader(Identification.class, null);
                object = jibxReader.read(content,
                    DeviceRepositoryConstants.IDENTIFICATION_XML);
            }
            return (Identification) object;
        } catch (IOException e) {
            throw new RepositoryException(EXCEPTION_LOCALIZER.format(
                "cannot-read-object", new Object[]{
                    Identification.class.getName(),
                    DeviceRepositoryConstants.IDENTIFICATION_XML}), e);
        }
    }

    /**
     * Runs the device identification analyser. The argument string array must
     * contain the file name to the XML device repository and optionally a
     * device name.
     *
     * <p>If device name is present, only tests for that device will be
     * performed, otherwise all of the devices found will be tested.</p>
     *
     * @param args an array with the path to the device repository and the
     * optional device name
     *
     * @throws Exception if something goes wrong.
     */
    public static void main(final String args[]) throws Exception {

        // Set logging level, change to DEBUG to enable debug messages.
        Logger.getRoot().setLevel(Level.INFO);

        BasicConfigurator.configure(
            new ConsoleAppender(new PatternLayout()));

        final String deviceName = args.length > 1? args[1]: null;

        final DeviceIdentificationAnalyserResult result =
            new DeviceIdentificationAnalyser().testDevicePatterns(
                args[0], deviceName);

        LOGGER.info("device-identification-statistics-header");
        LOGGER.info("device-identification-number-of-devices",
            new Integer(result.getNumberOfDevices()));
        LOGGER.info("device-identification-number-of-failed-devices",
            new Integer(result.getNumberOfFailedDevices()));
        LOGGER.info("device-identification-number-of-tests",
            new Integer(result.getNumberOfTests()));
        LOGGER.info("device-identification-number-of-failed-tests",
            new Integer(result.getNumberOfFailedTests()));

        for (Iterator iter = result.getErrors(); iter.hasNext();) {
            final DeviceIdentificationAnalyserResult.ErrorEntry error =
                (DeviceIdentificationAnalyserResult.ErrorEntry) iter.next();
            LOGGER.warn("device-identification-error-header");
            LOGGER.warn("device-identification-error-expected",
                error.getDeviceName());
            LOGGER.warn("device-identification-error-actual",
                error.getRecognisedName());
            LOGGER.warn("device-identification-error-user-agent",
                error.getUserAgentPattern());
            LOGGER.warn("device-identification-error-headers",
                error.getHeaders().toString());
        }
    }

    /**
     * HttpHeaders implementation using a map.
     *
     * <p>Note: keys in the map must be in lower case.</p>
     */
    private static class MapHttpHeaders implements HttpHeaders {
        private final Map baseMap;

        private MapHttpHeaders(final Map baseMap) {
            this.baseMap = baseMap;
        }

        // javadoc inherited
        public String getHeader(final String name) {
            final List values = (List) baseMap.get(name.toLowerCase());
            if (values == null || values.size() != 1) {
                return null;
            }
            return (String) values.get(0);
        }

        // javadoc inherited
        public Enumeration getHeaderNames() {
            return Collections.enumeration(baseMap.keySet());
        }

        // javadoc inherited
        public Enumeration getHeaders(final String name) {
            List values = (List) baseMap.get(name.toLowerCase());
            if (values == null) {
                values = Collections.EMPTY_LIST;
            }
            return Collections.enumeration(values);
        }
    }

    /**
     * Class to store all the important information about a test.
     *
     * <p>Used to eliminate duplicated tests.</p>
     */
    private static class TestEntry {
        /**
         * Expected name of the device.
         */
        private final String deviceName;
        /**
         * Regular expression for the user agent header.
         */
        private final String userAgentPattern;
        /**
         * Headers used to identify the device.
         */
        private final Map headers;

        public TestEntry(final String deviceName, final String userAgentPattern,
                         final Map headers) {
            this.deviceName = deviceName;
            this.userAgentPattern = userAgentPattern;
            this.headers = headers;
        }

        // javadoc inherited
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            final TestEntry testEntry = (TestEntry) o;

            if (!deviceName.equals(testEntry.deviceName)) return false;
            if (!headers.equals(testEntry.headers)) return false;
            return userAgentPattern.equals(testEntry.userAgentPattern);

        }

        // javadoc inherited
        public int hashCode() {
            int result;
            result = deviceName.hashCode();
            result = 29 * result + userAgentPattern.hashCode();
            result = 29 * result + headers.hashCode();
            return result;
        }
    }

    /**
     * Class to perform the tests and register the test results.
     */
    private static class AnalyserResult implements DeviceIdentificationAnalyserResult {
        /**
         * Set of tests performed.
         */
        private Set tests;
        /**
         * Number of tests failed.
         */
        private int testsFailed;
        /**
         * Number of devices.
         */
        private int devices;
        /**
         * Number of devices with failed tests.
         */
        private int devicesFailed;
        /**
         * True iff the current device has at least one failed test.
         */
        private boolean deviceFailed;
        /**
         * Name of the current device
         */
        private String deviceName;
        /**
         * The current user agent pattern
         */
        private String userAgentPattern;
        /**
         * The list of errors.
         */
        private List errors;
        /**
         * The repository to test.
         */
        private final DeviceRepository repository;

        public AnalyserResult(final DeviceRepository repository) {
            this.repository = repository;
            errors = new LinkedList();
            tests = new HashSet();
        }

        /**
         * Indicates the start of tests for a new device.
         * @param deviceName the name of the device, must not be null
         */
        public void startDevice(final String deviceName) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Checking device: " + deviceName);
            }
            devices++;
            this.deviceName = deviceName;
            deviceFailed = false;
        }

        /**
         * Ends the tests for the current device.
         */
        public void endDevice() {
            if (deviceName == null) {
                throw new IllegalStateException("endDevice without startDevice");
            }
            if (deviceFailed) {
                devicesFailed++;
            }
            deviceName = null;
        }

        /**
         * Starts tests for a new user agent pattern of the current device.
         *
         * @param userAgentPattern the user agent pattern to test, must not be
         * null
         */
        public void startUserAgentPattern(final String userAgentPattern) {
            this.userAgentPattern = userAgentPattern;
        }

        /**
         * Returns the current user agent pattern.
         *
         * @return the current user agent pattern
         */
        public String getUserAgentPattern() {
            return userAgentPattern;
        }

        /**
         * Ends the tests for the current user agent pattern.
         */
        public void endUserAgentPattern() {
            if (userAgentPattern == null) {
                throw new IllegalStateException(
                    "endUserAgentPattern without startUserAgentPattern");
            }
            userAgentPattern = null;
        }

        /**
         * Queries the device repository using the given headers, and checks if
         * the returned device name is the same as the current device name. If
         * the device names are different adds an error entry to the list of
         * errors.
         *
         * @param headers the headers to be used for the test
         * @throws DeviceRepositoryException if there is a problem reading the
         * device repository
         */
        public void testWithHeaders(final Map headers)
                throws DeviceRepositoryException {

            // only perform the test if this is a new test
            if (tests.add(new TestEntry(deviceName, userAgentPattern, headers))) {
                final Device device =
                    repository.getDevice(new MapHttpHeaders(headers));
                final String recognisedDeviceName = device.getName();
                if (!deviceName.equals(recognisedDeviceName)) {
                    final ErrorEntry error = new ErrorEntry(deviceName,
                        recognisedDeviceName, userAgentPattern, headers);
                    errors.add(error);
                    testsFailed++;
                    deviceFailed = true;
                }
            }
        }

        // javadoc inherited
        public int getNumberOfDevices() {
            return devices;
        }

        // javadoc inherited
        public int getNumberOfFailedDevices() {
            return devicesFailed;
        }

        // javadoc inherited
        public int getNumberOfTests() {
            return tests.size();
        }

        // javadoc inherited
        public int getNumberOfFailedTests() {
            return testsFailed;
        }

        // javadoc inherited
        public Iterator getErrors() {
            return errors.iterator();
        }
    }
}
