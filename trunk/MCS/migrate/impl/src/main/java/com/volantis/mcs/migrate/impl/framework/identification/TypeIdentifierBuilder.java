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

import com.volantis.mcs.migrate.api.framework.PathRecogniser;
import com.volantis.mcs.migrate.impl.framework.graph.Graph;

/**
 * An object for building type identifiers.
 *
 * @mock.generate
 */
public interface TypeIdentifierBuilder {

    /**
     * Set the name of the type that the created type identifier identifies.
     *
     * @param name the name of the type.
     */
    void setName(String name);

    /**
     * Set the path recogniser for the created type identifier.
     *
     * @param pathRecogniser the path recogniser that the created type
     *      identifier uses.
     */
    void setPathRecogniser(PathRecogniser pathRecogniser);

    /**
     * Add a content identifier to the created type identifier.
     *
     * @param contentIdentifier the content identifier to add.
     */
    void addContentIdentifier(ContentIdentifier contentIdentifier);

    /**
     * Set the migration step graph for the type identifier.
     *
     * @param graph
     */
    void setGraph(Graph graph);

    /**
     * Returns the completed type identifier.
     * <p>
     * This should be called once the name, various recognisers and graph
     * required have been added.
     *
     * @return the type identifier built by this object.
     */
    TypeIdentifier getCompletedTypeIdentifier();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 18-May-05	8036/10	geoff	VBM:2005050505 XDIMECP: Migration Framework

 16-May-05	8036/7	geoff	VBM:2005050505 XDIMECP: Migration Framework

 16-May-05	8036/4	geoff	VBM:2005050505 XDIMECP: Migration Framework

 11-May-05	8036/1	geoff	VBM:2005050505 XDIMECP: Migration Framework

 ===========================================================================
*/
