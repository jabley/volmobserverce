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
 * $Header: /src/voyager/com/volantis/mcs/logging/DefaultConfigurator.java,v 1.7 2002/08/06 14:09:17 aboyd Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 02-Jan-02    Paul            VBM:2002010201 - Created.
 * 14-Jan-02    Paul            VBM:2002011414 - Changed to use AsyncAppender.
 * 31-Jan-02    Paul            VBM:2001122105 - Made sure that the
 *                              AsyncAppender used the location info.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from
 *                              class to string.
 * 03-May-02    Paul            VBM:2002042203 - Changed default pattern to
 *                              use category instead of location and also made
 *                              sure that appenders don't try and get the
 *                              location information anyway.
 * 06-Aug-02    Allan           VBM:2002080102 - Modified configure to enable
 *                              compatibility with pre v1.2.5 and v1.2.5 and
 *                              later versions of log4j.
 * ----------------------------------------------------------------------------
 */

package com.volantis.synergetics.log;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Logger;

/**
 * This class provides helper methods to configure and shutdown Log4J.
 */
public class DefaultConfigurator {

    /**
     * The log4j object to log to once logging has been configured.
     */
    private static Logger logger =
        Logger.getLogger(DefaultConfigurator.class);

    /**
     * Configures logging with no appender.
     *
     * @param logToConsole true if logging to the console should be enabled;
     *                     false otherwise
     */
    public static void configure(boolean logToConsole) {
        configure(logToConsole, null);
    }

    /**
     * Configures logging with an appender to which output will always be
     * logged.
     *
     * @param logToConsole true if logging to the console should be enabled;
     *                     false otherwise
     * @param appender     the appender to use for the logging
     */
    public static void configure(boolean logToConsole,
                                 AppenderSkeleton appender) {
        Log4jHelper.initializeLogging(logToConsole, appender);

        // Now that logging has been configured, log the standard header.
        logger.info(LoggingUtilities.getStandardHeader());
    }

    /**
     * Shuts down log4j safely.
     */
    public static void shutdown() {
        Log4jHelper.shutdown();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 21-Apr-05	435/1	pcameron	VBM:2005040505 Logging initialisation changed

 19-Apr-05	428/32	pcameron	VBM:2005040505 Logging initialisation changed

 19-Apr-05	428/30	pcameron	VBM:2005040505 Logging initialisation changed

 18-Apr-05	428/28	pcameron	VBM:2005040505 Logging initialisation changed

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/3	doug	VBM:2004111702 Refactored Logging framework

 23-Nov-04	6272/1	philws	VBM:2004111803 Output error and fatal Log4J messages to the Eclipse Log

 ===========================================================================
*/
