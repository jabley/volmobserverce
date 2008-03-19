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

/**
 * Encapsulates the identity of an HTTPMessageEntityIdentity whose identity is
 * just the name.
 */
public class SimpleHTTPMessageEntityIdentity
        implements HTTPMessageEntityIdentity {

    /**
     * The name of the HTTPMessageEntity identified by this identity.
     */
    private String name;

    /**
     * The class of this object identified by this identity.
     */
    private Class objectClass;

    /**
     * Create a new SimpleHTTPMessageEntityIdentity with the specified name,
     * domain and path.
     * @param name The name of the HTTPMessageEntityIdentity this identity
     * identifies.
     * @param objectClass the class that this identity is identifying
     */
    public SimpleHTTPMessageEntityIdentity(String name,
                                           Class objectClass) {
        if (name == null) {
            throw new IllegalArgumentException("name cannot be null");
        }
        if (name.length() == 0) {
            throw new IllegalArgumentException("name cannot be 0 length.");
        }
        if (objectClass == null) {
            throw new IllegalArgumentException("objectClass cannot be null.");
        }
        this.name = name;
        this.objectClass = objectClass;
    }

    // javadoc inherited
    public boolean identityEquals(HTTPMessageEntityIdentity identity) {
        boolean equals = identity != null &&
                getClass().equals(identity.getClass());

        if (equals) {
            SimpleHTTPMessageEntityIdentity simple =
                    (SimpleHTTPMessageEntityIdentity)identity;

            equals = getName().equals(simple.getName()) &&
                    getObjectClass().equals(simple.getObjectClass());
        }
        return equals;
    }

    // javadoc inherited
    public boolean equals(Object o) {
        return o instanceof SimpleHTTPMessageEntityIdentity &&
                identityEquals((HTTPMessageEntityIdentity)o);
    }

    // javadoc inherited
    public int hashCode() {
        return getObjectClass().hashCode() + getName().hashCode();
    }

    /**
     * Get the name of the HTTPMessageEntity identified by this identity.
     * @return The name.
     */
    protected String getName() {
        return name;
    }

    /**
     * Get the class of the HTTPMessageEntity identified by this identity.
     * @return The objectClass.
     */
    protected Class getObjectClass() {
        return objectClass;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 13-Jul-04	751/4	doug	VBM:2004061405 Refactored WEB Driver so that all requests are performed via a generic interface allowing different HTTP frameworks to be plugged in

 12-Jul-04	751/2	doug	VBM:2004061405 Refactored WEB Driver so that all requests are performed via a generic interface allowing different HTTP frameworks to be plugged in

 31-Jul-03	217/3	allan	VBM:2003071702 Made HTTPMessageEntities into a set.

 31-Jul-03	217/1	allan	VBM:2003071702 Add and use identities for HTTPMessageEntity objects.

 ===========================================================================
*/
