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

import com.volantis.mcs.migrate.api.framework.ResourceMigrationException;
import com.volantis.mcs.migrate.api.framework.Version;
import com.volantis.mcs.migrate.api.framework.StepType;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * An object which can be used to migrate30 a resource from one version to
 * another.
 * <p>
 * Steps are designed so that they may be composed to enable the migration of
 * resources from multiple different older versions to the target version.
 *
 * @mock.generate
 */
public interface Step {

    /**
     * The version that this object can accept.
     *
     * @return the version that this object can accept.
     */
    Version getInput();

    /**
     * The version that this object can create from the accepted version.
     *
     * @return the version that this object can create from the accepted
     *      version.
     */
    Version getOutput();

    /**
     * Perform the migration from the input to output version, using the input
     * and output streams supplied.
     *
     * @param input used to read the content to be migrated
     * @param output used to write the migrated content.
     * @param typeOfValidation to be performed by this migration step
     * @throws ResourceMigrationException if there was a migration error.
     */
    void migrate(InputStream input, OutputStream output,
        StepType typeOfValidation)
            throws ResourceMigrationException;

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 18-May-05	8036/10	geoff	VBM:2005050505 XDIMECP: Migration Framework

 16-May-05	8036/8	geoff	VBM:2005050505 XDIMECP: Migration Framework

 16-May-05	8036/3	geoff	VBM:2005050505 XDIMECP: Migration Framework

 11-May-05	8036/1	geoff	VBM:2005050505 XDIMECP: Migration Framework

 ===========================================================================
*/
