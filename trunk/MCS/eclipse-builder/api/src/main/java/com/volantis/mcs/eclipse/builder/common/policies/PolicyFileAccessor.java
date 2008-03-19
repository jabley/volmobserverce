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

import com.volantis.mcs.interaction.Proxy;
import com.volantis.mcs.model.descriptor.ModelDescriptor;
import com.volantis.mcs.policies.PolicyBuilder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * An abstraction layer for manipulation of policy files, allowing high-level
 * operations (create, delete, move, load and save) to be carried out in
 * different ways depending on the project configuration.
 */
public interface PolicyFileAccessor {
    /**
     * Create a new policy at a specified location.
     *
     * @param location The location at which the policy file should be created
     * @param policy The policy to create
     * @param configuration The additional configuration info needed to perform
     * the create operation
     * @return IResource the created resource. May be null if the resource
     * could not be created
     * @throws PolicyFileAccessException if an error occurs
     */
    public IResource createPolicy(IPath location, PolicyBuilder policy, CreatePolicyConfiguration configuration) throws PolicyFileAccessException;

    /**
     * Delete a specified policy resource.
     *
     * @param policyResource The resource to delete
     * @throws PolicyFileAccessException if an error occurs
     */
    public void deletePolicy(IResource policyResource) throws PolicyFileAccessException;

    /**
     * Rename a policy.
     *
     * @param policyResource The resource to rename
     * @param destination The new location for the resource
     * @throws PolicyFileAccessException if an error occurs
     */
    public void renamePolicy(IResource policyResource, IPath destination) throws PolicyFileAccessException;

    /**
     * Save a policy to a specified location.
     *
     * @param policy The policy to save
     * @param policyResource The resource to which the policy should be saved
     * @param monitor The progress monitor to use for the save operation (may be null)
     * @throws PolicyFileAccessException if an error occurs
     */
    public void savePolicy(PolicyBuilder policy, IResource policyResource, IProgressMonitor monitor) throws PolicyFileAccessException;

    /**
     * Load a policy corresponding to a specified resource.
     *
     * @param policyResource The resource to load
     * @return The policy loaded from that resource
     * @throws PolicyFileAccessException if an error occurs
     */
    public PolicyBuilder loadPolicy(IResource policyResource) throws PolicyFileAccessException;

    /**
     * Wraps a policy builder in its interaction layer.
     *
     * <p>This should always be used to create the interaction layer for
     * policies, as it may carry out initialisation of its read/write state.</p>
     *
     * @param builder The builder to wrap
     * @param descriptor The model descriptor
     * @param project The project within which we are editing
     * @return A Proxy wrapping that builder
     * @throws PolicyFileAccessException if an error occurs
     */
    public Proxy wrapPolicy(PolicyBuilder builder, ModelDescriptor descriptor,
                            IProject project) throws PolicyFileAccessException;

    /**
     * Updates the state of a proxy based on the contained model and the state
     * of its related project.
     *
     * <p>In the current implementation, this will update the read/write state
     * of the proxy at all relevant levels if appropriate.</p>
     *
     * @param proxy A proxy wrapping a {@link PolicyBuilder}
     * @param project The project within which we are editing
     * @throws PolicyFileAccessException if an error occurs
     */
    public void updatePolicyProxyState(Proxy proxy, IProject project)
            throws PolicyFileAccessException;
}
