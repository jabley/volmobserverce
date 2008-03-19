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

package com.volantis.mcs.model.validation;

import java.util.List;

/**
 * 
 */
public interface Validator {

    /**
     * Validates the specified object.
     *
     * @return True if it was valid and false if it was not.
     */
    boolean validate(Validatable validatable);

    /**
     * Get the context within which the validation will be completed.
     *
     * @return The context.
     */
    ValidationContext getValidationContext();

    /**
     * Get the diagnostics.
     *
     * @return The list of {@link Diagnostic} objects, will be empty if no
     *         diagnostics were found.
     */
    List getDiagnostics();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 25-Oct-05	9961/1	pduffin	VBM:2005101811 Added diagnostic support and some commands

 ===========================================================================
*/
