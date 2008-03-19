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
package com.volantis.mcs.migrate.api.framework;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * An interface to allow user code to provide custom migration implementations.
 * <p>
 * Implementations of this class may be passed to
 * {@link ResourceMigratorBuilder#addStep}.
 *
 * @mock.generate
 */
public interface StreamMigrator {

    /**
     * Migrate a resource from one version to another.
     *
     * @param input the input stream to read the original resource from.
     * @param output the output stream to write the updated resource to.
     * @param stepType the type of step migrate is being called by.
     * @throws ResourceMigrationException if there was an error.
     */
    void migrate(InputStream input, OutputStream output, StepType stepType)
            throws ResourceMigrationException;

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 15-Nov-05	10098/1	phussain	VBM:2005110209 Migration Framework for Repository Parser - ready for review

 18-May-05	8036/3	geoff	VBM:2005050505 XDIMECP: Migration Framework

 16-May-05	8036/1	geoff	VBM:2005050505 XDIMECP: Migration Framework

 ===========================================================================
*/
