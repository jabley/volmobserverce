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
 * $Header: /src/mps/com/volantis/mps/attachment/DeviceMessageAttachment.java,v 1.3 2003/01/30 12:02:45 sfound Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 28-Nov-02    Sumit           VBM:2002112602 - Created
 * 28-Jan-03    Steve           VBM:2003012710 - Added javadoc.
 *                              Fixed equals method to take an Object parameter
 * 30-Jan-03    Steve           VBM:2003012710 - Removed all code changes.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mps.attachment;

import com.volantis.mps.localization.LocalizationFactory;
import com.volantis.mps.message.MessageException;
import com.volantis.synergetics.localization.ExceptionLocalizer;

/**
 * Represents a device-specific {@link MessageAttachment} to be used with a
 * {@link com.volantis.mps.message.MultiChannelMessage MultiChannelMessage}.
 *
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 */
public class DeviceMessageAttachment extends MessageAttachment {

    /**
     * The exception localizer instance for this class.
     */
    private static final ExceptionLocalizer localizer =
            LocalizationFactory.createExceptionLocalizer(
                    DeviceMessageAttachment.class);

    /**
     * The name of the device that the attachment is intended for
     */
    private String deviceName;

    /**
     * The channel name to which the attachment should be sent
     */
    private String channelName;

    /**
     * Initialize the new instance with an undefined type.
     */
    public DeviceMessageAttachment() {
    }

    /**
     * Initialize the new instance with a known type.
     *
     * @param value       The URL or name of the file that contains the
     *                    attachment content. Cannot be null.
     * @param mimeType    The attachment content type. Cannot be null.
     * @param valueType   The attachment type (URL or File).
     * @param deviceName  The name of the device that this attachment is
     *                    intended for. Cannot be null.
     * @param channelName The name of the channel on which the attachment
     *                    will be sent. Cannot be null.
     *
     * @throws MessageException if there were problems creating the message
     *                          attachment with the given values.
     */
    public DeviceMessageAttachment(String value,
                                   String mimeType,
                                   int valueType,
                                   String deviceName,
                                   String channelName) throws MessageException {
        super(value, mimeType, valueType);
            setDeviceName(deviceName);
            setChannelName(channelName);
    }

    /**
     * Sets the name of the device for which this attachment is intended.
     *
     * @param deviceName The name of the device. Cannot be null.
     * @throws MessageException if <code>deviceName</code> is null.
     */
    public void setDeviceName(String deviceName) throws MessageException {
        if (deviceName == null) {
            throw new MessageException(
                    localizer.format("device-name-null-invalid"));
        }
        this.deviceName = deviceName;
    }

    /**
     * Returns the name of the device for which this attachment is intended.
     *
     * @return The name of the device.
     * @throws MessageException if there were problems when retrieving the
     *                          device name.
     */
    public String getDeviceName() throws MessageException {
        return deviceName;
    }

    /**
     * Sets the name of the channel on which to send the attachment.
     *
     * @param channelName The name of the channel. Cannot be null.
     * @throws MessageException if <code>channelName</code> is null.
     */
    public void setChannelName(String channelName) throws MessageException {
        if (channelName == null) {
            throw new MessageException(
                    localizer.format("channel-name-null-invalid"));
        }
        this.channelName = channelName;
    }

    /**
     * Gets the name of the channel on which this attachment will be sent.
     *
     * @return The channel name.
     * @throws MessageException if there were problems retrieving the channel
     *                          name.
     */
    public String getChannelName() throws MessageException {
        return channelName;
    }

    // javadoc inherited
    public boolean equals(Object object) {
        boolean isEqual = false;

        if (super.equals(object)) {
            DeviceMessageAttachment attachment =
                    (DeviceMessageAttachment) object;
            // superclass is equal so check parameters specific to subclass
            if (channelName != null ?
                    channelName.equals(attachment.channelName) :
                    attachment.channelName == null) {
                // Channel names are equal so check device names
                if (deviceName != null ?
                        deviceName.equals(attachment.deviceName) :
                        attachment.deviceName == null) {
                    // Everything is equal!
                    isEqual = true;
                }
            }
        }

        return isEqual;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 22-Apr-05	610/1	philws	VBM:2005040503 Port Public API changes from 3.3

 22-Apr-05	608/1	philws	VBM:2005040503 Update MPS Public API

 18-Mar-05	430/1	emma	VBM:2005031707 Merge from MPS V3.3.0 - Ensuring mps' LocalizationFactory used; moved localization source code to localization subsystem

 18-Mar-05	428/1	emma	VBM:2005031707 Making all classes use mps' LocalizationFactory, and moving localization source code to localization subsystem

 29-Nov-04	243/4	geoff	VBM:2004112302 Provide new Logging Infrastructure: MPS

 17-Nov-04	238/1	pcameron	VBM:2004111608 PublicAPI doc fixes and additions

 19-Oct-04	198/1	matthew	VBM:2004101311 allow mss logging to work (stop MCS from hijacking it)

 24-Sep-04	142/4	claire	VBM:2004070806 Correct invalid attachment type on PublicAPI DeviceMessageAttachment

 ===========================================================================
*/
