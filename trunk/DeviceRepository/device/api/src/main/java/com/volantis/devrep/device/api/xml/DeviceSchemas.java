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
package com.volantis.devrep.device.api.xml;

import com.volantis.xml.schema.SchemaDefinition;

/**
 * XML Schema definitions for the device repository.
 */
public class DeviceSchemas {

    private static final String NAMESPACE_PREFIX =
            "http://www.volantis.com/xmlns/device-repository/";

    /**
     * Common base URI for device repository v3.0 metadata files.
     *
     * NOTE: the v3.0 refers to the version of the schema not the version of
     * the device repository.
     */
    private static final String LOCATION_PREFIX_V3_0 =
            "http://www.volantis.com/schema/device-repository/v3.0/";

    /**
     * Package that the local schema files are located in.
     */
    private static final String RESOURCE_PACKAGE =
            "com/volantis/devrep/device/api/xml/schema/";

    //
    // Normal Schemas
    //

    //
    // v3.0 schemas.
    //

    public static final SchemaDefinition DEVICE_V3_0 =
            new SchemaDefinition(
                    NAMESPACE_PREFIX + "device",
                    LOCATION_PREFIX_V3_0 + "device.xsd",
                    RESOURCE_PACKAGE + "device.xsd"
    );

    public static final SchemaDefinition CORE_V3_0 =
            new SchemaDefinition(
                    null, /* device-component is only ever included */
                    LOCATION_PREFIX_V3_0 + "device-core.xsd",
                    RESOURCE_PACKAGE + "device-core.xsd"
    );

    public static final SchemaDefinition HEIRARCHY_V3_0 =
            new SchemaDefinition(
                    NAMESPACE_PREFIX + "device-hierarchy",
                    LOCATION_PREFIX_V3_0 + "device-hierarchy.xsd",
                    RESOURCE_PACKAGE + "device-hierarchy.xsd"
    );

    public static final SchemaDefinition IDENTIFICATION_V3_0 =
            new SchemaDefinition(
                    NAMESPACE_PREFIX + "device-identification",
                    LOCATION_PREFIX_V3_0 + "device-identification.xsd",
                    RESOURCE_PACKAGE + "device-identification.xsd"
    );

    // NOTE: tac indentification was added after all the other v3 schemas
    // A v3.0 device repository therefore may or may not include tac
    // identification data. We should work in either case.

    public static final SchemaDefinition TAC_IDENTIFICATION_V3_0 =
            new SchemaDefinition(
                    NAMESPACE_PREFIX + "device-tac-identification",
                    LOCATION_PREFIX_V3_0 + "device-tac-identification.xsd",
                    RESOURCE_PACKAGE + "device-tac-identification.xsd"
    );

    public static final SchemaDefinition POLICY_DEFINITIONS_V3_0 =
            new SchemaDefinition(
                    NAMESPACE_PREFIX + "policy-definitions",
                    LOCATION_PREFIX_V3_0 + "device-policy-definitions.xsd",
                    RESOURCE_PACKAGE + "device-policy-definitions.xsd"
    );

    //
    // Current schemas.
    //

    public static final SchemaDefinition DEVICE_CURRENT = DEVICE_V3_0;

    public static final SchemaDefinition CORE_CURRENT = CORE_V3_0;

    public static final SchemaDefinition HEIRARCHY_CURRENT = HEIRARCHY_V3_0;

    public static final SchemaDefinition IDENTIFICATION_CURRENT =
            IDENTIFICATION_V3_0;

    public static final SchemaDefinition TAC_IDENTIFICATION_CURRENT =
            TAC_IDENTIFICATION_V3_0;

    public static final SchemaDefinition POLICY_DEFINITIONS_CURRENT =
            POLICY_DEFINITIONS_V3_0;

    //
    // Update schemas
    //

    // todo This location is inconsistent with the other device schema
    // todo URI's as it has a different prefix. This should be addressed
    // todo and will require an update to migration.
    public static final SchemaDefinition REPOSITORY_V3_0 =
            new SchemaDefinition(
                    "http://www.volantis.com/xmlns/device-repository",
                    "http://www.volantis.com/schema/v3.0/device-repository.xsd",
                    RESOURCE_PACKAGE + "device-repository.xsd"
    );
    
    public static final SchemaDefinition UPDATE_COMMON_V3_0 =
            new SchemaDefinition(
                    null, /* device-update-common is only ever included */
                    LOCATION_PREFIX_V3_0 + "device-update-common.xsd",
                    RESOURCE_PACKAGE + "device-update-common.xsd"
    );

    public static final SchemaDefinition REPOSITORY_CURRENT =
            REPOSITORY_V3_0;
    
    public static final SchemaDefinition UPDATE_COMMON_CURRENT =
            UPDATE_COMMON_V3_0;

    /**
     * This class cannot be constructed. It is only for static access to
     * constants.
     */
    private DeviceSchemas() {
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 13-Nov-05	9896/1	geoff	VBM:2005101906 Avoid using JDOM in MCS at runtime: rewrite runtime XML device repository

 ===========================================================================
*/
