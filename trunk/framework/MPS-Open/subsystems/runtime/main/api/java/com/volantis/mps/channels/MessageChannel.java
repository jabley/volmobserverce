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
 * $Header: /src/mps/com/volantis/mps/channels/MessageChannel.java,v 1.4 2003/03/20 10:15:36 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 12-Nov-02    ianw            VBM:2002103008 - Created
 * 13-Nov-02    ianw            VBM:2002111211 - Change to return 
 *                              MessageRecipients.
 * 29-Nov-02    Sumit           VBM:2002112602 - Changed to be an Abstract 
 *                              class and implemented send method to generate
 *                              a MultiChannelMessage with attachments specific
 *                              to this channel 
 * 19-Mar-03    Geoff           VBM:2003032001 - Make initialisation property
 *                              values into public constants; these are useful
 *                              for unit tests but should be there anyway.
 * ----------------------------------------------------------------------------
 */


package com.volantis.mps.channels;

import com.volantis.mps.attachment.AttachmentUtilities;
import com.volantis.mps.localization.LocalizationFactory;
import com.volantis.mps.message.MessageException;
import com.volantis.mps.message.MessageInternals;
import com.volantis.mps.message.MultiChannelMessage;
import com.volantis.mps.recipient.MessageRecipient;
import com.volantis.mps.recipient.MessageRecipients;
import com.volantis.mps.recipient.RecipientException;
import com.volantis.synergetics.localization.MessageLocalizer;
import com.volantis.synergetics.log.LogDispatcher;

import java.util.HashMap;
import java.util.Iterator;

/**
 * <p>This abstract class describes the interface for defining protocol-specific
 * adapters for sending messages via specific protocols.</p>
 *
 * <p><strong>Implementations must be designed for use in a multi-threaded
 * environment.</strong></p>
 * @volantis-api-include-in PublicAPI
 */
public abstract class MessageChannel {
    
    /**
     * The logger to use
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(MessageChannel.class);

    private static final MessageLocalizer messageLocalizer =
            LocalizationFactory.createMessageLocalizer(
                    MessageChannel.class);

    /**
     * The channel name representing the current channel.
     */
    protected String channelName;
    
    /**
     * A cache or already channelised MultiChannelMessages.
     */
    HashMap cache;

    /**
     * Sends the message via the channel.
     * <p>
     * @param multiChannelMessage The message.
     * @param messageRecipients   The list of recipients.
     * @param messageSender       The sender of the message.
     *
     * @return A list of failed recipients.
     *
     * @throws RecipientException if a problem occurred during recipient
     *                            processing.
     * @throws MessageException   if a problem occurred during message
     *                            processing.
     */
    public MessageRecipients send(MultiChannelMessage multiChannelMessage,
                    MessageRecipients messageRecipients,
                    MessageRecipient messageSender)
                        throws RecipientException, MessageException {
        MultiChannelMessage mcmClone = getCached(multiChannelMessage);
        if (mcmClone == null) {
            mcmClone = (MultiChannelMessage) multiChannelMessage.clone();
        }
        if (mcmClone != null) {
            try {
                mcmClone.addAttachments(
                        AttachmentUtilities.getAttachmentsForChannel(
                                getChannelName(),
                                MessageInternals.getAttachments(
                                        multiChannelMessage)));

                cache.put(multiChannelMessage, mcmClone);
            } catch (Exception e) {
                logger.error("multi-channel-message-channelise-attachments-failure",
                             e);
            }
        } else {
            logger.error("multi-channel-message-clone-failure-send-failed-for",
                         getChannelName());
            return messageRecipients;
        }

        // generate message recipient lists for each device
        // then call send with new device specific message
        // recipients list

        // maintain list of all failures for all device names
        MessageRecipients failures = new MessageRecipients();

        // list that is regenerated for each device name
        MessageRecipients deviceSpecificRecips = new MessageRecipients();

        // current device name
        String currentDeviceName = null;

        Iterator recipientsIterator = messageRecipients.getIterator();

        while (recipientsIterator.hasNext()) {

            MessageRecipient recipient =
                    (MessageRecipient) recipientsIterator.next();

            if (recipient.resolveDeviceName(false) != MessageRecipient.OK) {
                logger.warn("device-resolution-failed-for", recipient);
                recipient.setFailureReason(
                        messageLocalizer.format(
                                "device-resolution-failed-for",
                                recipient));
                failures.addRecipient(recipient);
                continue;
            }

            // if recipient is on this channel
            if (recipient.getChannelName().equals(channelName)) {

                // if we are already creating a device specific recipient
                // list
                if (currentDeviceName != null) {

                    // if this recipient is targeted for a different device than
                    // the last recipient
                    if (!currentDeviceName.equals(recipient.getDeviceName())) {

                        // send exisiting message for current device and
                        // then create a new device dependent recipient list
                        boolean failed = false;
                        Exception cause = null;
                        try {
                            // send message via implemented sendImpl and hold
                            // over failures
                            MessageRecipients localFails =
                                    sendImpl(multiChannelMessage,
                                             deviceSpecificRecips,
                                             messageSender);
                            Iterator lfIterator = localFails.getIterator();

                            while (lfIterator.hasNext()) {
                                failures.addRecipient(
                                        (MessageRecipient) lfIterator.next());
                            }
                        } catch (MessageException me) {
                            failed = true;
                            cause = me;
                        } catch (RecipientException re) {
                            failed = true;
                            cause = re;
                        } finally {
                            if (failed) {
                                populateFailures(deviceSpecificRecips,
                                                 failures,
                                                 cause);

                                continue;
                            }
                        }

                        deviceSpecificRecips = new MessageRecipients();
                        currentDeviceName = recipient.getDeviceName();
                        deviceSpecificRecips.addRecipient(recipient);
                    } else {
                        // else add this recipient to the current device specific
                        // recipient list
                        deviceSpecificRecips.addRecipient(recipient);
                    }
                } else {
                    // this must be the first recipient in the list so we
                    // start the processing by creating a new device specific
                    // recipients list and setting the current device name
                    // to the recipients device
                    currentDeviceName = recipient.getDeviceName();
                    deviceSpecificRecips = new MessageRecipients();
                    deviceSpecificRecips.addRecipient(recipient);
                }
            }

            // ignore recipients for other channels
        }

        // send final message
        boolean failed = false;
        Exception cause = null;
        try {
            MessageRecipients localFails = sendImpl(multiChannelMessage,
                                                    deviceSpecificRecips,
                                                    messageSender);
            Iterator lfIterator = localFails.getIterator();
            while (lfIterator.hasNext()) {
                failures.addRecipient((MessageRecipient) lfIterator.next());
            }
        } catch (MessageException me) {
            failed = true;
            cause = me;
        } catch (RecipientException re) {
            failed = true;
            cause = re;
        } finally {
            if (failed) {
                populateFailures(deviceSpecificRecips, failures, cause);
            }
        }

        // return failures to caller
        return failures;
    }

    /**
     * Takes the device specific recipients listed and adds them to the
     * specified failures list, setting each recipient's failure exception
     * to the given causative exception.
     *
     * @param deviceSpecificRecips the device-specific recipients for
     *                             which sends have failed
     * @param failures             the resulting set of failed recipients
     * @param cause                the exception that caused the failure
     * @throws RecipientException if there is a problem populating the
     *                            failures list
     */
    private void populateFailures(MessageRecipients deviceSpecificRecips,
                                  MessageRecipients failures,
                                  Exception cause)
            throws RecipientException {
        Iterator fIterator = deviceSpecificRecips.
                getIterator();
        while (fIterator.hasNext()) {
            MessageRecipient
                    failedRecipient =
                    (MessageRecipient) fIterator.next();

            String address = getAddress(failedRecipient);

            if (cause instanceof MessageException) {
                logger.warn("message-send-failed-to", address);
            } else {
                logger.warn("recipient-exception-for", address);
            }

            if ((failedRecipient.getFailureReason() == null) &&
                    (cause != null)) {
                failedRecipient.setFailureReason(cause.getMessage());
            }

            failures.addRecipient(failedRecipient);
        }
    }

    /**
     * Utility method to obtain an address for a recipient.
     *
     * @param recipient The recipient to resolve
     *
     * @return A <code>String</code> representing the specified recipient's
     *         <code>InternetAddress</code> or <code>MSISDN</code>
     *
     * @throws RecipientException if there was a problem getting the address
     */
    private String getAddress(MessageRecipient recipient)
            throws RecipientException {
        if (recipient.getAddress() != null) {
            return recipient.getAddress().getAddress();
        } else {
            return recipient.getMSISDN();
        }
    }

    /**
     * Sends the message via the channel.
     * <p>
     * @param multiChannelMessage The message.
     * @param messageRecipients   The list of recipients.
     * @param messageSender       The sender of the message.
     *
     * @return A list of failed recipients.
     *
     * @throws RecipientException if a problem occurred during recipient
     *                            processing.
     * @throws MessageException   if a problem occurred during message
     *                            processing.
     */
    protected abstract MessageRecipients sendImpl(
            MultiChannelMessage multiChannelMessage,
            MessageRecipients messageRecipients,
            MessageRecipient messageSender)
            throws RecipientException, MessageException;

    /**
     * Retrieves the channel name being used. This can be used to confirm that
     * messages are being sent to the correct channel.
     * <p>
     * @return the name of the channel
     */
    public String getChannelName(){
        return channelName;
    }
    
    private MultiChannelMessage getCached(MultiChannelMessage nonChannelised) {
        if(cache==null) {
            cache= new HashMap();
            return null;
        }
        return (MultiChannelMessage)cache.get(nonChannelised);
    }

    /**
     * Close any resources managed by this message channel.
     */
    public void close() {
        // Default implementation does nothing (for backwards compatibility).
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Jul-05	829/3	amoore	VBM:2005052302 Fixed encoding problems that caused DB characters to be represented by '?'

 10-May-05	712/3	amoore	VBM:2005042815 Incorporated error logging for recipients destined for unconfigured channels

 10-May-05	712/1	amoore	VBM:2005042815 Incorporated error logging for recipients destined for unconfigured channels

 10-May-05	710/1	amoore	VBM:2005042815 Incorporated error logging for recipients destined for unconfigured channels

 04-May-05	666/1	philws	VBM:2005050311 Port of failureReason from 3.3

 04-May-05	660/1	philws	VBM:2005050311 Add failureReason property API to MessageRecipient, set failureReasons in channel adapters and show example usage of failureReason

 29-Apr-05	614/2	amoore	VBM:2005042509 Refactored MessageChannel to implements redundant recipient processing in subclasses

 29-Nov-04	243/3	geoff	VBM:2004112302 Provide new Logging Infrastructure: MPS

 17-Nov-04	238/2	pcameron	VBM:2004111608 PublicAPI doc fixes and additions

 19-Oct-04	198/1	matthew	VBM:2004101311 allow mss logging to work (stop MCS from hijacking it)

 11-Aug-04	149/3	claire	VBM:2004073005 WAP Push for MPS: New channel adapter, generating messages as URLs, config update

 05-Aug-04	149/1	claire	VBM:2004073005 WAP Push for MPS: New channel adapter, generating messages as URLs, config update

 10-Jun-04	121/1	ianw	VBM:2004060111 Made to work with main 3.2 MCS stream

 ===========================================================================
*/
