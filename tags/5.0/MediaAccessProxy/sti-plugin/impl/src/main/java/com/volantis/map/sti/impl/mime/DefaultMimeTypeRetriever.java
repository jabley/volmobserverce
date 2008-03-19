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
 * (c) Volantis Systems Ltd 2007. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.map.sti.impl.mime;

import com.volantis.map.common.param.MissingParameterException;
import com.volantis.map.common.param.ParameterNames;
import com.volantis.map.operation.ResourceDescriptor;
import com.volantis.map.retriever.Representation;
import com.volantis.map.retriever.ResourceRetriever;
import com.volantis.map.retriever.ResourceRetrieverException;
import com.volantis.map.sti.mime.MimeTypeRetriever;
import com.volantis.map.sti.mime.MimeTypeRetrieverException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Retrieves MIME type out of the ResponseDescriptor.
 */
public class DefaultMimeTypeRetriever implements MimeTypeRetriever {

    private ResourceRetriever resourceRetriever;

    public DefaultMimeTypeRetriever(ResourceRetriever resourceRetriever) {
        this.resourceRetriever = resourceRetriever;
    }
    
    /**
     * 
     * @param descriptor
     * @return
     * @throws MimeTypeRetrieverException
     */
    public String retrieveMIMEType(ResourceDescriptor descriptor) throws MimeTypeRetrieverException {
        try {
            String urlString = (String) descriptor.getInputParameters()
                .getParameterValue(ParameterNames.SOURCE_URL);
            if (urlString == null) {
                throw new MimeTypeRetrieverException("no source url provided");
            }

            URL url = new URL(urlString);
        
            Representation representation = 
                resourceRetriever.execute(url, null);
            
            return representation.getFileType();
            
        } catch (MalformedURLException e) {
            throw new MimeTypeRetrieverException(e);
        } catch (ResourceRetrieverException e) {
            throw new MimeTypeRetrieverException(e);
        } catch (IOException e) {
            throw new MimeTypeRetrieverException(e);
        } catch (MissingParameterException e) {
            throw new MimeTypeRetrieverException(e);
        }
    }

}
