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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.synergetics.factory;

import junit.framework.TestCase;

import com.volantis.synergetics.UndeclaredThrowableException;


public class MetaFactoryTestCase extends TestCase {

    public void testNoSuchClass() throws Exception {
        MetaFactory metaFactory = new MetaFactory("no.such.ClassName",
                                                  MetaFactoryTestCase.class.getClassLoader());
        try {
            metaFactory.createInstance();
            fail("There shouldn't be any such class");
        } catch (UndeclaredThrowableException expected) {
            assertEquals(ClassNotFoundException.class,
                         expected.getUndeclaredThrowable().getClass());
        }
    }

    public void testNoParameters() throws Exception {
        MetaFactory metaFactory = new MetaFactory(
            "com.volantis.synergetics.factory.SampleForReflectiveInstantiation",
            MetaFactoryTestCase.class.getClassLoader());
        SampleForReflectiveInstantiation theObject =
            (SampleForReflectiveInstantiation) metaFactory.createInstance();

        assertNotNull(theObject);
        assertNull(theObject.getField());
    }


    public void testSingleParameter() throws Exception {
        MetaFactory metaFactory = new MetaFactory(
            "com.volantis.synergetics.factory.SampleForReflectiveInstantiation",
            MetaFactoryTestCase.class.getClassLoader(),
            new Class[]{String.class});
        SampleForReflectiveInstantiation instance =
            (SampleForReflectiveInstantiation) metaFactory.createInstance(
                new Object[]{"hello"});

        assertNotNull(instance);
        assertEquals("hello", instance.getField());
    }
}
