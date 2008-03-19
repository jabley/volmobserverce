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
 * $Header: /src/voyager/com/volantis/mcs/runtime/configuration/xml/RemotePoliciesRuleSet.java,v 1.1 2003/03/20 12:03:17 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 11-Mar-03    Geoff           VBM:2002112102 - Created; adds digester rules 
 *                              for the remote-policies tag. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.runtime.configuration.xml;

import com.volantis.mcs.runtime.configuration.RemotePoliciesConfiguration;
import com.volantis.mcs.runtime.configuration.RemotePolicyCacheConfiguration;
import com.volantis.mcs.runtime.configuration.RemotePolicyQuotaConfiguration;
import our.apache.commons.digester.Digester;

/**
 * Adds digester rules for the remote-policies tag.  
 */ 
public class RemotePoliciesRuleSet extends PrefixRuleSet {

    public RemotePoliciesRuleSet(String prefix) {
        this.prefix = prefix;
    }

    public void addRuleInstances(Digester digester) {
        // <remote-policies>
        final String pattern = prefix + "/remote-policies";
        // NOTE: the arrangement of the "global" policy cache as the parent of 
        // the other policy caches in the XML is rather unfortunate, makes
        // the following hard to understand, fragile and hacky.
        digester.addObjectCreate(pattern,
                RemotePoliciesConfiguration.class);
        digester.addSetNext(pattern,
                "setRemotePolicies");
        digester.addSetProperties(
                pattern, 
                "connection-timeout", "connectionTimeout");

        addRemotePolicyCacheRules(digester);
        addRemotePolicyQuotaRules(digester);
    }

    private void addRemotePolicyCacheRules(Digester digester) {

        // Caches
        String cacheTag = prefix + "/remote-policies/remote-policy-cache";

        digester.addObjectCreate(cacheTag,
                RemotePolicyCacheConfiguration.class);
            digester.addSetNext(cacheTag, "setPolicyCache");

        digester.addSetProperties(cacheTag,
                // NOTE: the following attribute names are inconsistent
                new String[] { "allowRetainDuringRetry",
                    "allowRetryFailedRetrieval", "cachePolicies",
                    "defaultRetainDuringRetry", "defaultRetryFailedRetrieval",
                    "defaultRetryInterval", "defaultRetryMaxCount",
                    "defaultTimeToLive", "maxCacheSize", "maxRetryMaxCount",
                    "maxTimeToLive", "minRetryInterval" },
                new String[] { "allowRetainDuringRetry",
                    "allowRetryFailedRetrieval", "defaultCacheThisPolicy",
                    "defaultRetainDuringRetry", "defaultRetryFailedRetrieval",
                    "defaultRetryInterval", "defaultRetryMaxCount",
                    "defaultTimeToLive", "maxCacheSize", "maxRetryMaxCount",
                    "maxTimeToLive", "minRetryInterval" }
        );
    }

    private void addRemotePolicyQuotaRules(Digester digester) {
        
        // Quotas
        String quotaTag = prefix + "/remote-policies/remote-policy-quotas/remote-policy-quota";
        digester.addObjectCreate(quotaTag,
                RemotePolicyQuotaConfiguration.class);
        digester.addSetNext(quotaTag, "addQuota");
        digester.addSetProperties(quotaTag,
                // NOTE: the following attribute names are inconsistent 
                new String[] { "URL", "percentage" }, 
                new String[] { "url", "percentage" }
        );
    }
    
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 09-Mar-04	2867/1	ianw	VBM:2004012603 Rationalised data source configuration and refactored code to cope with validated config schema

 26-Jan-04	2724/1	geoff	VBM:2004011911 Add projects to config

 ===========================================================================
*/
