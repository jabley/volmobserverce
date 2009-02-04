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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.runtime.configuration;



import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.mcs.localization.LocalizationFactory;

/**
 * This is an implementation of the DataSourceFactory which
 * provides the ability to create a JNDI specific datasource from the JNDI
 * specific configuration.
 */
public class JNDIDataSourceFactory {

    /**
     *  Volantis copyright mark.
     */
    private static String mark
        = "(c) Volantis Systems Ltd 2003. ";

    /**
     * Used for logging
     */
    private static final LogDispatcher logger = 
            LocalizationFactory.createLogger(JNDIDataSourceFactory.class);


    /**
     * Create the factory.
     */
    public JNDIDataSourceFactory() {

    }

    /**
     * Create the DataSource the JNDIConfiguration object and
     * jndiConfiguration map.
     *
     * @param config            the JNDIConfiguration object.
     * @param jndiConfiguration the jndi configuration map.
     */
    public DataSource createDataSource(JNDIDataSourceConfiguration config,
            JNDIConfiguration jndiConfiguration) {

                if (logger.isDebugEnabled()) {
                    logger.debug("Creating JNDI DataSource");
                }        
        if ((config != null) && (jndiConfiguration != null)) {
            Context ctx = null;

            String initialContext = config.getInitialContext();

            if (logger.isDebugEnabled()) {
                logger.debug("Context is " + initialContext);
            }            
            Hashtable ht = 
                (Hashtable)jndiConfiguration.getInitialContext(
                    initialContext).getParameters();
            try {
                ctx = new InitialContext(ht);
                DataSource ds = (DataSource) ctx.lookup(
                    config.getName());
                ctx.close();
                return ds;
            }
            catch (NamingException ne) {
                if (logger.isDebugEnabled()) {
                    logger.debug(ne.getLocalizedMessage(), ne);
                }
            }
        } else {
            if (logger.isDebugEnabled()) {
                logger.debug("Cannot create a datasource with a null " +
                             "configuration (config, jndiConfig):(" + config +
                             "," + jndiConfiguration + ")");
            }
        }
        return null;
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/6	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/4	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/4	doug	VBM:2004111702 Refactored Logging framework

 04-May-04	4023/1	ianw	VBM:2004032302 Added support for short length tables

 09-Mar-04	2867/3	ianw	VBM:2004012603 Rationalised data source configuration and refactored code to cope with validated config schema

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 18-Aug-03	1141/1	adrian	VBM:2003070902 fixed jndi factory

 02-Jul-03	478/1	byron	VBM:2003061401 Unfinished issues of 2003060403

 27-Jun-03	586/1	byron	VBM:2003062704 Username and password are not used if no connection pooling in mariner-config.xml

 13-Jun-03	316/3	byron	VBM:2003060403 Addressed some rework issues

 12-Jun-03	316/1	byron	VBM:2003060403 Read cache and sql connector information from xml file

 ===========================================================================
*/
