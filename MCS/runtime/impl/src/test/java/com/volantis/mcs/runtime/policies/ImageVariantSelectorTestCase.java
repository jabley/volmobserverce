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

package com.volantis.mcs.runtime.policies;

import com.volantis.mcs.devices.InternalDeviceMock;
import com.volantis.mcs.policies.PolicyFactory;
import com.volantis.mcs.policies.PolicyType;
import com.volantis.mcs.policies.VariablePolicyBuilder;
import com.volantis.mcs.policies.variants.Variant;
import com.volantis.mcs.policies.variants.VariantBuilder;
import com.volantis.mcs.policies.variants.VariantType;
import com.volantis.mcs.policies.variants.content.URLContentBuilder;
import com.volantis.mcs.policies.variants.image.ImageConversionMode;
import com.volantis.mcs.policies.variants.image.ImageEncoding;
import com.volantis.mcs.policies.variants.image.ImageMetaDataBuilder;
import com.volantis.mcs.policies.variants.metadata.EncodingCollectionMock;
import com.volantis.mcs.policies.variants.selection.TargetedSelectionBuilder;
import com.volantis.mcs.runtime.RuntimeProjectMock;
import com.volantis.synergetics.testtools.TestCaseAbstract;

public class ImageVariantSelectorTestCase
        extends TestCaseAbstract {

    private PolicyFactory factory;
    private SelectionContextMock selectionContextMock;
//    private PolicyAccessorMock policyAccessorMock;
    private InternalDeviceMock targetDeviceMock;
    private InternalDeviceMock pcDeviceMock;
    private InternalDeviceMock masterDeviceMock;
    private EncodingCollectionMock encodingCollectionMock;
    private RuntimeProjectMock actualProjectMock;
    private RuntimeProjectMock logicalProjectMock;

    protected void setUp() throws Exception {
        super.setUp();

        factory = PolicyFactory.getDefaultInstance();

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        selectionContextMock = new SelectionContextMock("selectionContextMock",
                expectations);

//        policyAccessorMock = new PolicyAccessorMock("policyAccessorMock",
//                expectations);

        targetDeviceMock = new InternalDeviceMock("targetDeviceMock",
                expectations);

        pcDeviceMock = new InternalDeviceMock("pcDeviceMock", expectations);

        masterDeviceMock = new InternalDeviceMock("masterDeviceMock",
                expectations);

        encodingCollectionMock = new EncodingCollectionMock(
                "encodingCollectionMock", expectations);

        actualProjectMock = new RuntimeProjectMock("actualProjectMock",
                expectations);

        logicalProjectMock = new RuntimeProjectMock("logicalProjectMock",
                expectations);

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        selectionContextMock.expects.getDevice().returns(targetDeviceMock)
                .any();

        targetDeviceMock.expects.getFallbackDevice().returns(pcDeviceMock)
                .any();
        targetDeviceMock.expects.getName().returns("Target").any();

        pcDeviceMock.expects.getFallbackDevice().returns(masterDeviceMock)
                .any();
        pcDeviceMock.expects.getName().returns("PC").any();

        masterDeviceMock.expects.getFallbackDevice().returns(null).any();
        masterDeviceMock.expects.getName().returns("Master").any();

    }

    /**
     * Test that an image targeted at a couple of devices works properly.
     */
    public void testDeviceTargeted()
        throws Exception {

        // =====================================================================
        //   Create Test Objects
        // =====================================================================

        VariablePolicyBuilder policyBuilder =
                factory.createVariablePolicyBuilder(PolicyType.IMAGE);

        VariantBuilder variantBuilder =
                factory.createVariantBuilder(VariantType.IMAGE);
        policyBuilder.addVariantBuilder(variantBuilder);

        // Selection.
        TargetedSelectionBuilder targeted =
                factory.createTargetedSelectionBuilder();
        targeted.addDevice("PC");
        targeted.addDevice("Master");

        variantBuilder.setSelectionBuilder(targeted);

        // Meta Data.
        ImageMetaDataBuilder imageBuilder =
                factory.createImageMetaDataBuilder();
        imageBuilder.setConversionMode(ImageConversionMode.NEVER_CONVERT);
        imageBuilder.setImageEncoding(ImageEncoding.GIF);
        imageBuilder.setWidth(10);
        imageBuilder.setHeight(50);
        imageBuilder.setPixelDepth(12);

        variantBuilder.setMetaDataBuilder(imageBuilder);

        // Content.
        URLContentBuilder content = factory.createURLContentBuilder();
        content.setURL("fred.gif");
        variantBuilder.setContentBuilder(content);

        Variant variant = variantBuilder.getVariant();

        ActivatedVariablePolicy activatedVariablePolicy =
                new ActivatedVariablePolicyImpl(
                        policyBuilder.getVariablePolicy(), actualProjectMock,
                        logicalProjectMock);

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        // When asked return the policyBuilder above.
//        policyAccessorMock.expects
//                .retrievePolicy(repositoryConnectionMock, reference)
//                .returns(activatedVariablePolicy);

        // The image encodings should be retrieved from the target device.
        targetDeviceMock.expects.getSupportedImageEncodings()
                .returns(encodingCollectionMock).any();

        // The target device supports GIF.
        encodingCollectionMock.expects.contains(ImageEncoding.GIF)
                .returns(true).any();

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        PolicyVariantSelector selector = new PolicyVariantSelector();
        SelectedVariant selected = selector.selectVariant(
                selectionContextMock, activatedVariablePolicy, null);

        assertSame("Selected", variant, selected.getVariant());
    }

    /**
     * Test that an image targeted at a couple of categories is selected if
     * the target device belongs in the same category.
     */
    public void testCategoryTargeted()
            throws Exception {

        // =====================================================================
        //   Create Test Objects
        // =====================================================================

        VariablePolicyBuilder policyBuilder = createVariablePolicyBuilder();

        Variant variant = createCategorizedVariant(policyBuilder);

        ActivatedVariablePolicy activatedVariablePolicy =
                new ActivatedVariablePolicyImpl(
                        policyBuilder.getVariablePolicy(), actualProjectMock,
                        logicalProjectMock);

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        // When asked return the policyBuilder above.
//        policyAccessorMock.expects.retrievePolicy(repositoryConnectionMock, reference)
//                .returns(activatedVariablePolicy);

        // The image encodings should be retrieved from the target device.
        targetDeviceMock.expects.getSupportedImageEncodings()
                .returns(encodingCollectionMock).any();

        // The target device supports GIF.
        encodingCollectionMock.expects.contains(ImageEncoding.GIF)
                .returns(true).any();

        targetDeviceMock.expects.getPolicyValue("category").returns("Ok");

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        PolicyVariantSelector selector = new PolicyVariantSelector();
        SelectedVariant selected = selector.selectVariant(selectionContextMock,
                activatedVariablePolicy, null);

        assertSame("Selected", variant, selected.getVariant());
    }

    /**
     * Test that an image targeted at a couple of categories is not selected if
     * the target device does not belong in the specified category.
     */
    public void testCategoryTargetedFails()
            throws Exception {

        // =====================================================================
        //   Create Test Objects
        // =====================================================================

        VariablePolicyBuilder policyBuilder = createVariablePolicyBuilder();

        createCategorizedVariant(policyBuilder);

        ActivatedVariablePolicy activatedVariablePolicy =
                new ActivatedVariablePolicyImpl(
                        policyBuilder.getVariablePolicy(), actualProjectMock,
                        logicalProjectMock);

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        // When asked return the policyBuilder above.
//        policyAccessorMock.expects.retrievePolicy(repositoryConnectionMock, reference)
//                .returns(activatedVariablePolicy);

        // The image encodings should be retrieved from the target device.
        targetDeviceMock.expects.getSupportedImageEncodings()
                .returns(encodingCollectionMock).any();

        // The target device supports GIF.
        encodingCollectionMock.expects.contains(ImageEncoding.GIF)
                .returns(true).any();

        targetDeviceMock.expects.getPolicyValue("category").returns("Bad");

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        PolicyVariantSelector selector = new PolicyVariantSelector();
        SelectedVariant selected = selector.selectVariant(selectionContextMock,
                activatedVariablePolicy, null);

        assertNull("No variant should have been selected as the category" +
                " does not match", selected);
    }

    private VariablePolicyBuilder createVariablePolicyBuilder() {
        VariablePolicyBuilder policy =
                factory.createVariablePolicyBuilder(PolicyType.IMAGE);
        policy.setCategorizationScheme("category");
        return policy;
    }

    private Variant createCategorizedVariant(VariablePolicyBuilder policyBuilder) {
        VariantBuilder variantBuilder =
                factory.createVariantBuilder(VariantType.IMAGE);
        policyBuilder.addVariantBuilder(variantBuilder);

        // Selection.
        TargetedSelectionBuilder targeted =
                factory.createTargetedSelectionBuilder();
        targeted.addCategory("Ok");
        targeted.addCategory("Good");

        variantBuilder.setSelectionBuilder(targeted);

        // Meta Data.
        ImageMetaDataBuilder imageBuilder =
                factory.createImageMetaDataBuilder();
        imageBuilder.setConversionMode(ImageConversionMode.NEVER_CONVERT);
        imageBuilder.setImageEncoding(ImageEncoding.GIF);
        imageBuilder.setWidth(10);
        imageBuilder.setHeight(50);
        imageBuilder.setPixelDepth(12);

        variantBuilder.setMetaDataBuilder(imageBuilder);

        // Content.
        URLContentBuilder content = factory.createURLContentBuilder();
        content.setURL("fred.gif");
        variantBuilder.setContentBuilder(content);

        return variantBuilder.getVariant();
    }
}
