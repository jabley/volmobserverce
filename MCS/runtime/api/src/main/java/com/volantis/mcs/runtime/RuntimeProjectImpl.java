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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.runtime;

import com.volantis.cache.group.Group;
import com.volantis.mcs.policies.CacheControl;
import com.volantis.mcs.policies.PolicyBuilderReader;
import com.volantis.mcs.policies.variants.VariantType;
import com.volantis.mcs.project.InternalProject;
import com.volantis.mcs.project.InternalProjectImpl;
import com.volantis.mcs.project.remote.RemotePolicySource;
import com.volantis.mcs.runtime.configuration.project.DefaultProjectLayoutLocator;
import com.volantis.mcs.runtime.configuration.project.ProjectThemes;
import com.volantis.mcs.runtime.policies.cache.CacheControlConstraintsMap;
import com.volantis.mcs.utilities.MarinerURL;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This class represents a project at runtime, and extends the internal
 * representation of a project.
 */
public class RuntimeProjectImpl
        extends InternalProjectImpl
        implements RuntimeProject {

    /**
     * Holds the base URL for the assets for this project.
     */
    private final MarinerURL assetsBaseURL;

    /**
     * Map from {@link VariantType} to MarinerURL.
     */
    private final Map variantType2PrefixURL;

    /**
     * Base directory for generated resources.
     * <p>
     * This may be null for backwards compatibility. In future this should
     * not be allowed to be null.
     */
    private final String generatedResourcesBaseDir;

    /**
     * the location of the default layout for the project
     */
    private final DefaultProjectLayoutLocator defaultProjectLayoutLocator;

    /**
     * additional themes available to this project
     */
    private final ProjectThemes projectThemes;
    /**
     * The name of the project - used to identify it uniquely in a collection
     * of all projects.
     */
    private final String name;

    /**
     * True if the project contains policies that are orphaned, false otherwise.
     *
     * <p>There are two project's that fulfull this requirement, the local
     * default project, and the global remote one.</p>
     */
    private final boolean containsOrphans;

    /**
     * The set of policy root aliases or null, if the project may contain
     * orphans.
     */
    private final Set policyRootAliases;

    /**
     * True if the project is remote, false if it is local.
     */
    private final boolean remote;

    /**
     * True if the project is defined in an mcs-project.xml, false otherwise.
     */
    private final boolean portable;

    private final RuntimeProject baseProject;

    /**
     * The list of base projects, the first is the project upon which this
     * directly depends, the last is the root base project.
     */
    private final List baseProjects;

    /**
     * The cache control constraints map.
     */
    private final CacheControlConstraintsMap cacheControlConstraintsMap;

    /**
     * The defaults to use for policies that were searched for in this project
     * but could not be found.
     */
    private final CacheControl cacheControlDefaults;

    /**
     * The cache group to which policies in this project will be added.
     */
    private final Group cacheGroup;

    public RuntimeProjectImpl(RuntimeProjectBuilder builder) {
        super(builder.getPolicySource());

        name = builder.getName();
        assetsBaseURL = builder.getAssetsBaseURL();
        variantType2PrefixURL = builder.getVariantPrefixURLMap();
        defaultProjectLayoutLocator = builder.getDefaultProjectLayoutLocator();
        projectThemes = builder.getProjectThemes();
        generatedResourcesBaseDir = builder.getGeneratedResourceBaseDir();
        containsOrphans = builder.getContainsOrphans();
        if (!containsOrphans) {
            policyRootAliases = new HashSet();
            final String policyRootAsString = builder.getPolicyRootAsString();
            if (policyRootAsString != null) {
                addPolicyRootAlias(policyRootAsString);
            }
        } else {
            policyRootAliases = null;
        }
        remote = builder.isRemote();
        portable = builder.isPortable();
        baseProject = builder.getBaseProject();

        // Build up a collection of base projects.
        if (baseProject == null) {
            baseProjects = Collections.EMPTY_LIST;
        } else {
            baseProjects = new ArrayList();
            RuntimeProject project = baseProject;
            while (project != null) {
                baseProjects.add(project);
                project = project.getBaseProject();
            }
        }

        cacheControlConstraintsMap = builder.getCacheControlConstraintsMap();
        cacheGroup = builder.getCacheGroup();
        cacheControlDefaults = builder.getCacheControlDefaults();
    }

    public boolean extendsProject(RuntimeProject other) {
        return baseProjects.contains(other);
    }

    public CacheControl getCacheControlDefaults() {
        return cacheControlDefaults;
    }

    // Javadoc inherited.
    public String makeAbsolutePolicyURL(String projectRelative) {

        if (!isRemote()) {
            throw new IllegalStateException("Cannot make path absolute as " +
                    "project is not remote");
        } else if (getContainsOrphans()) {
            throw new IllegalStateException("Cannot make path absolute as " +
                    "project is global");
        }

        // Convert it into an absolute path by prefixing with the policy
        // source's base URL.
        RemotePolicySource policySource = (RemotePolicySource)
                getPolicySource();
        String baseURL = policySource.getBaseURLWithoutTrailingSlash();
        return baseURL + projectRelative;
    }

    public Group getCacheGroup() {
        return cacheGroup;
    }

    // Javadoc inherited.
    public PolicyBuilderReader getPolicyBuilderReader() {
        return getPolicySource().getPolicyBuilderReader();
    }

    public CacheControlConstraintsMap getCacheControlConstraintsMap() {
        return cacheControlConstraintsMap;
    }

    public RuntimeProject getBaseProject() {
        return baseProject;
    }

    // Javadoc inherited.
    public InternalProject getInternalBaseProject() {
        return getBaseProject();
    }

    public MarinerURL getAssetsBaseURL() {
        return assetsBaseURL;
    }

    public MarinerURL getPrefixURL(VariantType variantType) {
        return (MarinerURL) variantType2PrefixURL.get(variantType);
    }

    public String getGeneratedResourcesBaseDir() {
        return generatedResourcesBaseDir;
    }

    public String getDefaultProjectLayoutLocation() {
        String defaultProjectLocation = null;
        if (defaultProjectLayoutLocator != null) {
            defaultProjectLocation =
                    defaultProjectLayoutLocator.getDefaultProjectLocation();
        }
        return defaultProjectLocation;
    }

    public List getProjectThemesLocations() {
        List themes = null;
        if (projectThemes != null) {
            themes = projectThemes.getThemes();
        }
        return themes;
    }

    // javadoc inherited
    public Set getPolicyRootAliases() {
        return policyRootAliases == null? null:
            Collections.unmodifiableSet(policyRootAliases);
    }

    // javadoc inherited
    public void addPolicyRootAlias(String alias) {
        if (containsOrphans) {
            throw new UnsupportedOperationException("Internal Error: " +
                    "Operation not supported on projects that contain orphans");
        }
        if (!alias.endsWith("/")) {
            alias += "/";
        }
        policyRootAliases.add(alias);
    }

    public boolean getContainsOrphans() {
        return containsOrphans;
    }

    public String getName() {
        return name;
    }

    // Javadoc inherited.
    public boolean containsPolicy(String urlAsString) {
        return getContainerPolicyRootAlias(urlAsString) != null;
    }

    /**
     * Returns the alias that is prefix of the specified URL.
     *
     * <p>Returns null if no such alias is found.</p>
     *
     * <p>Cannot be used if the project may contain orphan resources.</p>
     *
     * @param urlAsString the URL to check
     * @return the alias or null
     */
    private String getContainerPolicyRootAlias(final String urlAsString) {
        if (containsOrphans) {
            throw new UnsupportedOperationException("Internal Error: " +
                    "Operation not supported on projects that contain orphans");
        }

        String foundAlias = null;
        for (Iterator iter = policyRootAliases.iterator();
             iter.hasNext() && foundAlias == null;) {
            final String alias = (String) iter.next();
            if (urlAsString.startsWith(alias)) {
                foundAlias = alias;
            }
        }
        return foundAlias;
    }

    public String makeProjectRelativePath(MarinerURL url, boolean abort) {

        String urlAsString = url.getExternalForm();

        return makeProjectRelativePath(urlAsString, abort);
    }

    public String makeProjectRelativePath(String urlAsString, boolean abort) {

        final String alias = getContainerPolicyRootAlias(urlAsString);
        if (alias == null) {
            if (abort) {
                final String aliases = getPolicyRootAliasesAsString();
                throw new IllegalArgumentException("Cannot create project " +
                    "relative path from " + aliases + " to '" +
                    urlAsString + "'");
            } else {
                return null;
            }
        }

        // Calculate the project relative path by removing the policy
        // root from the start of the absolute URL but leaving the leading /.
        return urlAsString.substring(alias.length() - 1);
    }

    /**
     * Returns the string representation of the policy root aliases. If the
     * project may contain orphans, returns <code>"&lt;null&gt;"</code>
     *
     * <p>Mainly for debugging purposes.</p>
     *
     * @return the string representation
     */
    private String getPolicyRootAliasesAsString() {
        final String result;
        if (policyRootAliases != null) {
            final StringBuffer buffer = new StringBuffer();
            buffer.append("[");
            for (Iterator iter = policyRootAliases.iterator();
                 iter.hasNext(); ) {
                buffer.append(iter.next());
                if (iter.hasNext()) {
                    buffer.append(", ");
                }
            }
            buffer.append("]");
            result = buffer.toString();
        } else {
            result = "<null>";
        }
        return result;
    }

    public boolean isRemote() {
        return remote;
    }

    public boolean isPortable() {
        return portable;
    }

    // JavaDoc inherited
    public String toString() {

        if (getContainsOrphans()) {
            if (isRemote()) {
                return "GLOBAL REMOTE PROJECT";
            } else {
                return "DEFAULT LOCAL PROJECT";
            }
        } else if (name != null) {
            return "PREDEFINED PROJECT (" + name + ")";
        } else {
            return "(" + getPolicyRootAliasesAsString() + ")";
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Oct-04	6027/1	geoff	VBM:2004102104 chartimages generated within portlets need some form of unique identifier.

 28-Oct-04	5897/2	geoff	VBM:2004102104 chartimages generated within portlets need some form of unique identifier.

 27-Feb-04	3215/1	steve	VBM:2004021911 Patch from Proteus2 and fixes for RemoteProject

 19-Feb-04	3041/4	claire	VBM:2004021208 Removed unused setURL parameter

 17-Feb-04	3041/1	claire	VBM:2004021208 Refactored RuntimeProject and added a unit test

 10-Feb-04	2935/2	mat	VBM:2004011922 Merge changes

 04-Feb-04	2828/2	ianw	VBM:2004011922 Added MCSI content handler

 05-Feb-04	2846/1	claire	VBM:2004011915 Asset URL computation based on base and prefix

 03-Feb-04	2767/2	claire	VBM:2004012701 Adding project handling code

 01-Feb-04	2821/1	mat	VBM:2004012701 Change tests and generate scripts for Projects

 ===========================================================================
*/
