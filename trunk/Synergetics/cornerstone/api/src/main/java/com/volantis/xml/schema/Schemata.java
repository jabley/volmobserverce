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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A collection of schemata.
 */
public class Schemata {

    private List schemata;

    public Schemata() {
        this.schemata = new ArrayList();
    }

    public void addSchema(SchemaDefinition schema) {
        schemata.add(schema);
    }

    public void addSchemata(Schemata schemata) {
        Iterator iterator = schemata.iterator();
        while (iterator.hasNext()) {
            SchemaDefinition schema = (SchemaDefinition) iterator.next();
            addSchema(schema);
        }
    }

    public Iterator iterator() {
        return schemata.iterator();
    }

    /**
     * Get the value for the xsi:schemaLocation attribute that will associate
     * the namespaces and locations of all the schemata within this.
     *
     * @return The value for the xsi:schemaLocation attribute.
     */
    public String getXSISchemaLocation() {
        StringBuffer namespaceResources = new StringBuffer();
        Iterator iterator = schemata.iterator();
        while (iterator.hasNext()) {
            SchemaDefinition schema = (SchemaDefinition) iterator.next();

            // Add a separator if this is not the first schema definition.
            if (namespaceResources.length() > 0) {
                namespaceResources.append(" ");
            }

            // The schema location for the individual schema.
            namespaceResources.append(schema.getXSISchemaLocation());
        }

        return namespaceResources.toString();
    }
}
