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

import org.smpp.Data;
import org.smpp.Session;
import org.smpp.SmppException;
import org.smpp.pdu.EnquireLinkResp;
import com.volantis.mps.localization.LocalizationFactory;
import com.volantis.synergetics.log.LogDispatcher;

import java.io.IOException;
import java.util.Iterator;

/**
 * Regularly checks (at specified intervals) that the sessions in the supplied
 * pool are still valid (by sending EnquireLink PDUs to the SMSC).
 */
public class SessionValidator implements Runnable {

    /**
     * Used for logging.
     */
    private static final LogDispatcher LOGGER =
            LocalizationFactory.createLogger(SessionValidator.class);

    /**
     * Pool whose sessions should be regularly validated.
     */
    private final SessionPool sessionPool;

    /**
     * Flag which indicates that the validator should wait forever i.e.
     * not check.
     */
    public static final int WAIT_FOREVER = -1;

    /**
     * The interval at which the sessions should be validated in ms (Defaults
     * to waiting forever).
     */
    protected int validationInterval = WAIT_FOREVER;

    /**
     * Initialize a new instance using the given parameters.
     *
     * @param sessionPool               whose contents to validate
     * @param validationInterval        the time to sleep between checking the
     *                                  sessions' state.
     */
    public SessionValidator(SessionPool sessionPool, int validationInterval) {
        this.sessionPool = sessionPool;
        this.validationInterval = validationInterval;
    }

    public void run() {
        // Only bother starting to check if a non infinite interval has been
        // specified.
        if (validationInterval != WAIT_FOREVER) {
            // Once started should keep checking forever.
            while (true) {
                try {
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("SessionValidator will now sleep for " +
                                validationInterval + " ms");
                    }
                    Thread.sleep(validationInterval);
                } catch (InterruptedException e) {
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("SessionValidator was interrupted", e);
                    }
                }

                try {
                    synchronized (sessionPool) {
                        // Note when the sweep started...
                        long timer = System.currentTimeMillis();

                        // Go through the pool and check that all the available
                        // borrowable objects are still valid.
                        Iterator i = sessionPool.iterator();
                        while (i.hasNext()) {
                            Session session = (Session)i.next();
                            validateSession(session);
                        }

                        if (LOGGER.isDebugEnabled()) {
                            LOGGER.debug("Finished checking sessions: [took "
                                    + (System.currentTimeMillis() - timer)
                                    + " ms]");
                        }
                    }
                } catch (Exception e) {
                    LOGGER.warn("An exception occurred while validating " +
                            "sessions - ignoring", e);
                }
            }
        }
    }

    /**
     * Validate the supplied {@link Session} by sending an EnquireLink PDU.
     * NB: When running MPS asynchronously, sessions will always appear to be
     * invalid as they don't respond synchronously.
     *
     * @param session   to be validated
     * @throws Exception if there was a problem validating the session
     */
    private void validateSession(Session session) throws Exception {
        // If a valid EnquireLinkResponse is not received, then the session is
        // considered to be invalid. NB: this will always be the case when MPS
        // is communicating asynchronously with the SMSC.
        if (!sendEnquireLink(session)) {
            // The session is no longer valid and should be removed from the
            // pool. A new one will be created when it is next needed.
            sessionPool.invalidateObject(session);
        } else {
            // Session is still valid
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Session "+ session + " is still valid");
            }
        }

    }

    /**
     * This method attempts to send an EnquireLink PDU over a bound Session to
     * an SMSC. Returns true if an valid EnquireLinkResponse PDU is recieved in
     * response, and false otherwise.
     * <p/>
     * NB: the response will always be null when MPS is communicating
     * asynchronously with the SMSC.
     *
     * @param session   for which to send an enquireLink PDU
     * @return true if a valid EnquireLinkResponse was received and false
     * otherwise
     */
    public static boolean sendEnquireLink(Session session) {
        boolean valid = false;
        if (session != null) {
            EnquireLinkResp enquireResp = null;

            try {
                enquireResp = session.enquireLink();
            } catch (SmppException e) {
                LOGGER.error("SmppException while sending EnquireLink PDU", e);
            } catch (IOException e) {
                LOGGER.error("IOException while sending EnquireLink PDU", e);
            }

            if (enquireResp != null) {
                valid = enquireResp.getCommandStatus() == Data.ESME_ROK;
                if (!valid) {
                    LOGGER.warn("Unexpected response to EnquireLink PDU: " +
                            enquireResp.debugString());
                }
            } else {
                LOGGER.error("No response to the EnquireLink PDU - may be " +
                        "communicating asynchronously or the session " +
                        "may be invalid");
            }
        }
        return valid;
    }

    /**
     * Retrieve the interval at which all the sessions in the pool will be
     * revalidated by this session validator.
     *
     * @return int the interval at which all the sessions in the pool will be
     * revalidated by this session validator.
     */
    public int getValidationInterval() {
        return validationInterval;
    }
}
