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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.application;

import com.volantis.mcs.devices.InternalDevice;
import com.volantis.mcs.repository.LocalRepositoryConnection;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.mcs.runtime.RequestHeaders;
import com.volantis.mcs.runtime.Volantis;

public class DeviceReaderImpl implements DeviceReader {

    private final Volantis volantisBean;

    public DeviceReaderImpl(Volantis volantisBean) {
        this.volantisBean = volantisBean;
    }

    public InternalDevice getDevice(String deviceName)
            throws RepositoryException {

        LocalRepositoryConnection connection = null;
        try {
            connection = volantisBean.getDeviceConnection();
            return volantisBean.getDevice(connection, deviceName);
        } finally {
            // Ensure the connection is always freed.
            if (connection != null) {
                volantisBean.freeConnection(connection);
            }
        }
    }

    public InternalDevice getDevice(RequestHeaders headers)
            throws RepositoryException {

        LocalRepositoryConnection connection = null;
        try {
            connection = volantisBean.getDeviceConnection();
            return volantisBean.getDevice(connection, headers);
        } finally {
            // Ensure the connection is always freed.
            if (connection != null) {
                volantisBean.freeConnection(connection);
            }
        }
    }
}
