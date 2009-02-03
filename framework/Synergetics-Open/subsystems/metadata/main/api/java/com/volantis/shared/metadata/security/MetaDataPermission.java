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

package com.volantis.shared.metadata.security;

import java.security.acl.Permission;

/**
 * The set of permissions that can be given to users of the meta data
 * infrastructure.
 */
public class MetaDataPermission
        implements Permission {

    /**
     * Permission to read the meta data.
     */
    public static final MetaDataPermission READ = new MetaDataPermission(
            "READ");

    /**
     * Permission to write the meta data.
     */
    public static final MetaDataPermission WRITE = new MetaDataPermission(
            "WRITE");

    /**
     * The name of the enumeration.
     */
    private final String name;

    /**
     * Initialise.
     *
     * @param name The name of the enumeration.
     */
    private MetaDataPermission(String name) {
        this.name = name;
    }

    // Javadoc inherited.
    public String toString() {
        return name;
    }
}
