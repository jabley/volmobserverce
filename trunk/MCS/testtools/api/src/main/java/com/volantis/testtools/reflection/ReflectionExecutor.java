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
 * $Header: /src/voyager/com/volantis/testtools/reflection/ReflectionExecutor.java,v 1.1 2003/03/07 10:21:46 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 06-Mar-03    Geoff           VBM:2003010904 - Created; executor for use 
 *                              with ReflectionManager. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.testtools.reflection;

import java.lang.reflect.AccessibleObject;

/**
 * Executor for use with {@link ReflectionManager}.
 * <p> 
 * Clients of {@link ReflectionManager} ought to create instances of this
 * class to pass into {@link ReflectionManager#useAsAccessible}.
 */ 
public interface ReflectionExecutor {

    /**
     * The copyright statement.
     */
    String mark = "(c) Volantis Systems Ltd 2002. ";

    /**
     * Called by {@link ReflectionManager#useAsAccessible} while the 
     * object is accessible.
     * 
     * @param object object that has been made (temporarily) accessible.
     * @return any value that the executor wanted to return.
     * @throws Exception if the executor had a problem.
     */ 
    Object execute(AccessibleObject object) throws Exception;
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
