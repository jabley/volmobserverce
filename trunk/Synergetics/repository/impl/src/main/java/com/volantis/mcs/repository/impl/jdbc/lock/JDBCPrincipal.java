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

package com.volantis.mcs.repository.impl.jdbc.lock;

import java.security.Principal;

/**
 * A {@link Principal} that identifies the owner of a JDBC lock.
 */
public class JDBCPrincipal
        implements Principal {

    /**
     * The name of the principal.
     */
    private final String name;

    /**
     * Initialise.
     *
     * @param name The name of the principal.
     */
    public JDBCPrincipal(String name) {
        this.name = name;
    }

    /**
     * Get the name of the principal.
     *
     * @return The name of the principal.
     */
    public String getName() {
        return name;
    }

    // Javadoc inherited.
    public boolean equals(Object obj) {
        if (!(obj instanceof JDBCPrincipal)) {
            return false;
        }

        JDBCPrincipal other = (JDBCPrincipal) obj;
        return name.equals(other.name);
    }

    // Javadoc inherited.
    public int hashCode() {
        throw new UnsupportedOperationException();
    }

    public String toString() {
        return "Principal(" + name + ")";
    }
}
