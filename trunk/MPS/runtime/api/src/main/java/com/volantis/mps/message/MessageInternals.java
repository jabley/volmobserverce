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
 * $Header: /src/mps/com/volantis/mps/message/MessageInternals.java,v 1.2 2002/11/29 17:27:20 sumit Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 12-Nov-02    ianw            VBM:2002111211 - Created
 * 29-Nov-02    Sumit           VBM:2002112602 - Added getAttachments method
 *                              as MultiChannelMessage.getAttachments() is 
 *                              package scope
 * ----------------------------------------------------------------------------
 */

package com.volantis.mps.message;

import com.volantis.mps.attachment.MessageAttachments;

import java.util.Map;

/**
 * This class is used as a friend to <CODE>MultiChannelMessage</CODE>.
 * It provides access to non public interfaces on the class.
 */
public class MessageInternals {
    
    private static final String mark = "(c) Volantis Systems Ltd 2002.";
    
    /*
     * Indicates headers should be applied to all protocols.
     */
    final public static int ALL = MultiChannelMessage.ALL;
    /*
     * Indicates headers should be applied to MHTML protocol only.
     */
    final public static int MHTML = MultiChannelMessage.MHTML;
    /*
     * Indicates headers should be applied to MMS.
     */
    final public static int MMS = MultiChannelMessage.MMS;
    
    /** We only provide static methods <*/
    protected MessageInternals() {
    }
    

    public static Map getHeaders(MultiChannelMessage message, int messageType) 
        throws MessageException {
        return message.getHeaders(messageType);
    }
    
    /**
     * Static method to get the attachments from a MultiChannelMessage. Internal
     * use only.
     * @param message
     * @return MessageAttachments
     */
    public static MessageAttachments getAttachments(MultiChannelMessage message) {
        return message.getAttachments();
    }
 
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-04	243/1	geoff	VBM:2004112302 Provide new Logging Infrastructure: MPS

 19-Oct-04	198/1	matthew	VBM:2004101311 allow mss logging to work (stop MCS from hijacking it)

 ===========================================================================
*/
