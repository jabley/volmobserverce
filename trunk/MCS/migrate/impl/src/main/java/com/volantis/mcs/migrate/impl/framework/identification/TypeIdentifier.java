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

/**
 * An object which can identify a typed resource by version and calculate
 * the sequence of steps which can be used to migrate30 the resource to the
 * target version.
 *
 * @mock.generate
 */
public interface TypeIdentifier {

    /**
     * Return the name of the type that is identified by this object.
     *
     * @return the name of the identified type.
     */
    String getName();

    /**
     * Identify a typed resource by version, calculating the sequence of
     * steps required to migrate30 the resource to the target version if
     * identified. It may return null if either no match could be found for the
     * resource, or no sequence of migration steps could be identified because
     * no migration was required

     * @param meta Additional properties of the input stream.
     * @param input an input stream to the content of the resource.
     * @return a match containing the step sequence, or null if no match was
     * found for the resource or no migration was required for the resource.
     * @throws IOException if there was an I/O error reading the input.
     * @throws ResourceMigrationException if there was a migration error.
     */
    Match identifyResource(InputMetadata meta, RestartInputStream input)
            throws IOException, ResourceMigrationException;

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 15-Nov-05	10098/2	phussain	VBM:2005110209 Migration Framework for Repository Parser - ready for review

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 18-May-05	8036/11	geoff	VBM:2005050505 XDIMECP: Migration Framework

 16-May-05	8036/8	geoff	VBM:2005050505 XDIMECP: Migration Framework

 16-May-05	8036/6	geoff	VBM:2005050505 XDIMECP: Migration Framework

 16-May-05	8036/4	geoff	VBM:2005050505 XDIMECP: Migration Framework

 11-May-05	8036/1	geoff	VBM:2005050505 XDIMECP: Migration Framework

 ===========================================================================
*/
