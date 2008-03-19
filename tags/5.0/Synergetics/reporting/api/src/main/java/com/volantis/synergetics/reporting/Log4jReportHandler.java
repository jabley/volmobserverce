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
 * (c) Volantis Systems Ltd 2007. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.synergetics.reporting;

import com.volantis.shared.time.DateFormats;

import java.util.Date;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * A handler that writes to a log file using log4j.
 *
 * @deprecated Use OSGi event listeners instead.
 */
public class Log4jReportHandler implements ReportHandler {

    /**
     * The name of the parameter that will contain the message format string
     */
    public static final String MESSAGE_FORMAT_KEY = "message.format";

    /**
     * The logger instance to write to
     */
    private Logger logger;

    /**
     * The format string supplied
     */
    private final String format;

    private final String bindName;

    /**
     * The pattern used to substitute values into the pattern
     */
    private static final Pattern PATTERN = Pattern.compile("(.*?)\\{(.*?)}");

    /**
     * Standard constructor used to create the report handler.
     *
     * @param report the map of configuration parameters
     * @param bindingName
     */
    public Log4jReportHandler(Map report, String bindingName) {
        this.bindName = bindingName;
        logger = Logger.getLogger(bindingName);
        logger.setLevel(Level.ALL);
        // ensure the format string is never null
        String tmp = (String) report.get(MESSAGE_FORMAT_KEY);
        if (tmp == null) {
            format = "";
        } else {
            format = tmp;
        }
    }

    // javadoc inherited
    public void report(Map metrics) {
        if (!ExclusionManager.getDefaultInstance().
            isExcluded(bindName,metrics)) {

            StringBuffer buffer = new StringBuffer(MESSAGE_FORMAT_KEY.length());
            Matcher matcher = PATTERN.matcher(format);
            // used to indicate that at least one match was found
            int end = -1;
            while (matcher.find()) {
                buffer.append(matcher.group(1));
                Object subst = metrics.get(matcher.group(2));
                if (subst == null) {
                    subst = "";
                }
                if (subst instanceof Date) {
                    buffer.append(DateFormats.RFC_1036.create().format((Date)subst));
                } else {
                    buffer.append(subst);
                }
                end = matcher.end(2);
            }

            if (end >= 0 && end < format.length() - 1) {
                buffer.append(format.substring(end + 1));
            }

            logger.info(buffer.toString());
        }
    }
}
