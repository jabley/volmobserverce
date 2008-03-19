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
 * $Header: /src/mps/com/volantis/mps/recipient/MessageRecipient.java,v 1.6 2003/03/20 10:15:36 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 12-Nov-02    ianw            VBM:2002111211 - Created
 * 18-Nov-02    Mat             VBM:2002111815 - Added throws RecipientException
 *                              to get/setMSISDN(), get/setDeviceName(),
 *                              get/setChannelName(), resolveChannel/DeviceName,
 *                              get/setRecipientType() in line with architecture 
 *                              document (AN036)
 * 16-Dec-02    Ian             VBM:2002111211: Added equals message to honour
 *                              Set interface within MessageRecipients.
 * 19-Mar-03    Geoff           VBM:2003032001 - Did the usual cleanup that
 *                              IDEA informed me was required, i.e. removed 
 *                              unnecessary imports, code and casts.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mps.recipient;


import com.volantis.mps.localization.LocalizationFactory;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;

import javax.mail.internet.InternetAddress;


/**
 * This class represents the recipient of a {@link
 * com.volantis.mps.message.MultiChannelMessage MultiChannelMessage}. The
 * recipient should be supplied with an address or phone number as a minimum.
 * If a device or channel name are not supplied these will be resolved as
 * needed by the Message Preparation Server.
 *
 * @volantis-api-include-in PublicAPI
 */
public class MessageRecipient implements Cloneable {

    private static final String mark = "(c) Volantis Systems Ltd 2002.";

    /**
     * The logger to use
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(MessageRecipient.class);

    /**
     * The exception localizer instance for this class.
     */
    private static final ExceptionLocalizer localizer =
            LocalizationFactory.createExceptionLocalizer(
                    MessageRecipient.class);

    /**
     * The device/channel was not resolved.
     */
    final public static int NOT_RESOLVED = 1;

    /**
     * The device/channel was succesfully resolved.
     */
    final public static int OK = 0;

    /**
     * This is a TO recipient.
     */
    final static int TO_RECIPIENT = 0;

    /**
     * This is a CC recipient.
     */
    final static int CC_RECIPIENT = 1;

    /**
     * This is a BCC recipient.
     */
    final static int BCC_RECIPIENT = 2;

    /**
     * The internet address of this recipient.
     */
    private InternetAddress address;

    /**
     * The phone number for this recipient.
     */
    private String msISDN;

    /**
     * The device for this recipient.
     */
    private String deviceName;

    /**
     * The communication channel to use for this recipient.
     */
    private String channelName;

    /**
     * The class to use to resolve device/channel.
     */
    private MessageRecipientInfo messageRecipientInfo;

    /**
     * The type of recipient e.g TO, CC or BCC.
     */
    private int recipientType;

    /**
     * Holds a pre-localized reason for a failure to send a message to this
     * recipient. Will be null if there was no failure to send.
     */
    private String failureReason = null;

    /**
     * Helper class for this class
     */
    private MessageRecipientHelper helper = MessageRecipientHelper.getInstance();

    /**
     * Creates a new instance of MessegeRecipient.
     * <p>
     *
     * @throws RecipientException if there were problems creating the instance.
     */
    public MessageRecipient() throws RecipientException {
        messageRecipientInfo = helper.getMessageRecipientInfo();
    }

    /**
     * Creates a new instance of MessegeRecipient.
     * <p>
     *
     * @param address    The recipient's internet email address.
     * @param deviceName The name of the device for the recipient.
     * @throws RecipientException if there were problems creating the instance.
     */
    public MessageRecipient(InternetAddress address, String deviceName)
            throws RecipientException {
        this();
        this.address = address;
        this.deviceName = deviceName;
    }

    /**
     * Sets the internet email address for the recipient.
     * <p>
     *
     * @param address The recipient's internet email address.
     * @throws RecipientException if there were problems setting the address.
     */
    public void setAddress(InternetAddress address) throws RecipientException {
        this.address = address;
    }

    /**
     * Gets the internet email address of the recipient.
     * <p>
     *
     * @return The recipient's internet email address.
     * @throws RecipientException if there were problems retrieving the address.
     */
    public InternetAddress getAddress() throws RecipientException {
        return address;
    }

    /**
     * Sets the phone number for the recipient.
     * <p>
     *
     * @param msISDN The recipient's phone number.
     * @throws RecipientException if there were problems setting the number.
     */
    public void setMSISDN(String msISDN) throws RecipientException {
        this.msISDN = msISDN;
    }

    /**
     * Gets the phone number of the recipient.
     * <p>
     *
     * @return The recipient's phone number.
     * @throws RecipientException if there were problems retrieving the number.
     */
    public String getMSISDN() throws RecipientException {
        return msISDN;
    }

    /**
     * Sets the device for the recipient.
     * <p>
     *
     * @param deviceName The recipient's device.
     * @throws RecipientException if there were problems setting the device.
     */
    public void setDeviceName(String deviceName) throws RecipientException {
        this.deviceName = deviceName;
    }

    /**
     * Gets the device of the recipient.
     * <p>
     *
     * @return The recipient's device.
     * @throws RecipientException if there were problems retrieving the device.
     */
    public String getDeviceName() throws RecipientException {
        return deviceName;
    }

    /**
     * Sets the channel for the recipient.
     * <p>
     *
     * @param channelName The recipient's channel.
     * @throws RecipientException if there were problems setting the channel.
     */
    public void setChannelName(String channelName) throws RecipientException {
        this.channelName = channelName;
    }

    /**
     * Gets the channel of the recipient.
     * <p>
     *
     * @return The recipient's channel.
     * @throws RecipientException if there were problems retrieving the channel.
     */
    public String getChannelName() throws RecipientException {
        return channelName;
    }

    /**
     * Set the type of recipient.
     * <p>
     *
     * @param recipientType The type of recipient. Must be one of:
     *                      <ul>
     *                      <li><code>TO_RECIPIENT</code></li>
     *                      <li><code>CC_RECIPIENT</code></li>
     *                      <li><code>BCC_RECIPIENT</code></li>
     *                      </ul>
     * @throws RecipientException if there were problems setting the type.
     */
    void setRecipientType(int recipientType) throws RecipientException {
        this.recipientType = recipientType;
    }

    /**
     * Gets the type of recipient.
     * <p>
     *
     * @return The recipient's type.
     * @throws RecipientException if there were problems retrieving the type.
     */
    int getRecipientType() throws RecipientException {
        return recipientType;
    }

    /**
     * Resolves the device for this recipient using the user-supplied
     * <code>RecipientInfo</code>.
     * <p>
     *
     * @param force If true the resolved value will overwrite any existing
     *              device name.
     * @return <code>OK</code> if the device name was resolved successfully, or
     *         <code>NOT_RESOLVED</code> if unsuccessful.
     */
    public int resolveDeviceName(boolean force) throws RecipientException {

        if ((deviceName == null) || force) {
            if (messageRecipientInfo != null) {
                deviceName = messageRecipientInfo.resolveDeviceName(this);
            }
            if (deviceName == null) {
                return NOT_RESOLVED;
            }
        }
        return OK;
    }

    /**
     * Resolves the channel for this recipient using the user-supplied
     * <code>RecipientInfo</code>.
     * <p>
     *
     * @param force If true the resolved value will overwrite any existing
     *              channel name.
     * @return <code>OK</code> if the channel name was resolved successfully, or
     *         <code>NOT_RESOLVED</code> if unsuccessful.
     */
    public int resolveChannelName(boolean force) throws RecipientException {

        if ((channelName == null) || force) {
            if (messageRecipientInfo != null) {
                channelName = messageRecipientInfo.resolveChannelName(this);
            }
            if (channelName == null) {
                return NOT_RESOLVED;
            }
        }
        return OK;
    }

    /**
     * Returns the failure reason associated with the recipient.
     *
     * @return the failure reason or null if there is none
     */
    public String getFailureReason() {
        return failureReason;
    }

    /**
     * Records the given failure reason against this recipient. This should be
     * used by the {@link com.volantis.mps.channels.MessageChannel
     * MessageChannel} implementations when a send to this recipient fails.
     *
     * @param failureReason the failure reason message to be recorded against
     *                      the recipient. If localization is required this
     *                      should be a pre-localized message
     */
    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
    }

    /**
     * Returns a copy of this MessageRecipient, in line with the
     * weakly-specified contract described in {@link Object#clone()}. The
     * visibility of the method has been increased to <code>public</code>,
     * rather than the <code>protected</code> declaration in the superclass.
     *
     * @return a copy of this MessageRecipient.
     * @throws CloneNotSupportedException if the clone operation cannot be
     *                                    performed.
     */
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    // javadoc inherited
    public boolean equal(Object o) {
        MessageRecipient mr1 = this;
        MessageRecipient mr2 = (MessageRecipient) o;

        boolean result;
        try {
            String key1 = generateKeyRepresentation(mr1);

            if (mr2 != null) {
                String key2 = generateKeyRepresentation(mr2);
                System.err.println(key1 + "-" + key2);
                result = (key1.compareTo(key2) == 0);
            } else {
                return false;
            }
        } catch (RecipientException e) {
            logger.error(localizer.format("comparison-failure"), e);
            result = false;
        }
        return result;
    }

    /**
     * Return a String representation of the specified MessageRecipient.
     *
     * @param mr a MessageRecipient - not null.
     * @return a String.
     * @throws RecipientException if there was a problem reading the
     *                            MessageRecipient fields.
     */
    private static String generateKeyRepresentation(MessageRecipient mr)
            throws RecipientException {
        StringBuffer buf = new StringBuffer();
        buf.append(mr.getChannelName());
        buf.append(mr.getDeviceName());
        buf.append(mr.getAddress());
        buf.append(mr.getMSISDN());
        return buf.toString();
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 04-May-05	666/1	philws	VBM:2005050311 Port of failureReason from 3.3

 04-May-05	660/1	philws	VBM:2005050311 Add failureReason property API to MessageRecipient, set failureReasons in channel adapters and show example usage of failureReason

 22-Apr-05	610/1	philws	VBM:2005040503 Port Public API changes from 3.3

 22-Apr-05	608/1	philws	VBM:2005040503 Update MPS Public API

 29-Nov-04	243/3	geoff	VBM:2004112302 Provide new Logging Infrastructure: MPS

 17-Nov-04	238/2	pcameron	VBM:2004111608 PublicAPI doc fixes and additions

 19-Oct-04	198/1	matthew	VBM:2004101311 allow mss logging to work (stop MCS from hijacking it)

 ===========================================================================
*/
