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
 * $Header: /src/voyager/com/volantis/mcs/integration/AppServerInterface.java,v 1.2 2002/03/18 12:41:15 ianw Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 07-Nov-01    Mat             VBM:2001110701 - Add ability to get the 
 *                              datasource from the application server
 * 06-Dec-01    Paul            VBM:2001113004 - Removed all unnecessary
 *                              imports.
 * 09-Jan-02    Paul            VBM:2002010403 - Moved from appserver package
 *                              and added getUserFactory method.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from class
                                to string.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.integration;

import com.volantis.mcs.runtime.Volantis;
import com.volantis.mcs.management.tracking.PageTrackerFactory;

import javax.sql.DataSource;

/**
 * Defines an interface for the different application server classes to
 * implement.
 */
public interface AppServerInterface {
    /**
     * Defines the "path" required in the mcs configuration to find the app
     * server's configuration data.
     */
    String CONFIG_WEBAPP_ELEMENT =
            "mcs-config.web-application";

    /**
     * Allows the app server implementation to be given the Volantis bean that
     * should be used to retrieve various details of the running MCS instance.
     *
     * @param bean The bean to set
     */
    public void setVolantisBean(Volantis bean);

    // javadoc unnecessary
    public Volantis getVolantisBean();

    /**
     * Returns the value of use-server-connection-pool from the config file.
     *
     * @return Whether to use the App server connection pool
     */
    public boolean useAppServerDataSource();

    /**
     * Get the datasource from the application server, bypassing the normal
     * Volantis connection pooling and using that provided by the application
     * server instead.
     *
     * @return The datasource to use
     */
    public DataSource getAppServerDataSource();

    /**
     * Get a PageTrackerFactory.
     *
     * @return the PageTrackerFactory.
     */
    public PageTrackerFactory getPageTrackerFactory();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 04-Jul-05	8940/1	philws	VBM:2005060606 Provider installer compatible app server interface implementations

 28-Apr-05	7922/1	pduffin	VBM:2005042801 Removed User and UserFactory classes

 08-Dec-04	6416/4	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/2	ianw	VBM:2004120703 New Build

 16-Jun-04	4702/1	matthew	VBM:2004061402 management functionality added

 18-Dec-03	2246/1	geoff	VBM:2003121715 debrand config file

 ===========================================================================
*/
