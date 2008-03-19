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
package com.volantis.shared.net.http.cookies;

import com.volantis.shared.net.http.HTTPMessageEntityIdentity;
import com.volantis.shared.net.http.SimpleHTTPMessageEntityIdentity;

/**
 * Encapsulate the identity of a Cookie.
 */
public class CookieIdentity extends SimpleHTTPMessageEntityIdentity {

    /**
     * The domain of the Cookie identified by this identity.
     */
    private String domain;

    /**
     * The path of the Cookie identified by this identity.
     */
    private String path;

    /**
     * Create a new CookieIdentity with the specified name, domain and path.
     * @param name The cookie name.
     * @param domain The cookie domain - can be null.
     * @param path The cookie path - can be null.
     */
    public CookieIdentity(String name, String domain, String path) {
        super(name, Cookie.class);
        this.domain = domain;
        this.path = path;
    }

    // javadoc inherited.
    public boolean identityEquals(HTTPMessageEntityIdentity identity) {
        boolean equals = super.identityEquals(identity);
        if (equals) {
            CookieIdentity cookieIdentity = (CookieIdentity)identity;

            // Check domain
            equals = getDomain() == null ? cookieIdentity.getDomain() == null :
                    getDomain().equals(cookieIdentity.getDomain());

            if (equals) {
                // Check path
                equals = getPath() == null ? cookieIdentity.getPath() == null :
                        getPath().equals(cookieIdentity.getPath());
            }

        }

        return equals;
    }

    // javadoc inherited
    public int hashCode() {
        return super.hashCode() +
                (getDomain() == null ? 0 : getDomain().hashCode()) +
                (getPath() == null ? 0 : getPath().hashCode());
    }

    /**
     * Get the domain of the Cookie identified by this identity.
     * @return The domain.
     */
    protected String getDomain() {
        return domain;
    }

    /**
     * Get the path of the Cookie identified by this identity.
     * @return The path.
     */
    protected String getPath() {
        return path;
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 31-Jul-03	217/3	allan	VBM:2003071702 Made HTTPMessageEntities into a set.

 31-Jul-03	217/1	allan	VBM:2003071702 Add and use identities for HTTPMessageEntity objects.

 ===========================================================================
*/
