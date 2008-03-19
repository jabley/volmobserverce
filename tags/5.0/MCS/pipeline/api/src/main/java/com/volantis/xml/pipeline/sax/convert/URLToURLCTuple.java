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
 * Permits the URLToURLC process to be configured to convert a URL found in
 * a given attribute on a given element within a given namespace (or all
 * namespaces) into a URLC, possibly updating the existing attribute or
 * replacing the given attribute with a new one.
 */
public class URLToURLCTuple extends ConverterTuple {
    /**
     *  Volantis copyright mark.
     */
    private static String mark
            = "(c) Volantis Systems Ltd 2003. ";

    /**
     * The new attribute to be generated containing a URLC, or null if
     * the original attribute should be updated.
     */
    private String replacementAttribute;

    /**
     * Initializes the new instance with the given parameters.
     *
     * @param namespaceURI         the namespace URI in which the element
     *                             must exist, or null for "all namespaces"
     * @param element              the element on which the attribute must
     *                             exist
     * @param attribute            the attribute in which a URL will be
     *                             found
     * @param replacementAttribute the new attribute to be generated
     *                             containing the URLC, or null if the
     *                             original attribute is to be updated
     */
    public URLToURLCTuple(String namespaceURI,
                          String element,
                          String attribute,
                          String replacementAttribute) {

        super(namespaceURI, element, attribute);
        this.replacementAttribute = replacementAttribute;
    }

    /**
     * Returns the replacement attribute name or null if the original
     * attribute is to be updated.
     *
     * @return the replacement attribute name or null if the original
     *         attribute is to be updated
     */
    public String getReplacementAttribute() {
        return replacementAttribute;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 08-Aug-03	308/1	byron	VBM:2003080507 Provide ConvertAbsoluteToRelativeURL pipeline process - create external Tuple classes

 ===========================================================================
*/
