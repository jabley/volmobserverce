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
package com.volantis.mcs.management.tracking.jmx;

import javax.management.MBeanServerFactory;
import javax.management.MBeanServer;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Class containing some methods that are useful when testing JMX
 *
 */
public class JMXTestUtilities {

    /**
     * This class can not be instanciated.
     */
    private JMXTestUtilities() {
    }

    /**
     * Remove all registered MBeanServers.
     */
    public static void removeAllMBeanServers() {

        ArrayList mBeanServers = MBeanServerFactory.findMBeanServer(null);
        Iterator it = mBeanServers.listIterator();
        while(it.hasNext()) {
            MBeanServer server = (MBeanServer)it.next();
            MBeanServerFactory.releaseMBeanServer(server);
        }
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 16-Jun-04	4702/1	matthew	VBM:2004061402 management functionality added

 ===========================================================================
*/
