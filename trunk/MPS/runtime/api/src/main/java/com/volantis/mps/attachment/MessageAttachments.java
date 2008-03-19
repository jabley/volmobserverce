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
 * $Header: /src/mps/com/volantis/mps/attachment/MessageAttachments.java,v 1.4 2003/01/28 15:37:49 sfound Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 28-Nov-02    Sumit           VBM:2002112602 - Created
 * 28-Jan-03    Steve           VBM:2003012710 - Added javadoc.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mps.attachment;

import com.volantis.mcs.repository.RepositoryException;
import com.volantis.mps.message.MessageException;
import com.volantis.mps.localization.LocalizationFactory;
import com.volantis.synergetics.log.LogDispatcher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * The <code>MessageAttachments</code> class encapsulates an ordered list of
 * attachments  to be associated with a message. The list is maintained in the
 * order in  which individual message attachments are added to it. New
 * attachments are always added to the end.
 *
 * @volantis-api-include-in PublicAPI
 */
public class MessageAttachments {

    /**
     * The logger to use
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(MessageAttachments.class);

    /**
     * List of attachements
     */
    List attachments;

    /**
     * Creates a MessageAttachments object with no attachments defined.
     */
    public MessageAttachments(){
        attachments = new ArrayList();
    }

    /**
     * Adds the specified device attachment to this list of recipients.
     * Attachments are always added to the end of the list.
     * <p>
     * @param deviceMessageAttachment the device attachment to add.
     */
    public void addAttachment
                (DeviceMessageAttachment deviceMessageAttachment){
        attachments.add(deviceMessageAttachment);
    }
    
    
    /**
     * Removes a device specific attachment from the attachment list.
     * <p>
     * @param deviceMessageAttachment the device attachment to remove.
     */
    public void removeAttachment
                (DeviceMessageAttachment deviceMessageAttachment){
        attachments.remove(deviceMessageAttachment);
    }

    /**
     * Gets an iterator for the current attachments.
     * <p>
     * @return an iterator of DeviceMessageAttachment objects.
     */
    public Iterator iterator(){
        return attachments.iterator();
    }
    
    /**
     * Package scope method to get a list of DeviceMessageAttachments for a
     * specific channel. If a channel is not specified in the message attachment, 
     * the attachment is used on any channel.
     *
     * Use AttachmentUtilities to get access to this method as it is not 
     * part of the public API.
     * @param channelName The channel for which the recipients should be
     *                    returned for
     *
     * @return MessageAttachments Those attachments that match
     */
    MessageAttachments getAttachmentsForChannel(String channelName){
        MessageAttachments ret = new MessageAttachments();
        Iterator itr = iterator();
        while (itr.hasNext()) {
            DeviceMessageAttachment dma = (DeviceMessageAttachment)itr.next();
            try {
                // sanity check for undefined message attachments
                if (dma.getValueType() != MessageAttachment.UNDEFINED) {
                    if (dma.getChannelName().equals(channelName) ||
                        dma.getChannelName() == null) {
                        ret.addAttachment(dma);
                    }
                }
            } catch (MessageException mse) {
                logger.error("channel-message-list-error", channelName, mse);
            }
        }
        return ret;
    }
    
    /**
     * Package scope method to get a list of DeviceMessageAttachments for a
     * specific device. If the device specified in the message attachment is the 
     * same as or is an ancestor of the device specified the attachment is added 
     * 
     * Use AttachmentUtilities to get access to this method as it is not 
     * part of the public API.
     *
     * @param deviceName The device for which the recipients should be
     *                   returned for
     *
     * @return MessageAttachments Those attachments that match
     */
    MessageAttachments getAttachmentsForDevice(String deviceName){
        MessageAttachments ret = new MessageAttachments();
        Iterator itr = iterator();
        while (itr.hasNext()) {
            DeviceMessageAttachment dma = (DeviceMessageAttachment)itr.next();
            try{
                // sanity check for undefined message attachments
                if (dma.getValueType() != MessageAttachment.UNDEFINED) {

                    // First test if the device names match exactly which saves
                    // us going to the repository.  If not see if they are
                    // related in any fashion
                    if (dma.getDeviceName().equals(deviceName) ||
                            checkDeviceAncestry(dma, deviceName)) {
                        ret.addAttachment(dma);
                    }
                }

            } catch(Exception mse) {
                logger.error("device-message-list-error", deviceName, mse);
            }
        }
        return ret;
    }

    /**
     * Checks the device ancestry of the device contained within the message
     * attachment against the given device name.
     * <p>
     * @param dma        The device message attachment to check for ancestry
     *                   against the specified device.
     * @param deviceName The device name to check for being a descendant of
     *                   <code>dma</code>'s device.
     *
     * @return true if deviceName has <code>dma</code>'s device as an ancestor,
     *              false otherwise.
     *
     * @throws RepositoryException if there is a problem accessing the
     *                             repository.
     * @throws MessageException if there is a problem accessing the device
     *                          contained within the message attachment.
     */
    protected boolean checkDeviceAncestry(DeviceMessageAttachment dma,
                                          String deviceName)
            throws RepositoryException, MessageException {
        return AttachmentUtilities.isAncestorDevice(dma.getDeviceName(),
                                                    deviceName);
    }


}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 06-May-05	693/1	amoore	VBM:2005050315 Added file check for message attachments to ensure they are valid

 05-May-05	671/4	amoore	VBM:2005050315 Updated attachment file check logic and maintained coding standards

 05-May-05	671/2	amoore	VBM:2005050315 Added file check for message attachments to ensure they are valid

 29-Nov-04	243/3	geoff	VBM:2004112302 Provide new Logging Infrastructure: MPS

 17-Nov-04	238/2	pcameron	VBM:2004111608 PublicAPI doc fixes and additions

 19-Oct-04	198/1	matthew	VBM:2004101311 allow mss logging to work (stop MCS from hijacking it)

 24-Sep-04	140/1	claire	VBM:2004070704 Fixing testcases and replacing some hypersonic usage with stubs

 ===========================================================================
*/
