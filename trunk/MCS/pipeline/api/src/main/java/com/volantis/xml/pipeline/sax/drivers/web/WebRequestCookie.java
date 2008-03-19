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

import com.volantis.shared.net.http.cookies.CookieImpl;
import com.volantis.shared.net.http.cookies.CookieIdentity;
import com.volantis.shared.net.http.cookies.CookieVersion;
import com.volantis.shared.net.http.HTTPMessageEntityIdentity;

/**
 * A DerivableHTTPMessageEntity version of a Cookie.
 */
public class WebRequestCookie extends CookieImpl
        implements DerivableHTTPMessageEntity {

    /**
     * Flag to indicate whether the secure property has been set.
     */
    private boolean secureHasBeenSet;

    /**
     * Flag to indicate whether the maxAge property has been set.
     */
    private boolean maxAgeHasBeenSet;

    /**
     * Flag to indicate if the version property has been set.
     */
    private boolean versionHasBeenSet;

    /**
     * The [derivable] from property.
     */
    private String from;

    /**
     * Set the name of this WebRequestCookie. Overridden from parent so
     * that it can be made public.
     * @param name The name.
     */
    public void setName(String name) {
        super.setName(name);
    }

    /**
     * Set the domain of this WebRequestCookie. Overridden from parent so
     * that it can be made public.
     * @param domain The domain.
     */
    public void setDomain(String domain) {
        super.setDomain(domain);
    }

    /**
     * Set the path of this WebRequestCookie. Overridden from parent so
     * that it can be made public.
     * @param path The path.
     */
    public void setPath(String path) {
        super.setPath(path);
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
        return new CookieIdentity(getFrom(), getDomain(), getPath());
    }

    /**
     * Override setSecure() so that we can tell if it has been set.
     */
    // rest of javadoc inherited
    public void setSecure(boolean secure) {
        super.setSecure(secure);
        secureHasBeenSet = true;
    }

    /**
     * Override setMaxAge() so that we can tell if it has been set.
     */
    // rest of javadoc inherited
    public void setMaxAge(int maxAge) {
        super.setMaxAge(maxAge);
        maxAgeHasBeenSet = true;
    }

    /**
     * Override version() so that we can tell if it has been set. If null
     * is used as the parameter then it is ignored and this cookie will not
     * be marked as having its version set.
     */
    // rest of javadoc inherited
    public void setVersion(CookieVersion version) {
        if (version != null) {
            super.setVersion(version);
            versionHasBeenSet = true;
        }
    }

    /**
     * Will return true if the secure property has been set; false otherwise.
     * @return secureHasBeenSet.
     */
    public boolean secureHasBeenSet() {
        return secureHasBeenSet;
    }

    /**
     * Will return true if the maxAge property has been set; false otherwise.
     * @return secureHasBeenSet.
     */
    public boolean maxAgeHasBeenSet() {
        return maxAgeHasBeenSet;
    }

    /**
     * @return true is the version property has been set, false otherwise.
     */
    public boolean versionHasBeenSet() {
        return versionHasBeenSet;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 09-Mar-05	7337/1	matthew	VBM:2005030809 Add a CookieVersion typesafe enum and use it in the Cookie interface and its implementations

 08-Mar-05	7331/1	matthew	VBM:2005030212 Modify HTTPMessageEntity cookie implementations to allow a version number

 08-Mar-05	7284/1	matthew	VBM:2005030212 Modify HTTPMessageEntity cookie implementations to allow a version number

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 31-Jul-03	238/1	byron	VBM:2003072309 Create the adapter process for parent task v4

 31-Jul-03	217/7	allan	VBM:2003071702 Add and use identities for HTTPMessageEntity objects.

 28-Jul-03	217/5	allan	VBM:2003071702 Renamed and repacked set classes to http classes

 ===========================================================================
*/
