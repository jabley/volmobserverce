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

package com.volantis.mcs.xml.schema.impl.compiler;

import com.volantis.mcs.xml.schema.impl.validation.AbstractElementValidator;
import com.volantis.mcs.xml.schema.impl.validation.ElementValidator;
import com.volantis.mcs.xml.schema.model.Content;
import com.volantis.mcs.xml.schema.model.ElementType;
import com.volantis.mcs.xml.schema.validation.ValidationException;

import java.util.Set;

/**
 * An element validator that delaegates all its validating calls to the
 * containing validator.
 *
 * <p>This behaves as if this validator was not actually present. Because it
 * contains a reference to a containing validator instances of this are
 * stateful.</p>
 */
public class TransparentElementValidator
        extends AbstractElementValidator {

    /**
     * THe containing validator.
     */
    private ElementValidator containingValidator;

    /**
     * Initialise.
     *
     * @param type
     * @param exclusions
     * @param useAnywhere
     */
    public TransparentElementValidator(
            ElementType type, Set exclusions,
            boolean useAnywhere) {

        super(type, null, exclusions, useAnywhere, false /* Never used */);
    }

    // Javadoc inherited.
    public ElementValidator createValidator() {
        return new TransparentElementValidator(
                elementType, exclusions, useAnywhere);
    }

    public ElementType getValidatingElementType() {
        return containingValidator == null ? null :
                containingValidator.getValidatingElementType();
    }

    // Javadoc inherited.
    public void open(ElementValidator containingValidator) {
        this.containingValidator = containingValidator;
        super.open(containingValidator);
    }

    // Javadoc inherited.
    public boolean content(Content content, boolean required)
            throws ValidationException {

        if (containingValidator == null) {
            return required;
        } else {
            return containingValidator.content(content, required);
        }
    }

    // Javadoc inherited.
    public void close()
            throws ValidationException {
        containingValidator = null;
    }
}
