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

package com.volantis.mcs.xml.schema.impl.compiler;

import com.volantis.mcs.xml.schema.compiler.CompiledSchema;
import com.volantis.mcs.xml.schema.compiler.SchemaCompiler;
import com.volantis.mcs.xml.schema.impl.validation.ElementValidator;
import com.volantis.mcs.xml.schema.model.ElementSchema;
import com.volantis.mcs.xml.schema.model.ElementType;
import com.volantis.mcs.xml.schema.model.Schema;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class SchemaCompilerImpl implements SchemaCompiler {

    private Map validatorPrototypes;
    private ValidatorPrototypeBuilder builder;

    public SchemaCompilerImpl() {
        builder = new ValidatorPrototypeBuilder(new ValidatorPrototypeFactoryImpl());
        validatorPrototypes = new HashMap();
    }

    public void addSchema(Schema schema) {
        for (Iterator i = schema.elements(); i.hasNext();) {
            ElementSchema element = (ElementSchema) i.next();
            ElementType type = element.getElementType();
            ElementValidator elementValidator = builder.build(element);
            validatorPrototypes.put(type, elementValidator);
        }
    }

    public CompiledSchema getCompiledSchema() {
        return new CompiledSchemaImpl(validatorPrototypes);
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
