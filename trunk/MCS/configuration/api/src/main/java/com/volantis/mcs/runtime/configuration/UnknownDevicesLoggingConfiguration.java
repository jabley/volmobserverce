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
package com.volantis.mcs.runtime.configuration;

import com.volantis.mcs.runtime.configuration.xml.digester.AlwaysEnabled;

/**
 * Configuration information for logging unknown/abstract devices and sending
 * e-mail notifications.
 */
public class UnknownDevicesLoggingConfiguration implements AlwaysEnabled {

    /**
     * Name of the log file.
     */
    private String fileName;

    /**
     * Configuration for the e-mail sending part.
     */
    private EmailNotifierConfiguration emailNotifier;

    /**
     * Returns the name of the log file.
     *
     * @return the file name
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Sets the name of the log file.
     *
     * @param fileName the name
     */
    public void setFileName(final String fileName) {
        this.fileName = fileName;
    }

    /**
     * Returns the configuration of the e-mail sending part.
     *
     * @return the configuration for the e-mail notifier
     */
    public EmailNotifierConfiguration getEmailNotifier() {
        return emailNotifier;
    }

    /**
     * Sets the configuration of the e-mail sending part.
     *
     * @param emailNotifier the configuration for sending e-mails
     */
    public void setEmailNotifier(final EmailNotifierConfiguration emailNotifier) {
        this.emailNotifier = emailNotifier;
    }
}
