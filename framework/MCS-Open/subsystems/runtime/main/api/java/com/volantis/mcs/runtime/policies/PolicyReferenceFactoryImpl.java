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

import com.volantis.mcs.runtime.RuntimeProject;
import com.volantis.mcs.runtime.project.ProjectManager;
import com.volantis.mcs.policies.PolicyType;
import com.volantis.mcs.utilities.MarinerURL;

public class PolicyReferenceFactoryImpl
        implements PolicyReferenceFactory {

    private final ProjectManager projectManager;

    public PolicyReferenceFactoryImpl(ProjectManager projectManager) {
        this.projectManager = projectManager;
    }

    public RuntimePolicyReference createNormalizedReference(
            RuntimeProject project, String name, PolicyType expectedPolicyType) {
        return new NormalizedPolicyReferenceImpl(project, name,
                expectedPolicyType);
    }

    public RuntimePolicyReference createLazyNormalizedReference(
            RuntimeProject project, MarinerURL baseURL, String name,
            PolicyType expectedPolicyType) {
        return new RuntimePolicyReferenceImpl(project, baseURL, name,
                expectedPolicyType, projectManager);
    }

    public RuntimePolicyReference createNormalizedReference(
            RuntimeProject project, MarinerURL baseURL, String name,
            PolicyType expectedPolicyType) {

        RuntimePolicyReferenceImpl reference = new RuntimePolicyReferenceImpl(
                project, baseURL, name, expectedPolicyType,
                projectManager);

        return reference.getNormalizedReference();
    }
}
