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
 * A sub-interface of {@link Validatable} for classes within a model which
 * require their invalid components to be pruned from the model as part of
 * validation.
 */
public interface Pruneable extends Validatable {

    /**
     * Prune any invalid components.
     * <p>
     * This will be called after the validation has run but before the
     * diagnostics have been reported.
     * <p>
     * Any components which have error diagnostics registered for them
     * should be removed, and those diagnostics marked as pruned to prevent
     * them causing the validation to fail.
     *
     * @param validationContext the context of a validation which has been
     *      applied.
     * @param diagnostics the diagnostics which resulted from the validation.
     */
    void prune(ValidationContext validationContext, List diagnostics);
}
