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
package com.volantis.mcs.eclipse.common;

import com.volantis.devrep.device.api.xml.DeviceSchemas;
import com.volantis.devrep.repository.api.accessors.xml.EclipseEntityResolver;
import com.volantis.mcs.repository.xml.PolicySchemas;
import com.volantis.xml.schema.SchemaDefinition;
import com.volantis.xml.schema.Schemata;

import java.util.Iterator;

/**
 * A factory class for creating entity resolvers for the Eclipse GUI for
 * the repository schemas that the GUI allows editing of.
 */
public class RepositorySchemaResolverFactory {

    public static final Schemata GUI_SCHEMATA;
    static {
        Schemata schemata = new Schemata();
        // todo: should rpdm schemas be here as well?
        schemata.addSchemata(PolicySchemas.LOCAL_REPOSITORY_CURRENT);

        // NOTE: device schema is inluded as device repository might
        // be edited with device repository editor
        schemata.addSchema(DeviceSchemas.DEVICE_CURRENT);
        // NOTE: although these are not edited directly by the user
        // (except hierarchy) the XML's are modified and validated. 
        schemata.addSchema(DeviceSchemas.HEIRARCHY_CURRENT);
        schemata.addSchema(DeviceSchemas.IDENTIFICATION_CURRENT);
        schemata.addSchema(DeviceSchemas.TAC_IDENTIFICATION_CURRENT);
        schemata.addSchema(DeviceSchemas.POLICY_DEFINITIONS_CURRENT);

        GUI_SCHEMATA = schemata;
    }

    /**
     * Factory method that returns a <code>JarFileEntityResolver</code> that
     * is configured to resolve the systemId's required by the Eclipse GUI.
     *
     * @return a <code>JarFileEntityResolver</code> instance
     */
    public static EclipseEntityResolver create() {

        // create a jar file entity resolver
        EclipseEntityResolver resolver = new EclipseEntityResolver();

        Iterator iterator = GUI_SCHEMATA.iterator();
        while (iterator.hasNext()) {
            SchemaDefinition schema = (SchemaDefinition) iterator.next();
            resolver.addSystemIdMapping(schema);
        }

        return resolver;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 12-Dec-05	10374/1	emma	VBM:2005111705 Interim commit

 13-Dec-05	10345/1	adrianj	VBM:2005111601 Add style rule view

 13-Nov-05	9896/1	geoff	VBM:2005101906 Avoid using JDOM in MCS at runtime: rewrite runtime XML device repository

 ===========================================================================
*/
