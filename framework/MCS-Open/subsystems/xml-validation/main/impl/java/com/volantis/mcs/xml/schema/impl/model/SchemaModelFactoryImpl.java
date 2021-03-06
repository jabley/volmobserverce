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

package com.volantis.mcs.xml.schema.impl.model;

import com.volantis.mcs.xml.schema.model.BoundedContent;
import com.volantis.mcs.xml.schema.model.CompositeModel;
import com.volantis.mcs.xml.schema.model.ContentModel;
import com.volantis.mcs.xml.schema.model.ElementSchema;
import com.volantis.mcs.xml.schema.model.ElementType;
import com.volantis.mcs.xml.schema.model.SchemaModelFactory;

/**
 * Default implementation of the {@link SchemaModelFactory}.
 */
public class SchemaModelFactoryImpl
    extends SchemaModelFactory {

    /**
     * The default instance of empty content.
     */
    private static final ContentModel EMPTY_CONTENT = new EmptyContentImpl();

    /**
     * The default instance of an any content model.
     */
    private static final ContentModel ANY_CONTENT = new AnyContentImpl();
    
    private static final ContentModel PCDATA_CONTENT = new CharacterContentImpl();

    // Javadoc inherited.
    public ElementSchema createElementSchema(ElementType type) {
        return new ElementSchemaImpl(type);
    }

    // Javadoc inherited.
    public CompositeModel createContentChoice() {
        return new ContentChoiceImpl();
    }

    // Javadoc inherited.
    public CompositeModel createContentSequence() {
        return new ContentSequenceImpl();
    }

    // Javadoc inherited.
    public BoundedContent createBoundedContent(ContentModel model) {
        return new BoundedContentImpl(model);
    }

    public ContentModel createWrapperContent(ContentModel model) {
        return new WrappingContentImpl(model);
    }

    // Javadoc inherited.
    public ContentModel createEmptyContent() {
        return EMPTY_CONTENT;
    }

    // Javadoc inherited.
    public ContentModel createAnyContent() {
        return ANY_CONTENT;
    }

    public ContentModel createPCDATAContent() {
        return PCDATA_CONTENT;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 10-Oct-05	9673/1	pduffin	VBM:2005092906 Improved validation and fixed layout formatting

 ===========================================================================
*/
