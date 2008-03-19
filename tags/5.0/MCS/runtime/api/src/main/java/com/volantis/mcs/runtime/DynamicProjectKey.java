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

import com.volantis.mcs.policies.variants.VariantType;
import com.volantis.mcs.project.PolicySource;
import com.volantis.mcs.runtime.configuration.project.AssetConfiguration;
import com.volantis.mcs.runtime.configuration.project.AssetsConfiguration;
import com.volantis.mcs.utilities.MarinerURL;
import com.volantis.synergetics.ObjectHelper;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *
 */
public class DynamicProjectKey {

    private final PolicySource policySource;
    private final String generatedResourceBaseDir;
    private final MarinerURL baseURL;
    private final Map variantType2PrefixURL;

    public DynamicProjectKey(
            PolicySource policySource, AssetsConfiguration config,
            String generatedResourceBaseDir) {

        this.policySource = policySource;
        String baseURLString = config.getBaseUrl();
        baseURL = (baseURLString != null ?
                new MarinerURL(baseURLString) : null);
        variantType2PrefixURL = new HashMap();
        addVariantPrefixURL(VariantType.AUDIO, config.getAudioAssets());
        addVariantPrefixURL(VariantType.VIDEO, config.getDynamicVisualAssets());
        addVariantPrefixURL(VariantType.IMAGE, config.getImageAssets());
        addVariantPrefixURL(VariantType.SCRIPT, config.getScriptAssets());
        addVariantPrefixURL(VariantType.TEXT, config.getTextAssets());
        this.generatedResourceBaseDir = generatedResourceBaseDir;
    }

    public PolicySource getPolicySource() {
        return policySource;
    }

    public String getGeneratedResourceBaseDir() {
        return generatedResourceBaseDir;
    }

    public MarinerURL getBaseURL() {
        return baseURL;
    }

    /**
     * A utility method for extracting prefix URL strings from asset
     * configuration information, creating new MarinerURL objects
     * based on the extracted prefix and storing it in the map.
     *
     * @param configuration The configuration from which the prefixURL should
     *                    be extracted.
     */
    private void addVariantPrefixURL(VariantType variantType,
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

    public RuntimeProject getProject() {
        RuntimeProjectBuilder builder = new RuntimeProjectBuilder();
        builder.setPolicySource(policySource);
        builder.setGeneratedResourceBaseDir(generatedResourceBaseDir);
        builder.setAssetsBaseURL(baseURL);
        for (Iterator i = variantType2PrefixURL.entrySet().iterator(); i.hasNext();) {
            Map.Entry entry = (Map.Entry) i.next();
            VariantType variantType = (VariantType) entry.getKey();
            MarinerURL prefixURL = (MarinerURL) entry.getValue();
            builder.addVariantPrefixURL(variantType, prefixURL);
        }
        return builder.getProject();
    }

    public boolean equals(Object object) {
        if (object == null ||
                !(object instanceof DynamicProjectKey)) {
            return false;
        }
        DynamicProjectKey key = (DynamicProjectKey) object;

        return ObjectHelper.equals(policySource, key.policySource) &&
                ObjectHelper.equals(generatedResourceBaseDir,
                        key.generatedResourceBaseDir) &&
                ObjectHelper.equals(baseURL, key.baseURL) &&
                ObjectHelper.equals(variantType2PrefixURL,
                        key.variantType2PrefixURL);
    }

    public int hashCode() {
        int result = 0;
        result = 37 * result + ObjectHelper.hashCode(policySource);
        result = 37 * result + ObjectHelper.hashCode(generatedResourceBaseDir);
        result = 37 * result + ObjectHelper.hashCode(baseURL);
        result = 37 * result + ObjectHelper.hashCode(variantType2PrefixURL);
        return result;
    }
}
