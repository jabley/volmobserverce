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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/testtools/application/AppConfigurator.java,v 1.2 2003/03/07 10:21:46 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 21-Feb-03    Geoff           VBM:2003022004 - Created.
 * 06-Mar-03    Geoff           VBM:2003010904 - Refactored to use new 
 *                              ConfigValue object.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.testtools.application;

import com.volantis.testtools.config.ConfigValue;

/**
 * Implementations of this class can initialise a {@link ConfigValue} for use 
 * with a {@link com.volantis.testtools.config.ConfigFileBuilder}; used during 
 * {@link AppManager#useAppWith}.  
 */ 
public interface AppConfigurator {

    /**
     * The copyright statement.
     */
    public static String mark = "(c) Volantis Systems Ltd 2002.";
    
    /**
     * Initialise the config value to create the config file you want.
     * 
     * @param config the config value to initialise. 
     */ 
    void setUp(ConfigValue config) throws Exception;

    /**
     * Clean up any resources allocated as part of the config value that was 
     * created during set up.
     * 
     * @param config the config value that was previously initialised.
     */ 
    void tearDown(ConfigValue config);
    
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 13-May-05	8200/1	trynne	VBM:2005050412 Moved classes from oldtests to testtools-runtime and added testtools-runtime classes into testtools.jar so that MPS need only depend on testtools

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 31-Oct-03	1593/1	mat	VBM:2003101502 Adding pluginconfig to test file build

 18-Aug-03	1146/1	geoff	VBM:2003042305 Add tearDown() to AppConfigurator

 18-Aug-03	1144/1	geoff	VBM:2003042305 Add tearDown() to AppConfigurator

 18-Aug-03	670/1	geoff	VBM:2003042305 Add tearDown() to AppConfigurator

 ===========================================================================
*/
