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

package com.volantis.mcs.model.impl.validation;

import com.volantis.mcs.model.ModelFactory;
import com.volantis.mcs.model.validation.Validatable;
import com.volantis.mcs.model.validation.Validator;
import com.volantis.mcs.model.validation.ValidationContext;

import java.util.List;

public class ValidatorImpl
        implements Validator {

    private final ModelFactory modelFactory;

    private ValidationContextImpl context;
    private List diagnostics;

    public ValidatorImpl(ModelFactory modelFactory) {
        this.modelFactory = modelFactory;
        context = new ValidationContextImpl(modelFactory);
    }

    public boolean validate(Validatable validatable) {
        validatable.validate(context);

        diagnostics = context.getDiagnostics();

        return diagnostics.isEmpty();
    }

    public ValidationContext getValidationContext() {
        return context;
    }

    public List getDiagnostics() {
        return diagnostics;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 31-Oct-05	9961/3	pduffin	VBM:2005101811 Committing restructuring

 25-Oct-05	9961/1	pduffin	VBM:2005101811 Added diagnostic support and some commands

 ===========================================================================
*/
