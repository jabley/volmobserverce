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

package com.volantis.mcs.repository.impl.jdbc;

import com.volantis.mcs.repository.jdbc.JDBCDriverVendor;
import com.volantis.mcs.repository.jdbc.MCSConnectionPoolConfiguration;
import com.volantis.mcs.repository.jdbc.MCSDriverConfiguration;

public class MCSDriverConfigurationImpl
        implements MCSDriverConfiguration {

    private JDBCDriverVendor driverVendor;

    private boolean hostWasSet;
    private String host;

    private boolean portWasSet;
    private int port;

    private String source;

    private MCSConnectionPoolConfiguration connectionPoolConfiguration;

    public JDBCDriverVendor getDriverVendor() {
        return driverVendor;
    }

    public void setDriverVendor(JDBCDriverVendor driverVendor) {
        this.driverVendor = driverVendor;
    }

    public String getHost() {
        if (!hostWasSet) {
            throw new IllegalStateException("Host was not set");
        }
        return host;
    }

    public void setHost(String host) {
        if (host != null && !host.equals("")) {
            hostWasSet = true;
            this.host = host;
        }
    }

    public int getPort() {
        if (!portWasSet) {
            throw new IllegalStateException("Port was not set");
        }
        return port;
    }

    public void setPort(int port) {
        portWasSet = true;
        this.port = port;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public MCSConnectionPoolConfiguration getConnectionPoolConfiguration() {
        return connectionPoolConfiguration;
    }

    public void setConnectionPoolConfiguration(
            MCSConnectionPoolConfiguration connectionPoolConfiguration) {
        this.connectionPoolConfiguration = connectionPoolConfiguration;
    }
}
