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
 * $Header: $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who         Description
 * -----------  ----------- -------------------------------------------------
 * 28-Apr-2003  Sumit       VBM:2003041502 - Stub for use in Servlet/JSP 
 *                          test cases
 * ----------------------------------------------------------------------------
 */
 
package com.volantis.mcs.testtools.request.stubs;

import com.volantis.mcs.servlet.MarinerServletApplication;

import javax.servlet.ServletException;

/**
 * A stub that overrides the checkInitializationState method to always return
 * true. Used in the IncludePipelineTagTestCase
 */
public class MarinerServletApplicationStub extends MarinerServletApplication {

    /**
     * Default constructor.
     */
    public MarinerServletApplicationStub() {
    }

    /* (non-Javadoc)
     * @see com.volantis.mcs.servlet.MarinerServletApplication#checkInitializationState()
     */
    boolean checkInitializationState() throws ServletException {
        // always return true so we never have to bother with initialising the super class 
        return true;
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-May-05	7950/1	allan	VBM:2005041317 Some testcases for smart server

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 24-Jun-04	4737/3	allan	VBM:2004062202 Restrict volantis initialization.

 ===========================================================================
*/
