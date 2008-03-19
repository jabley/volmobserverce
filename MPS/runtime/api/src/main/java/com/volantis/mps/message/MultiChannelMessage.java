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
 * $Header: /src/mps/com/volantis/mps/message/MultiChannelMessage.java,v 1.11 2003/01/28 16:55:37 sfound Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 12-Nov-02    ianw            VBM:2002111211 - Created
 * 22-Nov-02    Steve           Removed the global MessageRequestor so
 *                              generateTargetMessage methods get an instance
 *                              as required. As MessageRequestor is a singleton
 *                              there is no object overhead. This makes it
 *                              possible to unit test some of the other
 *                              classes by doing new MultiChannelMessage()
 *                              without needing to stoke up the bean.
 * 28-Nov-02    Chris W         VBM:2002112704  - Constructors call
 *                              setDefaultHeaders() to set the MMS headers.
 * 28-Nov-02    Sumit           VBM:2002112602 - generateTargetMessageAs...()
 *                              now takes channel name as param
 * 29-Nov-02    Chris W         VBM:2002112704 - moved setting of MMS headers
 *                              to SMTPChannelAdapter i.e. undo the previous
 *                              change I made to this class.
 * 09-Jan-02    ianw            VBM:2002111211 - Added attachments into clone
 *                              method.
 * 28-Jan-03    Steve           VBM:2003012704 - Check for null attachments before
 *                              trying to add them.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mps.message;

import com.volantis.mps.attachment.MessageAttachments;

import java.net.URL;
import java.util.Map;

import javax.mail.internet.MimeMultipart;

/**
 * The <code>MultiChannelMessage</code> class encapsulates the device independent
 * message from which targeted messages are created. Messages are created from a
 * Mariner message definition. This can be supplied either as a
 * {@link java.lang.String} or as a {@link java.net.URL}. Where a
 * {@link java.net.URL} is provided, the message is read before being processed.
 * If both a message and a message URL are set, the message takes precedence.
 *
 * @volantis-api-include-in PublicAPI
 * @mock.generate
 */
public interface MultiChannelMessage {

    /**
     * Indicates headers should be applied to all protocols.
     */
    final public static int ALL = 0;

    /**
     * Indicates headers should be applied to MHTML protocol only.
     */
    final public static int MHTML = 1;

    /**
     * Indicates headers should be applied to MMS.
     */
    final public static int MMS = 2;

    /**
     * Sets the character encoding for this message. Supported character sets
     * can be found at:<br/>
     * <ul>
     * <li><a href="http://java.sun.com/j2se/1.4.2/docs/guide/intl/encoding.doc.html">
     *      http://java.sun.com/j2se/1.4.2/docs/guide/intl/encoding.doc.html</a></li>
     * <li><a href="http://java.sun.com/j2se/1.3/docs/guide/intl/encoding.doc.html">
     *      http://java.sun.com/j2se/1.3/docs/guide/intl/encoding.doc.html</a></li>
     * </ul>
     *
     * @param characterEncoding The new character encoding
     */
    public void setCharacterEncoding(String characterEncoding);

    /**
     * Gets the current character encoding for this message
     *
     * @return  The current character encoding or <code>null</code> if not set
     */
    public String getCharacterEncoding();

    /**
     * Sets the URL of the message to be used to generate the targeted messages.
     * The URL must resolve to a file in the local filesystem of the server on
     * which MCS is running. The contents of the file must be either valid MCS
     * XML markup or be a valid JSP containing valid MCS markup.
     * <p>
     * @param messageURL the message URL.
     * @throws MessageException if there was a problem setting the URL.
     */
    public void setMessageURL(URL messageURL) throws MessageException;

    /**
     * Gets the message URL.
     * <p>
     * @return the message URL.
     * @throws MessageException if there was a problem retrieving the URL.
     */
    public URL getMessageURL() throws MessageException;

    /**
     * Sets the message to be used to generate the targeted messages. The
     * message must be written in valid MCS XML markup.
     * <p>
     * @param messageContent the new message content.
     * @throws MessageException if there was a problem setting the content.
     */
    public void setMessage(String messageContent) throws MessageException;

    /**
     * Gets the message content.
     * <p>
     * @return the message content.
     * @throws MessageException if there was a problem retrieving the content.
     */
    public String getMessage() throws MessageException;

    /**
     * Sets the message subject.
     * <p>
     * @param subject the new message subject.
     * @throws MessageException if there was a problem setting the subject.
     */
    public void setSubject(String subject) throws MessageException;

    /**
     * Gets the message subject.
     * <p>
     * @return the message subject.
     * @throws MessageException if there was a problem retrieving the subject.
     */
    public String getSubject() throws MessageException;

    /**
     * Generates this message as a URL. Attachments are ignored. If the
     * message consists of XML this is stored and a URL reference to the
     * stored version is returned.
     * <p>
     * @param deviceName The device name to generate this message for.
     * @param mssUrl The URL to the MSS servlet. If null then an attempt
     *        will be made to locate the local MSS servlet.
     * @return URL The URL which contains the source of the message.
     * @throws MessageException if there is a problem generating the messsage
     *                          as a URL.
     */
    public URL generateTargetMessageAsURL(String deviceName, String mssUrl)
            throws MessageException;

    /**
     * Generates this message as a string for the given device. Attachments are
     * ignored.
     * <p>
     * @param deviceName the device name to generate this message for.
     * @return String the message text.
     * @throws MessageException if there was a problem during the generation.
     */
    public String generateTargetMessageAsString (String deviceName)
            throws MessageException;

    /**
     * Generates a MimeMultipart version of this message for the given device.
     * All attachments are processed for the specified device and attached to
     * the Multipart version.
     * <p>
     * @param deviceName The device to generate a message for.
     * @return MimeMultipart the message in Multipart format.
     * @throws MessageException if there was a probblem during the generation.
     */
    public MimeMultipart generateTargetMessageAsMimeMultipart (String deviceName)
            throws MessageException;

    /**
     * Adds an additional header to the message. Additional headers can apply to
     * all messages or to messages of a particular type. The
     * <code>messageType</code> parameter controls which message types the
     * header applies to.
     * <p>
     * @param messageType the message type. Must be one of:
     * <dl>
     * <dt><code>{@link #ALL}</code></dt>
     * <dd>The header applies to all types of message.</dd>
     * <dt><code>{@link #MHTML}</code></dt>
     * <dd>The header applies to all MHTML messages.</dd>
     * <dt><code>{@link #MMS}</code></dt>
     * <dd>The header applies to all MMS messages.</dd>
     * </dl>
     * @param name the name of the header.
     * @param value the value of the header.
     * @throws MessageException if an invalid message type was given.
     */
    public void addHeader(int messageType, String name, String value)
        throws MessageException;

    /**
     * Adds attachments to the message.
     * <p>
     * @param messageAttachments the attachments to add.
     * @throws MessageException if there were problems adding the attachments.
     */
    public void addAttachments(MessageAttachments messageAttachments)
        throws MessageException;

     /**
      * Removes all attachments from the message.
      * <p>
      * @throws MessageException if there were problems removing the
      * attachments.
      */
    public void removeAttachments() throws MessageException;

    /**
     * Retrieve the message headers corresponding to the specified type.
     *
     * @param messageType   of headers to be returned.
     * @return Map of message headers
     * @throws MessageException if there was a problem retrieving the headers
     */
    Map getHeaders(int messageType) throws MessageException;

    /**
     * Retrieve any message attachments.
     *
     * @return MessageAttachments
     */
    MessageAttachments getAttachments();

    /**
     * Returns a clone of this message. The clone contains only the fields set
     * by the following methods:
     * <ul>
     * <li><code>{@link #setMessageURL(java.net.URL)}</code></li>
     * <li><code>{@link #setMessage(String)}</code></li>
     * <li><code>{@link #setSubject(String)}</code></li>
     * <li><code>{@link #setCharacterEncoding(String)}</code></li>
     * </ul>
     * <p>
     * @see Object#clone()
     */
    public Object clone();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 07-Jul-05	829/2	amoore	VBM:2005052302 Fixed encoding problems that caused DB characters to be represented by '?'

 01-Jul-05	776/8	amoore	VBM:2005052302 Fixed encoding problems that caused DB characters to be represented by '?'

 17-May-05	744/1	amoore	VBM:2005051206 Updated MPS to ensure correct encoding of messages when using DBCS

 06-May-05	704/2	philws	VBM:2005042903 Fix merge conflicts

 04-May-05	651/1	amoore	VBM:2005042903 Made API changes to support message encoded in double byte languages

 05-May-05	671/3	amoore	VBM:2005050315 Updated attachment file check logic and maintained coding standards

 05-May-05	671/1	amoore	VBM:2005050315 Added file check for message attachments to ensure they are valid

 29-Apr-05	624/1	amoore	VBM:2005042509 Refactored MessageChannel to include redundant recipient processing in subclasses

 21-Mar-05	426/5	ianw	VBM:2005031506 Allow MSS to be installed without MPS

 21-Mar-05	426/3	ianw	VBM:2005031506 Allow MSS to be installed without MPS

 21-Mar-05	426/1	ianw	VBM:2005031506 Allow MSS to be installed without MPS

 09-Feb-05	320/1	ianw	VBM:2005020205 IBM fixes

 09-Feb-05	308/1	ianw	VBM:2005020205 IBM fixes

 20-Dec-04	270/1	pcameron	VBM:2004122004 New packagers for wemp

 29-Nov-04	243/4	geoff	VBM:2004112302 Provide new Logging Infrastructure: MPS

 17-Nov-04	238/2	pcameron	VBM:2004111608 PublicAPI doc fixes and additions

 19-Oct-04	205/1	matthew	VBM:2004100705 Change the message retrieval parameter from 'id' to 'pageid'

 19-Oct-04	198/1	matthew	VBM:2004101311 allow mss logging to work (stop MCS from hijacking it)

 13-Aug-04	155/3	claire	VBM:2004073006 WAP Push for MPS: Servlet to store and retrieve messages

 11-Aug-04	149/4	claire	VBM:2004073005 WAP Push for MPS: New channel adapter, generating messages as URLs, config update

 ===========================================================================
*/
