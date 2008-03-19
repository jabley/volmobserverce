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

package com.volantis.mcs.xml.schema.impl.validation;

import com.volantis.mcs.xml.schema.model.Content;

import java.util.Set;

public class UnlimitedValidator
    implements ContentValidator {

    private final ContentValidator validator;

    public UnlimitedValidator(ContentValidator validator) {
        if (validator.requiresPerElementState()) {
            throw new IllegalArgumentException(
                    "Only supports stateless validators");
        }
        this.validator = validator;
    }

    // Javadoc inherited.
    public ContentValidator createValidator() {
        return this;
    }

    // Javadoc inherited.
    public boolean requiresPerElementState() {
        return false;
    }

    // Javadoc inherited.
    public ValidationEffect check(Content content, ValidationState state) {
        ValidationEffect effect = validator.check(content, state);
        if (effect == ValidationEffect.WOULD_FAIL) {
            effect = ValidationEffect.WOULD_SATISFY;
        } else if (effect == ValidationEffect.CONSUMED_SATISFIED) {
            // The underlying validator consumed it and was satisfied but as
            // this validator can never be satisfied by consuming an event
            // indicate that it is consumed.
            effect = ValidationEffect.CONSUMED;
        }
        
        return effect;
    }

    public boolean determineNextExpectedContent(
            Set nextExpectedContent, ValidationState state) {

        validator.determineNextExpectedContent(nextExpectedContent, state);

        // This is always satisfiable.
        return true;
    }

    // Javadoc inherited.
    public SatisfactionLevel checkSatisfactionLevel() {
        // This can never be completely satisfied.
        return SatisfactionLevel.PARTIAL;
    }

    // Javadoc inherited.
    public void reset() {
        // Nothing to do.
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 01-Dec-05	9839/1	geoff	VBM:2005101702 Fix the XDIME2 Object element

 10-Oct-05	9673/1	pduffin	VBM:2005092906 Improved validation and fixed layout formatting

 ===========================================================================
*/
