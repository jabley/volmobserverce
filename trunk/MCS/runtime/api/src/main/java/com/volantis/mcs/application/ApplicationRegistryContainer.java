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
 * $Header: /src/voyager/com/volantis/mcs/application/ApplicationRegistryContainer.java,v 1.2 2003/02/11 12:50:18 ianw Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 11-Feb-03    Ian             VBM:2003020607 - Ported from Metis.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.application;

import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.mcs.localization.LocalizationFactory;

/**
 * This is a simple container holding a servlet and an internal 
 * application context factory.  Each application registers one of these 
 * classes in the ApplicationRegistry.
 */
public class ApplicationRegistryContainer {
    
    /**
     * The copyright statement.
     */
     private static final String mark = "(c) Volantis Systems Ltd 2002.";

    /**
     * Used for logging
     */
    private static final LogDispatcher logger = 
            LocalizationFactory.createLogger(ApplicationRegistryContainer.class);
            
    
    private ApplicationContextFactory internalApplicationContextFactory;
    private ApplicationContextFactory servletApplicationContextFactory;
    
    /** Creates a new instance of DefaultApplicationContextFactory */
    public ApplicationRegistryContainer() {
        this(null, null);
    }
    
    /** Creates a new instance of the container, populated with the 
     *  factories.
     */
    public ApplicationRegistryContainer(
                ApplicationContextFactory internalApplicationContextFactory,
                ApplicationContextFactory servletApplicationContextFactory) {
   
           this.internalApplicationContextFactory =
                internalApplicationContextFactory;
           this.servletApplicationContextFactory = 
                servletApplicationContextFactory;
    }
    
    /** Getter for property internalApplicationContextFactory.
    * @return Value of property internalApplicationContextFactory.
    *
     */
    public ApplicationContextFactory getInternalApplicationContextFactory() {
      return internalApplicationContextFactory;
    }

    /** Setter for property internalApplicationContextFactory.
    * @param internalApplicationContextFactory New value of property internalApplicationContextFactory.
    *
     */
    public void setInternalApplicationContextFactory(
                ApplicationContextFactory internalApplicationContextFactory) {
      this.internalApplicationContextFactory = internalApplicationContextFactory;
    }

    /** Getter for property servletApplicationContextFactory.
    * @return Value of property servletApplicationContextFactory.
    *
     */
    public ApplicationContextFactory getServletApplicationContextFactory() {
      return servletApplicationContextFactory;
    }

    /** Setter for property servletApplicationContextFactory.
    * @param servletApplicationContextFactory New value of property servletApplicationContextFactory.
    *
     */
    public void setServletApplicationContextFactory(
                ApplicationContextFactory servletApplicationContextFactory) {
      this.servletApplicationContextFactory = servletApplicationContextFactory;
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
