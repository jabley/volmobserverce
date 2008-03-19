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
 * $Header: /src/voyager/com/volantis/mcs/runtime/configuration/xml/PolicyCachesRuleSet.java,v 1.1 2003/03/20 12:03:17 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 11-Mar-03    Geoff           VBM:2002112102 - Created; adds digester rules 
 *                              for the policy-cache tag. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.runtime.configuration.xml;

import our.apache.commons.digester.Digester;
import com.volantis.mcs.runtime.configuration.PolicyCaches;
import com.volantis.mcs.runtime.configuration.PolicyCacheConfiguration;

/**
 * Adds digester rules for the policy-cache tag.  
 */ 
public class PolicyCachesRuleSet extends PrefixRuleSet {

    public PolicyCachesRuleSet(String prefix) {
        this.prefix = prefix;
    }

    public void addRuleInstances(Digester digester) {
        // <policy-cache>
        final String pattern = prefix + "/policy-cache";
        digester.addObjectCreate(pattern, 
                PolicyCaches.class);
        digester.addSetNext(pattern,
                "setPolicies");
        addPolicyCacheRules(digester,
                "asset-group", "AssetGroup");
        addPolicyCacheRules(digester,
                "audio-component", "AudioComponent");
        addPolicyCacheRules(digester,
                "button-image-component", "ButtonImageComponent");
        addPolicyCacheRules(digester,
                "chart-component", "ChartComponent");
        addPolicyCacheRules(digester,
                "device", "Device");
        addPolicyCacheRules(digester,
                "dynamic-visual-component", "DynamicVisualComponent");
        addPolicyCacheRules(digester,
                "image-component", "ImageComponent");
        addPolicyCacheRules(digester,
                "layout", "Layout");
        addPolicyCacheRules(digester,
                "link-component", "LinkComponent");
        addPolicyCacheRules(digester,
                "rollover-image-component", "RolloverImageComponent");
        addPolicyCacheRules(digester,
                "script-component", "ScriptComponent");
        addPolicyCacheRules(digester,
                "text-component", "TextComponent");
        addPolicyCacheRules(digester,
                "theme", "Theme");
        addPolicyCacheRules(digester,
                "url", "Url");
    }
    
    private void addPolicyCacheRules(Digester digester,
            String tagId, String methodId) {

        final String pattern = prefix + "/policy-cache";                
        digester.addObjectCreate(
            pattern + "/" + tagId + "-cache", 
                PolicyCacheConfiguration.class);
        digester.addSetNext(
                pattern + "/" + tagId + "-cache",
                "set" + methodId + "Cache");
        digester.addSetProperties(
                pattern + "/" + tagId + "-cache",
                new String[] {"strategy", "max-entries", "timeout"},
                new String[] {"strategy", "maxEntries", "timeout"});
    }

    
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 28-Apr-05	7914/1	pduffin	VBM:2005042714 Removing ExternalPluginDefinitionsManager, AssetGroup#repositoryName and related classes

 27-Apr-05	7896/1	pduffin	VBM:2005042709 Removing PolicyPreference and all related classes

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 09-Mar-04	2867/1	ianw	VBM:2004012603 Rationalised data source configuration and refactored code to cope with validated config schema

 06-Jan-04	2271/1	geoff	VBM:2003121716 Import/Export: Schemify configuration file: Enable schema validation

 21-Aug-03	1231/1	geoff	VBM:2003030502 policy-cache max-entries attribute is ignored

 ===========================================================================
*/
