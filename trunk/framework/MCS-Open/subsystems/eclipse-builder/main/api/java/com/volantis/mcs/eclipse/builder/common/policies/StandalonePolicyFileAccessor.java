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

import com.volantis.mcs.accessors.xml.jibx.JiBXReader;
import com.volantis.mcs.accessors.xml.jibx.JiBXWriter;
import com.volantis.mcs.eclipse.builder.common.policies.CreatePolicyConfiguration;
import com.volantis.mcs.eclipse.builder.common.policies.PolicyFileAccessException;
import com.volantis.mcs.eclipse.builder.common.policies.PolicyFileAccessor;
import com.volantis.mcs.interaction.InteractionFactory;
import com.volantis.mcs.interaction.InteractionModel;
import com.volantis.mcs.interaction.Proxy;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.model.descriptor.ModelDescriptor;
import com.volantis.mcs.policies.InternalPolicyFactory;
import com.volantis.mcs.policies.PolicyBuilder;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.shared.content.BinaryContentInput;
import com.volantis.shared.content.ContentInput;
import com.volantis.synergetics.log.LogDispatcher;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceStatus;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.ui.dialogs.ContainerGenerator;
import com.volantis.mcs.eclipse.builder.common.policies.AbstractPolicyFileAccessor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;

/**
 * Policy file accessor for standalone (ie. non-collaborative) projects.
 */
public class StandalonePolicyFileAccessor extends AbstractPolicyFileAccessor
        implements PolicyFileAccessor{
	
    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
        LocalizationFactory.createLogger(StandalonePolicyFileAccessor.class);
	
    // Javadoc inherited
    public IResource createPolicy(IPath location, PolicyBuilder policy,
                                  CreatePolicyConfiguration config)
            throws PolicyFileAccessException {
        try {
            JiBXWriter writer = new JiBXWriter(false);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(baos);
            writer.write(osw, policy);
            InputStream is = new ByteArrayInputStream(baos.toByteArray());
            return createNewPolicy(is, location, config.getProgressMonitor(),
                    policy.getName());
        } catch (RepositoryException re) {
            throw new PolicyFileAccessException(re);
        }
    }

    // Javadoc inherited
    public void deletePolicy(IResource policyResource) throws PolicyFileAccessException {
        try {
            policyResource.delete(true, new NullProgressMonitor());
        } catch (CoreException ce) {
            throw new PolicyFileAccessException(ce);
        }
    }

    // Javadoc inherited
    public void renamePolicy(IResource policyResource, IPath destination) {
        throw new UnsupportedOperationException("This operation not yet supported");
    }

    // Javadoc inherited
    public void savePolicy(PolicyBuilder policy, IResource policyResource, IProgressMonitor monitor) throws PolicyFileAccessException {
        if (!(policyResource instanceof IFile)) {
            throw new IllegalStateException("Can only save policies to files");
        }

        try {
            IFile file = (IFile) policyResource;
            StringWriter saved = new StringWriter();
            JiBXWriter writer = new JiBXWriter();
            writer.write(saved, policy);
            InputStream input = new ByteArrayInputStream(saved.toString().getBytes("UTF-8"));
            if (file.exists()) {
                file.setContents(input, IResource.KEEP_HISTORY, monitor);
            } else {
                file.create(input, IResource.KEEP_HISTORY, monitor);
            }
        } catch (UnsupportedEncodingException e) {
            // Needs to be catch due to getBytes("UTF-8")
            // Should never get an UnsupportedEncodingException cause UTF-8 is valid encoding
            logger.error("encoding-not-supported",e);
        } catch (CoreException ce) {
            throw new PolicyFileAccessException(ce);
        } catch (RepositoryException re) {
            throw new PolicyFileAccessException(re);
        }
    }

    // Javadoc inherited
    public PolicyBuilder loadPolicy(IResource policyResource) throws PolicyFileAccessException {
        if (!(policyResource instanceof IFile)) {
            throw new IllegalStateException("Can only read policies from files");
        }

        InputStream is = null;
        try {
            IFile file = (IFile) policyResource;

            // Read the model using JiBX
            is = file.getContents();
            ContentInput content = new BinaryContentInput(is);
            InternalPolicyFactory policyFactory =
                    InternalPolicyFactory.getInternalInstance();

            // Don't use schema validation, as we may have a partial layout,
            // with missing attributes and empty formats.
            // todo: re-enable validation by avoiding this overload once we have sorted out validation.
            JiBXReader reader =
                    policyFactory.createDangerousNonValidatingPolicyReader();

            String name = getPolicyName(policyResource);

            PolicyBuilder model =
                    (PolicyBuilder) reader.read(content, file.getName());
            model.setName(name);
            return model;
        } catch (CoreException ce) {
            throw new PolicyFileAccessException(ce);
        } catch (IOException e) {
            throw new PolicyFileAccessException(e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    throw new IllegalStateException(
                        "Cannot close input stream for " + policyResource.getName());
                }
            }
        }
    }

    // Javadoc inherited
    public Proxy wrapPolicy(PolicyBuilder policy, ModelDescriptor descriptor,
                            IProject project) throws PolicyFileAccessException {
        InteractionFactory interactionFactory =
                InteractionFactory.getDefaultInstance();
        InteractionModel interactionModel = interactionFactory
                .createInteractionModel(descriptor);
        return interactionModel.createProxyForModelObject(policy);
    }

    // Javadoc inherited
    public void updatePolicyProxyState(Proxy proxy, IProject project)
            throws PolicyFileAccessException {
        // For standalone projects there is no update required
    }

    /**
     * Creates a new policy resource in the selected container and with the selected
     * name. Creates any missing resource containers along the path; does nothing if
     * the container resources already exist.
     * <p>
     * In normal usage, this method is invoked after the user has pressed Finish on
     * the wizard; the enablement of the Finish button implies that all controls on
     * on this page currently contain valid values.
     * </p>
     * <p>
     * Note that this page caches the new policy once it has been successfully
     * created; subsequent invocations of this method will answer the same
     * file resource without attempting to create it again.
     * </p>
     *
     * @return the created file resource, or <code>null</code> if the file
     *    was not created
     */
    public IFile createNewPolicy(final InputStream contents,
                                 final IPath newPolicyPath,
                                 final IProgressMonitor progressMonitor,
                                 final String policyName)
            throws PolicyFileAccessException {

        // create the new policy and cache it if successful
        final IFile newPolicyFile = createPolicyHandle(newPolicyPath);

        IWorkspaceRunnable workspaceOp = new IWorkspaceRunnable() {
            public void run(IProgressMonitor monitor) throws CoreException {
                monitor.subTask(policyName);
                ContainerGenerator generator =
                        new ContainerGenerator(newPolicyPath.removeLastSegments(1));
                generator.generateContainer(monitor);
                createPolicy(newPolicyFile, contents, monitor);
            }
        };

        try {
            newPolicyFile.getWorkspace().run(workspaceOp, progressMonitor);
        } catch (CoreException e) {
            // TODO later check explicitly for cancellation
            throw new PolicyFileAccessException(e);
        }

        return newPolicyFile;
    }

    /**
     * Creates a file resource handle for the file with the given workspace path.
     * This method does not create the file resource; this is the responsibility
     * of <code>createFile</code>.
     *
     * @param policyPath the path of the file resource to create a handle for
     * @return the new file resource handle
     * @see #createPolicy
     */
    protected IFile createPolicyHandle(IPath policyPath) {
        return ResourcesPlugin.getWorkspace().getRoot().
                getFile(policyPath);
    }

    /**
     * Creates a file resource given the file handle and contents.
     *
     * @param fileHandle the file handle to create a file resource with
     * @param contents the initial contents of the new file resource, or
     *   <code>null</code> if none (equivalent to an empty stream)
     * @param monitor the progress monitor to show visual progress with
     * @exception org.eclipse.core.runtime.CoreException if the operation fails
     * @exception org.eclipse.core.runtime.OperationCanceledException if the
     * operation is canceled
     */
    protected void createPolicy(IFile fileHandle, InputStream contents,
                                IProgressMonitor monitor)
            throws CoreException {

        if (contents == null) {
            contents = new ByteArrayInputStream(new byte[0]);
        }

        try {
            fileHandle.create(contents, false, monitor);
        } catch (CoreException e) {
            // If the file already existed locally, just refresh to get contents
            if (e.getStatus().getCode() == IResourceStatus.PATH_OCCUPIED) {
                fileHandle.refreshLocal(IResource.DEPTH_ZERO, null);
            } else {
                throw e;
            }
        }

        if (monitor.isCanceled()) {
            throw new OperationCanceledException();
        }
    }
}
