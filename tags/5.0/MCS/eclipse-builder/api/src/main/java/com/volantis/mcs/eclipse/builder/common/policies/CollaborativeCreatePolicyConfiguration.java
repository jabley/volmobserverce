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
package com.volantis.mcs.eclipse.builder.common.policies;

import org.eclipse.core.runtime.IProgressMonitor;

/**
 * CreatePolicyConfiguration for collaborative projects
 */
public class CollaborativeCreatePolicyConfiguration
        extends GenericCreatePolicyConfiguration {

    /**
     * true if the policy should be kept locked after creation.
     */
    private final boolean keepLocked;

    /**
     * @param monitor The progress monitor to use (null if no progress
     *        monitoring is required)
     * @param keepLocked true if keep locked after creation
     */
    public CollaborativeCreatePolicyConfiguration(
            final IProgressMonitor monitor,
            final boolean keepLocked) {
        super(monitor);
        this.keepLocked = keepLocked;
    }

    /**
     * Returns true if the policy should be kept locked after creation.
     * @return if the policy should be kept locked
     */
    public boolean getKeepLocked() {
        return keepLocked;
    }
}
