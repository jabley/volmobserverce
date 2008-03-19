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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.migrate.impl.framework.identification;

import java.util.Iterator;

/**
 * Default implementation of {@link Match}.
 * <p>
 * This is implemented as a simple value object populated by
 * {@link TypeIdentifier}.
 */
public class DefaultMatch implements Match {

    /**
     * The name of the type that matched.
     */
    private String typeName;

    /**
     * The name of the version that matched.
     */
    private String versionName;

    /**
     * The sequence of {@link Step}s that is required to perform the migration.
     */
    private Iterator sequence;

    /**
     * Initialise.
     *
     * @param typeName the name of the type that matched.
     * @param versionName the name of the version that matched.
     * @param sequence the sequence of {@link Step}s required to perform the
     *      migration.
     */
    public DefaultMatch(String typeName, String versionName,
            Iterator sequence) {

        this.typeName = typeName;
        this.versionName = versionName;
        this.sequence = sequence;
    }

    // Javadoc inherited.
    public String getTypeName() {

        return typeName;
    }

    // Javadoc inherited.
    public String getVersionName() {

        return versionName;
    }

    // Javadoc inherited.
    public Iterator getSequence() {

        return sequence;
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-May-05	8036/10	geoff	VBM:2005050505 XDIMECP: Migration Framework

 16-May-05	8036/8	geoff	VBM:2005050505 XDIMECP: Migration Framework

 16-May-05	8036/6	geoff	VBM:2005050505 XDIMECP: Migration Framework

 16-May-05	8036/4	geoff	VBM:2005050505 XDIMECP: Migration Framework

 11-May-05	8036/1	geoff	VBM:2005050505 XDIMECP: Migration Framework

 ===========================================================================
*/
