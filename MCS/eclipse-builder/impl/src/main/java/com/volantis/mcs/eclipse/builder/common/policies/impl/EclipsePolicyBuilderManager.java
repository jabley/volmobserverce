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

import com.volantis.mcs.eclipse.builder.common.policies.GenericCreatePolicyConfiguration;
import com.volantis.mcs.eclipse.builder.common.policies.PolicyFileAccessException;
import com.volantis.mcs.eclipse.core.MCSProjectNature;
import com.volantis.mcs.objects.FileExtension;
import com.volantis.mcs.policies.PolicyBuilder;
import com.volantis.mcs.policies.PolicyType;
import com.volantis.mcs.project.BuilderBatchOperation;
import com.volantis.mcs.project.PolicyBuilderManager;
import com.volantis.mcs.project.TransactionLevel;
import com.volantis.mcs.repository.RepositoryException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * A policy manager implementation to allow an Eclipse project to be exposed as
 * a policy repository.
 *
 * <p>Note that no support for batch operations or transaction levels other than
 * {@link com.volantis.mcs.project.TransactionLevel#NONE} is provided.</p>
 *
 * <p>This implementation depends on the {@link StandalonePolicyFileAccessor}
 * functionality
 */
public class EclipsePolicyBuilderManager implements PolicyBuilderManager {
    private IProject project;

    private StandalonePolicyFileAccessor standaloneAccessor =
            new StandalonePolicyFileAccessor();

    public EclipsePolicyBuilderManager(IProject project) {
        this.project = project;
    }

    // Javadoc not required
    protected IProject getProject() {
        return project;
    }

    // Javadoc inherited
    public boolean supportsTransactionLevel(TransactionLevel level) {
        return TransactionLevel.NONE == level;
    }

    // Javadoc inherited
    public void beginBatchOperation(TransactionLevel level) throws RepositoryException {
        if (!supportsTransactionLevel(level)) {
            throw new RepositoryException("Invalid transaction level");
        }
    }

    // Javadoc inherited
    public void endBatchOperation() throws RepositoryException {
    }

    // Javadoc inherited
    public void abortBatchOperation() throws RepositoryException {
    }

    // Javadoc inherited
    public void performBatchOperation(BuilderBatchOperation operation,
            TransactionLevel level) throws RepositoryException {
    }

    // Javadoc inherited
    public Collection getPolicyBuilderNames(PolicyType policyType) throws RepositoryException {
        return (policyType == null) ? getAllPolicyBuilderNames() :
                getTypedPolicyBuilderNames(policyType);
    }

    /**
     * Gets all policy names that match a specified (non-null) policy type.
     *
     * @param policyType The type of policy to enumerate.
     * @return A collection of names for the specified policy type
     * @throws RepositoryException if an error occurs
     */
    private Collection getTypedPolicyBuilderNames(PolicyType policyType) throws RepositoryException {
        Collection policyNames = new ArrayList();
        try {
            FileExtension extension =
                    FileExtension.getFileExtensionForPolicyType(policyType);
            IFolder policySourceFolder = project.getFolder(getPolicySourcePath());
            addPolicyNames(policySourceFolder, extension, policyNames,
                    policySourceFolder.getProjectRelativePath());
        } catch (CoreException ce) {
            throw new RepositoryException(ce);
        }
        return policyNames;
    }

    /**
     * Gets all policy names that match any policy type.
     *
     * @return A collection of names for all policy types
     * @throws RepositoryException if an error occurs
     */
    private Collection getAllPolicyBuilderNames() throws RepositoryException {
        Collection policyNames = new ArrayList();
        Iterator types = PolicyType.getPolicyTypes().iterator();
        while (types.hasNext()) {
            policyNames.addAll(getTypedPolicyBuilderNames((PolicyType) types.next()));
        }
        return policyNames;
    }

    /**
     * Recursively add policies with a specified file extension into a list.
     *
     * @param folder The folder to process recursively
     * @param extension The file extension to match
     * @param policyNames A list of policy names to add matching policies to
     */
    private void addPolicyNames(IFolder folder, FileExtension extension,
                                Collection policyNames, IPath policyRoot)
            throws CoreException {
        IResource[] children = folder.members();
        for (int i = 0; i < children.length; i++) {
            if (children[i] instanceof IFolder) {
                addPolicyNames((IFolder) children[i], extension, policyNames, policyRoot);
            } else if (children[i] instanceof IFile) {
                IFile file = (IFile) children[i];
                String extensionString = file.getFileExtension();
                if (extension.matches(extensionString)) {
                    IPath policyPath = file.getProjectRelativePath();
                    if (policyRoot.isPrefixOf(policyPath)) {
                        IPath relativePolicyPath =
                                policyPath.removeFirstSegments(
                                        policyRoot.segmentCount());
                        String policyName = relativePolicyPath.toString();
                        if (!policyName.startsWith("/")) {
                            policyName = "/" + policyName;
                        }
                        policyNames.add(policyName);
                    } else {
                        throw new IllegalStateException("Unexpected policy location");
                    }
                }
            }
        }
    }

    // Javadoc inherited
    public PolicyBuilder getPolicyBuilder(String name) throws RepositoryException {
        PolicyBuilder policy = null;
        try {
            IFile policyFile = getFileFromPolicyName(name);
            if (policyFile.exists()) {
                policy = standaloneAccessor.loadPolicy(policyFile);
            }
        } catch (CoreException ce) {
            throw new RepositoryException(ce);
        } catch (PolicyFileAccessException pfae) {
            throw new RepositoryException(pfae);
        }

        return policy;
    }

    public void addPolicyBuilder(PolicyBuilder policy) throws RepositoryException {
        try {
            IFile policyFile = getFileFromPolicyName(policy.getName());
            standaloneAccessor.createPolicy(policyFile.getFullPath(), policy,
                new GenericCreatePolicyConfiguration(null));
        } catch (CoreException ce) {
            throw new RepositoryException(ce);
        } catch (PolicyFileAccessException pfae) {
            throw new RepositoryException(pfae);
        }
    }

    private IFile getFileFromPolicyName(String policyName) throws CoreException {
        IPath policyPath = new Path(policyName);
        IPath policySourcePath = getPolicySourcePath();
        IPath projectRelativePolicyPath =
                policySourcePath.append(policyPath.makeRelative());
        IFile policyFile = project.getFile(projectRelativePolicyPath);
        return policyFile;
    }

    public boolean removePolicyBuilder(String name) throws RepositoryException {
        boolean removed = false;
        try {
            IFile policyFile = getFileFromPolicyName(name);
            if (policyFile.exists()) {
                standaloneAccessor.deletePolicy(policyFile);
                removed = true;
            }
        } catch (CoreException ce) {
            throw new RepositoryException(ce);
        } catch (PolicyFileAccessException pfae) {
            throw new RepositoryException(pfae);
        }
        return removed;
    }

    // Javadoc inherited
    public boolean updatePolicyBuilder(PolicyBuilder policy) throws RepositoryException {
        boolean updated = false;
        try {
            IFile policyFile = getFileFromPolicyName(policy.getName());
            standaloneAccessor.savePolicy(policy, policyFile, new NullProgressMonitor());
        } catch (CoreException ce) {
            throw new RepositoryException(ce);
        } catch (PolicyFileAccessException pfae) {
            throw new RepositoryException(pfae);
        }
        return updated;
    }

    private IPath getPolicySourcePath() throws CoreException {
        MCSProjectNature mcsNature = (MCSProjectNature)
                project.getNature(MCSProjectNature.NATURE_ID);
        return mcsNature.getPolicySourcePath();
    }
}
