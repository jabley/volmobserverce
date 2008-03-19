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

package com.volantis.shared.net.http;

import our.apache.commons.httpclient.HostConfiguration;
import our.apache.commons.httpclient.HttpConnection;
import our.apache.commons.httpclient.URI;
import our.apache.commons.httpclient.protocol.Protocol;

import java.net.InetAddress;

/**
 * Wrapper for HostConfiguration to allow us to have the original
 * HostConfiguration use a Protocol of our choice. This class is for
 * using the 1.4 jdk Socket.connect() when available and required.
 */
class HostConfigurationWrapper
        extends HostConfiguration {

    final HostConfiguration delegate;
    final Protocol protocol;

    public Object clone() {
        HostConfigurationWrapper clone =
                new HostConfigurationWrapper(
                        (HostConfiguration) delegate.clone(),
                        protocol);
        return clone;
    }

    public boolean hostEquals(HttpConnection httpConnection) {
        return delegate.hostEquals(httpConnection);
    }

    public boolean proxyEquals(HttpConnection httpConnection) {
        return delegate.proxyEquals(httpConnection);
    }

    public boolean isHostSet() {
        return delegate.isHostSet();
    }

    public void setHost(String s, int i, String s1) {
        delegate.setHost(s, i, s1);
    }

    public void setHost(String s, String s1, int i, Protocol protocol) {
        delegate.setHost(s, s1, i, protocol);
    }

    public void setHost(String s, int i, Protocol protocol) {
        delegate.setHost(s, i, protocol);
    }

    public void setHost(String s, int i) {
        delegate.setHost(s, i);
    }

    public void setHost(String s) {
        delegate.setHost(s);
    }

    public void setHost(URI uri) {
        delegate.setHost(uri);
    }

    public String getHostURL() {
        return delegate.getHostURL();
    }

    public String getHost() {
        return delegate.getHost();
    }

    public String getVirtualHost() {
        return delegate.getVirtualHost();
    }

    public int getPort() {
        return delegate.getPort();
    }

    public Protocol getProtocol() {
        return protocol;
    }

    public boolean isProxySet() {
        return delegate.isProxySet();
    }

    public void setProxy(String s, int i) {
        delegate.setProxy(s, i);
    }

    public String getProxyHost() {
        return delegate.getProxyHost();
    }

    public int getProxyPort() {
        return delegate.getProxyPort();
    }

    public void setLocalAddress(InetAddress inetAddress) {
        delegate.setLocalAddress(inetAddress);
    }

    public InetAddress getLocalAddress() {
        return delegate.getLocalAddress();
    }

    public boolean equals(Object o) {
        return delegate.equals(o);
    }

    public int hashCode() {
        return delegate.hashCode();
    }

    HostConfigurationWrapper(
            HostConfiguration delegate,
            Protocol protocol) {
        this.delegate = delegate;
        this.protocol = protocol;
    }
}
