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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.runtime.configuration.xml;

import our.apache.commons.digester.Digester;
import com.volantis.mcs.runtime.configuration.DevicesConfiguration;
import com.volantis.mcs.runtime.configuration.FileRepositoryDeviceConfiguration;
import com.volantis.mcs.runtime.configuration.JDBCRepositoryDeviceConfiguration;
import com.volantis.mcs.runtime.configuration.UnknownDevicesLoggingConfiguration;
import com.volantis.mcs.runtime.configuration.EmailNotifierConfiguration;

/**
 * Adds digester rules for the devices element and it's sub elements.
 */
public class DevicesRuleSet extends PrefixRuleSet {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    /**
     * Construct an instance of this class, using the prefix provided.
     *
     * @param prefix the prefix to add the rules to the digester under.
     */
    public DevicesRuleSet(String prefix) {
        this.prefix = prefix;
    }

    // javadoc inherited.
    public void addRuleInstances(Digester digester) {

        // <devices>
        String pattern = prefix + "/devices";
        digester.addObjectCreate(pattern, DevicesConfiguration.class);
        digester.addSetNext(pattern, "setDevices");
        digester.addSetProperties(pattern,
                 new String[] { "default" },
                 new String[] { "defaultDeviceName" }
        );

        // <devices>/<standard>
        String standardPattern = pattern + "/standard";
        addFileRepositoryRules(digester, standardPattern,
                "setStandardDeviceRepository");

        // example code to demonstrate how custom might be added in future.
//        // <devices>/<custom>
//        String customPattern = pattern + "/custom";
//        addFileRepositoryRules(digester, customPattern, 
//                "addDeviceRepository");

        // add rules for unknown/abstract logging configuration
        final String loggingPattern = pattern + "/logging";
        addLoggingRules(digester, loggingPattern);
    }

    /**
      * Add digester rules for the child elements of an individual project tag.
      *
      * @param digester the digester to add rules to.
      * @param prefix prefix to add the rules under.
      */
     private void addFileRepositoryRules(Digester digester, String prefix,
                                         String setNextMethod) {

         // <file-repository>
         String pattern = prefix + "/file-repository";
         digester.addObjectCreate(pattern,
                 FileRepositoryDeviceConfiguration.class);
         digester.addSetNext(pattern, setNextMethod);
         digester.addSetProperties(pattern,
                 new String[] { "location" },
                 new String[] { "location" }
         );

         // <jdbc-repository>
         pattern = prefix + "/jdbc-repository";
         digester.addObjectCreate(pattern,
                 JDBCRepositoryDeviceConfiguration.class);
         digester.addSetNext(pattern, setNextMethod);
         digester.addSetProperties(pattern,
                 new String[] { "project" },
                 new String[] { "project" }
         );
     }

    /**
     * Adds digester rules for unknown/abstract devices logging.
     *
     * @param digester the digester to add rules to
     * @param prefix the prefix to add the rules under
     */
    private void addLoggingRules(final Digester digester, final String prefix) {
        digester.addObjectCreate(prefix,
            UnknownDevicesLoggingConfiguration.class);
        digester.addSetNext(prefix, "setUnknownDevicesLogging");

        digester.addCallMethod(prefix + "/log-file", "setFileName", 0,
            new Class[]{String.class});

        final String emailPattern = prefix + "/e-mail";
        digester.addObjectCreate(emailPattern, EmailNotifierConfiguration.class);
        digester.addSetNext(emailPattern, "setEmailNotifier");

        digester.addCallMethod(emailPattern + "/e-mail-sending",
            "setEmailSending", 0, new Class[]{String.class});
        digester.addCallMethod(emailPattern + "/config/smtp/host",
            "setSmtpHost", 0, new Class[]{String.class});
        digester.addCallMethod(emailPattern + "/config/smtp/port",
            "setSmtpPort", 0, new Class[]{Integer.class});
        digester.addCallMethod(emailPattern + "/config/smtp/user-name",
            "setSmtpUserName", 0, new Class[]{String.class});
        digester.addCallMethod(emailPattern + "/config/smtp/password",
            "setSmtpPassword", 0, new Class[]{String.class});

        digester.addCallMethod(emailPattern + "/config/from/address",
            "setFromAddress", 0, new Class[]{String.class});
        digester.addCallMethod(emailPattern + "/config/from/name",
            "setFromName", 0, new Class[]{String.class});
        digester.addCallMethod(emailPattern + "/config/to/address",
            "setToAddress", 0, new Class[]{String.class});
        digester.addCallMethod(emailPattern + "/config/to/name", "setToName", 0,
            new Class[]{String.class});

        digester.addCallMethod(emailPattern + "/config/subject", "setSubject",
            0, new Class[]{String.class});
        digester.addCallMethod(emailPattern + "/config/period", "setPeriod", 0,
            new Class[]{String.class});
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 24-Mar-04	3482/1	geoff	VBM:2004030205 The runtime needs to support the jdbc-repository device repository configuration

 09-Mar-04	2867/1	ianw	VBM:2004012603 Rationalised data source configuration and refactored code to cope with validated config schema

 03-Mar-04	3277/1	claire	VBM:2004021606 Added devices to configuration and cli options

 ===========================================================================
*/
