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

import com.volantis.shared.net.http.cookies.Cookie;
import com.volantis.shared.net.http.headers.Header;
import com.volantis.shared.net.http.parameters.RequestParameter;

import java.io.Serializable;

/**
 * A class for creating Http objects.
 *
 * <p><strong>Warning: This is a facade provided for use by user code, not for
 * implementation. User implementations of this class are highly likely to
 * be incompatible with future releases of the product at both binary and source
 * levels.</strong></p>
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 */
public abstract class HTTPFactory implements Serializable {

    /**
     * The HTTPFactory instance for this class.
     */
    private static HTTPFactory netFactory = new SimpleHTTPFactory();

    /**
     * Get the default instance of this factory.
     * @return The default instance of this factory.
     */
    public static HTTPFactory getDefaultInstance() {
        return netFactory;
    }

    /**
     * Create a HTTPMessageEntities.
     * @return A new instance of HTTPMessageEntities.
     */
    public abstract HTTPMessageEntities createHTTPMessageEntities();

    /**
     * Create and return a Cookie.

     * The form of the domain name is specified by RFC 2109. A domain name
     * begins with a dot (.foo.com) and means that the cookie is visible to
     * servers in a specified Domain Name System (DNS) zone (for example,
     * www.foo.com, but not a.b.foo.com). By default, cookies are only returned
     * to the server that sent them.
     *
     * The cookie path must include the servlet that set the cookie, for
     * example, /catalog, which makes the cookie visible to all directories on
     * the server under /catalog.
     * @param name The Cookie name.
     * @param domain The cookie domain - can be null.
     * @param path The cookie path - can be null.
     * @return The created Cookie.
     */
    public abstract Cookie createCookie(String name, String domain, String path);

    /**
     * Create and return a Header.
     * @param name The Header name.
     * @return The created Header.
     */
    public abstract Header createHeader(String name);

    /**
     * Create and return a WebRequestParameter.
     * @return The created WebRequestParameter.
     */
    public abstract RequestParameter createRequestParameter(String name);

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 31-Jul-03	217/11	allan	VBM:2003071702 Add and use identities for HTTPMessageEntity objects.

 28-Jul-03	217/9	allan	VBM:2003071702 Intermediate group level changes

 28-Jul-03	217/7	allan	VBM:2003071702 Renamed and repacked set classes to http classes

 24-Jul-03	217/1	allan	VBM:2003071702 WebDriver implementation. Intermediate commit

 23-Jul-03	230/1	allan	VBM:2003072101 Restructure cookies, headers and request parameters and their containers. Remove PossiblyImmutable and HeaderConversions. Rename HttpFactory to HTTPFactory.

 ===========================================================================
*/
