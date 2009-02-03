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
package com.volantis.xml.pipeline.sax.drivers.webservice;

import com.volantis.shared.net.url.URLConfiguration;
import com.volantis.shared.net.url.URLContent;
import com.volantis.shared.net.url.URLContentManager;
import com.volantis.xml.pipeline.sax.XMLPipelineContext;
import com.volantis.xml.pipeline.sax.url.URLConfigurationFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.xml.sax.InputSource;

/**
 * A class providing utility methods for use by the WSDriver classes. Some
 * or all of these methods may be useful outside the wsdriver package but at
 * the moment we can't think of a better place for this.
 */
public class WSDriverUtils {

    /**
     * Create an InputSource from a URI.
     *
     * @param uri     the URI as a string
     * @param manager the content manager to retrieve the content
     * @param context the pipeline context to build the url configuration
     * @return The InputSource based on the URI.
     * @throws java.io.IOException If there is a problem with the URI.
     */
    public static InputSource createURIInputSource(final String uri,
                final URLContentManager manager,
                final XMLPipelineContext context)
            throws IOException {

        return createURLInputSource(new URL(uri), manager, context);
    }

    /**
     * Create an InputSource from a URL.
     *
     * @param url     the URL for which the InputSource is required
     * @param manager the content manager to retrieve the content
     * @param urlConfig additional information to get the content, may be null
     * @return The InputSource based on the URL.
     * @throws java.io.IOException If there is a problem with the URL.
     */
    public static InputSource createURLInputSource(
            URL url, URLContentManager manager, URLConfiguration urlConfig)
            throws IOException {

        // Get the content, use the default timeout.
        URLContent content = manager.getURLContent(url, null, urlConfig);

        InputStream inputStream = content.getInputStream();

        InputSource result = new InputSource(inputStream);
        result.setSystemId(url.toExternalForm());

        return result;
    }

    /**
     * Create an InputSource from a URL.
     *
     * @param url the URL for which the InputSource is required
     * @param manager the content manager to retrieve the content
     * @param context the pipeline context to build the url configuration
     * @return The InputSource based on the URL.
     * @throws java.io.IOException If there is a problem with the URL.
     */
    public static InputSource createURLInputSource(final URL url,
                final URLContentManager manager,
                final XMLPipelineContext context)
            throws IOException {

        final URLConfiguration urlConfig =
            URLConfigurationFactory.getURLConfiguration(url, context);
        return createURLInputSource(url, manager, urlConfig);
    }
}
