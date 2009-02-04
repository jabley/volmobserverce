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
import com.volantis.mcs.eclipse.builder.common.policies.PolicyFileAccessException;
import com.volantis.mcs.eclipse.builder.common.policies.PolicyFileAccessor;
import com.volantis.mcs.eclipse.core.MCSProjectNature;
import com.volantis.mcs.policies.InternalPolicyFactory;
import com.volantis.mcs.policies.PolicyBuilder;
import com.volantis.shared.content.BinaryContentInput;
import com.volantis.shared.content.ContentInput;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

import java.io.IOException;
import java.io.InputStream;

/**
 * Common superclass for policy file accessors providing useful helper methods.
 */
public abstract class AbstractPolicyFileAccessor implements PolicyFileAccessor {
    /**
     * Calculate the name of a policy from the IResource associated with it.
     *
     * @param resource The {@link IResource} representing the policy file.
     * @return The name of the policy, or null if it can not be calculated
     */
    protected String getPolicyName(IResource resource) {
        return getPolicyName(resource.getFullPath(), resource.getProject());
    }

    /**
     * Calculate the name of a policy from its full path (including project
     * name) and project.
     *
     * @param fullPath The full path for the policy
     * @param project The project for the policy
     * @return The name of the policy, or null if it can not be calculated
     */
    protected String getPolicyName(IPath fullPath, IProject project) {
        String policySource = MCSProjectNature.getPolicySourcePath(project);
        IPath policySourcePath = new Path(policySource).makeAbsolute();

        IPath projectRelativePath = fullPath.removeFirstSegments(1).makeAbsolute();

        String name = null;
        if (projectRelativePath.matchingFirstSegments(policySourcePath) ==
                policySourcePath.segmentCount()) {
            IPath relativePath = projectRelativePath.removeFirstSegments(
                    policySourcePath.segmentCount());
            name = makeAbsolute(relativePath.toString());
        }

        return name;
    }

    /**
     * Make a string representing a path absolute by adding a preceding / if
     * necessary.
     *
     * @param path The path to be made absolute
     * @return The absolute form of the path
     */
    private String makeAbsolute(String path) {
        if (!path.startsWith("/")) {
            path = "/" + path;
        }
        return path;
    }

    public PolicyBuilder readPolicyResource(final IResource policyResource)
            throws PolicyFileAccessException {
        if (!(policyResource instanceof IFile)) {
            throw new IllegalArgumentException(
                "Can only read policies from files");
        }
        InputStream is = null;
        try {
            final IFile file = (IFile) policyResource;

            // Read the model using JiBX
            is = file.getContents();
            final ContentInput content =
                new BinaryContentInput(is);
            final InternalPolicyFactory policyFactory =
                    InternalPolicyFactory.getInternalInstance();

            // Don't use schema validation, as we may have a partial layout,
            // with missing attributes and empty formats.
            // todo: re-enable validation by avoiding this overload once we have
            // sorted out validation.
            final JiBXReader reader =
                policyFactory.createDangerousNonValidatingPolicyReader();

            return (PolicyBuilder) reader.read(content, file.getName());
        } catch (CoreException e) {
            throw new PolicyFileAccessException(e);
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
}
