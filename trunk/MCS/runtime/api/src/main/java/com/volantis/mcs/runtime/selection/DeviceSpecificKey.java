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
 * $Header: /src/voyager/com/volantis/mcs/runtime/DeviceSpecificKey.java,v 1.5 2002/05/29 13:22:56 pduffin Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 06-Feb-02    Paul            VBM:2001122103 - Created to use as a key into
 *                              a cache.
 * 11-Feb-02    Paul            VBM:2001122105 - Moved from accessors.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from 
 *                              class to string.
 * 22-Mar-02    Adrian          VBM:2002031503 - Added Object parameter to
 *                              the key as more information that the object
 *                              identity may be required to retrieve and store
 *                              the object in a cache
 * 29-May-02    Paul            VBM:2002050301 - Removed changes made to
 *                              support specific repositories and instead
 *                              added mechanisms to allow repository
 *                              to control caching as that is the only
 *                              difference between the repositories. Also
 *                              removed retrieveBestObject added to support
 *                              the css renderers as they have been modified to
 *                              use the original interface.
 * 03-Jun-03    Allan           VBM:2003060301 - ObjectHelper moved to 
 *                              Synergetics. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.runtime.selection;



/**
 * This class creates a device specific key from a device name and some
 * extract information.
 *
 * It can be used as a key in a cache.
 */
public class DeviceSpecificKey {

    /**
     * The name of the device.
     */
    private final String deviceName;

    /**
     * Any extra information need to define the object
     */
    private final Object extraInfo;

    /**
     * Create a new <code>DeviceSpecificKey</code>.
     *
     * @param deviceName The name of the device.
     * @throws IllegalArgumentException If either parameters is null.
     */
    public DeviceSpecificKey(
            String deviceName,
            Object extraInfo) {

        if (deviceName == null) {
            throw new IllegalArgumentException("Device name must not be null");
        }

        this.deviceName = deviceName;
        this.extraInfo = extraInfo;
    }

    /**
     * Get the device name.
     *
     * @return The device name.
     */
    public String getDeviceName() {
        return deviceName;
    }

    /**
     * Get the extraInfo
     *
     * @return The <code>Object</code> representing the extra information
     */
    public Object getExtraInfo() {
        return extraInfo;
    }

    public boolean equals(Object object) {
        if (object == null || object.getClass() != getClass()) {
            return false;
        }

        DeviceSpecificKey key = (DeviceSpecificKey) object;

        if (extraInfo == null) {
            return (deviceName.equals(key.deviceName));
        } else {
            return (deviceName.equals(key.deviceName)
                    && extraInfo.equals(key.extraInfo));
        }
    }

    public int hashCode() {
        if (extraInfo == null) {
            return deviceName.hashCode();
        } else {
            return (deviceName.hashCode()
                    + extraInfo.hashCode());
        }
    }

    // Javadoc inherited from super class.
    public String toString() {
        return getClass().getName()
                + "@" + Integer.toHexString(System.identityHashCode(this))
                + " [" + paramString() + "]";
    }

    /**
     * Return a String representation of the state of the object.
     *
     * @return The String representation of the state of the object.
     */
    protected String paramString() {
        return deviceName + "," + extraInfo;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
