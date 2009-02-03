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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Unit tests to ensure that the DataAccessor mechanism actualy works.
 */
public class DataAccessorTestCase extends TestCase {


    /**
     * Test that the read through mechanism works with a simple array based
     * data accessor.
     *
     * @throws Exception
     */
    public void testSimpleArrayReadThrough() throws Exception {

        // set up a backing store
        Random rnd = new Random(System.currentTimeMillis());
        final int MAX_SIZE = 1000;
        Integer[] data = new Integer[MAX_SIZE];
        for (int i = 0; i < MAX_SIZE; i++) {
            data[i] = new Integer(rnd.nextInt());
        }

        // create the cache.
        GenericCache gc = new LimitedCache(new ArrayFutureResultFactory(data),
                                           MAX_SIZE,
                                           new LeastRecentlyUsedCacheStrategy());

        // run through the data and get the values from the cache. In this
        // rather contrived situation the values returned should equal the key
        // used.
        for (int i = 0; i < data.length; i++) {
            Integer value = (Integer) gc.get(new Integer(i));
            assertEquals("values must be the same", data[i], value);
        }

    }

    public static void main(String[] args) {
        DataAccessorTestCase d = new DataAccessorTestCase();
        try {
            d.doTestMultithreadedSimpleArrayReadThrough();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Test that the read through mechanism works with a simple array based
     * data accessor.
     *
     * @throws Exception
     */
    public void doTestMultithreadedSimpleArrayReadThrough() throws Exception {

        // set up a backing store
        Random rnd = new Random(System.currentTimeMillis());
        final int MAX_SIZE = 1000;
        Integer[] data = new Integer[MAX_SIZE];
        for (int i = 0; i < MAX_SIZE; i++) {
            data[i] = new Integer(rnd.nextInt());
        }

        // create the cache.
        GenericCache gc = new LimitedCache(new ArrayFutureResultFactory(data),
                                           MAX_SIZE,
                                           new LeastRecentlyUsedCacheStrategy());

        final int numThreads = 1000;
        final Object lock = new Object();
        List threadList = new ArrayList();
        for (int i = 0; i < numThreads; i++) {
            Thread t = new Thread(
                new ReaderThread(numThreads, lock, i, data, gc));
            t.start();
            threadList.add(t);
        }

        Iterator iterator = threadList.iterator();
        while (iterator.hasNext()) {
            Thread t = (Thread) iterator.next();
            t.join();
        }
    }


    /**
     * Simple factory that creates accessors which access an internal array of
     * integers. The keys used to query the cache MUST be Integers.
     */
    static class ArrayFutureResultFactory extends FutureResultFactory {

        private Integer[] data;

        public ArrayFutureResultFactory(Integer[] data) {
            super();
            if (data == null) {
                throw new IllegalArgumentException("data must not be null");
            }
            this.data = data;
        }

        /**
         * @return a FutureResult object that indexes directly into the
         *         internal data array.
         */
        protected ReadThroughFutureResult
            createCustomFutureResult(Object key, int timeToLive) {

            return new ReadThroughFutureResult(key, timeToLive) {
                protected Object performUpdate(Object key) {
                    return data[((Integer) key).intValue()];
                }
            };
        }
    }

    public static class ReaderThread implements Runnable {

        private int numThreads = 0;

        private Object lock;

        private int myId;

        private Integer[] data;

        private GenericCache gc;

        ReaderThread(int numThreads, Object lock, int myId, Integer[] data,
                     GenericCache gc) {
            this.numThreads = numThreads;
            this.lock = lock;
            this.myId = myId;
            this.data = data;
            this.gc = gc;
        }

        public void run() {
            try {
                Random rnd = new Random(myId);
                synchronized (lock) {
                    if (myId != numThreads - 1) {
                        try {
                            System.err.println("Thread " + myId + " waiting");
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else {
                        System.err.println(
                            "Thread " + myId + " calling notifyAll");
                        lock.notifyAll();
                    }
                }
                for (int i = 0; i < 10000; i++) {
                    int index = Math.abs(rnd.nextInt() % data.length);
                    assertEquals("Not what I expected", data[index],
                                 gc.get(new Integer(index)));
                }
                System.err.println("Thread " + myId + " Finshed ");

            } catch (Throwable t) {
                t.printStackTrace();
                fail("Exception thrown" + t);

            }
        }
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 14-Feb-05	397/1	matthew	VBM:2005020308 Implement CachingPluggableHTTPManager

 14-Feb-05	391/3	matthew	VBM:2005020308 More minor changes

 09-Feb-05	391/1	matthew	VBM:2005020308 Make cache implementation slightly more flexible

 03-Feb-05	379/5	matthew	VBM:2005012601 Change the caching implemenation to be (hopefully) more performant

 02-Feb-05	379/3	matthew	VBM:2005012601 Change the caching implemenation to be (hopefully) more performant

 02-Feb-05	379/1	matthew	VBM:2005012601 Change the caching implemenation to be (hopefully) more performant

 ===========================================================================
*/
