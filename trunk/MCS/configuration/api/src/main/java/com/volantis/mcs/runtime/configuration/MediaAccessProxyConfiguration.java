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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.runtime.configuration;

/**
 *  The configuration for the Media Access Proxy.
 */
public class MediaAccessProxyConfiguration {

    /**
     * The URL prefix to access MAP resources.
     */
    private String urlPrefix;

    /**
     * Returns the value of the URL prefix.
     *
     * @return The URL prefix as a string.
     */
    public String getUrlPrefix() {
        return urlPrefix;
    }

    /**
     * Sets the value of the URL prefix.
     *
     * @param urlPrefix The new URL prefix.
     */
    public void setUrlPrefix(final String urlPrefix) {
        this.urlPrefix = urlPrefix;
    }
}
