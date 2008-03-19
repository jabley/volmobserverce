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
 * $Header: /src/mps/com/volantis/mps/recipient/ResolverUtilities.java,v 1.1 2002/11/13 20:08:35 ianw Exp $
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

import com.volantis.mcs.application.DeviceReader;
import com.volantis.mcs.devices.InternalDevice;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.mcs.runtime.Volantis;

/**
 *
 * @author  ianw
 */
public class ResolverUtilities {

    /** Creates a new instance of ResolverUtilities */
    protected  ResolverUtilities() {
    }
    
    public static String getDeviceMessageProtocol(String deviceName) {

        // Get the Volantis Bean
        Volantis volantisBean = Volantis.getInstance();

        if (volantisBean == null) {
            throw new IllegalStateException("Volantis has not been initialised");
        }

        DeviceReader deviceReader = volantisBean.getDeviceReader();
        String channel = null;
        try {
            InternalDevice device = deviceReader.getDevice(deviceName);
            channel = device.getPolicyValue("preferredmessageprotocol");
        } catch (RepositoryException e) {
        }

        return channel;
    }
        
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Dec-04	270/1	pcameron	VBM:2004122004 New packagers for wemp

 29-Nov-04	243/1	geoff	VBM:2004112302 Provide new Logging Infrastructure: MPS

 19-Oct-04	198/1	matthew	VBM:2004101311 allow mss logging to work (stop MCS from hijacking it)

 18-Dec-03	77/1	mat	VBM:2003120106 Change Device to InternalDevice

 ===========================================================================
*/
