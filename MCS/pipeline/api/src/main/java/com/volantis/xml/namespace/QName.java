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

package com.volantis.xml.namespace;

/**
 * Represents an XML qualified name.
 *
 * <p><strong>Warning: This is a facade provided for use by user code, not for
 * implementation. User implementations of this interface are highly likely to
 * be incompatible with future releases of the product at both binary and source
 * levels.</strong></p>
 *
 * <p>A qualified name consists of a prefix and a local name. The prefix is
 * used as a short hand for a namespace URI.</p>
 * <p><strong>Note, a null prefix is represented as an empty string, not a Java
 * null.</strong></p>
 * <p>It is common for QNames to be used as keys into HashMaps so
 * instances of this object must implement equals and hashCode. Some helper
 * methods are provided in the {@link NamespaceFactory} class.</p>
 *
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 */
public abstract class QName {

    /**
     * The volantis copyright statement
     */
    String mark = "(c) Volantis Systems Ltd 2003. ";

    /** The prefix */
    String prefix;

    /** The local name */
    String localName;

    /**
     * Create a QName instance
     * @param prefix the name prefix
     * @param localName the localname of the element
     */
    public QName(String prefix, String localName) {
        this.prefix = prefix;
        this.localName = localName;
        if ((localName == null) || (prefix == null)) {
            throw new NullPointerException(
                    "Local name and prefix must not be null");
        }
    }

    /**
     * Create a QName from a combine localname and prefix
     * @param longName the name in the form "prefix:localname"
     */
    public QName(String longName) {
        if (longName == null) {
            throw new NullPointerException(
                    "Name must not be null");
        }
        int colon = longName.indexOf(":");
        if (colon == -1) {
            prefix = "";
            localName = longName;
        } else {
            prefix = longName.substring(0, colon);
            localName = longName.substring(colon + 1);
        }
    }

    /**
     * Get the prefix.
     * @return The prefix.
     */
    public String getPrefix() {
        return prefix;
    }

    /**
     * Get the local name.
     * @return The local name.
     */
    public String getLocalName() {
        return localName;
    }

    /**
     * Check whether this QName is equal to the other object.
     * <p>This QName is equal to the other object if that other object is
     * an instance of this interface, the local names of both are equal and
     * the prefixes of both are equal.</p>
     * @param o The object to check against.
     * @return True if the objects are equal and false otherwise.
     */
    public boolean equals(Object o) {

        boolean eq = false;

        if (o != null) {
            if (o == this) {
                eq = true;
            } else if (o instanceof QName) {
                QName ei = (QName)o;
                String ln = ei.getLocalName();
                String pfx = ei.getPrefix();
                eq = ln.equals(localName) &&
                        (null == pfx) ? (null == prefix) : pfx.equals(prefix);
            }
        }
        return eq;
    }

    /**
     * Return the hash code of this QName.
     * <p>The returned hashCode is calculated from the hashCodes of the
     * prefix and the local name.
     * @return The hash code of this QName.
     */
    public int hashCode() {
        return ((prefix == null) ? 0 : prefix.hashCode()) +
                ((localName == null) ? 0 : localName.hashCode());
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 21-Aug-03	388/1	doug	VBM:2003082103 Fixed null pointer problem with unprefixed function expressions

 31-Jul-03	222/1	philws	VBM:2003071802 New pipeline API and implementation of the equals and not equals expression feature

 25-Jul-03	242/5	steve	VBM:2003072310 Added single parameter constructor for QName

 25-Jul-03	242/3	steve	VBM:2003072310 Replaced pauls NamespacePrefixTracker and added PublicAPI tags

 25-Jul-03	242/1	steve	VBM:2003072310 Implement namespace package and refactor exitsting code to fit it

 16-Jul-03	208/1	doug	VBM:2003071603 Added as part of Pipeline public API

 ===========================================================================
*/
