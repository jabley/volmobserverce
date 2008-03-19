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

package com.volantis.xml.pipeline.sax.convert;

/**
 * Permits the RelativeToAbsoluteURL and URLToURLC process to be
 * configured to convert a URL found in a given attribute on a given
 * element within a given namespace (or all namespaces).
 */
public class ConverterTuple {
    /**
     *  Volantis copyright mark.
     */
    private static String mark
            = "(c) Volantis Systems Ltd 2003. ";

    /**
     * The namespace URI in which the element must exist or null for "all
     * namespaces".
     */
    private String namespaceURI;

    /**
     * The element on which the attribute must exist.
     */
    private String element;

    /**
     * The attribute in which a URL will be found.
     */
    private String attribute;

    /**
     * Initializes the new instance with the given parameters.
     *
     * @param namespaceURI         the namespace URI in which the element
     *                             must exist, or null for "all namespaces"
     * @param element              the element on which the attribute must
     *                             exist
     * @param attribute            the attribute in which a URL will be
     *                             found
     */
    public ConverterTuple(String namespaceURI,
                          String element,
                          String attribute) {

        if ((element == null) ||
                "".equals(element)) {
            throw new IllegalArgumentException(
                    "A non-null/non-empty element name is required");
        } else if ((attribute == null) ||
                "".equals(attribute)) {
            throw new IllegalArgumentException(
                    "A non-null/non-empty attribute name is required");
        }

        this.namespaceURI = namespaceURI;
        this.element = element;
        this.attribute = attribute;
    }

    /**
     * Returns the namespace URI, or null for "all namespaces".
     *
     * @return the namespace URI
     */
    public String getNamespaceURI() {
        return namespaceURI;
    }

    /**
     * Returns the element name.
     *
     * @return the element on which the attribute must exist
     */
    public String getElement() {
        return element;
    }

    /**
     * Returns the attribute name.
     *
     * @return the attribute in which a URL will be found
     */
    public String getAttribute() {
        return attribute;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 08-Aug-03	308/3	byron	VBM:2003080507 Provide ConvertAbsoluteToRelativeURL pipeline process - create external Tuple classes

 08-Aug-03	308/1	byron	VBM:2003080507 Provide ConvertAbsoluteToRelativeURL pipeline process - create external Tuple classes

 ===========================================================================
*/
