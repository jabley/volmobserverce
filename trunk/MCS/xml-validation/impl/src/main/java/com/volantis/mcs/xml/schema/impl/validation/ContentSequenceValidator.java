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

public class ContentSequenceValidator
        extends AbstractCompositeValidator {

    /**
     * The position of the next validator to satisfy.
     */
    private int currentPosition;

    public ContentSequenceValidator(ContentValidator[] validators) {
        super(validators);
    }

    // Javadoc inherited.
    protected ContentValidator createValidator(ContentValidator[] validators) {
        return new ContentSequenceValidator(validators);
    }

    public ValidationEffect check(Content content, ValidationState state) {

        int position = getPosition(state);

        // Try all the remaining unsatisfied validators until we find one
        // that will fail, or consume the event.
        ValidationEffect effect = ValidationEffect.WOULD_SATISFY;
        int size = validators.length;
        for (int i = position;
             i < size && effect == ValidationEffect.WOULD_SATISFY; i += 1) {

            // Check to see if the next unsatisfied validator can consume the
            // event.
            ContentValidator validator = validators[i];
            effect = validator.check(content, state);

            // If the content was consumed by this validator then update
            // the position to ensure that when the event is processed that
            // it will be processed by the correct validator.
            if (effect.wasToConsume()) {

                if (state == ValidationState.INITIAL) {
                    // The event was consumed and this is the start of a loop
                    // so reset all the validators apart from the one that
                    // just consumed the content as it will have reset itself
                    // just like this validator is doing right now.
                    resetAllApartFrom(validator);
                }

                currentPosition = i;

                // If consuming the event means that the validator is now satisfied
                // move onto the next validator.
                if (effect.wasOrWouldBeToSatisfyValidator()) {
                    currentPosition += 1;

                    // Check to see whether this validator is satisfied because
                    // if it is not then it needs to report that the content
                    // was consumed, rather than consumed and satisfied.
                    if (currentPosition < size) {
                        effect = ValidationEffect.CONSUMED;
                    }
                }
            }
        }

        return effect;
    }


    public void reset() {
        currentPosition = 0;
    }

    private int getPosition(ValidationState state) {
        int position;
        if (state == ValidationState.INITIAL) {
            position = 0;
        } else if (state == ValidationState.CURRENT) {
            position = currentPosition;
        } else {
            throw new IllegalArgumentException(
                    "Unknown validator state " + state);
        }
        return position;
    }

    public boolean determineNextExpectedContent(
            Set nextExpectedContent, ValidationState state) {

        // If this is completely satisfied then it cannot consume any more
        // content.
        if (isCompletelySatisfied(state)) {
            return false;
        }

        // Iterate over the not yet completely satisfied validators adding
        // their content into the set until either the end is reached, or one
        // of the validators would not be at least partially satisfied by it.
        int position = getPosition(state);
        boolean satisfiable = true;
        int size = validators.length;
        for (int i = position;
             i < size && satisfiable; i += 1) {

            ContentValidator validator = validators[i];
            satisfiable =
                    validator.determineNextExpectedContent(
                            nextExpectedContent, state);
        }

        // If the last validator visited was satisfiable then this is
        // satisfiable, otherwise it is not.
        return satisfiable;
    }

    protected boolean isCompletelySatisfied(ValidationState state) {
        int position = getPosition(state);

        return (position == validators.length);
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
