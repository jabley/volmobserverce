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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.xml.schema;

/**
 * Definition of the meta data associated with an XML schema.
 * <p>
 * Currently this stores the namespace and location, along with the java
 * resource name to the local definition of the schema.
 */
public class SchemaDefinition {

    /**
     * The URL of the namespace of the schema.
     */
    private final String namespaceURL;

    /**
     * The URL of the location of the schema.
     */
    private final String locationURL;

    /**
     * The name of the schema java resource which can be used as the local
     * definition of the schema.
     */
    private final String resourceName;

    /**
     * The value of the xsi:schemaLocation attribute.
     *
     * <p>namespace followed by a space and the location URL.
     */
    private final String xsiSchemaLocation;

    /**
     * Initialise.
     *
     * @param namespaceURL The URL of the namespace of the schema.
     * @param locationURL The URL of the location of the schema.
     * @param resourceName The name of the java resource which can be
     * used as the local definition of the schema.
     */
    public SchemaDefinition(String namespaceURL, String locationURL,
            String resourceName) {

        this.namespaceURL = namespaceURL;
        this.locationURL = locationURL;
        this.resourceName = resourceName;
        xsiSchemaLocation = namespaceURL + " " + locationURL;
    }

    /**
     * @see #namespaceURL
     */
    public String getNamespaceURL() {
        return namespaceURL;
    }

    /**
     * @see #locationURL
     */
    public String getLocationURL() {
        return locationURL;
    }

    /**
     * @see #resourceName 
     */
    public String getResourceName() {
        return resourceName;
    }

    /**
     * @see #xsiSchemaLocation
     */
    public String getXSISchemaLocation() {
        return xsiSchemaLocation;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 13-Nov-05	9896/2	geoff	VBM:2005101906 Avoid using JDOM in MCS at runtime: rewrite runtime XML device repository

 11-Oct-05	9729/1	geoff	VBM:2005100507 Mariner Export fails with NPE

 ===========================================================================
*/
