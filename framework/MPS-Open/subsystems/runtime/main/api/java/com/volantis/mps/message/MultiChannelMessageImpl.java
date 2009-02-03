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
package com.volantis.mps.message;

import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.mps.localization.LocalizationFactory;
import com.volantis.mps.attachment.MessageAttachments;
import com.volantis.mps.attachment.AttachmentUtilities;
import com.volantis.mps.attachment.DeviceMessageAttachment;
import com.volantis.mps.channels.HTTPHelper;
import com.volantis.mps.servlet.MessageStoreServlet;
import com.volantis.mps.assembler.MessageRequestor;
import com.volantis.mps.assembler.MessageAssembler;
import com.volantis.mps.assembler.PlainTextMessageAssembler;
import com.volantis.mps.assembler.MimeMessageAssembler;
import com.volantis.charset.EncodingManager;
import com.volantis.mcs.runtime.Volantis;

import java.net.URL;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.io.IOException;

import javax.mail.internet.MimeMultipart;

import our.apache.commons.httpclient.methods.PostMethod;
import our.apache.commons.httpclient.Header;

/**
 * Implementation of {@link com.volantis.mps.message.MultiChannelMessage}.
 */
public class MultiChannelMessageImpl implements MultiChannelMessage {

    /**
     * The LOGGER to use
     */
    private static final LogDispatcher LOGGER =
            LocalizationFactory.createLogger(MultiChannelMessageImpl.class);

    /**
     * The exception LOCALIZER instance for this class.
     */
    private static final ExceptionLocalizer LOCALIZER =
            LocalizationFactory.createExceptionLocalizer(
                    MultiChannelMessageImpl.class);

    /**
     * The encoding manager used to determine valid charsets
     */
    private static final EncodingManager ENCODING_MANAGER =
            new EncodingManager();

    /**
     * The URL for the message.
     */
    protected URL messageURL;

    /**
     * The xml message.
     */
    protected String message;

    /**
     * The subject header for the message.
     */
    protected String subject;

    private Map rawMessageCache = new HashMap();

    private Map mimeMessageCache = new HashMap();

    private Map allHeaders = new HashMap();

    private Map mhtmlHeaders = new HashMap();

    private Map mmsHeaders = new HashMap();

    /**
     * The list of message attachments.
     */
    protected MessageAttachments messageAttachments;

    /**
     * The URL that can be used with the host deployment URL to reference
     * the servlet that handles the message as a URL.
     */
    protected static final String SERVLET_PARTIAL_URL = "mss";

    /**
     * The path separator to use when building URLs.  Just provided as a
     * constant as a convenience for the code in this class to avoid lots of
     * little <code>String</code>s being created everywhere!
     */
    protected final String URL_PATH_CHARACTER = "/";

    /**
     * The helper object used for making requests.
     */
    protected HTTPHelper httpHelper = HTTPHelper.getDefaultInstance();

    /**
     * The character encoding for this message
     */
    protected String characterEncoding = null;

    /**
     * Creates a new instance of <code>MultiChannelMessage</code>.
     */
    public MultiChannelMessageImpl() {
    }

    /**
     * Create a new instance of <code>MultiChannelMessage</code> with the specified
     * character encoding. Supported character sets can be found at:<br/>
     * <ul>
     * <li><a href="http://java.sun.com/j2se/1.4.2/docs/guide/intl/encoding.doc.html">
     *      http://java.sun.com/j2se/1.4.2/docs/guide/intl/encoding.doc.html</a></li>
     * <li><a href="http://java.sun.com/j2se/1.3/docs/guide/intl/encoding.doc.html">
     *      http://java.sun.com/j2se/1.3/docs/guide/intl/encoding.doc.html</a></li>
     * </ul>
     *
     * @param characterEncoding     The message character encoding
     *
     * @throws IllegalArgumentException If the character encoding is not valid.
     *
     */
      public MultiChannelMessageImpl(String characterEncoding) {
          // check character encoding is valid
          if (characterEncoding != null &&
                ENCODING_MANAGER.getEncoding(characterEncoding) == null) {
              String message = LOCALIZER.format("unsupported-encoding",
                                              characterEncoding);
              throw new IllegalArgumentException(message);
          }
          this.characterEncoding = characterEncoding;
      }

    /**
     * Creates a new instance of <code>MultiChannelMessage</code> with the
     * given message URL and subject.
     * <p>
     * @param messageURL    The message URL.
     * @param subject       The message subject.
     */
    public MultiChannelMessageImpl(URL messageURL, String subject) {
        this.messageURL = messageURL;
        this.subject = subject;
    }

    /**
     * Create a new instance of <code>MultiChannelMessage</code> with the
     * given message URL and subject using the specified character encoding.
     * Supported character sets can be found at:<br/>
     * <ul>
     * <li><a href="http://java.sun.com/j2se/1.4.2/docs/guide/intl/encoding.doc.html">
     *      http://java.sun.com/j2se/1.4.2/docs/guide/intl/encoding.doc.html</a></li>
     * <li><a href="http://java.sun.com/j2se/1.3/docs/guide/intl/encoding.doc.html">
     *      http://java.sun.com/j2se/1.3/docs/guide/intl/encoding.doc.html</a></li>
     * </ul>
     *
     * @param messageURL            The message URL
     * @param subject               The message subject
     * @param characterEncoding     The message character encoding
     */
    public MultiChannelMessageImpl(URL messageURL,
                               String subject,
                               String characterEncoding) {
        // check character encoding is valid
        if (characterEncoding != null &&
                ENCODING_MANAGER.getEncoding(characterEncoding) == null) {
              String message = LOCALIZER.format("unsupported-encoding",
                                               characterEncoding);
              throw new IllegalArgumentException(message);
        }
        this.messageURL = messageURL;
        this.subject = subject;
        this.characterEncoding = characterEncoding;
    }

    /**
     * Creates a new instance of <code>MultiChannelMessage</code> with the
     * given message content and subject.
     * <p>
     * @param messageContent the message content.
     * @param subject the message subject.
     */
    public MultiChannelMessageImpl(String messageContent, String subject) {
        this.message = messageContent;
        this.subject = subject;
    }

    /**
     * Creates a new instance of <code>MultiChannelMessage</code> with the
     * given message content and subject using the specified character encoding.
     * Supported character sets can be found at:<br/>
     * <ul>
     * <li><a href="http://java.sun.com/j2se/1.4.2/docs/guide/intl/encoding.doc.html">
     *      http://java.sun.com/j2se/1.4.2/docs/guide/intl/encoding.doc.html</a></li>
     * <li><a href="http://java.sun.com/j2se/1.3/docs/guide/intl/encoding.doc.html">
     *      http://java.sun.com/j2se/1.3/docs/guide/intl/encoding.doc.html</a></li>
     * </ul>
     *
     * @param messageContent        The message content
     * @param subject               The message subject
     * @param characterEncoding     The message character set
     */
    public MultiChannelMessageImpl(String messageContent,
                               String subject,
                               String characterEncoding) {
        // check character encoding is valid
        if (characterEncoding != null &&
                ENCODING_MANAGER.getEncoding(characterEncoding) == null) {
            String message = LOCALIZER.format("unsupported-encoding",
                                              characterEncoding);
            throw new IllegalArgumentException(message);
        }
        this.message = messageContent;
        this.subject = subject;
        this.characterEncoding = characterEncoding;
    }

    // Javadoc inherited.
    public void setCharacterEncoding(String characterEncoding) {
        // check character encoding is valid
        if (characterEncoding != null &&
                ENCODING_MANAGER.getEncoding(characterEncoding) == null) {
            String message = LOCALIZER.format("unsupported-encoding",
                                              characterEncoding);
            throw new IllegalArgumentException(message);
        }
        this.characterEncoding = characterEncoding;
    }

    // Javadoc inherited.
    public String getCharacterEncoding() {
        return characterEncoding;
    }

    // Javadoc inherited.
    public void setMessageURL(URL messageURL)
        throws MessageException {

        this.messageURL = messageURL;
        if (message==null) {
            rawMessageCache.clear();
        }
    }

    // Javadoc inherited.
    public URL getMessageURL()
        throws MessageException {

        return messageURL;
    }

    // Javadoc inherited.
    public void setMessage(String messageContent)
        throws MessageException {

        this.message = messageContent;
        rawMessageCache.clear();
    }

    // Javadoc inherited.
    public String getMessage()
        throws MessageException {

        return message;
    }

    // Javadoc inherited.
    public void setSubject(String subject)
        throws MessageException {

        this.subject = subject;
    }

    // Javadoc inherited.
    public String getSubject()
        throws MessageException {

        return subject;
    }

    // Javadoc inherited.
    public URL generateTargetMessageAsURL(String deviceName, String mssUrl)
            throws MessageException {
        if (deviceName == null) {
            throw new MessageException(
                    LOCALIZER.format("device-name-null-invalid"));
        }
        // Currently the device name is not used here...  On retrieval the
        // device name of the requesting device is contained in the headers
        // so this can be used to get an accurate page rendering.

        // See if the message is already in URL form
        URL returnURL = messageURL;

        // It isn't, so process the XML as a URL
        if (returnURL == null) {
            // This is an XML message so need to convert contents to a URL
            // and cache the XML in a message store

            // The id that ultimately will come from the response
            String id = null;

            String fullURL;

            StringBuffer hostBuffer = new StringBuffer();
            if (mssUrl == null) {
                Volantis bean = getVolantis();
                String internalURL = bean.getInternalURL().getExternalForm();
                String baseURL = bean.getPageBase();


                // Create the URL used to make the request and also to send
                // back with the id later
                hostBuffer.append(internalURL);
                if (!internalURL.endsWith(URL_PATH_CHARACTER)) {
                    hostBuffer.append(URL_PATH_CHARACTER);
                }
                hostBuffer.append(baseURL);
                if ((!baseURL.endsWith(URL_PATH_CHARACTER)) &&
                        (!SERVLET_PARTIAL_URL.startsWith(URL_PATH_CHARACTER))) {
                    hostBuffer.append(URL_PATH_CHARACTER);
                }
                hostBuffer.append(SERVLET_PARTIAL_URL);

                // Create the request method
                fullURL = hostBuffer.toString();
            } else {
                hostBuffer.append(mssUrl);
                fullURL = mssUrl;
            }

            PostMethod method = new PostMethod(fullURL);


            // Add the xml to the POST request
            method.setRequestBody(message);

            // set request headers
            if (characterEncoding != null) {
                method.addRequestHeader("Content-Type", "text/xml; charset=" +
                        characterEncoding);
                method.addRequestHeader("Accept-Charset",
                        characterEncoding);
            } else {
                method.addRequestHeader("Content-Type", "text/xml");
            }

            try {
                int statusCode = httpHelper.executeRequest(method,
                                                           fullURL);

                // If the status code is -1 then ran out of retries to
                // connect to the servlet so the xml cannot be processed
                if (statusCode == -1) {
                    final String messageKey =
                            "message-store-connection-failure-for-id-url";
                    LOGGER.error(messageKey);
                    throw new MessageException(
                            LOCALIZER.format(messageKey));
                }

                // Ensure there was a successful send
                if (statusCode != HttpURLConnection.HTTP_OK) {
                    final String messageKey =
                            "message-store-connection-failure-http-error-of";
                    final Object messageParam = new Integer(statusCode);
                    LOGGER.error(messageKey, messageParam);
                    throw new MessageException(
                            LOCALIZER.format(messageKey, messageParam));
                }

                // Read the response header
                Header idHeader = method.getResponseHeader(
                        MessageStoreServlet.MESSAGE_RESPONSE_HEADER_NAME);

                // Check for a null value which indicates that the header was
                // not set for some reason
                if (idHeader == null) {
                    final String messageKey =
                            "message-id-missing-url-construction-failed";
                    LOGGER.error(messageKey);
                    throw new MessageException(
                            LOCALIZER.format(messageKey));
                }

                id = idHeader.getValue();

                // And finally construct the URL to return, encoding the ? and
                // = as this is required by the NowSMS gateway BUT the whole
                // URL cannot be encoded as the / should not be encoded!
                // Rest of URL to return was created above
                hostBuffer.append("%3fpageid%3d");
                hostBuffer.append(id);
                returnURL = new URL(hostBuffer.toString());
            } catch (MalformedURLException mue) {
                final String messageKey = "message-url-invalid-for";
                final Object messageParam = SERVLET_PARTIAL_URL + "%3fid%3d" +
                        id;
                LOGGER.error(messageKey, messageParam);
                throw new MessageException(
                        LOCALIZER.format(messageKey, messageParam), mue);
            } catch (IOException ioe) {
                final String messageKey = "message-store-transport-error";
                LOGGER.error(messageKey);
                throw new MessageException(
                        LOCALIZER.format(messageKey), ioe);
            } finally {
                // Release the connection.
                method.releaseConnection();
            }
        }

        // Return the URL of the message
        return returnURL;
    }

    // Javadoc inherited.
    protected Volantis getVolantis() {
        return Volantis.getInstance();
    }

    // Javadoc inherited.
    public String generateTargetMessageAsString (String deviceName)
            throws MessageException {
        if (deviceName != null) {
            ProtocolIndependentMessage rawMessage =
                (ProtocolIndependentMessage)rawMessageCache.get(deviceName);

            if (rawMessage == null) {
                MessageRequestor messageRequestor =
                                            MessageRequestor.getInstance();
                rawMessage = messageRequestor.getChannelIndependentMessage(
                        deviceName, this);
                rawMessageCache.put(deviceName,rawMessage);
            }
            MessageAssembler messageAssembler = new PlainTextMessageAssembler();
            return (String)messageAssembler.assembleMessage(rawMessage,
                                                            messageAttachments);
        } else {
            throw new MessageException(
                    LOCALIZER.format("device-name-null-invalid"));
        }

    }

    // Javadoc inherited.
    public MimeMultipart generateTargetMessageAsMimeMultipart (String deviceName)
            throws MessageException {
        if (deviceName != null) {
            MimeMultipart mimeMultipart =
                    (MimeMultipart)mimeMessageCache.get(deviceName);

            if (mimeMultipart == null) {
                ProtocolIndependentMessage rawMessage =
                    (ProtocolIndependentMessage)rawMessageCache.get(deviceName);

                if (rawMessage == null) {
                    MessageRequestor messageRequestor =
                                            MessageRequestor.getInstance();
                    rawMessage = messageRequestor.getChannelIndependentMessage(
                        deviceName, this);
                    rawMessageCache.put(deviceName,rawMessage);
                }
                MessageAssembler messageAssembler =
                        new MimeMessageAssembler();

                MessageAttachments attachments =
                    AttachmentUtilities.getAttachmentsForDevice(deviceName,
                                                       messageAttachments);
                mimeMultipart =
                        (MimeMultipart)messageAssembler
                                .assembleMessage(rawMessage, attachments);
                mimeMessageCache.put(deviceName,mimeMultipart);
            }

            return mimeMultipart;
        } else {
            throw new MessageException(
                    LOCALIZER.format("device-name-null-invalid"));
        }

    }

    // Javadoc inherited.
    public void addHeader(int messageType, String name, String value)
        throws MessageException {

        Map headers;
        switch (messageType) {
            case ALL: {
                headers = allHeaders;
                break;
            }
            case MHTML: {
                headers = mhtmlHeaders;
                break;
            }
            case MMS: {
                headers = mmsHeaders;
                break;
            }
            default: {
                throw new MessageException(
                        LOCALIZER.format("message-type-invalid"));
            }
        }
        headers.put(name,value);
    }

    // Javadoc inherited.
    public void addAttachments(MessageAttachments messageAttachments)
        throws MessageException {
        this.messageAttachments = messageAttachments;
    }

    // Javadoc inherited.
    public void removeAttachments() throws MessageException {
        this.messageAttachments = null;
    }

    // Javadoc inherited.
    public Map getHeaders(int messageType)
        throws MessageException {
        switch (messageType) {
            case ALL: {
                return allHeaders;
            }
            case MHTML: {
                return mhtmlHeaders;
            }
            case MMS: {
                return mmsHeaders;
            }
            default: {
                throw new MessageException(
                        LOCALIZER.format("message-type-invalid"));
            }
        }
    }

    // Javadoc inherited.
    public MessageAttachments getAttachments() {
        return messageAttachments;
    }

    // Javadoc inherited.
    public Object clone() {
        MultiChannelMessageImpl clone = new MultiChannelMessageImpl();
        try {
            clone.setMessageURL(this.getMessageURL());
            clone.setMessage(this.getMessage());
            clone.setSubject(this.getSubject());
            clone.setCharacterEncoding(this.getCharacterEncoding());

            // copy across headers for all protocols
            Iterator i = allHeaders.keySet().iterator();
            while (i.hasNext()) {
                String key = (String)i.next();
                String value = (String)allHeaders.get(key);
                clone.addHeader(ALL, key, value);
            }

            // copy across headers for mms protocols
            i = mmsHeaders.keySet().iterator();
            while (i.hasNext()) {
                String key = (String)i.next();
                String value = (String)mmsHeaders.get(key);
                clone.addHeader(MMS, key, value);
            }

            // copy across headers for mhtml protocols
            i = mhtmlHeaders.keySet().iterator();
            while (i.hasNext()) {
                String key = (String)i.next();
                String value = (String)mhtmlHeaders.get(key);
                clone.addHeader(MHTML, key, value);
            }

            MessageAttachments clonedAttachments = new MessageAttachments();
            if( messageAttachments != null ) {
                i = messageAttachments.iterator();
                while (i.hasNext()) {
                    DeviceMessageAttachment clonedAttachment =
                        (DeviceMessageAttachment)i.next();

                    clonedAttachments.addAttachment(clonedAttachment);
                }
                clone.addAttachments(clonedAttachments);
            }
        } catch (Exception e) {
            LOGGER.error(LOCALIZER.format("message-clone-failed"), e);
            clone = null;
        }
        return clone;
    }
}
