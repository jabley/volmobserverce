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
package com.volantis.mcs.runtime.pipeline;

import com.volantis.mcs.integration.NativeURLParameterProvider;
import com.volantis.xml.pipeline.sax.XMLPipelineContext;
import com.volantis.xml.pipeline.sax.convert.URLConversionException;
import com.volantis.xml.pipeline.sax.convert.URLConverter;

import java.net.URL;

/**
 * This URL to URLC converter is Transforce specific. This must be thread-safe
 * and is (it holds no state).
 */
public class TranscoderURLConverter implements URLConverter {
    /**
     * The host parameter name. May be null.
     */
    private final String hostParam;

    /**
     * The port parameter name. May be null.
     */
    private final String portParam;

    /**
     * Initializes the new instance using the given parameters.
     *
     * @param hostParam the host parameter name. If null this defaults to the
     *                  ICS parameter name.
     * @param portParam the port parameter name. If null this defaults to the
     *                  ICS parameter name.
     */
    public TranscoderURLConverter(String hostParam, String portParam) {
        if (hostParam == null) {
            this.hostParam = NativeURLParameterProvider.SINGLETON.
                    getHostParameterName();
        } else {
            this.hostParam = hostParam;
        }

        if (portParam == null) {
            this.portParam = NativeURLParameterProvider.SINGLETON.
                    getPortParameterName();
        } else {
            this.portParam = portParam;
        }
    }

    /**
     * This method converts the given <tt>imageURL</tt> of the form:
     *
     * <p>[ [ <i>protocol</i> <b>:</b> [ <b>//</b> <i>host</i> [
     * <b>:</b><i>port</i> ] ] ] <b>/</b> ] <i>path</i> [ <b>?</b><i>query</i>
     * ] [ <b>#</b><i>ref</i> ]</p>
     *
     * <p>to</p>
     *
     * <p><tt>serverURL</tt> <b>/</b> <i>path</i> <b>?</b> [
     * <i>query</i><b>&</b> ] <b><i>host-param</i>=</b><i>host</i>
     * <b>&<i>port-param</i>=</b><i>port</i></p>
     *
     * <p>where the <i>host-param</i> and <i>port-param</i> names are the
     * values supplied to the constructor of this class.</p>
     *
     * <p>If no <i>port</i> is specified in the <tt>imageURL</tt> the default
     * port 80 is used in the output.</p>
     *
     * <p>The <tt>serverURL</tt> is expected to be well-formed. The
     * <tt>imageURL</tt> is permitted to have an empty host. No
     * <i>host-param</i> or <i>port-param</i> parameters are added.</p>
     *
     * <p>The <tt>imageURL</tt> can have a set, absolute or relative path. If
     * absolute or relative the current base URI from the pipeline context is
     * used to construct a set path.</p>
     */
    public String toURLC(XMLPipelineContext pipelineContext,
                         String imageURL,
                         String serverURL) throws URLConversionException {
        String result = null;

        try {
            // The resulting URLC starts with the specified serverURL. The
            // latter is assumed to be well-formed (it is not checked here).
            StringBuffer urlc = new StringBuffer(serverURL);

            // Use a URL to simplify processing of the imageURL components
            URL baseURL = pipelineContext.getCurrentBaseURI();
            URL url = new URL(baseURL, imageURL);
            String path = url.getPath();
            String query = url.getQuery();
            int port = url.getPort();

            // Handle a missing port specification in the imageURL
            if (port == -1) {
                port = 80;
            }

            // Strip out the "/" on the end of the serverURL if there is one
            // because the path always starts with a "/"
            if (serverURL.endsWith("/")) {
                urlc.setLength(urlc.length() - 1);
            }

            final String hostValue = url.getHost();

            // Add the path
            urlc.append(path);

            // Only add a ? if there will be following parameters
            if (query != null ||
                    (hostValue != null && hostValue.length() > 0)) {
                urlc.append('?');
            }

            // Add the query
            if (query != null) {
                urlc.append(query);
            }

            // Only add the host and port parameters if the host from the URL
            // is specified.
            if (hostValue != null && hostValue.length() > 0) {

                // Check if adding after query.
                if (query != null) {
                    urlc.append('&');
                }
                urlc.append(hostParam).append('=').append(hostValue).
                        append('&').
                        append(portParam).append('=').append(port);
            }

            result = urlc.toString();
        } catch (Exception e) {
            throw new URLConversionException(e);
        }

        return result;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 03-May-05	7984/2	pcameron	VBM:2005050306 Only add image host and port to transcoder URL when image host is specified

 18-Jan-05	6705/1	allan	VBM:2005011708 Remove the height from the width parameter

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 04-Nov-04	6109/3	philws	VBM:2004072013 Update the convertImageURLTo... pipeline processes to utilize the current pluggable asset transcoder's parameter names

 07-Aug-03	981/1	philws	VBM:2003080605 Provide Transforce PictureIQ specific pipeline URL to URLC conversion

 ===========================================================================
*/
