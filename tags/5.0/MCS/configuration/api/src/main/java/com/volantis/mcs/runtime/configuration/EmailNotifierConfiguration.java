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
 * Configuration information for sending e-mails
 */
public class EmailNotifierConfiguration implements AlwaysEnabled {

    /**
     * True iff e-mail sending is enabled.
     */
    private boolean enabled;

    /**
     * The name/address of the SMTP host.
     */
    private String smtpHost;

    /**
     * The port number for the SMTP service.
     */
    private int smtpPort;

    /**
     * User name for the SMTP service.
     */
    private String smtpUserName;

    /**
     * Password for the SMTP service.
     */
    private String smtpPassword;

    /**
     * From address for the e-mails.
     */
    private String fromAddress;

    /**
     * From name for the e-mails.
     */
    private String fromName;

    /**
     * To address for the e-mails.
     */
    private String toAddress;

    /**
     * To name for the e-mails.
     */
    private String toName;

    /**
     * Subject line of the e-mails.
     */
    private String subject;

    /**
     * E-mail sending period, stored as String object.
     */
    private String period;

    public EmailNotifierConfiguration() {
    }

    /**
     * Turns e-mail sending on/off.
     * @param value the new value, accepted values: "enable" and "disable"
     */
    public void setEmailSending(final String value) {
        if ("enable".equals(value)) {
            enabled = true;
        } else if ("disable".equals(value)) {
            enabled = false;
        } else {
            throw new IllegalArgumentException(
                "Illegal e-mail sending value: " + value);
        }
    }
    /**
     * Returns true iff the e-mail sending is enabled.
     * @return true iff the e-mail sending is enabled.
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Enables/disables the e-mail sending.
     * @param enable the new value
     */
    public void setEnabled(final boolean enable) {
        this.enabled = enable;
    }

    /**
     * Returns the SMTP host.
     * @return the SMTP host
     */
    public String getSmtpHost() {
        return smtpHost;
    }

    /**
     * Sets the SMTP host.
     * @param smtpHost the new value
     */
    public void setSmtpHost(final String smtpHost) {
        this.smtpHost = smtpHost;
    }

    /**
     * Returns the SMTP port.
     * @return the SMTP port
     */
    public int getSmtpPort() {
        return smtpPort;
    }

    /**
     * Sets the SMTP port.
     * @param smtpPort the new value
     */
    public void setSmtpPort(final int smtpPort) {
        this.smtpPort = smtpPort;
    }

    /**
     * Returns the SMTP user name
     * @return the SMTP user name
     */
    public String getSmtpUserName() {
        return smtpUserName;
    }

    /**
     * Sets the SMTP user name
     * @param smtpUserName the new value
     */
    public void setSmtpUserName(final String smtpUserName) {
        this.smtpUserName = smtpUserName;
    }

    /**
     * Returns the SMTP password
     * @return the SMTP password
     */
    public String getSmtpPassword() {
        return smtpPassword;
    }

    /**
     * Sets the SMTP password
     * @param smtpPassword the new value
     */
    public void setSmtpPassword(final String smtpPassword) {
        this.smtpPassword = smtpPassword;
    }

    /**
     * Returns the from address
     * @return the from address
     */
    public String getFromAddress() {
        return fromAddress;
    }

    /**
     * Sets the from address
     * @param fromAddress the new value
     */
    public void setFromAddress(final String fromAddress) {
        this.fromAddress = fromAddress;
    }

    /**
     * Returns the from name
     * @return the from name
     */
    public String getFromName() {
        return fromName;
    }

    /**
     * Sets the from name
     * @param fromName the new value
     */
    public void setFromName(final String fromName) {
        this.fromName = fromName;
    }

    /**
     * Returns the to address
     * @return the to address
     */
    public String getToAddress() {
        return toAddress;
    }

    /**
     * Sets the to address
     * @param toAddress the new value
     */
    public void setToAddress(final String toAddress) {
        this.toAddress = toAddress;
    }

    /**
     * Returns the to name
     * @return the to name
     */
    public String getToName() {
        return toName;
    }

    /**
     * Sets the to name
     * @param toName the new value
     */
    public void setToName(final String toName) {
        this.toName = toName;
    }

    /**
     * Returns the subject line
     * @return the subject line
     */
    public String getSubject() {
        return subject;
    }

    /**
     * Sets the subject line
     * @param subject the new value
     */
    public void setSubject(final String subject) {
        this.subject = subject;
    }

    /**
     * Returns the period as string
     * @return the period
     */
    public String getPeriod() {
        return period;
    }

    /**
     * Sets the period as string
     * @param period the new value
     */
    public void setPeriod(final String period) {
        this.period = period;
    }
}
