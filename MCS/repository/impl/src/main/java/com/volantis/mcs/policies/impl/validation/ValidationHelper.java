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

package com.volantis.mcs.policies.impl.validation;

import com.volantis.mcs.model.validation.DiagnosticLevel;
import com.volantis.mcs.model.validation.SourceLocation;
import com.volantis.mcs.model.validation.ValidationContext;

/**
 * Contains common validation methods.
 */
public class ValidationHelper {

    /**
     * Check that the percentage is within the expected range of 1 to 100.
     *
     * @param context         The context to which diagnostics are reported.
     * @param sourceLocation  The location of the item being checked in the
     *                        source.
     * @param percentage      The percentage value to check.
     * @param rangeMessageKey The key to use for the out of range message.
     */
    public static void checkPercentage(
            ValidationContext context, SourceLocation sourceLocation,
            int percentage, String rangeMessageKey) {

        if (percentage < 0 || percentage > 100) {
            context.addDiagnostic(sourceLocation, DiagnosticLevel.ERROR,
                    context.createMessage(rangeMessageKey,
                            new Integer(percentage)));
        }
    }
}
