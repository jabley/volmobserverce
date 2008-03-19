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

package com.volantis.mcs.policies.compatibility;

import com.volantis.mcs.assets.AssetGroup;
import com.volantis.mcs.components.AudioComponent;
import com.volantis.mcs.components.ChartComponent;
import com.volantis.mcs.components.DynamicVisualComponent;
import com.volantis.mcs.components.ImageComponent;
import com.volantis.mcs.components.LinkComponent;
import com.volantis.mcs.components.RolloverImageComponent;
import com.volantis.mcs.components.ScriptComponent;
import com.volantis.mcs.components.TextComponent;
import com.volantis.mcs.objects.CacheableRepositoryObject;
import com.volantis.mcs.policies.BaseURLPolicyBuilder;
import com.volantis.mcs.policies.CacheControl;
import com.volantis.mcs.policies.CacheControlBuilder;
import com.volantis.mcs.policies.Policy;
import com.volantis.mcs.policies.PolicyBuilder;
import com.volantis.mcs.policies.PolicyType;
import com.volantis.mcs.policies.RolloverImagePolicyBuilder;
import com.volantis.mcs.policies.VariablePolicyBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Test converting from old components / assets to new policies.
 */
public class Component2PolicyTestCase
        extends Old2NewConverterTestAbstract {

    /**
     * Test that an audio component can be converted to a policy.
     */
    public void testAudioComponent() {
        AudioComponent component = new AudioComponent();
        component.setName("/foo.mimg");
        component.setFallbackAudioComponentName("/fallback.mauc");
        component.setFallbackTextComponentName("/fallback.mtxt");

        VariablePolicyBuilder policyBuilder = (VariablePolicyBuilder)
                converter.component2VariablePolicyBuilder(component);

        checkName(component, policyBuilder);
        checkVariablePolicyType(policyBuilder, PolicyType.AUDIO);

        // Check alternates
        List alternates = policyBuilder.getAlternatePolicies();

        List expectedAlternates = new ArrayList();
        expectedAlternates.add(factory.createPolicyReference("/fallback.mauc",
                PolicyType.AUDIO));
        expectedAlternates.add(factory.createPolicyReference("/fallback.mtxt",
                PolicyType.TEXT));

        assertEquals("Alternates", expectedAlternates, alternates);
    }

    /**
     * Test that a chart component can be converted to a policy.
     */
    public void testChartComponent() {
        ChartComponent component = new ChartComponent();
        component.setName("/foo.mimg");
        component.setFallbackImageComponentName("/fallback.mimg");
        component.setFallbackChartComponentName("/fallback.mcht");
        component.setFallbackTextComponentName("/fallback.mtxt");

        VariablePolicyBuilder policyBuilder = (VariablePolicyBuilder)
                converter.component2VariablePolicyBuilder(component);

        ensureCacheControlNotSet(policyBuilder);

        checkName(component, policyBuilder);
        checkVariablePolicyType(policyBuilder, PolicyType.CHART);

        // Check alternates
        List alternates = policyBuilder.getAlternatePolicies();

        List expectedAlternates = new ArrayList();
        expectedAlternates.add(factory.createPolicyReference("/fallback.mcht",
                PolicyType.CHART));
        expectedAlternates.add(factory.createPolicyReference("/fallback.mimg",
                PolicyType.IMAGE));
        expectedAlternates.add(factory.createPolicyReference("/fallback.mtxt",
                PolicyType.TEXT));

        assertEquals("Alternates", expectedAlternates, alternates);
    }

    private void ensureCacheControlNotSet(PolicyBuilder policyBuilder) {
        CacheControlBuilder cacheControl =
                policyBuilder.getCacheControlBuilder();
        assertNull("Cache Control not null", cacheControl);
    }

    /**
     * Test that a dynamic visual component can be converted to a policy.
     */
    public void testDynamicVisualComponent() {
        DynamicVisualComponent component = new DynamicVisualComponent();
        component.setName("/foo.mimg");
        component.setFallbackImageComponentName("/fallback.mimg");
        component.setFallbackAudioComponentName("/fallback.mauc");
        component.setFallbackDynVisComponentName("/fallback.mdyv");
        component.setFallbackTextComponentName("/fallback.mtxt");

        VariablePolicyBuilder policyBuilder = (VariablePolicyBuilder)
                converter.component2VariablePolicyBuilder(component);

        ensureCacheControlNotSet(policyBuilder);

        checkName(component, policyBuilder);
        checkVariablePolicyType(policyBuilder, PolicyType.VIDEO);

        // Check alternates
        List alternates = policyBuilder.getAlternatePolicies();

        List expectedAlternates = new ArrayList();
        expectedAlternates.add(factory.createPolicyReference("/fallback.mdyv",
                PolicyType.VIDEO));
        expectedAlternates.add(factory.createPolicyReference("/fallback.mauc",
                PolicyType.AUDIO));
        expectedAlternates.add(factory.createPolicyReference("/fallback.mimg",
                PolicyType.IMAGE));
        expectedAlternates.add(factory.createPolicyReference("/fallback.mtxt",
                PolicyType.TEXT));

        assertEquals("Alternates", expectedAlternates, alternates);
    }

    /**
     * Test that an image component can be converted to a policy.
     */
    public void testImageComponent() {
        ImageComponent component = new ImageComponent();
        component.setName("/foo.mimg");
        component.setFallbackImageComponentName("/fallback.mimg");
        component.setFallbackTextComponentName("/fallback.mtxt");

        VariablePolicyBuilder policyBuilder = (VariablePolicyBuilder)
                converter.component2VariablePolicyBuilder(component);

        ensureCacheControlNotSet(policyBuilder);

        checkName(component, policyBuilder);
        checkVariablePolicyType(policyBuilder, PolicyType.IMAGE);

        // Check alternates
        List alternates = policyBuilder.getAlternatePolicies();

        List expectedAlternates = new ArrayList();
        expectedAlternates.add(factory.createPolicyReference("/fallback.mimg",
                PolicyType.IMAGE));
        expectedAlternates.add(factory.createPolicyReference("/fallback.mtxt",
                PolicyType.TEXT));

        assertEquals("Alternates", expectedAlternates, alternates);
    }

    /**
     * Test that a link component can be converted to a policy.
     */
    public void testLinkComponent() {
        LinkComponent component = new LinkComponent();
        component.setName("/foo.mimg");
        component.setFallbackTextComponentName("/fallback.mtxt");

        VariablePolicyBuilder policyBuilder = (VariablePolicyBuilder)
                converter.component2VariablePolicyBuilder(component);

        ensureCacheControlNotSet(policyBuilder);

        checkName(component, policyBuilder);
        checkVariablePolicyType(policyBuilder, PolicyType.LINK);

        // Check alternates
        List alternates = policyBuilder.getAlternatePolicies();

        List expectedAlternates = new ArrayList();
        expectedAlternates.add(factory.createPolicyReference("/fallback.mtxt",
                PolicyType.TEXT));

        assertEquals("Alternates", expectedAlternates, alternates);
    }

    /**
     * Test that a rollover component can be converted to a policy.
     */
    public void testRolloverComponent() {
        RolloverImageComponent component = new RolloverImageComponent();
        component.setName("/foo.mimg");
        component.setFallbackTextComponentName("/fallback.mtxt");
        component.setNormalImageComponentName("/normal.mimg");
        component.setOverImageComponentName("/over.mimg");

        RolloverImagePolicyBuilder policyBuilder = (RolloverImagePolicyBuilder)
                converter.component2VariablePolicyBuilder(component);

        ensureCacheControlNotSet(policyBuilder);

        checkName(component, policyBuilder);
        assertEquals("Normal image",
                factory.createPolicyReference("/normal.mimg", PolicyType.IMAGE),
                policyBuilder.getNormalPolicy());
        assertEquals("Over image",
                factory.createPolicyReference("/over.mimg", PolicyType.IMAGE),
                policyBuilder.getOverPolicy());

        // Check alternates
        List alternates = policyBuilder.getAlternatePolicies();

        List expectedAlternates = new ArrayList();
        expectedAlternates.add(factory.createPolicyReference("/fallback.mtxt",
                PolicyType.TEXT));

        assertEquals("Alternates", expectedAlternates, alternates);
    }

    /**
     * Test that a script component can be converted to a policy.
     */
    public void testScriptComponent() {
        ScriptComponent component = new ScriptComponent();
        component.setName("/foo.mimg");

        VariablePolicyBuilder policyBuilder = (VariablePolicyBuilder)
                converter.component2VariablePolicyBuilder(component);

        ensureCacheControlNotSet(policyBuilder);

        checkName(component, policyBuilder);
        checkVariablePolicyType(policyBuilder, PolicyType.SCRIPT);

        // Check alternates
        List alternates = policyBuilder.getAlternatePolicies();

        List expectedAlternates = new ArrayList();

        assertEquals("Alternates", expectedAlternates, alternates);
    }

    /**
     * Test that a text component can be converted to a policy.
     */
    public void testTextComponent() {
        TextComponent component = new TextComponent();
        component.setName("/foo.mimg");
        component.setFallbackTextComponentName("/fallback.mtxt");

        VariablePolicyBuilder policyBuilder = (VariablePolicyBuilder)
                converter.component2VariablePolicyBuilder(component);

        checkName(component, policyBuilder);
        checkVariablePolicyType(policyBuilder, PolicyType.TEXT);

        // Check alternates
        List alternates = policyBuilder.getAlternatePolicies();

        List expectedAlternates = new ArrayList();
        expectedAlternates.add(factory.createPolicyReference("/fallback.mtxt",
                PolicyType.TEXT));

        assertEquals("Alternates", expectedAlternates, alternates);
    }

    /**
     * Test that if a component is cached then a cache control object is
     * created and populated.
     */
    public void testCacheControl() {
        AudioComponent component = new AudioComponent();
        component.setName("/foo.mauc");
        CacheableRepositoryObject cacheable =
                (CacheableRepositoryObject) component;
        cacheable.setCacheThisPolicy(true);
        cacheable.setRetainDuringRetry(true);
        cacheable.setRetryFailedRetrieval(true);
        cacheable.setRetryInterval(5);
        cacheable.setRetryMaxCount(10);
        cacheable.setTimeToLive(15);

        Policy policy = converter.component2VariablePolicyBuilder(component)
                .getPolicy();

        CacheControl cacheControl = policy.getCacheControl();
        assertNotNull("Cache Control is null", cacheControl);
        assertEquals("CacheThisPolicy", cacheable.getCacheThisPolicy(),
                cacheControl.getCacheThisPolicy());
        assertEquals("RetainDuringRetry", cacheable.getRetainDuringRetry(),
                cacheControl.getRetainDuringRetry());
        assertEquals("RetryFailedRetrieval",
                cacheable.getRetryFailedRetrieval(),
                cacheControl.getRetryFailedRetrieval());
        assertEquals("RetryInterval", cacheable.getRetryInterval(),
                cacheControl.getRetryInterval());
        assertEquals("RetryMaxCount", cacheable.getRetryMaxCount(),
                cacheControl.getRetryMaxCount());
        assertEquals("TimeToLive", cacheable.getTimeToLive(),
                cacheControl.getTimeToLive());
    }

    public void testAssetGroup() {
        AssetGroup assetGroup = new AssetGroup();
        assetGroup.setName("/foo.mgrp");
        assetGroup.setPrefixURL("abc");

        BaseURLPolicyBuilder policyBuilder = (BaseURLPolicyBuilder)
                converter.component2VariablePolicyBuilder(assetGroup);

        checkName(assetGroup, policyBuilder);
        assertEquals(policyBuilder.getBaseURL(), "abc");
    }

}
