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
 * Default implementation of the WebDriverRequest interface
 */
public class WebDriverRequestImpl implements WebDriverRequest {

    /**
     * The volantis copyright statement
     */
    private static final String mark = "(c) Volantis Systems Ltd 2003.";

    /**
     * The CookieJar associated with this request.
     */
    private HTTPMessageEntities cookies;

    /**
     * The Headers associated with this request.
     */
    private HTTPMessageEntities headers;

    /**
     * The parameters associtated with this request.
     */
    private HTTPMessageEntities parameters;

    // javadoc inherited
    public void setCookies(HTTPMessageEntities cookies) {
        this.cookies = cookies;
    }

    // javadoc inherited
    public HTTPMessageEntities getCookies() {
        return cookies;
    }

    // javadoc inherited
    public void setHeaders(HTTPMessageEntities headers) {
        this.headers = headers;
    }

    // javadoc inherited
    public HTTPMessageEntities getHeaders() {
        return headers;
    }

    // javadoc inherited
    public void setRequestParameters(HTTPMessageEntities parameters) {
        this.parameters = parameters;
    }

    // javadoc inherited
    public HTTPMessageEntities getRequestParameters() {
        return parameters;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 28-Jul-03	217/1	allan	VBM:2003071702 Renamed and repacked set classes to http classes

 23-Jul-03	230/1	allan	VBM:2003072101 Restructure cookies, headers and request parameters and their containers. Remove PossiblyImmutable and HeaderConversions. Rename HttpFactory to HTTPFactory.

 18-Jul-03	220/3	steve	VBM:2003071705 More PossiblyImmutable refactoring

 18-Jul-03	220/1	steve	VBM:2003071705 Renamed mutableCopy to createMutableCopy

 17-Jul-03	215/3	steve	VBM:2003070806 Removed throws ImmutableObjectException declarations

 17-Jul-03	215/1	steve	VBM:2003070806 Implement we driver

 17-Jul-03	200/1	doug	VBM:2003070806 Introduced the Web Driver support classes

 ===========================================================================
*/
