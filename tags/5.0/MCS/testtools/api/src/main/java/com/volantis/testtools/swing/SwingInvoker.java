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
 * $Header: /src/voyager/com/volantis/testtools/swing/SwingInvoker.java,v 1.1 2003/03/28 15:01:52 payal Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 28-Mar-03    Payal           VBM:2002101401 - Created an interface used by 
 *                              SwingTestHelper class.
 * ----------------------------------------------------------------------------
 */
package com.volantis.testtools.swing;

/**
 * This interface is used by  SwingTestHelper class.This for example 
 * invokes swing thread when the invoke() method of this 
 * interface is invoked.
 */
public interface SwingInvoker {

    /**
     * The copyright statement.
     */
    public static String mark = "(c) Volantis Systems Ltd 2003.";

    /**
     * Execute some code (using the Command pattern).
     */
    public void invoke();
}
/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
