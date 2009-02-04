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
 * Default implementation of {@link TypeIdentifierBuilder}.
 */
public class DefaultTypeIdentifierBuilder implements TypeIdentifierBuilder {

    /**
     * Factory for creating identification objects.
     */
    private IdentificationFactory factory;

    /**
     * The type that this builder is constructing.
     */
    private DefaultTypeIdentifier type;

    /**
     * Initialise.
     *
     * @param factory factory for creating identification objects.
     */
    public DefaultTypeIdentifierBuilder(IdentificationFactory factory) {

        this.factory = factory;
    }

    // Javadoc inherited.
    public void setName(String name) {

        type = new DefaultTypeIdentifier(factory, name);
    }

    // Javadoc inherited.
    public void setPathRecogniser(PathRecogniser pathRecogniser) {

        type.setPathRecogniser(pathRecogniser);
    }

    // Javadoc inherited.
    public void addContentIdentifier(ContentIdentifier contentRecogniser) {

        type.addContentIdentifier(contentRecogniser);
    }

    // Javadoc inherited.
    public void setGraph(Graph graph) {

        type.setGraph(graph);
    }

    // Javadoc inherited.
    public TypeIdentifier getCompletedTypeIdentifier() {

        return type;
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-May-05	8036/12	geoff	VBM:2005050505 XDIMECP: Migration Framework

 16-May-05	8036/9	geoff	VBM:2005050505 XDIMECP: Migration Framework

 16-May-05	8036/6	geoff	VBM:2005050505 XDIMECP: Migration Framework

 16-May-05	8036/4	geoff	VBM:2005050505 XDIMECP: Migration Framework

 11-May-05	8036/1	geoff	VBM:2005050505 XDIMECP: Migration Framework

 ===========================================================================
*/
