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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.eclipse.common;

import com.volantis.synergetics.testtools.TestCaseAbstract;

import java.util.ResourceBundle;
import java.util.MissingResourceException;
import java.util.Enumeration;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Test case for EclipseCommonMessages.
 */
public class EclipseCommonMessagesTestCase extends TestCaseAbstract {

    /**
     * Test getString() positive i.e. the test succeeds if the requested
     * property is found and returned as a String.
     */
    public void testGetStringPositive() {
        String key = "key";
        String value = "value";
        MapResourceBundle bundle = new MapResourceBundle();

        bundle.putProperty(key, value);

        String result = EclipseCommonMessages.getString(bundle, key);

        assertEquals(value, result);
    }


    /**
     * Test getString() negative i.e. the test succeeds if the requested
     * property is not found.
     *
     * Note that this test is compromised by the fact that an Eclipse
     * environment is required for the failing version of getString() to
     * work - since it calls logError() in EclipseCommonMessage. So though
     * the call will fail it will not fail in the same way as it would in
     * an Eclipse environment.
     */
    public void testGetStringNegative() {
        MapResourceBundle bundle = new MapResourceBundle();
        String result = null;
        // Within an Eclipse environment an exception would not be
        // expected. Normally a MissingResourceException would be logged
        // and null returned. Outside of Eclipse the call to create the log
        // message will generate an exception - so we expect an exception.
        try {
            result = EclipseCommonMessages.getString(bundle, "nothing");
            fail("An exception should have been thrown since we out" +
                    "outside the Eclipse environment when logError should" +
                    "have been called");
        } catch (Exception e) {
            // ok so far
        }

        assertNull(result);
    }

    /**
     * Test getInteger() positive i.e. the test succeeds if the requested
     * property is found and returned as a String.
     */
    public void testGetIntegerPositive() {
        String key = "key";
        String value = "1";
        MapResourceBundle bundle = new MapResourceBundle();

        bundle.putProperty(key, value);

        Integer result = EclipseCommonMessages.getInteger(bundle, key);

        Integer expected = new Integer(1);

        assertEquals(expected, result);
    }

    /**
     * Tests the {@link EclipseCommonMessages#getString} fallback method works
     * @throws Exception if an error occurs
     */
    public void testGetStringWithfallbackPropertyExists() throws Exception {
        String key = "key";
        String value = "value";

        MapResourceBundle bundle = new MapResourceBundle();
        bundle.putProperty(key, value);

        String result = EclipseCommonMessages.getString(bundle,
                                                        key,
                                                        "fallback");
        assertEquals("Value not as", value, result);
    }

    /**
     * Tests the {@link EclipseCommonMessages#getString} fallback method works
     * @throws Exception if an error occurs
     */
    public void testGetStringWithfallbackPropertyDoesNotExist()
        throws Exception {

        String key = "key";
        MapResourceBundle bundle = new MapResourceBundle();

        String result = EclipseCommonMessages.getString(bundle,
                                                        key,
                                                        "fallback");
        assertEquals("Value not as", "fallback", result);
    }

    /**
     * Test getInteger() negative i.e. the test succeeds if the requested
     * property is not found.
     *
     * Note that this test is compromised by the fact that an Eclipse
     * environment is required for the failing version of getIntger() to
     * work - since it calls logError() in EclipseCommonMessage. So though
     * the call will fail it will not fail in the same way as it would in
     * an Eclipse environment.
     */
    public void testGetIntegerNegative() {
        MapResourceBundle bundle = new MapResourceBundle();
        Integer result = null;        
        // Within an Eclipse environment an exception would not be
        // expected. Normally a MissingResourceException would be logged
        // and null returned. Outside of Eclipse the call to create the log
        // message will generate an exception - so we expect an exception.
        try {
            result = EclipseCommonMessages.getInteger(bundle, "nothing");
            fail("An exception should have been thrown since we out" +
                    "outside the Eclipse environment when logError should" +
                    "have been called");
        } catch (Exception e) {
            // ok so far
        }

        assertNull(result);
    }

    /**
     * An implementation of ResourceBundle based on a Map so that there
     * is no need for a properties file.
     */
    private static class MapResourceBundle extends ResourceBundle {
        private Map properties = new HashMap();

        /**
         * Implement handleGetObject() by delegating to the properties
         * HashMap.
         */
        // rest of javadoc inherited
        protected Object handleGetObject(String key)
                throws MissingResourceException {
            Object o = properties.get(key);
            if(o==null) {
                throw new MissingResourceException("Could not find property.",
                        this.getClass().getName(), key);
            }

            return o;
        }

        /**
         * Implement getKeys() by delegating to the properties HashMap.
         */
        // rest of javadoc inherited
        public Enumeration getKeys() {
            final Iterator iterator =  properties.keySet().iterator();
            return new Enumeration() {
                public boolean hasMoreElements() {
                    return iterator.hasNext();
                }

                public Object nextElement() {
                    return iterator.next();
                }
            };
        }

        /**
         * Put a property into the TestResourceBundle.
         * @param key The property key.
         * @param value The property value.
         * @return The property replaced by this one or null if none were
         * replaced.
         */
        public Object putProperty(Object key, Object value) {
            return properties.put(key, value);
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

 06-Jan-04	2323/1	doug	VBM:2003120701 Added better validation error messages

 13-Oct-03	1549/1	allan	VBM:2003101302 Eclipse Common plugin

 ===========================================================================
*/
