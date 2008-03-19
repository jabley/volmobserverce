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
package com.volantis.synergetics.reporting.impl.osgi;

import com.volantis.shared.time.DateFormats;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.localization.LocalizationFactory;
import com.volantis.synergetics.reporting.impl.DefaultMetrics;

import java.util.Date;
import java.util.Dictionary;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.event.Event;

/**
 * A handler that writes to a log file using log4j.
 */
public class Log4JReportHandler implements ReportEventHandler {

    /**
     * Used to localize the messages in exceptions
     */
    private static final ExceptionLocalizer EXCEPTION_LOCALIZER =
        LocalizationFactory.createExceptionLocalizer(Log4JReportHandler.class);

    /**
     * The prefix for configuration properties for the log4j report handler
     */
    public static final String CONFIG_PROPERTIES_PREFIX =
        DefaultMetrics.REPORT_PREFIX + "log4j";

    /**
     * The key for the property containing the logger
     */
    public static final String LOGGER_NAME_PROPERTY =
        CONFIG_PROPERTIES_PREFIX + ".logger";

    /**
     * The key for the property containing the message format string
     */
    public static final String MESSAGE_FORMAT_PROPERTY =
        CONFIG_PROPERTIES_PREFIX + ".message-format";

    /**
     * The pattern used to substitute values into the pattern
     */
    private static final Pattern PATTERN = Pattern.compile("(.*?)\\{(.*?)}");

    /**
     * Lock object.
     */
    private final Object LOCK = new Object();

    /**
     * The logger instance to write to
     */
    private Logger logger = null;

    /**
     * The format string supplied
     */
    private String format = null;

    /**
     * Standard constructor used to create the report handler.
     *
     * @param configuration the Dictionary containing configuration information
     *                      for this event handler.
     */
    Log4JReportHandler(Dictionary configuration)
        throws ConfigurationException {
        setConfiguration(configuration);
    }

    /**
     * Configure this event listener.
     *
     * @param configuration The dictionary containing configuration
     *                      information
     */
    public void setConfiguration(Dictionary configuration)
        throws ConfigurationException {
        if (null == configuration) {
            throw new IllegalArgumentException(
                EXCEPTION_LOCALIZER.format(
                    "argument-must-not-be",
                    new Object[]{"configuration", "null"}));
        }

        synchronized (LOCK) {
            // message format string may be empty but not null
            String messageFormat = (String)
                configuration.get(MESSAGE_FORMAT_PROPERTY);
            if (null == messageFormat) {
                throw new ConfigurationException(
                    MESSAGE_FORMAT_PROPERTY, EXCEPTION_LOCALIZER.format(
                    "property-is-not-set", MESSAGE_FORMAT_PROPERTY));
            }
            // logger must be not null and not empty
            String loggerName = (String)
                configuration.get(LOGGER_NAME_PROPERTY);
            if (null == loggerName || "".equals(loggerName)) {
                throw new ConfigurationException(
                    LOGGER_NAME_PROPERTY, EXCEPTION_LOCALIZER.format(
                    "property-is-not-set", LOGGER_NAME_PROPERTY));
            }

            logger = Logger.getLogger(loggerName);
            logger.setLevel(Level.ALL);
            format = messageFormat;
        }
    }

    /**
     * Implement the event handler interface.
     *
     * @param event
     */
    public void handleEvent(Event event) {
        // copy references to method locals to avoid hold the synch block too
        // long
        Logger localLogger = null;
        String localFormat = null;
        synchronized (LOCK) {
            localLogger = logger;
            localFormat = format;
        }

        // set an intitial length length to the size of the format string
        // this is pretty arbitrary
        StringBuffer buffer = new StringBuffer(localFormat.length());
        Matcher matcher = PATTERN.matcher(localFormat);
        // used to indicate that at least one match was found
        int end = -1;
        while (matcher.find()) {
            buffer.append(matcher.group(1));
            //noinspection StringContatenationInLoop
            Object subst = event.getProperty(
                DefaultMetrics.REPORT_PROPERTIES_PREFIX + matcher.group(2));
            if (subst == null) {
                subst = "";
            }
            if (subst instanceof Date) {
                buffer.append(
                    DateFormats.RFC_1036.create().format((Date) subst));
            } else {
                buffer.append(subst);
            }
            end = matcher.end(2);
        }
        if (end >= 0 && end < localFormat.length() - 1) {
            buffer.append(localFormat.substring(end + 1));
        }
        localLogger.info(buffer.toString());
    }
}

