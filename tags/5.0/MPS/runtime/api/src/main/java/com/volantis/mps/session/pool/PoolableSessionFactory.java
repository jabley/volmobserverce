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

import com.volantis.mps.channels.SMSCResponseListener;
import com.volantis.mps.localization.LocalizationFactory;
import com.volantis.mps.message.MessageException;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;

import java.io.IOException;

import org.apache.commons.pool.PoolableObjectFactory;
import org.smpp.Connection;
import org.smpp.Data;
import org.smpp.Session;
import org.smpp.TCPIPConnection;
import org.smpp.TimeoutException;
import org.smpp.WrongSessionStateException;
import org.smpp.pdu.BindRequest;
import org.smpp.pdu.BindResponse;
import org.smpp.pdu.BindTransmitter;
import org.smpp.pdu.PDUException;
import org.smpp.pdu.UnbindResp;

/**
 * {@link PoolableObjectFactory} implementation which provides {@link Session}
 * instances.
 *
 * @mock.generate
 */
public class PoolableSessionFactory implements PoolableObjectFactory {

    /**
     * Used for logging.
     */
    private static final LogDispatcher LOGGER =
            LocalizationFactory.createLogger(PoolableSessionFactory.class);
    
    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer EXCEPTION_LOCALIZER =
            LocalizationFactory.createExceptionLocalizer(
                    PoolableSessionFactory.class);

    /**
     * Address of the SMSC to which the {@link Session} instances created by
     * this factory should be bound.
     */
    private String address;

    /**
     * Port of the SMSC to which the {@link Session} instances created by
     * this factory should be bound.
     */
    private int port;

    /**
     * Username with which the {@link Session} instances created by
     * this factory should bind to the SMSC.
     */
    private String SMSCUser;

    /**
     * Password with which the {@link Session} instances created by
     * this factory should bind to the SMSC.
     */
    private String SMSCPassword;

    /**
     * Indicates whether the {@link Session} instances created by this factory
     * should be bound synchronously or asynchronously to the SMSC.
     */
    private String SMSCBindType;

    /**
     * Initialize a new instance using the given parameters.
     *
     * @param address       address of the SMSC to which the sessions created
     *                      by this factory should be bound
     * @param port          port of the SMSC to which the sessions created
     *                      by this factory should be bound
     * @param SMSCUser      username with which to bind to the SMSC
     * @param SMSCPassword  password with which to bind to the SMSC
     * @param SMSCBindType  indicates whether the session should be bound
     */
    public PoolableSessionFactory(String address,
                                  int port,
                                  String SMSCUser,
                                  String SMSCPassword,
                                  String SMSCBindType) {
        this.address = address;
        this.port = port;
        this.SMSCUser = SMSCUser;
        this.SMSCPassword = SMSCPassword;
        this.SMSCBindType = SMSCBindType;
    }

    // Javadoc inherited.
    public Object makeObject() throws MessageException {
        Connection connection = new TCPIPConnection(address, port);
        connection.setReceiveTimeout(20 * 1000);
        Session session = new Session(connection);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Created a new org.smpp.Session : "+ session);
        }
        bind(session);
        return session;
    }

    // Javadoc inherited.
    public void destroyObject(Object object) throws MessageException {
        try {
            Session session = (Session) object;
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Destroying org.smpp.Session : " + session);
            }
            // Unbind the session.
            unbind(session);

            // Close the unbound session.
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Attempting to close Session.");
            }
            session.close();
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Successfully closed Session.");
            }

            // Close any associated connection.
            Connection c = session.getConnection();
            if (c != null) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Attempting to close the open connection.");
                }
                c.close();
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Successfully closed the Connection.");
                }
            }
        } catch (Exception e) {
            final String messageKey = "smsc-unbind-error";
            LOGGER.error(messageKey);
            throw new MessageException(
                    EXCEPTION_LOCALIZER.format(messageKey), e);
        }
    }

    // Javadoc inherited.
    public boolean validateObject(Object object) {
        boolean isValid = false;
        // Need to check that the object is actually a session and that it is
        // alive and well.
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Validating session object : " + object);
        }

        if (object instanceof Session) {
            Session session = (Session) object;

            // Verify that the session is open, bound and responsive to
            // enquire link PDUs.
            if (session.isOpened() && session.isBound() &&
                    SessionValidator.sendEnquireLink(session)) {
                isValid = true;
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Session was successfully validated");
                }
            }
        }

        if (!isValid) {
            // Warn that the session was found to be invalid - the SessionPool
            // will handle getting another one.
            LOGGER.warn("session-invalid");
        }
        return isValid;
    }

    // Javadoc inherited.
    public void activateObject(Object object) throws Exception {
        // NO-OP
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Activating org.smpp.Session : " + object);
        }
    }

    // Javadoc inherited.
    public void passivateObject(Object object) throws Exception {
        // NO-OP
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Passivating org.smpp.Session : " + object);
        }
    }

    /**
     * Bind to the SMSC.
     *
     * If the SMSC supports asynchronous binding then create a listener so that
     * responses can be processed asynchronously.
     *
     * @param session   which should be bound to the SMSC as a transmitter
     * @throws MessageException if there was a problem binding to the SMSC
     */
    protected void bind(Session session) throws MessageException {

        BindRequest request = new BindTransmitter();
        BindResponse response;
        LOGGER.info("logica-smpp-version-is",
                new Byte(request.getInterfaceVersion()));
        
        // Send the request.
        if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("Bind request " + request.debugString());
        }

        try {
            request.setSystemId(SMSCUser);
            request.setPassword(SMSCPassword);
            request.setSystemType(null);
            request.setInterfaceVersion((byte) 0x34);

            if("async".equals(SMSCBindType)){                
                response = session.bind(request, new SMSCResponseListener());
            } else {
                response = session.bind(request);
            }

            if (response != null) {
                if (response.getCommandStatus() == Data.ESME_ROK) {
                    LOGGER.debug("Successfully bound to SMSC.");
                } else {
                    LOGGER.error("Unexpected response to bind. " +
                            "Response is " + response.debugString());
                }
            } else {
                LOGGER.debug("BindResponse was null when SMSCBindType was " +
                        SMSCBindType);
            }
        } catch (Exception e) {
            final String messageKey = "smsc-bind-error";
            LOGGER.fatal(messageKey, e);
            throw new MessageException(
                    EXCEPTION_LOCALIZER.format(messageKey), e);
        }
    }

    /**
     * Unbind from the SMSC.
     *
     * @param session   which should be unbound from the SMSC
     * @throws WrongSessionStateException if the session was in an
     * unexpected state
     * @throws TimeoutException if there was a problem unbinding from the SMSC
     * @throws IOException if there was a problem unbinding from the SMSC
     * @throws PDUException if the PDU was incorrectly formatted
     */
    protected void unbind(Session session) throws WrongSessionStateException,
            TimeoutException, IOException, PDUException {

        LOGGER.debug("Attempting to unbind the session");
        UnbindResp response = session.unbind();

        if (response != null) {
            if (response.getCommandStatus() == Data.ESME_ROK) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("The session was successfully unbound");
                }
            } else {
                LOGGER.warn("Unexpected response to unbind. Response is "
                        + response.debugString());
            }
        } else {
            LOGGER.debug("UnbindResponse was null when SMSCBindType was " +
                    SMSCBindType);
        }
    }
}
