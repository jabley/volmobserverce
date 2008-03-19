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
 * $Header: /src/voyager/com/volantis/mcs/runtime/AppServerInterfaceManager.java,v 1.5 2003/03/20 15:15:33 sumit Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 07-Nov-01    Mat             VBM:2001110701 - Add ability to get the
 *                              datasource from the application server
 * 12-Dec-01    Paul            VBM:2001121702 - Removed unused imports.
 * 02-Jan-02    Paul            VBM:2002010201 - Use log4j for logging.
 * 09-Jan-02    Paul            VBM:2002010403 - Moved from appserver package
 *                              and added getUserFactory method.
 * 31-Jan-02    Paul            VBM:2001122105 - Fixed some javadoc problems.
 * 14-Mar-02    Mat             VBM:2002013005 - Added Websphere40Interface
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from class
                                to string.
 * 20-Mar-03    sumit           VBM:2003031809 - Wrapped logger.debug
 *                              statements in if(logger.isDebugEnabled()) block
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.runtime;

import com.volantis.mcs.integration.AbstractAppServer;
import com.volantis.mcs.integration.AppServerInterface;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.synergetics.log.LogDispatcher;

import javax.sql.DataSource;

/**
 * This delegating {@link AppServerInterface} implementation is responsible
 * for creating the correct type of delegate <code>AppServerInstance</code>
 * based on the MCS configuration.
 */
public class AppServerInterfaceManager extends AbstractAppServer {
    /**
     * Used for logging
     */
    private static final LogDispatcher logger = 
            LocalizationFactory.createLogger(AppServerInterfaceManager.class);

    /**
     * The underlying AppServerInterface class.
     */
    private AppServerInterface asi;

    /**
     * Initializes the new instance using the given parameters.
     *
     * @param config       the MCS configuration to be queried
     * @param volantisBean the bean that can be used to obtain information
     *                     about the MCS instance
     */
    public AppServerInterfaceManager(Config config, Volantis volantisBean)
            throws ClassNotFoundException,
            InstantiationException,
            IllegalAccessException {

        String appServer = config.getAttributeValue(CONFIG_WEBAPP_ELEMENT,
                "app-server-name");

        if (appServer == null) {
            logger.warn("config-app-server-missing");
            appServer = "Generic";
        }

        String asiClass;

        // Hard code the mapping from name to class for now but in future we
        // need to do this mapping some other way, such as using services
        // files in the jar file.
        if ("Tomcat31".equalsIgnoreCase(appServer) ||
                "Tomcat41".equalsIgnoreCase(appServer) ||
                "Generic".equalsIgnoreCase(appServer)) {
            asiClass = "com.volantis.mcs.runtime.GenericAppServerInterface";
        } else if ("Weblogic60".equalsIgnoreCase(appServer)) {
            asiClass = "com.volantis.mcs.bea.weblogic.Weblogic60Interface";
        } else if ("Weblogic92".equalsIgnoreCase(appServer)) {
            asiClass = "com.volantis.mcs.bea.weblogic.Weblogic81Interface";
        } else if ("Dynamo511".equalsIgnoreCase(appServer)) {
            asiClass = "com.volantis.mcs.atg.dynamo.Dynamo511Interface";
        } else if ("Websphere40".equalsIgnoreCase(appServer)) {
            asiClass = "com.volantis.mcs.ibm.websphere.Websphere40Interface";
        } else if ("Websphere60".equalsIgnoreCase(appServer)) {
            asiClass = "com.volantis.mcs.ibm.websphere.Websphere5Interface";
        } else {
            // Default to using the application server name as the class
            // name itself. This is *not* to be documented and is *not*
            // formally supported.
            asiClass = appServer;
        }

        if (logger.isDebugEnabled()) {
            logger.debug("Application server : " + asiClass);
        }

        asi = (AppServerInterface) Class.forName(asiClass).newInstance();

        setVolantisBean(volantisBean);
    }

    // Javadoc inherited from interface.
    public DataSource getAppServerDataSource() {
        return asi.getAppServerDataSource();
    }

    // Javadoc inherited from interface
    public boolean useAppServerDataSource() {
        return asi.useAppServerDataSource();
    }

    // Javadoc inherited from interface
    public void setVolantisBean(Volantis bean) {
        asi.setVolantisBean(bean);
    }

    // Javadoc inherited from interface
    public Volantis getVolantisBean() {
        return asi.getVolantisBean();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 04-Jul-05	8940/1	philws	VBM:2005060606 Provider installer compatible app server interface implementations

 24-May-05	7890/2	pduffin	VBM:2005042705 Committing extensive restructuring changes

 28-Apr-05	7922/1	pduffin	VBM:2005042801 Removed User and UserFactory classes

 23-May-05	8399/1	rgreenall	VBM:2005051616 Added support for Tomcat 4.

 08-Dec-04	6416/4	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/2	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/4	doug	VBM:2004111702 Refactored Logging framework

 21-Jun-04	4702/3	matthew	VBM:2004061402 rework PageTracking

 16-Jun-04	4702/1	matthew	VBM:2004061402 management functionality added

 19-Feb-04	2789/4	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/2	tony	VBM:2004012601 Localised logging (and exceptions)

 02-Feb-04	2802/1	ianw	VBM:2004011921 Fixed copyright and added into AppServerInterface

 ===========================================================================
*/
