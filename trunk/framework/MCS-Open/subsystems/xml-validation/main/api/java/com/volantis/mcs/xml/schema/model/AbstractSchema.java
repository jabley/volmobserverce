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
 * (c) Volantis Systems Ltd 2007. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.xml.schema.model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public abstract class AbstractSchema
        implements Schema {

    private static final SchemaModelFactory SCHEMA_MODEL_FACTORY =
            SchemaModelFactory.getDefaultInstance();

    protected static final ContentModel EMPTY = SCHEMA_MODEL_FACTORY.createEmptyContent();

    protected static final ContentModel ANY = SCHEMA_MODEL_FACTORY.createAnyContent();

    protected static final ContentModel PCDATA = SCHEMA_MODEL_FACTORY.createPCDATAContent();

    private final Map elements;

    protected AbstractSchema() {
        elements = new HashMap();
    }

    protected ElementSchema createElementSchema(ElementType type) {
        ElementSchema element = (ElementSchema) elements.get(type);
        if (element == null) {
            element = SCHEMA_MODEL_FACTORY.createElementSchema(type);
            elements.put(type, element);
        }

        return element;
    }

    public void importSchema(Schema schema) {
        Iterator i = schema.elements();
        while (i.hasNext()) {
            ElementSchema elementSchema = (ElementSchema) i.next();
            elements.put(elementSchema.getElementType(), elementSchema);
        }
    }

    // Javadoc inherited.
    public Iterator elements() {
        return elements.values().iterator();
    }

    protected CompositeModel choice() {
        return SCHEMA_MODEL_FACTORY.createContentChoice();
    }

    protected CompositeModel sequence() {
        return SCHEMA_MODEL_FACTORY.createContentSequence();
    }

    protected BoundedContent bounded(ElementSchema element) {
        return bounded(element.getElementReference());
    }

    protected BoundedContent bounded(ContentModel model) {
        return SCHEMA_MODEL_FACTORY.createBoundedContent(model);
    }

    protected ContentModel wrapper(ElementSchema element) {
        return wrapper(element.getElementReference());
    }

    protected ContentModel wrapper(ContentModel model) {
        return SCHEMA_MODEL_FACTORY.createWrapperContent(model);
    }
}
