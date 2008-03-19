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

import com.volantis.shared.net.http.headers.HeaderImpl;
import com.volantis.shared.net.http.headers.HeaderIdentity;
import com.volantis.shared.net.http.HTTPMessageEntityIdentity;

public class WebRequestHeader extends HeaderImpl
        implements DerivableHTTPMessageEntity {

    /**
     * The [derivable] from property.
     */
    private String from;

    /**
     * Set the name of this WebRequestCookie.
     * @param name The name.
     */
    public void setName(String name) {
        super.setName(name);
    }

    /**
     * Get the from property.
     * @return The from.
     */
    public String getFrom() {
        return from;
    }

    /**
     * Set the from property
     * @param from The from.
     */
    public void setFrom(String from) {
        this.from = from;
    }

    // javadoc inherited
    public HTTPMessageEntityIdentity getFromIdentity() {
        return new HeaderIdentity(getFrom());
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 12-Jul-04	751/1	doug	VBM:2004061405 Refactored WEB Driver so that all requests are performed via a generic interface allowing different HTTP frameworks to be plugged in

 31-Jul-03	217/6	allan	VBM:2003071702 Add and use identities for HTTPMessageEntity objects.

 28-Jul-03	217/4	allan	VBM:2003071702 Renamed and repacked set classes to http classes

 24-Jul-03	217/1	allan	VBM:2003071702 WebDriver implementation. Intermediate commit

 ===========================================================================
*/
