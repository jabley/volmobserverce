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

/**
 * Validates that a minimum number of pieces of content have been seen.
 */
public class BoundedContentValidator
        extends AbstractStatefulValidator {

    private final ContentValidator validator;

    private final int minimum;
    private final int maximum;

    /**
     * The number of times that the nested expectation has been satisfied.
     */
    private int currentIterationCount;

    /**
     * The number of events that have been processed by this expectation.
     */
    private int currentContentProcessedCount;

    public BoundedContentValidator(
            ContentValidator validator, int minimum, int maximum) {
        this.validator = validator;
        this.minimum = minimum;
        this.maximum = maximum;
    }

    public ContentValidator createValidator() {
        return new BoundedContentValidator(
                validator.createValidator(), minimum, maximum);
    }

    public boolean requiresPerElementState() {
        return true;
    }

    public ValidationEffect check(Content content, ValidationState state) {

        int contentProcessedCount;
        int iterationCount;
        if (state == ValidationState.INITIAL) {
            contentProcessedCount = 0;
            iterationCount = 0;
        } else if (state == ValidationState.CURRENT) {
            contentProcessedCount = currentContentProcessedCount;
            iterationCount = currentIterationCount;
        } else {
            throw new IllegalArgumentException("Unknown state " + state);
        }

        // The effect that the content will have.
        ValidationEffect effect = null;

        // Assess the effect on the nested validator.
        ValidationEffect nestedEffect = validator.check(content, state);

        if (nestedEffect.wasToConsume()) {
            // The nested validation consumed the content.

            if (nestedEffect.wasOrWouldBeToSatisfyValidator()) {
                // The nested validator has been satisfied so this iteration
                // has finished.
                currentIterationCount += 1;

                // Now check to see whether this has completely satisfied this
                // validator.
                if (currentIterationCount == maximum) {

                    // This is completely satisfied so report it.
                    effect = ValidationEffect.CONSUMED_SATISFIED;
                } else {

                    // This is not completely satisfied so reset the validator
                    // for next time and report that the content was consumed.

                    // Reset the nested validator.
                    validator.reset();

                    effect = ValidationEffect.CONSUMED;
                }
            } else {

                effect = nestedEffect;
            }

        } else {
            // The nested validation did not consume the content.

            effect = nestedEffect;
            if (!nestedEffect.wasOrWouldBeToSatisfyValidator()) {
                // The nested validator was not satisfied by the content so
                // check to see whether the

                if (iterationCount >= minimum) {
                    effect = ValidationEffect.WOULD_SATISFY;
                }
            }
        }
        if (!nestedEffect.wasToConsume()) {

            // The content was not consumed by the nested validator so there
            // are a couple of possible options.
            // 1 - The validator is partially, or completely satisfied


        }

//        if (nestedEffect == ValidationEffect.WOULD_FAIL) {
//            SatisfactionLevel satisfactionLevel;
//            if (state == ValidationState.CURRENT
//                    && (satisfactionLevel = validator.checkSatisfactionLevel())
//                    != SatisfactionLevel.UNSATISFIED) {
//
//                // Check to see whether we should do another iteration. If the
//                // nested validator is partially satisfied and this
//                // event cannot be processed then we can
//                effect = checkNextIteration(content, count, satisfactionLevel);
//
//            } else if (contentProcessedCount == 0 && minimum == 0) {
//                // The nested validators have not matched any events and is
//                // optional so will be satisfied by this event.
//                if (count != 0) {
//                    throw new IllegalStateException(
//                            "Count not zero - " + count);
//                }
//                effect = ValidationEffect.WOULD_SATISFY;
//            } else {
//                effect = ValidationEffect.WOULD_FAIL;
//            }
//        } else if (nestedEffect == ValidationEffect.CONSUMED) {
//
//            // Carry on.
//            effect = nestedEffect;
//
//        } else if (nestedEffect == ValidationEffect.WOULD_SATISFY) {
//            // This should probably behave just as in the situation when the
//            // nested validator fails because it is completely satisfied.
//
//            // Check to see whether we should do another iteration.
//            effect = checkNextIteration(
//                    content, count, SatisfactionLevel.PARTIAL);
//
//        } else {
//            throw new IllegalArgumentException("Unknown event effect "
//                    + nestedEffect);
//        }
//
//        if (effect.wasConsumed()) {
//
//            // If this has been called in the initial state then reset the
//            // state.
//            if (state == ValidationState.INITIAL) {
//                validator.reset();
//            }
//
//            currentContentProcessedCount += 1;
//
//            // If the validator was satisfied then
//            if (effect.satisfiedValidator()) {
//                currentIterationCount += 1;
//            }
//        }

        return effect;
    }

    /**
     * Checks the effect of the event on the nested validator when it is in
     * its initial state in order to determine whether it is appropriate to
     * start another loop.
     *
     * @param satisfactionLevel
     * @param content           The content to assess.
     * @return The assessment.
     */
    private ValidationEffect checkNextIteration(
            Content content, int count,
            SatisfactionLevel satisfactionLevel) {

        ValidationEffect effect;

        // The validator fails because it is completely satisfied,
        // and this is not being done in the initial state. As we know
        // that this validator is not completely satisfied (otherwise
        // the code should not have reached here) we should see whether
        // we should start another iteration in the loop by checking
        // the effect of the event in the initial state.
        ValidationEffect initialEffect = validator.check(
                content, ValidationState.INITIAL);

        if (initialEffect == ValidationEffect.WOULD_FAIL) {
            // The nested validator would fail if it processed the event.

            if (satisfactionLevel == SatisfactionLevel.PARTIAL) {
                // The nested validator is partially satisfied and the next
                // iteration cannot process this event so we must treat the
                // nested validator as if it was completely satisfied. This
                // means that the number of iterations that have been completed
                // needs increasing by one.
                count += 1;
            }

            // The event will cause the nested one to fail in the
            // initial state so we need to check whether the event will
            // partially satisfy this.
            if (count >= minimum) {
                effect = ValidationEffect.WOULD_SATISFY;
            } else {
                effect = ValidationEffect.WOULD_FAIL;
            }

        } else if (initialEffect == ValidationEffect.CONSUMED) {

            effect = initialEffect;

        } else if (initialEffect == ValidationEffect.WOULD_SATISFY) {

            // The event satisfies the nested on in its initial state
            // so it would satisfy it again and again for as many times
            // as necessary to satisfy this validator.
            effect = ValidationEffect.WOULD_SATISFY;

        } else {
            throw new IllegalArgumentException(
                    "Unknown event effect "
                    + initialEffect);
        }
        return effect;
    }

    public boolean determineNextExpectedContent(
            Set nextExpectedContent, ValidationState state) {

        // If this is completely satisfied then it cannot consume any more
        // content.
        if (isCompletelySatisfied(state)) {
            return false;
        }

        // The next allowable content types are the union of the follow:
        // 1) The nested validator's current allowable content types, which may
        //    be null if the validator is completely satisfied.
        //
        // 2) The nested validator's allowable content types if it was reset
        //    as long as the following hold:
        //    a) The state is current and not the initial state.
        //    b) The nested validator is at least partially satisfied.
        //    c) There is at least one more iteration remaining after completion
        //       of the current one.
        //
        boolean nestedSatisfiable = validator.determineNextExpectedContent(
                nextExpectedContent, state);

        if (state == ValidationState.CURRENT &&
                nestedSatisfiable && currentIterationCount + 1 < maximum) {
            validator.determineNextExpectedContent(
                    nextExpectedContent, ValidationState.INITIAL);
        }

        return nestedSatisfiable;
    }

    public void reset() {
        throw new UnsupportedOperationException();
    }

    protected boolean isCompletelySatisfied(ValidationState state) {
        return (currentIterationCount == maximum);
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
