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
import com.volantis.mcs.migrate.impl.framework.identification.Step;

/**
 * An object for building graphs of migration steps.
 *
 * @mock.generate
 */
public interface GraphBuilder {

    /**
     * Set the target version for this migration step graph.
     * <p>
     * All paths though the graph must end in this version in order to be
     * valid.
     * <p>
     * NOTE: This method must be called before {@link #addStep}.
     * @param version the target version for this graph.
     */
    void setTarget(Version version);

    /**
     * Add a single step to the graph.
     * <p>
     * Each step defines the path from one version to another.
     *
     * @param step the step to add.
     */
    void addStep(Step step);

    /**
     * Returns the completed migration step graph.
     * <p>
     * This should be called once the target version has been set and all the
     * steps required have been added.
     *
     * @return the graph built by this object.
     */
    Graph getCompletedGraph();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 18-May-05	8036/5	geoff	VBM:2005050505 XDIMECP: Migration Framework

 16-May-05	8036/3	geoff	VBM:2005050505 XDIMECP: Migration Framework

 11-May-05	8036/1	geoff	VBM:2005050505 XDIMECP: Migration Framework

 ===========================================================================
*/
