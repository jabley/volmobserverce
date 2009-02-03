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

import com.volantis.pipeline.localization.LocalizationFactory;
import com.volantis.shared.net.http.HTTPMessageEntityIdentity;
import com.volantis.shared.net.http.HTTPMessageEntityImpl;
import com.volantis.synergetics.log.LogDispatcher;

/**
 * An implementation of Cookie.
 */
public class CookieImpl extends HTTPMessageEntityImpl implements Cookie {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(CookieImpl.class);

    private boolean secure;

    private String comment;

    private String path;

    private String domain;
    private int maxAge = -1;

    /**
     * The cookie specification version.
     */
    private CookieVersion version = CookieVersion.NETSCAPE;

    /**
     * A constructor that relies on specializations to call
     * setName(), setDomain() and setPath().
     */
    protected CookieImpl() {
    }

    /**
     * Create a new CookieImpl with the specified name, domain and path.
     * @param name The cookie name.
     * @param domain The cookie domain - can be null.
     * @param path The cookie path - can be null.
     */
    public CookieImpl(String name, String domain, String path) {
        super(name);
        setDomain(domain);
        setPath(path);
    }

    // javadoc inherited
    public boolean isSecure() {
        return secure;
    }

    // javadoc inherited
    public void setSecure(boolean secure) {
        this.secure = secure;
    }

    // javadoc inherited
    public String getComment() {
        return comment;
    }

    // javadoc inherited
    public void setComment(String comment) {
        this.comment = comment;
    }

    // javadoc inherited
    public String getPath() {
        return path;
    }

    /**
     * Specifies a path for the cookie to which the client should return the
     * cookie. The cookie is visible to all the pages in the directory
     * specified, and all the  pages in that directory's subdirectories. A
     * cookie's path must include the servlet that set the cookie, for example,
     * /catalog, which makes the cookie visible to all directories on the server
     * under /catalog.
     * @param path a String specifying a path
     */
    protected void setPath(String path) {
        this.path = path;
    }

    // javadoc inherited
    public String getDomain() {
        return domain;
    }

    /**
     * Specifies the domain within which this cookie should be presented.
     * The form of the domain name is specified by RFC 2109. A domain name
     * begins with a dot (.foo.com) and means that the cookie is visible to
     * servers in a specified Domain Name System (DNS) zone (for example,
     * www.foo.com, but not a.b.foo.com). By default, cookies are only returned
     * to the server that sent them.
     * @param domain a String containing the domain name within which this cookie is visible;
     * form is according to RFC 2109
     */
    protected void setDomain(String domain) {
        this.domain = domain;
    }

    // javadoc inherited
    public int getMaxAge() {
        return maxAge;
    }

    // javadoc inherited
    public void setMaxAge(int maxAge) {
        this.maxAge = maxAge;
    }

    // javadoc inherited
    public HTTPMessageEntityIdentity getIdentity() {
        return new CookieIdentity(getName(), getDomain(), getPath());
    }

    // javadoc inherited
    public void setVersion(CookieVersion version) {
        // do nothing if version is null so we ensure we always have a valid
        // version number.
        if(version != null) {

            if(logger.isDebugEnabled()) {
                logger.debug("Setting cookie version to " + version);
            }
            this.version = version;
        } else {
            if(logger.isDebugEnabled()) {
                logger.debug("Unable to set cookie version: null parameter");
            }
        }
    }

    // javadoc inherited
    public CookieVersion getVersion() {
        return version;
    }

    // javadoc inherited
    public boolean equals(Object o) {
        boolean equals = super.equals(o);

        if (equals) {
            Cookie cookie = (Cookie)o;
            equals = getMaxAge() == cookie.getMaxAge() &&
                    (getComment() == null ? cookie.getComment() == null :
                    getComment().equals(cookie.getComment())) &&
                    isSecure() == cookie.isSecure() &&
                    getVersion() == cookie.getVersion();
        }

        return equals;
    }

    // javadoc inherited
    public int hashCode() {
        return super.hashCode() + getMaxAge() + (isSecure() ? 0 : 1) +
                (getComment() == null ? 0 : getComment().hashCode()) +
                getVersion().hashCode();

    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 09-Mar-05	7337/3	matthew	VBM:2005030809 Add a CookieVersion typesafe enum and use it in the Cookie interface and its implementations

 08-Mar-05	7331/1	matthew	VBM:2005030212 Modify HTTPMessageEntity cookie implementations to allow a version number

 08-Mar-05	7284/1	matthew	VBM:2005030212 Modify HTTPMessageEntity cookie implementations to allow a version number

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 09-Jul-04	769/2	doug	VBM:2004070502 Improved integration tests for the Web Driver

 31-Jul-03	217/7	allan	VBM:2003071702 Made HTTPMessageEntities into a set.

 31-Jul-03	217/5	allan	VBM:2003071702 Add and use identities for HTTPMessageEntity objects.

 28-Jul-03	217/3	allan	VBM:2003071702 Renamed and repacked set classes to http classes

 24-Jul-03	217/1	allan	VBM:2003071702 WebDriver implementation. Intermediate commit

 23-Jul-03	230/1	allan	VBM:2003072101 Restructure cookies, headers and request parameters and their containers. Remove PossiblyImmutable and HeaderConversions. Rename HttpFactory to HTTPFactory.

 ===========================================================================
*/
