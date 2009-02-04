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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.runtime.configuration.project;

import org.jibx.runtime.ITrackSource;

/**
 * Holds configuration information which is common for all runtime projects.
 */
public class RuntimeProjectConfiguration {

    /**
     * Policies configuration.
     */
    private AbstractPoliciesConfiguration policies;

    /**
     * Assets configuration.
     */
    private AssetsConfiguration assets;

    /**
     * Generated resources configuration.
     */
    private GeneratedResourcesConfiguration generatedResources;

    /**
     * The default layout as specified by the project
     */
    private DefaultProjectLayoutLocator defaultProjectLayoutLocator;

    /**
     * The themes as specified by the project
     */
    private ProjectThemes projectThemes;

    /**
     * Indicates whether the policy should be preloaded.
     */
    private Boolean preload;
    /**
     * The name of the project.
     */
    private String name;

    /**
     * The name of the fallback project.
     */
    private String fallbackProjectName;

    /**
     * The configuration for the partition used by this project within the
     * policy cache.
     */
    private PolicyCachePartitionConfiguration policyCachePartition;

    /**
     * Return the policies configuration.
     */
    public AbstractPoliciesConfiguration getPolicies() {
        return policies;
    }

    /**
     * Get the location from which the project was loaded.
     *
     * <p>This is only valid for configuration loaded by JIBX.</p>
     *
     * @return The location, or null.
     */
    public String getLocation() {
        if (this instanceof ITrackSource) {
            ITrackSource source = (ITrackSource) this;
            return source.jibx_getDocumentName();
        }
        return null;
    }

    /**
     * Sets the policies configuration.
     */
    public void setPolicies(AbstractPoliciesConfiguration policies) {
        this.policies = policies;
    }

    /**
     * Return the assets configuration.
     */
    public AssetsConfiguration getAssets() {
        return assets;
    }

    /**
     * Sets the assets configuration.
     */
    public void setAssets(AssetsConfiguration assets) {
        this.assets = assets;
    }

    /**
     * @see #generatedResources
     */
    public GeneratedResourcesConfiguration getGeneratedResources() {
        return generatedResources;
    }

    /**
     * @see #generatedResources
     */
    public void setGeneratedResources(
            GeneratedResourcesConfiguration generatedResources) {
        this.generatedResources = generatedResources;
    }

    //javadoc unnecessary
    public DefaultProjectLayoutLocator getDefaultProjectLayoutLocator() {
        return defaultProjectLayoutLocator;
    }

    //javadoc unnecessary
    public void setDefaultProjectLayoutLocator (
            DefaultProjectLayoutLocator defaultProjectLayoutLocator) {
        this.defaultProjectLayoutLocator = defaultProjectLayoutLocator;
    }

    //javadoc unnecessary
    public void setProjectThemes (
            ProjectThemes projectThemes) {
        this.projectThemes = projectThemes;
    }

    //javadoc unnecessary
    public ProjectThemes getProjectThemes() {
        return projectThemes;
    }

    public Boolean getPreload() {
        return preload;
    }

    public void setPreload(Boolean preload) {
        this.preload = preload;
    }

    /**
     * Returns the name of the project.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the project.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the name of the fallback project.
     */
    public String getFallbackProjectName() {
        return fallbackProjectName;
    }

    /**
     * Returns the name of the fallback project.
     */
    public void setFallbackProjectName(String fallbackProjectName) {
        this.fallbackProjectName = fallbackProjectName;
    }

    /**
     * Get the configuration for the policy cache partition.
     *
     * @return The configuration for the policy cache partition.
     */
    public PolicyCachePartitionConfiguration getPolicyCachePartition() {
        return policyCachePartition;
    }

    /**
     * Set the configuration for the policy cache partition.
     *
     * @param policyCachePartition The configuration for the policy cache
     *                             partition.
     */
    public void setPolicyCachePartition(
            PolicyCachePartitionConfiguration policyCachePartition) {
        this.policyCachePartition = policyCachePartition;
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

 28-Oct-04	5897/1	geoff	VBM:2004102104 chartimages generated within portlets need some form of unique identifier.

 26-Jan-04	2724/3	geoff	VBM:2004011911 Add projects to config (whoops - add javadoc)

 26-Jan-04	2724/1	geoff	VBM:2004011911 Add projects to config

 ===========================================================================
*/
