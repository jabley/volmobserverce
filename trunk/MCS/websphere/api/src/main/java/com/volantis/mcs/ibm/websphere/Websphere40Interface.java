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
 * $Header: /src/voyager/com/volantis/mcs/ibm/websphere/Websphere40Interface.java,v 1.3 2003/03/20 15:15:31 sumit Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 *
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 08-Mar-02    Mat             VBM:2002013005 - Created
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from class
                                to string.
 * 20-Mar-03    sumit           VBM:2003031809 - Wrapped logger.debug 
 *                              statements in if(logger.isDebugEnabled()) block
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.ibm.websphere;

import com.volantis.mcs.integration.AbstractAppServer;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.runtime.Config;
import com.volantis.synergetics.log.LogDispatcher;
  
import java.util.Properties;
  
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * Methods to access information from WebSphere 4.0.
 */
public class Websphere40Interface extends AbstractAppServer {
    /**
     * Used for logging
     */
    private static final LogDispatcher logger = 
            LocalizationFactory.createLogger(Websphere40Interface.class);
    
    /**
     * Initializes the new instance.
     */
    public Websphere40Interface() {
        if (logger.isDebugEnabled()){
            logger.debug("Websphere " + appServerVersion() +
                         " Application Server Interface started");
        }
    }
    
    /**
     * Returns a string of the version of the app server implemented.
     *
     * @return the app server version number
     */
    protected String appServerVersion() {
        return "4.0";
    }

    // Javadoc inherited from interface.
    public DataSource getAppServerDataSource() {
        // Retrieve a DataSource through the JNDI Naming Service
        
        
        String dataSource = volantisBean.getConfig().
                getAttributeValue(CONFIG_WEBAPP_ELEMENT, "datasource");

        if (dataSource == null) {
            logger.info("missing-datasource-pool");

            return null;
        }

        Properties parms = new Properties();

        parms.setProperty(Context.INITIAL_CONTEXT_FACTORY,
                          "com.ibm.websphere.naming.WsnInitialContextFactory");
        
        try {
            // Create the Initial Naming Context
            Context ctx = new InitialContext(parms);
            DataSource ds = 
                    (DataSource) ctx.lookup(dataSource);
            ctx.close();
            logger.info("websphere-datasource-info", new Object[]{dataSource});

            return ds;
        } catch (NamingException ne) {
            logger.error("unexpected-exception", ne);

            return null;
        }
    }
    
    // Javadoc inherited from interface.
    public boolean useAppServerDataSource() {
        Config c = volantisBean.getConfig();

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

 27-Apr-05	7896/1	pduffin	VBM:2005042709 Removing PolicyPreference and all related classes

 09-Dec-04	6417/1	philws	VBM:2004120703 Committing tidy up

 08-Dec-04	6416/4	ianw	VBM:2004120703 New Build

 08-Dec-04	6232/6	doug	VBM:2004111702 refactored logging framework

 29-Nov-04	6332/1	doug	VBM:2004112913 Refactored logging framework

 29-Nov-04	6232/4	doug	VBM:2004111702 Refactored Logging framework

 21-Jun-04	4702/3	matthew	VBM:2004061402 rework PageTracking

 16-Jun-04	4702/1	matthew	VBM:2004061402 management functionality added

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 ===========================================================================
*/
