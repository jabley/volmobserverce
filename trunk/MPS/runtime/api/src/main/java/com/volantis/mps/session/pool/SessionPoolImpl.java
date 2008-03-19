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
 * (c) Volantis Systems Ltd 2007. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mps.session.pool;

import com.volantis.mps.localization.LocalizationFactory;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

import org.apache.commons.pool.PoolableObjectFactory;
import org.smpp.Session;

/**
 * Implementation of {@link SessionPool} which provides full pooling.
 */
public class SessionPoolImpl extends SessionPool {

    /**
     * Used for logging.
     */
    private static final LogDispatcher LOGGER =
            LocalizationFactory.createLogger(SessionPoolImpl.class);

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer EXCEPTION_LOCALIZER =
            LocalizationFactory.createExceptionLocalizer(SessionPoolImpl.class);

    protected int maxSize;
    private byte whenExhaustedAction;
    private long maxWait;

    /**
     * Indicates the behaviour required when the pool is exhausted and a new
     * borrowObject request is made.
     */
    public static final byte WHEN_EXHAUSTED_FAIL = 0;
    public static final byte WHEN_EXHAUSTED_BLOCK = 1;
    public static final byte WHEN_EXHAUSTED_GROW = 2;

    /**
     * Initialize a new instance using the given parameters.
     *
     * @param objectFactory     used to create objects in the pool
     * @param maxSize           maximum size of the pool
     * @param testOnBorrow      true if the object should be validated
     *                          before returning it from the pool, false
     *                          otherwise
     */
    public SessionPoolImpl(PoolableObjectFactory objectFactory,
                           int maxSize,
                           boolean testOnBorrow) {
        this(objectFactory, maxSize, WHEN_EXHAUSTED_FAIL, -1, testOnBorrow);
    }

    /**
     * Initialize a new instance using the given parameters.
     *
     * @param objectFactory         used to create objects in the pool
     * @param maxSize               maximum size of the pool
     * @param whenExhaustedAction   what to do when the pool is exhausted;
     *                              block, grow or fail
     * @param maxWait               maximum amount of time to wait for an
     *                              object to become idle if whenExhaustedAction
     *                              is BLOCK (failing if this is exceeded)
     * @param testOnBorrow          true if the object should be validated
     *                              before returning it from the pool, false
     *                              otherwise
     */
    public SessionPoolImpl(PoolableObjectFactory objectFactory,
                           int maxSize,
                           byte whenExhaustedAction,
                           long maxWait,
                           boolean testOnBorrow) {
        this.objectFactory = objectFactory;
        this.maxSize = maxSize;
        this.whenExhaustedAction = whenExhaustedAction;
        this.maxWait = maxWait;
        this.testOnBorrow = testOnBorrow;
    }

    // Javadoc inherited.
    public synchronized Object borrowObject() throws Exception {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Attempting to borrow a session from the pool");
        }

        Session session = null;
        while (session == null) {
            if (available.isEmpty()) {
                if (hasRoom() || whenExhaustedAction == WHEN_EXHAUSTED_GROW) {
                    // There are no available sessions, but there is room to
                    // create a new one.
                    session = createNewSession();
                } else {
                    // There are no available sessions, and can't create any... how
                    // this should be handled is defined by #whenExhaustedAction
                    session = handleExhaustedPool();
                }
            } else {
                // Don't need to create a new session, there are some available
                // in the pool.
                session = getNextAvailableSession();
           }
        }

        return session;
    }

    // Javadoc inherited.
    public synchronized void addObject() throws Exception {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Attempting to add a session to the pool");
        }
        if (hasRoom()) {
            Session session = (Session) objectFactory.makeObject();
            available.add(session);
        } else {
            // can't create an object right now...
            throw new SessionException(EXCEPTION_LOCALIZER.format(
                    "session-pool-full"));
        }
    }

    /**
     * Return the maximum number of {@link Session} instances that can be in
     * the pool at one time.
     *
     * @return int the maximum number of sessions that this pool can hold
     */
    public int getMaxSize() {
        return maxSize;
    }

    // Javadoc inherited.
    public void close() throws Exception {
        // Close all active and available sessions and clear the set.
        super.close();
        maxSize = 0;
    }

    /**
     * Return a iterator which iterates over a copy of the underlying pool of
     * sessions. Modifying this will not impact the underlying pool.
     *
     * @return Iterator to iterate over the sessions in the pool
     */
    protected Iterator iterator() {
        // Copy list so we can invalidate sessions (i.e. remove them from the
        // original) while iterating through.
        ArrayList list = new ArrayList(available);
        return list.iterator();
    }

    /**
     * Return the next available session (will also be validated if the session
     * pool is configured to validate on borrow).
     *
     * @return Session the next available session. May be null if none is available
     * @throws Exception if there was a problem getting the next available session
     */
    private Session getNextAvailableSession() throws Exception {

        Session session = null;
        Iterator iterator = available.iterator();

        // Try all of the available sessions to get a valid one
        ArrayList badObjects = new ArrayList();
        while (iterator.hasNext() && session == null) {
            Object obj = iterator.next();
            if (obj instanceof Session && validateIfRequired((Session) obj)) {
                session = (Session) obj;
                active.add(session);
                available.remove(session);
            } else {
                LOGGER.warn("session-pool-contains-bad-object", obj);
                badObjects.add(obj);
            }
        }
        // Remove any bad objects from the pool.
        for (int i=0; i<badObjects.size(); i++ ){
            available.remove(badObjects.get(i));
        }
        
        return session;
    }

    /**
     * Determine what should be done when the pool is exhausted; this is
     * controlled by {@link #whenExhaustedAction}. It will either fail or block
     * (and then fail if nothing has changed).
     *
     * @return Session if one could be found, may be null
     * @throws NoSuchElementException if the pool should fail when exhausted
     * @throws InterruptedException if the pool is configured to block when
     * exhausted, and was interrupted
     * @throws Exception if there was a problem validating the session
     */
    private Session handleExhaustedPool()
            throws NoSuchElementException, InterruptedException, Exception {
        // There are no available sessions, and can't create any...
        Session session = null;
        if (whenExhaustedAction == WHEN_EXHAUSTED_FAIL) {
            throw new NoSuchElementException(
                    EXCEPTION_LOCALIZER.format("session-pool-exhausted"));
        } else if (whenExhaustedAction == WHEN_EXHAUSTED_BLOCK) {
            // block for a bit and try again...
            this.wait(maxWait);
            if (available.isEmpty()) {
                // If there's still nothing available, then fail.
                throw new NoSuchElementException(
                    EXCEPTION_LOCALIZER.format("session-pool-exhausted"));
            } else {
                session = (Session) available.iterator().next();
                // Check the session is still ok.
                if (!validateIfRequired(session)) {
                    session = null;
                }
            }
        }
        return session;
    }

    /**
     * Return true if the pool can contain an unlimited number of {@link Session}
     * instances and false otherwise.
     *
     * @return true if the pool can contain an unlimited number of
     * {@link Session} instances and false otherwise.
     */
    public boolean sizeIsInfinite() {
        return maxSize == UNLIMITED_ACTIVE_SESSIONS;
    }

    /**
     * Return true if there is room for more {@link Session} instances in this
     * pool, and false otherwise.
     *
     * @return true if there is room for more {@link Session} instances in this
     * pool, and false otherwise.
     */
    public boolean hasRoom() {
        return sizeIsInfinite() || active.size() + available.size() < maxSize;
    }
}
