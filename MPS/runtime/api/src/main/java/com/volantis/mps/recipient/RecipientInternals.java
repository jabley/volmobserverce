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
 * $Header: /src/mps/com/volantis/mps/recipient/RecipientInternals.java,v 1.3 2002/12/17 13:53:48 ianw Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 12-Nov-02    ianw            VBM:2002111211 - Created
 * 20-Nov-02    Sumit           VBM:2002111815 - Added try/catch blocks
 * 16-Dec-02    ianw            VBM:2002111211 - Added raddRecipients method.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mps.recipient;

/**
 * This class is used as a friend to <CODE>MessageRecipient</CODE>.
 * It provides access to non public interfaces on the class.
 */
public class RecipientInternals {
    
    private static final String mark = "(c) Volantis Systems Ltd 2002.";
    
    /**
     * This is a TO recipient.
     */
    final public static int TO_RECIPIENT = MessageRecipient.TO_RECIPIENT;
    
    /**
     * This is a CC recipient.
     */
    final public static int CC_RECIPIENT = MessageRecipient.CC_RECIPIENT;
    
    /**
     * This is a BCC recipient.
     */
    final public static int BCC_RECIPIENT = MessageRecipient.BCC_RECIPIENT;
    
    /** We only provide static methods <*/
    protected RecipientInternals() {
    }
    
    /**
     * Set the type of recipient.
     * @param recipient The <CODE>MessageRecipient</CODE> who's type we wish to set.
     * @param recipientType The type of recipient which can be on of
     * <CODE>TO_RECIPIENT, CC_RECIPIENT</CODE> or <CODE>BCC_RECIPIENT</CODE>
     */

    public static void setRecipientType(MessageRecipient recipient, int recipientType) 
        throws RecipientException {
        recipient.setRecipientType(recipientType);
    }
 
    /**
     * Gets the type of recipient.
     * @param recipient The <CODE>MessageRecipient</CODE> who's type we wish to get.
     * @return The recipients channel.
     */    
    public static int getRecipientType(MessageRecipient recipient) 
        throws RecipientException {
        return recipient.getRecipientType();
    }
    
    /**
     * Adds recipients to existing recipients list.
     * @param recipients The <CODE>MessageRecipients</CODE> to add too.
     * @param additions The <CODE>MessageRecipients</CODE> to add.
     */
    public static void addRecipients(MessageRecipients recipients, 
        MessageRecipients additions) 
        throws RecipientException {

        try {
            recipients.addRecipients(additions);
        } catch (Exception e) {
            throw new RecipientException(e);
        }
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
