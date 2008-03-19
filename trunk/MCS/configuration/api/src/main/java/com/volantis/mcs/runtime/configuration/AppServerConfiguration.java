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
 * $Header: /src/voyager/com/volantis/mcs/runtime/configuration/AppServerConfiguration.java,v 1.1 2003/03/20 12:03:17 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 11-Mar-03    Geoff           VBM:2002112102 - Created; holds configuration 
 *                              information about application servers. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.runtime.configuration;

/**
 * Holds configuration information about application servers. 
 */ 
public class AppServerConfiguration {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2002.";

    private String baseUrl;
    private String internalUrl;
    private String pageBase;
    private String appServerName;
    private Boolean useServerConnectionPool;
    private String jndiProvider;
    private String datasourceVendor;
    private String datasource;
    private String user;
    private String password;
    private Boolean anonymous;

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getInternalUrl() {
        return internalUrl;
    }

    public void setInternalUrl(String internalUrl) {
        this.internalUrl = internalUrl;
    }

    public String getPageBase() {
        return pageBase;
    }

    public void setPageBase(String pageBase) {
        this.pageBase = pageBase;
    }

    public String getAppServerName() {
        return appServerName;
    }

    public void setAppServerName(String appServerName) {
        this.appServerName = appServerName;
    }

    public Boolean getUseServerConnectionPool() {
        return useServerConnectionPool;
    }

    public void setUseServerConnectionPool(Boolean useServerConnectionPool) {
        this.useServerConnectionPool = useServerConnectionPool;
    }

    public String getJndiProvider() {
        return jndiProvider;
    }

    public void setJndiProvider(String jndiProvider) {
        this.jndiProvider = jndiProvider;
    }

    public String getDatasourceVendor() {
        return datasourceVendor;
    }

    public void setDatasourceVendor(String datasourceVendor) {
        this.datasourceVendor = datasourceVendor;
    }

    public String getDatasource() {
        return datasource;
    }

    public void setDatasource(String datasource) {
        this.datasource = datasource;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getAnonymous() {
        return anonymous;
    }

    public void setAnonymous(Boolean anonymous) {
        this.anonymous = anonymous;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/6	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/4	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/2	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
