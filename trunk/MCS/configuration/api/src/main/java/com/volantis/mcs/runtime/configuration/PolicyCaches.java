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
 * $Header: /src/voyager/com/volantis/mcs/runtime/configuration/PolicyCaches.java,v 1.1 2003/03/20 12:03:17 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 11-Mar-03    Geoff           VBM:2002112102 - Created; holds configuration 
 *                              information about all (local) policies. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.runtime.configuration;

import com.volantis.mcs.policies.PolicyType;

import java.util.HashMap;
import java.util.Map;

/**
 * Holds configuration information about all (local) policies. 
 */ 
public class PolicyCaches {

    /**
     * Map from policy type to cache configuration.
     */
    private Map policyTypeToCache;

    private PolicyCacheConfiguration deviceCache;
    private PolicyCacheConfiguration urlCache;

    public PolicyCaches() {
        policyTypeToCache = new HashMap();
    }

    public PolicyCacheConfiguration getPolicyCache(PolicyType policyType) {
        return (PolicyCacheConfiguration) policyTypeToCache.get(policyType);
    }

    public void setAssetGroupCache(PolicyCacheConfiguration cache) {
        policyTypeToCache.put(PolicyType.BASE_URL, cache);
    }

    public void setAudioComponentCache(PolicyCacheConfiguration cache) {
        policyTypeToCache.put(PolicyType.AUDIO, cache);
    }

    public void setButtonImageComponentCache(PolicyCacheConfiguration cache) {
        policyTypeToCache.put(PolicyType.BUTTON_IMAGE, cache);
    }

    public void setChartComponentCache(PolicyCacheConfiguration cache) {
        policyTypeToCache.put(PolicyType.CHART, cache);
    }

    public PolicyCacheConfiguration getDeviceCache() {
        return deviceCache;
    }

    public void setDeviceCache(PolicyCacheConfiguration deviceCache) {
        this.deviceCache = deviceCache;
    }

    public void setDynamicVisualComponentCache(PolicyCacheConfiguration cache) {
        policyTypeToCache.put(PolicyType.VIDEO, cache);
    }

    public void setImageComponentCache(PolicyCacheConfiguration cache) {
        policyTypeToCache.put(PolicyType.IMAGE, cache);
    }

    public void setLayoutCache(PolicyCacheConfiguration cache) {
        policyTypeToCache.put(PolicyType.LAYOUT, cache);
    }

    public void setLinkComponentCache(PolicyCacheConfiguration cache) {
        policyTypeToCache.put(PolicyType.LINK, cache);
    }

    public void setRolloverImageComponentCache(PolicyCacheConfiguration cache) {
        policyTypeToCache.put(PolicyType.ROLLOVER_IMAGE, cache);
    }

    public void setScriptComponentCache(PolicyCacheConfiguration cache) {
        policyTypeToCache.put(PolicyType.SCRIPT, cache);
    }

    public void setTextComponentCache(PolicyCacheConfiguration cache) {
        policyTypeToCache.put(PolicyType.TEXT, cache);
    }

    public void setThemeCache(PolicyCacheConfiguration cache) {
        policyTypeToCache.put(PolicyType.THEME, cache);
    }

    public PolicyCacheConfiguration getUrlCache() {
        return urlCache;
    }

    public void setUrlCache(PolicyCacheConfiguration urlCache) {
        this.urlCache = urlCache;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 28-Apr-05	7922/2	pduffin	VBM:2005042801 Removed User and UserFactory classes

 28-Apr-05	7914/1	pduffin	VBM:2005042714 Removing ExternalPluginDefinitionsManager, AssetGroup#repositoryName and related classes

 08-Dec-04	6416/6	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/4	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/2	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
