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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.xml.pipeline.sax.proxy;

/**
 * This class represents the proxy element in the configuration file.
 */
public class DefaultProxy implements Proxy {
    /**
     * The id property.
     */
    private String id;

    /**
     * The proxy host.
     */
    private String host;

    /**
     * The port for the proxy.
     */
    private int port;

    /**
     * Construct a new Proxy with the specified id.
     */
    public DefaultProxy() {
    }

    // javadoc inherited
    public String getId() {
        return id;
    }

    /**
     * Set the id.
     * @param id The id.
     */
    public void setId(String id) {
        this.id = id;
    }

    // javadoc inherited
    public String getHost() {
        return host;
    }

    /**
     * Set the proxy host.
     * @param host The host.
     */
    public void setHost(String host) {
        this.host = host;
    }

    // javadoc inherited
    public int getPort() {
        return port;
    }

    /**
     * Set the proxt port.
     * @param port The port.
     */
    public void setPort(int port) {
        this.port = port;
    }

    // javadoc inherited
    public String getUser() {
        // Default proxy does not support authentication and then does not have
        // a user name.
        return null;
    }

    // Javadoc inherited.
    public String getPassword() {
        // Default proxy does not support authentication and then does not have
        // a password.
        return null;
    }

    // Javadoc inherited.
    public boolean useAuthorization() {
        // Default proxy does not support authentication.
        return false;
    }

    // Javadoc inherited.
    public boolean useForHost(String hostname) {
        // Default proxy does not restrict the set of hosts to which it
        // applies.
        return true;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 30-Sep-05	9649/3	matthew	VBM:2005092809 Allow proxy configuration via system properties

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 28-Jul-03	217/3	allan	VBM:2003071702 Renamed and repacked set classes to http classes

 24-Jul-03	217/1	allan	VBM:2003071702 WebDriver implementation. Intermediate commit

 ===========================================================================
*/
