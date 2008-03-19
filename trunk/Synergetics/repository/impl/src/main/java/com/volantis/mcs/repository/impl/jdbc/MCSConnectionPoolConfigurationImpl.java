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

import com.volantis.mcs.repository.jdbc.MCSConnectionPoolConfiguration;

import javax.sql.DataSource;

public class MCSConnectionPoolConfigurationImpl
        implements MCSConnectionPoolConfiguration {

    private boolean enabled;

    private int maxConnections = 10;

    private int maxFreeConnections = 8;

    private int minFreeConnections = 2;

    private int initialConnections = 2;

    private boolean keepAliveActive;

    private int keepAlivePollInterval;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public int getMaxConnections() {
        return maxConnections;
    }

    public void setMaxConnections(int maxConnections) {
        this.maxConnections = maxConnections;
    }

    public int getMaxFreeConnections() {
        return maxFreeConnections;
    }

    public void setMaxFreeConnections(int maxFreeConnections) {
        this.maxFreeConnections = maxFreeConnections;
    }

    public int getMinFreeConnections() {
        return minFreeConnections;
    }

    public void setMinFreeConnections(int minFreeConnections) {
        this.minFreeConnections = minFreeConnections;
    }

    public int getInitialConnections() {
        return initialConnections;
    }

    public void setInitialConnections(int initialConnections) {
        this.initialConnections = initialConnections;
    }

    public boolean isKeepAliveActive() {
        return keepAliveActive;
    }

    public void setKeepAliveActive(boolean keepAliveActive) {
        this.keepAliveActive = keepAliveActive;
    }

    public int getKeepAlivePollInterval() {
        return keepAlivePollInterval;
    }

    public void setKeepAlivePollInterval(int keepAlivePollInterval) {
        this.keepAlivePollInterval = keepAlivePollInterval;
    }
}
