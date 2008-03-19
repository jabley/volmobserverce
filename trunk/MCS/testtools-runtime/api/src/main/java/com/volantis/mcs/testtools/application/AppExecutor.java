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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/testtools/application/AppExecutor.java,v 1.1 2003/04/23 13:08:09 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 25-Mar-03    Geoff           VBM:2003042306 - Created; an interface which 
 *                              implements the Command pattern for use with 
 *                              AppManager's useAppWith().
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.testtools.application;

/**
 * An interface which implements the Command pattern for use with 
 * {@link AppManager#useAppWith}.
 */ 
public interface AppExecutor {

    /**
     * The copyright statement.
     */
    String mark = "(c) Volantis Systems Ltd 2003. ";

    /**
     * Execute some code that requires that a "full Mariner application" is
     * up and running.
     * 
     * @param context the context that the application is running in.
     * 
     * @throws Exception if there was a problem executing the code.
     */ 
    void execute(AppContext context) throws Exception;
    
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 13-May-05	8200/2	trynne	VBM:2005050412 Moved classes from oldtests to testtools-runtime and added testtools-runtime classes into testtools.jar so that MPS need only depend on testtools

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
