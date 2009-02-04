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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.management.tracking;

/**
 * Default implementaion of the PageDetails interface. This class
 * just returns the objects that it was created with.
 */
public class DefaultPageDetails implements PageDetails {

    /**
     * The canvas details object.
     */
    private CanvasDetails canvasDetails = null;

    /**
     * The device name.
     */
    private String deviceName = null;

    /**
     * Explicit constructor that allows all of the objects this default
     * implementaion stores to be explicitly provided.
     * @param canvasDetails the canvas details object.
     * @param deviceName the name of the device.
     */
    public DefaultPageDetails(CanvasDetails canvasDetails, String deviceName) {
        if ((canvasDetails == null) || (deviceName == null)) {
            throw new IllegalArgumentException(
                    "None of the parameters to the constructor may be null");
        }

        this.canvasDetails = canvasDetails;
        this.deviceName = deviceName;

    }

    /**
     * javadoc inherited
     */
    public CanvasDetails getCanvasDetails() {
        return this.canvasDetails;
    }

    /**
     * javadoc inherited.
     */
    public String getDeviceName() {
        return this.deviceName;
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 21-Jun-04	4702/1	matthew	VBM:2004061402 rework PageTracking

 11-Jun-04	4689/1	matthew	VBM:2004060706 add basic PageTracking facilities

 ===========================================================================
*/
