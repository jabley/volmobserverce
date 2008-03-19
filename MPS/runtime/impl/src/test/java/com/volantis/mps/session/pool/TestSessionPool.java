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

import com.volantis.mps.session.pool.SessionPoolImpl;
import com.volantis.mps.session.pool.PoolableSessionFactory;

/**
 * Test class which extends {@link SessionPoolImpl} and allows a poolto be
 * created in a particular state.
 */
public class TestSessionPool extends SessionPoolImpl {

    /**
     * Configuration details for the poolable session factory under test.
     */
    private static final String ADDRESS = "127.0.0.1";
    private static final int PORT = 54321;
    private static final String SMSC_USERNAME = "username";
    private static final String SMSC_PASSWORD = "password";
    private static final String SMSC_BINDTYPE = "async";

    /**
     * Create a session pool in the specified state.
     *
     * @param maxActive     maximum number of active sessions that this pool
     *                      can manage
     * @param numActive     the number of sessions that should be active
     * @param numIdle       the number of sessions that should be idle and
     *                      therefore available
     * @throws Exception if there was a problem creating the session pool
     */
    public TestSessionPool(int maxActive, int numActive, int numIdle)
            throws Exception {
        this(maxActive, numActive, numIdle, false);
    }

    /**
     * Create a session pool in the specified state.
     *
     * @param maxActive     maximum number of active sessions that this pool
     *                      can manage
     * @param numActive     the number of sessions that should be active
     * @param numIdle       the number of sessions that should be idle and
     *                      therefore available
     * @param testOnBorrow  true if the sessions should be validated by the pool
     *                      before being returned, false otherwise
     * @throws Exception if there was a problem creating the session pool
     */
    public TestSessionPool(int maxActive,
                           int numActive,
                           int numIdle,
                           boolean testOnBorrow) throws Exception {
        this(new PoolableSessionFactory(ADDRESS, PORT, SMSC_USERNAME,
                SMSC_PASSWORD, SMSC_BINDTYPE), 
                maxActive,
                numActive,
                numIdle,
                testOnBorrow);
    }

    public TestSessionPool(PoolableSessionFactory sessionFactory,
                           int maxActive,
                           int numActive,
                           int numIdle,
                           boolean testOnBorrow) throws Exception {
        super(sessionFactory, maxActive, testOnBorrow);
        int numCreated = numActive + numIdle;
        for (int i=1; i<= numCreated; i++) {
            addObject();
        }
        for (int i=1; i<=numActive; i++) {
            borrowObject();
        }
    }
}
