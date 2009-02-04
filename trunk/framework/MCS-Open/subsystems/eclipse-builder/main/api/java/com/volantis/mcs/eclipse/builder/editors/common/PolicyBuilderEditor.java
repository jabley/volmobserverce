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
package com.volantis.mcs.eclipse.builder.editors.common;

import com.volantis.mcs.policies.PolicyBuilder;
import org.eclipse.core.resources.IProject;

/**
 * Common interface implemented by all editors for policy builders.
 */
public interface PolicyBuilderEditor {
    /**
     * Get the current state of the policy builder being edited.
     *
     * @return The current state of the policy builder
     */
    public PolicyBuilder getPolicyBuilder();

    /**
     * Set the policy builder being edited.
     *
     * <p>Note that this method should be used with care, as the policy builder
     * currently being edited by this editor will be lost.</p>
     */
    public void setPolicyBuilder(PolicyBuilder newBuilder);

    /**
     * Get the name of the policy being edited.
     *
     * @return The name of the policy being edited
     */
    public String getPolicyName();

    /**
     * Get the project with which the policy builder is associated.
     *
     * @return The project associated with this policy builder
     */
    public IProject getProject();

    /**
     * Get the context for this editor.
     *
     * @return The context for this editor
     */
    public EditorContext getContext();
}
