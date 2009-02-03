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

package com.volantis.shared.servlet.http;

import com.volantis.shared.net.http.HTTPMessageEntityIdentity;
import com.volantis.shared.net.http.cookies.Cookie;
import com.volantis.shared.net.http.cookies.CookieIdentity;
import com.volantis.shared.net.http.cookies.CookieVersion;

import java.io.IOException;


/**
 * A Cookie that wraps a javax.servlet.http.Cookie.
 */
public class HTTPServletCookie implements Cookie {

    /**
     * The wrapped cookie
     */
    private transient javax.servlet.http.Cookie cookie;

    /**
     * Construct a new HTTPServletCookie with the given name and a null
     * domain and path.
     * @param name The name.
     */
    public HTTPServletCookie(String name) {
        this(name, null, null);
    }

    /**
     * Construct a new HTTPServletCookie with the given name, domain and path.
     * @param name The name.
     * @param domain The domain.
     * @param path The path.
     */
    public HTTPServletCookie(String name, String domain, String path) {
        if (name == null) {
            throw new IllegalArgumentException("name cannot be null");
        }

        cookie = new javax.servlet.http.Cookie(name, null);
        setDomain(domain);
        setPath(path);
        // ensure the defaults are the same as for all other implementations of
        // Cookie.
        setMaxAge(-1);
        setVersion(CookieVersion.NETSCAPE);
    }

    /**
     * Handle deserializaion of this and the wrapped Cookie.
     * @param in The stream from which to read this object.
     * @throws IOException if any io error occurs.
     * @throws ClassCastException
     */
    private void readObject(java.io.ObjectInputStream in)
            throws IOException, ClassNotFoundException {

        // read default object stuff. This makes no difference here as there
        // are no serializable members but allows the class to have
        // non-transient members added later and still work (although
        // serialized objects will not be backward compatible)
        in.defaultReadObject();
        // crete Cookie with name, value args
        String name = (String) in.readObject();
        String value = (String) in.readObject();
        cookie = new javax.servlet.http.Cookie(name, value);

        // add the rest of the information to the cookie
        cookie.setVersion(in.readInt());
        cookie.setSecure(((Boolean) in.readObject()).booleanValue());
        cookie.setPath((String) in.readObject());
        cookie.setMaxAge(in.readInt());
        cookie.setDomain((String) in.readObject());
        cookie.setComment((String) in.readObject());
    }


    /**
     * Handle serializaion of this and the wrapped Cookie.
     * @param out The stream in which to write this object.
     * @throws IOException if any error occurs.
     */
    private void writeObject(java.io.ObjectOutputStream out) throws IOException{

        // Write default object stuff. This makes no difference here as there
        // are no serializable members but allows the class to have
        // non-transient members added and still work (although serialized
        // objects will not be backward compatible)
        out.defaultWriteObject();

        // write the rest of the cookie information
        out.writeObject(cookie.getName());
        out.writeObject(cookie.getValue());
        out.writeInt(cookie.getVersion());
        out.writeObject(cookie.getSecure() ? Boolean.TRUE : Boolean.FALSE);
        out.writeObject(cookie.getPath());
        out.writeInt(cookie.getMaxAge());
        out.writeObject(cookie.getDomain());
        out.writeObject(cookie.getComment());
    }

    /**
     * Set the version property.
     * @param version The version for this cookie. Must not be null.
     */
    public void setVersion(CookieVersion version) {
        cookie.setVersion(version.getNumber());
    }

    /**
     * Get the cookie specification version.
     * @return The version. This should never be null.
     */
    public CookieVersion getVersion() {
        return CookieVersion.getCookieVersion(cookie.getVersion());
    }

    //javadoc inherited
    public void setComment(String s) {
        cookie.setComment(s);
    }

    //javadoc inherited
    public String getComment() {
        return cookie.getComment();
    }

    //javadoc inherited
    public void setMaxAge(int i) {
        cookie.setMaxAge(i);
    }

    //javadoc inherited
    public int getMaxAge() {
        return cookie.getMaxAge();
    }

    //javadoc inherited
    public void setSecure(boolean b) {
        cookie.setSecure(b);
    }

    //javadoc inherited
    public boolean isSecure() {
        return getSecure();
    }

    //javadoc inherited
    protected boolean getSecure() {
        return cookie.getSecure();
    }

    //javadoc inherited
    public String getName() {
        return cookie.getName();
    }

    //javadoc inherited
    public String getPath() {
        return cookie.getPath();
    }

    //javadoc inherited
    public String getDomain() {
        return cookie.getDomain();
    }

    //javadoc inherited
    protected void setDomain(String s) {
        if (s != null) {
            cookie.setDomain(s);
        }
    }

    //javadoc inherited
    protected void setPath(String s) {
        if (s != null) {
            cookie.setPath(s);
        }
    }

    //javadoc inherited
    public void setValue(String s) {
        cookie.setValue(s);
    }

    //javadoc inherited
    public HTTPMessageEntityIdentity getIdentity() {
        return new CookieIdentity(getName(), getDomain(), getPath());
    }

    //javadoc inherited
    public String getValue() {
        return cookie.getValue();
    }



    /**
     * Construct a new HTTPServletCookie that wraps a given
     * javax.servlet.http.Cookie.
     * @param cookie The cookie to wrap.
     */
    public HTTPServletCookie(javax.servlet.http.Cookie cookie) {
        this.cookie = cookie;
    }

    /**
     * Get the wrapped javax.servlet.http.Cookie.
     * @return The wrapped cookie.
     */
    public javax.servlet.http.Cookie getServletCookie() {
        return cookie;
    }

    /**
     * Two cookies are equal if all their properties are equal. The
     * identity is used to check for two cookies that have the same identity.
     * @param o The object to compare for equality.
     * @return true if o is equal to this cookie; false otherwise.
     */
    public boolean equals(Object o) {
        boolean equals = o != null && o instanceof HTTPServletCookie;

        if (equals) {
            HTTPServletCookie cookie = (HTTPServletCookie)o;
            equals = getClass().equals(cookie.getClass()) &&
                    this.getIdentity().identityEquals(cookie.getIdentity());

            if (equals) {
                equals = getMaxAge() == cookie.getMaxAge() &&
                        getComment() == null ? cookie.getComment() == null :
                        getComment().equals(cookie.getComment()) &&
                        isSecure() == cookie.isSecure() &&
                        getVersion() == cookie.getVersion();
            }
        }

        return equals;
    }

    // javadoc inherited
    public int hashCode() {
        return getClass().hashCode() + getIdentity().hashCode() +
                (getValue() == null ? 0 : getValue().hashCode()) +
                getMaxAge() + (isSecure() ? 0 : 1) +
                (getComment() == null ? 0 : getComment().hashCode()) +
                getVersion().hashCode();
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 09-Mar-05	7337/4	matthew	VBM:2005030809 Add a CookieVersion typesafe enum and use it in the Cookie interface and its implementations

 08-Mar-05	7331/1	matthew	VBM:2005030212 Modify HTTPMessageEntity cookie implementations to allow a version number

 08-Mar-05	7284/1	matthew	VBM:2005030212 Modify HTTPMessageEntity cookie implementations to allow a version number

 27-Oct-04	5990/1	matthew	VBM:2004102621 Mark HTTPMessageEntity as Serializable and modify its javadoc

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 04-Aug-03	294/1	allan	VBM:2003070709 Fixed merge conflicts

 01-Aug-03	217/13	allan	VBM:2003071702 Rename and re-write HttpCookie

 31-Jul-03	217/9	allan	VBM:2003071702 Made HTTPMessageEntities into a set.

 31-Jul-03	217/7	allan	VBM:2003071702 Add and use identities for HTTPMessageEntity objects.

 28-Jul-03	217/5	allan	VBM:2003071702 Renamed and repacked set classes to http classes

 24-Jul-03	217/3	allan	VBM:2003071702 WebDriver implementation. Intermediate commit

 23-Jul-03	230/1	allan	VBM:2003072101 Restructure cookies, headers and request parameters and their containers. Remove PossiblyImmutable and HeaderConversions. Rename HttpFactory to HTTPFactory.

 18-Jul-03	220/5	steve	VBM:2003071705 Removed unneccessary declarations of ImmutableObjectException

 18-Jul-03	220/3	steve	VBM:2003071705 More PossiblyImmutable refactoring

 18-Jul-03	220/1	steve	VBM:2003071705 Renamed mutableCopy to createMutableCopy

 15-Jul-03	197/1	steve	VBM:2003071501 Cookie and environment implementation

 11-Jul-03	181/3	steve	VBM:2003070802 rework fixes

 11-Jul-03	181/1	steve	VBM:2003070802 Environment implementation

 ===========================================================================
*/
