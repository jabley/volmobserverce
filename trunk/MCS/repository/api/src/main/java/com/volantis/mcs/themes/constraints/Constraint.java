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
package com.volantis.mcs.themes.constraints;

import com.volantis.mcs.model.validation.SourceLocation;
import com.volantis.mcs.model.validation.Validatable;

/**
 */
public interface Constraint extends SourceLocation, Validatable {
    /**
     * Gets the value against which the attribute value is being tested (may be
     * null because it's not always required).
     *
     * @return the value against which the attribute is being tested.
     */
    String getValue();

    /**
     * Set the value against which the attribute value should be tested.
     *
     * @param value against which the attribute is being tested.
     */
    void setValue(String value);

    /**
     * Evaluate the constraint and determine whether it is satisfied.
     *
     * @param actualValue        The value to test, may be null.
     *
     * @return True if the constraint is satisfied, false if it is not.
     */
    boolean evaluate(String actualValue);

    /**
     * Returns true if this constraint requires a value and false otherwise.
     *
     * @return true if this constraint requires a value and false otherwise.
     */
    boolean requiresValue();
}
