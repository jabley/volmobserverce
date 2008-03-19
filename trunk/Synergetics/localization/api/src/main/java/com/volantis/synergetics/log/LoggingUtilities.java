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
 * $Header: /src/voyager/com/volantis/mcs/logging/LoggingUtilities.java,v 1.2 2002/03/18 12:41:16 ianw Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 02-Jan-02    Paul            VBM:2002010201 - Created.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from class
                                to string.
 * ----------------------------------------------------------------------------
 */

package com.volantis.synergetics.log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;

/**
 * This class provides utilities for logging.
 */
public class LoggingUtilities {

    /**
     * Return a StringBuffer containing the standard header to log.
     *
     * @return A StringBuffer containing some standard information which may be
     *         useful when debugging.
     */
    public static StringBuffer getStandardHeader() {

        StringWriter writer = new StringWriter();
        PrintWriter printer = new PrintWriter(writer);

        printer.println();
        printer.println("Log opened on " + new Date());
        printer.println();

        printer.println("JVM System Properties");
        printer.println("---------------------");
        Properties properties = System.getProperties();
        Enumeration keys = properties.propertyNames();
        while (keys.hasMoreElements()) {
            String name = (String) keys.nextElement();
            String value = properties.getProperty(name);
            printer.println(name + "=" + value);
        }

        return writer.getBuffer();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 21-Apr-05	435/1	pcameron	VBM:2005040505 Logging initialisation changed

 19-Apr-05	428/11	pcameron	VBM:2005040505 Logging initialisation changed

 18-Apr-05	428/9	pcameron	VBM:2005040505 Logging initialisation changed

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
