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
package com.volantis.mcs.ibm.websphere.portal.logging;

import com.ibm.wps.logging.LogManager;
import com.ibm.wps.logging.Logger;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.synergetics.localization.MessageLocalizer;

import java.util.Set;
import java.util.HashSet;

import org.apache.log4j.Appender;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Layout;
import org.apache.log4j.Level;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.spi.ThrowableInformation;


/**
 * Log4J Appender to re-route messages through to WPS logging on
 * the websphere server.
 */
public class WPSJlogAppender extends AppenderSkeleton implements Appender {
    
    /**
     * Used to obtain localized messages
     */
    private static final MessageLocalizer messageLocalizer =
                LocalizationFactory.createMessageLocalizer(
                        WPSJlogAppender.class);

    /**
     * Populated with the "class" names that don't map onto real classes. Once
     * a "class" has been reported as missing once we don't want to report it
     * again, hence this set. This is static so it is shared across all
     * appender instances.
     */
    private static final Set reportedMissingClasses = new HashSet();

    /**
     * Default constructor. If we do not have one, log4j throws an
     * InstantiationException when we try to create the appender.
     */
    public WPSJlogAppender() {
        super();
    }

    /**
     * Create the appender with a known layout.
     *
     * @param layout the layout defining the output format.
     */
    public WPSJlogAppender(Layout layout) {
        super.layout = layout;
    }

    /**
     * Called by the AppenderSkeleton when a logging event occurs causing
     * output to this appender. This appender converts the log4j event into its
     * WPS logger equivalent and logs it through there.
     *
     * @param event the event to be logged
     * @see org.apache.log4j.AppenderSkeleton#append(LoggingEvent)
     */
    protected void append(LoggingEvent event) {

        // Try finding a WPS logger which matches the log4j logger.
        Logger logger = null;
        final String loggerName = event.getLoggerName();

        try {
            // Try looking up a class from the log4j logger name. This may
            // fail since log4j allows you to pass logger names as strings.
            Class clazz = Class.forName(loggerName);
            // Look up a WPS logger for the log4j logger class name.
            logger = LogManager.getLogManager().getLogger(clazz);
        } catch (ClassNotFoundException e) {
            // The log4j logger name was not a valid class name.
            // This will happen if we have created a Category with a String
            // which is out of date or has a spelling mistake, or if a third
            // party uses something other than class names as logger names.
            // In this case we just use ourselves as the logger source and
            // report the logger mismatch as an info level log as well.
            logger = LogManager.getLogManager().getLogger(this.getClass());

            synchronized(reportedMissingClasses) {
                if (!reportedMissingClasses.contains(loggerName)) {
                    // Only report a given missing "class" once
                    logger.text(Logger.INFO, null, messageLocalizer.format(
                        "unable-to-find-logger",
                        new Object[]{loggerName, this.getClass().getName()}));
                    reportedMissingClasses.add(loggerName);
                }
            }
        }

        // Map the Log4J 'priority' to the WPS equivalent 'level'.
        int level;
        int priority = event.getLevel().toInt();
        switch (priority) {

            case Level.ERROR_INT:
            case Level.FATAL_INT:
                level = Logger.ERROR;
                break;

            case Level.WARN_INT:
                level = Logger.WARN;
                break;

            case Level.INFO_INT:
                level = Logger.INFO;
                break;

            case Level.DEBUG_INT:
            case Level.ALL_INT:
                level = Logger.TRACE_HIGH;
                break;

            default:
                level = Logger.TRACE_MEDIUM;
                break;
        }

        // If the logger has this log level enabled...
        if (logger.isLogging(level)) {
            // Then, generate the output message, including the required text
            // and optional throwable
            String message = null;
            Throwable throwable = null;
            final ThrowableInformation throwableInformation =
                    event.getThrowableInformation();

            if (throwableInformation != null) {
                throwable = throwableInformation.getThrowable();
            }

            if (layout != null) {
                message = layout.format(event);
            } else {
                message = event.getRenderedMessage();
            }

            if ((message != null) || (throwable != null)) {
                // Log the message via WPS
                logger.text(level, null, message, throwable);
            }
        }
    }

    /**
     * Determine whether the appender requires a layout. Our appender returns
     * false as we will use a layout if it is supplied but will not error if
     * there is not one. If a layout is defined, it will be used to format the
     * actual looging message. The message format is configured through the
     * log4j mariner configuration file.
     *
     * @return always returns false
     * @see org.apache.log4j.Appender#requiresLayout()
     */
    public boolean requiresLayout() {
        return false;
    }

    /**
     * Close the appender. There is nothing to do here since we are using the
     * static WPS LogManager which we should not close. The method must be
     * implemented though as it is defined in AppenderSkeleton
     */
    public void close() {
    }
}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-May-05	8029/1	philws	VBM:2005050313 Port WebSphere category handling from 3.3

 05-May-05	8019/1	philws	VBM:2005050313 Document the categories and suppress multiple reports of missing 'categories' in WebSphere logging integration

 24-Mar-05	7493/9	emma	VBM:2005032305 Merge from 3.3.0 - Reducing log level to INFO as the condition is not an error

 16-Feb-05	6996/2	philws	VBM:2005021516 Provide throwable information to WPS JLog when available

 16-Feb-05	6992/1	philws	VBM:2005021516 Provide throwable information to WPS JLog when available

 23-Mar-05	7489/3	emma	VBM:2005032305 Localizing log message

 23-Mar-05	7489/1	emma	VBM:2005032305 Reducing log level to INFO as the condition is not an error

 16-Feb-05	6992/3	philws	VBM:2005021516 Provide throwable information to WPS JLog when available

 08-Dec-04	6416/7	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/5	ianw	VBM:2004120703 New Build

 24-Mar-04	3542/1	geoff	VBM:2004031913 WPSJLogAppender very slow - generates error location method names

 24-Mar-04	3532/1	geoff	VBM:2004031913 WPSJLogAppender very slow - generates error location method names

 22-Mar-04	3496/1	geoff	VBM:2004031913 WPSJLogAppender very slow - generates error location method names

 12-Feb-04	2976/1	philws	VBM:2004020504 Fix Log4J to JLog level mappings

 16-Oct-03	1534/3	steve	VBM:2003100601 Jalopy code tidy and renamed XML config file

 16-Oct-03    1534/1    steve    VBM:2003100601 Websphere WPS Appender added

 ===========================================================================
*/
