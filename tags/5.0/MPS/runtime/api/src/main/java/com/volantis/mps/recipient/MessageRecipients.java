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
 * $Header: /src/mps/com/volantis/mps/recipient/MessageRecipients.java,v 1.3 2002/12/17 13:53:39 ianw Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 12-Nov-02    ianw            VBM:2002111211 - Created
 * 18-Nov-02    Mat             VBM:2002111815 - Added throws RecpientException
 *                              to add/removeRecipient(), resolveChannelNames()
 *                              as per architecture document (AN036)
 * 16-Dec-02    ianw            VBM:2002111211 - Forced getIterator to re-sort
 *                              recipients and added addRecipients method to
 *                              merge recipient lists.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mps.recipient;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * The <code>MessageRecipients</code> class encapsulates a list of recipients.
 * It can be used to represent the recipients, those on the copy list or those
 * on the blind copy list for transmission of a message.
 *
 * @volantis-api-include-in PublicAPI
 */
public class MessageRecipients {

    private static final String mark = "(c) Volantis Systems Ltd 2002.";

    /**
     * The sorted recipient list
     */
    SortedSet recipientsList = new TreeSet(new MessageRecipientComparator());

    /**
     * Creates a new instance of MessageRecipients.
     */
    public MessageRecipients() {
    }

    /**
     * Adds the specified recipient to this list of recipients.
     * <p>
     * @param recipient The recipient to add.
     * @throws RecipientException if <code>recipient</code> could not be added.
     */
    public void addRecipient(MessageRecipient recipient)
            throws RecipientException {
        recipientsList.add(recipient);
    }

    /**
     * Adds a <code>MessageRecipients</code> to the <code>MessageRecipients</code>
     * list.
     * <p>
     * @param recipients The <code>MessageRecipients</code> to add to the list.
     * @throws RecipientException if <code>recipient</code> could not be added
     */
    void addRecipients(MessageRecipients recipients) throws RecipientException {
        try {
            Iterator i = recipients.getIterator();
            while (i.hasNext()) {
                recipientsList.add(i.next());
            }
        } catch (Exception e) {
            throw new RecipientException(e);
        }
    }

    /**
     * Removes the specified recipient from this list of recipients.
     * <p>
     * @param recipient The recipient to remove.
     * @throws RecipientException if <code>recipient</code> could not be removed.
     */
    public void removeRecipient(MessageRecipient recipient)
            throws RecipientException {
        recipientsList.remove(recipient);
    }

    /**
     * Resolves the device for recipients using the user-supplied
     * <code>RecipientInfo</code>.
     * <p>
     * @param force If true the resolved value will overwrite an existing
     * device name.
     * @return The number of recipients with unresolved devices.
     */
    public int resolveDeviceNames(boolean force) throws RecipientException {
        int unresolved = 0;
        Iterator i = recipientsList.iterator();

        MessageRecipient recipient;
        while (i.hasNext()) {
            recipient = (MessageRecipient)i.next();
            if (recipient.resolveDeviceName(force) ==
                    MessageRecipient.NOT_RESOLVED) {
                unresolved++;
            }
        }

        return unresolved;
    }

    /**
     * Resolves the channel for recipients using the user-supplied
     * <code>RecipientInfo</code>.
     * <p>
     * @param force If true the resolved value will overwrite an existing
     * channel name.
     * @return The number of recipients with unresolved channels.
     */
    public int resolveChannelNames(boolean force) throws RecipientException {
        int unresolved = 0;

        Iterator i = recipientsList.iterator();

        MessageRecipient recipient;

        while (i.hasNext()) {
            recipient = (MessageRecipient)i.next();
            if (recipient.resolveChannelName(force) ==
                    MessageRecipient.NOT_RESOLVED) {
                unresolved++;
            }
        }
        return unresolved;
    }

    /**
     * Gets an <code>Iterator</code> for this <code>MessageRecipients</code>.
     * The <code>Iterator</code> is guaranteed to be in channel and device
     * order.
     * <p>
     * @return The <code>Iterator</code>.
     */
    public Iterator getIterator() {

        Collection c = new HashSet(recipientsList);

        TreeSet sortedSet = new TreeSet(new MessageRecipientComparator());

        sortedSet.addAll(c);

        return sortedSet.iterator();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-04	243/3	geoff	VBM:2004112302 Provide new Logging Infrastructure: MPS

 17-Nov-04	238/2	pcameron	VBM:2004111608 PublicAPI doc fixes and additions

 19-Oct-04	198/1	matthew	VBM:2004101311 allow mss logging to work (stop MCS from hijacking it)

 ===========================================================================
*/
