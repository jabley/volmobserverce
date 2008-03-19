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

import com.volantis.mcs.eclipse.builder.common.policies.PolicyFileAccessorFactory;
import com.volantis.mcs.eclipse.builder.common.policies.PolicyFileAccessor;
import com.volantis.mcs.eclipse.builder.common.ClassVersionProperties;
import com.volantis.mcs.eclipse.core.MCSProjectNature;
import org.eclipse.core.resources.IProject;

/**
 * The default implementation of the policy file accessor factory.
 *
 * <p>Creates either a collaborative or standalone policy file accessor
 * depending on how the project has been configured.</p>
 */
public class DefaultPolicyFileAccessorFactory extends PolicyFileAccessorFactory {
    // Javadoc inherited
    public PolicyFileAccessor getPolicyFileAccessor(IProject project) {
        return new StandalonePolicyFileAccessor();
    }
}
