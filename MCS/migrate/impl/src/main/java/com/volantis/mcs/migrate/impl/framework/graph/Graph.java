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
package com.volantis.mcs.migrate.impl.framework.graph;

import com.volantis.mcs.migrate.api.framework.Version;

import java.util.Iterator;

/**
 * A high level interface to a graph of migration steps for a particular
 * resource type.
 *
 * @mock.generate
 */
public interface Graph {

    /**
     * Return the sequence of steps required to migrate30 from the version
     * provided to the target version as an iterator.
     *
     * @param startVersion the version to start from.
     * @return the sequence of steps from the start version to the target
     *      version.
     */
    Iterator getSequence(Version startVersion);

    /**
     * Return the target version of this graph.
     *
     * @return Version the target version of this graph.
     */
    Version getTargetVersion();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 19-May-05	8036/7	geoff	VBM:2005050505 XDIMECP: Migration Framework

 18-May-05	8036/5	geoff	VBM:2005050505 XDIMECP: Migration Framework

 16-May-05	8036/3	geoff	VBM:2005050505 XDIMECP: Migration Framework

 11-May-05	8036/1	geoff	VBM:2005050505 XDIMECP: Migration Framework

 ===========================================================================
*/
