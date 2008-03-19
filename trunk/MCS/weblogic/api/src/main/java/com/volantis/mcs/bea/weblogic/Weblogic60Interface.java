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
 * $Header: /src/voyager/com/volantis/mcs/bea/weblogic/Weblogic60Interface.java,v 1.3 2003/03/20 15:15:29 sumit Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 *
 * Created on 04 June 2001, 16:57
 *
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 23-Aug-01    Mat             VBM: 2001082303 - Get volantisBean from the 
 *                              pageContext
 * 07-Nov-01    Mat             VBM:2001110701 - Add ability to get the 
 *                              datasource from the application server
 * 21-Dec-01    Paul            VBM:2001121702 - Removed unused imports.
 * 02-Jan-02    Paul            VBM:2002010201 - Use log4j for logging.
 * 09-Jan-02    Paul            VBM:2002010403 - Moved from appserver package.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from class
                                to string.
 * 20-Mar-03    sumit           VBM:2003031809 - Wrapped logger.debug 
 *                              statements in if(logger.isDebugEnabled()) block
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.bea.weblogic;

import com.volantis.mcs.integration.AbstractAppServer;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.synergetics.log.LogDispatcher;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * An {@link com.volantis.mcs.integration.AppServerInterface
 * AppServerInterface} implementation appropriate to Weblogic 6.0.
 */
public class Weblogic60Interface extends AbstractAppServer {
    
    /**
     * Used for logging
     */
    private static final LogDispatcher logger = 
            LocalizationFactory.createLogger(Weblogic60Interface.class);
    
    /**
     * Initializes the new instance.
     */
    public Weblogic60Interface() {
        if (logger.isDebugEnabled()) {
            logger.debug("Weblogic " + appServerVersion() +
                         " Application Server Interface started");
        }
    }
    
    /**
     * Returns a string of the version of the app server implemented.
     *
     * @return the app server version number
     */
    protected String appServerVersion() {
        return "6.0";
    }

    // Javadoc inherited from interface.
    public DataSource getAppServerDataSource() {
        // This method uses the jndi-provider and datasource
        // from the config file to get the datasource reference
        // from Weblogic
        Context ctx = null;

        String dataSource = volantisBean.getConfig().
                getAttributeValue(CONFIG_WEBAPP_ELEMENT, "datasource");

        if (dataSource == null) {
            logger.info("connection-pool-datasource-not-found");
            return null;
        }

        Hashtable ht;
        String jndiProvider = volantisBean.getConfig().
                getAttributeValue(CONFIG_WEBAPP_ELEMENT, "jndi-provider");

        if (jndiProvider == null) {
            logger.info("jndi-provider-not-found");
            ht = null;
        } else {
            ht = new Hashtable();
            logger.info("jndi-provider-config-used");     
            ht.put(Context.INITIAL_CONTEXT_FACTORY,
                "weblogic.jndi.WLInitialContextFactory");
            ht.put(Context.PROVIDER_URL,jndiProvider);
        }

        try {
            ctx = new InitialContext(ht);
        } catch (NamingException nex) {
            logger.error("unexpected-exception", nex);
            return null;
        }

        try {
            DataSource ds = (DataSource)ctx.lookup(dataSource);
            ctx.close();

            if (logger.isDebugEnabled()) {
                logger.debug("Using Weblogic Datasource " + dataSource);
            }

            return ds;
        } catch (NamingException ne) {
            logger.error("unexpected-exception", ne);

            return null;
        }
    }

    // Javadoc inherited from interface.
    public boolean useAppServerDataSource() {
        com.volantis.mcs.runtime.Config c = volantisBean.getConfig();
        if (logger.isDebugEnabled()) {
            logger.debug(CONFIG_WEBAPP_ELEMENT +
                         ".use-server-connection-pool = " +
                         c.getAttributeValue(CONFIG_WEBAPP_ELEMENT,
                                             "use-server-connection-pool"));
        }

        return c.getBooleanAttributeValue(CONFIG_WEBAPP_ELEMENT,
                                          "use-server-connection-pool");
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 04-Jul-05	8940/1	philws	VBM:2005060606 Provider installer compatible app server interface implementations

 28-Apr-05	7922/1	pduffin	VBM:2005042801 Removed User and UserFactory classes

 09-Dec-04	6417/1	philws	VBM:2004120703 Committing tidy up

 08-Dec-04	6416/4	ianw	VBM:2004120703 New Build

 08-Dec-04	6232/7	doug	VBM:2004111702 refactored logging framework

 29-Nov-04	6332/1	doug	VBM:2004112913 Refactored logging framework

 29-Nov-04	6232/5	doug	VBM:2004111702 Refactored Logging framework

 21-Jun-04	4702/3	matthew	VBM:2004061402 rework PageTracking

 16-Jun-04	4702/1	matthew	VBM:2004061402 management functionality added

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 28-Jul-03	886/1	chrisw	VBM:2003072302 If there is no jndi-provider in the mariner-config.xml file default to local jndi-provider

 28-Jul-03	884/3	chrisw	VBM:2003072302 If there is no jndi-provider in the mariner-config.xml file default to local jndi-provider

 28-Jul-03	878/1	chrisw	VBM:2003072302 If there is no jndi-provider in the mariner-config.xml file default to local jndi-provider

 ===========================================================================
*/
