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

package com.volantis.mcs.runtime;

import com.volantis.cache.group.Group;
import com.volantis.mcs.policies.CacheControl;
import com.volantis.mcs.policies.PolicyBuilderReader;
import com.volantis.mcs.policies.variants.VariantType;
import com.volantis.mcs.project.InternalProject;
import com.volantis.mcs.runtime.policies.cache.CacheControlConstraintsMap;
import com.volantis.mcs.utilities.MarinerURL;

import java.util.List;
import java.util.Set;

/**
 * @mock.generate base="InternalProject"
 */
public interface RuntimeProject
        extends InternalProject {

    /**
     * Get the base project, to which this defers if a policy cannot be found.
     *
     * @return The base project, null if there is no base project.
     */
    RuntimeProject getBaseProject();

    /**
     * Retrieves the base URL for assets belonging to the enclosing project.
     *
     * @return The base URL
     */
    MarinerURL getAssetsBaseURL();

    /**
     * Retrieves the prefix URL for the given variant type.
     *
     * @param variantType The type of variant the URL is required for.
     * @return A MarinerURL representation of the prefix URL.
     */
    MarinerURL getPrefixURL(VariantType variantType);

    /**
     */
    String getGeneratedResourcesBaseDir();

    /**
     * get the default project layout location
     *
     * @return
     */
    String getDefaultProjectLayoutLocation();

    /**
     * get the list of locations for the themes made available for this project
     *
     * @return
     */
    List getProjectThemesLocations();

    /**
     * Check whether the project is the global one.
     *
     * @return True if the project is the global project, false otherwise.
     */
    boolean getContainsOrphans();

    /**
     * Retrieves the name of this project
     *
     * @return The project name, or null if it does not have one.
     */
    String getName();

    /**
     * Check to see whether this project contains the policy referenced by the
     * URL.
     *
     * <p>This method is not supported on the global project.</p>
     *
     * @param urlAsString The URL to the policy as a string.
     * @return True if the project does contain the policy referenced by the
     *         URL and false if it does not.
     */
    boolean containsPolicy(String urlAsString);

    /**
     * Make a project relative path to the policy referenced by the URL.
     *
     * <p>This method is not supported on the global project.</p>
     *
     * @param url   The URL to the policy.
     * @param abort Indicates whether this method should abort, i.e throw an
     *              exception if it cannot create a project relative path.
     * @return The string representation of the project relative path, or null
     *         if it could not and the abort flag is set to false.
     */
    String makeProjectRelativePath(MarinerURL url, boolean abort);

    /**
     * Make a project relative path to the policy referenced by the URL.
     *
     * <p>This method is not supported on the global project.</p>
     *
     * @param urlAsString The URL to the policy as a string.
     * @param abort       Indicates whether this method should abort, i.e throw
     *                    an exception if it cannot create a project relative
     *                    path.
     * @return The string representation of the project relative path, or null
     *         if it could not and the abort flag is set to false.
     */
    String makeProjectRelativePath(String urlAsString, boolean abort);

    /**
     * True if the project is remote, false if it is not.
     *
     * @return True if the project is remote.
     */
    boolean isRemote();

    /**
     * True only for those projects that have been defined using an
     * mcs-project.xml.
     *
     * @return True if the project was defined using an mcs-project.xml and
     *         false otherwise.
     */
    boolean isPortable();

    /**
     * Returns the set of aliases of the policy roots as strings.
     *
     * <p>Returns null, if the project can contain orphans.</p>
     *
     * @return The aliases of the root of the policies or null.
     */
    Set getPolicyRootAliases();

    /**
     * Adds a new policy root alias.
     *
     * <p>This method can only be used, if the project doesn't contain orphans.
     * </p>
     *
     * @param alias the new alias
     */
    void addPolicyRootAlias(final String alias);

    /**
     * Checks to see whether this project extends the other project.
     *
     * <p>This returns true if the project extends the other project directly,
     * or indirectly.</p>
     *
     * @param other The other project.
     * @return True if it extends the other project, false if it does not.
     */
    boolean extendsProject(RuntimeProject other);

    /**
     * Get the cache group for the project.
     *
     * <p>Each project may have its own group within the policy cache.</p>
     *
     * @return The cache group, may be null.
     */
    Group getCacheGroup();

    /**
     * Get the {@link PolicyBuilderReader} to use for accessing policies from
     * this source.
     *
     * <p>The returned instance is thread safe.</p>
     *
     * @return The {@link PolicyBuilderReader}.
     */
    PolicyBuilderReader getPolicyBuilderReader();

    /**
     * Get the cache control constraints map for policies within this project.
     *
     * @return The cache control constraints map.
     */
    CacheControlConstraintsMap getCacheControlConstraintsMap();

    /**
     * Get the default cache control used when could not retrieve a policy.
     *
     * @return The default cache control properties.
     */
    CacheControl getCacheControlDefaults();

    /**
     * Make an absolute URL from the project relative policy path.
     *
     * @param projectRelative The project relative path.
     * @return The absolute URL.
     */
    String makeAbsolutePolicyURL(String projectRelative);
}
