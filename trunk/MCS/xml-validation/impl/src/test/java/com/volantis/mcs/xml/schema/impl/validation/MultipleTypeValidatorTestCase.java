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

package com.volantis.mcs.xml.schema.impl.validation;

import com.volantis.mcs.xml.schema.model.Content;

import java.util.HashSet;
import java.util.Set;

/**
 * Tests for {@link MultipleTypeValidator}.
 */
public class MultipleTypeValidatorTestCase
        extends StatelessValidatorTestAbstract {

    protected Set validTypes;


    protected void setUp() throws Exception {
        super.setUp();

        validTypes = new HashSet();
        validTypes.add(element);
        validTypes.add(element2);
    }

    ContentValidator createPrototypeValidator() {
        return new MultipleTypeValidator(validTypes);
    }

    /**
     * Ensure that it validates the content correctly.
     */
    public void testValidation() {

        ContentValidator validator = new MultipleTypeValidator(validTypes);

        // Make sure that it can accept the elements it was created with.
        assertEquals(validator.check(element, ValidationState.CURRENT),
                ValidationEffect.CONSUMED_SATISFIED);
        assertEquals(validator.check(element2, ValidationState.CURRENT),
                ValidationEffect.CONSUMED_SATISFIED);

        // Make sure that it does not accept another element.
        assertEquals(validator.check(other, ValidationState.CURRENT),
                ValidationEffect.WOULD_FAIL);

        // Make sure that it does not accept PCDATA.
        assertEquals(validator.check(Content.PCDATA, ValidationState.CURRENT),
                ValidationEffect.WOULD_FAIL);
    }

}
