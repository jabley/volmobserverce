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
 * Converted from com.volantis.xml.pipeline.sax.XMLQname
 * ----------------------------------------------------------------------------
 */
package com.volantis.xml.namespace;

import com.volantis.synergetics.ObjectHelper;

/**
 * Represents an XML expanded name.
 *
 * See <a href="http://www.w3.org/TR/xpath#dt-expanded-name">XPath Specification</a> for
 * the full specification of an XML expanded name.
 *
 * Note, a null namespace URI is represented as an empty string, not a Java null.
 *
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 */
public abstract class ExpandedName {

    /**
     * The volantis copyright statement
     */
    private static final String mark = "(c) Volantis Systems Ltd 2003.";

    /**
     * The local name of the element
     */
    String localName;

    /**
     * The URI of the namespace the element belongs to
     */
    String namespaceURI;

    /**
     * Create an ExpandedName instance.
     *
     * @param namespaceURI the URI of the namespace
     * @param localName the localname of the elemet
     */
    public ExpandedName(String namespaceURI, String localName) {
        this.localName = localName;
        this.namespaceURI = namespaceURI;
        if ((localName == null) || (namespaceURI == null)) {
            throw new NullPointerException(
                    "Local name and namespace URI must not be null");
        }
    }

    /**
     * Get the processNamespaceURI property.
     *
     * @return the value of the processNamespaceURI property of null if it has not
     * been set
     */
    public String getNamespaceURI() {
        return namespaceURI;
    }

    /**
     * Get the value of the processLocalName property.
     *
     * @return the value of the processLocalName property or null if it has not been
     * set.
     */
    public String getLocalName() {
        return localName;
    }

    /**
     * Return an immutable copy of this.
     *
     * <p>If this is already immutable then a reference to this class may be
     * returned to prevent the need to allocate a new object.</p>
     *
     * @return An immutable copy.
     */
    public abstract ImmutableExpandedName getImmutableExpandedName();

    /**
     * Return a mutable copy of this.
     *
     * @return A mutable copy.
     */
    public MutableExpandedName getMutableExpandedName() {
        return new MutableExpandedName(this);
    }

    // javadoc inherited from Object class
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        //TODO - equals method shouldn't use instanceof and should be
        //using a comparison of the class types. Before this is changed
        //investigation is required to see if this error is now dependant
        //on in the code.
        if ((o != null) && (o instanceof ExpandedName)) {
            ExpandedName ei = (ExpandedName)o;

            String ln = ei.getLocalName();
            String ns = ei.getNamespaceURI();

            // namespace uri & processPrefixedName may be null.
            return (ObjectHelper.equals(ln, localName) &&
                    ObjectHelper.equals(ns, namespaceURI));
        }
        return false;
    }

    // javadoc inherited from Object class
    public int hashCode() {

        return localName.hashCode() +
                ObjectHelper.hashCode(namespaceURI);

    }

    // javadoc inherited from Object class
    public String toString() {
        return "("+namespaceURI+")"+localName;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 25-Jul-03	242/3	steve	VBM:2003072310 Replaced pauls NamespacePrefixTracker and added PublicAPI tags

 25-Jul-03	242/1	steve	VBM:2003072310 Implement namespace package and refactor exitsting code to fit it

 ===========================================================================
*/
