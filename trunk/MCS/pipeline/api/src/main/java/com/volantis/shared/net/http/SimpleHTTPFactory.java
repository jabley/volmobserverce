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
package com.volantis.shared.net.http;

import com.volantis.shared.net.http.parameters.RequestParameter;
import com.volantis.shared.net.http.parameters.RequestParameterImpl;
import com.volantis.shared.net.http.cookies.Cookie;
import com.volantis.shared.net.http.cookies.CookieImpl;
import com.volantis.shared.net.http.headers.Header;
import com.volantis.shared.net.http.headers.HeaderImpl;
import com.volantis.shared.net.http.HTTPFactory;
import com.volantis.shared.net.http.HTTPMessageEntities;

/**
 * A basic implementation of a HTTPFactory.
 */
public class SimpleHTTPFactory extends HTTPFactory {

    // javadoc inherited
    public HTTPMessageEntities createHTTPMessageEntities() {
        return new SimpleHTTPMessageEntities();
    }

    // javadoc inherited
    public Cookie createCookie(String name, String domain, String path) {
        return new CookieImpl(name, domain, path);
    }

    // javadoc inherited
    public Header createHeader(String name) {
        return new HeaderImpl(name);
    }

    // javadoc inherited
    public RequestParameter createRequestParameter(String name) {
        return new RequestParameterImpl(name);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 31-Jul-03	217/13	allan	VBM:2003071702 Add and use identities for HTTPMessageEntity objects.

 28-Jul-03	217/11	allan	VBM:2003071702 Intermediate group level changes

 28-Jul-03	217/9	allan	VBM:2003071702 Renamed and repacked set classes to http classes

 23-Jul-03	230/1	allan	VBM:2003072101 Restructure cookies, headers and request parameters and their containers. Remove PossiblyImmutable and HeaderConversions. Rename HttpFactory to HTTPFactory.

 ===========================================================================
*/
