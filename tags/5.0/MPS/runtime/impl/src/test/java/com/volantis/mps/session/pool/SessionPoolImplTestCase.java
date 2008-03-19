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

import com.volantis.mps.channels.MessageListener;
import com.volantis.mps.channels.TestSimulator;
import com.volantis.mps.localization.LocalizationFactory;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.testtools.mock.ExpectationBuilder;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.smpp.Session;
import org.smpp.TCPIPConnection;

/**
 * Verifies that {@SessionPool} works as expected.
 */
public class SessionPoolImplTestCase extends TestCaseAbstract {

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer EXCEPTION_LOCALIZER =
            LocalizationFactory
                    .createExceptionLocalizer(SessionPoolImplTestCase.class);

    /**
     * Configuration details for the poolable session factory under test.
     */
    private static final String ADDRESS = "127.0.0.1";
    private static final int PORT = 54321;
    private static final String SMSC_USERNAME = "username";
    private static final String SMSC_PASSWORD = "password";
    private static final String ASYNC_BINDTYPE = "async";
    private static final String SYNC_BINDTYPE = "sync";

    PoolableSessionFactory sessionFactory;
    private TestSimulator testSimulator;

    // Javadoc inherited.
    protected void setUp() throws Exception {
        super.setUp();
        sessionFactory = new PoolableSessionFactory(
                ADDRESS, PORT, SMSC_USERNAME, SMSC_PASSWORD, ASYNC_BINDTYPE);

        // Create the mock SMSC and start it listening.
        testSimulator = new TestSimulator(PORT);
        MessageListener listener = new MessageListener();
        testSimulator.start(listener);
    }

    // Javadoc inherited.
    public void tearDown() throws Exception {
        super.tearDown();
        testSimulator.stop();
    }

    /**
     * Verify that objects retrieved from the Iterator supplied by
     * {@link SessionPoolImpl#iterator} can be invalidated without causing a
     * ConcurrentModificationException.
     *
     * @throws Exception if there was a problem running the test
     */
    public void testIterator() throws Exception {
        SessionPoolImpl pool = new TestSessionPool(10, 3, 7);
        Iterator i = pool.iterator();
        int count = 0;
        // Should invalidate the available sessions (not any currently active).
        while (i.hasNext()) {
            Session session = (Session) i.next();
            pool.invalidateObject(session);
            count++;
        }
        assertEquals(7, count);
    }

    public void testBorrowObjectWhenEmpty() throws Exception {
        SessionPoolImpl pool = new TestSessionPool(5, 0, 0);
        assertEquals(0, pool.getNumActive());
        assertEquals(0, pool.getNumIdle());
        Session session = (Session) pool.borrowObject();
        assertEquals(1, pool.getNumActive());
        assertEquals(0, pool.getNumIdle());
    }

    public void testBorrowObjectWhenSessionIsAvailable() throws Exception {
        SessionPoolImpl pool = new TestSessionPool(5, 0, 1);
        assertEquals(0, pool.getNumActive());
        assertEquals(1, pool.getNumIdle());
        Session session = (Session) pool.borrowObject();
        assertEquals(1, pool.getNumActive());
        assertEquals(0, pool.getNumIdle());
    }

    public void testBorrowObjectWhenAlreadyMaxedOut() throws Exception {
        SessionPoolImpl pool = new TestSessionPool(5, 5, 0);
        assertEquals(5, pool.getNumActive());
        assertEquals(0, pool.getNumIdle());
        try {
            pool.borrowObject();
            fail("Should throw a NoSuchElementException when calling " +
                    "borrowObject on a exhausted pool");
        } catch (NoSuchElementException e) {
            // Do nothing - correct behaviour.
        }

        // Verify that didn't change the state.
        assertEquals(5, pool.getNumActive());
        assertEquals(0, pool.getNumIdle());
    }

    /**
     * Verify that when if validating on borrow, it is not possible to retrieve
     * an object in an invalid state from the pool; either it will invalidate
     * that one and return a valid one, or an exception will be thrown.
     * <p/>
     * This is testing an error case required by R27644; if the connection in
     * the pool is found to be invalid before usage, it should log a warning
     * and re-establish the connection if possible.
     *
     * @throws Exception if there was a problem running the test
     */
    public void testBorrowObjectWhenInvalidAndValidatingOnBorrow()
            throws Exception {
        // Make sure that the factory is communicating synchronously with the
        // SMSC otherwise validation will ALWAYS fail.
        PoolableSessionFactory sessionFactory = new PoolableSessionFactory(
                ADDRESS, PORT, SMSC_USERNAME, SMSC_PASSWORD, SYNC_BINDTYPE);
        SessionPoolImpl pool =
                new TestSessionPool(sessionFactory, 5, 0, 0, true);

        // Add two invalid objects and one valid session.
        Object invalidObject = new Object();
        pool.available.add(invalidObject);
        pool.addObject();
        Object invalidObjectTwo = new Object();
        pool.available.add(invalidObjectTwo);

        assertEquals(0, pool.getNumActive());
        assertEquals(3, pool.getNumIdle());

        // Should ignore the first invalid element and return the second valid
        // session.
        Session session = (Session) pool.borrowObject();

        // Can't check the number of idle Sessions at this point, because the
        // objects in the set could be returned in any order.
        assertEquals(1, pool.getNumActive());

        // There was only one valid session in the pool, so when a second one
        // is requested this should guarantee that the invalid ones are removed
        // and a new one is created (because we still have room).
        Session sessionTwo = (Session) pool.borrowObject();
        assertNotEquals(invalidObject, sessionTwo);
        assertNotEquals(invalidObjectTwo, sessionTwo);
        assertNotEquals(session, sessionTwo);
        assertEquals(2, pool.getNumActive());
        assertEquals(0, pool.getNumIdle());
    }

    public void testBorrowObjectWhenInvalidAndNotValidatingOnBorrow()
            throws Exception {
        SessionPoolImpl pool = new TestSessionPool(5, 0, 0, false);

        Object invalidObject = new Object();
        pool.available.add(invalidObject);
        pool.addObject();
        Object invalidObjectTwo = new Object();
        pool.available.add(invalidObjectTwo);

        assertEquals(0, pool.getNumActive());
        assertEquals(3, pool.getNumIdle());

        // Should ignore the first invalid element and return the second valid
        // session.
        Session session = (Session) pool.borrowObject();

        // Can't check the number of idle Sessions at this point, because the
        // objects in the set could be returned in any order.
        assertEquals(1, pool.getNumActive());

        // There was only one valid session in the pool, so when a second one
        // is requested this should guarantee that the invalid ones are removed
        // and a new one is created (because we still have room).
        Session sessionTwo = (Session) pool.borrowObject();
        assertNotEquals(invalidObject, sessionTwo);
        assertNotEquals(invalidObjectTwo, sessionTwo);
        assertNotEquals(session, sessionTwo);
        assertEquals(2, pool.getNumActive());
        assertEquals(0, pool.getNumIdle());
    }

    public void testReturnObjectWhenFromPool() throws Exception {
        SessionPoolImpl pool = new TestSessionPool(5, 0, 0);
        Session session = (Session) pool.borrowObject();
        assertEquals(1, pool.getNumActive());
        assertEquals(0, pool.getNumIdle());
        pool.returnObject(session);
        assertEquals(0, pool.getNumActive());
        assertEquals(1, pool.getNumIdle());
    }


    public void testReturnObjectWhenNotFromPool() throws Exception {
        SessionPoolImpl pool = new TestSessionPool(5, 0, 0);
        pool.borrowObject();
        assertEquals(1, pool.getNumActive());
        assertEquals(0, pool.getNumIdle());
        Session session = (Session) sessionFactory.makeObject();
        try {
            pool.returnObject(session);
            fail("Should throw an exception if attempting to return an " +
                    "object to a pool which wasn't in it.");
        } catch (SessionException e) {
            // Do nothing, correct behaviour.
        }
    }

    public void testReturnObjectWhenNoneActive() throws Exception {
        SessionPoolImpl pool = new TestSessionPool(5, 0, 0);
        // Try to return an object that did come from this pool, but is not
        // active at present (returning objects that didn't come from this pool
        // is captured in #testBorrowObjectWhenNotFromPool)
        Session session = (Session) pool.borrowObject();
        pool.returnObject(session);
        try {
            pool.returnObject(session);
            fail("Should throw an exception if attempting to return an " +
                    "object to a pool which wasn't in it.");
        } catch (SessionException e) {
            // Do nothing, correct behaviour.
        }
    }

    public void testInvalidateActiveSession() throws Exception {
        SessionPoolImpl pool = new TestSessionPool(5, 0, 0);
        Session session = (Session) pool.borrowObject();
        assertEquals(1, pool.getNumActive());
        assertEquals(0, pool.getNumIdle());
        pool.invalidateObject(session);
        assertEquals(0, pool.getNumActive());
        assertEquals(0, pool.getNumIdle());
    }

    public void testInvalidateAvailableSession() throws Exception {
        SessionPoolImpl pool = new TestSessionPool(5, 0, 1);
        assertEquals(1, pool.available.size());
        Session session = (Session)pool.available.iterator().next();
        assertEquals(0, pool.getNumActive());
        assertEquals(1, pool.getNumIdle());
        pool.invalidateObject(session);
        assertEquals(0, pool.getNumActive());
        assertEquals(0, pool.getNumIdle());
    }

    public void testInvalidateUnknownSession() throws Exception {
        SessionPoolImpl pool = new TestSessionPool(5, 1, 1);
        assertEquals(1, pool.getNumActive());
        assertEquals(1, pool.getNumIdle());
        Session session = (Session) sessionFactory.makeObject();

        try {
            pool.invalidateObject(session);
            fail("Should throw an exception if attempting to invalidate an " +
                    "object which isn't from this pool");
        } catch (SessionException e) {
            // Do nothing, correct behaviour.
            assertEquals(1, pool.getNumActive());
            assertEquals(1, pool.getNumIdle());
        }
    }

    public void testAddObjectWhenRoom() throws Exception {
        SessionPoolImpl pool = new TestSessionPool(5, 0, 0);
        pool.addObject();
        assertEquals(0, pool.getNumActive());
        assertEquals(1, pool.getNumIdle());
    }

    public void testAddObjectMaxedOut() throws Exception {
        SessionPoolImpl pool = new TestSessionPool(5, 2, 3);
        try {
            pool.addObject();
            fail("Should throw an SessionException when " +
                    "attempting to add an object to a full pool");
        } catch (SessionException e) {
            // Do nothing - correct behaviour.
        }

        // Verify that didn't change the state.
        assertEquals(2, pool.getNumActive());
        assertEquals(3, pool.getNumIdle());
    }

    /**
     * Verify that this error case (required by R27644) is supported. If the
     * connection in the pool is found to be invalid before usage, it should
     * log a warning and re-establish the connection if possible.
     *
     * @throws Exception if there was a problem running the test
     */
    public void testSessionInvalidBeforeUsage() throws Exception {
        // Create test objects.
        ExpectationBuilder orderedExpectations =
                mockFactory.createOrderedBuilder();
        PoolableSessionFactoryMock sessionFactory =
                new PoolableSessionFactoryMock("sessionFactory",
                        orderedExpectations, ADDRESS, PORT, SMSC_USERNAME,
                        SMSC_PASSWORD, SYNC_BINDTYPE);
        Session badSession = new Session(new TCPIPConnection(ADDRESS, PORT));
        Session goodSession = new Session(new TCPIPConnection(ADDRESS, PORT));
        // Set expectations.
        sessionFactory.expects.validateObject(badSession).returns(false);
        sessionFactory.expects.makeObject().returns(goodSession);
        sessionFactory.expects.validateObject(goodSession).returns(true);

        // Run test.
        SessionPoolImpl sessionPool = new SessionPoolImpl(sessionFactory, 3, true);
        assertEquals(0, sessionPool.getNumActive());
        assertEquals(0, sessionPool.getNumIdle());
        // Add an invalid session and check there is now one available.
        sessionPool.available.add(badSession);
        assertEquals(0, sessionPool.getNumActive());
        assertEquals(1, sessionPool.getNumIdle());

        // Attempt to retrieve an object from the pool - expect it to throw
        // away the invalid one and make a new one.
        sessionPool.borrowObject();

        // Verify that we only have the one active session.
        assertEquals(1, sessionPool.getNumActive());
        assertEquals(0, sessionPool.getNumIdle());
    }

    /**
     * Verify that this error case (required by R27644) is supported. If the
     * connection fails (i.e. can't reconnect), it should log a fatal error.
     *
     * @throws Exception if there was a problem running the test
     */
    public void testSessionFailsToReconnect() throws Exception {
        // Create test objects.
        ExpectationBuilder orderedExpectations =
                mockFactory.createOrderedBuilder();
        PoolableSessionFactoryMock sessionFactory =
                new PoolableSessionFactoryMock("sessionFactory",
                        orderedExpectations, ADDRESS, PORT, SMSC_USERNAME,
                        SMSC_PASSWORD, SYNC_BINDTYPE);
        Session badSession = new Session(new TCPIPConnection(ADDRESS, PORT));
        Session badSessionTwo = new Session(new TCPIPConnection(ADDRESS, PORT));
        // Set expectations.
        sessionFactory.expects.validateObject(badSession).returns(false);
        sessionFactory.expects.makeObject().returns(badSessionTwo);
        sessionFactory.expects.validateObject(badSessionTwo).returns(false);

        // Run test.
        SessionPoolImpl sessionPool = new SessionPoolImpl(sessionFactory, 3, true);
        assertEquals(0, sessionPool.getNumActive());
        assertEquals(0, sessionPool.getNumIdle());
        // Add an invalid session and check there is now one available.
        sessionPool.available.add(badSession);
        assertEquals(0, sessionPool.getNumActive());
        assertEquals(1, sessionPool.getNumIdle());

        // Attempt to retrieve an object from the pool.
        try {
            sessionPool.borrowObject();
            fail("Should throw an exception if there are no valid available " +
                    "sessions and can't create any");
        } catch(SessionException e) {
            // Make sure it's the *right* SessionException
            assertEquals(EXCEPTION_LOCALIZER.format("cannot-create-session"),
                    e.getMessage());
            // do nothing, correct behaviour.
        }
        // Verify that the bad one has been removed, but none has replaced it
        // because can't reconnect to the SMSC.
        assertEquals(0, sessionPool.getNumActive());
        assertEquals(0, sessionPool.getNumIdle());
    }
}
