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
 * CreatePolicyConfiguration that contains the common implementation for both
 * collaborative and standalone versions.
 */
public class GenericCreatePolicyConfiguration
        implements CreatePolicyConfiguration {

    private final IProgressMonitor monitor;

    /**
     * Create a CreatePolicyConfiguration instance.
     *
     * @param monitor runnable monitor to be used
     */
    public GenericCreatePolicyConfiguration(final IProgressMonitor monitor) {
        this.monitor = monitor;
    }

    // javadoc inherited
    public IProgressMonitor getProgressMonitor() {
        return monitor;
    }
}
