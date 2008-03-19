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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/runtime/configuration/xml/PolicyCacheFinder.java,v 1.1 2003/03/20 12:03:17 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 12-Mar-03    Geoff           VBM:2002112102 - Created; implements a mapping 
 *                              between mariner-config tag names and the 
 *                              individual PolicyCacheConfiguration objects of 
 *                              a PolicyCaches object.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.runtime.configuration.xml;

import com.volantis.mcs.policies.PolicyType;
import com.volantis.mcs.runtime.configuration.PolicyCacheConfiguration;
import com.volantis.mcs.runtime.configuration.PolicyCaches;

import java.util.HashMap;
import java.util.Map;

/**
 * Implements a mapping between mcs-config tag names and the individual 
 * {@link PolicyCacheConfiguration} objects of a {@link PolicyCaches}
 * object.
 */ 
public class PolicyCacheFinder {

    /**
     * Private interface for anonymous inner classes to implement for 
     * providing policy cache values.
     */ 
    private static interface PolicyCacheProvider {
        PolicyCacheConfiguration getCache(PolicyCaches policies);
    }

    private static class PolicyTypeCacheProvider
            implements PolicyCacheProvider {

        private final PolicyType policyType;

        public PolicyTypeCacheProvider(PolicyType policyType) {
            this.policyType = policyType;
        }

        public PolicyCacheConfiguration getCache(PolicyCaches policies) {
            return policies.getPolicyCache(policyType);
        }
    }


    /**
     * The map between mcs-config tag names and 
     * {@link PolicyCacheProvider} objects.
     */ 
    private Map providerMap = new HashMap();

    /**
     * Construct an instance of this class.
     */ 
    public PolicyCacheFinder() {
        //"asset-group"
        providerMap.put("asset-group",
                new PolicyTypeCacheProvider(PolicyType.BASE_URL));

        //"audio-component"
        providerMap.put("audio-component",
                new PolicyTypeCacheProvider(PolicyType.AUDIO));

        //"button-image-component"
        providerMap.put("button-image-component",
                new PolicyTypeCacheProvider(PolicyType.BUTTON_IMAGE));

        //"chart-component"
        providerMap.put("chart-component",
                new PolicyTypeCacheProvider(PolicyType.CHART));

        //"device"
        providerMap.put("device",
                new PolicyCacheProvider() {
                    public PolicyCacheConfiguration getCache(
                            PolicyCaches policies) {
                        return policies.getDeviceCache();
                    }
                });

        //"dynamic-visual-component"
        providerMap.put("dynamic-visual-component",
                new PolicyTypeCacheProvider(PolicyType.VIDEO));

        //"image-component"
        providerMap.put("image-component",
                new PolicyTypeCacheProvider(PolicyType.IMAGE));

        //"layout"
        providerMap.put("layout",
                new PolicyTypeCacheProvider(PolicyType.LAYOUT));

        //"link-component"
        providerMap.put("link-component",
                new PolicyTypeCacheProvider(PolicyType.LINK));

        //"rollover-image-component"
        providerMap.put("resource-component",
                new PolicyTypeCacheProvider(PolicyType.RESOURCE));

        //"rollover-image-component"
        providerMap.put("rollover-image-component",
                new PolicyTypeCacheProvider(PolicyType.ROLLOVER_IMAGE));

        //"script-component"
        providerMap.put("script-component",
                new PolicyTypeCacheProvider(PolicyType.SCRIPT));

        //"text-component"
        providerMap.put("text-component",
                new PolicyTypeCacheProvider(PolicyType.TEXT));

        //"theme"
        providerMap.put("theme",
                new PolicyTypeCacheProvider(PolicyType.THEME));
    }

    /**
     * Returns individual {@link PolicyCacheConfiguration}s from a 
     * {@link PolicyCaches} object by mcs-config tag name, for
     * test purposes.
     * 
     * @param policies
     * @param tagName
     * @return the PolicyCacheConfiguration
     */ 
    public PolicyCacheConfiguration find(PolicyCaches policies,
            String tagName) {
        PolicyCacheProvider provider = 
                (PolicyCacheProvider) providerMap.get(tagName);
        if (provider != null) {
            return provider.getCache(policies);
        }
        return null;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 28-Apr-05	7922/2	pduffin	VBM:2005042801 Removed User and UserFactory classes

 28-Apr-05	7914/1	pduffin	VBM:2005042714 Removing ExternalPluginDefinitionsManager, AssetGroup#repositoryName and related classes

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 23-Mar-04	3362/1	steve	VBM:2003082208 Move API doclet to Synergetics and myriads of javadoc fixes

 06-Jan-04	2271/1	geoff	VBM:2003121716 Import/Export: Schemify configuration file: Enable schema validation

 18-Dec-03	2246/1	geoff	VBM:2003121715 debrand config file

 ===========================================================================
*/
