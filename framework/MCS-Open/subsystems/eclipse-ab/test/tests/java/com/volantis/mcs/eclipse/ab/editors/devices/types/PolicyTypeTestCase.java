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

package com.volantis.mcs.eclipse.ab.editors.devices.types;

import com.volantis.devrep.repository.api.devices.DeviceRepositorySchemaConstants;
import com.volantis.mcs.eclipse.ab.editors.devices.BooleanPolicyValueModifier;
import com.volantis.mcs.eclipse.ab.editors.devices.ComboPolicyValueModifier;
import com.volantis.mcs.eclipse.ab.editors.devices.DevicesMessages;
import com.volantis.mcs.eclipse.ab.editors.devices.PolicyValueModifier;
import com.volantis.mcs.eclipse.ab.editors.devices.StructurePolicyValueModifier;
import com.volantis.mcs.eclipse.ab.editors.devices.TextPolicyValueModifier;
import com.volantis.mcs.eclipse.common.odom.ODOMFactory;
import com.volantis.mcs.eclipse.core.DeviceRepositoryAccessorManager;
import com.volantis.devrep.repository.impl.testtools.device.TestDeviceRepositoryCreator;
import com.volantis.synergetics.cornerstone.utilities.xml.jaxp.TestTransformerMetaFactory;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.synergetics.testtools.io.TemporaryFileExecutor;
import com.volantis.synergetics.testtools.io.TemporaryFileManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.input.DefaultJDOMFactory;
import org.jdom.input.JDOMFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Test case for PolicyType.
 */
public class PolicyTypeTestCase extends TestCaseAbstract {

    /**
     * A map of policy type to the PolicyValueModifier subclass that is used
     * to modify the values of the policy type.
     */
    private static final Map TYPE_TO_PVM_CLASS;

    /**
     * A map of policy type to policy which provides a policy for each type to
     * test.
     */
    private static final Map TYPE_TO_TEST_POLICY;

    /**
     * The selection values to test. The only values that are supplied to
     * multi-value PolicyValueModifiers that can be tested at runtime are
     * for selection types.
     */
    private static final String[] SELECTION_VALUES;

    /**
     * JDOMFactory for creating JDOM related objects
     */
    private static final JDOMFactory JDOM_FACTORY = new DefaultJDOMFactory();

    /**
     * Device repository Namespace
     */
    private static final Namespace DEVICE_NS = Namespace.getNamespace("ns",
            "http://www.volantis.com/xmlns/device-repository/device");

    /**
     * Populate the two maps with test data.
     */
    static {
        TYPE_TO_PVM_CLASS = new HashMap(8, 1);
        TYPE_TO_TEST_POLICY = new HashMap(8, 1);

        TYPE_TO_PVM_CLASS.put("boolean", BooleanPolicyValueModifier.class);
        TYPE_TO_TEST_POLICY.put("boolean", "beep");

        TYPE_TO_PVM_CLASS.put("EmulateEmphasisTag",
                StructurePolicyValueModifier.class);
        TYPE_TO_TEST_POLICY.put("EmulateEmphasisTag",
                "protocol.wml.emulate.bigTag");

        TYPE_TO_PVM_CLASS.put("int", TextPolicyValueModifier.class);
        TYPE_TO_TEST_POLICY.put("int", "pixelsx");

        TYPE_TO_PVM_CLASS.put("range", TextPolicyValueModifier.class);
        TYPE_TO_TEST_POLICY.put("range", "localsec");

        TYPE_TO_PVM_CLASS.put("selection", ComboPolicyValueModifier.class);
        TYPE_TO_TEST_POLICY.put("selection", "java");
        SELECTION_VALUES =
                new String[]{"none", "J2SE", "J2ME", "JavaCard",
                             "PersonalJava"};

        TYPE_TO_PVM_CLASS.put("text", TextPolicyValueModifier.class);
        TYPE_TO_TEST_POLICY.put("text", "name");
    }

    /**
     * Constructor for PolicyTypeTestCase.
     * @param arg0 the title of the test
     */
    public PolicyTypeTestCase(String arg0) {
        super(arg0);
    }

    /**
     * Tests the policy types of the typesafe enumeration.
     */
    public void testPolicyTypes() throws Exception {

        TemporaryFileManager tempFileMgr = new TemporaryFileManager(
                new TestDeviceRepositoryCreator());
        tempFileMgr.executeWith(new TemporaryFileExecutor() {
            public void execute(File repository) throws Exception {

                DeviceRepositoryAccessorManager deviceRAM =
                        new DeviceRepositoryAccessorManager(
                                repository.getPath(),
                                new TestTransformerMetaFactory(),
                                JDOM_FACTORY, false);

                // Iterate over the policy types.
                Iterator it = TYPE_TO_PVM_CLASS.keySet().iterator();

                while (it.hasNext()) {

                    // Get the next policy type name.
                    String name = (String) it.next();

                    // Get the PolicyType for the name.
                    PolicyType policyType = PolicyType.getType(name);

                    // The PolicyType should not be null.
                    assertNotNull(policyType);

                    // The name of the retrieved PolicyType should match the named
                    // used to retrieve it.
                    assertTrue(policyType.getName().equals(name));

                    // Get the policy name to use for testing the policy type.
                    String policyName = (String) TYPE_TO_TEST_POLICY.get(name);

                    // Create the PolicyValueModifier
                    PolicyValueModifier pvMod =
                            policyType.createPolicyValueModifier(
                                    new Shell(Display.getDefault()),
                                    SWT.NONE, policyName, deviceRAM);

                    // The PolicyValueModifier should not be null.
                    assertNotNull(pvMod);

                    // The class of the created PolicyValueModifier should be the
                    // expected class.
                    assertEquals(TYPE_TO_PVM_CLASS.get(name), pvMod.getClass());

                    // Check the values of the PolicyValueModifier for selection type.
                    if (policyType.getName().equals(PolicyType.SELECTION.getName())) {
                        assertTrue(checkSelectionValues(pvMod));
                    }
                }
            }
        });
    }

    /**
     * Checks that the inital values of the PolicyValueModifier for selection
     * types is correct.
     * @param pvMod the PolicyValueModifier
     * @return true if values are correct, false otherwise
     */
    private boolean checkSelectionValues(PolicyValueModifier pvMod) {

        // Get the Combo and retrieve its items.
        Combo combo = (Combo) pvMod.getControl();
        String[] values = combo.getItems();

        // Check that the values are what is expected.
        boolean valuesEqual = Arrays.equals(SELECTION_VALUES, values);

        return valuesEqual;
    }

    /**
     * Helper method to create and return the policy element with a default
     * value for the specified policy type.
     * @param policyType the policy type
     * @return the policy element with an appropriate default value
     */
    private Element createPolicyElement(final PolicyType policyType) {
        final JDOMFactory factory = new ODOMFactory();

        final Element policiesElement = factory.element(
                DeviceRepositorySchemaConstants.POLICIES_ELEMENT_NAME,
                DEVICE_NS);
        policyType.addDefaultPolicyValue(policiesElement,
                "myPolicy", factory, null);

        Element policyElement = (Element) policiesElement.getChildren().get(0);

        return policyElement;
    }

    /**
     * Tests the PolicyType.addTypeElement method for PolicyType.BOOLEAN.
     * @throws Exception
     */
    public void testAddPolicyTypeElementForBooleanType() throws Exception {
        Element element = createPolicyTypeElement(PolicyType.BOOLEAN);

        checkPolicyTypeElement(element,
                DeviceRepositorySchemaConstants.
                POLICY_DEFINITION_BOOLEAN_ELEMENT_NAME);
    }

    /**
     * Tests the PolicyType.addTypeElement method for
     * PolicyType.EMULATE_EMPHASIS_TAG.
     * @throws Exception
     */
    public void testAddPolicyTypeElementForEmulateEmphasisTagType() throws Exception {
        Element type = JDOM_FACTORY.element("type");

        PolicyType.EMULATE_EMPHASIS_TAG.addPolicyTypeElement(type,
                JDOM_FACTORY);

        List children =
                type.getChildren(DeviceRepositorySchemaConstants.
                POLICY_DEFINITION_TYPE_ELEMENT_NAME, DEVICE_NS);

        assertEquals("There should no children of the type element",
                0, children.size());

        String refName =
                type.getAttributeValue(DeviceRepositorySchemaConstants.
                POLICY_DEFINITION_REF_ATTRIBUTE_NAME);

        assertTrue(DeviceRepositorySchemaConstants.
                POLICY_DEFINITION_EMULATE_EMPHASIS_TAG_NAME.equals(refName));
    }

    /**
     * Tests the PolicyType.addTypeElement method for PolicyType.INT.
     * @throws Exception
     */
    public void testAddPolicyTypeElementForIntType() throws Exception {
        Element element = createPolicyTypeElement(PolicyType.INT);

        checkPolicyTypeElement(element,
                DeviceRepositorySchemaConstants.
                POLICY_DEFINITION_INT_ELEMENT_NAME);
    }

    /**
     * Tests the PolicyType.addTypeElement method for PolicyType.RANGE_INCLUSIVE_INCLUSIVE.
     * @throws Exception
     */
    public void testAddPolicyTypeElementForRangeType() throws Exception {
        Element element = createPolicyTypeElement(PolicyType.RANGE);

        checkPolicyTypeElement(element,
                DeviceRepositorySchemaConstants.
                POLICY_DEFINITION_RANGE_ELEMENT_NAME);
    }

    /**
     * Tests the PolicyType.addTypeElement method for PolicyType.SELECTION.
     * @throws Exception
     */
    public void testAddPolicyTypeElementForSelectionType() throws Exception {
        Element element = createPolicyTypeElement(PolicyType.SELECTION);

        checkPolicyTypeElement(element,
                DeviceRepositorySchemaConstants.
                POLICY_DEFINITION_SELECTION_ELEMENT_NAME);
    }

    /**
     * Tests the PolicyType.addTypeElement method for PolicyType.TEXT.
     * @throws Exception
     */
    public void testAddPolicyTypeElementForTextType() throws Exception {
        Element element = createPolicyTypeElement(PolicyType.TEXT);

        checkPolicyTypeElement(element,
                DeviceRepositorySchemaConstants.
                POLICY_DEFINITION_TEXT_ELEMENT_NAME);
    }

    /**
     * Helper method to create a type element with a parent.
     * @param policyType the policy type
     */
    private Element createPolicyTypeElement(PolicyType policyType) {
        // Create the parent element with the device namespace.
        final Element parent = JDOM_FACTORY.element("type", DEVICE_NS);

        // add the type element
        policyType.addPolicyTypeElement(parent, JDOM_FACTORY);

        return parent;
    }

    /**
     * Helper method to check the type element.
     * @param parent the parent element of the type element
     * @param name the name of the policy type
     */
    private void checkPolicyTypeElement(Element parent, String name) {

        List children = parent.getChildren();

        // There is only the one child element with the given name
        assertEquals("A single element should have been added.",
                1, children.size());

        Element child = (Element) children.get(0);
        assertEquals("Expected a " + name + " element to be added. ",
                name, child.getName());
    }

    /**
     * Tests the PolicyType.addDefaultPolicyValue method for PolicyType.BOOLEAN.
     * @throws Exception
     */
    public void testAddDefaultPolicyValueForBooleanType() throws Exception {
        Element policyElement = createPolicyElement(PolicyType.BOOLEAN);
        assertTrue("false".equals(policyElement.getAttributeValue(
                DeviceRepositorySchemaConstants.POLICY_VALUE_ATTRIBUTE)));
    }

    /**
     * Tests the PolicyType.addDefaultPolicyValue method for
     * PolicyType.EMULATE_EMPHASIS_TAG.
     * @throws Exception
     */
    public void testAddDefaultPolicyValueForEmulateEmphasisTagType()
            throws Exception {

        TemporaryFileManager tempFileMgr = new TemporaryFileManager(
                new TestDeviceRepositoryCreator());
        tempFileMgr.executeWith(new TemporaryFileExecutor() {
            public void execute(File repository) throws Exception {

                DeviceRepositoryAccessorManager deviceRAM =
                        new DeviceRepositoryAccessorManager(
                                repository.getPath(),
                                new TestTransformerMetaFactory(),
                                JDOM_FACTORY, false);

                final Element policiesElement = JDOM_FACTORY.element(
                        DeviceRepositorySchemaConstants.POLICIES_ELEMENT_NAME,
                        DEVICE_NS);

                EmulateEmphasisTagType eetPT =
                        (EmulateEmphasisTagType) PolicyType.EMULATE_EMPHASIS_TAG;

                eetPT.addDefaultPolicyValue(policiesElement,
                        "protocol.wml.emulate.bigTag",
                        JDOM_FACTORY, deviceRAM);

                List policyChildren = policiesElement.getChildren(
                        DeviceRepositorySchemaConstants.POLICY_ELEMENT_NAME, DEVICE_NS);

                assertEquals(1, policyChildren.size());

                Element policyElement = (Element) policyChildren.get(0);

                assertTrue("protocol.wml.emulate.bigTag".
                        equals(policyElement.getAttributeValue(
                                DeviceRepositorySchemaConstants.
                        POLICY_NAME_ATTRIBUTE)));

                List fieldChildren = policyElement.getChildren(
                        DeviceRepositorySchemaConstants.
                        POLICY_DEFINITION_FIELD_ELEMENT_NAME, DEVICE_NS);

                assertEquals(4, fieldChildren.size());

                List fieldNames = new ArrayList(4);
                fieldNames.add(DeviceRepositorySchemaConstants.
                        POLICY_DEFINITION_ENABLE_FIELD_NAME);
                fieldNames.add("prefix");
                fieldNames.add("suffix");
                fieldNames.add("altTag");

                // Check that all field names are present.
                Iterator it = fieldChildren.iterator();
                boolean containsAll = true;
                while (it.hasNext()) {
                    Element field = (Element) it.next();
                    String name = field.getAttributeValue(
                            DeviceRepositorySchemaConstants.POLICY_NAME_ATTRIBUTE);
                    containsAll = containsAll &&
                            fieldNames.contains(name);
                }
                assertTrue(containsAll);
            }
        });
    }


    /**
     * Tests the getPolicyValueStructure method for PolicyType.BOOLEAN.
     */
    public void testGetPolicyValueStructureForBoolean() throws Exception {
        assertSame(PolicyType.BOOLEAN.getPolicyValueStructure(),
                PolicyType.PolicyValueStructure.SINGLE_VALUED);
    }

    /**
     * Tests the getPolicyValueStructure method for
     * PolicyType.EMULATE_EMPHASIS_TAG.
     */
    public void testGetPolicyValueStructureForEmulateEmphasisTag() throws Exception {
        assertSame(PolicyType.EMULATE_EMPHASIS_TAG.getPolicyValueStructure(),
                PolicyType.PolicyValueStructure.UNSPECIFIED);
    }

    /**
     * Tests the getPolicyValueStructure method for PolicyType.INT.
     */
    public void testGetPolicyValueStructureForInt() throws Exception {
        assertSame(PolicyType.INT.getPolicyValueStructure(),
                PolicyType.PolicyValueStructure.SINGLE_VALUED);
    }

    /**
     * Tests the getPolicyValueStructure method for PolicyType.RANGE_INCLUSIVE_INCLUSIVE.
     */
    public void testGetPolicyValueStructureForRange() throws Exception {
        assertSame(PolicyType.RANGE.getPolicyValueStructure(),
                PolicyType.PolicyValueStructure.SINGLE_VALUED);
    }

    /**
     * Tests the getPolicyValueStructure method for PolicyType.SELECTION.
     */
    public void testGetPolicyValueStructureForSelection() throws Exception {
        assertSame(PolicyType.SELECTION.getPolicyValueStructure(),
                PolicyType.PolicyValueStructure.SINGLE_VALUED);
    }

    /**
     * Tests the getPolicyValueStructure method for PolicyType.TEXT.
     */
    public void testGetPolicyValueStructureForText() throws Exception {
        assertSame(PolicyType.TEXT.getPolicyValueStructure(),
                PolicyType.PolicyValueStructure.SINGLE_VALUED);
    }

    /**
     * Test getSingleValuedPolicyTypes() returns the expected result.
     */
    public void testGetSingleValuedPolicyTypes() throws Exception {
        PolicyType[] singleValuedPolicyTypes =
                PolicyType.getSingleValuedPolicyTypes();
        // There should be 5 SVPTs
        assertEquals("There should be 5 SINGLE_VALUED PolicyTypes.",
                5, singleValuedPolicyTypes.length);

        Collection svpts = Arrays.asList(singleValuedPolicyTypes);
        assertTrue("Expected PolicyType.BOOLEAN to be single valued.",
                svpts.contains(PolicyType.BOOLEAN));
        assertTrue("Expected PolicyType.INT to be single valued.",
                svpts.contains(PolicyType.INT));
        assertTrue("Expected PolicyType.RANGE_INCLUSIVE_INCLUSIVE to be single valued.",
                svpts.contains(PolicyType.RANGE));
        assertTrue("Expected PolicyType.SELECTION to be single valued.",
                svpts.contains(PolicyType.SELECTION));
        assertTrue("Expected PolicyType.TEXT to be single valued.",
                svpts.contains(PolicyType.TEXT));
    }

    /**
     * Tests the PolicyType.addDefaultPolicyValue method for PolicyType.INT.
     * @throws Exception
     */
    public void testAddDefaultPolicyValueForIntType() throws Exception {
        Element policyElement = createPolicyElement(PolicyType.INT);
        assertTrue("0".equals(policyElement.getAttributeValue(
                DeviceRepositorySchemaConstants.POLICY_VALUE_ATTRIBUTE)));
    }

    /**
     * Tests the PolicyType.addDefaultPolicyValue method for PolicyType.RANGE_INCLUSIVE_INCLUSIVE.
     * @throws Exception
     */
    public void testAddDefaultPolicyValueForRangeType() throws Exception {
        Element policyElement = createPolicyElement(PolicyType.RANGE);
        assertTrue("0".equals(policyElement.getAttributeValue(
                DeviceRepositorySchemaConstants.POLICY_VALUE_ATTRIBUTE)));
    }

    /**
     * Tests the PolicyType.addDefaultPolicyValue method for
     * PolicyType.SELECTION.
     * @throws Exception
     */
    public void testAddDefaultPolicyValueForSelectionType() throws Exception {
        Element policyElement = createPolicyElement(PolicyType.SELECTION);
        assertTrue("".equals(policyElement.getAttributeValue(
                DeviceRepositorySchemaConstants.POLICY_VALUE_ATTRIBUTE)));
    }

    /**
     * Tests the PolicyType.addDefaultPolicyValue method for PolicyType.TEXT.
     * @throws Exception
     */
    public void testAddDefaultPolicyValueForTextType() throws Exception {
        Element policyElement = createPolicyElement(PolicyType.TEXT);
        assertTrue("".equals(policyElement.getAttributeValue(
                DeviceRepositorySchemaConstants.POLICY_VALUE_ATTRIBUTE)));
    }

    /**
     * Tests reading the policy type resources from EditorMessages. The keys
     * for the resources contain the names passed to the PolicyType
     * constructors. This test ensures there is no mismatch between the keys
     * and the constructed PolicyTypes.
     */
    public void testPolicyTypeResources() throws Exception {
        final String prefix = "DevicesMessages.policyType.";
        PolicyType[] policyTypes = new PolicyType[]{
            PolicyType.BOOLEAN, PolicyType.EMULATE_EMPHASIS_TAG,
            PolicyType.INT, PolicyType.RANGE, PolicyType.SELECTION,
            PolicyType.TEXT
        };
        for (int i = 0; i < policyTypes.length; i++) {
            final String name = policyTypes[i].getName();
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

 13-Sep-04	5315/4	geoff	VBM:2004082404 Improve testsuite device repository test speed. (merge conflicts)

 27-Aug-04	5315/1	geoff	VBM:2004082404 Improve testsuite device repository test speed.

 08-Sep-04	5451/19	pcameron	VBM:2004081609 Empty attribute values are deleted by editors

 25-Aug-04	5298/2	geoff	VBM:2004081720 Make automagic mdpr migration compatible with the runtime

 09-Jul-04	4841/1	adrianj	VBM:2004010802 Removal of ODOMValidatable infrastructure

 11-May-04	4161/1	doug	VBM:2004031604 Added the PolicyDefinitionSection composite

 04-May-04	4121/6	pcameron	VBM:2004042910 Localised the policy type and composition names, and completed test cases

 29-Apr-04	4103/1	allan	VBM:2004042812 Redesign PolicyType & PolicyTypeComposition.

 08-Apr-04	3806/1	doug	VBM:2004040810 Paramaterized the DeviceRepositoryAccessorManager and the XMLDeviceRepositoryAccessor contstructors with a JDOMFactory

 08-Apr-04	3686/6	pcameron	VBM:2004032204 Some further tweaks to PolicyType

 06-Apr-04	3686/4	pcameron	VBM:2004032204 Added new methods to PolicyType and refactored

 06-Apr-04	3686/1	pcameron	VBM:2004032204 Added new methods to PolicyType and refactored

 22-Mar-04	3480/1	pcameron	VBM:2004030410 Added PolicyType

 ===========================================================================
*/
