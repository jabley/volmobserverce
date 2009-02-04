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
package com.volantis.mcs.migrate.impl.framework.identification;

import com.volantis.mcs.migrate.api.framework.ResourceMigrationException;
import com.volantis.mcs.migrate.api.framework.Version;

import java.io.InputStream;

/**
 * An object which can identify a a particular content version of a typed
 * resource.
 *
 * @mock.generate
 */
public interface ContentIdentifier {

    /**
     * Returns the version that this object identifies.
     *
     * @return the version that object identifies.
     */
    Version getVersion();

    /**
     * Identify a particular version of a typed resource, returning true if
     * successful and false otherwise.
     *
     * @param input an input stream to the resource content.
     * @return true if the content matches the version specified.
     */
    boolean identifyContent(InputStream input)
            throws ResourceMigrationException;

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 19-May-05	8036/5	geoff	VBM:2005050505 XDIMECP: Migration Framework

 18-May-05	8036/3	geoff	VBM:2005050505 XDIMECP: Migration Framework

 16-May-05	8036/1	geoff	VBM:2005050505 XDIMECP: Migration Framework

 ===========================================================================
*/
