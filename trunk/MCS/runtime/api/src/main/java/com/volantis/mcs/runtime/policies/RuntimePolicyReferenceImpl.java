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

import com.volantis.mcs.policies.PolicyType;
import com.volantis.mcs.project.Project;
import com.volantis.mcs.runtime.RuntimeProject;
import com.volantis.mcs.runtime.project.ProjectManager;
import com.volantis.mcs.utilities.MarinerURL;

/**
 * A lazily normalized policy reference.
 */
public class RuntimePolicyReferenceImpl
        implements RuntimePolicyReference {

    private final RuntimeProject containingProject;
    private final MarinerURL baseURL;
    private final String name;
    private final PolicyType expectedPolicyType;

    private final ProjectManager projectManager;

    private Project normalizedProject;
    private String normalizedName;
    private boolean brandable;

    public RuntimePolicyReferenceImpl(
            Project containingProject, MarinerURL baseURL,
            String name,
            PolicyType expectedPolicyType,
            ProjectManager projectManager) {

        if (containingProject == null) {
            throw new IllegalArgumentException(
                    "containingProject cannot be null");
        }
        if (baseURL == null) {
            throw new IllegalArgumentException("baseURL cannot be null");
        }
        if (name == null) {
            throw new IllegalArgumentException("name cannot be null");
        }
        this.containingProject = (RuntimeProject) containingProject;
        this.baseURL = baseURL;
        this.name = name;
        this.expectedPolicyType = expectedPolicyType;
        this.projectManager = projectManager;
    }

    public PolicyType getExpectedPolicyType() {
        return expectedPolicyType;
    }

    /**
     * Ensure that this reference has been normalized.
     */
    private void ensureNormalized() {

        // Check to see whether it has been normalized, if it has then return
        // immediately.
        if (this.normalizedProject != null) {
            return;
        }

        String unprefixedName;
        // Assume that the reference is brandable.
        boolean brandable = true;
        if (name.startsWith("^")) {
            unprefixedName = name.substring(1);
            // A name that starts with a ^ is unbrandable.
            brandable = false;
        } else {
            unprefixedName = name;
        }

        MarinerURL url = new MarinerURL(unprefixedName);

        RuntimeProject normalizedProject;
        String normalizedName = null;
        if (url.isAbsolute()) {
            // An absolute URL so try and find the project, look in the
            // containing project first and then in the others. If the project
            // was not already known then defer loading it until the policy
            // has been retrieved as that will reduce the number of times that
            // the remote server gets hit.
            RuntimeProject project = projectManager.queryProject(unprefixedName,
                    containingProject);

            if (project == null) {
                // Assume that it is in the global project for now.
                project = projectManager.getGlobalProject();
            }

            normalizedProject = project;

            normalizedName = normalizeURLToName(normalizedProject, url);

            // Specifying an absolute URL in the input means that it is not
            // brandable.
            brandable = false;
        } else {
            if (url.getPathType() == MarinerURL.HOST_RELATIVE_PATH) {

                // A host relative path, i.e. a project relative reference.
                normalizedProject = containingProject;
                normalizedName = unprefixedName;

            } else {
                // Resolve against the base URL, the result must be either
                // absolute, or host relative.
                url = new MarinerURL(baseURL, url);
                if (url.isAbsolute()) {

                    // Get the project for the url.
                    String urlAsString = url.getExternalForm();
                    RuntimeProject project =
                            projectManager.getProject(urlAsString, null);

                    // Now check to make sure that the resulting URL is still
                    // within the same project as it started from as document
                    // relative paths cannot move outside the project.
                    if (containingProject.getContainsOrphans()) {
                        // The project is either local default or global
                        // project so the policies they contain are defined by
                        // exclusion so in order to determine whether a policy
                        // is in one of these it is necessary to check to see
                        // if it is in another project.
                        if (project == null) {
                            // The referenced policy is still in the global
                            // project so use the abolute URL as the normalized
                            // name.
                            normalizedName = urlAsString;
                        } else {
                            // The referenced policy is in another project which
                            // is invalid, so set the name to null so an
                            // appropriate exception will be thrown below.
                            normalizedName = null;
                        }
                    } else {

                        if (project == containingProject ||
                                containingProject.extendsProject(project)) {

                            // Ask the project that contains it to make the
                            // URL project relative.
                            normalizedName = project.makeProjectRelativePath(
                                    url, true);

                        } else {
                            // The project is not the same as the containing
                            // one and does is not extended by the containing
                            // one so set the name to null so that it fails
                            // further on.
                            normalizedName = null;
                        }
                    }
                } else if (url.containsHostRelativePath()) {
                    // The base URL must have been host relative so the
                    // resolved URL is also host relative and so it must
                    // belong in the current project.
                    //
                    // Note: This relies on the MarinerURL detecting when a
                    // relative path is invalid, i.e. contains .. that attempt
                    // to take it above the root URL. Apparently there is a
                    // problem with that.
                    normalizedName = url.getExternalForm();

                } else {
                    throw new IllegalStateException("Resolving " +
                            unprefixedName +
                            " against " + baseURL + " resulted in " +
                            url.getExternalForm() + " which is not absolute");
                }

                // If the resulting URL could not be mapped to a project
                // relative URL, because it moved to another project, then
                // fail.
                if (normalizedName == null) {
                    throw new IllegalStateException(
                            "Document relative path " +
                            "cannot move outside project, when '" +
                            unprefixedName + "' is resolved relative to '" +
                            baseURL.getExternalForm() + "' the result '" +
                            url.getExternalForm() +
                            "' is not within project '" + containingProject +
                            "'");
                }

                // The normalized project is the same as the containing
                // project.
                normalizedProject = containingProject;
            }
        }

        this.normalizedProject = normalizedProject;
        this.normalizedName = normalizedName;
        this.brandable = brandable;
    }

    /**
     * Normalize the URL to a string name.
     *
     * <p>If the project is the global one than an absolute URL is returned,
     * otherwise the URL is made project relative.</p>
     *
     * @param project The project that contains the URL.
     * @param url     The URL to normalize.
     * @return The normalized URL as a string.
     */
    private String normalizeURLToName(RuntimeProject project, MarinerURL url) {

        if (project.isRemote()) {
            // References to policies in remote projects must be absolute.
            if (!url.isAbsolute()) {
                throw new IllegalStateException("Reference to policy in global project must be " +
                        "absolute but is '" + url.getExternalForm() + "'");
            }

            return url.getExternalForm();
        } else if (url.isAbsolute()) {
            return project.makeProjectRelativePath(url, true);
        } else if (url.getPathType() == MarinerURL.HOST_RELATIVE_PATH) {
            return url.getExternalForm();
        } else {
            throw new IllegalStateException("Expected absolute, or project relative reference " +
                    "but was '" + url.getExternalForm() + "'");
        }
    }

    public Project getProject() {
        ensureNormalized();

        return normalizedProject;
    }

    public String getName() {
        ensureNormalized();

        return normalizedName;
    }

    public boolean isBrandable() {
        ensureNormalized();

        return brandable;
    }

    public RuntimePolicyReference getNormalizedReference() {
        ensureNormalized();

        return new NormalizedPolicyReferenceImpl(normalizedProject, normalizedName,
                expectedPolicyType, brandable);
    }
}
