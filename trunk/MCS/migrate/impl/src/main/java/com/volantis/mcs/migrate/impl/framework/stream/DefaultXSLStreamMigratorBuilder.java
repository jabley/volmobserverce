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
package com.volantis.mcs.migrate.impl.framework.stream;

import com.volantis.mcs.migrate.api.framework.XSLStreamMigratorBuilder;
import com.volantis.mcs.migrate.api.framework.StreamMigrator;
import com.volantis.mcs.migrate.api.framework.ResourceMigrationException;
import com.volantis.mcs.migrate.api.notification.NotificationReporter;
import com.volantis.xml.schema.SchemaDefinition;
import com.volantis.xml.schema.Schemata;
import org.xml.sax.EntityResolver;

import java.util.Iterator;

public class DefaultXSLStreamMigratorBuilder
        implements XSLStreamMigratorBuilder {

    private NotificationReporter reporter;

    private XSLStreamMigrator migrator;

    public DefaultXSLStreamMigratorBuilder(NotificationReporter reporter) {
        this.reporter = reporter;
    }

    public void setXSL(String xslResourcePath)
            throws ResourceMigrationException {
        
        migrator = new XSLStreamMigrator(xslResourcePath, reporter);
    }

    public void addEntityResolver(EntityResolver entityResolver) {
        migrator.setEntityResolver(entityResolver);
    }

    public void addInputSchema(SchemaDefinition schema) {
        migrator.addInputSchema(schema);
    }

    public void addInputSchemata(Schemata schemata) {
        Iterator iterator = schemata.iterator();
        while (iterator.hasNext()) {
            SchemaDefinition schema = (SchemaDefinition) iterator.next();
            migrator.addInputSchema(schema);
        }
    }

    public void setStrictMode(boolean strictMode) {
            migrator.setStrictMode(strictMode);
    }

    public void addOutputSchema(SchemaDefinition schema) {
        migrator.addOutputSchema(schema);
    }

    public void addOutputSchemata(Schemata schemata) {
        // Add schemata for output validation.
        Iterator iterator = schemata.iterator();
        while (iterator.hasNext()) {
            SchemaDefinition schema = (SchemaDefinition) iterator.next();
            migrator.addOutputSchema(schema);
        }
    }

    public StreamMigrator getCompletedMigrator() {
        return migrator;
    }

}
