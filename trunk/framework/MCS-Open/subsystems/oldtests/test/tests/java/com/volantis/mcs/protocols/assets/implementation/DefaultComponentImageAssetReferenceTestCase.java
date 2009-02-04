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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols.assets.implementation;

import com.volantis.mcs.integration.PageURLType;
import com.volantis.mcs.policies.PolicyReference;
import com.volantis.mcs.policies.PolicyType;
import com.volantis.mcs.protocols.assets.ImageAssetReference;
import com.volantis.mcs.protocols.assets.TextAssetReference;
import com.volantis.mcs.runtime.policies.ActivatedVariablePolicyMock;
import com.volantis.mcs.runtime.policies.RuntimePolicyReference;
import com.volantis.mcs.runtime.policies.RuntimePolicyReferenceMock;
import com.volantis.mcs.runtime.policies.SelectedVariantMock;

/**
 * This is a test class for a subclass of {@link AbstractComponentImageAssetReference}
 * which provides an implementation of a test instance and also tests the
 * functionality introduced in the specialisation.
 */
public class DefaultComponentImageAssetReferenceTestCase
                    extends AbstractComponentImageAssetReferenceTestAbstract {

    /**
     * The identity used when creating and test instance, and in the tests.
     */
    protected RuntimePolicyReferenceMock policyReferenceMock;
    protected AssetResolverMock assetResolverMock;

    protected void setUp() throws Exception {
        super.setUp();

        assetResolverMock = new AssetResolverMock("assetResolverMock",
                expectations);

        policyReferenceMock = new RuntimePolicyReferenceMock(
                "policyReferenceMock", expectations);
    }

    /**
     * This tests retrieving the url from the test instance of an
     * {@link com.volantis.mcs.protocols.assets.ImageAssetReference}.
     */
    public void testGetURL() throws Exception{

        final SelectedVariantMock selectedVariantMock =
                new SelectedVariantMock("selectedVariantMock", expectations);

        assetResolverMock.expects.selectBestVariant(policyReferenceMock, null)
                .returns(selectedVariantMock).any();

        assetResolverMock.expects.computeURLAsString(selectedVariantMock)
                .returns("/resolved.gif").any();
        
        assetResolverMock.expects.rewriteURLWithPageURLRewriter("/resolved.gif", PageURLType.IMAGE)
                .returns("/resolved.gif").any();

        ImageAssetReference test = createTestInstance(policyReferenceMock);
        String url = test.getURL();
        assertEquals("/resolved.gif", url);
//        String expected = AssetResolverTestHelper.TEST_PREFIX +
//                          AssetResolverTestHelper.TEST_NAME;
//        assertNotNull("The url should not be null", url);
//        assertEquals("URL should match: " + expected + "but was: " + url,
//                     url, expected);
    }

    /**
     * This tests getting the text fallback asset from the asset based on an
     * instance of an
     * {@link com.volantis.mcs.protocols.assets.ImageAssetReference}.
     */
    public void testGetTextFallback() throws Exception {

        final SelectedVariantMock selectedVariantMock =
                new SelectedVariantMock("selectedVariantMock", expectations);

        assetResolverMock.expects.selectBestVariant(policyReferenceMock, null)
                .returns(selectedVariantMock).any();

        final ActivatedVariablePolicyMock policyMock =
                new ActivatedVariablePolicyMock("policyMock",
                        expectations);

        selectedVariantMock.expects.getPolicy()
                .returns(policyMock).any();

        final RuntimePolicyReferenceMock alternativeMock =
                new RuntimePolicyReferenceMock("alternativeMock",
                        expectations);

        policyMock.expects.getAlternatePolicy(PolicyType.TEXT)
                .returns(alternativeMock).any();

        ImageAssetReference test = createTestInstance(policyReferenceMock);
        TextAssetReference fallback = test.getTextFallback();
        assertNotNull("fallback should exist", fallback);
    }

    /**
     * This tests getting the text fallback asset from the asset if the policy
     * is null.
     */
    public void testGetTextFallbackWhenPolicyIsNull() throws Exception {

        final SelectedVariantMock selectedVariantMock =
                new SelectedVariantMock("selectedVariantMock", expectations);

        assetResolverMock.expects.selectBestVariant(policyReferenceMock, null)
                .returns(selectedVariantMock).any();

        final ActivatedVariablePolicyMock policyMock =
                new ActivatedVariablePolicyMock("policyMock",
                        expectations);

        selectedVariantMock.expects.getPolicy()
                .returns(null).any();

        final RuntimePolicyReferenceMock alternativeMock =
                new RuntimePolicyReferenceMock("alternativeMock",
                        expectations);

        policyMock.expects.getAlternatePolicy(PolicyType.TEXT)
                .returns(alternativeMock).any();

        ImageAssetReference test = createTestInstance(policyReferenceMock);
        TextAssetReference fallback = test.getTextFallback();
        assertNull("fallback should not exist", fallback);
    }

    /**
     * A shortcut to {@link #createTestInstance(AssetResolver,com.volantis.mcs.runtime.policies.RuntimePolicyReference)}
     * that provides an asset resolver.
     *
     * @return     A test instance of an ImageAssetReference
     */
    protected ImageAssetReference createTestInstance(RuntimePolicyReference reference) {
        return createTestInstance(assetResolverMock, reference);
    }

    /**
     * This tests the retrieval of a component identity from the test instance
     */
    public void testGetComponentIdentity() throws Exception {
        AbstractComponentImageAssetReference test =
                (AbstractComponentImageAssetReference)createTestInstance(policyReferenceMock);
        PolicyReference testReference = test.getPolicyReference();
        assertNotNull("The reference should not be null", testReference);
        assertEquals("The references should match", policyReferenceMock, testReference);
    }

    // JavaDoc inherited
    protected ImageAssetReference createTestInstance(
            AssetResolver resolver,
            RuntimePolicyReference reference) {

        return new DefaultComponentImageAssetReference(reference, resolver);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 07-Apr-04	3753/1	claire	VBM:2004040612 Increasing laziness of reference resolution

 26-Mar-04	3500/1	claire	VBM:2004031806 Initial implementation of abstract component image references

 ===========================================================================
*/
