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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/runtime/configuration/xml/PolicyCachesRuleSetTestCase.java,v 1.1 2003/03/20 12:03:17 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 11-Mar-03    Geoff           VBM:2002112102 - Created; test case for 
 *                              PolicyCachesRuleSet. 
 * 03-Jun-03    Allan           VBM:2003060301 - TestCaseAbstract moved to 
 *                              Synergetics. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.runtime.configuration.xml;

import com.volantis.mcs.runtime.configuration.ConfigurationException;
import com.volantis.mcs.runtime.configuration.PolicyCacheConfiguration;
import com.volantis.mcs.runtime.configuration.PolicyCaches;
import com.volantis.mcs.runtime.configuration.MarinerConfiguration;
import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * Test case for {@link PolicyCachesRuleSet}. 
 */ 
public class PolicyCachesRuleSetTestCase extends TestCaseAbstract {

    private static PolicyCacheValue defaultCacheValue;
    static {
        defaultCacheValue = new PolicyCacheValue();
        defaultCacheValue.enabled = Boolean.TRUE;
        defaultCacheValue.strategy = "least-recently-used";
        defaultCacheValue.maxEntries = new Integer("25");
        defaultCacheValue.timeout = new Integer(3600);
    }

    // NOTE: I could reduce the size of this code by using introspection to 
    // find the cache method, but this would take more coding and would 
    // prevent automatic tool support for refactoring from finding this in
    // future. Thus, I deem it is not worth it.
    
    public void testNull() throws ConfigurationException {
        checkPolicyValue(null);
    }

    public void testEmpty() throws ConfigurationException {
        PolicyCacheValue value = new PolicyCacheValue();
        // default values
        value.enabled = Boolean.TRUE;
        value.strategy = "least-used";
        value.maxEntries = new Integer(-1);
        value.timeout = new Integer(-1);
        checkPolicyValue(value);
    }

    public void testFull() throws ConfigurationException {
        PolicyCacheValue value = new PolicyCacheValue();
        // default values
        value.enabled = Boolean.TRUE;
        value.strategy = "least-used";
        value.maxEntries = new Integer("25");
        value.timeout = new Integer(3600);
        checkPolicyValue(value);
    }

    private void checkPolicyValue(PolicyCacheValue value) 
            throws ConfigurationException {
        // could use any value policy name here...
        checkPolicy("asset-group", value);
    }

    
    //addPolicyCacheRules("asset-group", "AssetGroup");
    public void testAssetGroup() throws ConfigurationException {
        checkPolicy("asset-group", defaultCacheValue);
    }

    //addPolicyCacheRules("audio-component", "AudioComponent");
    public void testAudioComponent() throws ConfigurationException {
        checkPolicy("audio-component", defaultCacheValue);
    }
    
    //addPolicyCacheRules("button-image-component", "ButtonImageComponent");
    public void testButtonImageComponent() throws ConfigurationException {
        checkPolicy("button-image-component", defaultCacheValue);
    }

    //addPolicyCacheRules("chart-component", "ChartComponent");
    public void testChartComponent() throws ConfigurationException {
        checkPolicy("chart-component", defaultCacheValue);
    }

    //addPolicyCacheRules("device", "Device");
    public void testDevice() throws ConfigurationException {
        checkPolicy("device", defaultCacheValue);
    }

    //addPolicyCacheRules("dynamic-visual-component", "DynamicVisualComponent");
    public void testDynamicVisualComponent()
            throws ConfigurationException {
        checkPolicy("dynamic-visual-component", defaultCacheValue);
    }

    //addPolicyCacheRules("image-component", "ImageComponent");
    public void testImageComponent() throws ConfigurationException {
        checkPolicy("image-component", defaultCacheValue);
    }

    //addPolicyCacheRules("layout", "Layout");
    public void testLayout() throws ConfigurationException {
        checkPolicy("layout", defaultCacheValue);
    }

    //addPolicyCacheRules("link-component", "LinkComponent");
    public void testLinkComponent() throws ConfigurationException {
        checkPolicy("link-component", defaultCacheValue);
    }

    //addPolicyCacheRules("rollover-image-component", "RolloverImageComponent");
    public void testRolloverImageComponent()
            throws ConfigurationException {
        checkPolicy("rollover-image-component", defaultCacheValue);
    }

    //addPolicyCacheRules("script-component", "ScriptComponent");
    public void testScriptComponent() throws ConfigurationException {
        checkPolicy("script-component", defaultCacheValue);
    }

    //addPolicyCacheRules("text-component", "TextComponent");
    public void testTextComponent() throws ConfigurationException {
        checkPolicy("text-component", defaultCacheValue);
    }

    //addPolicyCacheRules("theme", "Theme");
    public void testTheme() throws ConfigurationException {
        checkPolicy("theme", defaultCacheValue);
    }

    /**
     * Create a subset of the mcs-config XML document from the values 
     * supplied, parse it into a {@link PolicyCaches} object,
     * and ensure that the values supplied are match those found.
     * 
     * @param tagId
     * @param cacheValue
     * @throws ConfigurationException
     */
    private void checkPolicy(String tagId, PolicyCacheValue cacheValue) 
            throws ConfigurationException {
        String doc = ""; 
        doc += "  <policy-cache> \n";
        if (cacheValue != null) {
            doc += "    <" + tagId + "-cache \n";
            if (cacheValue.enabled != null) {
                doc += "  enabled=\"" + cacheValue.enabled + "\"\n"; 
            }
            if (cacheValue.strategy != null) {
                doc += "  strategy=\""+ cacheValue.strategy + "\"\n"; 
            }
            if (cacheValue.maxEntries != null) {
                doc += "  max-entries=\"" + cacheValue.maxEntries + "\"\n";
            }
            if (cacheValue.timeout != null) {
                doc += "  timeout=\"" + cacheValue.timeout + "\"\n";
            }
            doc += "    /> \n";
        }
        doc += "  </policy-cache> \n";
        
        TestXmlConfigurationBuilder configBuilder = 
                new TestXmlConfigurationBuilder(doc);
        MarinerConfiguration config = configBuilder.buildConfiguration();
        assertNotNull(config);
        PolicyCaches caches = config.getPolicies();
        assertNotNull(caches);
        PolicyCacheFinder cacheFinder = new PolicyCacheFinder();
        PolicyCacheConfiguration cache = cacheFinder.find(caches, tagId);
        if (cacheValue != null) {
            assertNotNull("policyCacheConfiguration", cache);
            // enabled attribute is tested via the EnabledDigester test case
            assertEquals(cacheValue.strategy, cache.getStrategy());
            assertEquals(cacheValue.maxEntries, cache.getMaxEntries());
            assertEquals(cacheValue.timeout, cache.getTimeout());
        } else {
            assertNull("policyCacheConfiguration", cache);
        }
    }

    /**
     * A private Value Object class for holding policy cache values.
     */ 
    private static class PolicyCacheValue {
        Boolean enabled;
        String strategy;
        Integer maxEntries;
        Integer timeout;
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

 24-Mar-04	3482/1	geoff	VBM:2004030205 The runtime needs to support the jdbc-repository device repository configuration

 09-Mar-04	2867/1	ianw	VBM:2004012603 Rationalised data source configuration and refactored code to cope with validated config schema

 06-Jan-04	2271/2	geoff	VBM:2003121716 Import/Export: Schemify configuration file: Enable schema validation

 18-Dec-03	2246/1	geoff	VBM:2003121715 debrand config file

 13-Oct-03	1517/1	pcameron	VBM:2003100706 Removed all traces of licensing from MCS

 21-Aug-03	1231/1	geoff	VBM:2003030502 policy-cache max-entries attribute is ignored

 ===========================================================================
*/
