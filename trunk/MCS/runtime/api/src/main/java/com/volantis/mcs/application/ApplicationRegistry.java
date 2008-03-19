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
 * $Header: /src/voyager/com/volantis/mcs/application/ApplicationRegistry.java,v 1.3 2003/03/20 15:15:29 sumit Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 11-Feb-03    Ian             VBM:2003020607 - Ported from Metis.
 * 20-Mar-03    sumit           VBM:2003031809 - Wrapped logger.debug 
 *                              statements in if(logger.isDebugEnabled()) block
 * ----------------------------------------------------------------------------
 */


package com.volantis.mcs.application;

import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.mcs.localization.LocalizationFactory;

import java.util.HashMap;
import java.util.Map;
/**
 * This class provides a container for registering outboard applications
 * within Mariner. This is a core requirement for MPS support.
 * 
 * @mock.generate
 */
public class ApplicationRegistry {
    
    /**
     * Volantis copyright object.
     */
    private static String mark = "(c) Volantis Systems Ltd 2002.";
    
    /** The name of the default application */
    public static final String DEFAULT_APPLICATION_NAME = "mcs";
    
    /**
     * Used for logging
     */
    private static final LogDispatcher logger = 
            LocalizationFactory.createLogger(ApplicationRegistry.class);
    
    static protected Map applicationMap = new HashMap();
    
    static protected ApplicationRegistry instance =
            new ApplicationRegistry();
    
    /** Creates a new instance of ServletApplicationContextFactory */
    protected ApplicationRegistry() {
        if(logger.isDebugEnabled()){
            logger.debug("Initialising Application Registry");
        }
    }

    /**
     * Get the singleton instance of the InternalApplicationRegistry.
     *@return InternalApplicationRegistry
     */
    static public ApplicationRegistry getSingleton() {
        return instance;
    }   
        
    /**
     * Registers an applications applicationRegistryContainer with
     * mariner.
     * @param applicationName The name by which this application identifies 
     * itself.
     * @param applicationRegistryContainer The application specific
     * ApplicationContextFactory.
     */
    public void registerApplication(String applicationName,
        ApplicationRegistryContainer applicationRegistryContainer) {
       
        if (!applicationMap.containsKey(applicationName)) {
            applicationMap.put(applicationName, applicationRegistryContainer);
            if(logger.isDebugEnabled()){
                logger.debug("Application " + applicationName + " registered as " +
                applicationRegistryContainer);
            }
        } else {
            logger.warn("application-already-registered", new Object[]{applicationName});
        }
        
    }
    
    /**
     * UnRegisters an applications applicationRegistryContainer ffrom
     * mariner.
     * @param applicationName The name by which this application identifies 
     * itself.
     */
    public void unregisterApplication(String applicationName,
        ApplicationRegistryContainer applicationContextFactory) {
        
        if (!applicationMap.containsKey(applicationName)) {
            applicationMap.remove(applicationName);
            if(logger.isDebugEnabled()){
                logger.debug("Application " + applicationName + " unregistered");
            }
        }
        
    }

    /**
     * Gets the applicationRegistryContainer for the application.
     * @param applicationName The application name.
     * @return applicationRegistryContainer The factory for this application
     * or null if not found.
     */
    public ApplicationRegistryContainer getApplicationRegistryContainer(
                                            String applicationName) {
            
       
        ApplicationRegistryContainer applicationRegistryContainer =
                (ApplicationRegistryContainer)applicationMap
                        .get(applicationName);
        
        return applicationRegistryContainer;
        
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/5	doug	VBM:2004111702 Refactored Logging framework

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 25-Jul-03	860/1	geoff	VBM:2003071405 merge from mimas

 25-Jul-03	858/1	geoff	VBM:2003071405 merge from metis; fix dissection test case sizes

 23-Jul-03	807/1	geoff	VBM:2003071405 works and tested but no design review yet

 ===========================================================================
*/
