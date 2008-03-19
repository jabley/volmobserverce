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

import com.volantis.mcs.repository.jdbc.InternalJDBCRepositoryConfiguration;
import com.volantis.mcs.repository.jdbc.JDBCDriverVendor;

import javax.sql.DataSource;

public class JDBCRepositoryConfigurationImpl
        implements InternalJDBCRepositoryConfiguration {

    private JDBCDriverVendor driverVendor;

    private DataSource dataSource;

    private Boolean anonymous;

    private String username;

    private String password;

    private boolean shortNames;

    private boolean releaseConnectionsImmediately;

    public JDBCDriverVendor getDriverVendor() {
        return driverVendor;
    }

    public void setDriverVendor(JDBCDriverVendor driverVendor) {
        this.driverVendor = driverVendor;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        if (dataSource instanceof AnonymousDataSource) {
            final AnonymousDataSource anonymousDataSource =
                (AnonymousDataSource) dataSource;
            if (username == null && password == null) {
                username = anonymousDataSource.getUserName();
                password = anonymousDataSource.getPassword();
            }
        }
    }

    public boolean isAnonymous() {
        return anonymous == null? dataSource instanceof AnonymousDataSource:
                                  anonymous.booleanValue();
    }

    public void setAnonymous(boolean anonymous) {
        this.anonymous = Boolean.valueOf(anonymous);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isShortNames() {
        return shortNames;
    }

    public void setShortNames(boolean shortNames) {
        this.shortNames = shortNames;
    }

    public boolean isReleaseConnectionsImmediately() {
        return releaseConnectionsImmediately;
    }

    public void setReleaseConnectionsImmediately(
            boolean releaseConnectionsImmediately) {
        this.releaseConnectionsImmediately = releaseConnectionsImmediately;
    }
}
