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

import com.volantis.testtools.mock.expectations.OrderedExpectations;
import junit.framework.Assert;

public class BoundedContentValidatorTestCase
        extends CompositeValidatorTestAbstract {

    protected ContentValidator createCompositeValidator(
            ContentValidator validator) {
        return new BoundedContentValidator(validator, 1, 1);
    }

    /**
     * Check that if the nested validator fails but the minimum number is 0
     * then the composite would be satisfied.
     */
    public void testNestedFailsButAsOptionalSucceeds() {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        validator1Mock.expects.check(contentMock, ValidationState.CURRENT)
                .returns(ValidationEffect.WOULD_FAIL);

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        ContentValidator validator = new BoundedContentValidator(
                validator1Mock, 0, 1);

        ValidationEffect effect =
                validator.check(contentMock, ValidationState.CURRENT);
        assertEquals(
                "Should be valid", ValidationEffect.WOULD_SATISFY, effect);
    }

    /**
     * Check that if the min and max bounds are the same and the nested
     * validator is satisfied the same number of times then the composite is
     * completely satisfied.
     */
    public void testFixedBoundsSatisfied() {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        expectations.add(
                new OrderedExpectations() {
                    public void add() {

                        validator1Mock.expects.check(
                                contentMock, ValidationState.CURRENT)
                                .returns(ValidationEffect.CONSUMED_SATISFIED);

                        validator1Mock.expects.reset();

                        validator1Mock.expects.check(
                                contentMock, ValidationState.CURRENT)
                                .returns(ValidationEffect.CONSUMED_SATISFIED);
                    }
                });

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        ContentValidator validator = new BoundedContentValidator(
                validator1Mock, 2, 2);

        ValidationEffect effect;

        // The first piece of content should just be consumed.
        effect = validator.check(contentMock, ValidationState.CURRENT);
        assertEquals(
                "Should have consumed", ValidationEffect.CONSUMED, effect);

        // The second piece of content should have been consumed and satisfied.
        effect = validator.check(contentMock, ValidationState.CURRENT);
        assertEquals(
                "Should have consumed and satisfied",
                ValidationEffect.CONSUMED_SATISFIED, effect);

        // The composite should be completely satisfied.
        Assert.assertEquals(
                "Should be completely satisfied", SatisfactionLevel.COMPLETE,
                validator.checkSatisfactionLevel());
    }

    /**
     * Check that if the minimum number of iterations has been reached but the
     * maximum has not and the next piece of content is not consumed by the
     * nested validator that the composite reports that it would have been
     * satisfied.
     */
    public void testMinimumSatisfied() {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        expectations.add(
                new OrderedExpectations() {
                    public void add() {

                        validator1Mock.expects
                                .check(contentMock, ValidationState.CURRENT)
                                .returns(ValidationEffect.CONSUMED_SATISFIED);

                        validator1Mock.expects.reset();

                        validator1Mock.expects
                                .check(
                                        badContentMock,
                                        ValidationState.CURRENT)
                                .returns(ValidationEffect.WOULD_FAIL);
                    }
                });

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        ContentValidator validator = new BoundedContentValidator(
                validator1Mock, 1, 3);

        ValidationEffect effect;

        // The first piece of content should just be consumed.
        effect = validator.check(contentMock, ValidationState.CURRENT);
        assertEquals(
                "Should have consumed", ValidationEffect.CONSUMED, effect);

        // The second piece of content should satisfy but not be consumed.
        effect = validator.check(badContentMock, ValidationState.CURRENT);
        assertEquals(
                "Should have consumed and satisfied",
                ValidationEffect.WOULD_SATISFY, effect);
    }

    /**
     * Check that if the minimum number of iterations has not been reached
     * and the next piece of content is not consumed by the nested validator
     * that the composite reports that it would have failed.
     */
    public void testMinimumNotSatisfied() {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        expectations.add(
                new OrderedExpectations() {
                    public void add() {

                        validator1Mock.expects
                                .check(contentMock, ValidationState.CURRENT)
                                .returns(ValidationEffect.CONSUMED_SATISFIED);

                        validator1Mock.expects.reset();

                        validator1Mock.expects
                                .check(
                                        badContentMock,
                                        ValidationState.CURRENT)
                                .returns(ValidationEffect.WOULD_FAIL);
                    }
                });

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        ContentValidator validator = new BoundedContentValidator(
                validator1Mock, 2, 3);

        ValidationEffect effect;

        // The first piece of content should just be consumed.
        effect = validator.check(contentMock, ValidationState.CURRENT);
        assertEquals(
                "Should have consumed", ValidationEffect.CONSUMED, effect);

        // The second piece of content should satisfy but not be consumed.
        effect = validator.check(badContentMock, ValidationState.CURRENT);
        assertEquals(
                "Should have consumed and satisfied",
                ValidationEffect.WOULD_FAIL, effect);
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
