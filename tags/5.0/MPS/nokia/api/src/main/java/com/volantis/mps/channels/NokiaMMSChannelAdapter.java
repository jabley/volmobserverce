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
 * $Header: /src/mps/com/volantis/mps/channels/NokiaMMSChannelAdapter.java,v 1.13 2003/03/26 17:43:13 sumit Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 11-Nov-02    Steve           VBM:2002103008 - Nokia MMS Channel adapter
 * 13-Nov-02    ianw            VBM:2002111211 - Updated send routine to use
 *                              MessageRecipients.getIterator().
 * 14-Nov-02    Steve           VBM:2002103008 - Add MMS headers to sender.
 * 20-Nov-02	Sumit		    VBM:2002111815 - Added try/catch blocks
 * 20-Nov-02    Sumit           VBM:2002111501 - Logs information about
 *                              the MMS version supported in the constructor
 * 26-Nov-02    Mat             VBM:2002112007 - Amended constructor to take
 *                              a Map of config values, rather than read them
 *                              from the config file.
 * 28-Nov-02    Sumit           VBM:2002112602 - generateTar...MimeMultipart()
 *                              now takes channel name in send()
 * 29-Nov-02    Sumit           VBM:2002112602 - Removed above change and impl
 *                              method sendImpl
 * 09-Dec-02    Chris W         VBM:2002120913 - Tidied up addHeaders to remove
 *                              cut and paste code. createMessage now sets the
 *                              correct headers. Helper methods added to
 *                              determine correct headers for the smil part.
 * 27-Jan-03    Mat             VBM:2003011704 - Changed getMMContent() to use
 *                              the BodyPart DataHandler to get the content of
 *                              the file, rather than guessing.
 * 27-Jan-03    Mat             VBM:2003011704 - Removed some extraneous debug
 *                              statements.
 * 19-Mar-03    Geoff           VBM:2003032001 - Make initialisation property
 *                              values into public constants; these are useful
 *                              for unit tests but should be there anyway,
 *                              commented out some code that IDEA indicated
 *                              was not used.
 * 26-Mar-03    Sumit           VBM:2003032602 - Wrapped logger.debug
 *                              statements in if(logger.isDebugEnabled()) block
 * ----------------------------------------------------------------------------
 */

package com.volantis.mps.channels;


import com.nokia.mobile.services.api.DriverException;
import com.nokia.mobile.services.api.mms.ContentPart;
import com.nokia.mobile.services.api.mms.MMSAddress;
import com.nokia.mobile.services.api.mms.MMSDriverFactory;
import com.nokia.mobile.services.api.mms.MMSEngine;
import com.nokia.mobile.services.api.mms.MMSMessage;
import com.nokia.mobile.services.api.mms.MimePart;
import com.nokia.mobile.services.api.mms.MultipartMimeContent;
import com.nokia.mobile.services.driver.mms.ReleaseInfo;

import com.volantis.mcs.utilities.StringHash;
import com.volantis.mps.localization.LocalizationFactory;
import com.volantis.mps.message.MessageException;
import com.volantis.mps.message.MessageInternals;
import com.volantis.mps.message.MultiChannelMessage;
import com.volantis.mps.recipient.MessageRecipient;
import com.volantis.mps.recipient.MessageRecipients;
import com.volantis.mps.recipient.RecipientException;
import com.volantis.mps.recipient.RecipientInternals;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.localization.MessageLocalizer;
import com.volantis.synergetics.log.LogDispatcher;

import javax.activation.DataHandler;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * A channel adaptor to enable MMS messages to be sent via Nokia's MMS API.
 * <p/>
 *
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 */
public class NokiaMMSChannelAdapter extends PhoneGatewayChannelAdapter {

    // prevent the NMSS driver from logging
    static {
        System.setProperty("nmslib.isLogEnabled", "false");
    }

    /**
     * The logger to use
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(NokiaMMSChannelAdapter.class);

    /**
     * The exception localizer instance for this class.
     */
    private static final ExceptionLocalizer localizer =
            LocalizationFactory.createExceptionLocalizer(
                    NokiaMMSChannelAdapter.class);

    /**
     * Used to retrieve localized messages.
     */
    private static final MessageLocalizer messageLocalizer =
            LocalizationFactory.createMessageLocalizer(
                    NokiaMMSChannelAdapter.class);

    /**
     * Driver factory used to create MMSEngine instances
     */
    private final MMSDriverFactory mmsDriverFactory;

    /**
     * Creates a new instance of MMSChannelAdapter
     * @param channelName       identifies the channel
     * @param channelInfo       contains the channel configuration information
     * @throws MessageException if there was a problem creating the channel
     */
    public NokiaMMSChannelAdapter(String channelName, Map channelInfo)
            throws MessageException {
        super(channelName, channelInfo);

        // get driver factory props and create new factory
        Properties factoryProps = getFactoryProperties(channelInfo);
        try {
            mmsDriverFactory = MMSDriverFactory.getInstance(factoryProps);
        } catch (DriverException de) {
            throw new MessageException(
                    localizer.format("factory-creation-exception"), de);
        }

        try {
            // test engine creation and throw exception if we can't create a
            // new engine
            mmsDriverFactory.createEngine();

            // This is a bit nasty as we are making use of not exactly public
            // aspects of the driver JAR here in order to avoid having
            // the ReleaseInfo class start up a Swing session.
            Properties props = new Properties();
            props.load(ReleaseInfo.class.getResourceAsStream(
                    ReleaseInfo.RELEASE_INFO_FILE_NAME));

            logger.info("nokia-mms-version-is",
                        props.getProperty(ReleaseInfo.
                                          RELEASE_VERSION_PROPERTY));
        } catch (DriverException de) {
            throw new MessageException(
                    localizer.format("engine-creation-exception"), de);
        } catch (IOException e) {
            throw new MessageException(
                    localizer.format("unexpected-ioexception"),
                    e);
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

        // create engine and initial message
        MMSEngine senderEngine;
        MMSMessage message;
        synchronized(mmsDriverFactory) {
            try {
                senderEngine = mmsDriverFactory.createEngine();

                message = createMessage(mmsDriverFactory, multiChannelMessage,
                        messageSender);
            } catch (DriverException de) {
                throw new MessageException("engine-creation-exception", de);
            } catch (MessagingException me) {
                throw new MessageException("message-creation-exception", me);
            }
        }
        MessageRecipient recipient = null;

        while (recipientsIterator.hasNext()) {
            recipient = (MessageRecipient) recipientsIterator.next();


            // Get the address of the recipient
            String address = getRecipientAddress(recipient);

            if (address == null) {
                // Address not found - error log will contain toString()
                // value as no point trying to embed address!
                logger.warn("address-resolution-failed-for",
                            recipient.toString());
                recipient.setFailureReason(messageLocalizer.format(
                        "address-resolution-failed-for",
                        recipient.toString()));
                failures.addRecipient(recipient);
                continue;
            }

            MMSAddress mmsAddress = null;
            try {
                mmsAddress = new MMSAddress(address);
            } catch (DriverException de) {
                logger.warn("mmsaddress-creation-failed", address, de);
                recipient.setFailureReason(messageLocalizer.format(
                        "mmsaddress-creation-failed",
                        address));
                failures.addRecipient(recipient);
                continue;
            }

            // Use the type of recipient (to, cc, or bcc) to add the
            // address to the correct part of the message.
            switch (RecipientInternals.getRecipientType(recipient)) {
            case RecipientInternals.TO_RECIPIENT:
                message.addTO(mmsAddress);
                multipleRecipients.add(recipient);
                break;

            case RecipientInternals.CC_RECIPIENT:
                message.addCC(mmsAddress);
                multipleRecipients.add(recipient);
                break;

            case RecipientInternals.BCC_RECIPIENT:
                message.addBCC(mmsAddress);
                multipleRecipients.add(recipient);
                break;

            default:
                break;
            }

        }

        // get the device name fdr these message recipients and generate
        // device specific content
        String currentDeviceName = recipient.getDeviceName();
        try {
            setContent(message, multiChannelMessage, currentDeviceName);
        } catch (MessagingException me) {
            throw new MessageException(me);
        }

        // If there is a non null message, try and send it
        if (message != null) {
            addHeaders(message, multiChannelMessage);
            try {
                sendMessage(senderEngine, message);
            } catch (DriverException de) {
                for (Iterator i = multipleRecipients.iterator();
                     i.hasNext();) {
                    MessageRecipient failedRecipient = (MessageRecipient) i.next();
                    logger.warn("message-send-failed-to",
                                getRecipientAddress(failedRecipient));
                    failedRecipient.setFailureReason(de.getMessage());
                    failures.addRecipient(failedRecipient);
                }
                logger.warn("message-send-failed", de);
            }
        }

        // Return any failures
        return failures;
    }

    /**
     * A utility method that extract the address from a given message recipient
     * object.
     *
     * @param recipient The message recipient to extract the address from
     * @return The address if one could be found, null otherwise.
     * @throws RecipientException If there are any problems extracting values
     *                            from the message recipient.
     */
    private String getRecipientAddress(MessageRecipient recipient)
            throws RecipientException {
        // Grab the address from the phone number initiallly
        String address = formatMSISDN(recipient.getMSISDN(), true, false);

        // If the address is not found, use indirection to get it
        if (address == null) {
            address = formatEmail(recipient.getAddress().getAddress());
        }

        // Return the address, which may be null if no address was found
        return address;
    }

    /**
     * Send a message over the specified <CODE>MMSEngine</CODE>
     *
     * @param sender  <CODE>MMSEngine</CODE> used to send message
     * @param message <CODE>MMSMessage</CODE> to send
     * @return The ID of the sent message
     * @throws DriverException Thrown if message was unable to be sent
     */
    private synchronized String sendMessage(MMSEngine sender,
                                            MMSMessage message)
            throws DriverException {

        sender.connect();
        String msgID = sender.send(message);

        // @TODO: sender disconnect throws NullPointerException for unknown
        // reason. Possible problem with Nokia MMSC library
        //sender.disconnect();

        return msgID;
    }

    /**
     * Add headers to the specified message
     *
     * @param message             The <CODE>MMSMessage</CODE> to be sent
     * @param multiChannelMessage The <CODE>MultiChannelMessage</CODE>
     *                            containing the headers
     * @throws MessageException Errors during message related operations
     */
    private void addHeaders(MMSMessage message,
                            MultiChannelMessage multiChannelMessage)
            throws MessageException {

        // add common headers to the message
        addProtocolSpecificHeaders(message,
                                   multiChannelMessage,
                                   MessageInternals.ALL);

        // add MMS headers to the message
        multiChannelMessage.addHeader(MultiChannelMessage.MMS,
                                      "X-NOKIA-MMSC-Charging",
                                      "100");
        addProtocolSpecificHeaders(message,
                                   multiChannelMessage,
                                   MessageInternals.MMS);
    }

    /**
     * Add protocol specific headers to the message
     *
     * @param message             The <CODE>MMSMessage</CODE> to be sent
     * @param multiChannelMessage The <CODE>MultiChannelMessage</CODE>
     *                            containing the headers
     * @param type                The type of message e.g. ALL, MHTML, MMS
     * @throws MessageException Errors during message related operations
     */
    private void addProtocolSpecificHeaders(
            MMSMessage message,
            MultiChannelMessage multiChannelMessage,
            int type)
            throws MessageException {

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

    /**
     * Create a MMSC format multi-part mime message
     *
     * @param mmsDriverFactory    Factory used to create <CODE>MMSMessage</CODE>
     *                            objects
     * @param multiChannelMessage Message to be sent over this channel
     * @param messageSender       The originator of the message
     * @return Targeted <CODE>MMSMessage</CODE> for this device
     * @throws MessageException   Errors during message related operations
     * @throws MessagingException <CODE>javax.mail</CODE> related exceptions
     */
    private MMSMessage createMessage(MMSDriverFactory mmsDriverFactory,
                                     MultiChannelMessage multiChannelMessage,
                                     MessageRecipient messageSender)
            throws MessageException, MessagingException {

        MMSMessage mmsMessage = null;
        try {
            mmsMessage = mmsDriverFactory.createMMSMessage();
        } catch (DriverException de) {
            throw new MessageException(localizer.format(
                    "message-creation-exception"), de);
        }

        long millis = System.currentTimeMillis();

        // set message options
        mmsMessage.setSentDate( new Date( millis ) );
        mmsMessage.setReadReportFlag( false );
        mmsMessage.setDeliveryReportFlag( false );
        mmsMessage.setMessageClass( MMSConstants.MESSAGE_CLASS_PERSONAL );
        mmsMessage.setPriority( MMSConstants.MESSAGE_PRIORITY_NORMAL );

        if(multiChannelMessage.getCharacterEncoding() != null) {
            mmsMessage.setSubject( multiChannelMessage.getSubject(), 
                                   multiChannelMessage.getCharacterEncoding() );
        }
        else {
            mmsMessage.setSubject( multiChannelMessage.getSubject() );
        }
        
        mmsMessage.setContentType( MimePart.CT_MULTIPART_RELATED );

        if (messageSender != null) {
            InternetAddress address = null;
            try {
                // Attempt to get the email address of the sender.
                address = messageSender.getAddress();
                if (address == null) {
                    // Lets see if the sender has an MSISDN.
                    address = new InternetAddress(messageSender.getMSISDN());
                }
                
                mmsMessage.setFROM(new MMSAddress(address.getAddress()));
            } catch (RecipientException re) {
                throw new MessageException(
                        localizer.format("sender-address-retrieval-failure",
                                         re));
            } catch (DriverException de) {
                throw new MessageException(
                        localizer.format("mmsaddress-creation-failed",
                                         address),
                        de);
            }

        }

        return mmsMessage;
    }

    /**
     * Set the content for the specified MMSMessage as derived from device name
     * and multiChannelMessage arguments
     *
     * @param mmsMessage          The message to set content in
     * @param multiChannelMessage The multiChannelMessage from which to derive
     *                            content
     * @param deviceName          The device name for content generation
     * @return The same MMSMessage passed in but with added content
     * @throws MessagingException
     * @throws MessageException
     */
    private MMSMessage setContent(MMSMessage mmsMessage,
                                  MultiChannelMessage multiChannelMessage,
                                  String deviceName)
            throws MessagingException, MessageException {
        MimeMultipart multiPart =
                multiChannelMessage.generateTargetMessageAsMimeMultipart(
                        deviceName);
        MultipartMimeContent messageMultipartMimeContent =
                mmsMessage.getMultipart();

        int parts = multiPart.getCount();
        for (int p = 0; p < parts; p++) {
            try {
                MimeBodyPart part = (MimeBodyPart) multiPart.getBodyPart(p);

                ContentPart contentPart = getContentPart(part);

                String contentId = getContentId(part);
                contentPart.setContentID(contentId);
                if (isSmil(contentPart)) {
                    mmsMessage.setPresentationId(contentId);
                }
                messageMultipartMimeContent.addPart(contentPart);
            } catch (IOException ioe) {
                throw new MessageException(localizer.format(
                        "messaging-exception"),
                                           ioe);
            }
        }

        return mmsMessage;
    }

    /**
     * Returns the content id of a MimeBodyPart or makes one up if it's the
     * smil part.
     *
     * @param part The MimeBodyPart
     * @return String
     */
    private String getContentId(MimeBodyPart part) throws IOException,
            MessagingException {
        String contentId = part.getContentID();
        if (contentId == null) {
            // If the content id is null, get a String representation
            // of the object and return its digest as a string of hex digits.
            return StringHash.getDigestAsHex(part.getContent().toString());
        }
        return contentId;
    }

    /**
     * Returns true if the content passed in is smil
     */
    private boolean isSmil(ContentPart contentPart) {
        return MMSConstants.CT_APPLICATION_SMIL.equals(
                contentPart.getContentType());
    }

    /**
     * Read in the content part specified by the <CODE>MimeBodyPart</CODE> and
     * create a new <CODE>ContentPart</CODE>
     *
     * @param part Content part
     * @return A new <CODE>ContentPart</CODE>
     * @throws IOException        Errors reading in content
     * @throws MessagingException Errors reading content
     */
    private ContentPart getContentPart(MimeBodyPart part)
            throws IOException, MessagingException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        InputStream in = part.getInputStream();
        String contentType = null;

        if (in == null) {
            throw new IOException("Part has no input stream");
        }

        DataHandler dh = part.getDataHandler();
        if (dh != null) {
            contentType = dh.getContentType();
        } else {
            contentType = MMSConstants.CT_TEXT_PLAIN;
        }

        int c;
        while ((c = in.read()) != -1) {
            out.write(c);
        }
        byte[] contentBytes = out.toByteArray();
        ContentPart contentPart = new ContentPart();
        contentPart.setContent(contentBytes, contentType);

        return contentPart;

    }

    /**
     * Format a MMSC Email address
     */
    private String formatEmail(String addr) {
        if (addr == null) {
            return null;
        }
        StringBuffer buff = new StringBuffer(addr);
        if (addr.endsWith("=EMAIL") == false) {
            buff.append("/TYPE=EMAIL");
        }
        return buff.toString();
    }

    /**
     * Utility method to construct the Nokia MMS engine factory properties.
     *
     * @param channelInfo Configuration properties for the channel
     * @return a <CODE>Properties</CODE> object containing entries to configure
     *         the MMS engine factory.
     */
    private Properties getFactoryProperties(Map channelInfo) {
        Properties fprops = new Properties();

        fprops.setProperty("mmsc.Factory.Implementaion",
                           "com.nokia.mobile.services.driver.mmsc.eaif.MMSDriverFactoryImpl");
        fprops.setProperty("mmsc.Engine.MMSCURL",
                           (String) channelInfo.get("url"));
        fprops.setProperty("mmsc.Engine.max.concurrent.requests", "-1");
        fprops.setProperty("mmsc.Engine.mode", "originating");
        fprops.setProperty("mmsc.Message.Header.X-Mms-MMS-Version", "1.0");

        return fprops;
    }

    /**
     * Interface defining common mime type constants for MMS messages
     */
    public static interface MMSConstants {

        /**
         * Mime type: <CODE>text/html</CODE>
         */
        public static final String CT_TEXT_HTML = "text/html";

        /**
         * Mime type: <CODE>text/plain</CODE>
         */
        public static final String CT_TEXT_PLAIN = "text/plain";

        /**
         * Mime type: <CODE>text/vnd.wap.wml</CODE>
         */
        public static final String CT_TEXT_WML = "text/vnd.wap.wml";

        /**
         * Mime type: <CODE>image/gif</CODE>
         */
        public static final String CT_IMAGE_GIF = "image/gif";

        /**
         * Mime type: <CODE>image/jpeg</CODE>
         */
        public static final String CT_IMAGE_JPEG = "image/jpeg";

        /**
         * Mime type: <CODE>image/tiff</CODE>
         */
        public static final String CT_IMAGE_TIFF = "image/tiff";

        /**
         * Mime type: <CODE>image/png</CODE>
         */
        public static final String CT_IMAGE_PNG = "image/png";

        /**
         * Mime type: <CODE>image/vnd.wap.wbmp</CODE>
         */
        public static final String CT_IMAGE_WBMP = "image/vnd.wap.wbmp";

        /**
         * Mime type: <CODE>application/vnd.wap.multipart.mixed</CODE>
         */
        public static final String CT_APPLICATION_MULTIPART_MIXED =
                "application/vnd.wap.multipart.mixed";

        /**
         * Mime type: <CODE>application/vnd.wap.multipart.related</CODE>
         */
        public static final String CT_APPLICATION_MULTIPART_RELATED =
                "application/vnd.wap.multipart.related";

        /**
         * Mime type: <CODE>application/smil</CODE>
         */
        public static final String CT_APPLICATION_SMIL = "application/smil";

        /**
         * Personal MMS message class
         */
        public static final String MESSAGE_CLASS_PERSONAL = "Personal";

        /**
         * Informational MMS message class
         */
        public static final String MESSAGE_CLASS_INFORMATIONAL = "Informational";

        /**
         * Advertisement MMS message class
         */
        public static final String MESSAGE_CLASS_ADVERTISEMENT = "Advertisement";

        /**
         * Auto MMS message class
         */
        public static final String MESSAGE_CLASS_AUTO = "Auto";

        /**
         * Low priority MMS message
         */
        public static final String MESSAGE_PRIORITY_LOW = "Low";

        /**
         * Normal priority MMS message
         */
        public static final String MESSAGE_PRIORITY_NORMAL = "Normal";

        /**
         * High priority MMS message
         */
        public static final String MESSAGE_PRIORITY_HIGH = "High";
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 07-Jul-05	829/1	amoore	VBM:2005052302 Fixed encoding problems that caused DB characters to be represented by '?'

 06-May-05	704/2	philws	VBM:2005042903 Fix merge conflicts

 04-May-05	651/4	amoore	VBM:2005042903 Made API changes to support message encoded in double byte languages

 04-May-05	651/2	amoore	VBM:2005042903 Made API changes to support message encoded in double byte languages

 05-May-05	671/3	amoore	VBM:2005050315 Updated attachment file check logic and maintained coding standards

 05-May-05	671/1	amoore	VBM:2005050315 Added file check for message attachments to ensure they are valid

 04-May-05	660/1	philws	VBM:2005050311 Add failureReason property API to MessageRecipient, set failureReasons in channel adapters and show example usage of failureReason

 29-Apr-05	624/3	amoore	VBM:2005042509 Refactored MessageChannel to include redundant recipient processing in subclasses

 27-Apr-05	624/1	amoore	VBM:2005042509 Moved device specific recipient processing into MessageChannel class

 25-Apr-05	550/5	amoore	VBM:2005032908 Updated MMS channel to use new Nokia libraries

 15-Apr-05	550/1	amoore	VBM:2005032908 Code style changes

 15-Apr-05	548/3	amoore	VBM:2005032908 Removing coding standard violations

 15-Apr-05	548/1	amoore	VBM:2005032908 Changed sendImpl(...) to ensure all recipients are added to TO field rather than BCC or CC

 03-Mar-05	380/1	emma	VBM:2005021708 mergevbm from MPS 3.3

 03-Mar-05	376/1	emma	VBM:2005021708 Workaround for Nokia channel bug

 29-Nov-04	243/3	geoff	VBM:2004112302 Provide new Logging Infrastructure: MPS

 17-Nov-04	238/1	pcameron	VBM:2004111608 PublicAPI doc fixes and additions

 19-Oct-04	198/1	matthew	VBM:2004101311 allow mss logging to work (stop MCS from hijacking it)

 11-Aug-04	149/3	claire	VBM:2004073005 WAP Push for MPS: New channel adapter, generating messages as URLs, config update

 14-Jul-04	136/4	claire	VBM:2004070301 Implementing failed recipients management so code adheres to API and JavaDoc

 09-Jul-04	129/1	claire	VBM:2004070708 Ensure MMS messages are sent to all specified recipients

 ===========================================================================
*/
