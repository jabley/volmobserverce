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

import junit.framework.Assert;


public class ContentSequenceValidatorTestCase
        extends CompositeValidatorTestAbstract {

    protected ContentValidator createCompositeValidator(
            final ContentValidator validator) {
        return new ContentSequenceValidator(
                new ContentValidator[]{validator});
    }

    /**
     * Check that when the first validator consumes and is satisfied and the
     * second consumed and is satisfied them the composite reports that
     * it consumed and is satisfied.
     */
    public void testFirstFailsSecondMatches() {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        validator1Mock.expects.check(contentMock, ValidationState.CURRENT)
                .returns(ValidationEffect.CONSUMED_SATISFIED);

        validator2Mock.expects.check(contentMock, ValidationState.CURRENT)
                .returns(ValidationEffect.CONSUMED_SATISFIED);

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        ContentValidator validator = new ContentSequenceValidator(
                new ContentValidator[]{
                    validator1Mock,
                    validator2Mock
                });

        ValidationEffect effect;

        effect = validator.check(contentMock, ValidationState.CURRENT);
        assertEquals("Should be consumed", ValidationEffect.CONSUMED, effect);

        effect = validator.check(contentMock, ValidationState.CURRENT);
        assertEquals(
                "Should be consumed and satisfied",
                ValidationEffect.CONSUMED_SATISFIED, effect);
        Assert.assertEquals(
                "Should be completely satisfied", SatisfactionLevel.COMPLETE,
                validator.checkSatisfactionLevel());
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
