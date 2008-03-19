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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/testtools/application/ConfigChecker.java,v 1.2 2003/04/23 13:08:09 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 06-Mar-03    Geoff           VBM:2003010904 - Created; checks that Volantis 
 *                              was initialised with config values provided.
 * 25-Mar-03    Geoff           VBM:2003042306 - Use refactored 
 *                              ConfigCheckExecutor which does not need to 
 *                              have knowledge of AppManager.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.testtools.application;

import com.volantis.testtools.stubs.ServletContextStub;
import com.volantis.mcs.runtime.Volantis;

/**
 * Checks (as far as possible) that Volantis was initialised with the config
 * values provided.
 * <p>
 * This checks initialisation via the XML config file for now, but in future it
 * could also check using the new config objects.
 */ 
public class ConfigChecker {
    private Volantis volantis;
    private ServletContextStub servletContext;
    private AppConfigurator appConf;
        
    /**
     * Construct a new config checker.
     * 
     * @param volantis the Volantis instance to initialise.
     * @param servletContext the servlet context to initialise with.
     */ 
    public ConfigChecker(Volantis volantis, 
            ServletContextStub servletContext) {
        this.volantis = volantis;
        this.servletContext = servletContext;
    }

    /**
     * Sets the object which will be used to configure the config values
     * used for the initialisation.
     * 
     * @param appConf configures config values for initialisation.
     */ 
    public void setAppConf(AppConfigurator appConf) {
        this.appConf = appConf;
    }

    /**
     * Initialise Volantis, then check that the values used match the values
     * within Volantis, as far as is possible.
     * 
     * @throws Exception
     */ 
    public void checkInitialisation() throws Exception {
        AppManager appManager = new AppManager(volantis, servletContext);
        appManager.setAppConf(appConf);
        AppExecutor executor = new ConfigCheckExecutor(volantis);
        appManager.useAppWith(executor);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/4	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/2	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
