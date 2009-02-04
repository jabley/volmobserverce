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
import com.volantis.mcs.policies.variants.VariantType;
import com.volantis.mcs.policies.CacheControl;
import com.volantis.mcs.project.PolicySource;
import com.volantis.mcs.runtime.configuration.project.AssetConfiguration;
import com.volantis.mcs.runtime.configuration.project.AssetsConfiguration;
import com.volantis.mcs.runtime.configuration.project.DefaultProjectLayoutLocator;
import com.volantis.mcs.runtime.configuration.project.ProjectThemes;
import com.volantis.mcs.runtime.policies.cache.CacheControlConstraintsMap;
import com.volantis.mcs.utilities.MarinerURL;

import java.util.HashMap;
import java.util.Map;

public class RuntimeProjectBuilder {

    private String name;

    private MarinerURL assetsBaseURL;

    private PolicySource policySource;

    /**
     * Map from {@link VariantType} to MarinerURL.
     */
    private Map variantType2PrefixURL;

    private String generatedResourceBaseDir;
    private boolean preload;

    private DefaultProjectLayoutLocator defaultProjectLayoutLocator;

    private ProjectThemes projectThemes;

    private boolean containsOrphans;

    private String policyRootAsString;

    private boolean remote;

    private boolean portable;

    private RuntimeProject baseProject;
    private CacheControlConstraintsMap cacheControlConstraintsMap;
    private Group cacheGroup;
    private CacheControl cacheControlDefaults;

    public RuntimeProjectBuilder() {
        variantType2PrefixURL = new HashMap();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PolicySource getPolicySource() {
        return policySource;
    }

    public void setPolicySource(PolicySource policySource) {
        this.policySource = policySource;
    }

    public void setAssetsConfiguration(AssetsConfiguration configuration) {
        assetsBaseURL = new MarinerURL(configuration.getBaseUrl());
        assetsBaseURL.makeReadOnly();

        addVariantPrefixURL(VariantType.AUDIO, configuration.getAudioAssets());
        addVariantPrefixURL(VariantType.VIDEO,
                configuration.getDynamicVisualAssets());
        addVariantPrefixURL(VariantType.IMAGE, configuration.getImageAssets());
        addVariantPrefixURL(VariantType.SCRIPT,
                configuration.getScriptAssets());
        addVariantPrefixURL(VariantType.TEXT, configuration.getTextAssets());
    }

    public MarinerURL getVariantPrefixURL(VariantType variantType) {
        return (MarinerURL) variantType2PrefixURL.get(variantType);
    }

    public void addVariantPrefixURL(
            VariantType variantType,
            AssetConfiguration configuration) {
        MarinerURL prefixURL;
        if (configuration == null) {
            prefixURL = null;
        } else {
            prefixURL = new MarinerURL(configuration.getPrefixUrl());
            prefixURL.makeReadOnly();
        }

        variantType2PrefixURL.put(variantType, prefixURL);
    }

    public void addVariantPrefixURL(
            VariantType variantType,
            MarinerURL prefixURL) {

        if (prefixURL != null) {
            prefixURL.makeReadOnly();
        }
        variantType2PrefixURL.put(variantType, prefixURL);
    }

    public Map getVariantPrefixURLMap() {
        return variantType2PrefixURL;
    }

    public MarinerURL getAssetsBaseURL() {
        return assetsBaseURL;
    }

    public void setAssetsBaseURL(MarinerURL assetsBaseURL) {
        this.assetsBaseURL = assetsBaseURL;
    }

    public void setGeneratedResourceBaseDir(String generatedResourceBaseDir) {
        this.generatedResourceBaseDir = generatedResourceBaseDir;
    }

    public String getGeneratedResourceBaseDir() {
        return generatedResourceBaseDir;
    }

    public RuntimeProject getProject() {
        return new RuntimeProjectImpl(this);
    }

    public DefaultProjectLayoutLocator getDefaultProjectLayoutLocator() {
        return defaultProjectLayoutLocator;
    }

    public void setDefaultProjectLayoutLocator(
            DefaultProjectLayoutLocator defaultProjectLayoutLocator) {
        this.defaultProjectLayoutLocator = defaultProjectLayoutLocator;
    }

    public ProjectThemes getProjectThemes() {
        return projectThemes;
    }

    public void setProjectThemes(ProjectThemes projectThemes) {
        this.projectThemes = projectThemes;
    }

    public boolean isPreload() {
        return preload;
    }

    public void setPreload(boolean preload) {
        this.preload = preload;
    }

    /**
     * There are two projects, a local one and a remote one that contain
     * orphan policies and pages.
     *
     * @return
     */
    public boolean getContainsOrphans() {
        return containsOrphans;
    }

    public void setContainsOrphans(boolean containsOrphans) {
        this.containsOrphans = containsOrphans;
    }

    public String getPolicyRootAsString() {
        return policyRootAsString;
    }

    public void setPolicyRootAsString(String policyRootAsString) {
        this.policyRootAsString = policyRootAsString;
    }

    public void setRemote(boolean remote) {
        this.remote = remote;
    }

    public boolean isRemote() {
        return remote;
    }

    public boolean isPortable() {
        return portable;
    }

    public void setPortable(boolean portable) {
        this.portable = portable;
    }

    public RuntimeProject getBaseProject() {
        return baseProject;
    }

    public void setBaseProject(RuntimeProject baseProject) {
        this.baseProject = baseProject;
    }

    public CacheControlConstraintsMap getCacheControlConstraintsMap() {
        return cacheControlConstraintsMap;
    }

    public void setCacheControlDefaultsMap(
            CacheControlConstraintsMap cacheControlConstraintsMap) {
        this.cacheControlConstraintsMap = cacheControlConstraintsMap;
    }

    public void setCacheGroup(Group cacheGroup) {
        this.cacheGroup = cacheGroup;
    }

    public Group getCacheGroup() {
        return cacheGroup;
    }

    public void setCacheControlDefaults(CacheControl cacheControlDefaults) {
        this.cacheControlDefaults = cacheControlDefaults;
    }

    public CacheControl getCacheControlDefaults() {
        return cacheControlDefaults;
    }
}
