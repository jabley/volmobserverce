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
 * (c) Copyright Volantis Systems Ltd. 2006. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.synergetics.reporting.impl;

import com.volantis.synergetics.localization.LocalizationFactory;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.synergetics.reporting.Report;
import com.volantis.synergetics.reporting.ReportingException;
import com.volantis.synergetics.reporting.ReportingTransactionFactory;
import com.volantis.synergetics.reporting.DynamicReport;
import com.volantis.synergetics.reporting.config.JibxReportingConfigParser;
import com.volantis.synergetics.reporting.config.ReportingConfig;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

/**
 * This factory crates a {@link Report} instance using the {@link
 * MetricGroupProxyFactory}.
 *
 * To specify the location of the configuration file a key {@link
 * #LOCATION_KEY} must be put in the configuration and map to either a {@link
 * URL}, a {@link String} representation of a URL or an {@link InputStream}.
 */
public class DefaultReportingTransactionFactory
    implements ReportingTransactionFactory {

    /**
     * Used for logging.
     */
    private static final LogDispatcher LOGGER =
        LocalizationFactory.createLogger(
            DefaultReportingTransactionFactory.class);

    /**
     * The key used to find the location of the configuration file
     */
    public static final String LOCATION_KEY = "reporting.config.location";

    /**
     * The metric group proxy factory.
     */
    private final MetricGroupProxyFactory FACTORY;

    /**
     * Create a new ReportingTransaction instance using the parameters
     * specified in the config.
     *
     * @param config a map of configuration information
     * @throws ReportingException this indicates a fatal error occurred.
     */
    public DefaultReportingTransactionFactory(Map config)
        throws ReportingException {

        // Read the location of the config file from the config
        // load the config
        // determine which interfaces we are active for.

        Object location = config.get(LOCATION_KEY);

        InputStream is = null;
        try {
            if (location instanceof String) {
                URL url = new URL((String) location);
                is = url.openStream();
            } else if (location instanceof URL) {
                is = ((URL) location).openStream();
            } else if (location instanceof InputStream) {
                is = (InputStream) location;
            } else {
                LOGGER.warn("invalid-url", location);
                LOGGER.warn("reporting-disabled");
            }
        } catch (MalformedURLException e) {
            LOGGER.warn("invalid-url", location);
            LOGGER.warn("reporting-disabled");
        } catch (IOException e) {
            LOGGER.warn("configuration-file-not-found", location);
            LOGGER.warn("reporting-disabled");
        }

        // we either have 'null' or a URL. If we have null we know it has
        // been logged.
        ReportingConfig reportingConfig = null;
        if (is != null) {
            try {
                JibxReportingConfigParser parser =
                    new JibxReportingConfigParser();
                reportingConfig = parser.parse(is);
            } catch (RuntimeException re) {
                // this is a fatal error. Apps that rely on reporting cannot
                // continue if this occurs
                LOGGER.fatal("failed-to-parse-configuration-file", location, re);
                throw new ReportingException(
                    "failed-to-parse-configuration-file", location);
            }
        }

        FACTORY = new MetricGroupProxyFactory(reportingConfig);
    }


    // javadoc inherited
    public Report createReport(Class clazz) {
        return FACTORY.createReport(clazz);
    }

    // javadoc inherited
    public Report createReport(Class clazz, String transactionID) {
        return FACTORY.createReport(clazz, transactionID);
    }
    // javadoc inherited
    public DynamicReport createDynamicReport(String binding) {
        return FACTORY.createDynamicReport(binding);
    }

    // javadoc inherited
    public DynamicReport createDynamicReport(String binding, String transactionID) {
        return FACTORY.createDynamicReport(binding, transactionID);
    }
}
