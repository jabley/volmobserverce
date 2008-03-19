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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.runtime.configuration.xml;

import com.volantis.xml.schema.SchemaDefinition;

/**
 * Contains definitions of the MCS configuration namespaces and schemata.
 */
public class ConfigSchemas {

    // MCS V3.5 Configuration Namespace
    public static final String MCS_3_5_CONFIG_NAMESPACE =
            "http://www.volantis.com/xmlns/mcs/config";

    // MCS V3.5 Configuration Schema
    public static final SchemaDefinition MCS_3_5_CONFIG_SCHEMA =
            new SchemaDefinition(
                    MCS_3_5_CONFIG_NAMESPACE,
                    "http://www.volantis.com/schema/config/v3.5/mcs-config.xsd",
                    "com/volantis/schema/config/v3.5/mcs-config.xsd");
}
