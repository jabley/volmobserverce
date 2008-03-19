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

import com.volantis.mcs.xml.schema.impl.validation.ElementValidator;
import com.volantis.mcs.xml.schema.impl.validation.PrototypeElementValidator;
import com.volantis.mcs.xml.schema.model.ElementType;

import java.util.Map;

public class CompiledSchemaImpl
        implements CompiledSchemaInternal {

    private final Map validatorPrototypes;

    public CompiledSchemaImpl(Map validatorPrototypes) {
        this.validatorPrototypes = validatorPrototypes;
    }

    public ElementValidator getElementValidator(ElementType type) {
        PrototypeElementValidator prototype = (PrototypeElementValidator)
                validatorPrototypes.get(type);
        if (prototype != null) {
            return prototype.createValidator();
        }

        return null;
    }

    public boolean containsElementType(ElementType type) {
        return validatorPrototypes.containsKey(type);
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
