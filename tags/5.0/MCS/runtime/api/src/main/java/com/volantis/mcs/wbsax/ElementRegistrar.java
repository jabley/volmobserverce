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
 * $Header:$
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 16-May-03    Geoff           VBM:2003042904 - Created; registers WBXML 
 *                              token mappings for elements.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.wbsax;

/**
 * Registers WBXML token mappings for elements. 
 */ 
public interface ElementRegistrar {

    /**
     * The copyright statement.
     */
    String mark = "(c) Volantis Systems Ltd 2003. ";

    /**
     * Register the element name supplied against the token supplied.
     *  
     * @param token the token to register with.
     * @param name the element name to register with.
     */ 
    void registerElement(int token, String name);
    
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 14-Jul-03	790/1	geoff	VBM:2003070404 manual merge and just copy wbsax from metis to ensure we got everything

 10-Jul-03	751/1	geoff	VBM:2003070703 second go at cleaning up WBDOM test cases

 ===========================================================================
*/
