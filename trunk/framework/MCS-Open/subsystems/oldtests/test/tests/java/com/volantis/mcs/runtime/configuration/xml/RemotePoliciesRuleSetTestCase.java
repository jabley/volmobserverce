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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/runtime/configuration/xml/RemotePoliciesRuleSetTestCase.java,v 1.1 2003/03/20 12:03:17 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 11-Mar-03    Geoff           VBM:2002112102 - Created; test case for 
 *                              RemotePoliciesRuleSet. 
 * 03-Jun-03    Allan           VBM:2003060301 - TestCaseAbstract moved to 
 *                              Synergetics. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.runtime.configuration.xml;

import com.volantis.mcs.runtime.configuration.ConfigurationException;
import com.volantis.mcs.runtime.configuration.MarinerConfiguration;
import com.volantis.mcs.runtime.configuration.RemotePoliciesConfiguration;
import com.volantis.mcs.runtime.configuration.RemotePolicyCacheConfiguration;
import com.volantis.mcs.runtime.configuration.RemotePolicyQuotaConfiguration;
import com.volantis.synergetics.testtools.TestCaseAbstract;

import java.util.Iterator;

/**
 * Test case for {@link RemotePoliciesRuleSet}. 
 */ 
public class RemotePoliciesRuleSetTestCase extends TestCaseAbstract {

    private static final RemotePolicyCacheValue defaultRemoteCacheValue;
    static { 
        RemotePolicyCacheValue value = 
            new RemotePolicyCacheValue();
        value.cachePolicies = Boolean.TRUE;
        value.defaultTimeToLive = new Integer(60);
        value.defaultRetryFailedRetrieval = Boolean.TRUE;
        value.defaultRetryInterval = new Integer(62);
        value.defaultRetryMaxCount = new Integer(1);
        value.defaultRetainDuringRetry = Boolean.TRUE;
        value.maxCacheSize = new Integer(1024);
        value.maxTimeToLive = new Integer(63);
        value.allowRetryFailedRetrieval = Boolean.TRUE;
        value.minRetryInterval = new Integer(64);
        value.maxRetryMaxCount = new Integer(2);
        value.allowRetainDuringRetry = Boolean.TRUE;
        defaultRemoteCacheValue = value;
    }
    
    private static final RemotePolicyQuotaValue[] defaultRemoteQuotaValues;
    static { 
        RemotePolicyQuotaValue value = new RemotePolicyQuotaValue();
        value.url = "/some/url/";
        value.percentage = new Integer(10);
        defaultRemoteQuotaValues = new RemotePolicyQuotaValue[]{
            value, value
        };
    }
    
    // NOTE: I could reduce the size of this code by using introspection to 
    // find the cache and quota methods, but this would take more coding and 
    // would prevent automatic tool support for refactoring from finding this 
    // in future. Thus, I deem it is not worth it.
    
    public void testNull() throws ConfigurationException {
        testRemotePolicyValue(null, null);
    }

    public void testEmpty() throws ConfigurationException {
        RemotePolicyCacheValue cacheValue = 
            new RemotePolicyCacheValue();
        RemotePolicyQuotaValue quotaValue = new RemotePolicyQuotaValue();
        RemotePolicyQuotaValue[] quotaValues = new RemotePolicyQuotaValue[]{
            quotaValue, quotaValue
        };
        testRemotePolicyValue(cacheValue, quotaValues);
    }

    public void testFull() throws ConfigurationException {
        RemotePolicyCacheValue cacheValue = 
            new RemotePolicyCacheValue();
        // all are optional
        cacheValue.cachePolicies = Boolean.TRUE;
        cacheValue.defaultTimeToLive = new Integer(60);
        cacheValue.defaultRetryFailedRetrieval = Boolean.TRUE;
        cacheValue.defaultRetryInterval = new Integer(62);
        cacheValue.defaultRetryMaxCount = new Integer(1);
        cacheValue.defaultRetainDuringRetry = Boolean.FALSE;
        cacheValue.maxCacheSize = new Integer(1024);
        cacheValue.maxTimeToLive = new Integer(63);
        cacheValue.allowRetryFailedRetrieval = Boolean.TRUE;
        cacheValue.minRetryInterval = new Integer(64);
        cacheValue.maxRetryMaxCount = new Integer(2);
        cacheValue.allowRetainDuringRetry = Boolean.FALSE;
        RemotePolicyQuotaValue quotaValue = new RemotePolicyQuotaValue();
        quotaValue.url = "/some/url/";
        quotaValue.percentage = new Integer(10);
        RemotePolicyQuotaValue[] quotaValues = new RemotePolicyQuotaValue[]{
            quotaValue, quotaValue
        };
        testRemotePolicyValue(cacheValue, quotaValues);
    }
    
    private void testRemotePolicyValue(RemotePolicyCacheValue cacheValue,
            RemotePolicyQuotaValue[] quotaValues) 
            throws ConfigurationException {
        // could choose any value policy type here...
        checkRemotePolicy(null,
                cacheValue, quotaValues
        );
    }

    public void testGlobal() throws ConfigurationException {
        checkRemotePolicy(new Integer(1),
                defaultRemoteCacheValue, defaultRemoteQuotaValues);
    }
    
    // asset-group
    // NOTE: named inconsistently in Schema (no "Policy")
    public void testPolicy() throws ConfigurationException {
        checkRemotePolicy(new Integer(2),
                defaultRemoteCacheValue, defaultRemoteQuotaValues);
    }

    // NOTE: the way that defaults are handled for cache and quotas is
    // inconsistent and makes this much more complex than it ought to be 

    /**
     * Create a subset of the mcs-config XML document from the values 
     * supplied, parse it into a {@link RemotePoliciesConfiguration} object, 
     * and ensure that the values supplied are match those found.
     * 
     * @param connectionTimeout
     * @param cacheValue
     * @param quotaValues
     * @throws ConfigurationException
     */ 
    private void checkRemotePolicy(
            Integer connectionTimeout,
            RemotePolicyCacheValue cacheValue,
            RemotePolicyQuotaValue quotaValues[]) throws ConfigurationException {

        String doc = ""; 
        doc += "  <remote-policies";
        if (connectionTimeout != null) {
            doc += "    connection-timeout=\"" + connectionTimeout + "\" \n";
        }
        doc += "> \n";

        if (cacheValue != null) {
            // Caches
            doc += "    <remote-policy-cache \n";

            if (cacheValue.cachePolicies != null) {
                doc += "        cachePolicies=\"" + 
                        cacheValue.cachePolicies + "\" \n";
            }
            if (cacheValue.defaultTimeToLive != null) {
                doc += "        defaultTimeToLive=\"" + 
                        cacheValue.defaultTimeToLive + "\" \n";
            }
            if (cacheValue.defaultRetryFailedRetrieval != null) {
                doc += "        defaultRetryFailedRetrieval=\"" + 
                        cacheValue.defaultRetryFailedRetrieval + "\" \n";
            }
            if (cacheValue.defaultRetryInterval != null) {
                doc += "        defaultRetryInterval=\"" + 
                        cacheValue.defaultRetryInterval + "\" \n";
            }
            if (cacheValue.defaultRetryMaxCount != null) {
                doc += "        defaultRetryMaxCount=\"" + 
                        cacheValue.defaultRetryMaxCount + "\" \n";
            }
            if (cacheValue.defaultRetainDuringRetry != null) {
                doc += "        defaultRetainDuringRetry=\"" + 
                        cacheValue.defaultRetainDuringRetry + "\" \n";
            }
            if (cacheValue.maxCacheSize != null) {
                doc += "        maxCacheSize=\"" + 
                        cacheValue.maxCacheSize + "\" \n";
            }
            if (cacheValue.maxTimeToLive != null) {
                doc += "        maxTimeToLive=\"" + 
                        cacheValue.maxTimeToLive + "\" \n";
            }
            if (cacheValue.allowRetryFailedRetrieval != null) {
                doc += "        allowRetryFailedRetrieval=\"" + 
                        cacheValue.allowRetryFailedRetrieval + "\" \n";
            }
            if (cacheValue.minRetryInterval != null) {
                doc += "        minRetryInterval=\"" + 
                        cacheValue.minRetryInterval + "\" \n";
            }
            if (cacheValue.maxRetryMaxCount != null) {
                doc += "        maxRetryMaxCount=\"" + 
                        cacheValue.maxRetryMaxCount + "\" \n";
            }
            if (cacheValue.allowRetainDuringRetry != null) {
                doc += "        allowRetainDuringRetry=\"" + 
                        cacheValue.allowRetainDuringRetry + "\" \n";
            }

            doc += "      /> \n";
        }
        
        // Quotas
        // NOTE: Quotas uses all-policies to represent global policies as per
        // the spec.
        
        if (quotaValues != null) {
            doc += "    <remote-policy-quotas> \n";
            if (quotaValues.length > 0) {
                for (int i=0; i < quotaValues.length; i++) {
                    RemotePolicyQuotaValue quotaValue = quotaValues[i];
                    if (quotaValue != null) {
                        doc += "        <remote-policy-quota \n";
                        if (quotaValue.url != null) {
                            doc += "          URL=\"" + quotaValue.url + 
                                    "\" \n";
                        }
                        if (quotaValue.percentage != null) {
                            doc += "          percentage=\"" + 
                                    quotaValue.percentage + "\" \n";
                        }
                        doc += "        /> \n";
                    }
                }
            }
            doc += "    </remote-policy-quotas> \n";
        }
        
        doc += "  </remote-policies> \n";
        
        TestXmlConfigurationBuilder configBuilder = 
                new TestXmlConfigurationBuilder(doc);
        MarinerConfiguration config = configBuilder.buildConfiguration();
        assertNotNull(config);
        RemotePoliciesConfiguration policies = config.getRemotePolicies();
        assertNotNull(policies);
        assertEquals(connectionTimeout, policies.getConnectionTimeout());
        RemotePolicyCacheConfiguration cache = policies.getPolicyCache();
        Iterator quotas = policies.getQuotaIterator();

        if (cacheValue != null) {
            assertNotNull("RemotePolicyCacheConfiguration", cache);
            assertEquals(cacheValue.cachePolicies, 
                    cache.getDefaultCacheThisPolicy());
            assertEquals(cacheValue.defaultTimeToLive, 
                    cache.getDefaultTimeToLive());
            assertEquals(cacheValue.defaultRetryFailedRetrieval, 
                    cache.getDefaultRetryFailedRetrieval());
            assertEquals(cacheValue.defaultRetryInterval, 
                    cache.getDefaultRetryInterval());
            assertEquals(cacheValue.defaultRetryMaxCount, 
                    cache.getDefaultRetryMaxCount());
            assertEquals(cacheValue.defaultRetainDuringRetry, 
                    cache.getDefaultRetainDuringRetry());
            assertEquals(cacheValue.maxCacheSize, 
                    cache.getMaxCacheSize());
            assertEquals(cacheValue.maxTimeToLive, 
                    cache.getMaxTimeToLive());
            assertEquals(cacheValue.allowRetryFailedRetrieval, 
                    cache.getAllowRetryFailedRetrieval());
            assertEquals(cacheValue.minRetryInterval, 
                    cache.getMinRetryInterval());
            assertEquals(cacheValue.maxRetryMaxCount, 
                    cache.getMaxRetryMaxCount());
            assertEquals(cacheValue.allowRetainDuringRetry, 
                    cache.getAllowRetainDuringRetry());
        } else {
            assertNull("RemotePolicyCacheConfiguration", cache);
        }

        if (quotaValues != null) {
            assertNotNull("quotas", quotas);
            assertTrue(quotas.hasNext());
            for (int i=0; i < quotaValues.length; i++) {
                RemotePolicyQuotaConfiguration quota = 
                        (RemotePolicyQuotaConfiguration) quotas.next();
                RemotePolicyQuotaValue quotaValue = quotaValues[i];
                if (quotaValue != null) {
                    assertNotNull("quota", quota);
                    assertEquals(quotaValue.url, quota.getUrl());
                    assertEquals(quotaValue.percentage, quota.getPercentage());
                } else {
                    assertNull("quota", quota);
                }
            }
            assertTrue(!quotas.hasNext());
        } else {
            assertNotNull("quotas", quotas);
            assertTrue(!quotas.hasNext());
        }
    }

    /**
     * A private Value Object class for holding remote policy values.
     */ 
    private static class RemotePolicyCacheValue {
        Boolean cachePolicies;
        Integer defaultTimeToLive;
        Boolean defaultRetryFailedRetrieval;
        Integer defaultRetryInterval;
        Integer defaultRetryMaxCount;
        Boolean defaultRetainDuringRetry;
        Integer maxCacheSize;
        Integer maxTimeToLive;
        Boolean allowRetryFailedRetrieval;
        Integer minRetryInterval;
        Integer maxRetryMaxCount;
        Boolean allowRetainDuringRetry;
    }

    private static class RemotePolicyQuotaValue {
        String url = "/some/url/";
        Integer percentage = new Integer(10);
    }
    
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 24-Mar-04	3482/1	geoff	VBM:2004030205 The runtime needs to support the jdbc-repository device repository configuration

 09-Mar-04	2867/1	ianw	VBM:2004012603 Rationalised data source configuration and refactored code to cope with validated config schema

 06-Jan-04	2271/2	geoff	VBM:2003121716 Import/Export: Schemify configuration file: Enable schema validation

 18-Dec-03	2246/1	geoff	VBM:2003121715 debrand config file

 13-Oct-03	1517/1	pcameron	VBM:2003100706 Removed all traces of licensing from MCS

 ===========================================================================
*/
