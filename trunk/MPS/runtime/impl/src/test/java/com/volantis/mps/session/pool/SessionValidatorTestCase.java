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

import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.mps.message.MessageException;
import com.volantis.mps.session.pool.SessionValidator;
import com.volantis.mps.session.pool.PoolableSessionFactory;
import com.volantis.mps.channels.TestSimulator;
import com.volantis.mps.channels.MessageListener;

import org.smpp.Session;

/**
 * Verifies that {@link SessionValidator} works as expected.
 */
public class SessionValidatorTestCase extends TestCaseAbstract {

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
    private MessageListener listener;

    // Javadoc inherited.
    protected void setUp() throws Exception {
        super.setUp();
        sessionFactory = new PoolableSessionFactory(
                ADDRESS, PORT, SMSC_USERNAME, SMSC_PASSWORD, ASYNC_BINDTYPE);
        // Create the mock SMSC and start it listening.
        testSimulator = new TestSimulator(PORT);
        listener = new MessageListener();
        testSimulator.start(listener);
    }

    // Javadoc inherited.
    public void tearDown() throws Exception {
        super.tearDown();
        testSimulator.stop();
    }

    /**
     * Trivial test which is basically intended to capture the fact that
     * sending an enquire link does result in an EnquireLinkResponse PDU if MPS
     * is communicating synchronously with the SMSC.
     *
     * @throws MessageException if there was a problem running the test
     */
    public void testSendEnquireLinkWhenSynchronous() throws MessageException {
        PoolableSessionFactory sessionFactory = new PoolableSessionFactory(
                ADDRESS, PORT, SMSC_USERNAME, SMSC_PASSWORD, SYNC_BINDTYPE);
        Session session = (Session) sessionFactory.makeObject();
        assertTrue(SessionValidator.sendEnquireLink(session));
    }

    /**
     * Trivial test which is basically intended to capture the fact that
     * sending an enquire link does not result in an EnquireLinkResponse PDU if
     * MPS is communicating asynchronously with the SMSC.
     *
     * @throws MessageException if there was a problem running the test
     */
    public void testSendEnquireLinkWhenAsynchronous() throws MessageException {
        PoolableSessionFactory sessionFactory = new PoolableSessionFactory(
                ADDRESS, PORT, SMSC_USERNAME, SMSC_PASSWORD, ASYNC_BINDTYPE);
        Session session = (Session) sessionFactory.makeObject();
        assertFalse(SessionValidator.sendEnquireLink(session));
    }
}
