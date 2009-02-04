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
 * An object which is the result of a successful identification.
 * <p>
 * This allows the client to retrieve the individual migration steps that are
 * required to be run in order to perform the migration.
 *
 * @mock.generate
 */
public interface Match {

    /**
     * The name of the type that was identified.
     *
     * @return the name of the type that was identified.
     */
    String getTypeName();

    /**
     * The name of the version that was identified.
     *
     * @return the name of the version that was identifed.
     */
    String getVersionName();

    /**
     * Return an iterator for the sequence of migration {@link Step}s required
     * to be run in order to perform the migration.
     * <p>
     * This iterator will contain at least one step (the step that results in
     * the target version).
     *
     * @return iterator of {@link Step}s.
     */
    Iterator getSequence();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 18-May-05	8036/8	geoff	VBM:2005050505 XDIMECP: Migration Framework

 16-May-05	8036/6	geoff	VBM:2005050505 XDIMECP: Migration Framework

 16-May-05	8036/4	geoff	VBM:2005050505 XDIMECP: Migration Framework

 11-May-05	8036/1	geoff	VBM:2005050505 XDIMECP: Migration Framework

 ===========================================================================
*/
