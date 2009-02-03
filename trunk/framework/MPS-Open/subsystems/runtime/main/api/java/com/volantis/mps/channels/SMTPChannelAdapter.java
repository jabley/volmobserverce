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
 * $Header: /src/mps/com/volantis/mps/channels/SMTPChannelAdapter.java,v 1.13 2003/03/26 17:43:13 sumit Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 13-Nov-02    ianw            VBM:2002092306 - Created
 * 20-Nov-02	Sumit		 	VBM:2002111815 - Added try/catch blocks
 * 20-Nov-02    Sumit           VBM:2002111501 - Logs information about
 *                              the SMTP provider being used 
 * 26-Nov-02    Mat             VBM:2002112007 - Amended constructor to take
 *                              a Map of config values, rather than read them 
 *                              from the config file.
 * 28-Nov-02    Chris W         VBM:2002112704 - addHeaders add the MMS headers
 *                              as we can send MMS message via SMTP
 * 28-Nov-02    Sumit           VBM:2002112602 - generateTar...MimeMultipart()
 *                              now takes channel name in send
 * 29-Nov-02    Sumit           VBM:2002112602 - Removed above change and impl
 *                              method sendImpl
 * 29-Nov-02    Chris W         VBM:2002112704 - Code to add the MMS headers
 *                              moved from MultiChannelMessage to this class.
 * 17-Jan-03    Chris W         VBM:2002111501 - Writes information on the
 *                              version of javax.mail and javax.activation to
 *                              the info log.
 * 19-Mar-03    Geoff           VBM:2003032001 - Make initialisation property
 *                              values into public constants; these are useful
 *                              for unit tests but should be there anyway.
 * 26-Mar-03    Sumit           VBM:2003032602 - Wrapped logger.debug 
 *                              statements in if(logger.isDebugEnabled()) block
 * ----------------------------------------------------------------------------
 */
package com.volantis.mps.channels;

import com.volantis.mps.message.MessageException;
import com.volantis.mps.message.MessageInternals;
import com.volantis.mps.message.MultiChannelMessage;
import com.volantis.mps.recipient.MessageRecipient;
import com.volantis.mps.recipient.MessageRecipients;
import com.volantis.mps.recipient.RecipientException;
import com.volantis.mps.recipient.RecipientInternals;
import com.volantis.mps.recipient.ResolverUtilities;
import com.volantis.mps.localization.LocalizationFactory;
import com.volantis.synergetics.log.LogDispatcher;

import javax.activation.MimeType;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Provider;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * This is the SMTP channel adapter. It supports a basic SMTP relay.
 *
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 */
public class SMTPChannelAdapter extends MessageChannel {

    /**
     * The logger to use
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(SMTPChannelAdapter.class);

    /**
     * Name of the initialisation property used to store the host name.
     */
    public static final String HOST_NAME = "host";

    /**
     * Name of the initialisation property used to store the flag which
     * controls server authentication.
     */
    public static final String REQUIRES_AUTH = "auth";

    /**
     * Name of the initialisation property used to store the user name.
     */
    public static final String USER_NAME = "user";

    /**
     * Name of the initialisation property used to store the password.
     */
    public static final String PASSWORD = "password";

    /**
     * Contains the properties and defaults used by the mail APIs and provides
     * access to the protocol providers.
     */
    private final Session session;

    /**
     * Creates a new instance of SMTPChannelAdapter
     *
     * @param channelName       identifies the channel
     * @param channelInfo       contains the channel configuration information
     * @throws MessageException if there was a problem creating the channel
     */
    public SMTPChannelAdapter(String channelName, Map channelInfo)
            throws MessageException {
        try {
            this.channelName = channelName;

            String host = (String) channelInfo.get(HOST_NAME);
            boolean auth = ((String) channelInfo.get(REQUIRES_AUTH)).equalsIgnoreCase(
                    "true");
            String user = (String) channelInfo.get(USER_NAME);
            String password = (String) channelInfo.get(PASSWORD);
            // copy the system properties so we don't affect other instances of
            // SMTPChannelAdapter
            Properties props = new Properties();
            props.putAll(System.getProperties());

            if (logger.isDebugEnabled()) {
                logger.debug("MPS SMTP Host: " + host);
                logger.debug("MPS SMTP Auth: " + auth);
                logger.debug("MPS SMTP User: " + user);
            }

            props.put("mail.smtp.host", host);
            props.put("mail.smtp.auth", Boolean.toString(auth));

            Package javaxMail = Package.getPackage("javax.mail");
            logger.info("package-implementation-title", new Object[]{
                javaxMail.getName(), javaxMail.getImplementationTitle()});
            logger.info("package-implementation-vendor", new Object[]{
                javaxMail.getName(), javaxMail.getImplementationVendor()});
            logger.info("package-implementation-version", new Object[]{
                javaxMail.getName(), javaxMail.getImplementationVersion()});
            logger.info("package-specification-title", new Object[]{
                javaxMail.getName(), javaxMail.getSpecificationTitle()});
            logger.info("package-specification-vendor", new Object[]{
                javaxMail.getName(), javaxMail.getSpecificationVendor()});
            logger.info("package-specification-version", new Object[]{
                javaxMail.getName(), javaxMail.getSpecificationVersion()});

            // The next line MimeType mimeType = new MimeType();
            // must not be removed! If it is, the class loader will not be
            // able to find the javax.activation package thus making
            // javaxActivation object null and causing NullPointerExceptions.
            MimeType mimeType = new MimeType();
            Package javaxActivation = Package.getPackage("javax.activation");
            logger.info("package-implementation-title", new Object[]{
                javaxActivation.getName(),
                javaxActivation.getImplementationTitle()});
            logger.info("package-implementation-vendor", new Object[]{
                javaxActivation.getName(),
                javaxActivation.getImplementationVendor()});
            logger.info("package-implementation-version", new Object[]{
                javaxActivation.getName(),
                javaxActivation.getImplementationVersion()});
            logger.info("package-specification-title", new Object[]{
                javaxActivation.getName(),
                javaxActivation.getSpecificationTitle()});
            logger.info("package-specification-vendor", new Object[]{
                javaxActivation.getName(),
                javaxActivation.getSpecificationVendor()});
            logger.info("package-specification-version", new Object[]{
                javaxActivation.getName(),
                javaxActivation.getSpecificationVersion()});

            if (auth) {
                DefaultAuthenticator authenticator = new DefaultAuthenticator();
                if (user != null) {
                    props.put("mail.smtp.user", user);
                }
                authenticator.setUser(user);
                authenticator.setPassword(password);
                session = Session.getDefaultInstance(props, authenticator);
            } else {
                session = Session.getDefaultInstance(props);
            }
            Provider providers[] = session.getProviders();
            for (int loop = 0; loop < providers.length; loop++) {
                final Provider provider = providers[loop];
                logger.info("provider-using-for", new Object[]{
                    provider.getProtocol(), provider.getClassName(),
                    provider.getVersion(), provider.getVendor()});
            }
        } catch (Exception e) {
            throw new MessageException(e);
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

        MimeMessage message;
  
        try {
            message = new MimeMessage(session);
  
            if (multiChannelMessage.getCharacterEncoding() != null) {
                message.setSubject(
                        multiChannelMessage.getSubject(),
                        multiChannelMessage.getCharacterEncoding());
            } else {
                message.setSubject( multiChannelMessage.getSubject() );
            }

            if (messageSender != null) {
                message.setFrom( messageSender.getAddress() );
            }

        } catch (Exception e) {
            // Failure to create the message hence no failure logged here
            throw new MessageException(e);
        }

        Iterator recipientsIterator = messageRecipients.getIterator();
        MessageRecipient recipient = null;

        while (recipientsIterator.hasNext()) {
            recipient = (MessageRecipient) recipientsIterator.next();
            int recipientType = RecipientInternals.getRecipientType(recipient);
            try {
                switch (recipientType) {
                case RecipientInternals.TO_RECIPIENT:
                    {
                        message.addRecipient(Message.RecipientType.TO,
                                             recipient.getAddress());
                        multipleRecipients.add(recipient);
                        break;
                    }
                case RecipientInternals.CC_RECIPIENT:
                    {
                        message.addRecipient(Message.RecipientType.CC,
                                             recipient.getAddress());
                        multipleRecipients.add(recipient);
                        break;
                    }
                case RecipientInternals.BCC_RECIPIENT:
                    {
                        message.addRecipient(Message.RecipientType.BCC,
                                             recipient.getAddress());
                        multipleRecipients.add(recipient);
                        break;
                    }
                }
            } catch (MessagingException me) {
                // This exception can come from addRecipient so the
                // recipient needs to be added to the failures here
                // and it will not already have been added to the
                // multipleRecipients list so no need to remove it.
                logger.warn("message-recipient-addition-failed-for",
                            recipient.getAddress(), me);
                recipient.setFailureReason(me.getMessage());
                failures.addRecipient(recipient);
            }
        }

        // obtain device name for message header generation
        // from last recipient added
        String currentDeviceName = recipient.getDeviceName();


        // Try and send the messsage
        try {
            message = (MimeMessage) addHeaders(multiChannelMessage,
                                               message,
                                               currentDeviceName);
            MimeMultipart content =
                    multiChannelMessage.generateTargetMessageAsMimeMultipart(
                            currentDeviceName);
            message.setContent(content, content.getContentType());
            Transport.send(message);
        } catch (MessagingException me) {
            for (Iterator i = multipleRecipients.iterator(); i.hasNext();) {
                MessageRecipient failedRecipient = (MessageRecipient) i.next();
                logger.warn("message-send-failed-to",
                            failedRecipient.getAddress());
                logger.error(me);
                failedRecipient.setFailureReason(me.getMessage());
                failures.addRecipient( failedRecipient );
            }
        }

        // Return any failures
        return failures;
    }

    /**
     * Add Headers into the message.
     *
     * @param multiChannelMessage   The <CODE>MultiChannelMessage</CODE>
     *                              containing the headers.
     * @param message               The Mime Message to add the headers into.
     * @param deviceName            identifies the device for which the headers
     *                              are being generated
     * @return The Mime Message containing the headers.
     * @throws MessagingException if there was a problem adding the headers
     * @throws MessageException if there was a problem adding the headers
     */
    private Message addHeaders(MultiChannelMessage multiChannelMessage,
                               Message message, String deviceName)
            throws MessageException, MessagingException {

        // Add common headers to the message.
        addProtocolSpecificHeaders(multiChannelMessage, message,
                                   MessageInternals.ALL);

        String messageProtocol =
                ResolverUtilities.getDeviceMessageProtocol(deviceName);

        if ("MHTML".equals(messageProtocol)) {
            // Add MHTML headers
            addProtocolSpecificHeaders(multiChannelMessage, message,
                                       MessageInternals.MHTML);
        } else if ("MMS".equals(messageProtocol)) {
            // Add MMS headers
            multiChannelMessage.addHeader(MultiChannelMessage.MMS,
                                          "X-MMS-Message-Type",
                                          "m-retrieve-conf");
            multiChannelMessage.addHeader(MultiChannelMessage.MMS,
                                          "X-MMS-Transaction-ID", "1");
            multiChannelMessage.addHeader(MultiChannelMessage.MMS,
                                          "X-MMS-Mms-Version", "1.0");
            addProtocolSpecificHeaders(multiChannelMessage, message,
                                       MessageInternals.MMS);
        }

        return message;
    }

    /**
     * Add Protocol specific headers
     *
     * @param multiChannelMessage The MultiChannelMessage containing the
     *                            headers
     * @param message             The Mime Message to add the headers to
     * @param type                The type of message e.g. ALL, MHTML, MMS
     * @throws MessageException if there was a problem adding the headers
     * @throws MessagingException if there was a problem adding the headers
     */
    private void addProtocolSpecificHeaders(
            MultiChannelMessage multiChannelMessage,
            Message message,
            int type)
            throws MessageException, MessagingException {
        Map headers = MessageInternals.getHeaders(multiChannelMessage, type);
        Iterator headerIterator = headers.keySet().iterator();
        String headerName;
        String headerValue;

        while (headerIterator.hasNext()) {
            headerName = (String) headerIterator.next();
            headerValue = (String) headers.get(headerName);
            message.addHeader(headerName, headerValue);
        }
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 07-Jul-05	829/2	amoore	VBM:2005052302 Fixed encoding problems that caused DB characters to be represented by '?'

 01-Jul-05	776/3	amoore	VBM:2005052302 Fixed encoding problems that caused DB characters to be represented by '?'

 17-May-05	744/1	amoore	VBM:2005051206 Updated MPS to ensure correct encoding of messages when using DBCS

 06-May-05	704/2	philws	VBM:2005042903 Fix merge conflicts

 04-May-05	651/4	amoore	VBM:2005042903 Made API changes to support message encoded in double byte languages

 04-May-05	651/2	amoore	VBM:2005042903 Made API changes to support message encoded in double byte languages

 05-May-05	671/3	amoore	VBM:2005050315 Updated attachment file check logic and maintained coding standards

 05-May-05	671/1	amoore	VBM:2005050315 Added file check for message attachments to ensure they are valid

 04-May-05	660/1	philws	VBM:2005050311 Add failureReason property API to MessageRecipient, set failureReasons in channel adapters and show example usage of failureReason

 29-Apr-05	624/3	amoore	VBM:2005042509 Refactored MessageChannel to include redundant recipient processing in subclasses

 27-Apr-05	624/1	amoore	VBM:2005042509 Moved device specific recipient processing into MessageChannel class

 06-Apr-05	385/1	matthew	VBM:2004123104 allow multiple instances of SMTPChannelAdapter to be used at once

 29-Nov-04	243/3	geoff	VBM:2004112302 Provide new Logging Infrastructure: MPS

 17-Nov-04	238/1	pcameron	VBM:2004111608 PublicAPI doc fixes and additions

 19-Oct-04	198/1	matthew	VBM:2004101311 allow mss logging to work (stop MCS from hijacking it)

 11-Aug-04	149/1	claire	VBM:2004073005 WAP Push for MPS: New channel adapter, generating messages as URLs, config update

 14-Jul-04	136/4	claire	VBM:2004070301 Implementing failed recipients management so code adheres to API and JavaDoc

 ===========================================================================
*/
