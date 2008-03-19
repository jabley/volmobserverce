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
package com.volantis.shared.net.http.headers;

import com.volantis.shared.net.http.SimpleHTTPMessageEntityIdentity;
import com.volantis.shared.net.http.HTTPMessageEntityIdentity;

/**
 * {@link HTTPMessageEntityIdentity} for headers.
 */
public class HeaderIdentity extends SimpleHTTPMessageEntityIdentity {

    /**
     * Creates a <code>HeaderIdentity</code> instance.
     * @param name the name of the header
     */
    public HeaderIdentity(String name) {
        super(name, Header.class);
    }

    // javadoc inherited
    public boolean identityEquals(HTTPMessageEntityIdentity identity) {
        boolean equals = identity != null &&
                getClass().equals(identity.getClass());

        if (equals) {
            HeaderIdentity header = (HeaderIdentity)identity;

            // Header names are not case sensitive.
            equals = getName().equalsIgnoreCase(header.getName()) &&
                    getObjectClass().equals(header.getObjectClass());
        }
        return equals;
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 13-Jul-04	751/3	doug	VBM:2004061405 Refactored WEB Driver so that all requests are performed via a generic interface allowing different HTTP frameworks to be plugged in

 12-Jul-04	751/1	doug	VBM:2004061405 Refactored WEB Driver so that all requests are performed via a generic interface allowing different HTTP frameworks to be plugged in

 ===========================================================================
*/
