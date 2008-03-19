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


/**
 * A reference to a policy.
 *
 * <p>A policy is uniquely identified by its name (which is actually a URL) and
 * if the name is relative to the project then its project is needed. In future,
 * all projects within MCS will have a unique project root, even local projects
 * and then the name will be sufficient. For this reason the policy reference
 * does not contain a reference to the project. Project relative names are
 * assumed to refer to a policy within the same project as the policy or page
 * that contains the reference.</p>
 *
 * <p>It is not generally possible to tell the type of policy from its name,
 * in fact this is only possible for policies stored locally as they have to
 * have the appropriate extension for the policy type. However, there are no
 * such restrictions placed on remote policy names. The only way to determine
 * the policy type is to actually retrieve it. As many references have to be of
 * a specific type a reference also can contain an optional expected type. If
 * when the policy is retrieved it does not match the expected type then it
 * behaves as if it did not exist.</p>
 *
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 * @mock.generate
 * @since 3.5.1
 */
public interface PolicyReference {

    /**
     * Get the name of the policy being referenced.
     *
     * @return The name of the policy being referenced.
     */
    String getName();

    /**
     * Get the expected type of the policy being referenced.
     *
     * <p>In general it is not possible to determine the type of policy
     * referenced without actually retrieving it. The file extension of a local
     * policy will uniquely identify the type of the policy but as there is no
     * way of determining whether a policy reference will be used as a local or
     * remote no validation can be done to make sure that the policy type
     * matches the real type of the policy.</p>
     *
     * @return The expected type of the policy being referenced.
     */
    PolicyType getExpectedPolicyType();

    /**
     * Two policy references are equal if within the same context they refer
     * to the same policy.
     */
    boolean equals(Object object);

    /**
     * Generate a hash code for the reference that is compliant with
     * {@link #equals(Object)}.
     */
    int hashCode();
}
