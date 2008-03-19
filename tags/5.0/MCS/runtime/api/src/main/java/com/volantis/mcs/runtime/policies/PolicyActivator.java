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

import com.volantis.mcs.policies.PolicyBuilder;
import com.volantis.mcs.runtime.RuntimeProject;

/**
 * This interface allows various implementations of a post-load ("activation")
 * processor to be defined. These operate on repository policies to undertake
 * some configuration-specific initialization type processing.
 *
 * @mock.generate
 */
public interface PolicyActivator {

    /**
     * The given policy's post-load initialization is performed
     * by this method.
     *
     * @param actualProject  The <code>Project</code> from which the policy was
     *                       retrieved.
     * @param policyBuilder  the policy to be post-load initialized
     * @param logicalProject The <code>Project</code> to which the policy
     *                       appears to belong..
     */
    ActivatedPolicy activate(
            RuntimeProject actualProject, PolicyBuilder policyBuilder,
            RuntimeProject logicalProject);
}
