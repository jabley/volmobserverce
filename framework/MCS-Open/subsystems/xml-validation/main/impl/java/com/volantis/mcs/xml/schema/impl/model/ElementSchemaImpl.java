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

import com.volantis.mcs.xml.schema.model.ContentModel;
import com.volantis.mcs.xml.schema.impl.model.ElementReferenceImpl;
import com.volantis.mcs.xml.schema.model.ElementSchema;
import com.volantis.mcs.xml.schema.model.ElementType;
import com.volantis.mcs.xml.schema.model.ElementReference;


/**
 * Identifies an element within the validation mechanism.
 */
public class ElementSchemaImpl
        extends ElementReferenceImpl
        implements ElementSchema {

    private final ElementType elementType;

    private ContentModel model;

    private boolean useAnywhere;

    private boolean transparent;

    public ElementSchemaImpl(ElementType type) {
        this.elementType = type;
    }

    public ElementSchemaImpl(ElementType type, ContentModel model) {
        this(type);
        
        this.model = model;
    }


    public void setContentModel(ContentModel model) {
        this.model = model;
    }

    public ContentModel getContentModel() {
        return model;
    }

    public ElementType getElementType() {
        return elementType;
    }

    public boolean getUseAnywhere() {
        return useAnywhere;
    }

    public void setUseAnywhere(boolean useAnywhere) {
        this.useAnywhere = useAnywhere;
    }

    public boolean isTransparent() {
        return transparent;
    }

    public void setTransparent(boolean transparent) {
        this.transparent = transparent;
    }

    public ElementReference getElementReference() {
        return this;
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
