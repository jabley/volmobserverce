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

package com.volantis.mcs.xml.schema.model;

import com.volantis.synergetics.factory.MetaDefaultFactory;

/**
 * Provides methods to create schema model related instances.
 */
public abstract class SchemaModelFactory {

    /**
     * Obtain a reference to the default factory implementation.
     */
    private static final MetaDefaultFactory metaDefaultFactory;

    static {
        metaDefaultFactory =
                new MetaDefaultFactory(
                        "com.volantis.mcs.xml.schema.impl.model.SchemaModelFactoryImpl",
                        SchemaModelFactory.class.getClassLoader());
    }

    /**
     * Get the default instance of this factory.
     *
     * @return The default instance of this factory.
     */
    public static SchemaModelFactory getDefaultInstance() {
        return (SchemaModelFactory) metaDefaultFactory.getDefaultFactoryInstance();
    }

    /**
     * Create an element schema for the specified element type.
     *
     * @param type The type for which the schema is created.
     * @return The element schema.
     */
    public abstract ElementSchema createElementSchema(ElementType type);

    /**
     * Create a content model that accepts content as long as it is from the
     * predefined set of content.
     *
     * <p>The model can be modified by calling
     * {@link CompositeModel#add(ContentModel)} and
     * {@link CompositeModel#add(ElementSchema)}.
     *
     * @return A content model that accepts content from a predefined set of
     *         content.
     */
    public abstract CompositeModel createContentChoice();

    /**
     * Create a content model that accepts content as long as it is in a
     * predefined order.
     *
     * <p>The model can be modified by calling
     * {@link CompositeModel#add(ContentModel)} and
     * {@link CompositeModel#add(ElementSchema)}.
     *
     * @return A content model that accepts content from a predefined set of
     *         content.
     */
    public abstract CompositeModel createContentSequence();

    /**
     * Create a content model that places boundaries on another model.
     *
     * <p>By default the returned model will accept from 0 to infinite
     * occurences of the bound model. This can be changed by calling various
     * methods on the returned model.</p>
     *
     * @param model The model to be bound.
     * @return The bounded content model.
     */
    public abstract BoundedContent createBoundedContent(ContentModel model);

    /**
     * Create a wrapper model around the specified model to allow exclusions
     * to be added.
     *
     * @param model The model to be wrapped.
     * @return The wrapping model.
     */
    public abstract ContentModel createWrapperContent(ContentModel model);

    /**
     * Create a content model that does not allow any content.
     *
     * @return A content model that does not allow any content.
     */
    public abstract ContentModel createEmptyContent();

    /**
     * Create a content model that can allow any single element content.
     *
     * @return A content model that allows any single element content.
     */
    public abstract ContentModel createAnyContent();

    public abstract ContentModel createPCDATAContent();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 10-Oct-05	9673/1	pduffin	VBM:2005092906 Improved validation and fixed layout formatting

 ===========================================================================
*/
