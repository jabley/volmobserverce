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

import com.volantis.mcs.xml.schema.model.ContentMock;
import com.volantis.synergetics.testtools.TestCaseAbstract;

public abstract class CompositeValidatorTestAbstract
        extends TestCaseAbstract {

    protected ContentValidatorMock validator1Mock;
    protected ContentValidatorMock validator2Mock;
    protected ContentMock contentMock;
    protected ContentMock badContentMock;

    protected void setUp() throws Exception {
        super.setUp();

        validator1Mock = new ContentValidatorMock(
                "validator1Mock", expectations);

        validator2Mock = new ContentValidatorMock(
                "validator2Mock", expectations);

        contentMock = new ContentMock("contentMock", expectations);

        badContentMock = new ContentMock("badContentMock", expectations);
    }

    protected abstract ContentValidator createCompositeValidator(
            ContentValidator validator);

    /**
     * Check that when a single validator consumes the content but does not
     * become completely satisfied in the process then the composite reports
     * that the content has been consumed but that it is not yet completely
     * satisfied.
     */
    public void testSingleConsumesNotSatisfied() {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        validator1Mock.expects.check(contentMock, ValidationState.CURRENT)
                .returns(ValidationEffect.CONSUMED);

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        ContentValidator validator = createCompositeValidator(validator1Mock);

        ValidationEffect effect = validator.check(contentMock,
                                                  ValidationState.CURRENT);
        assertEquals("Should be valid", ValidationEffect.CONSUMED, effect);
    }

    /**
     * Check that when a single validator consumes the content and is
     * satisifed in the process then the composite reports that the content
     * has been consumed and it is completely satisfied.
     */
    public void testSingleConsumesIsSatisfied() {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        validator1Mock.expects.check(contentMock, ValidationState.CURRENT)
                .returns(ValidationEffect.CONSUMED_SATISFIED);

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        ContentValidator validator = createCompositeValidator(validator1Mock);

        ValidationEffect effect = validator.check(contentMock,
                                                  ValidationState.CURRENT);
        assertEquals("Should be valid",
                     ValidationEffect.CONSUMED_SATISFIED, effect);
    }

    /**
     * Check that when a single validator would fail the composite would fail.
     */
    public void testSingleWouldFail() {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        validator1Mock.expects.check(contentMock, ValidationState.CURRENT)
                .returns(ValidationEffect.WOULD_FAIL);

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        ContentValidator validator = createCompositeValidator(validator1Mock);

        ValidationEffect effect = validator.check(
                contentMock, ValidationState.CURRENT);
        assertEquals("Should be valid", ValidationEffect.WOULD_FAIL, effect);
    }

    /**
     * Check that when a single validator would fail but be satisfied then
     * the composite would fail.
     */
    public void testSingleWouldSatisfy() {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        validator1Mock.expects.check(contentMock, ValidationState.CURRENT)
                .returns(ValidationEffect.WOULD_SATISFY);

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        ContentValidator validator = createCompositeValidator(validator1Mock);

        ValidationEffect effect = validator.check(
                contentMock, ValidationState.CURRENT);
        assertEquals("Should be valid", ValidationEffect.WOULD_SATISFY, effect);
    }

    /**
     * Check that when creating a validator it creates copies of the validators.
     */
    public void testCopiesNestedValidators() {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        // Validator 1 is referenced from the prototype so it is asked to create
        // a new copy of itself.
        validator1Mock.expects.createValidator().returns(validator2Mock);

        // Validator 2 is the new copy and it should be used by the newly
        // created validator.
        validator2Mock.expects.check(contentMock, ValidationState.CURRENT)
                .returns(ValidationEffect.CONSUMED);

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        ContentValidator prototype = createCompositeValidator(validator1Mock);
        ContentValidator validator = prototype.createValidator();

        ValidationEffect effect = validator.check(
                contentMock, ValidationState.CURRENT);
        assertEquals("Should be valid", ValidationEffect.CONSUMED, effect);
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
