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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.runtime;

import com.volantis.mcs.assets.ScriptAsset;
import com.volantis.mcs.context.MarinerPageContextMock;
import com.volantis.mcs.dom.DOMFactory;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.integration.PageURLType;
import com.volantis.mcs.policies.variants.VariantMock;
import com.volantis.mcs.policies.variants.VariantType;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.DOMOutputBufferFactory;
import com.volantis.mcs.protocols.DOMProtocolMock;
import com.volantis.mcs.protocols.DefaultProtocolSupportFactory;
import com.volantis.mcs.protocols.PageHead;
import com.volantis.mcs.protocols.ScriptAttributes;
import com.volantis.mcs.protocols.assets.implementation.AssetResolverMock;
import com.volantis.mcs.protocols.assets.implementation.DefaultComponentScriptAssetReference;
import com.volantis.mcs.runtime.policies.ActivatedVariablePolicyMock;
import com.volantis.mcs.runtime.policies.RuntimePolicyReferenceMock;
import com.volantis.mcs.runtime.policies.SelectedVariantMock;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.testtools.mock.method.MethodAction;
import com.volantis.testtools.mock.method.MethodActionEvent;

import java.util.LinkedList;
import java.util.List;

/**
 * Tests for the ScriptLibraryManager class
 */
public class ScriptLibraryManagerTestCase extends TestCaseAbstract {

    public void testDuplicates() {
        final AssetResolverMock assetResolverMock =
            new AssetResolverMock("AssetResolverMock", expectations);

        final DOMProtocolMock protocolMock =
            new DOMProtocolMock("DOMProtocolMock", expectations,
                new DefaultProtocolSupportFactory(), null);
        protocolMock.expects.pushHeadBuffer().any();
        final List scriptAttributesList = new LinkedList();
        protocolMock.fuzzy.writeOpenScript(mockFactory.expectsAny()).does(
            new MethodAction() {
                public Object perform(MethodActionEvent event) {
                    scriptAttributesList.add(event.getArguments()[0]);
                    return null;
                }
            }).any();
        protocolMock.fuzzy.writeCloseScript(mockFactory.expectsAny()).returns().any();
        protocolMock.expects.popHeadBuffer().any();
        protocolMock.expects.getDOMFactory().returns(
            DOMFactory.getDefaultInstance()).any();
        final PageHead pageHead = new PageHead();
        pageHead.setOutputBufferFactory(
            new DOMOutputBufferFactory(DOMFactory.getDefaultInstance()));
        protocolMock.expects.getPageHead().returns(pageHead).any();
        protocolMock.fuzzy.createScriptElement(
            mockFactory.expectsInstanceOf(ScriptAttributes.class)).does(
                new MethodAction(){
                    public Object perform(final MethodActionEvent event)
                            throws Throwable {
                        final ScriptAttributes attributes =
                            (ScriptAttributes) event.getArguments()[0];
                        final Element scriptElement =
                            protocolMock.getDOMFactory().createElement("script");
                        scriptElement.setAttribute("charset",
                            attributes.getCharSet());
                        scriptElement.setAttribute("language",
                            attributes.getLanguage());
                        scriptElement.setAttribute("src",
                            attributes.getScriptReference().getURL());
                        return scriptElement;
                    }
                }).any();

        final MarinerPageContextMock marinerPageContextMock =
            new MarinerPageContextMock("MarinerPageContextMock", expectations);
        marinerPageContextMock.expects.getAssetResolver().returns(
            assetResolverMock).any();
        marinerPageContextMock.expects.getProtocol().returns(protocolMock).any();

        final RuntimePolicyReferenceMock runtimePolicyReferenceMock1 =
            new RuntimePolicyReferenceMock("RuntimePolicyReferenceMock1",
                expectations);
        runtimePolicyReferenceMock1.expects.getName().returns("name1").any();
        final RuntimePolicyReferenceMock runtimePolicyReferenceMock2 =
            new RuntimePolicyReferenceMock("RuntimePolicyReferenceMock2",
                expectations);
        runtimePolicyReferenceMock2.expects.getName().returns("name2").any();
        final RuntimePolicyReferenceMock runtimePolicyReferenceMock3 =
            new RuntimePolicyReferenceMock("RuntimePolicyReferenceMock3",
                expectations);
        runtimePolicyReferenceMock3.expects.getName().returns("name3").any();
        final RuntimePolicyReferenceMock runtimePolicyReferenceMock4 =
            new RuntimePolicyReferenceMock("RuntimePolicyReferenceMock4",
                expectations);
        runtimePolicyReferenceMock4.expects.getName().returns("name4").any();

        final ScriptLibraryManager slm = new ScriptLibraryManager(protocolMock);
        final VariantMock variantMock =
            new VariantMock("VariantMock", expectations);
        variantMock.expects.getVariantType().returns(VariantType.SCRIPT).any();

        final ActivatedVariablePolicyMock policyMock1 =
            new ActivatedVariablePolicyMock("ActivatedVariablePolicyMock1",
                expectations);
        policyMock1.expects.getName().returns("name1").any();
        final SelectedVariantMock selectedVariantMock1 =
            new SelectedVariantMock("SelectedVariantMock1", expectations);
        assetResolverMock.expects.selectBestVariant(
            runtimePolicyReferenceMock1, null).returns(selectedVariantMock1).any();
        selectedVariantMock1.expects.getVariant().returns(variantMock).any();
        selectedVariantMock1.expects.getPolicy().returns(policyMock1).any();
        selectedVariantMock1.expects.getOldObject().returns(
            new ScriptAsset("name1", "deviceName1", "javascript1", "mimeType1",
                "charSet1", ScriptAsset.URL, "assetGroupName1", "value1")).any();
        final DefaultComponentScriptAssetReference scriptAssetReference1 =
            new DefaultComponentScriptAssetReference(
                runtimePolicyReferenceMock1, assetResolverMock);
        assertTrue(slm.addScript(scriptAssetReference1));

        final ActivatedVariablePolicyMock policyMock2 =
            new ActivatedVariablePolicyMock("ActivatedVariablePolicyMock2",
                expectations);
        policyMock2.expects.getName().returns("name2").any();
        final SelectedVariantMock selectedVariantMock2 =
            new SelectedVariantMock("SelectedVariantMock2", expectations);
        selectedVariantMock2.expects.getVariant().returns(variantMock).any();
        selectedVariantMock2.expects.getPolicy().returns(policyMock2).any();
        selectedVariantMock2.expects.getOldObject().returns(
            new ScriptAsset("name2", "deviceName2", "javascript2", "mimeType2",
                "charSet2", ScriptAsset.URL, "assetGroupName2", "value2")).any();
        assetResolverMock.expects.selectBestVariant(
            runtimePolicyReferenceMock2, null).returns(selectedVariantMock2).any();
        final DefaultComponentScriptAssetReference scriptAssetReference2 =
            new DefaultComponentScriptAssetReference(
                runtimePolicyReferenceMock2, assetResolverMock);
        assertTrue(slm.addScript(scriptAssetReference2));

        final SelectedVariantMock selectedVariantMock1b =
            new SelectedVariantMock("SelectedVariantMock1b", expectations);
        selectedVariantMock1b.expects.getVariant().returns(variantMock).any();
        selectedVariantMock1b.expects.getPolicy().returns(policyMock1).any();
        selectedVariantMock1b.expects.getOldObject().returns(new ScriptAsset(
            "name1b", "deviceName1b", "javascript1b", "mimeType1b", "charSet1b",
            ScriptAsset.URL, "assetGroupName1b", "value1b")).any();
        final DefaultComponentScriptAssetReference scriptAssetReference1b =
            new DefaultComponentScriptAssetReference(
                runtimePolicyReferenceMock1, assetResolverMock);
        assertFalse(slm.addScript(scriptAssetReference1b));

        final ActivatedVariablePolicyMock policyMock3 =
            new ActivatedVariablePolicyMock("ActivatedVariablePolicyMock3",
                expectations);
        policyMock3.expects.getName().returns("name3").any();
        final SelectedVariantMock selectedVariantMock3 =
            new SelectedVariantMock("SelectedVariantMock3", expectations);
        selectedVariantMock3.expects.getVariant().returns(variantMock).any();
        selectedVariantMock3.expects.getPolicy().returns(policyMock3).any();
        selectedVariantMock3.expects.getOldObject().returns(
            new ScriptAsset("name3", "deviceName3", "javascript3", "mimeType3",
                "charSet3", ScriptAsset.URL, "assetGroupName3", "value3")).any();
        assetResolverMock.expects.selectBestVariant(
            runtimePolicyReferenceMock3, null).returns(selectedVariantMock3).any();
        final DefaultComponentScriptAssetReference scriptAssetReference3 =
            new DefaultComponentScriptAssetReference(
                runtimePolicyReferenceMock3, assetResolverMock);
        assertTrue(slm.addScript(scriptAssetReference3));

        assetResolverMock.expects.computeURLAsString(selectedVariantMock1).
            returns("source1").any();
        assetResolverMock.expects.computeURLAsString(selectedVariantMock2).
            returns("source2").any();
        assetResolverMock.expects.computeURLAsString(selectedVariantMock3).
            returns("source3").any();

        assetResolverMock.expects.rewriteURLWithPageURLRewriter("source1", PageURLType.SCRIPT)
            .returns("source1").any();
        assetResolverMock.expects.rewriteURLWithPageURLRewriter("source2", PageURLType.SCRIPT)
            .returns("source2").any();
        assetResolverMock.expects.rewriteURLWithPageURLRewriter("source3", PageURLType.SCRIPT)
            .returns("source3").any();
        
        slm.writeScriptElements();

        assertEquals(3, scriptAttributesList.size());
        final ScriptAttributes scriptAttributes1 =
            (ScriptAttributes) scriptAttributesList.get(0);
        assertEquals("charSet1", scriptAttributes1.getCharSet());
        assertEquals("javascript1", scriptAttributes1.getLanguage());
        assertEquals("source1", scriptAttributes1.getScriptReference().getURL());
        final ScriptAttributes scriptAttributes2 =
            (ScriptAttributes) scriptAttributesList.get(1);
        assertEquals("charSet2", scriptAttributes2.getCharSet());
        assertEquals("javascript2", scriptAttributes2.getLanguage());
        assertEquals("source2", scriptAttributes2.getScriptReference().getURL());
        final ScriptAttributes scriptAttributes3 =
            (ScriptAttributes) scriptAttributesList.get(2);
        assertEquals("charSet3", scriptAttributes3.getCharSet());
        assertEquals("javascript3", scriptAttributes3.getLanguage());
        assertEquals("source3", scriptAttributes3.getScriptReference().getURL());

        final Element headRoot =
            ((DOMOutputBuffer) pageHead.getHead()).getRoot();
        assertEquals("DELETE_ME", ((Element) headRoot.getHead()).getName());

        final ActivatedVariablePolicyMock policyMock4 =
            new ActivatedVariablePolicyMock("ActivatedVariablePolicyMock4",
                expectations);
        policyMock4.expects.getName().returns("name4").any();
        final SelectedVariantMock selectedVariantMock4 =
            new SelectedVariantMock("SelectedVariantMock4", expectations);
        selectedVariantMock4.expects.getVariant().returns(variantMock).any();
        selectedVariantMock4.expects.getPolicy().returns(policyMock4).any();
        selectedVariantMock4.expects.getOldObject().returns(
            new ScriptAsset("name4", "deviceName4", "javascript4", "mimeType4",
                "charSet4", ScriptAsset.URL, "assetGroupName4", "value4")).any();
        assetResolverMock.expects.selectBestVariant(
            runtimePolicyReferenceMock4, null).returns(selectedVariantMock4).any();
        assetResolverMock.expects.computeURLAsString(selectedVariantMock4).
            returns("source4").any();
        assetResolverMock.expects.rewriteURLWithPageURLRewriter("source4", PageURLType.SCRIPT).
            returns("source4").any();
        final DefaultComponentScriptAssetReference scriptAssetReference4 =
            new DefaultComponentScriptAssetReference(
                runtimePolicyReferenceMock4, assetResolverMock);
        assertTrue(slm.addScript(scriptAssetReference4));
        Element scriptElement = ((Element) headRoot.getHead());
        assertEquals("script", scriptElement.getName());
        assertEquals("charSet4", scriptElement.getAttributeValue("charset"));
        assertEquals("javascript4", scriptElement.getAttributeValue("language"));
        assertEquals("source4", scriptElement.getAttributeValue("src"));
        assertEquals("DELETE_ME", ((Element) scriptElement.getNext()).getName());

        slm.close();
        scriptElement = ((Element) headRoot.getHead());
        assertEquals("script", scriptElement.getName());
        assertEquals("charSet4", scriptElement.getAttributeValue("charset"));
        assertEquals("javascript4", scriptElement.getAttributeValue("language"));
        assertEquals("source4", scriptElement.getAttributeValue("src"));
        assertNull(scriptElement.getNext());
    }
}
