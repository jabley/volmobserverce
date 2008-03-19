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

package com.volantis.mcs.runtime.policies;



/**
 * Responsible for fetching policies.
 *
 * <p>Implementations of this are responsible for handling all aspects of
 * policy fetching, including branding, fallback projects, document / policy
 * and project relative paths, absolute paths.</p>
 *
 * @mock.generate 
 */
public interface PolicyFetcher {

    /**
     * Fetch the referenced policy.
     *
     * @param reference The reference to the policy.
     * @return The policy referenced, or null if it could not be found.
     */
    ActivatedPolicy fetchPolicy(RuntimePolicyReference reference);
}
