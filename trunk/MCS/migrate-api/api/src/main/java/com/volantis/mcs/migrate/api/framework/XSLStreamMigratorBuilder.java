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
package com.volantis.mcs.migrate.api.framework;

import org.xml.sax.EntityResolver;
import com.volantis.xml.schema.SchemaDefinition;
import com.volantis.xml.schema.Schemata;

public interface XSLStreamMigratorBuilder {

    /**
     *
     * @param xslResourcePath the resource path to the XSL that does the
     *      migration.
     */
    void setXSL(String xslResourcePath) throws ResourceMigrationException;

    /**
     *
     * @param entityResolver An entity resolver to use with the transformation.
     */
    void addEntityResolver(EntityResolver entityResolver);

    void addInputSchema(SchemaDefinition schema);

    void addInputSchemata(Schemata schemata);

    void setStrictMode(boolean strictMode);

    void addOutputSchema(SchemaDefinition schema);

    void addOutputSchemata(Schemata schemata);

    StreamMigrator getCompletedMigrator();

}
