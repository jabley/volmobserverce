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
 * $Header: /src/mps/testsuite/integration/com/volantis/mps/channels/LogicaSMSChannelAdapterTestCase.java,v 1.8 2003/03/20 10:15:37 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 20-Nov-02    Mat             VBM:2002103007 - Created
 * 18-Dec-02    Sumit           VBM:2002103007 - Fixed testcase to work with
 *                              new channel interface and with functional db
 * 24-Jan-03    Sumit           VBM:2003012401 - Added drop table statements in
 *                              removeRepository() to prevent junit errors
 * 30-Jan-03    Chris W         VBM:2003011609 - Added <message> tags around
 *                              <canvas> tags in markup.
 * 24-Feb-03    Mat             VBM:2003022002 - Changed to use BeanInitialiser
 *                              and to set up the config file correctly.
 * 17-Mar-03    Geoff           VBM:2003031403 - Removed unused/dead imports.
 * 19-Mar-03    Geoff           VBM:2003032001 - Made error handling compatible
 *                              with JUnit, commented out large swathes of
 *                              strangely unused code, use refactored
 *                              ConfigValue stuff, hook channel config
 *                              directly to the Config objectM.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mps.channels;

import com.volantis.mps.message.MessageException;
import com.volantis.mps.message.MultiChannelMessage;
import com.volantis.mps.message.MultiChannelMessageImpl;
import com.volantis.mps.message.MultiChannelMessageMock;
import com.volantis.mps.recipient.MessageRecipient;
import com.volantis.mps.recipient.MessageRecipients;
import com.volantis.mps.recipient.RecipientException;
import com.volantis.mps.session.pool.PoolableSessionFactory;
import com.volantis.mps.session.pool.PoolableSessionFactoryMock;
import com.volantis.mps.session.pool.SessionPool;
import com.volantis.mps.session.pool.SessionPoolImpl;
import com.volantis.mps.session.pool.SessionPoolMock;
import com.volantis.mps.session.pool.SessionValidator;
import com.volantis.mps.session.pool.UnpooledSessionPool;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.testtools.mock.ExpectationBuilder;
import com.volantis.testtools.mock.concurrency.PerThreadExpectationBuilder;
import com.volantis.testtools.mock.concurrency.ThreadMatcher;
import com.volantis.testtools.mock.expectations.UnorderedExpectations;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Iterator;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.apache.commons.pool.PoolableObjectFactory;
import org.smpp.Data;
import org.smpp.Session;
import org.smpp.TCPIPConnection;
import org.smpp.pdu.BindResponse;
import org.smpp.pdu.PDUException;
import org.smpp.pdu.Response;
import org.smpp.pdu.SubmitMultiSM;
import org.smpp.pdu.SubmitSM;
import org.smpp.pdu.WrongLengthOfStringException;
import org.smpp.smscsim.PDUProcessor;
import org.smpp.smscsim.PDUProcessorFactory;
import org.smpp.smscsim.SMSCSession;

/**
 * A test case for the SMS channel adaptor.
 *
 * Please note that the start/stop of the simulator can take a long time
 * (~70 seconds), so don't lose faith when running the test.
 *
 * @author mat
 *
 * @todo one failure causes all subsequent tests to fail because it doesn't
 *      seem to clean up the listener if a test fails?
 */
public class LogicaSMSChannelAdapterTestCase extends TestCaseAbstract {

    private TestSimulator testSimulator;
    protected LogicaSMSChannelAdapter adapter;
    private MessageListener listener;
    MultiChannelMessage message;
    MultiChannelMessageMock mockMessage;
    MessageRecipients recipients;
    MessageRecipient sender;
    public static final String CHANNEL_NAME = "testChannel";
    public static final String ADDRESS = "127.0.0.1";
    public static final int PORT = 54321;
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String SYNC_BINDTYPE = "sync";

    public void setUp() throws Exception {
        super.setUp();

        message = new MultiChannelMessageImpl("http://localhost:8080/dummy",
                "Test message");
        recipients = new MessageRecipients();
        MessageRecipient recipient = new MessageRecipient(
                new InternetAddress("recipient@volantis.com"), "PC");
        recipients.addRecipient(recipient);
        sender = new MessageRecipient();
        sender.setAddress( new InternetAddress("test@volantis.com"));
        testSimulator = new TestSimulator(PORT);
        listener = new MessageListener();
        //testSimulator.start(listener);
    }

    public void tearDown() throws Exception {
        super.tearDown();
        //testSimulator.stop();
    }

    public void testIsChannelNameSet()
            throws MessageException, IOException, RecipientException {

        Map channelInfo = createChannelInfoMap();
        channelInfo.put(LogicaSMSChannelAdapter.SUPPORTS_POOLING, "no");
        LogicaSMSChannelAdapter channelAdapter =
                new LogicaSMSChannelAdapter(CHANNEL_NAME, channelInfo);
        assertEquals("channelName not set properly",channelAdapter.getChannelName(),
                CHANNEL_NAME);
    }

    /**
     * Verify that if the connection to the SMSC drops during usage (i.e.
     * sessions become invalid while they are being used) that an warning is
     * logged and the connection is re-established.
     * <p/>
     * NB: This behaviour is required by R27644
     *
     * @throws MessageException if there was a problem running the test
     * @throws IOException if there was a problem running the test
     * @throws RecipientException if there was a problem sending the message
     */
    public void testConnectionToSMSCDropsDuringUsage()
            throws MessageException, IOException, RecipientException {

        testSimulator.start(listener);

        // Create test objects.
        ExpectationBuilder orderedExpectations =
                mockFactory.createOrderedBuilder();
        HashMap channelInfo = createChannelInfoMap();
        channelInfo.put(LogicaSMSChannelAdapter.BINDTYPE, SYNC_BINDTYPE);
        SessionPoolMock sessionPool =
                new SessionPoolMock("sessionPool", orderedExpectations);
        mockMessage = new MultiChannelMessageMock("mockMessage", orderedExpectations);
        Session badSession = new Session(new TCPIPConnection(ADDRESS, PORT));
        PoolableSessionFactory sessionFactory = new PoolableSessionFactory(
                ADDRESS,  PORT, USERNAME, PASSWORD, SYNC_BINDTYPE);
        Session goodSession = (Session) sessionFactory.makeObject();

        // Set expectations.
        mockMessage.fuzzy.generateTargetMessageAsString(
                mockFactory.expectsInstanceOf(String.class)).
                returns("Test message");
        sessionPool.expects.borrowObject().returns(badSession);
        sessionPool.expects.invalidateObject(badSession);
        sessionPool.expects.borrowObject().returns(goodSession);
        sessionPool.expects.returnObject(goodSession);

        // Run test.
        adapter = new LogicaSMSChannelAdapter(sessionPool, CHANNEL_NAME,
                channelInfo);
        // Attempt to send a message.
        adapter.sendImpl(mockMessage, recipients, sender);

        testSimulator.stop();
    }

    /**
     * Run the pooling test.
     *
     * @param onPoolExhaustion          byte which indicates how the pool under
     *                                  test should behave on exhaustion
     * @param onPoolExhaustionConfig    string which describes how the pool
     *                                  under test should behave on exhaustion
     *                                  (as expected in the channel config)
     *
     * @throws MessageException if there was a problem running the test
     * @throws RecipientException if there was a problem running the test
     * @throws AddressException if there was a problem running the test
     * @throws InterruptedException if there was a problem running the test
     * @throws IOException if there was a problem running the test
     */
    public void doTestPooling(byte onPoolExhaustion,
                              String onPoolExhaustionConfig)
            throws MessageException, RecipientException, AddressException,
            InterruptedException, IOException {

        // Start the mock SMSC off...
        testSimulator.start(listener);

        // Create test objects.
        SendTest threadOne = new SendTest("Thread one", 7, onPoolExhaustion);
        SendTest threadTwo = new SendTest("Thread two", 5, onPoolExhaustion);
        SendTest threadThree = new SendTest("Thread three", 2, onPoolExhaustion);
        SendTest threadFour = new SendTest("Thread four", 15, onPoolExhaustion);
        SendTest threadFive = new SendTest("Thread four", 11, onPoolExhaustion);
        SendTest[] threads = new SendTest[] {threadOne, threadTwo,
                threadThree, threadFour, threadFive};

        PerThreadExpectationBuilder perThreadExpectations =
                mockFactory.createPerThreadBuilder();
        ExpectationBuilder expectationsOne = mockFactory.createUnorderedBuilder();
        ExpectationBuilder expectationsTwo = mockFactory.createUnorderedBuilder();
        ExpectationBuilder expectationsThree = mockFactory.createUnorderedBuilder();
        ExpectationBuilder expectationsFour = mockFactory.createUnorderedBuilder();
        ExpectationBuilder expectationsFive = mockFactory.createUnorderedBuilder();

        ThreadMatcher matcherOne = mockFactory.createKnownThreadMatcher(
                threadOne.getName(), threadOne);
        ThreadMatcher matcherTwo = mockFactory.createKnownThreadMatcher(
                threadTwo.getName(), threadTwo);
        ThreadMatcher matcherThree = mockFactory.createKnownThreadMatcher(
                threadThree.getName(), threadThree);
        ThreadMatcher matcherFour = mockFactory.createKnownThreadMatcher(
                threadFour.getName(), threadFour);
        ThreadMatcher matcherFive = mockFactory.createKnownThreadMatcher(
                threadFive.getName(), threadFive);

        perThreadExpectations.addThreadSpecificBuilder(matcherOne,
                expectationsOne);
        perThreadExpectations.addThreadSpecificBuilder(matcherTwo,
                expectationsTwo);
        perThreadExpectations.addThreadSpecificBuilder(matcherThree,
                expectationsThree);
        perThreadExpectations.addThreadSpecificBuilder(matcherFour,
                expectationsFour);
        perThreadExpectations.addThreadSpecificBuilder(matcherFive,
                expectationsFive);

        SynchronizedPoolableSessionFactory sessionFactoryWrapper =
                new SynchronizedPoolableSessionFactory(
                        mockFactory.createOrderedBuilder(), ADDRESS, PORT,
                        USERNAME, PASSWORD, SYNC_BINDTYPE);
        mockMessage = new MultiChannelMessageMock("message",
                perThreadExpectations);

        // Set expectations.
        // 40 messages should be sent.
        addSendTestExpectations(perThreadExpectations, matcherOne, 7);
        addSendTestExpectations(perThreadExpectations, matcherTwo, 5);
        addSendTestExpectations(perThreadExpectations, matcherThree, 2);
        addSendTestExpectations(perThreadExpectations, matcherFour, 15);
        addSendTestExpectations(perThreadExpectations, matcherFive, 11);

        PoolableSessionFactoryMock sessionFactoryMock =
                sessionFactoryWrapper.getMockedObject();

        final Session sessionOne = sessionFactoryWrapper.makeRealSession();
        if (onPoolExhaustion == SessionPoolImpl.WHEN_EXHAUSTED_GROW) {
            // If the pool should grow when exhausted then basically the max
            // size is ignored.
            sessionFactoryMock.expects.makeObject().returns(sessionOne).any();
        } else {
            // Depending on how fast the threads execute, not all of the 
            // sessions may be required. However at max three sessions should 
            // be created.
            Session sessionTwo = sessionFactoryWrapper.makeRealSession();
            Session sessionThree = sessionFactoryWrapper.makeRealSession();

            sessionFactoryMock.expects.makeObject().returns(sessionOne);
            sessionFactoryMock.expects.makeObject().returns(sessionTwo);
            sessionFactoryMock.expects.makeObject().returns(sessionThree);
            // Never destroy the objects - they stay in the pool
        }
        // Run test.
        Map channelInfo = createChannelInfoMap();
        channelInfo.put(LogicaSMSChannelAdapter.ON_POOL_EXHAUSTED,
                onPoolExhaustionConfig);
        adapter = new LogicaSMSChannelAdapter(
                sessionFactoryWrapper, CHANNEL_NAME, channelInfo);
        // Start the individual threads off.
        for (int i=0; i<threads.length; i++) {
            threads[i].start();
        }

        // Wait for the threads to die.
        for (int i=0; i<threads.length; i++) {
            threads[i].join();
        }
        testSimulator.stop();
    }

    /**
     * Verify that even though multiple threads send multiple messages to the
     * SMSC, only three sessions are created with the SMSC.
     * <p/>
     * If the pool becomes exhausted (i.e. all sessions are active) then the
     * request for a session should fail. This means that not all messages
     * will be successfully sent to the SMSC, even though the right number of
     * requests were made.
     * 
     * @throws MessageException if there was a problem running the test
     * @throws RecipientException if there was a problem running the test
     * @throws AddressException if there was a problem running the test
     * @throws InterruptedException if there was a problem running the test
     * @throws IOException if there was a problem running the test
     */
    public void testPoolingWhenFailOnExhaustion() throws MessageException,
            RecipientException, AddressException, InterruptedException, IOException {
        doTestPooling(SessionPoolImpl.WHEN_EXHAUSTED_FAIL, "fail");
    }

    /**
     * Verify that as many Sessions with the SMSC as required are created (even
     * above the specified max size of three). If the pool becomes
     * exhausted (i.e. all sessions are active) then the pool should grow
     * (ignoring the pool max size configuration parameter). This means that
     * all messages should be successfully sent, even if the pool was exhausted.
     *
     * @throws MessageException if there was a problem running the test
     * @throws RecipientException if there was a problem running the test
     * @throws AddressException if there was a problem running the test
     * @throws InterruptedException if there was a problem running the test
     * @throws IOException if there was a problem running the test
     */
    public void testPoolingWhenGrowOnExhaustion() throws MessageException,
            RecipientException, AddressException, InterruptedException, IOException {
        doTestPooling(SessionPoolImpl.WHEN_EXHAUSTED_GROW, "grow");
    }

    /**
     * Add expectations for a particular {@link SendTest} thread to the per
     * thread expectations builder.
     *
     * @param perThreadExpectations to which the expectations should be added
     * @param matcher               identifies the thread to be matched
     * @param expectedNum           number of times the send test expectations
     */
    private void addSendTestExpectations(
            PerThreadExpectationBuilder perThreadExpectations,
            ThreadMatcher matcher,
            final int expectedNum) {

        perThreadExpectations.add(matcher, new UnorderedExpectations() {
            // Javadoc inherited.
            public void add() {
                mockMessage.fuzzy.generateTargetMessageAsString(
                        mockFactory.expectsInstanceOf(String.class)).
                        returns("Test message").fixed(expectedNum);
            }
        });
    }

    /**
     * Verify that if the configuration specifies that pooling isn't supported,
     * an {@link UnpooledSessionPool} instance is created.
     *
     * @throws MessageException if there was a problem running the test
     */
    public void testConstructorWhenPoolingNotSupported()
            throws MessageException {
        Map channelInfo = createChannelInfoMap();
        channelInfo.put(LogicaSMSChannelAdapter.SUPPORTS_POOLING, "no");
        LogicaSMSChannelAdapter channelAdapter =
                new LogicaSMSChannelAdapter(CHANNEL_NAME, channelInfo);
        assertTrue(channelAdapter.sessionPool instanceof UnpooledSessionPool);
    }

    /**
     * Verify that if the configuration:
     * <ul>
     * <li>specifies that pooling IS supported</li>
     * <li>specified an invalid (i.e. non integer) poolsize</li>
     * </ul>
     * then an exception is thrown.
     */
    public void testConstructorWhenPoolingSupportedAndPoolsizeInvalid() {
        Map channelInfo = createChannelInfoMap();
        channelInfo.put(LogicaSMSChannelAdapter.SUPPORTS_POOLING, "yes");
        channelInfo.put(LogicaSMSChannelAdapter.POOL_SIZE, "invalidInt");
        try {
            LogicaSMSChannelAdapter channelAdapter =
                    new LogicaSMSChannelAdapter(CHANNEL_NAME, channelInfo);
            fail("Should throw an exception if a invalid pool size is specified");
        } catch (MessageException e) {
            // do nothing, correct behaviour.
        }
    }

    /**
     * Verify that if the configuration:
     * <ul>
     * <li>specifies that pooling IS supported</li>
     * <li>has not specified a poolsize</li>
     * </ul>
     * then a {@link SessionPoolImpl} instance is created with the default
     * unlimited pool size.
     *
     * @throws MessageException if there was a problem running the test
     */
    public void testConstructorWhenPoolingSupportedAndPoolsizeMissing()
            throws MessageException {
        Map channelInfo = createChannelInfoMap();
        channelInfo.put(LogicaSMSChannelAdapter.SUPPORTS_POOLING, "yes");
        channelInfo.put(LogicaSMSChannelAdapter.POOL_SIZE, null);
        LogicaSMSChannelAdapter channelAdapter =
                new LogicaSMSChannelAdapter(CHANNEL_NAME, channelInfo);
        assertTrue(channelAdapter.sessionPool instanceof SessionPoolImpl);
        SessionPoolImpl sessionPool
                = ((SessionPoolImpl) channelAdapter.sessionPool);
        // Should default to having an unlimited pool size if no pool size
        // is specified.
        assertEquals(SessionPool.UNLIMITED_ACTIVE_SESSIONS,
                sessionPool.getMaxSize());
    }

    /**
     * Verify that if the configuration:
     * <ul>
     * <li>specifies synchronous comms with the SMSC</li>
     * <li>specifies that pooling IS supported</li>
     * <li>has specified a valid poolsize</li>
     * <li>has specified that each Session should be validated before it is
     * returned from the pool</li>
     * <li>has specified a valid session validation interval</li>
     * </ul>
     * then a {@link SessionPoolImpl} instance is created with the specified
     * pool size. It will validate on borrow, but no session validator is
     * created.
     *
     * @throws MessageException if there was a problem running the test
     */
    public void testConstructorWhenPoolingSupportedAndSync()
            throws MessageException {
        Map channelInfo = createChannelInfoMap();
        channelInfo.put(LogicaSMSChannelAdapter.BINDTYPE, "sync");
        LogicaSMSChannelAdapter channelAdapter =
                new LogicaSMSChannelAdapter(CHANNEL_NAME, channelInfo);
        assertTrue(channelAdapter.sessionPool instanceof SessionPoolImpl);
        SessionPoolImpl sessionPool
                = ((SessionPoolImpl) channelAdapter.sessionPool);
        assertEquals(3, sessionPool.getMaxSize());
        assertTrue(sessionPool.isValidatingOnBorrow());
        assertNull(channelAdapter.sessionValidator);
    }

    /**
     * Verify that if the configuration:
     * <ul>
     * <li>specifies asynchronous comms with the SMSC</li>
     * <li>specifies that pooling IS supported</li>
     * <li>has specified a valid poolsize</li>
     * <li>has specified that each Session should be validated before it is
     * returned from the pool</li>
     * <li>has specified a valid session validation interval</li>
     * </ul>
     * then a {@link SessionPoolImpl} instance is created with the specified
     * pool size. It will not validate on borrow and no session validator will
     * be created.
     *
     * @throws MessageException if there was a problem running the test
     */
    public void testConstructorWhenPoolingSupportedAndAsync()
            throws MessageException {
        Map channelInfo = createChannelInfoMap();
        LogicaSMSChannelAdapter channelAdapter =
                new LogicaSMSChannelAdapter(CHANNEL_NAME, channelInfo);
        assertTrue(channelAdapter.sessionPool instanceof SessionPoolImpl);
        SessionPoolImpl sessionPool
                = ((SessionPoolImpl) channelAdapter.sessionPool);
        assertEquals(3, sessionPool.getMaxSize());
        // Can't validate if communicating asynchronously, so always false.
        assertFalse(sessionPool.isValidatingOnBorrow());
        assertNull(channelAdapter.sessionValidator);
    }

    /**
     * Verify that if the configuration:
     * <ul>
     * <li>specifies synchronous comms with the SMSC</li>
     * <li>specifies that pooling IS supported</li>
     * <li>has specified a valid poolsize</li>
     * <li>has specified that each Session should not be validated before it is
     * returned from the pool</li>
     * <li>has specified a valid session validation interval</li>
     * </ul>
     * then a {@link SessionPoolImpl} instance is created with the specified
     * pool size. It will not validate on borrow and a valid session validator
     * will have been created.
     * <p/>
     * It also verifies that if an invalid (i.e. non integer) session
     * validation interval is specified, then an exception will be thrown.
     * <p/>
     * It also verifies that if no session validation interval is specified,
     * then it will default to waiting forever.
     *
     * @throws MessageException if there was a problem running the test
     */
    public void testConstructorWhenPoolingNotTestingOnBorrowAndSync()
            throws MessageException {
        // Firstly check that the session validator is expected if all of the
        // arguments are valid.
        Map channelInfo = createChannelInfoMap();
        channelInfo.put(LogicaSMSChannelAdapter.BINDTYPE, "sync");
        channelInfo.put(LogicaSMSChannelAdapter.VALIDATE_SESSION_BEFORE_USE, "false");
        LogicaSMSChannelAdapter channelAdapter =
                new LogicaSMSChannelAdapter(CHANNEL_NAME, channelInfo);
        assertTrue(channelAdapter.sessionPool instanceof SessionPoolImpl);
        SessionPoolImpl sessionPool
                = ((SessionPoolImpl) channelAdapter.sessionPool);
        assertEquals(3, sessionPool.getMaxSize());
        assertFalse(sessionPool.isValidatingOnBorrow());
        assertNotNull(channelAdapter.sessionValidator);
        assertEquals(60000,
                channelAdapter.sessionValidator.getValidationInterval());

        // Then check that if the session validation interval is missing it
        // will default to waiting forever.
        channelInfo.put(LogicaSMSChannelAdapter.VALIDATION_INTERVAL, null);
        channelAdapter = new LogicaSMSChannelAdapter(CHANNEL_NAME, channelInfo);
        assertEquals(SessionValidator.WAIT_FOREVER,
                channelAdapter.sessionValidator.getValidationInterval());

        // Then check that if the session validation interval is invalid an
        // exception will be thrown.
        channelInfo.put(LogicaSMSChannelAdapter.VALIDATION_INTERVAL, "invalidInt");
        try {
            channelAdapter = new LogicaSMSChannelAdapter(CHANNEL_NAME,
                    channelInfo);
            fail("Should have failed if an invalid (i.e. non integer) " +
                    "session validation interval was specified");
        } catch (MessageException e) {
            // do nothing, correct behaviour.
        }
    }

    /**
     * Verify that if the configuration:
     * <ul>
     * <li>specifies asynchronous comms with the SMSC</li>
     * <li>specifies that pooling IS supported</li>
     * <li>has specified a valid poolsize</li>
     * <li>has specified that each Session should not be validated before it is
     * returned from the pool</li>
     * <li>has specified a valid session validation interval</li>
     * </ul>
     * then a {@link SessionPoolImpl} instance is created with the specified
     * pool size. It will not validate on borrow and no session validator will
     * be created.
     *
     * @throws MessageException if there was a problem running the test
     */
    public void testConstructorWhenNotTestingOnBorrowAndAsync()
            throws MessageException {
        Map channelInfo = createChannelInfoMap();
        LogicaSMSChannelAdapter channelAdapter =
                new LogicaSMSChannelAdapter(CHANNEL_NAME, channelInfo);
        assertTrue(channelAdapter.sessionPool instanceof SessionPoolImpl);
        SessionPoolImpl sessionPool
                = ((SessionPoolImpl) channelAdapter.sessionPool);
        assertEquals(3, sessionPool.getMaxSize());
        // Can't validate if communicating asynchronously, so always false.
        assertFalse(sessionPool.isValidatingOnBorrow());
        assertNull(channelAdapter.sessionValidator);
    }

    /**
     * Create a Map containing default channel configuration information.
     *
     * @return Map containing default channel configuration information
     */
    private HashMap createChannelInfoMap() {
        HashMap channelInfo = new HashMap();
        channelInfo.put(LogicaSMSChannelAdapter.ADDRESS, "127.0.0.1");
        channelInfo.put(LogicaSMSChannelAdapter.PORT, Integer.toString(PORT));
        channelInfo.put(LogicaSMSChannelAdapter.USERNAME, "username");
        channelInfo.put(LogicaSMSChannelAdapter.PASSWORD, "password");
        channelInfo.put(LogicaSMSChannelAdapter.SUPPORTS_POOLING, "yes");
        channelInfo.put(LogicaSMSChannelAdapter.POOL_SIZE, "3");
        channelInfo.put(LogicaSMSChannelAdapter.VALIDATE_SESSION_BEFORE_USE, "true");
        channelInfo.put(LogicaSMSChannelAdapter.VALIDATION_INTERVAL, "60000");
        return channelInfo;
    }

    /**
     * Inner class which send messages in a separate thread, but can access the
     * mocked objects.
     */
    class SendTest extends Thread {

        private String name;
        private int count;
        private byte onPoolExhausted;

        public SendTest(String name, int count, byte onPoolExhausted) {
            super(name);
            this.name = name;
            this.count = count;
            this.onPoolExhausted = onPoolExhausted;
        }

        // Javadoc inherited.
        public void run() {
            List failureReasons = new ArrayList();
            for (int i=0; i<count; i++) {
                try {
                    // Attempt to send a message.
                    MessageRecipients failures =
                            adapter.sendImpl(mockMessage, recipients, sender);
                    // Record if not all of the messages were sent successfully.
                    final Iterator iterator = failures.getIterator();
                    while(iterator.hasNext()) {
                        MessageRecipient failure =
                                (MessageRecipient) iterator.next();
                        failureReasons.add(failure.getFailureReason());
                    }
                } catch (RecipientException e) {
                    fail(e.getMessage());
                } catch (MessageException e) {
                    fail(e.getMessage());
                }
            }

            if (!failureReasons.isEmpty() &&
                    onPoolExhausted == SessionPoolImpl.WHEN_EXHAUSTED_GROW) {
                // It's only definitely a problem if we get errors when the
                // pool we're using should grow when exhausted (blocking or
                // failing pools can throw exceptions if the pool is exhausted).
                fail("The right number of attempts were made to send " +
                        "messages, however " + failureReasons.size() +
                        " were not successful");
            }
        }
    }
}

class TestPDUProcessorFactory implements PDUProcessorFactory {
    //    private PDUProcessorGroup procGroup;
    //    private DeliveryInfoSender deliveryInfoSender;
    private MessageListener listener;

    /**
     * If the information about processing has to be printed
     * to the standard output.
     */
    private boolean displayInfo = false;

    /**
     * Constructs processor factory with given processor group,
     * message store for storing of the messages and a table of
     * users for authentication. The message store and users parameters are
     * passed to generated instancies of <code>SimulatorPDUProcessor</code>.
    //     * @param procGroup the group the newly generated PDU processors will belong to
    //     * @param messageStore the store for messages received from the client
    //     * @param users the list of users used for authenticating of the client
     */
    public TestPDUProcessorFactory(/*PDUProcessorGroup procGroup,*/
    MessageListener listener
    /*,DeliveryInfoSender deliveryInfoSender*/
    ) {
        //        this.procGroup = procGroup;
        this.listener = listener;
        //        this.deliveryInfoSender = deliveryInfoSender;
    }

    /**
     * Creates a new instance of <code>SimulatorPDUProcessor</code> with
     * parameters provided in construction of th factory.
     *
     * @param session the sessin the PDU processor will work for
     * @return newly created <code>SimulatorPDUProcessor</code>
     */
    public PDUProcessor createPDUProcessor(SMSCSession session) {
        TestPDUProcessor testPDUProcessor =
            new TestPDUProcessor(session /*, null*/
        );
        testPDUProcessor.setListener(listener);
        System.out.println("new connection accepted");
        return testPDUProcessor;
    }

    /**
     * Sets if the info about processing has to be printed on
     * the standard output.
     */
    public void setDisplayInfo(boolean on) {
        displayInfo = on;
    }

    /**
     * Returns status of printing of processing info on the standard output.
     */
    public boolean getDisplayInfo() {
        return displayInfo;
    }

}

class TestPDUProcessor extends PDUProcessor {

    /**
     * The session this processor uses for sending of PDUs.
     */
    private SMSCSession session = null;

    private MessageListener messageListener;
    //    /**
    //     * The thread which sends delivery information for messages
    //     * which require delivery information.
    //     */
    //    private DeliveryInfoSender deliveryInfoSender = null;

    //    /**
    //     * The table with system id's and passwords for authenticating
    //     * of the bounding ESMEs.
    //     */
    //    private Table users = null;

    /**
     * Indicates if the bound has passed.
     */
    private boolean bound = false;

    //    /**
    //     * The system id of the bounded ESME.
    //     */
    //    private String systemId = null;

    //    /**
    //     * If the information about processing has to be printed
    //     * to the standard output.
    //     */
    //    private boolean displayInfo = false;

    //    /**
    //     * The message id assigned by simulator to submitted messages.
    //     */
    //    private static int intMessageId = 2000;

    /**
     * System id of this simulator sent to the ESME in bind response.
     */
    private static final String SYSTEM_ID = "Smsc Simulator";

    //    /**
    //     * The name of attribute which contains the system id of ESME.
    //     */
    //    private static final String SYSTEM_ID_ATTR = "name";

    public TestPDUProcessor(SMSCSession session /*, ShortMessageStore messageStore*/
    ) {
        this.session = session;
        //this.messageStore = messageStore;
        // this.users = users;
    }

    public void clientRequest(org.smpp.pdu.Request request) {
        Response response;
        //        int commandStatus;
        int commandId = request.getCommandId();
        try {
            System.out.println("client request: " + request.debugString());
            if (!bound) { // the first PDU must be bound request
                if (commandId == Data.BIND_TRANSMITTER
                    || commandId == Data.BIND_RECEIVER
                    || commandId == Data.BIND_TRANSCEIVER) {
                    //commandStatus = checkIdentity((BindRequest)request);
                    // firstly generate proper bind response
                    BindResponse bindResponse =
                        (BindResponse) request.getResponse();
                    bindResponse.setSystemId(SYSTEM_ID);
                    // and send it to the client via serverResponse
                    serverResponse(bindResponse);
                    // success => bound
                    bound = true;
                } else {
                    // the request isn't a bound req and this is wrong: if not
                    // bound, then the server expects bound PDU
                    if (request.canResponse()) {
                        // get the response
                        response = request.getResponse();
                        response.setCommandStatus(Data.ESME_RINVBNDSTS);
                        // and send it to the client via serverResponse
                        serverResponse(response);
                    } else {
                        // cannot respond to a request which doesn't have
                        // a response :-(
                    }
                    // bind failed, stopping the session
                    session.stop();
                }
            } else { // already bound, can receive other PDUs
                if (request.canResponse()) {
                    response = request.getResponse();
                    ShortMessageValues smv;
                    switch (commandId) { // for selected PDUs do extra steps
                        case Data.SUBMIT_SM :
                            System.out.println("Got single message submit");
                            smv = new ShortMessageValues((SubmitSM) request);
                            messageListener.setMessage(smv);
                            break;

                        case Data.SUBMIT_MULTI :
                            smv =
                                new ShortMessageValues((SubmitMultiSM) request);
                            messageListener.setMessage(smv);
                            break;

                        case Data.DELIVER_SM :
                            System.out.println("Got deliver_sm");
                            break;

                        case Data.DATA_SM :
                            System.out.println("Got data_sm");
                            break;

                        case Data.QUERY_SM :
                            System.out.println("Got query_sm");
                            break;

                        case Data.CANCEL_SM :
                            System.out.println("Got cancel_sm");
                            break;

                        case Data.REPLACE_SM :
                            System.out.println("Got query_sm");
                            break;

                        case Data.UNBIND :
                            // do nothing, just respond and after sending
                            // the response stop the session
                            break;
                    }
                    // send the prepared response
                    serverResponse(response);
                    if (commandId == Data.UNBIND) {
                        // unbind causes stopping of the session
                        session.stop();
                    }
                } else {
                    // can't respond => nothing to do :-)
                }
            }
        } catch (WrongLengthOfStringException e) {
            System.out.println(e);
        } catch (IOException e) {
            System.out.println(e);
        } catch (PDUException e) {
            System.out.println(e);
        }
    }

    public void clientResponse(org.smpp.pdu.Response response) {
        System.out.println("Got clientResponse - " + response.debugString());
    }

    public void serverRequest(org.smpp.pdu.Request request)
            throws IOException, PDUException {
        session.send(request);
    }

    public void serverResponse(org.smpp.pdu.Response response)
            throws IOException, PDUException {
        session.send(response);
    }

    public void setListener(MessageListener listener) {
        this.messageListener = listener;
    }

    public SMSCSession getSession() {
        return session;
    }

    public void stop() {
    }
}

class ShortMessageValues {
    String serviceType;
    String sourceAddr;
    ArrayList destinationAddrs;
    String shortMessage;

    /**
     * Constructor for building the object from <code>SubmitSM</code>
     * PDU.
     *
    //     * @param systemId system id of the client
     * @param submit the PDU send from the client
     */
    ShortMessageValues(SubmitMultiSM submit) {
        short dests = submit.getNumberOfDests();
        destinationAddrs = new ArrayList();
        for (int i = 0; i < dests; i++) {
            destinationAddrs.add(submit.getDestAddress(i).getAddress());
        }
        shortMessage = submit.getShortMessage();
    }
    ShortMessageValues(SubmitSM submit) {
        shortMessage = submit.getShortMessage();
    }
}

/**
 * Wrapper class which delegates to a {@link PoolableSessionFactoryMock}
 * ensuring that all calls are synchronized (so that multiple threads can
 * access it).
 * <p/>
 * It can also be used to create sessions that are actually bound to an SMSC.
 */
class SynchronizedPoolableSessionFactory implements PoolableObjectFactory {

    /**
     * The mock to which we'll delegate calls during the test.
     */
    private PoolableSessionFactoryMock sessionFactory;

    // Confusingly, we use a real session factory to create the sessions so
    // they're actually bound. Should only be used to create test objects, not
    // as part of the test.
    private PoolableSessionFactory realSessionFactory;

    public SynchronizedPoolableSessionFactory(ExpectationBuilder expectations,
                                              String address,
                                              int port,
                                              String SMSCUser,
                                              String SMSCPassword,
                                              String SMSCBindType) {
        sessionFactory = new PoolableSessionFactoryMock("sessionFactory",
                expectations, address, port, SMSCUser, SMSCPassword,
                SMSCBindType);
        realSessionFactory = new PoolableSessionFactory(
                address, port, SMSCUser, SMSCPassword, SMSCBindType);
    }

    // Javadoc inherited.
    public synchronized Object makeObject() throws MessageException {
        return sessionFactory.makeObject();
    }

    // Javadoc inherited.
    public synchronized void destroyObject(Object object)
            throws MessageException {
        sessionFactory.destroyObject(object);
    }

    // Javadoc inherited.
    public synchronized boolean validateObject(Object object) {
        return sessionFactory.validateObject(object);
    }

    // Javadoc inherited.
    public synchronized void activateObject(Object object) throws Exception {
        sessionFactory.activateObject(object);
    }

    // Javadoc inherited.
    public synchronized void passivateObject(Object object) throws Exception {
        sessionFactory.passivateObject(object);
    }

    // Javadoc unnecessary.
    protected PoolableSessionFactoryMock getMockedObject() {
        return sessionFactory;
    }

    /**
     * Create real sessions (so they're actually bound to the SMSC).
     *
     * @return Session which is bound to the SMSC
     * @throws MessageException if there was a problem creating the session
     */
    protected Session makeRealSession() throws MessageException {
        return (Session) realSessionFactory.makeObject();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Dec-04	270/1	pcameron	VBM:2004122004 New packagers for wemp

 08-Jul-04	127/1	claire	VBM:2004070702 Update layout names to include extensions

 10-Jun-04	121/1	ianw	VBM:2004060111 Made to work with main 3.2 MCS stream

 19-Dec-03	75/2	geoff	VBM:2003121715 Import/Export: Schemify configuration file: Clean up existing elements

 27-Oct-03	45/6	mat	VBM:2003101502 Fix MessageRecipient(s) testcases

 24-Oct-03	45/4	mat	VBM:2003101502 Rework tests to use AppManager properly

 23-Oct-03	45/2	mat	VBM:2003101502 Rework tests to use AppManager and generally tidy them up

 ===========================================================================
*/
