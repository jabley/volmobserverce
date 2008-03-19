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

import com.volantis.synergetics.localization.LocalizationFactory;
import com.volantis.synergetics.log.LogDispatcher;

import java.lang.reflect.InvocationTargetException;

/*
  File: FutureResult.java

  Originally written by Doug Lea and released into the public domain.
  This may be used for any purposes whatsoever without acknowledgment.
  Thanks for the assistance and support of Sun Microsystems Labs,
  and everyone contributing, testing, and using this code.

  History:
  Date       Who                What
  30Jun1998  dl               Create public version
*/

/**
 * This class is derived from the FutureResult code provided by Doug Lee.
 *
 * The primary difference between this class and the original FutureResult is
 * that the first thread to call getValue() on this class is actually used to
 * perform the population of the Result. The original required an external
 * thread to perform the population. Additional functionality from the
 * CacheObject class has been modved into this.
 *
 * This class should be extended to provide required functionality when
 * getValue is called.
 *
 * @deprecated Use {@link com.volantis.cache.Cache} instead.
 */
public abstract class ReadThroughFutureResult implements Comparable {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
        LocalizationFactory.createLogger(ReadThroughFutureResult.class);

    /**
     * The result of the operation
     */
    protected Object value = null;

    /**
     * Status -- true after first set
     */
    protected boolean ready = false;

    /**
     * the exception encountered by operation producing result
     */
    protected InvocationTargetException exception = null;

    /**
     * variable used to determine if this reader is the first
     */
    protected boolean amFirst = true;

    /**
     * The key for the CacheObject.
     */
    volatile Object key;

    /**
     * Time that this object was created in millis since 1970
     */
    volatile long creationTime;

    /**
     * Number of times this object has been retrieved from the cache.
     */
    long hitCount;

    /**
     * Time of last hit in millis since 1970
     */
    long lastHitTime;

    /**
     * Time to live in seconds. A value of -1 indicates that this property
     * should not be considered when retrieving from the cache.
     */
    volatile int timeToLive = -1;

    /**
     * Create an unset FutureResult with a
     *
     * @param timeout
     */
    protected ReadThroughFutureResult(Object key, int timeout) {
        timeToLive = timeout;
        this.key = key;
        long time = System.currentTimeMillis();
        creationTime = time;
        lastHitTime = time;

    }

    /**
     * internal utility: either get the value or throw the exception. Not
     * synchronzied so should only be called from a synchronized context.
     */
    private Object doGet() throws InvocationTargetException {
        if (exception != null) {
            throw exception;
        } else {
            return value;
        }
    }

    /**
     * Access the reference, waiting if necessary until it is ready. The first
     * object to try and access the reference will call the performUpdate
     * method set in the constructor and get the data. All subsequent callers
     * will wait until the first has returned the value.
     *
     * @param key the key being used for the current request
     * @return current value
     *
     * @throws InterruptedException      if current thread has been
     *                                   interrupted
     * @throws InvocationTargetException if the operation producing the value
     *                                   encountered an exception.
     */
    protected Object get(Object key)
        throws InterruptedException, InvocationTargetException {
        // create a local copy of the amFirst varaible so that it can
        // be rechecked outside the synch block
        boolean localAmFirst = false;
        Object localObject = null;

        synchronized (this) {
            // set variable inside the synch block to ensure correct value;
            localAmFirst = amFirst;
            if (!amFirst) {
                while (!ready) {
                    wait();
                }
                localObject = doGet();
            } else {
                amFirst = false;
            }
        }
        // end of synchronization so lets go and get the data if necessary
        // this is done outsize the synchronization so that subsequent callers
        // can go into the wait() above rather then spin locking on the synch
        // barrier.
        if (localAmFirst) {
            try {
                if (logger.isDebugEnabled()) {
                    logger.debug("Getting fresh copy of data");
                }
                // get a fresh copy of the data value
                set(performUpdate(key));
                if (logger.isDebugEnabled()) {
                    logger.debug("Resetting the creation time");
                }
                // reset the creation time so that timeouts work correctly
                setCreationTime(System.currentTimeMillis());
            } catch (Throwable ex) {
                setException(ex);
            }
            // resynch and create local copy of the object to be returned.
            synchronized (this) {
                localObject = doGet();
                notifyAll();
            }
        }
        return localObject;
    }

    /**
     * Implement this method to contain the functionality required to get the
     * actual object. This method will be called when the FutureResult is first
     * added to the cache and then subsequent to calls to reset.
     *
     * @param key the key used in the current request.
     * @return the object to place as the value in this ReadThroughFutureResult
     *
     * @throws Exception on error.
     */
    protected abstract Object performUpdate(Object key) throws Exception;

    /**
     * Set the reference, and signal that it is ready. It is not considered an
     * error to set the value more than once, but it is not something you would
     * normally want to do.
     *
     * @param newValue The value that will be returned by a subsequent get();
     */
    protected synchronized void set(Object newValue) {
        value = newValue;
        ready = true;
        notifyAll();
    }

    /**
     * Set the exception field, also setting ready status.
     *
     * @param ex The exception. It will be reported out wrapped within an
     *           InvocationTargetException
     */
    protected synchronized void setException(Throwable ex) {
        exception = new InvocationTargetException(ex);
        ready = true;
        notifyAll();
    }

    /**
     * Get the exception, or null if there isn't one (yet). This does not wait
     * until the future is ready, so should ordinarily only be called if you
     * know it is.
     *
     * @return the exception encountered by the operation setting the future,
     *         wrapped in an InvocationTargetException
     */
    protected synchronized InvocationTargetException getException() {
        return exception;
    }

    /**
     * Return whether the reference or exception have been set.
     *
     * @return true if has been set. else false
     */
    public synchronized boolean isReady() {
        return ready;
    }

    /**
     * Access the reference, even if not ready
     *
     * @return current value
     */
    public synchronized Object peek() {
        return value;
    }

    /**
     * Reset this object so that the next read will repopulate the result.
     */
    public synchronized void reset() {
        ready = false;
        amFirst = true;
        value = null;
        exception = null;
    }

    /**
     * Get the value of lastHitTime.
     *
     * @return Value of lastHitTime.
     */
    public synchronized long getLastHitTime() {
        return lastHitTime;
    }

    /**
     * Set the value of lastHitTime.
     *
     * @param v Value to assign to lastHitTime.
     */
    public synchronized void setLastHitTime(long v) {
        this.lastHitTime = v;
    }

    /**
     * Get the value of hitCount.
     *
     * @return Value of hitCount.
     */
    public synchronized long getHitCount() {
        return hitCount;
    }

    /**
     * Set the value of hitCount.
     *
     * @param v Value to assign to hitCount.
     */
    public synchronized void setHitCount(long v) {
        this.hitCount = v;
    }

    /**
     * @return Value of creationTime.
     */
    public synchronized long getCreationTime() {
        return creationTime;
    }

    /**
     * Set the value of creationTime.
     *
     * @param v Value to assign to creationTime.
     */
    public synchronized void setCreationTime(long v) {
        this.creationTime = v;
    }

    /**
     * @return the key
     */
    public synchronized Object getKey() {
        return key;
    }

    /**
     * Set the key of this CacheObject.
     *
     * @param key The key.
     */
    public synchronized void setKey(Object key) {
        this.key = key;
    }

    /**
     * Get the value of value.
     *
     * NOTE: This is not synchronized but is thread safe.
     *
     * @return Value of value.
     */
    public Object getValue(Object key)
        throws InterruptedException, InvocationTargetException {
        long time = System.currentTimeMillis();
        int timeToLive = getTimeToLive();

        if (timeToLive != GenericCacheConfiguration.FOREVER) {
            // The object might have timed out.
            boolean timedOut = (time - getCreationTime()) / 1000 >=
                timeToLive;
            if (timedOut) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Value for " + key + " has timed out");
                }
                // reset myself
                reset();
            }
        }
        return get(key);
    }

    /**
     * Set the timeToLive value.
     *
     * @param timeToLive The timeToLive in seconds. A value of -1 will unset
     *                   this property.
     */
    public synchronized void setTimeToLive(int timeToLive) {
        if (timeToLive < -1) {
            throw new IllegalArgumentException("timeToLive must >= -1; was " +
                                               timeToLive);
        }
        this.timeToLive = timeToLive;
    }

    /**
     * Get the timeToLive value. -1 indicates this property is not set.
     *
     * @return timeToLive in seconds.
     */
    public synchronized int getTimeToLive() {
        return timeToLive;
    }

    /**
     * This is not synchronized but is thread safe.
     *
     * @return true if this object equals other.
     */
    public boolean equals(Object o) {
        return o.toString().equals(this.toString());
    }

    /**
     * @return the hashcode. This is inefficient and only exists because equals
     *         does.
     */
    public int hashCode() {
        return toString().hashCode();
    }

    /**
     * This is not synchronized but is thread safe.
     *
     * @return a String representation of this object
     */
    public String toString() {
        String result = null;
        try {
            // pass in the key that was used to create me.
            result = get(getKey()).toString();
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use Options | File Templates.
        } catch (InvocationTargetException e) {
            e.printStackTrace();  //To change body of catch statement use Options | File Templates.
        }
        return result;
    }

    /**
     * This is not synchronized but is thread safe.
     *
     * @param o the object to compare this with
     * @return +, 0 or - if this is greater then, equal to or less then o.
     */
    public int compareTo(Object o) {
        return o.toString().compareTo(this.toString());
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 12-Aug-05	518/2	pcameron	VBM:2005072607 Extra logging for caching

 31-May-05	469/1	matthew	VBM:2005053107 when a cache entry timeouts it times out for ever

 14-Feb-05	397/1	matthew	VBM:2005020308 Implement CachingPluggableHTTPManager

 14-Feb-05	391/5	matthew	VBM:2005020308 Javadoc changes

 10-Feb-05	391/3	matthew	VBM:2005020308 Change Clock to show real time in millis rather then a counter

 09-Feb-05	391/1	matthew	VBM:2005020308 Make cache implementation slightly more flexible

 03-Feb-05	379/7	matthew	VBM:2005012601 Change the caching implemenation to be (hopefully) more performant

 02-Feb-05	379/3	matthew	VBM:2005012601 Change the caching implemenation to be (hopefully) more performant

 02-Feb-05	379/1	matthew	VBM:2005012601 Change the caching implemenation to be (hopefully) more performant

 ===========================================================================
*/
