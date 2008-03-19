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

import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.mps.localization.LocalizationFactory;

import java.util.Iterator;
import java.util.Set;
import java.util.HashSet;

import org.apache.commons.pool.BaseObjectPool;
import org.apache.commons.pool.PoolableObjectFactory;
import org.smpp.Session;

/**
 * Common superclass for both the actual and unpooled (i.e. creates a new
 * session for each request) session pool implementations.
 *
 * @mock.generate
 */
public abstract class SessionPool extends BaseObjectPool {

    /**
     * Used for logging.
     */
    private static final LogDispatcher LOGGER =
            LocalizationFactory.createLogger(SessionPool.class);

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer EXCEPTION_LOCALIZER =
            LocalizationFactory.createExceptionLocalizer(SessionPool.class);

    /**
     * Set containing all the active sessions in this pool.
     */
    protected Set active = new HashSet();

    /**
     * Set containing all the available (i.e. idle) sessions in this pool.
     */
    protected Set available = new HashSet();

    /**
     * Used to create {@link Session} instances.
     */
    protected PoolableObjectFactory objectFactory;

    /**
     * Indicates whether each session should be validated (using
     * {@link PoolableObjectFactory#validateObject(Object)} before being
     * returned from {@link #borrowObject}
     */
    protected boolean testOnBorrow;

    /**
     * Indicates that this session pool can have unlimited active sessions.
     */
    public static final int UNLIMITED_ACTIVE_SESSIONS = -1;

    // Javadoc inherited.
    public synchronized void returnObject(Object object) throws Exception {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Attempting to return a session to the pool");
        }

        if (!active.remove(object)) {
            throw new SessionException(EXCEPTION_LOCALIZER.format(
                    "object-not-from-session-pool", object));
        }
        available.add(object);
    }

    // Javadoc inherited.
    public int getNumActive() throws UnsupportedOperationException {
        return active.size();
    }

    // Javadoc inherited.
    public int getNumIdle() throws UnsupportedOperationException {
        return available.size();
    }

    // Javadoc inherited.
    public synchronized void clear() throws Exception,
            UnsupportedOperationException {
        // Close all available sessions and clear the set.
        for (Iterator i = available.iterator(); i.hasNext();) {
            Session s = (Session) i.next();
            s.close();
        }
        available.clear();
    }

    // Javadoc inherited.
    public synchronized void close() throws Exception {
        // Close all active sessions and clear the set.
        for (Iterator i = active.iterator(); i.hasNext();) {
            Session s = (Session) i.next();
            s.close();
        }
        active.clear();

        // Close all available sessions and clear the set.
        clear();
    }

    // Javadoc inherited.
    public synchronized void invalidateObject(Object object) throws Exception {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Attempting to invalidate a session");
        }

        if (!available.remove(object) && !active.remove(object)) {
            throw new SessionException(EXCEPTION_LOCALIZER.format(
                    "object-not-from-session-pool", object));
        }
    }

    // Javadoc inherited.
    public void setFactory(PoolableObjectFactory poolableObjectFactory)
            throws IllegalStateException, UnsupportedOperationException {
        this.objectFactory = poolableObjectFactory;
    }
    
    /**
     * Return a iterator which iterates over a copy of the underlying pool of
     * sessions. Modifying this will not impact the underlying pool.
     *
     * @return Iterator to iterate over the sessions in the pool
     */
    protected abstract Iterator iterator();

    /**
     * Create a new active session (validating it if validation on borrowing is
     * required).
     *
     * @return Session new active session
     * @throws Exception if there was a problem creating the new session.
     */
    protected Session createNewSession() throws Exception {
        Session session = (Session) objectFactory.makeObject();
        active.add(session);

        if (!validateIfRequired(session)) {
            // If a new session is invalid we have a problem, so throw
            // an exception and log a fatal error.
            final String key = "cannot-create-session";
            LOGGER.fatal(key);
            throw new SessionException(EXCEPTION_LOCALIZER.format(key));
        }

        return session;
    }

    /**
     * Ensure that the session is valid, and if not invalidate it. If this
     * method returns false, the session will be nulled.
     *
     * @param session   to validate
     * @return true if the session is valid or if validation is not required,
     * false if the session is not valid (and has now been invalidated)
     * @throws Exception if there was a problem validating the session
     */
    protected boolean validateIfRequired(Session session) throws Exception {
        boolean valid = true;
        if (testOnBorrow) {
            valid = objectFactory.validateObject(session);
            if (!valid) {
                invalidateObject(session);
                session = null;
            }
        }
        return valid;
    }

    /**
     * Return true if the objects returned by this pool will be validated when
     * borrowed, and false otherwise
     *
     * @return true if the objects returned by this pool will be validated when
     * borrowed, and false otherwise
     */
    public boolean isValidatingOnBorrow() {
        return testOnBorrow;
    }
}
