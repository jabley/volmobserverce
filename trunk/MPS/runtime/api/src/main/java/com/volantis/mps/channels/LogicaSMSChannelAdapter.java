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
 * $Header: /src/mps/com/volantis/mps/channels/LogicaSMSChannelAdapter.java,v 1.14 2003/03/26 17:43:13 sumit Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 11-Nov-02    Mat             VBM:2002103007 - Created
 * 13-Nov-02    ianw            VBM:2002111211 - Updated send routines to use
 *                              MessageRecipients.getIterator().
 * 13-Nov-02    ianw            VBM:2002103007 - Made all config fields 
 *                              lowercase.
 * 20-Nov-02    Sumit           VBM:2002111501 - Logs information about the 
 *                              logica version inf bind()
 * 26-Nov-02    Mat             VBM:2002112007 - Amended constructor to take
 *                              a Map of config values, rather than read them 
 *                              from the config file.
 * 28-Nov-02    Sumit           VBM:2002112602 - generateTar...MimeMultipart()
 *                              now takes channel name in send()
 * 29-Nov-02    Sumit           VBM:2002112602 - Removed above change and impl
 *                              method sendImpl
 * 12-Dec-02    Chris W         VBM:2002121020 - Moved the call to bind() from
 *                              the constructor to sendImpl() to prevent us
 *                              connecting to the SMSC if we are not going to
 *                              send sms. Tidied up sendImpl() to ensure unbind()
 *                              is called no matter what execution path is taken.
 * 16-Dec-02    Sumit           VBM:2002121217 - Added support for Async msging
 *                              Support for SUBMIT_MULTI and sending multiple
 *                              messages via SUBMIT_SINGLE. Support 
 *                              for the optional service type and service 
 *                              address parameters
 * 17-Jan-03    Chris W         VBM:2002111501 - Writes version of smpp jar to
 *                              info log.
 * 19-Mar-03    Geoff           VBM:2003032001 - Make initialisation property
 *                              values into public constants; these are useful
 *                              for unit tests but should be there anyway.
 * 26-Mar-03    Sumit           VBM:2003032602 - Wrapped logger.debug 
 *                              statements in if(logger.isDebugEnabled()) block
 * ----------------------------------------------------------------------------
 */
package com.volantis.mps.channels;

import com.volantis.mps.localization.LocalizationFactory;
import com.volantis.mps.message.MessageException;
import com.volantis.mps.message.MultiChannelMessage;
import com.volantis.mps.recipient.MessageRecipient;
import com.volantis.mps.recipient.MessageRecipients;
import com.volantis.mps.recipient.RecipientException;
import com.volantis.mps.session.pool.PoolableSessionFactory;
import com.volantis.mps.session.pool.SessionPool;
import com.volantis.mps.session.pool.SessionPoolImpl;
import com.volantis.mps.session.pool.SessionValidator;
import com.volantis.mps.session.pool.UnpooledSessionPool;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.smpp.Data;
import org.smpp.Session;
import org.smpp.pdu.Address;
import org.smpp.pdu.BindRequest;
import org.smpp.pdu.BindTransmitter;
import org.smpp.pdu.DestinationAddress;
import org.smpp.pdu.SubmitMultiSM;
import org.smpp.pdu.SubmitMultiSMResp;
import org.smpp.pdu.SubmitSM;
import org.smpp.pdu.SubmitSMResp;
import org.apache.commons.pool.PoolableObjectFactory;

/**
 * A channel adaptor to enable SMS messages to be sent via the OpenSMPP API.
 *
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 */
public class LogicaSMSChannelAdapter extends MessageChannel {

    /**
     * The logger to use.
     */
    private static final LogDispatcher LOGGER =
            LocalizationFactory.createLogger(LogicaSMSChannelAdapter.class);
    
    /**
     * The exception message localiser for this class.
     */
    private static ExceptionLocalizer localizer =
            LocalizationFactory.createExceptionLocalizer(
                    LogicaSMSChannelAdapter.class);

    /**
     * Name of the initialisation property used to store the address.
     */ 
    public static final String ADDRESS = "smsc-ip";

    /**
     * Name of the initialisation property used to store the port.
     */ 
    public static final String PORT = "smsc-port";

    /**
     * Name of the initialisation property used to store the username.
     */ 
    public static final String USERNAME = "smsc-user";

    /**
     * Name of the initialisation property used to store the password.
     */ 
    public static final String PASSWORD = "smsc-password";

    /**
     * Name of the initialisation property used to store the bind type.
     */ 
    public static final String BINDTYPE = "smsc-bindtype";

    /**
     * Name of the initialisation property used to store the service type.
     */ 
    public static final String SERVICE_TYPE = "smsc-svctype";

    /**
     * Name of the initialisation property used to store the service address.
     */ 
    public static final String SERVICE_ADDRESS = "smsc-svcaddr";

    /**
     * Name of the initialisation property used to store the flag which 
     * controls whether the smsc supports SUBMIT_MULTI.
     */ 
    public static final String SUPPORTS_MULTI = "smsc-supportsmulti";

    /**
     * Name of the initialisation property used to stored the flag which
     * controls whether the connections to the SMSC should be pooled or not.
     */
    public static final String SUPPORTS_POOLING = "smsc-pooling";

    /**
     * Name of the initialisation property used to store the size of the pool
     * of connections to the SMSC.
     */
    public static final String POOL_SIZE = "smsc-poolsize";

    /**
     * Name of the initialisation property used to store the string which
     * indicates how the session pool should behave if a session is requested
     * when the pool is exhausted (grow, fail or block).
     */
    public static final String ON_POOL_EXHAUSTED =
            "smsc-on-pool-exhausted";

    /**
     * Name of the initialisation property used to store the length of time (in
     * ms) which indicates how long the session pool should block for if a
     * session was requested when the pool was exhausted.
     */
    public static final String ON_POOL_EXHAUSTED_WAIT =
            "smsc-on-pool-exhausted-wait";

    /**
     * Name of the initialisation property used to store the interval (in ms)
     * between validation checks of all currently bound sessions.
     */
    public static final String VALIDATION_INTERVAL =
            "smsc-validation-interval";

    /**
     * Name of the initialisation property used to store the flag which
     * indicates if sessions should be validated before use or not.
     */
    public static final String VALIDATE_SESSION_BEFORE_USE =
            "smsc-validate-before-use";

    /**
     * Channel configuration information.
     */
    private String SMSCAddress;
    private int SMSCPort;
    private String SMSCUser;
    private String SMSCPassword;
    private String SMSCBindType;
    private String SMSCSvcType;
    private String SMSCSvcAddr;
    private boolean SMSCSupportsMulti;

    /**
     * Indicates whether this channel supports session pooling. If this is
     * false a new Session should be created for each message send.
     */
    private boolean supportsPooling;

    /**
     * Indicates how many {@link Session} instances should be managed by the
     * {@link SessionPool}. Default to having an infinitely large pool size
     * (i.e. creates a new session each time).
     */
    private int poolSize = SessionPool.UNLIMITED_ACTIVE_SESSIONS;

    /**
     * Indicates whether each session should be validated before being returned
     * from the {@link SessionPool}. Defaults to false.
     */
    private boolean validateBeforeUse = false;

    /**
     * Manages the {@link Session} instances used by this channel.
     */
    protected final SessionPool sessionPool;

    /**
     * Regularly validate all of the {@link Session} instances in the {@link
     * SessionPool}. This may be null if regular validation is not required
     * (i.e. if {@link Session} instances are validated every time they are
     * returned from the {@link SessionPool} or if the channel is communicating
     * asynchronously with the SMSC).
     */
    protected final SessionValidator sessionValidator;

    /**
     * Only used for testing.
     *
     * @param sessionFactory    to be used to create and destory sessions
     * @param channelName       identifies the channel
     * @param channelInfo       contains the channel configuration information
     *
     * @throws MessageException A problem sending the message.
     */
    protected LogicaSMSChannelAdapter(PoolableObjectFactory sessionFactory,
                                      String channelName,
                                      Map channelInfo)
            throws MessageException {

        processChannelInfo(channelName, channelInfo);
        this.sessionPool = initializeSessionPool(channelInfo, sessionFactory);
        this.sessionValidator = initializeSessionValidator(channelInfo);
    }

    /**
     * Only used for testing.
     *
     * @param sessionPool   to be used to retrieve sessions
     * @param channelName   identifies the channel
     * @param channelInfo   contains the channel configuration information
     *
     * @throws MessageException A problem sending the message.
     */
    protected LogicaSMSChannelAdapter(SessionPool sessionPool,
                                      String channelName,
                                      Map channelInfo) throws MessageException {

        processChannelInfo(channelName, channelInfo);
        this.sessionPool = sessionPool;
        this.sessionValidator = initializeSessionValidator(channelInfo);
    }

    /**
     * Creates a new LogicaSMSChannelAdapter object.
     *
     * @param channelName   identifies the channel
     * @param channelInfo   contains the channel configuration information
     *
     * @throws MessageException A problem sending the message.
     * @throws IllegalStateException A problem with MCS
     */
    public LogicaSMSChannelAdapter(String channelName, Map channelInfo)
                            throws MessageException {

        try {
            processChannelInfo(channelName, channelInfo);
            final PoolableObjectFactory sessionFactory =
                    new PoolableSessionFactory(SMSCAddress, SMSCPort, SMSCUser,
                            SMSCPassword, SMSCBindType);
            this.sessionPool = initializeSessionPool(channelInfo, sessionFactory);
            this.sessionValidator = initializeSessionValidator(channelInfo);

            BindRequest request = new BindTransmitter();
            LOGGER.info("logica-smpp-version-is",
                    new Byte(request.getInterfaceVersion()));

        } catch (Exception e) {
            throw new MessageException(e);
        }
    }

    /**
     * Process the supplied channel information and use it to configure this
     * channel instance.
     *
     * @param channelName   identifies the channel
     * @param channelInfo   contains the channel configuration information
     * @throws MessageException if there was a problem processing the channel
     * information
     */
    private void processChannelInfo(String channelName, Map channelInfo)
            throws MessageException {

        this.channelName = channelName;
        SMSCAddress = (String) channelInfo.get(ADDRESS);
        if (SMSCAddress == null) {
            throw new MessageException(
                    localizer.format("channel-property-missing",
                            new Object[] {channelName, ADDRESS}));
        }

        String portString = (String) channelInfo.get(PORT);
        if (portString == null) {
            throw new MessageException(
                    localizer.format("channel-property-missing",
                            new Object[] {channelName, PORT}));
        }
        try {
            SMSCPort = Integer.parseInt(portString);
        } catch (NumberFormatException nfe) {
            throw new MessageException(
                    localizer.format("channel-property-invalid",
                            new Object[] {PORT, portString}));
        }

        SMSCUser = (String) channelInfo.get(USERNAME);
        if (SMSCUser == null) {
            throw new MessageException(
                    localizer.format("channel-property-missing",
                            new Object[] {channelName, USERNAME}));
        }

        SMSCPassword = (String) channelInfo.get(PASSWORD);
        if (SMSCPassword == null) {
            throw new MessageException(
                    localizer.format("channel-property-missing",
                            new Object[] {channelName, PASSWORD}));
        }

        SMSCBindType = (String) channelInfo.get(BINDTYPE);
        if(SMSCBindType==null){
                SMSCBindType="async";
            }

        SMSCSvcType = (String)channelInfo.get(SERVICE_TYPE);

        SMSCSvcAddr = (String)channelInfo.get(SERVICE_ADDRESS);

        String tmp = (String)channelInfo.get(SUPPORTS_MULTI);
        if(tmp!=null){

                SMSCSupportsMulti = Boolean.valueOf(tmp).booleanValue();
            if (SMSCSupportsMulti) {
                LOGGER.info("smsc-supports-multi");
            } else {
                LOGGER.info("smsc-supports-multi-not");
            }
        }

        tmp = (String) channelInfo.get(SUPPORTS_POOLING);
        if (tmp != null) {
            if ("yes".equals(tmp)) {
                supportsPooling = true;
            }
        }
    }

    /**
     * Initialise the {@link SessionPool}.
     * 
     * @param channelInfo       containing the channel configuration information
     * @param sessionFactory    with which to initialize the session pool
     * @return SessionPool the initialized session pool
     * @throws MessageException if there was a problem initializing the pool
     */
    private SessionPool initializeSessionPool(
            Map channelInfo, PoolableObjectFactory sessionFactory)
            throws MessageException {

        final SessionPool sessionPool;
        if (!supportsPooling) {
            // If session pooling isn't supported, then we don't need to
            // check the rest of the session pooling configuration, but can
            // just create an UnpooledSessionPool which just creates a new
            // Session for each request.
            sessionPool = new UnpooledSessionPool(sessionFactory,
                        poolSize, validateBeforeUse);
        } else {
            // Otherwise determine the rest of the pool configuration info.
            poolSize = getIntValue(channelInfo, POOL_SIZE, false,
                    SessionPool.UNLIMITED_ACTIVE_SESSIONS);
            LOGGER.info("smsc-supports-pooling", new Integer(poolSize));

            // If MPS is communicating with the SMSC asynchronously then we
            // can't validate sessions.
            if (!"async".equals(SMSCBindType)) {
                String tmp = (String) channelInfo.get(
                        VALIDATE_SESSION_BEFORE_USE);
                validateBeforeUse = Boolean.valueOf(tmp).booleanValue();
            }

            // Determine what the pool should do when exhausted.
            String tmp = (String) channelInfo.get(ON_POOL_EXHAUSTED);
            final byte onPoolExhausted;
            if ("fail".equals(tmp)) {
                onPoolExhausted = SessionPoolImpl.WHEN_EXHAUSTED_FAIL;
            } else if ("block".equals(tmp)) {
                onPoolExhausted = SessionPoolImpl.WHEN_EXHAUSTED_BLOCK;
            } else if ("grow".equals(tmp)) {
                onPoolExhausted = SessionPoolImpl.WHEN_EXHAUSTED_GROW;
            } else {
                onPoolExhausted = SessionPoolImpl.WHEN_EXHAUSTED_FAIL;
                LOGGER.warn("invalid-exhausted-channel-argument", tmp);
            }

            // See how long the pool should wait for (if it should block
            // when exhausted).
            final int waitTime = getIntValue(channelInfo,
                    ON_POOL_EXHAUSTED_WAIT, false, 60000);

            // Create the session pool.
            sessionPool = new SessionPoolImpl(sessionFactory, poolSize,
                    onPoolExhausted, waitTime, validateBeforeUse);
        }

        return sessionPool;
    }

    /**
     * Initialize the {@link SessionValidator} using the configuration data
     * from the supplied Map and start it running.
     *
     * @param channelInfo   contains the channel configuration data
     * @return SessionValidator the initialized and running session validator
     * - may be null if not required
     * @throws MessageException if there was a problem initializing the
     * session validator
     */
    private SessionValidator initializeSessionValidator(Map channelInfo)
            throws MessageException {

        // If MPS is communicating with the SMSC asynchronously then we
        // can't validate sessions.
        SessionValidator sessionValidator = null;
        if (!"async".equals(SMSCBindType)) {
            // Don't bother running the session validator if each
            // session is validated before it is used.
            if (!validateBeforeUse) {
                int sessionCheckingInterval = getIntValue(channelInfo,
                        VALIDATION_INTERVAL, false, SessionValidator.WAIT_FOREVER);
                LOGGER.info("session-validation-interval",
                        new Integer(sessionCheckingInterval));

                sessionValidator = new SessionValidator(
                        sessionPool, sessionCheckingInterval);
                Thread sessionCheckerThread = new Thread(sessionValidator,
                        "Thread-SessionValidator");
                sessionCheckerThread.start();
            }
        }
        return sessionValidator;
    }

    /**
     * Send the message, either as a multi message or as many single messages
     * for multiple recipients.
     * @param message The message to send.
     * @throws MessageException Problems sending
     */
    public void send(SMSMessage message)
              throws MessageException {
        if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("Sending " + message);
        }

        String theMessage = message.getMessage();

        // Check the message length.
        if (theMessage.length() > 160) {
            throw new MessageException(
                    localizer.format("message-too-long",
                                       new Integer(theMessage.length())));
        }
        
        if(SMSCSupportsMulti) {
            sendMulti(message);
        } else {
            sendSingle(message);
        }
     
    }
    /**
     * Sends the specified list of message as many single message. This is 
     * only used if the smsc does not support sending MULTI
     * @param message   to be sent
     * @throws MessageException if there was a problem sending the message
     */
    private void sendSingle(SMSMessage message) throws MessageException{
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Sending message using sendSingle");
        }
        final SubmitSM request;
        try {
            String theMessage = message.getMessage();
            request = new SubmitSM();
            // set values
            request.setServiceType(SMSCSvcType);
            request.setSourceAddr(SMSCSvcAddr);
            request.setReplaceIfPresentFlag((byte) 0x00);
            request.setShortMessage(theMessage);
            request.setEsmClass((byte) 0x00);
            request.setProtocolId((byte) 0x00);
            request.setPriorityFlag((byte) 0x00);
            request.setRegisteredDelivery((byte) 0x00);
            request.setDataCoding((byte) 0x00);
            request.setSmDefaultMsgId((byte) 0x00);
            // send the request
            request.assignSequenceNumber(false);
        } catch (Exception e) {
            final String messageKey = "message-submit-failure";
            LOGGER.warn(messageKey);
            throw new MessageException(localizer.format(messageKey), e);
        }

        int numberOfDestinations = message.getDestinationCount();
        final List failedRecipients = new LinkedList();
        // if we have multiple message destinations we need to send it
        // multiple times as single messages
        for (int i = 0; i < numberOfDestinations; i++) {
            try {
                request.setDestAddr(new Address(message.getDestination(i)));
                if(LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Submit request " + request.debugString());
                }

                // Get a session from the pool and attempt to send the message.
                boolean submitted = false;
                SubmitSMResp response = null;
                while (!submitted) {
                    Session SMSSession = (Session) sessionPool.borrowObject();
                    try {
                        response = SMSSession.submit(request);
                        submitted = true;
                        sessionPool.returnObject(SMSSession);
                    } catch (Exception e) {
                        LOGGER.warn("message-submit-failure-retrying", e.getMessage());
                        sessionPool.invalidateObject(SMSSession);
                    }
                }

                if(response!=null){
                    if(LOGGER.isDebugEnabled()) {
                        LOGGER.debug("Submit response " +
                                response.debugString());
                    }
                    if (response.getCommandStatus() != Data.ESME_ROK) {
                        final MessageRecipient failedRecipient =
                            message.getMessageRecipient(i);
                        failedRecipient.setFailureReason(
                            localizer.format("message-submit-failure-with-status",
                                new Integer(response.getCommandStatus())));
                        failedRecipients.add(failedRecipient);
                    }
                }
            } catch (Exception e) {
                final MessageRecipient failedRecipient =
                    message.getMessageRecipient(i);
                failedRecipient.setFailureReason(e.getMessage());
                failedRecipients.add(failedRecipient);
            }
        }        
        if (!failedRecipients.isEmpty()) {
            throw new RequestFailedException(
                "message-submit-failure", failedRecipients);
        }
    }

    /**
     * Send messages to receipients using the send multi 
     * request type
     * @param message   to be sent
     * @throws MessageException if there was a problem sending the message
     */
    private void sendMulti(SMSMessage message) throws MessageException {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Sending message using sendMulti");
        }                          
        try {
            String theMessage = message.getMessage();
            SubmitMultiSM request = new SubmitMultiSM();
            SubmitMultiSMResp response = null;
            // set values
            request.setServiceType(null);
            request.setSourceAddr((String) null);

            int numberOfDestinations = message.getDestinationCount();

            for (int i = 0; i < numberOfDestinations; i++) {
                String dest = message.getDestination(i);
                final Address address;
                if(dest.startsWith("+")) {
                    address =
                            new Address((byte) 0x01, (byte) 0x0, dest.substring(1));
                } else {
                    address = new Address(dest);
                }
                request.addDestAddress(new DestinationAddress(address));
            }
            request.setServiceType(SMSCSvcType);
            request.setSourceAddr(SMSCSvcAddr);
            request.setReplaceIfPresentFlag((byte) 0x00);
            request.setShortMessage(theMessage);
            request.setEsmClass((byte) 0x00);
            request.setProtocolId((byte) 0x00);
            request.setPriorityFlag((byte) 0x00);
            request.setRegisteredDelivery((byte) 0x00);
            request.setDataCoding((byte) 0x00);
            request.setSmDefaultMsgId((byte) 0x00);
            // send the request
            request.assignSequenceNumber(false);
            if(LOGGER.isDebugEnabled()) {
                LOGGER.debug("Submit request " + request.debugString());
            }

            // Get a session from the pool and attempt to send the message.
            boolean submitted = false;
            while (!submitted) {
                Session SMSSession = (Session) sessionPool.borrowObject();
                try {
                    response = SMSSession.submitMulti(request);
                    submitted = true;
                    sessionPool.returnObject(SMSSession);
                } catch (Exception e) {
                    LOGGER.warn("message-submit-failure-retrying", e.getMessage());
                    sessionPool.invalidateObject(SMSSession);
                }
            }

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Submit response " + response.debugString());
            }
            if (response.getCommandStatus() != Data.ESME_ROK) {
                throw new MessageException(
                    localizer.format("message-submit-failure"));
            }
        } catch (MessageException e) {
            // don't wrap MessageExceptions
            throw e;
        } catch (Exception e) {
            final String messageKey = "message-submit-failure";
            LOGGER.warn(messageKey);
            throw new MessageException(
                    localizer.format(messageKey), e);
        }
    }

    // JavaDoc inherited
    protected MessageRecipients sendImpl(
            MultiChannelMessage multiChannelMessage,
            MessageRecipients messageRecipients,
            MessageRecipient messageSender)
            throws RecipientException, MessageException {

        // Maintain a record of recipients for who the message send failed
        MessageRecipients failures = new MessageRecipients();

        // Maintain references to multiple recipients so a failure can be
        // recorded against multiple recipients where to/cc/bcc are combined
        // and can each contain multiple values.
        List multipleRecipients = new ArrayList();

        Iterator recipientsIterator = messageRecipients.getIterator();

        // The message currently under construction.
        SMSMessage currentMessage = new SMSMessage();

        String deviceName = null;

        // Loop over all the recipients.
        while (recipientsIterator.hasNext()) {
            final MessageRecipient recipient =
                    (MessageRecipient) recipientsIterator.next();
            currentMessage.addRecipient(recipient);
            multipleRecipients.add(recipient);

            // check the device name
            final String currentDevice = recipient.getDeviceName();
            if (deviceName == null) {
                deviceName = currentDevice;
            } else if (currentDevice != null &&
                        !deviceName.equals(currentDevice)) {
                throw new IllegalStateException(
                    "Same device name is expected for all of the recipients.");
            }
        }

        if (deviceName == null) {
            throw new IllegalStateException(
                "At least one recipient with device name is expected.");
        }

        // Try and send the final messsage we were working on.
        try {
            currentMessage.setMessage(
                    multiChannelMessage.generateTargetMessageAsString(
                            deviceName));

            send(currentMessage);
        } catch (RequestFailedException e) {
            for (Iterator iter = e.getFailedRecipients(); iter.hasNext();) {
                final MessageRecipient failedRecipient =
                    (MessageRecipient) iter.next();
                LOGGER.warn("message-send-failed-to",
                        failedRecipient.getMSISDN());
                failures.addRecipient(failedRecipient);
            }
            LOGGER.warn("message-send-failed", e);
        } catch (MessageException me) {
            for (Iterator i = multipleRecipients.iterator(); i.hasNext();) {
                MessageRecipient failedRecipient =
                        (MessageRecipient) i.next();
                LOGGER.warn("message-send-failed-to",
                        failedRecipient.getMSISDN());
                failedRecipient.setFailureReason(me.getMessage());
                failures.addRecipient(failedRecipient);
            }            
            LOGGER.warn("message-send-failed", me);
        }

        // Return any failures
        return failures;
    }

    // Javadoc inherited.
    public void close() {
        try {
            // Close the session pool so that all pooled sessions and
            // connections are closed.
            sessionPool.close();
        } catch (Exception e) {
            LOGGER.warn("session-pool-could-not-be-closed", e);
        }
    }

    /**
     * Retrieve the channel argument with the specified argument, throwing an
     * exception if a mandatory argument is missing or is an invalid integer.
     *
     * @param channelInfo   from which to retrieve the channel argument
     * @param argumentName  which identifies the channel argument
     * @param required      true if this argument must be present, false otherwise
     * @param defaultValue  to which missing non mandatory arguments should default
     * @return int
     * @throws MessageException if there was a problem retrieving the int argument
     */
    private int getIntValue(Map channelInfo,
                            String argumentName,
                            boolean required,
                            int defaultValue) throws MessageException {
        final String tmp = (String)channelInfo.get(argumentName);
        int value;
        if (tmp == null) {
            if (required) {
                throw new MessageException(localizer.format(
                        "missing-channel-argument", argumentName));
            } else {
                value = defaultValue;
            }
        } else {
            try {
                value = Integer.parseInt(tmp);
            } catch (NumberFormatException nfe) {
                throw new MessageException(localizer.format(
                        "invalid-integer-channel-argument",
                        new Object[]{argumentName, tmp}));
            }
        }
        return value;
    }
}

/**
 * Class to hold a message along with multiple destinations
 */
class SMSMessage {

    private ArrayList destinations = new ArrayList();
    private String message;

    /**
     * Creates a new SMSMessage object.
     */
    public SMSMessage() {
    }

    /**
     * Getter for property message.
     * 
     * @return Value of property message.
     */
    public String getMessage() {

        return message;
    }

    /**
     * Setter for property message.
     * 
     * @param message New value of property message.
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Add a recipient to the list.
     *
     * @param recipient The recipient
     */
    public void addRecipient(final MessageRecipient recipient) {
        destinations.add(recipient);
    }

    /**
     * Returns the number of destinations.
     *
     * @return the number of destinations
     */
    public int getDestinationCount() {
        return destinations.size();
    }

    /**
     * Get indexth destination.
     *
     * @param index the index of the destination
     * @return The destination
     * @throws RecipientException if there is a problem retrieving the
     * destination address of the indexth recipient
     */
    public String getDestination(final int index) throws RecipientException {

        return getMessageRecipient(index).getMSISDN();
    }

    /**
     * Release the resources used by this SMSMessage so that it can be reused.
     */
    public void release() {
        destinations.clear();
        message = null;
    }

    /**
     * A string representation of the class.
     * 
     * @return The string representation.
     */
    public String toString() {

        return "Message: " + message + "\nto " + destinations;
    }

    /**
     * Returns the indexed recipient
     *
     * @param index the index of the recipient
     * @return the recipient
     */
    public MessageRecipient getMessageRecipient(final int index) {
        return (MessageRecipient) destinations.get(index);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 10-May-05	712/1	amoore	VBM:2005042815 Incorporated error logging for recipients destined for unconfigured channels

 10-May-05	710/2	amoore	VBM:2005042815 Incorporated error logging for recipients destined for unconfigured channels

 06-May-05	693/1	amoore	VBM:2005050315 Added file check for message attachments to ensure they are valid

 05-May-05	671/3	amoore	VBM:2005050315 Updated attachment file check logic and maintained coding standards

 05-May-05	671/1	amoore	VBM:2005050315 Added file check for message attachments to ensure they are valid

 04-May-05	666/1	philws	VBM:2005050311 Port of failureReason from 3.3

 04-May-05	660/1	philws	VBM:2005050311 Add failureReason property API to MessageRecipient, set failureReasons in channel adapters and show example usage of failureReason

 29-Apr-05	614/1	amoore	VBM:2005042509 Refactored MessageChannel to implements redundant recipient processing in subclasses

 05-Apr-05	481/1	geoff	VBM:2005033110 SMS not generating messages correctly.

 05-Apr-05	475/1	geoff	VBM:2005033110 SMS not generating messages correctly.

 02-Mar-05	371/1	emma	VBM:2005022812 mergevbm from MPS 3.3

 02-Mar-05	366/1	emma	VBM:2005022812 Fixing leftover localization logging problems

 29-Nov-04	243/3	geoff	VBM:2004112302 Provide new Logging Infrastructure: MPS

 17-Nov-04	238/1	pcameron	VBM:2004111608 PublicAPI doc fixes and additions

 19-Oct-04	198/1	matthew	VBM:2004101311 allow mss logging to work (stop MCS from hijacking it)

 11-Aug-04	149/1	claire	VBM:2004073005 WAP Push for MPS: New channel adapter, generating messages as URLs, config update

 14-Jul-04	136/4	claire	VBM:2004070301 Implementing failed recipients management so code adheres to API and JavaDoc

 01-Jul-03	14/1	sumit	VBM:2002121217 Added logging for SUPPORTS_MULTI SMSC parameter

 ===========================================================================
*/
