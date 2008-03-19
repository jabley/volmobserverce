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
/** (c) Volantis Systems Ltd 2004.  */
package com.volantis.mcs.project;

import com.volantis.mcs.policies.PolicyBuilderReader;

/**
 * A PolicySource represents the location for of policies within a project.
 *
 * @mock.generate
 */
public interface PolicySource {

    /**
     * Create a {@link PolicyBuilderManager} that can be used to manage
     * policies in this policy source.
     *
     * @return A newly created {@link PolicyBuilderManager}, or null if this
     *         policy source does not support managing of the policies, e.g. if
     *         it is remote.
     * @param project
     */
    PolicyBuilderManager createPolicyBuilderManager(InternalProject project);

    /**
     * Get the {@link PolicyBuilderReader} to use for accessing policies from
     * this source.
     *
     * <p>The returned instance is thread safe.</p>
     *
     * @return The {@link PolicyBuilderReader}.
     */
    PolicyBuilderReader getPolicyBuilderReader();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 27-Jan-04	2769/1	mat	VBM:2004012702 Add PolicySource

 ===========================================================================
*/
