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

import com.volantis.shared.net.http.HTTPMessageEntity;

/**
 * Interface representing a cookie which is a piece of information which is shared
 * between the web browser and an HTTP based web server.
 * <p>Typically a web server will create a cookies and pass it to the browser
 * using HTTP response headers. The browser will store it (in a cookies jar) and
 * will send it back to the server in HTTP request headers.</p>
 * <p>In its simplest form a cookies has a name and a value. It also has some
 * optional attributes such as path, domain, maxAge and comment.</p>
 * 
 * <b>Warning:</b>  Serialized implementaions of this interface will not be
 * compatible with future releases. The current serialization support is only
 * appropriate for short term storage.
 *
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 */
public interface Cookie extends HTTPMessageEntity {

    /**
     * Indicates to the client whether the cookie should only be sent using a secure
     * protocol, such as HTTPS or SSL. The default value is false.
     *
     * @param secure if true, sends the cookie from the client to the server only
     * when using a secure protocol; if false, sent on any protocol
     *
     */
    public void setSecure(boolean secure);

    /**
     * Returns true if the client is sending cookies only over a secure protocol, or
     * false if the client can send cookies using any protocol.
     *
     * @return false if the client can use any standard protocol; otherwise, true
     */
    public boolean isSecure();

    /**
     * Specifies a comment that describes a cookie's purpose.
     * The comment is useful if the client presents the cookie to the user.
     * Comments are not supported by Netscape Version 0 cookies.
     * @param comment a String specifying the comment to display to the user
     */
    public void setComment(String comment);

    /**
     * Returns the comment describing the purpose of this cookie, or null if the cookie
     * has no comment.
     * @return a String containing the comment, or null if none
     */
    public String getComment();

    /**
     * Returns the path on the server to which the client returns this cookie.
     * The cookie is visible to all subpaths on the server.
     * @return a String specifying a path that contains a servlet name
     */
    public String getPath();

    /**
     * Returns the domain name set for this cookie.
     * The form of the domain name is set by RFC 2109.
     * @return a String containing the domain name
     */
    public String getDomain();

    /**
     * Returns the name of the cookie. The name cannot be changed after creation.
     * @return a String specifying the cookie's name
     */
    public String getName();

    /**
     * Assigns a new value to a cookie after the cookie is created.
     * If you use a binary value, you may want to use BASE64 encoding.
     * With Version 0 cookies, values should not contain white space, brackets,
     * parentheses, equals signs, commas, double quotes, slashes, question marks, at signs,
     * colons, and semicolons. Empty values may not behave the same way on all clients.
     * @param value a String specifying the new value
     */
    public void setValue(String value);

    /**
     * Returns the value of the cookie.
     * @return a String containing the cookie's present value
     */
    public String getValue();

    /**
     * Sets the maximum age of the cookie in seconds.
     *
     * A positive value indicates that the cookie will expire after that many seconds have
     * passed. Note that the value is the maximum age when the cookie will expire, not the
     * cookie's current age.
     *
     * A negative value means that the cookie is not stored persistently and will be deleted
     * when the Web browser exits. A zero value causes the cookie to be deleted.
     * @param maxAge an integer specifying the maximum age of the cookie in seconds;
     * if negative, means the cookie is not stored; if zero, deletes the cookie
     */
    public void setMaxAge(int maxAge);

    /**
     * Returns the maximum age of the cookie, specified in seconds,
     * By default, -1 indicating the cookie will persist until client shutdown.
     * @return an integer specifying the maximum age of the cookie in seconds;
     * if negative, means the cookie persists until client shutdown
     */
    public int getMaxAge();

    /**
     * Set the version of the cookie. Version 0 corresponds to the
     * <a href="http://wp.netscape.com/newsref/std/cookie_spec.html">Netscape</a>
     * preliminary specification Version 1 corresponds to
     * <a href="http://www.w3.org/Protocols/rfc2109/rfc2109">RFC 2109</a>
     * specification. Setting the version can affect how this cookie is handled.
     *
     * @param version the specification version of this cookie. A null argument
     * will be ignored.
     */
    public void setVersion(CookieVersion version);

    /**
     * Returns the specification version of this cookie. All implementations
     * default to CookieVersion.NETSCAPE (corresponding to the netscape
     * preliminary specification).
     *
     * @return the version of this cookie. This should never return null.
     */
    public CookieVersion getVersion();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 09-Mar-05	7337/3	matthew	VBM:2005030809 Add a CookieVersion typesafe enum and use it in the Cookie interface and its implementations

 08-Mar-05	7331/1	matthew	VBM:2005030212 Modify HTTPMessageEntity cookie implementations to allow a version number

 08-Mar-05	7284/1	matthew	VBM:2005030212 Modify HTTPMessageEntity cookie implementations to allow a version number

 27-Oct-04	5990/1	matthew	VBM:2004102621 Mark HTTPMessageEntity as Serializable and modify its javadoc

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 04-Aug-03	294/1	allan	VBM:2003070709 Fixed merge conflicts

 31-Jul-03	217/7	allan	VBM:2003071702 Fixed javadoc issue with contains and containsIdentity.

 31-Jul-03	217/5	allan	VBM:2003071702 Made HTTPMessageEntities into a set.

 31-Jul-03	217/3	allan	VBM:2003071702 Add and use identities for HTTPMessageEntity objects.

 28-Jul-03	217/1	allan	VBM:2003071702 Renamed and repacked set classes to http classes

 23-Jul-03	230/1	allan	VBM:2003072101 Restructure cookies, headers and request parameters and their containers. Remove PossiblyImmutable and HeaderConversions. Rename HttpFactory to HTTPFactory.

 18-Jul-03	220/1	steve	VBM:2003071705 Removed unneccessary declarations of ImmutableObjectException

 15-Jul-03	197/1	steve	VBM:2003071501 Cookie and environment implementation

 11-Jul-03	181/1	steve	VBM:2003070802 Environment implementation

 ===========================================================================
*/
