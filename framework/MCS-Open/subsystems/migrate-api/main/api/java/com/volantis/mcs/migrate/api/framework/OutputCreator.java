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

import java.io.OutputStream;

/**
 * A "callback" interface to allow user code to create an output stream to
 * write migrated resource data to.
 * <p>
 * This is required to avoid creating the output if the migration cannot
 * be performed for any reason.
 * <p>
 * Implementations of this class may be passed to
 * {@link ResourceMigrator#migrate}.
 *
 * @mock.generate
 */
public interface OutputCreator {

    /**
     * Called to create an output stream to write the migrated resource data
     * to.
     *
     * @return an output stream to write the migrated resource data to.
     */
    OutputStream createOutputStream();

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 18-May-05	8036/3	geoff	VBM:2005050505 XDIMECP: Migration Framework

 16-May-05	8036/1	geoff	VBM:2005050505 XDIMECP: Migration Framework

 ===========================================================================
*/
