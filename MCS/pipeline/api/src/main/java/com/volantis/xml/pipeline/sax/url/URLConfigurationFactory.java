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
package com.volantis.xml.pipeline.sax.url;

import com.volantis.shared.net.url.URLConfiguration;
import com.volantis.shared.net.url.http.HttpUrlConfiguration;
import com.volantis.xml.pipeline.sax.XMLPipelineContext;

import java.net.URL;

/**
 * Factory to create URLConfiguration objects.
 */
public class URLConfigurationFactory {

    /**
     * Header name for the list of visited MCS instances
     */
    public static final String VISITED_MCS_INSTANCES_HEADER_NAME =
        "X-Volantis-Visited-MCS-IDs";

    /**
     * Creates a URLConfiguration for the given URL using information stored in
     * the pipeline context.
     *
     * @param url the URL for which the URLConfiguration is required
     * @param context the pipeline context that contains the necessary
     * information
     * @return the built URLConfiguration object, may return null
     */
    public static URLConfiguration getURLConfiguration(
            final URL url, final XMLPipelineContext context) {

        URLConfiguration urlConfig = null;
        final String protocol = url.getProtocol();
        if ("http".equalsIgnoreCase(protocol) ||
            "https".equalsIgnoreCase(protocol)) {

            // add visited MCS instances header
            final String instances =
                (String) context.getProperty(VISITED_MCS_INSTANCES_HEADER_NAME);
            if (instances != null) {
                final HttpUrlConfiguration httpConfig =
                    new HttpUrlConfiguration(context.getDependencyContext());
                httpConfig.addHeader(
                    VISITED_MCS_INSTANCES_HEADER_NAME, instances);
                urlConfig = httpConfig;
            }
        }
        return urlConfig;
    }

}
