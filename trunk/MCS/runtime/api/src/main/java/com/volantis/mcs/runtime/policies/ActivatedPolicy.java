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

import com.volantis.mcs.policies.Policy;
import com.volantis.mcs.runtime.RuntimeProject;

/**
 * A {@link Policy} that has been activated for use at runtime.
 *
 * <p>An activated policy extends policy with the logical and the actual
 * projects with which the policy is associated. These will be the same unless
 * the logical project extends another project in which case they can be
 * different.</p>
 *
 * <p>Project extension (otherwise known as project fallback) is akin to
 * deriving a Java class from another. i.e.
 * <ul>
 * <li>The base project is equivalent to the Java base class.</li>
 * <li>The derived project is equivalent to the derived Java class.</li>
 * <li>The policies are equivalent to Java methods.</li>
 * <li>Policy fetching is equivalent to Java method dispatching.</li>
 * </ul>
 *
 * <p>A policy reference contains the project to which the policy logically
 * belongs. Fetching the policy for that reference first looks in the logical
 * project. If it is found there then it is returned, otherwise the base
 * project is searched. This continues until either the policy is found or
 * there is no base project. If the policy is not found then nothing more is
 * done. If however it is found then the project within which it was
 * contained is the actual project for that policy and the policy is
 * activated.</p>
 *
 * <p>Some of the activating involves creating runtime representations of the
 * policy in order to enhance performance but most of it involves ensuring
 * that the references are activated.</p>
 *
 * <p>Activating a reference involves making sure that when the reference is
 * resolved that it will be resolved against the logical project. This simply
 * involves storing the logical project in the references and not the actual
 * project.</p>
 *
 * <p>The actual project is needed when creating the URL for a resource
 * referenced from a Variant.</p>
 *
 * <p>
 * <strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels. </strong>
 * </p>
 *
 * @mock.generate base="Policy" 
 */
public interface ActivatedPolicy
        extends Policy {

    /**
     * Get the actual project.
     *
     * @return The actual project.
     */
    RuntimeProject getActualProject();

    /**
     * Get the logical project.
     *
     * @return The logical project.
     */
    RuntimeProject getLogicalProject();
}
