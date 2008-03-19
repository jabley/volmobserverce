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
 * (c) Copyright Volantis Systems Ltd. 2007. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.map.sti.configuration.impl;

import com.volantis.map.localization.LocalizationFactory;
import com.volantis.map.sti.configuration.Configuration;
import com.volantis.synergetics.localization.ExceptionLocalizer;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Dictionary;

/**
 * Default implementation of Configuration
 * <p>
 * Reads all its values from System properties.
 */
public class DefaultConfiguration implements Configuration {
    /**
     * The name of the property containing service URL.
     */
    private final static String SERVICE_URL = "com.volantis.map.sti.ServiceURL";

    /**
     * The name of the property containing originator ID.
     */
    private final static String ORIGINATOR_ID = "com.volantis.map.sti.OriginatorID";

    /**
     * Used to localize the messages in exceptions
     */
    private static final ExceptionLocalizer EXCEPTION_LOCALIZER = LocalizationFactory
            .createExceptionLocalizer(DefaultConfiguration.class);

    /**
     * The properties provided by OSGi
     */
    private final Dictionary properties;

    /**
     * Configuraiton properties
     *
     * @param properties the properties containing the configuration information
     */
    protected DefaultConfiguration(Dictionary properties) {
        this.properties = properties;
    }

    // Javadoc inherited
    public URL getServiceURL() {
        String urlString = getConfigProperty(SERVICE_URL, true);

        try {
            return new URL(urlString);
        } catch (MalformedURLException e) {
            throw new RuntimeException(EXCEPTION_LOCALIZER.format(
                    "malformed-configuration-property-url", new Object[] {
                            SERVICE_URL, urlString }));
        }
    }

    // Javadoc inherited
    public String getOriginatorID() {
        String originatorID = getConfigProperty(ORIGINATOR_ID, false);

        if (originatorID == null) {
            originatorID = "default-originator";
        }

        return originatorID;
    }

    /**
     * Returns value of the system property. If property is missing, it returns
     * null, unless mandatory flag is true - it throws RuntimeException in that
     * case.
     * 
     * @param name property name
     * @return property value
     * @throws RuntimeException in case mandatory flag is true and property is
     *             missing.
     */
    private String getConfigProperty(String name, boolean mandatory) {
        String property = (String) properties.get(name);

        if ((property == null) && mandatory) {
            throw new RuntimeException(EXCEPTION_LOCALIZER.format(
                    "missing-mandatory-configuration-property", name));
        }

        return property;
    }
}
