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
 * $Header: /src/mps/com/volantis/mps/recipient/DefaultRecipientResolver.java,v 1.1 2002/11/13 20:08:22 ianw Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 12-Nov-02    ianw            VBM:2002111211 - Created
 * ----------------------------------------------------------------------------
 */
package com.volantis.mps.recipient;

/**
 *
 * @author  ianw
 */
public class DefaultRecipientResolver implements MessageRecipientInfo {
    
    private static final String mark = "(c) Volantis Systems Ltd 2002.";
    
    protected final static String SMTP_CHANNEL = "smtp";
    
    protected final static String MMSC_CHANNEL = "nokia-mmsc";

    protected final static String SMSC_CHANNEL = "smpp-smsc";
    
    /** Creates a new instance of DefaultRecipientResolver */
    public DefaultRecipientResolver() {
               
    }

    // Javadoc inherited
    public String resolveChannelName(MessageRecipient recipient) {

        // VBM:2006112810 jabley - use the specified channel if there is one.
        try {
            if (null != recipient.getChannelName()) {
                return recipient.getChannelName();
            }
        } catch (RecipientException e) {
            // ignore
        }

        // We need to know what type of device we are sending to so
        // that we can get the message protocol.
        String deviceName = resolveDeviceName(recipient);
        
        if (deviceName != null) {
            // Get the message protocol for this device
            String messageProtocol = 
                    ResolverUtilities.getDeviceMessageProtocol(deviceName);

            if ("MHTML".equals(messageProtocol)) {
                // This is a email client so lets use channel smtp
                return SMTP_CHANNEL;
            } else if ("MMS".equals(messageProtocol)) {
                // This is an mms message so send via an mmsc
                return MMSC_CHANNEL;
            } else if ("SMS".equals(messageProtocol)) {
                // This is an mms message so send via an smsc
                return SMSC_CHANNEL;
            }
        }
        // We did not find a suitable channel
        return null;
    }

    // Javadoc inherited
    public String resolveDeviceName(MessageRecipient recipient) {
        // Let's assume for now that everyone who has not got a device is
        // using outlook
        try {
            if (null != recipient.getDeviceName()) {
                return recipient.getDeviceName();
            }
        } catch (RecipientException e) {
            // ignore
        }

        return "Outlook";
    }

    // Javadoc inherited
    public String resolveCharacterEncoding(MessageRecipient recipient) {
        return null;
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 07-Jul-05	829/1	amoore	VBM:2005052302 Fixed encoding problems that caused DB characters to be represented by '?'

 04-May-05	651/1	amoore	VBM:2005042903 Made API changes to support message encoded in double byte languages

 29-Nov-04	243/1	geoff	VBM:2004112302 Provide new Logging Infrastructure: MPS

 19-Oct-04	198/1	matthew	VBM:2004101311 allow mss logging to work (stop MCS from hijacking it)

 24-Sep-04	140/1	claire	VBM:2004070704 Fixing testcases and replacing some hypersonic usage with stubs

 ===========================================================================
*/
