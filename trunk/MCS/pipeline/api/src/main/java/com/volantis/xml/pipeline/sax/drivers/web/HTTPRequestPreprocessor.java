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
package com.volantis.xml.pipeline.sax.drivers.web;

import com.volantis.shared.net.http.HTTPMessageEntities;

/**
 * An interface that allows implementations to preprocess request headers
 * before the Web driver processes them in the normal way.
 */
public interface HTTPRequestPreprocessor {
    /**
     * Allows clients to preprocess the message entities that will be used to
     * perform an HTTP request
     * @param headers will contain the request headers that will be used
     * to execute the request
     * @param cookies will contain the request cookies
     * @param parameters will contain the request parameters
     * @param url the request url.
     * @return the potentially modified URL
     */
    public String preprocessRequest(HTTPMessageEntities headers,
                                    HTTPMessageEntities cookies,
                                    HTTPMessageEntities parameters,
                                    String url);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 07-Sep-04	865/1	doug	VBM:2004090707 Add web driver request preprocessing

 ===========================================================================
*/
