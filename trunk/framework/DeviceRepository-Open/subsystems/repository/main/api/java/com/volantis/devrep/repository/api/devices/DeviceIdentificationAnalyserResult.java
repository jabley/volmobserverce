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

import java.util.Map;
import java.util.Iterator;

/**
 * Interface to strore the result of the {@link DeviceIdentificationAnalyser}.
 */
public interface DeviceIdentificationAnalyserResult {

    /**
     * @return the number of tested devices.
     */
    public int getNumberOfDevices();

    /**
     * @return the number of devices with failed tests.
     */
    public int getNumberOfFailedDevices();

    /**
     * @return the number of tests performed
     */
    public int getNumberOfTests();

    /**
     * @return the number of test failed
     */
    public int getNumberOfFailedTests();

    /**
     * @return the iterator over the test errors, the objects returned by this
     * iterator are {@link ErrorEntry} objects.
     */
    public Iterator getErrors();

    public class ErrorEntry {
        /**
         * The expected device name.
         */
        private final String deviceName;
        /**
         * The recognised device name.
         */
        private final String recognisedName;
        /**
         * The tested user agent pattern.
         */
        private final String userAgentPattern;
        /**
         * Headers used to query the device repository.
         */
        private final Map headers;

        public ErrorEntry(final String deviceName, final String recognisedName,
                          final String userAgentPattern, final Map headers) {

            this.deviceName = deviceName;
            this.recognisedName = recognisedName;
            this.userAgentPattern = userAgentPattern;
            this.headers = headers;
        }

        /**
         * @return the expected device name
         */
        public String getDeviceName() {
            return deviceName;
        }

        /**
         * @return the device name returned by the device repository
         */
        public String getRecognisedName() {
            return recognisedName;
        }

        /**
         * @return the tested user agent pattern
         */
        public String getUserAgentPattern() {
            return userAgentPattern;
        }

        /**
         * @return headers used to query the device repository
         */
        public Map getHeaders() {
            return headers;
        }

        // javadoc inherited
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            final ErrorEntry that = (ErrorEntry) o;

            if (!deviceName.equals(that.deviceName)) return false;
            if (!headers.equals(that.headers)) return false;
            if (!recognisedName.equals(that.recognisedName)) return false;
            if (!userAgentPattern.equals(that.userAgentPattern)) return false;

            return true;
        }

        // javadoc inherited
        public int hashCode() {
            int result;
            result = deviceName.hashCode();
            result = 29 * result + recognisedName.hashCode();
            result = 29 * result + userAgentPattern.hashCode();
            result = 29 * result + headers.hashCode();
            return result;
        }
    }
}
