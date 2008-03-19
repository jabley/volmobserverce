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
 * $Header: /src/mps/com/volantis/mps/attachment/AttachmentUtilities.java,v 1.6 2002/12/19 16:10:39 sumit Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 28-Nov-02    Sumit           VBM:2002112602 - Created
 * 29-Nov-02    Sumit           VBM:2002112602 - New methods to get attachments
 *                              for device and channel
 * 09-Dec-02    Sumit           VBM:2002112602 - Fixed NullPointerException
 *                              in isAnc...ice(), exposed by unit test
 * 18-Dec-02    Sumit           VBM:2002103007 - Attachments only processed 
 *                              if the list is not empty in getAttachments..()
 * ----------------------------------------------------------------------------
 */
package com.volantis.mps.attachment;

import com.volantis.mcs.application.DeviceReader;
import com.volantis.mcs.devices.InternalDevice;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.mcs.runtime.Volantis;

/**
 * A utility class to give us access to the package scope methods in this package.
 * Prevents public API being affected.
 */
public class AttachmentUtilities {

    /**
     * Gets a MessageAttachments object from the incoming MessageAttachments
     * object with a specified channel name.
     * @param channelName
     * @param attachments
     * @return MessageAttachments
     */    
    public static MessageAttachments getAttachmentsForChannel(String channelName,
                                             MessageAttachments attachments) {
        if(attachments!=null) {                                                
            return attachments.getAttachmentsForChannel(channelName);
        }
        return attachments;                                                
    }
    
    /**
     * Gets a MessageAttachments object from the incoming MessageAttachments
     * object with a specified channel name.
     * @param deviceName
     * @param attachments
     * @return MessageAttachments
     */
    public static MessageAttachments getAttachmentsForDevice(String deviceName,
                                             MessageAttachments attachments) {
        if(attachments!=null) {
            return attachments.getAttachmentsForDevice(deviceName);
        }
        return attachments;
    }

    /**
     * Verifies if a given set of devices are related in an ancestor decendant 
     * type relation
     * @param ancestor
     * @param child
     * @return boolean True if ancestor has a child called child
     */    
    public static boolean isAncestorDevice(String ancestor, String child)
            throws RepositoryException {

        //get the volantis bean
        Volantis volantisBean = Volantis.getInstance();
        if (volantisBean == null) {
            throw new IllegalStateException("Volantis has not been initialised");
        }

        DeviceReader deviceReader = volantisBean.getDeviceReader();
        //get the device with the child's name and walkback up the fallback
        //chain to see if ancestor occurs somewhere up the tree
        InternalDevice device = deviceReader.getDevice(child);
        if (device == null) {
            return false;
        }
        do {
            if (device.getName().equals(ancestor)) {
                // We found an ancestor!
                return true;
            }
            //null indicates end of the fallback chain
        } while ((device = device.getFallbackDevice()) != null);

        return false;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Dec-04	270/1	pcameron	VBM:2004122004 New packagers for wemp

 18-Dec-03	77/1	mat	VBM:2003120106 Change Device to InternalDevice

 23-Oct-03	45/1	mat	VBM:2003101502 Rework tests to use AppManager and generally tidy them up

 ===========================================================================
*/
