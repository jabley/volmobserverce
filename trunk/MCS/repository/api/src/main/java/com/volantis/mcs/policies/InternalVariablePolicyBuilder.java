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
package com.volantis.mcs.policies;

import com.volantis.mcs.model.validation.Pruneable;

/**
 * @mock.generate 
 */
public interface InternalVariablePolicyBuilder extends Pruneable {

    /**
     * Validate the object, and prune any components of the objects which are
     * invalid.
     */
    void validateAndPrune();

    /**
     * Compare two variable policy builders, ignoring any variants.
     * <p>
     * This is especially useful for the collaboration API in order to
     * decide if the policy part rather than the variant parts have changed.
     *
     * @param obj the object to compare to.
     * @return true if they are equal excluding variants.
     */
    boolean equalsExcludingVariants(Object obj);

}
