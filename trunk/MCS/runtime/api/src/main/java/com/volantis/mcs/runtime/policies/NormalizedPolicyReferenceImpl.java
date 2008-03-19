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

import com.volantis.mcs.policies.PolicyType;
import com.volantis.mcs.project.Project;

/**
 * A fully normalized {@link RuntimePolicyReference}.
 */
public class NormalizedPolicyReferenceImpl
        implements RuntimePolicyReference {

    private final String name;
    private final Project project;
    private final PolicyType expectedPolicyType;
    private final boolean brandable;

    public NormalizedPolicyReferenceImpl(
            Project project, String name,
            PolicyType expectedPolicyType,
            boolean brandable) {
        this.name = name;
        this.project = project;
        this.expectedPolicyType = expectedPolicyType;
        this.brandable = brandable;

    }

    public NormalizedPolicyReferenceImpl(
            Project project, String name,
            PolicyType expectedPolicyType) {
        this(project, name, expectedPolicyType, false);
    }

    public String getName() {
        return name;
    }

    public Project getProject() {
        return project;
    }

    public PolicyType getExpectedPolicyType() {
        return expectedPolicyType;
    }

    public boolean isBrandable() {
        return brandable;
    }
}
