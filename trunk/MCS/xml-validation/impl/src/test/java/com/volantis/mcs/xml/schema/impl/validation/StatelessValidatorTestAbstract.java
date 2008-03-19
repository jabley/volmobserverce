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

import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.mcs.xml.schema.model.ElementType;

/**
 * Common tests for classes derived from {@link StatelessValidator}.
 */
public abstract class StatelessValidatorTestAbstract
        extends TestCaseAbstract {

    protected ElementType element = new ElementType("ns", "local");
    protected ElementType element2 = new ElementType("ns", "local2");
    protected ElementType other = new ElementType("ns", "other");

    abstract ContentValidator createPrototypeValidator();

    /**
     * Ensure that the prototype returns itself.
     */
    public void testCreateValidator() {

        ContentValidator prototype = createPrototypeValidator();
        ContentValidator validator = prototype.createValidator();
        assertSame(validator, prototype);
    }

    /**
     * Ensure that the prototype does not need per element state.
     */
    public void testNeedsPerElementState() {

        ContentValidator prototype = createPrototypeValidator();
        assertFalse(prototype.requiresPerElementState());
    }
}
