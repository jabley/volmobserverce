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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols.assets.implementation;

import com.volantis.mcs.policies.ConcretePolicyMock;
import com.volantis.mcs.policies.PolicyType;
import com.volantis.mcs.policies.variants.VariantMock;
import com.volantis.mcs.policies.variants.text.TextEncoding;
import com.volantis.mcs.policies.variants.text.TextMetaDataMock;
import com.volantis.mcs.protocols.assets.TextAssetReferenceMock;
import com.volantis.mcs.runtime.policies.RuntimePolicyReferenceMock;
import com.volantis.mcs.runtime.policies.SelectedVariantMock;
import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * A test case for {@link FallbackComponentTextAssetReference}.
 * 
 * @todo this should probably be included in the same hierarchy as the
 * other asset reference tests. No time now...
 */ 
public class FallbackComponentTextAssetReferenceTestCase 
        extends TestCaseAbstract {
    private AssetResolverMock assetResolverMock;
    private ConcretePolicyMock concretePolicyMock;
    private TextAssetReferenceMock textAssetReferenceMock;
    private RuntimePolicyReferenceMock referenceMock;
    private VariantMock variantMock;
    private SelectedVariantMock selectedVariantMock;
    private TextMetaDataMock textMetaDataMock;

    protected void setUp() throws Exception {
        super.setUp();

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        assetResolverMock = new AssetResolverMock("assetResolverMock", expectations);

        concretePolicyMock = new ConcretePolicyMock("concretePolicyMock", expectations);

        textAssetReferenceMock = new TextAssetReferenceMock("textAssetReferenceMock",
                                expectations);

        referenceMock = new RuntimePolicyReferenceMock("referenceMock",
                                expectations);

        variantMock = new VariantMock("variantMock", expectations);

        selectedVariantMock = new SelectedVariantMock("selectedVariantMock", expectations);

        textMetaDataMock = new TextMetaDataMock("textMetaDataMock", expectations);
    }

    /**
     * Basic test for getText().
     */
    public void testGetText() throws Exception {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        concretePolicyMock.expects.getAlternatePolicy(PolicyType.TEXT)
                .returns(referenceMock).any();

        assetResolverMock.expects.selectBestVariant(referenceMock, null)
                .returns(selectedVariantMock).any();

        selectedVariantMock.expects.getVariant().returns(variantMock).any();

        variantMock.expects.getMetaData().returns(textMetaDataMock).any();

        textMetaDataMock.expects.getTextEncoding()
                .returns(TextEncoding.PLAIN).any();

        assetResolverMock.expects.getContentsFromVariant(selectedVariantMock)
                .returns("Text").any();

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        FallbackComponentTextAssetReference reference =
                new FallbackComponentTextAssetReference(assetResolverMock,
                        concretePolicyMock, textAssetReferenceMock);

        String actual = reference.getText(TextEncoding.PLAIN);

        assertEquals("Expected", "Text", actual);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 07-Apr-04	3735/1	geoff	VBM:2004033102 Enhance Menu Support: Address some issues with asset references

 ===========================================================================
*/
