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
package com.volantis.xml.pipeline.sax.convert;

import com.volantis.xml.pipeline.sax.XMLPipelineContext;

import java.net.URL;

/**
 * This is a test-only URLConverter implementation for integration testing.
 */
public class TestURLCGenerator implements URLConverter {
    // javadoc inherited
    public String toURLC(XMLPipelineContext pipelineContext,
                         String imageURL,
                         String serverURL) throws URLConversionException {
        String result = null;

        try {
            StringBuffer urlc = new StringBuffer(serverURL);
            URL url = new URL(imageURL);
            String path = url.getPath();
            String query = url.getQuery();
            String ref = url.getRef();
            int port = url.getPort();

            if (port == -1) {
                port = 80;
            }

            urlc.append(path);

            if (ref != null) {
                urlc.append('#').append(ref);
            }

            urlc.append('?');

            if (query != null) {
                urlc.append(query).append('&');
            }

            urlc.append("tf.source.host=").append(url.getHost()).
                append("&tf.source.port=").append(port);

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

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 07-Aug-03	299/5	philws	VBM:2003080504 Provide the pipeline context to the URLConverter on URLC conversion

 07-Aug-03	299/3	philws	VBM:2003080504 Remove the relativeWidth and maxFileSize attributes from the URL to URLC converter following architectural change

 06-Aug-03	299/1	philws	VBM:2003080504 Pipeline work for the DSB convertImageURLToDMS process

 ===========================================================================
*/
