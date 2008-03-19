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
package com.volantis.mcs.migrate.api.framework;

import java.io.IOException;
import java.io.InputStream;

/**
 * An object which can automagically migrate resources to the latest version.
 * <p>
 * <strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User extensions of this class are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels. </strong>
 * </p>
 *
 * @mock.generate
 */
public interface ResourceMigrator {

    /**
     * Automagically migrate a resource to the latest version.
     * <p>
     * This uses the meta data suppplied during the building of the resource
     * migrator to attempt to identify the resource based on it's name and
     * content and then process it accordingly.
     *
     * @param meta Additional properties of the input stream.
     * @param inputStream a stream to allow reading of the input resource.
     * @param outputCreator an object which allows creation of a stream to
     *      allow writing to the output resource.
     * @throws IOException if there was an I/O exception reading from the input
     *      or writing to the output.
     * @throws ResourceMigrationException if there was a migration error.
     */
    void migrate(InputMetadata meta, InputStream inputStream,
            OutputCreator outputCreator)
                    throws IOException, ResourceMigrationException;

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 15-Nov-05	10098/1	phussain	VBM:2005110209 Migration Framework for Repository Parser - ready for review

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 18-May-05	8036/8	geoff	VBM:2005050505 XDIMECP: Migration Framework

 16-May-05	8036/6	geoff	VBM:2005050505 XDIMECP: Migration Framework

 16-May-05	8036/4	geoff	VBM:2005050505 XDIMECP: Migration Framework

 11-May-05	8036/1	geoff	VBM:2005050505 XDIMECP: Migration Framework

 ===========================================================================
*/
