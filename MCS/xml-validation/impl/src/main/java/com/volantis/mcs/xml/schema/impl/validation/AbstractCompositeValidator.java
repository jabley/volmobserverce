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



public abstract class AbstractCompositeValidator
        extends AbstractStatefulValidator {

    protected final ContentValidator[] validators;

    public AbstractCompositeValidator(ContentValidator[] validators) {
        this.validators = validators;
    }

    public ContentValidator createValidator() {
        // Copy the array and populate it with new validators.
        int length = validators.length;
        ContentValidator[] newValidators = new ContentValidator[length];
        for (int i = 0; i < length; i++) {
            ContentValidator validator = validators[i];
            newValidators[i] = validator.createValidator();
        }

        return createValidator(newValidators);
    }

    /**
     * Create a new validator from the array of validators.
     *
     * @param validators The array of newly created validators.
     *
     * @return A new validator.
     */
    protected abstract
    ContentValidator createValidator(ContentValidator[] validators);

    public boolean requiresPerElementState() {
        return true;
    }

    /**
     * Reset the state of this and all nested validators.
     *
     * @param alreadyReset The validator that has already been reset as a
     *                     result of processing an content in the initial state. This will be
     *                     null if all nested validators should be reset.
     */
    protected void resetAllApartFrom(ContentValidator alreadyReset) {
        int size = validators.length;
        for (int i = 0; i < size; i += 1) {
            ContentValidator validator = validators[i];
            if (validator != alreadyReset) {
                validator.reset();
            }
        }
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
