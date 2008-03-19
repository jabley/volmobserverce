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

public class ContentChoiceValidator
        extends AbstractCompositeValidator {

    private ContentValidator currentActiveValidator;

    private boolean consumedContent;

    public ContentChoiceValidator(ContentValidator[] validators) {
        super(validators);
    }

    // Javadoc inherited.
    protected ContentValidator createValidator(ContentValidator[] validators) {
        return new ContentChoiceValidator(validators);
    }

    public ValidationEffect check(Content content, ValidationState state) {

        ContentValidator activeValidator = getActiveValidator(state);

        // If a validator is active then check to see whether it can handle
        // the event.
        ValidationEffect effect = null;
        ValidationEffect nestedEffect;
        if (activeValidator != null) {
            nestedEffect = activeValidator.check(content, state);
            if (nestedEffect.wasToConsume()) {
                if (nestedEffect.wasOrWouldBeToSatisfyValidator()) {
                    currentActiveValidator = null;
                    effect = nestedEffect;
                } else {
                    effect = ValidationEffect.CONSUMED;
                }
            } else {
                effect = nestedEffect;
            }

        } else {
            int size = validators.length;
            // Assume that if one of the validators does not consume the content
            // then all of them will be partially satisfied so this would be
            // partially satisfied.
            boolean partiallySatisfiable = true;
            for (int i = 0; i < size; i += 1) {
                ContentValidator validator = validators[i];

                nestedEffect = validator.check(content, state);
                if (nestedEffect.wasToConsume()) {

                    // Remember that this validator has consumed some content.
                    consumedContent = true;

                    // If this is in the initial state then reset the count of the
                    // number of satisfied nested validators.
                    if (state == ValidationState.INITIAL) {
                        resetAllApartFrom(validator);
                    }

                    if (nestedEffect.wasOrWouldBeToSatisfyValidator()) {
                        // The content had the effect of satisfying the nested
                        // validator so this validator is satisfied also.
                        effect = nestedEffect;
                    } else {
                        // The content did not satisfy the nested validator so
                        // set it as the active one and report that the content
                        // was consumed.
                        currentActiveValidator = validator;
                        effect = ValidationEffect.CONSUMED;
                    }

                } else if (nestedEffect == ValidationEffect.WOULD_FAIL) {
                    // The content would not satisfy the current validator, not
                    // even partially so the composite validator will not be
                    // partially satisfied.
                    partiallySatisfiable = false;
                }
            }

            if (effect == null) {
                if (partiallySatisfiable) {
                    effect = ValidationEffect.WOULD_SATISFY;
                } else {
                    effect = ValidationEffect.WOULD_FAIL;
                }
            }
        }

        return effect;
    }


    public void reset() {
        currentActiveValidator = null;
        consumedContent = false;
    }

    private ContentValidator getActiveValidator(ValidationState state) {
        ContentValidator activeValidator;
        if (state == ValidationState.CURRENT) {
            activeValidator = currentActiveValidator;
        } else if (state == ValidationState.INITIAL) {
            activeValidator = null;
        } else {
            throw new IllegalStateException(
                    "Unknown validation state " + state);
        }
        return activeValidator;
    }

    public boolean determineNextExpectedContent(
            Set nextExpectedContent, ValidationState state) {

        // If this is completely satisfied then it cannot consume any more
        // content.
        if (isCompletelySatisfied(state)) {
            return false;
        }

        // If there is an active validator then only the content that can next
        // be consumed by it is allowed, otherwise all the content that can
        // next be consumed by any of the validators is allowed.
        ContentValidator activeValidator = getActiveValidator(state);

        boolean satisfiable;
        if (activeValidator == null) {
            // If any of the validators are satisfiable then this validator is,
            // otherwise this is not.
            satisfiable = false;
            for (int i = 0; i < validators.length; i++) {
                ContentValidator validator = validators[i];
                boolean nestedSatisfiable =
                        validator.determineNextExpectedContent(
                                nextExpectedContent, state);
                satisfiable = satisfiable || nestedSatisfiable;
            }
        } else {
            satisfiable = activeValidator.determineNextExpectedContent(
                    nextExpectedContent, state);
        }

        return satisfiable;
    }

    protected boolean isCompletelySatisfied(ValidationState state) {
        return consumedContent && currentActiveValidator == null;
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
