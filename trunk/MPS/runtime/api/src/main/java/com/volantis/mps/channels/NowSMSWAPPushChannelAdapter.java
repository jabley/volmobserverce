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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mps.channels;

import com.volantis.mps.localization.LocalizationFactory;
import com.volantis.mps.message.MessageException;
import com.volantis.mps.message.MultiChannelMessage;
import com.volantis.mps.recipient.MessageRecipient;
import com.volantis.mps.recipient.MessageRecipients;
import com.volantis.mps.recipient.RecipientException;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.localization.MessageLocalizer;
import com.volantis.synergetics.log.LogDispatcher;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * This is an implementation of a {@link MessageChannel} for use with MPS.  It
 * provides WAP Push functionality via the <a href="http://www.nowsms.com/"
 * title="NowSMS">NowSMS</a> gateway.
 *
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 */
public class NowSMSWAPPushChannelAdapter extends PhoneGatewayChannelAdapter {

    /**
     * The logger to use
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(NowSMSWAPPushChannelAdapter.class);

    /**
     * The exception localizer instance for this class.
     */
    private static final ExceptionLocalizer localizer =
            LocalizationFactory.createExceptionLocalizer(
                    NowSMSWAPPushChannelAdapter.class);

    /**
     * Used to retrieve localized messages.
     */
    private static final MessageLocalizer messageLocalizer =
            LocalizationFactory.createMessageLocalizer(
                    NowSMSWAPPushChannelAdapter.class);

    /**
     * The parameter containing the URL of the message store servlet
     */
    public static final String MSS_URL = "message-store-url";

    /**
     * The destination key used when constructing a NowSMS WAP Push URL.
     */
    public static final String DESTINATION = "PhoneNumber";

    /**
     * The message url key used when constructing a NowSMS WAP Push URL.
     */
    public static final String MESSAGE = "WAPURL";

    /**
     * The subject key used when constructing a NowSMS WAP Push URL.  The
     * presence of this indicates the message will be sent as a Service
     * Indicator (SI) message.  Essentially either this key or {@link
     * #NO_SUBJECT_KEY} should be used.  It makes no sense to use both.
     */
    public static final String SUBJECT = "Text";

    /**
     * The key used when constructing a NowSMS WAP Push URL that does not have
     * a subject specified or the sending should force a Service Load (SL)
     * message.   Essentially either this key or {@link #SUBJECT} should be
     * used.  It makes no sense to use both.
     */
    public static final String NO_SUBJECT_KEY = "WAPSL";

    /**
     * The value that should be used with {@link #NO_SUBJECT_KEY} when creating
     * a key/value pair.
     */
    public static final String NO_SUBJECT_VALUE = "1";

    /**
     * The URL of the MSS Servlet;
     */
    private final String mssUrl;

    /**
     * Initialise a new instance of NowSMSWAPPushChannelAdapter
     *
     * @param channelName The name of the channel
     * @param channelInfo The parameters defined for the channel
     * @throws MessageException If there is a problem initialising the channel
     */
    public NowSMSWAPPushChannelAdapter(String channelName,
                                       Map channelInfo)
            throws MessageException {
        super(channelName, channelInfo);
        mssUrl = (String) channelInfo.get(MSS_URL);
    }

    // JavaDoc inherited
    protected MessageRecipients sendImpl(MultiChannelMessage multiChannelMessage,
                                         MessageRecipients messageRecipients,
                                         MessageRecipient messageSender)
            throws RecipientException, MessageException {

        // Maintain a record of recipients for who the message send failed
        MessageRecipients failures = new MessageRecipients();

        Iterator recipientsIterator = messageRecipients.getIterator();

        // pick up device name from first recipient in list
        // and reset iterator
        String currentDeviceName =
                ((MessageRecipient) recipientsIterator.next()).getDeviceName();
        recipientsIterator = messageRecipients.getIterator();
        String messageURL = createMessage(multiChannelMessage,
                                          currentDeviceName);

        // NowSMS implements "send" by this code talking to the server
        // using a url with paramters in a get request format for destination,
        // message, subject, ...
        // This means that there is no composite send of the same message
        // to multiple recipients in one go; for each recipient this code needs
        // to do a send to the server!


        while (recipientsIterator.hasNext()) {
            MessageRecipient recipient = (MessageRecipient) recipientsIterator.next();

            try {
                // Construct parameters
                String parameterString =
                        constructParameters(formatMSISDN(recipient.getMSISDN(),
                                                         false,
                                                         true),
                                            messageURL,
                                            multiChannelMessage.getSubject());
                // Send the message
                boolean sendSuccess = sendAsGetRequest(parameterString);
                if (!sendSuccess) {
                    handleFailure(recipient, null, failures);
                }
            } catch (MessageException me) {
                handleFailure(recipient, me, failures);
            } catch (IOException ioe) {
                handleFailure(recipient, ioe, failures);
            }
        }

        // Return any failures
        return failures;
    }

    /**
     * Handle a failure to send a message to a given recipient.  Since this is
     * an method internal to this class, no checks are made on parameters and
     * it is assumed that the failures list and recipient will be valid
     * object.
     *
     * @param recipient The recipient to whom the message send failed on.
     * @param exception The exception that occurred
     * @param failures  The list of failures which should be updated.
     * @throws RecipientException If there was a problem storing the recipient
     */
    private void handleFailure(MessageRecipient recipient,
                               Exception exception,
                               MessageRecipients failures)
            throws RecipientException {

        // Log the actual exception as well as the message
        logger.warn("message-send-failed-to", recipient.getMSISDN(),
                    exception);

        if (recipient.getFailureReason() == null) {
            if (exception != null) {
                recipient.setFailureReason(exception.getMessage());
            } else {
                recipient.setFailureReason(messageLocalizer.format(
                        "message-send-failed-to", recipient.getMSISDN()));
            }
        }

        // And store the recipient in the failure list
        failures.addRecipient(recipient);
    }

    /**
     * Given a multi-channel message and the current device name create the
     * message as a URL with the protocol removed so that it is valid for use
     * with the NowSMS gateway.
     *
     * @param multiChannelMessage The message to generate as a URL.
     * @param currentDeviceName   The current device so that the message can be
     *                            tailored for it.
     * @return A string representation of the url of the message without a
     *         protocol.
     * @throws MessageException If there is a problem in generating the message
     *                          as a URL.
     */
    protected String createMessage(MultiChannelMessage multiChannelMessage,
                                   String currentDeviceName)
            throws MessageException {
        String messageURL;
        // Remove protocol so the URL should be left with it
        // in the form of /server/path/[file.x]
        messageURL = removeProtocol(
                multiChannelMessage.generateTargetMessageAsURL(
                        currentDeviceName,
                        mssUrl));
        return messageURL;
    }

    /**
     * This takes the destination number (in the correct format for sending),
     * the message url, and optionally a subject, and then creates a valid
     * paramater string of these for use with the NowSMS gateway.
     *
     * @param MSISDN  The number to send the message to.  This needs to be in
     *                the correct format (i.e. any + needs to be %2B) and ready
     *                to use.  May not be null.
     * @param message The url of the message, also in the correct format
     *                without the protocol, to be added to the parameter
     *                string. May not be null.
     * @param subject The subject (if one exists, may be null) to use with the
     *                message.  This controls whether the message is sent as a
     *                SI or SL message.  If not specified the message is send
     *                as SL.
     * @return A string that contains all parameters in a form that can be used
     *         with a get request.
     * @throws MessageException If there is a problem in constructing the
     *                          parameters.  This is thrown in all cases, and
     *                          undeclared exceptions such as <code>IllegalArgumentException</code>
     *                          are never used.  This is because all exceptions
     *                          need to be caught within the send to record
     *                          failures.
     */
    protected String constructParameters(String MSISDN,
                                         String message,
                                         String subject)
            throws MessageException {
        // Handle any null paramter values as exceptional conditions
        if (MSISDN == null) {
            throw new MessageException(localizer.format("msisdn-null-invalid"));
        }
        if (message == null) {
            throw new MessageException(localizer.format("message-null-invalid"));
        }
        // Subject can be null, in which case use SL not SI

        // Create pair arrays of key-value combinations, encoding the
        // subject if necesary but not the message as this is assumed to be a
        // valid URL and requires the / to be left as / for the NowSMS gateway.
        // It is also assumed the number is encoded OK too.
        String[] destination = new String[]{DESTINATION, MSISDN};
        String[] content = new String[]{MESSAGE, message};
        String[] title = new String[2];
        if (subject == null) {
            title[0] = NO_SUBJECT_KEY;
            title[1] = NO_SUBJECT_VALUE;
        } else {
            title[0] = SUBJECT;
            title[1] = URLEncoder.encode(subject);
        }

        List parameters = new ArrayList();
        parameters.add(destination);
        parameters.add(content);
        parameters.add(title);

        // Obtain and return an appropriate unencoded string
        return constructParamString(parameters, false);
    }

    /**
     * This converts a <code>URL</code> into a string representation of that
     * url and also removes the protocol.  This is required for sending urls as
     * WAP Push urls via the NowSMS gateway. <p/> Examples:<br />
     * <pre>
     * http://www.volantis.com/ becomes www.volantis.com/
     * http://a.b.com/vt/somefile.xml becomes a.b.com/vt/somefile.xml
     * </pre>
     *
     * @param url The URL to convert to a protocol-less string url.  If the
     *            protocol does not exist then the string form of the url as
     *            passed in is returned unaltered.
     * @return A string containing the url without a specified protocol
     * @throws MessageException If there is a problem in removing the protocol.
     *                          This is thrown in all cases, and undeclared
     *                          exceptions such as <code>IllegalArgumentException</code>
     *                          are never used.  This is because all exceptions
     *                          need to be caught within the send to record
     *                          failures.
     */
    protected String removeProtocol(URL url) throws MessageException {
        if (url == null) {
            throw new MessageException(localizer.format(
                    "message-url-null-invalid"));
        }
        String urlString = url.toExternalForm();
        int index = urlString.indexOf("://");
        return index == -1 ? urlString : urlString.substring(index + 3);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 07-Jul-05	829/1	amoore	VBM:2005052302 Fixed encoding problems that caused DB characters to be represented by '?'

 06-May-05	693/1	amoore	VBM:2005050315 Added file check for message attachments to ensure they are valid

 01-Jul-05	776/2	amoore	VBM:2005052302 Fixed encoding problems that caused DB characters to be represented by '?'

 05-May-05	671/1	amoore	VBM:2005050315 Removed javax.swing.* imports

 04-May-05	666/1	philws	VBM:2005050311 Port of failureReason from 3.3

 04-May-05	660/1	philws	VBM:2005050311 Add failureReason property API to MessageRecipient, set failureReasons in channel adapters and show example usage of failureReason

 29-Apr-05	614/1	amoore	VBM:2005042509 Refactored MessageChannel to implements redundant recipient processing in subclasses

 29-Apr-05	624/4	amoore	VBM:2005042509 Refactored MessageChannel to include redundant recipient processing in subclasses

 27-Apr-05	624/1	amoore	VBM:2005042509 Moved device specific recipient processing into MessageChannel class

 21-Mar-05	435/1	ianw	VBM:2005031506 Allow MSS to be installed without MPS

 21-Mar-05	426/1	ianw	VBM:2005031506 Allow MSS to be installed without MPS

 29-Nov-04	243/3	geoff	VBM:2004112302 Provide new Logging Infrastructure: MPS

 17-Nov-04	238/1	pcameron	VBM:2004111608 PublicAPI doc fixes and additions

 19-Oct-04	198/1	matthew	VBM:2004101311 allow mss logging to work (stop MCS from hijacking it)

 13-Aug-04	155/1	claire	VBM:2004073006 WAP Push for MPS: Servlet to store and retrieve messages

 11-Aug-04	149/3	claire	VBM:2004073005 WAP Push for MPS: New channel adapter, generating messages as URLs, config update

 ===========================================================================
*/
