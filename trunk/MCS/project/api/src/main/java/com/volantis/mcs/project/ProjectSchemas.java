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

package com.volantis.mcs.project;

import com.volantis.xml.schema.SchemaDefinition;

/**
 * The definitions of the XML schemata used in project files.
 */
public class ProjectSchemas {

    private static final String MCS_3_5_PROJECT_NAMESPACE =
            "http://www.volantis.com/xmlns/mcs/project";
    /**
     * The original schema for the project.
     */
    public static final SchemaDefinition MCS_3_5_PROJECT =
            new SchemaDefinition(
                    MCS_3_5_PROJECT_NAMESPACE,
                    "http://www.volantis.com/schema/project/3-5/mcs-project.xsd",
                    "com/volantis/schema/project/3-5/mcs-project.xsd");

}
