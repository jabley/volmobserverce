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

public class ContentChoiceValidatorTestCase
        extends CompositeValidatorTestAbstract {

    protected ContentValidator createCompositeValidator(
            ContentValidator validator) {
        return new ContentChoiceValidator(new ContentValidator[]{validator});
    }

    /**
     * Check that when the nested validator has to consume more than one piece
     * of content in order to be satisfied that it becomes the active validator.
     */
    public void testMultipleContent() {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        expectations.add(
                new OrderedExpectations() {
                    public void add() {

                        // The first validator fails.
                        validator1Mock.expects
                                .check(contentMock, ValidationState.CURRENT)
                                .returns(ValidationEffect.WOULD_FAIL);

                        // The second validator consumes the first piece of
                        // content but is not satisfied by it.
                        validator2Mock.expects
                                .check(contentMock, ValidationState.CURRENT)
                                .returns(ValidationEffect.CONSUMED);

                        // The second validator consumes the second piece of
                        // content but is not satisfied by it.
                        validator2Mock.expects
                                .check(contentMock, ValidationState.CURRENT)
                                .returns(ValidationEffect.CONSUMED);

                        // The second validator consumes the second piece of
                        // content and is satisfied by it.
                        validator2Mock.expects
                                .check(contentMock, ValidationState.CURRENT)
                                .returns(ValidationEffect.CONSUMED_SATISFIED);
                    }
                });

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        ContentValidator validator = new ContentChoiceValidator(
                new ContentValidator[]{
                    validator1Mock,
                    validator2Mock,
                });

        ValidationEffect effect;

        effect = validator.check(contentMock, ValidationState.CURRENT);
        assertEquals("Should be valid", ValidationEffect.CONSUMED, effect);

        effect = validator.check(contentMock, ValidationState.CURRENT);
        assertEquals("Should be valid", ValidationEffect.CONSUMED, effect);

        effect = validator.check(contentMock, ValidationState.CURRENT);
        assertEquals(
                "Should be valid", ValidationEffect.CONSUMED_SATISFIED, effect);
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
