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
package com.volantis.synergetics.cache;

import junit.framework.TestCase;

import java.lang.reflect.InvocationTargetException;

/**
 * A class testing the ReadThroughFutureResult object.
 */
public class ReadThroughFutureResultTestCase extends TestCase {

    /**
     * Test that a get on the FutureResult only gets the data once. This also
     * tests the set(Object) method of the future result.
     *
     * @throws Exception
     */
    public void testGetValue() throws Exception {

        ReadThroughFutureResult futureResult = new CountingFutureResult();
        // so call getValue twice and ensure that we get 0 both times.
        assertEquals("Expect getValue to return ",
                     new Integer(0), futureResult.getValue(new Object()));
        assertEquals("Expect getValue to still return ",
                     new Integer(0), futureResult.getValue(new Object()));
    }

    /**
     * Show that calling reset on the FutureResult object causes it to refetch
     * the data using its DataAccessor.
     *
     * @throws Exception
     */
    public void testGetValueAfterReset() throws Exception {

        ReadThroughFutureResult futureResult = new CountingFutureResult();
        // so call getValue twice and ensure that we get 0 both times.
        assertEquals("Expect getValue to return ",
                     new Integer(0), futureResult.getValue(new Object()));
        futureResult.reset();
        // reset and get the next value
        assertEquals("Expect getValue to still return ",
                     new Integer(1), futureResult.getValue(new Object()));
    }

    /**
     * Test the simple getters and setters of the ReadThroughFutureResult
     * object
     *
     * @throws Exception
     */
    public void testSimpleSettersGetters() throws Exception {
        ReadThroughFutureResult futureResult =
            new CountingFutureResult();
        // Time to live
        futureResult.setTimeToLive(47);
        assertEquals("Ensure that TimeToLive is set correctly",
                     47, futureResult.getTimeToLive());

        // Creation time
        futureResult.setCreationTime(457);
        assertEquals("Ensure that creation time is set correcly",
                     457, futureResult.getCreationTime());

        // Hit count
        futureResult.setHitCount(756);
        assertEquals("Ensure hit count is set correctly",
                     756, futureResult.getHitCount());

        // Last hit time.
        futureResult.setLastHitTime(384);
        assertEquals("Ensure last hit time is set correcly",
                     384, futureResult.getLastHitTime());
    }

    /**
     * Ensure that an Exception is thrown
     *
     * @throws Exception
     */
    public void testExceptionInAccessor() throws Exception {

        // simple accessor that throws an exception


        ReadThroughFutureResult futureResult =
            new ReadThroughFutureResult(new Object(), -1) {
                protected Object performUpdate(Object key) {
                    throw new IllegalStateException();
                }
            };

        try {
            futureResult.getValue(new Object());
            fail("Exception should have been thrown");
        } catch (InvocationTargetException ise) {
            // success
        }

    }


    /**
     * create a simple counting FutureResult. Each time get is called the value
     * it returns in incremented.
     */
    static class CountingFutureResult extends ReadThroughFutureResult {

        /**
         * The counter
         */
        private int counter = 0;

        CountingFutureResult() {
            super(new Object(), -1);
        }

        //javadoc inherited
        protected Object performUpdate(Object key) {
            Object result = new Integer(counter);
            counter++;
            return result;
        }
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 14-Feb-05	397/1	matthew	VBM:2005020308 Implement CachingPluggableHTTPManager

 09-Feb-05	391/1	matthew	VBM:2005020308 Make cache implementation slightly more flexible

 02-Feb-05	379/1	matthew	VBM:2005012601 Change the caching implemenation to be (hopefully) more performant

 ===========================================================================
*/
