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

import org.smpp.Session;
import org.smpp.WrongSessionStateException;
import org.smpp.TCPIPConnection;
import org.smpp.TimeoutException;
import org.smpp.pdu.PDUException;

import com.volantis.mps.message.MessageException;
import com.volantis.mps.localization.LocalizationFactory;
import com.volantis.mps.channels.TestSimulator;
import com.volantis.mps.channels.MessageListener;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.synergetics.localization.ExceptionLocalizer;

import java.io.IOException;

/**
 * Verifies that {@link PoolableSessionFactory} behaves as expected.
 */
public class PoolableSessionFactoryTestCase extends TestCaseAbstract {

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer EXCEPTION_LOCALIZER =
            LocalizationFactory.createExceptionLocalizer(
                    PoolableSessionFactoryTestCase.class);

    /**
     * Configuration details for the poolable session factory under test.
     */
    private static final String ADDRESS = "127.0.0.1";
    private static final int PORT = 54321;
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String ASYNC_BINDTYPE = "async";
    private static final String SYNC_BINDTYPE = "sync";

    PoolableSessionFactory sessionFactory;
    private TestSimulator testSimulator;
    private MessageListener listener;

    // Javadoc inherited.
    protected void setUp() throws Exception {
        super.setUp();
        sessionFactory = new PoolableSessionFactory(
                ADDRESS, PORT, USERNAME, PASSWORD, ASYNC_BINDTYPE);
        listener = new MessageListener();
        // Create the mock SMSC and start it listening.
        testSimulator = new TestSimulator(PORT);
        testSimulator.start(listener);
    }

    // Javadoc inherited.
    public void tearDown() throws Exception {
        super.tearDown();
        testSimulator.stop();
    }

    /**
     * Verify that {@link PoolableSessionFactory#makeObject} works as expected.
     *
     * @throws MessageException if there was a problem running the test
     * @throws WrongSessionStateException if there was a problem running the test
     * @throws IOException if there was a problem running the test
     */
    public void testMakeObject()
            throws MessageException, WrongSessionStateException, IOException {
        testSimulator.start(listener);
        sessionFactory = new PoolableSessionFactory(
                ADDRESS, PORT, USERNAME, PASSWORD, ASYNC_BINDTYPE);
        Object object = sessionFactory.makeObject();
        assertTrue(object instanceof Session);
        Session session = (Session) object;
        assertTrue(session.isOpened());
        assertTrue(session.isBound());
        session.checkState(Session.STATE_TRANSMITTER);
        assertEquals(20*1000, session.getConnection().getReceiveTimeout());
    }

    /**
     * Verify that {@link PoolableSessionFactory#makeObject} fails if it cannot
     * reach the SMSC.
     *
     * @throws MessageException if there was a problem running the test
     * @throws WrongSessionStateException if there was a problem running the test
     * @throws IOException if there was a problem running the test
     */
    public void testMakeObjectFailsIfSMSCIsDown()
            throws MessageException, WrongSessionStateException, IOException {
        // Make sure the SMSC is down.
        testSimulator.stop();
        sessionFactory = new PoolableSessionFactory(
                ADDRESS, PORT, USERNAME, PASSWORD, ASYNC_BINDTYPE);
        try {
            sessionFactory.makeObject();
        } catch (MessageException e) {
            // Make sure it failed for the reason we expected.
            assertEquals(EXCEPTION_LOCALIZER.format("smsc-bind-error"),
                    e.getMessage());
        }
    }

    /**
     * Verify that {@link PoolableSessionFactory#destroyObject} works as expected.
     *
     * @throws MessageException if there was a problem running the test
     * @throws IOException if there was a problem running the test
     */
    public void testDestroyObject() throws MessageException, IOException {
        Session session = (Session) sessionFactory.makeObject();
        assertTrue(session.isOpened());
        assertTrue(session.isBound());
        sessionFactory.destroyObject(session);
        assertFalse(session.isOpened());
        assertFalse(session.isBound());
    }

    /**
     * Verify that {@link PoolableSessionFactory#validateObject} works as expected.
     *
     * @throws MessageException if there was a problem running the test
     * @throws IOException if there was a problem running the test
     */
    public void testValidateObject() throws MessageException, IOException {
        sessionFactory = new PoolableSessionFactory(
                ADDRESS, PORT, USERNAME, PASSWORD, SYNC_BINDTYPE);
        Session session = (Session) sessionFactory.makeObject();
        assertTrue(session.isOpened());
        assertTrue(session.isBound());
        assertTrue(sessionFactory.validateObject(session));

        sessionFactory.destroyObject(session);
        assertFalse(sessionFactory.validateObject(session));
    }

    /**
     * Verify that at present {@link PoolableSessionFactory#validateObject}
     * fails to validate when MPS is communicating asynchronously with the SMSC
     * (because it doesn't get a EnquireLinkResp PDU back).
     *
     * @throws MessageException if there was a problem running the test
     * @throws IOException if there was a problem running the test
     */
    public void testValidateObjectFailsWhenAsynchronous()
            throws MessageException, IOException {
        // Create an object to test with.
        Session session = (Session) sessionFactory.makeObject();
        assertTrue(session.isOpened());
        assertTrue(session.isBound());
        // Validation checks that the object is a Session, is opened and bound
        // and responds to EnquireLink PDUs. Asynchronous comms means the
        // validation method gets no response to the EnquireLink PDU and so it
        // always fails.
        assertFalse(sessionFactory.validateObject(session));

        // Same result as after the session has been destroyed.
        sessionFactory.destroyObject(session);
        assertFalse(sessionFactory.validateObject(session));
    }

    /**
     * Verify that the behaviour of bind and unbind is identical refardless of
     * whether the Session is bound synchronously or asynchronously to the SMSC.
     *
     * @throws MessageException if there was a problem running the test
     * @throws WrongSessionStateException if there was a problem running the test
     * @throws TimeoutException if there was a problem running the test
     * @throws IOException if there was a problem running the test
     * @throws PDUException if there was a problem running the test
     */
    public void testBindAndUnbindHandleSynchronousAndAsynchronous()
            throws MessageException, WrongSessionStateException,
            TimeoutException, IOException, PDUException {
        // Verify that binding using an asynchronous factory is fine.
        Session session = new Session(new TCPIPConnection(ADDRESS, PORT));
        sessionFactory.bind(session);
        sessionFactory.unbind(session);

        // Verify that binding using a synchronous factory is fine.
        sessionFactory = new PoolableSessionFactory(
                ADDRESS, PORT, USERNAME, PASSWORD, SYNC_BINDTYPE);
        session = new Session(new TCPIPConnection(ADDRESS, PORT));
        sessionFactory.bind(session);
        sessionFactory.unbind(session);
    }
}
