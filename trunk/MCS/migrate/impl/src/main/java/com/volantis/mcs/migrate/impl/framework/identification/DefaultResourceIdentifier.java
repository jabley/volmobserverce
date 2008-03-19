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

import com.volantis.mcs.migrate.api.framework.InputMetadata;
import com.volantis.mcs.migrate.api.framework.ResourceMigrationException;
import com.volantis.mcs.migrate.impl.framework.io.RestartInputStream;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * Default implementation of {@link ResourceIdentifier}.
 * <p>
 * This is implemented by managing a collection of types and delegating to
 * each type in turn to identify the resource.
 */
public class DefaultResourceIdentifier implements ResourceIdentifier {

    /**
     * A collection of the types that this resource identifier manages.
     */
    private Collection types = new ArrayList();

    /**
     * Initialise.
     */
    public DefaultResourceIdentifier() {
    }

    /**
     * Add a type to the resource identifier.
     *
     * @param type the type to add.
     */
    public void addType(TypeIdentifier type) {

        types.add(type);
    }

    // Javadoc inherited.
    public Match identifyResource(InputMetadata meta, RestartInputStream input)
            throws IOException, ResourceMigrationException {

        if (types.size() == 0) {
            throw new IllegalStateException(
                    "Cannot recoginise a resource without any contained types");
        }

        Match singleMatch = null;

        Iterator typeIterator = types.iterator();
        while (typeIterator.hasNext()) {
            TypeIdentifier type = (TypeIdentifier) typeIterator.next();

            Match match = type.identifyResource(meta, input);
            if (match != null) {
                if (singleMatch == null) {
                    singleMatch = match;
                } else {
                    throw new ResourceMigrationException("Duplicate " +
                            "type match: " + match.getTypeName() + ":" +
                            match.getVersionName() + " matches " +
                            singleMatch.getTypeName() + ":" +
                            singleMatch.getVersionName());
                }
            }
        }

        return singleMatch;
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 15-Nov-05	10098/2	phussain	VBM:2005110209 Migration Framework for Repository Parser - ready for review

 19-May-05	8036/14	geoff	VBM:2005050505 XDIMECP: Migration Framework

 18-May-05	8036/12	geoff	VBM:2005050505 XDIMECP: Migration Framework

 16-May-05	8036/10	geoff	VBM:2005050505 XDIMECP: Migration Framework

 16-May-05	8036/6	geoff	VBM:2005050505 XDIMECP: Migration Framework

 16-May-05	8036/4	geoff	VBM:2005050505 XDIMECP: Migration Framework

 11-May-05	8036/1	geoff	VBM:2005050505 XDIMECP: Migration Framework

 ===========================================================================
*/
