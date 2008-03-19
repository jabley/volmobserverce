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
package com.volantis.mcs.eclipse.common;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Level;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.spi.ErrorCode;
import org.apache.log4j.spi.ThrowableInformation;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.Status;

/**
 * This Log4J appender outputs events to the Eclipse log framework via a
 * specific Eclipse log instance.
 */
public class EclipseLogAppender extends AppenderSkeleton {
    /**
     * The Eclipse log instance to which the appender will log.
     */
    private ILog log;

    /**
     * Sets the Eclipse log instance to which the appender will log.
     *
     * @param log the Eclipse log to append to
     */
    void setLog(ILog log) {
        this.log = log;
    }

    /**
     * Initializes the new instance.
     */
    public EclipseLogAppender() {
    }

    /**
     * Initializes the new instance using the given parameters.
     *
     * @param log the Eclipse log to append to
     */
    public EclipseLogAppender(ILog log) {
        this.log = log;
    }

    /**
     * Log the specified event if is within the required threshold.
     *
     * <p>The Log4J level is translated into as close an equivalent Eclipse
     * {@link Status} level.</p>
     *
     * @param event the event to be logged
     */
    public void append(LoggingEvent event) {
        String text;
        Throwable thrown = null;
        Level level = event.getLevel();
        int severity = Status.OK;

        if (log == null) {
            throw new IllegalStateException(
                    "Must call #setLog or construct the appender with a " +
                    "non-null ILog instance before attempting to use the " +
                    "appender");
        }
        
        if (layout == null) {
            text = event.getRenderedMessage();
        } else {
            text = layout.format(event);
        }

        if (layout == null || layout.ignoresThrowable()) {
            ThrowableInformation info = event.getThrowableInformation();

            if (info != null) {
                thrown = info.getThrowable();
            }
        }

        // Translate the log level to an equivalent status code
        if (level.toInt() >= Level.ERROR_INT) {
            severity = Status.ERROR;
        } else if (level.toInt() >= Level.WARN_INT) {
            severity = Status.WARNING;
        } else if (level.toInt() >= Level.DEBUG_INT) {
            severity = Status.INFO;
        }

        log.log(new Status(severity,
                           log.getBundle().getSymbolicName(),
                           level.toInt(),
                           text,
                           thrown));
    }

    // javadoc inherited
    public void close() {
        closed = true;
    }

    // javadoc inherited
    public boolean requiresLayout() {
        return false;
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 23-Nov-04	6272/1	philws	VBM:2004111803 Output error and fatal Log4J messages to the Eclipse Log

 ===========================================================================
*/
