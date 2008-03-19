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

import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.mps.localization.LocalizationFactory;

import java.util.Iterator;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.pool.PoolableObjectFactory;

/**
 * A session "pool" which actually creates a new Session for each request.
 */
public class UnpooledSessionPool extends SessionPool {

    /**
     * Used for logging.
     */
    private static final LogDispatcher LOGGER =
            LocalizationFactory.createLogger(UnpooledSessionPool.class);

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer EXCEPTION_LOCALIZER =
            LocalizationFactory
                    .createExceptionLocalizer(UnpooledSessionPool.class);

    /**
     * Initialize a new instance using the given parameters.
     *
     * @param objectFactory     used to create objects in the pool
     * @param maxSize           maximum size of the pool
     * @param testOnBorrow      true if the object should be validated
     *                          before returning it from the pool, false
     *                          otherwise
     */
    public UnpooledSessionPool(PoolableObjectFactory objectFactory,
                               int maxSize,
                               boolean testOnBorrow) {
        this.objectFactory = objectFactory;
        // Ignore max size - just create Sessions on demand.
        this.testOnBorrow = testOnBorrow;
    }

    // Javadoc inherited.
    public synchronized Object borrowObject() throws Exception {
        // Create a new bound session.
        return createNewSession();
    }

    // Javadoc inherited.
    public synchronized void returnObject(Object object) throws Exception {
        // Override parent implementation to make sure that the object is
        // destroyed, and not added to the available set.
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Attempting to return a session to the pool");
        }

        if (!active.remove(object)) {
            throw new SessionException(EXCEPTION_LOCALIZER.format(
                    "object-not-from-session-pool", object));
        }
        // Destroy the object to ensure that the Session is unbound.
        objectFactory.destroyObject(object);
    }

    // Javadoc inherited.
    protected Iterator iterator() {
        return CollectionUtils.EMPTY_ITERATOR;
    }
}
