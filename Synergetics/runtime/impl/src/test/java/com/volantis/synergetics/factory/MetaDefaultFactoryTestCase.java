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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.synergetics.factory;

import junit.framework.TestCase;

import com.volantis.synergetics.UndeclaredThrowableException;

import java.io.OutputStream;
import java.util.Map;

/**
 * SImple tests ojnt he Meta factory
 */
public class MetaDefaultFactoryTestCase extends TestCase {

    /**
     * standard unit test constructor
     *
     * @param name the name of the test case
     */
    public MetaDefaultFactoryTestCase(String name) {
        super(name);
    }

    /**
     * Check that exceptions are not thrown in normal use
     *
     * @throws Exception
     */
    public void testNormalFactoryCreation() throws Exception {
        AbstractExampleMapFactory factory = AbstractExampleMapFactory.getDefaultInstance();
        Map map = factory.createMap();
    }

    /**
     * Check that exceptions are thrown when a factory implementaion can not be
     * instantiated.
     *
     * @throws Exception
     */
    public void testBrokenFactoryCreation() throws Exception {
        try {
            AbstractBrokenFactory factory = AbstractBrokenFactory.getDefaultInstance();
            // exception no thrown so fail
            fail();
        } catch (UndeclaredThrowableException e) {
            // caught specified exception so it is a success
        }
    }

    /**
     * Simple factory that is broken (it cannot load its implementation)
     */
    static abstract class AbstractBrokenFactory {

        /**
         * create the MetaDefaultFactory
         */
        static MetaDefaultFactory instance = new MetaDefaultFactory(
            "gibberish.class.name",
            AbstractBrokenFactory.class.getClassLoader());

        /**
         * get the default broken factory instance
         *
         * @return
         */
        public static AbstractBrokenFactory getDefaultInstance() {
            return (AbstractBrokenFactory) instance.getDefaultFactoryInstance();
        }

        /**
         * What the broken factory would produce if it was not broken
         *
         * @return an OutputStream
         */
        public abstract OutputStream getOutputStream();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 27-Jun-05	487/1	matthew	VBM:2005062701 Create a DefaultFactory factory

 ===========================================================================
*/
