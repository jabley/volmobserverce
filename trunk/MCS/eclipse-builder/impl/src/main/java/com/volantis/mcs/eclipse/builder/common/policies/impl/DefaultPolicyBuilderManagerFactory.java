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
package com.volantis.mcs.eclipse.builder.common.policies.impl;

import org.eclipse.core.resources.IProject;
import com.volantis.mcs.project.PolicyBuilderManager;
import com.volantis.mcs.eclipse.builder.common.policies.PolicyBuilderManagerFactory;
import com.volantis.mcs.eclipse.builder.common.ClassVersionProperties;

/**
 */
public class DefaultPolicyBuilderManagerFactory extends PolicyBuilderManagerFactory {
    public PolicyBuilderManager createPolicyBuilderManager(IProject project) {
        return (PolicyBuilderManager) ClassVersionProperties.
                getInstance("PolicyBuilderManager.class",
                        new Class[] { IProject.class }, new Object[] {project});
    }
}
