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
 * $Header: /src/voyager/com/volantis/testtools/reflection/ReflectionManager.java,v 1.2 2003/03/20 12:03:17 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 06-Mar-03    Geoff           VBM:2003010904 - Created; manages access to 
 *                              normally inaccessible things via Reflection.
 * 10-Mar-03    Geoff           VBM:2002112102  - Added important usage 
 *                              warning that I forgot when I created it.
 * ----------------------------------------------------------------------------
 */
package com.volantis.testtools.reflection;

import junit.framework.Assert;

import java.lang.reflect.AccessibleObject;

import com.volantis.testtools.reflection.ReflectionExecutor;

/**
 * Manages access to normally inaccessible things via Reflection.
 * <p>
 * Needing to use this class is an indication of a "Design Smell". It 
 * indicates that the class under test has a poor design. Before thinking of 
 * using this, please consider refactoring the class under test to be more 
 * sensible. Excessive usage of this class will cause us pain in the future.
 * <p>
 * If using this class, please explain the reasons for the use in the Design.
 * 
 * @deprecated Only use access to private state as a last resort! 
 *      See the class comment.
 */ 
public class ReflectionManager {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2002. ";

    /**
     * The reflection object under management (constructor, method or field).
     */ 
    private AccessibleObject object;

    /**
     * Construct an instance of this class to manage the reflection object the
     * client is interested in - either a constructor, method or field.
     * 
     * @param object the reflection object to manage.
     * @see java.lang.reflect.Constructor
     * @see java.lang.reflect.Method
     * @see java.lang.reflect.Field
     */ 
    public ReflectionManager(AccessibleObject object) {
        this.object = object;
    }

    /**
     * Run the code provided by the executor whilst ensuring the reflection
     * object under management is accessible. 
     * 
     * @param executor provides the code to run.
     * @return any value returned by the executor.
     * @throws Exception if the executor had a problem.
     */ 
    public Object useAsAccessible(ReflectionExecutor executor) 
            throws Exception {
        
        // NOTE: this fails in IBM JDK 1.4.1 with IllegalAccessException
        // the *second* time it is run. It looks like a VM bug to me.
        // I have worked around this problem by making some previously private
        // methods of Volantis package protected for now - see 
        // VolantisInternals.
        
        boolean accessible = object.isAccessible();
        // Should not be accessible by default, otherwise using this is daft.
        Assert.assertEquals(accessible, false);
        try {
            // Make it accessible - by force!
            object.setAccessible(true);
            // Run the code which needs to access the object
            return executor.execute(object);
        } finally {
            // Ensure we reset the state so we do not disturb subsequent tests.
            object.setAccessible(accessible);
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 12-Sep-03	1295/3	geoff	VBM:2003082109 Build all jars and run the junit testsuite with IBM JDK 1.4.1 (fix up comments again)

 02-Sep-03	1295/1	geoff	VBM:2003082109 Certify & package GUIs, runtime & CLIs against IBM JRE/JDK 1.4

 ===========================================================================
*/
