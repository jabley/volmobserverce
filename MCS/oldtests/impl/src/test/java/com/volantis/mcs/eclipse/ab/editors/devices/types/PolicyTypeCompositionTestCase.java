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
package com.volantis.mcs.eclipse.ab.editors.devices.types;

import com.volantis.devrep.repository.api.devices.DeviceRepositorySchemaConstants;
import com.volantis.mcs.eclipse.ab.editors.devices.BooleanPolicyValueModifier;
import com.volantis.mcs.eclipse.ab.editors.devices.ListPolicyValueModifier;
import com.volantis.mcs.eclipse.ab.editors.devices.PolicyValueModifier;
import com.volantis.mcs.eclipse.ab.editors.devices.StructurePolicyValueModifier;
import com.volantis.mcs.eclipse.ab.editors.devices.DevicesMessages;
import com.volantis.mcs.eclipse.core.DeviceRepositoryAccessorManager;
import com.volantis.synergetics.cornerstone.utilities.xml.jaxp.TestTransformerMetaFactory;
import com.volantis.devrep.repository.impl.testtools.device.TestDeviceRepositoryCreator;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.synergetics.testtools.io.TemporaryFileManager;
import com.volantis.synergetics.testtools.io.TemporaryFileExecutor;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.jdom.Element;
import org.jdom.input.DefaultJDOMFactory;
import org.jdom.input.JDOMFactory;

/**
 * Test case for PolicyTypeComposition.
 */
public class PolicyTypeCompositionTestCase extends TestCaseAbstract {

    /**
     * The shell to use for the PolicyValueModifiers created by
     * {@link PolicyTypeComposition#createPolicyValueModifier}.
     */
    private static final Shell SHELL = new Shell(Display.getDefault());

    /**
     * Test that the expected number of PolicyTypeCompositions returned by
     * PolicyTypeComposition.getPolicyTypeCompositions()
     */
    public void testGetPolicyTypeCompostions() {
        assertEquals("There should be 4 PolicyTypeCompositions available.",
                4, PolicyTypeComposition.getPolicyTypeCompositions().length);
    }

    /**
     * Generic test for PolicyTypeCompositions that can contain multiple
     * single valued policy types calling addTypeElement.
     * @param composition the PolicyTypeComposition to test
     * @param policyType the single valued policy type.
     * @param expectedElementName the name of the element representing the
     * single valued PolicyType to be added.
     */
    private void addMultiTypeElementTest(PolicyTypeComposition composition,
                                         PolicyType policyType,
                                         String expectedElementName,
                                         String expectedSubElementName) {

        JDOMFactory factory = new DefaultJDOMFactory();
        Element policy = factory.element("policy");
        composition.addTypeElement(policy, policyType, factory);
        assertEquals("There should be a single child element of policy.",
                1, policy.getChildren().size());
        Element type = (Element) policy.getChildren().get(0);
        assertEquals("Expected a " +
                DeviceRepositorySchemaConstants.
                POLICY_DEFINITION_TYPE_ELEMENT_NAME +
                " element.",
                DeviceRepositorySchemaConstants.
                POLICY_DEFINITION_TYPE_ELEMENT_NAME,
                type.getName());
        assertEquals("There should be a single child element of type.",
                1, type.getChildren().size());
        Element policyTypeElement = (Element) type.getChildren().get(0);
        assertEquals("Expected a " + expectedElementName + " element.",
                expectedElementName, policyTypeElement.getName());
        assertEquals("There should be a single child element of the " +
                expectedElementName + "element.",
                1, policyTypeElement.getChildren().size());
        Element subPolicyTypeElement = (Element) policyTypeElement.
                getChildren().get(0);
        assertEquals("Expected a " + expectedSubElementName + " element.",
                expectedSubElementName, subPolicyTypeElement.getName());
        assertEquals("There should be a no children of subPolicyTypeElement.",
                0, subPolicyTypeElement.getChildren().size());
    }

    /**
     * Generic test for a SINGLE PolicyTypeComposition calling addTypeElement.
     * @param policyType the PolicyType to add.
     * @param expectedElementName the name of the element representing the
     * single valued PolicyType to be added.
     */
    private void addSingleTypeElementTest(PolicyType policyType,
                                          String expectedElementName) {

        JDOMFactory factory = new DefaultJDOMFactory();
        Element policy = factory.element("policy");
        PolicyTypeComposition.SINGLE.addTypeElement(policy, policyType,
                factory);
        assertEquals("There should be a single child element of policy.",
                1, policy.getChildren().size());
        Element type = (Element) policy.getChildren().get(0);
        assertEquals("Expected a " +
                DeviceRepositorySchemaConstants.
                POLICY_DEFINITION_TYPE_ELEMENT_NAME +
                " element.",
                DeviceRepositorySchemaConstants.
                POLICY_DEFINITION_TYPE_ELEMENT_NAME,
                type.getName());
        assertEquals("There should be a single child element of type.",
                1, type.getChildren().size());
        Element policyTypeElement = (Element) type.getChildren().get(0);
        assertEquals("Expected a " + expectedElementName + " element.",
                expectedElementName, policyTypeElement.getName());
        assertEquals("There should be a no children of policyTypeElement.",
                0, policyTypeElement.getChildren().size());
    }


    /**
     * Test that addTypeElement works with a ORDERED_SET PolicyTypeComposition
     * PolicyType.BOOLEAN.
     */
    public void testAddTypeElementOrderedSetBoolean() {
        addMultiTypeElementTest(PolicyTypeComposition.ORDERED_SET,
                PolicyType.BOOLEAN,
                DeviceRepositorySchemaConstants.
                POLICY_DEFINITION_ORDEREDSET_ELEMENT_NAME,
                DeviceRepositorySchemaConstants.
                POLICY_DEFINITION_BOOLEAN_ELEMENT_NAME);
    }

    /**
     * Test that addTypeElement works with a UNORDERED_SET PolicyTypeComposition
     * PolicyType.INT.
     */
    public void testAddTypeElementUnOrderedSetInt() {
        addMultiTypeElementTest(PolicyTypeComposition.UNORDERED_SET,
                PolicyType.INT,
                DeviceRepositorySchemaConstants.
                POLICY_DEFINITION_UNORDEREDSET_ELEMENT_NAME,
                DeviceRepositorySchemaConstants.
                POLICY_DEFINITION_INT_ELEMENT_NAME);
    }

    /**
     * The that addTypeElement works with a STRUCTURE PolicyTypeComposition.
     */
    public void testAddTypeElementStructure() {

        JDOMFactory factory = new DefaultJDOMFactory();
        Element policy = factory.element("policy");
        PolicyTypeComposition.STRUCTURE.addTypeElement(policy,
                PolicyType.EMULATE_EMPHASIS_TAG, factory);
        assertEquals("There should be a single child element of policy.",
                1, policy.getChildren().size());
        Element type = (Element) policy.getChildren().get(0);

        String ref = type.getAttributeValue(DeviceRepositorySchemaConstants.
                POLICY_DEFINITION_REF_ATTRIBUTE_NAME);
        assertEquals("Expected the ref attribute to be " +
                DeviceRepositorySchemaConstants.
                POLICY_DEFINITION_EMULATE_EMPHASIS_TAG_NAME,
                DeviceRepositorySchemaConstants.
                POLICY_DEFINITION_EMULATE_EMPHASIS_TAG_NAME,
                ref);
        assertEquals("Expected a " +
                DeviceRepositorySchemaConstants.
                POLICY_DEFINITION_TYPE_ELEMENT_NAME +
                " element.",
                DeviceRepositorySchemaConstants.
                POLICY_DEFINITION_TYPE_ELEMENT_NAME,
                type.getName());
        assertEquals("There should be a no children of type.",
                0, type.getChildren().size());
    }

    /**
     * TestPolicyTypeComposition.ORDERED_SET#canHandleType(Element).
     */
    public void testOrderedSetCanHandleTypeElement() throws Exception {
        JDOMFactory factory = new DefaultJDOMFactory();
        Element type = factory.element(DeviceRepositorySchemaConstants.
                POLICY_DEFINITION_TYPE_ELEMENT_NAME);
        Element orderedSet = factory.element(
                DeviceRepositorySchemaConstants.
                POLICY_DEFINITION_ORDEREDSET_ELEMENT_NAME);
        type.addContent(orderedSet);

        assertTrue("Should be able to handle an " +
                DeviceRepositorySchemaConstants.
                POLICY_DEFINITION_ORDEREDSET_ELEMENT_NAME +
                " element",
                PolicyTypeComposition.ORDERED_SET.canHandleType(type));
    }

    /**
     * TestPolicyTypeComposition.SINGLE#canHandleType(Element).
     */
    public void testSingleCanHandleTypeElement() throws Exception {
        PolicyType[] policyTypes = new PolicyType[]{
            PolicyType.BOOLEAN, PolicyType.INT, PolicyType.RANGE,
            PolicyType.SELECTION, PolicyType.TEXT
        };
        canHandleString(PolicyTypeComposition.SINGLE, policyTypes);
    }

    /**
     * TestPolicyTypeComposition.UNORDERED_SET#canHandleType(Element).
     */
    public void testUnorderedSetCanHandleTypeElement() throws Exception {
        JDOMFactory factory = new DefaultJDOMFactory();
        Element type = factory.element(DeviceRepositorySchemaConstants.
                POLICY_DEFINITION_TYPE_ELEMENT_NAME);
        Element orderedSet = factory.element(
                DeviceRepositorySchemaConstants.
                POLICY_DEFINITION_UNORDEREDSET_ELEMENT_NAME);
        type.addContent(orderedSet);

        assertTrue("Should be able to handle an " +
                DeviceRepositorySchemaConstants.
                POLICY_DEFINITION_UNORDEREDSET_ELEMENT_NAME +
                " element",
                PolicyTypeComposition.UNORDERED_SET.canHandleType(type));
    }

    /**
     * Helper method that checks the given composition can handle the specified
     * policy types.
     * @param ptc the composition under test
     * @param policyTypes the policy types that the given composition should
     * be able to handle
     */
    private void canHandleString(PolicyTypeComposition ptc,
                                 PolicyType[] policyTypes) {
        for (int i = 0; i < policyTypes.length; i++) {
            PolicyType pt = policyTypes[i];
            assertTrue("Should be able to handle " + pt.getName() + " type",
                    ptc.canHandleType(createTypeElement(policyTypes[i])));
        }
    }

    /**
     * Helper method that creates the type element for the given policy type.
     * @param policyType the policy type
     * @return the type element
     */
    private Element createTypeElement(PolicyType policyType) {
        JDOMFactory factory = new DefaultJDOMFactory();
        Element type = factory.element(DeviceRepositorySchemaConstants.
                POLICY_DEFINITION_TYPE_ELEMENT_NAME);
        // this is a bit clumsy
        if (policyType == PolicyType.EMULATE_EMPHASIS_TAG) {
            type.setAttribute(DeviceRepositorySchemaConstants.
                                POLICY_DEFINITION_TYPE_NAME_ATTRIBUTE_NAME,
                              PolicyType.EMULATE_EMPHASIS_TAG.getName());
        }
        Element element = factory.element(
                policyType.getName());
        type.addContent(element);
        return type;
    }

    /**
     * Test canHandleType(Element) with a StuctureComposition.
     */
    public void testCanHandleTypeElementStructure() {
        JDOMFactory factory = new DefaultJDOMFactory();
        Element type = factory.element(DeviceRepositorySchemaConstants.
                POLICY_DEFINITION_TYPE_ELEMENT_NAME);
        Element range = factory.element("range");
        type.addContent(range);
        assertFalse("Should not be able handle type with no name attribute",
                PolicyTypeComposition.STRUCTURE.canHandleType(type));

        range.detach();

        type.setAttribute(DeviceRepositorySchemaConstants.
                POLICY_DEFINITION_TYPE_NAME_ATTRIBUTE_NAME,
                DeviceRepositorySchemaConstants.
                POLICY_DEFINITION_EMULATE_EMPHASIS_TAG_NAME);

        assertTrue("Should be able to handle an emulate emphasis tag",
                PolicyTypeComposition.STRUCTURE.canHandleType(type));
    }


    /**
     * TestPolicyTypeComposition.ORDERED_SET#canHandleType(String).
     */
    public void testOrderedSetCanHandleTypeString() throws Exception {
        String typeName = DeviceRepositorySchemaConstants.
                POLICY_DEFINITION_ORDEREDSET_ELEMENT_NAME;
        assertTrue("Should be able to handle " +
                typeName,
                PolicyTypeComposition.ORDERED_SET.canHandleType(typeName));
    }

    /**
     * TestPolicyTypeComposition.SINGLE#canHandleType(String).
     */
    public void testSingleCanHandleTypeString() throws Exception {
        String[] policyTypeNames = new String[]{
            PolicyType.BOOLEAN.getName(),
            PolicyType.INT.getName(),
            PolicyType.RANGE.getName(),
            PolicyType.SELECTION.getName(),
            PolicyType.TEXT.getName()
        };
        for (int i = 0; i < policyTypeNames.length; i++) {
            assertTrue(PolicyTypeComposition.SINGLE.
                    canHandleType(policyTypeNames[i]));
        }
    }

    /**
     * TestPolicyTypeComposition.UNORDERED_SET#canHandleType(String).
     */
    public void testUnorderedSetCanHandleTypeString() throws Exception {
        String typeName = DeviceRepositorySchemaConstants.
                POLICY_DEFINITION_UNORDEREDSET_ELEMENT_NAME;
        assertTrue("Should be able to handle " +
                typeName,
                PolicyTypeComposition.UNORDERED_SET.canHandleType(typeName));
    }

    /**
     * Test that addTypeElement works with a SINGLE PolicyTypeComposition
     * PolicyType.BOOLEAN.
     */
    public void testAddTypeElementSingleBoolean() {
        addSingleTypeElementTest(PolicyType.BOOLEAN,
                DeviceRepositorySchemaConstants.
                POLICY_DEFINITION_BOOLEAN_ELEMENT_NAME);
    }

    /**
     * Test that addTypeElement works with a SINGLE PolicyTypeComposition
     * PolicyType.INT.
     */
    public void testAddTypeElementSingleInt() {
        addSingleTypeElementTest(PolicyType.INT,
                DeviceRepositorySchemaConstants.
                POLICY_DEFINITION_INT_ELEMENT_NAME);
    }

    /**
     * Test that addTypeElement works with a SINGLE PolicyTypeComposition
     * PolicyType.RANGE_INCLUSIVE_INCLUSIVE.
     */
    public void testAddTypeElementSingleRange() {
        addSingleTypeElementTest(PolicyType.RANGE,
                DeviceRepositorySchemaConstants.
                POLICY_DEFINITION_RANGE_ELEMENT_NAME);
    }

    /**
     * Test that addTypeElement works with a SINGLE PolicyTypeComposition
     * PolicyType.SELECTION.
     */
    public void testAddTypeElementSingleSelection() {
        addSingleTypeElementTest(PolicyType.SELECTION,
                DeviceRepositorySchemaConstants.
                POLICY_DEFINITION_SELECTION_ELEMENT_NAME);
    }

    /**
     * Test that addTypeElement works with a SINGLE PolicyTypeComposition
     * PolicyType.TEXT.
     */
    public void testAddTypeElementSingleText() {
        addSingleTypeElementTest(PolicyType.TEXT,
                DeviceRepositorySchemaConstants.
                POLICY_DEFINITION_TEXT_ELEMENT_NAME);
    }

    /**
     * Tests PolicyTypeComposition#createPolicyValueModifier for
     * PolicyTypeComposition.ORDERED_SET.
     * @throws Exception
     */
    public void testCreatePolicyValueModifierOrderedSet() throws Exception {
        createPolicyValueModifier(PolicyTypeComposition.ORDERED_SET,
                "UAProf.CcppAccept-Language", ListPolicyValueModifier.class);
    }

    /**
     * Tests PolicyTypeComposition#createPolicyValueModifier for
     * PolicyTypeComposition.UNORDERED_SET.
     * @throws Exception
     */
    public void testCreatePolicyValueModifierUnorderedSet() throws Exception {
        createPolicyValueModifier(PolicyTypeComposition.UNORDERED_SET,
                "UAProf.DownloadableBrowserApps",
                ListPolicyValueModifier.class);
    }

    /**
     * Tests PolicyTypeComposition#createPolicyValueModifier for
     * PolicyTypeComposition.STRUCTURE.
     * @throws Exception
     */
    public void testCreatePolicyValueModifierStructure() throws Exception {
        createPolicyValueModifier(PolicyTypeComposition.STRUCTURE,
                "protocol.wml.emulate.bigTag",
                StructurePolicyValueModifier.class);
    }

    /**
     * Tests PolicyTypeComposition#createPolicyValueModifier for
     * PolicyTypeComposition.SINGLE.
     * @throws Exception
     */
    public void testCreatePolicyValueModifierSingle() throws Exception {
        createPolicyValueModifier(PolicyTypeComposition.SINGLE,
                "beep", BooleanPolicyValueModifier.class);
    }

    /**
     *
     * @param composition
     * @param policyName
     * @param modifierClass
     * @throws Exception
     */
    private void createPolicyValueModifier(
           final PolicyTypeComposition composition, final String policyName,
           final Class modifierClass) throws Exception {

        TemporaryFileManager tempFileMgr = new TemporaryFileManager(
                new TestDeviceRepositoryCreator());
        tempFileMgr.executeWith(new TemporaryFileExecutor() {
            public void execute(File repository) throws Exception {

                DeviceRepositoryAccessorManager deviceRAM =
                        new DeviceRepositoryAccessorManager(
                                repository.getPath(),
                                new TestTransformerMetaFactory(),
                                new DefaultJDOMFactory(), false);

                PolicyValueModifier pvMod = composition.
                        createPolicyValueModifier(SHELL, SWT.NONE,
                                policyName, deviceRAM);

                assertNotNull(pvMod);
                assertSame(modifierClass, pvMod.getClass());
            }
        });
    }

    /**
     * Tests PolicyTypeComposition#getComposition(Element) for
     * PolicyType.BOOLEAN elements.
     * @throws Exception
     */
    public void testGetCompositionElementBoolean() throws Exception {
        checkCompositionElement(PolicyType.BOOLEAN,
                PolicyTypeComposition.SINGLE);
    }

    /**
     * Tests PolicyTypeComposition#getComposition(Element) for
     * PolicyType.EMULATE_EMPHASIS_TAG elements.
     * @throws Exception
     */
    public void testGetCompositionElementEmulateEmphasisTag() throws Exception {
        checkCompositionElement(PolicyType.EMULATE_EMPHASIS_TAG,
                PolicyTypeComposition.STRUCTURE);
    }

    /**
     * Tests PolicyTypeComposition#getComposition(Element) for
     * PolicyType.INT elements.
     * @throws Exception
     */
    public void testGetCompositionElementInt() throws Exception {
        checkCompositionElement(PolicyType.INT,
                PolicyTypeComposition.SINGLE);
    }

    /**
     * Tests PolicyTypeComposition#getComposition(Element) for
     * PolicyType.RANGE_INCLUSIVE_INCLUSIVE elements.
     * @throws Exception
     */
    public void testGetCompositionElementRange() throws Exception {
        checkCompositionElement(PolicyType.RANGE,
                PolicyTypeComposition.SINGLE);
    }

    /**
     * Tests PolicyTypeComposition#getComposition(Element) for
     * PolicyType.SELECTION elements.
     * @throws Exception
     */
    public void testGetCompositionElementSelection() throws Exception {
        checkCompositionElement(PolicyType.SELECTION,
                PolicyTypeComposition.SINGLE);
    }

    /**
     * Tests PolicyTypeComposition#getComposition(Element) for
     * PolicyType.TEXT elements.
     * @throws Exception
     */
    public void testGetCompositionElementText() throws Exception {
        checkCompositionElement(PolicyType.TEXT,
                PolicyTypeComposition.SINGLE);
    }

    /**
     * Helper method to create an element of the specified policy type and
     * check that the returned composition cfor the element is correct.
     * @param policyType
     * @param composition
     */
    private void checkCompositionElement(PolicyType policyType,
                                         PolicyTypeComposition composition) {
        Element typeElement = createTypeElement(policyType);
        PolicyTypeComposition ptc =
                PolicyTypeComposition.getComposition(typeElement);
        assertNotNull(ptc);
        assertSame(ptc, composition);
    }

    /**
     * Tests PolicyTypeComposition#getComposition(String) with the name of
     * PolicyType.BOOLEAN.
     * @throws Exception
     */
    public void testGetCompositionStringBoolean() throws Exception {
        checkCompositionString(PolicyType.BOOLEAN,
                PolicyTypeComposition.SINGLE);
    }

    /**
     * Tests PolicyTypeComposition#getComposition(String) with the name of
     * PolicyType.EMULATE_EMPHASIS_TAG.
     * @throws Exception
     */
    public void testGetCompositionStringEmulateEmphasisTag() throws Exception {
        checkCompositionString(PolicyType.EMULATE_EMPHASIS_TAG,
                PolicyTypeComposition.STRUCTURE);
    }

    /**
     * Tests PolicyTypeComposition#getComposition(String) with the name of
     * PolicyType.INT.
     * @throws Exception
     */
    public void testGetCompositionStringInt() throws Exception {
        checkCompositionString(PolicyType.INT,
                PolicyTypeComposition.SINGLE);
    }

    /**
     * Tests PolicyTypeComposition#getComposition(String) with the name of
     * PolicyType.RANGE_INCLUSIVE_INCLUSIVE.
     * @throws Exception
     */
    public void testGetCompositionStringRange() throws Exception {
        checkCompositionString(PolicyType.RANGE,
                PolicyTypeComposition.SINGLE);
    }

    /**
     * Tests PolicyTypeComposition#getComposition(String) with the name of
     * PolicyType.SELECTION.
     * @throws Exception
     */
    public void testGetCompositionStringSelection() throws Exception {
        checkCompositionString(PolicyType.SELECTION,
                PolicyTypeComposition.SINGLE);
    }

    /**
     * Tests PolicyTypeComposition#getComposition(String) with the name of
     * PolicyType.TEXT.
     * @throws Exception
     */
    public void testGetCompositionStringText() throws Exception {
        checkCompositionString(PolicyType.TEXT,
                PolicyTypeComposition.SINGLE);
    }

    /**
     * A helper method which checks the retrieved composition for a given
     * policy type is the expected one.
     * @param policyType the policy type
     * @param composition the composition
     */
    private void checkCompositionString(PolicyType policyType,
                                        PolicyTypeComposition composition) {
        PolicyTypeComposition ptc =
                PolicyTypeComposition.getComposition(policyType.getName());
        assertNotNull(ptc);
        assertSame(ptc, composition);
    }

    /**
     * Tests PolicyTypeComposition#getNamedComposition for
     * PolicyTypeComposition.ORDERED_SET.
     * @throws Exception
     */
    public void testGetNamedCompositionOrderedSet() throws Exception {
        checkNamedComposition(PolicyTypeComposition.ORDERED_SET);
    }

    /**
     * Tests PolicyTypeComposition#getNamedComposition for
     * PolicyTypeComposition.SINGLE.
     * @throws Exception
     */
    public void testGetNamedCompositionSingle() throws Exception {
        checkNamedComposition(PolicyTypeComposition.SINGLE);
    }

    /**
     * Tests PolicyTypeComposition#getNamedComposition for
     * PolicyTypeComposition.STRUCTURE.
     * @throws Exception
     */
    public void testGetNamedCompositionStructure() throws Exception {
        checkNamedComposition(PolicyTypeComposition.STRUCTURE);
    }

    /**
     * Tests PolicyTypeComposition#getNamedComposition for
     * PolicyTypeComposition.UNORDERED_SET.
     * @throws Exception
     */
    public void testGetNamedCompositionUnorderedSet() throws Exception {
        checkNamedComposition(PolicyTypeComposition.UNORDERED_SET);
    }

    /**
     * Helper method that checks the returned named composition is correct.
     * @param ptc
     */
    private void checkNamedComposition(PolicyTypeComposition ptc) {
        PolicyTypeComposition ptcRetrieved =
                PolicyTypeComposition.getNamedComposition(
                        ptc.name);
        assertNotNull(ptcRetrieved);
        assertSame(ptcRetrieved.getClass(), ptc.getClass());
    }


    /**
     * Tests PolicyTypeComposition#getSupportedPolicyTypes for
     * PolicyTypeCompositon.ORDERED_SET.
     * @throws Exception
     */
    public void testGetSupportedPolicyTypesOrderedSet() throws Exception {
        PolicyTypeComposition ptc = PolicyTypeComposition.ORDERED_SET;
        List policyTypes = Arrays.asList(ptc.getSupportedPolicyTypes());
        assertNotNull(policyTypes);
        assertEquals("There should be 4 policy types", 4, policyTypes.size());
        assertTrue(policyTypes.contains(PolicyType.INT));
        assertTrue(policyTypes.contains(PolicyType.RANGE));
        assertTrue(policyTypes.contains(PolicyType.SELECTION));
        assertTrue(policyTypes.contains(PolicyType.TEXT));
    }

    /**
     * Tests PolicyTypeComposition#getSupportedPolicyTypes for
     * PolicyTypeCompositon.SINGLE.
     * @throws Exception
     */
    public void testGetSupportedPolicyTypesSingle() throws Exception {
        PolicyTypeComposition ptc = PolicyTypeComposition.SINGLE;
        List policyTypes = Arrays.asList(ptc.getSupportedPolicyTypes());
        assertNotNull(policyTypes);
        assertEquals("There should be 5 policy types", 5, policyTypes.size());
        assertTrue(policyTypes.contains(PolicyType.BOOLEAN));
        assertTrue(policyTypes.contains(PolicyType.INT));
        assertTrue(policyTypes.contains(PolicyType.RANGE));
        assertTrue(policyTypes.contains(PolicyType.SELECTION));
        assertTrue(policyTypes.contains(PolicyType.TEXT));
    }

    /**
     * Tests PolicyTypeComposition#getSupportedPolicyTypes for
     * PolicyTypeCompositon.STRUCTURE.
     * @throws Exception
     */
    public void testGetSupportedPolicyTypesStructure() throws Exception {
        PolicyTypeComposition ptc = PolicyTypeComposition.STRUCTURE;
        List policyTypes = Arrays.asList(ptc.getSupportedPolicyTypes());
        assertNotNull(policyTypes);
        assertEquals("There should be 1 policy type", 1, policyTypes.size());
        assertTrue(policyTypes.contains(PolicyType.EMULATE_EMPHASIS_TAG));
    }

    /**
     * Tests PolicyTypeComposition#getSupportedPolicyTypes for
     * PolicyTypeCompositon.UNORDERED_SET.
     * @throws Exception
     */
    public void testGetSupportedPolicyTypesUnorderedSet() throws Exception {
        PolicyTypeComposition ptc = PolicyTypeComposition.UNORDERED_SET;
        List policyTypes = Arrays.asList(ptc.getSupportedPolicyTypes());
        assertNotNull(policyTypes);
        assertEquals("There should be 4 policy types", 4, policyTypes.size());
        assertTrue(policyTypes.contains(PolicyType.INT));
        assertTrue(policyTypes.contains(PolicyType.RANGE));
        assertTrue(policyTypes.contains(PolicyType.SELECTION));
        assertTrue(policyTypes.contains(PolicyType.TEXT));
    }

    /**
     * Tests reading the policy type resources from EditorMessages. The keys
     * for the resources contain the names passed to the PolicyType
     * constructors. This test ensures there is no mismatch between the keys
     * and the constructed PolicyTypes.
     */
    public void testPolicyTypeCompositionResources() throws Exception {
        final String prefix = "DevicesMessages.policyTypeComposition.";
        PolicyTypeComposition[] compositions =
                PolicyTypeComposition.getPolicyTypeCompositions();
        for (int i = 0; i < compositions.length; i++) {
            final String name = compositions[i].name;
            assertNotNull(DevicesMessages.getString(prefix + name + ".name"));
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 06-Dec-05	10539/1	adrianj	VBM:2005111712 Added 'New' button for device themes

 01-Dec-05	10520/1	adrianj	VBM:2005111712 Implement New... button for device themes

 17-Nov-04	6012/1	allan	VBM:2004051307 Remove standard elements in admin mode.

 27-Aug-04	5315/1	geoff	VBM:2004082404 Improve testsuite device repository test speed.

 25-Aug-04	5298/2	geoff	VBM:2004081720 Make automagic mdpr migration compatible with the runtime

 11-May-04	4161/2	doug	VBM:2004031604 Added the PolicyDefinitionSection composite

 04-May-04	4121/3	pcameron	VBM:2004042910 Localised the policy type and composition names, and completed test cases

 29-Apr-04	4103/3	allan	VBM:2004042812 Redesign PolicyType & PolicyTypeComposition.

 29-Apr-04	4103/1	allan	VBM:2004042812 Redesign PolicyType & PolicyTypeComposition.

 ===========================================================================
*/
