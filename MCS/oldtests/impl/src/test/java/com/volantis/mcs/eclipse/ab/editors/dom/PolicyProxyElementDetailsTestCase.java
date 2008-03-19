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
package com.volantis.mcs.eclipse.ab.editors.dom;

import com.volantis.mcs.eclipse.common.PolicyAttributesDetails;
import com.volantis.mcs.eclipse.common.odom.ODOMElement;
import com.volantis.mcs.repository.xml.PolicySchemas;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.input.DefaultJDOMFactory;
import org.jdom.input.JDOMFactory;

import java.util.ArrayList;

/**
 * Test case for PolicyProxyElementDetails.
 */
public class PolicyProxyElementDetailsTestCase extends TestCaseAbstract {

    /**
     * Test that setProxiedElements() behaves as expected. In this case testing
     * with reasons of ATTRIB and ATTRIB_VALUES should return false and
     * other reasons should return true if the element name has changed.
     */
    public void testSetProxiedElementsReasons() {
        PolicyProxyElementDetails details =
                new PolicyProxyElementDetails("imageComponent");
        ProxyElementDetails.ChangeReason reason =
                ProxyElementDetails.ATTRIBUTES;
        JDOMFactory factory = new DefaultJDOMFactory();
        ArrayList elements = new ArrayList();
        Element element = factory.element("deviceImageAsset");
        elements.add(element);

        assertFalse("Should be false with an ATTRIBUTES reason.",
                details.setProxiedElements(elements.iterator(), reason));

        reason = ProxyElementDetails.ATTRIB_VALUES;
        assertFalse("Should be false with an ATTRIB_VALUES reason.",
                details.setProxiedElements(elements.iterator(), reason));

        element.setName("genericImageAsset");
        reason = ProxyElementDetails.ELEMENT_NAMES;
        assertTrue("Element name has changed and reason is ELEMENT_NAMES.",
                details.setProxiedElements(elements.iterator(), reason));

        element.setName("convertibleImageAsset");
        reason = ProxyElementDetails.ELEMENTS;
        assertTrue("Element name has changed and reason is ELEMENT.",
                details.setProxiedElements(elements.iterator(), reason));

    }

    /**
     * Test the setProxiedElement with elements containing at least one null
     * element.
     */
    public void testSetProxiedElementsNullElement() {
        PolicyProxyElementDetails details =
                new PolicyProxyElementDetails("imageComponent");

        ProxyElementDetails.ChangeReason reason = ProxyElementDetails.ELEMENTS;

        ArrayList elements = new ArrayList();
        elements.add(null);

        assertFalse("Should be false with an ATTRIBUTES reason.",
                details.setProxiedElements(elements.iterator(), reason));
    }


    /**
     * Test that setProxiedElements() returns false when there is no
     * change in the element name.
     */
    public void testSetProxiedElementsNoChange() {
        PolicyProxyElementDetails details =
                new PolicyProxyElementDetails("imageComponent");
        ProxyElementDetails.ChangeReason reason =
                ProxyElementDetails.ELEMENTS;
        JDOMFactory factory = new DefaultJDOMFactory();
        ArrayList elements = new ArrayList();
        elements.add(factory.element(ODOMElement.NULL_ELEMENT_NAME));

        assertFalse("Should be false since the first time " +
                "setProxiedElements is called, element should be \"" +
                ODOMElement.NULL_ELEMENT_NAME + "\".",
                details.setProxiedElements(elements.iterator(), reason));
    }

    /**
     * Test that setProxiedElements() returns true and resets its
     * element name when it should.
     */
    public void testSetProxiedElementsChanged() {
        PolicyProxyElementDetails details =
                new PolicyProxyElementDetails("imageComponent");
        ProxyElementDetails.ChangeReason reason =
                ProxyElementDetails.ELEMENTS;
        JDOMFactory factory = new DefaultJDOMFactory();
        ArrayList elements = new ArrayList();

        // Test that when the element changes then the element name changes.
        elements.add(factory.element("deviceImageAsset"));
        assertTrue("Element name has changed and reason is ELEMENTS.",
                details.setProxiedElements(elements.iterator(), reason));

        assertEquals("Element name should now be \"deviceImageAsset\"",
                "deviceImageAsset", details.getElementName());

        // Test that different element names produce UNDEFINED_ELEMENT_NAME
        Element element = factory.element("genericImageAsset");
        elements.add(element);
        assertTrue("Element name has changed and reason is ELEMENTS.",
                details.setProxiedElements(elements.iterator(), reason));
        assertEquals("Element name should now be \"" +
                ODOMElement.UNDEFINED_ELEMENT_NAME + "\"",
                ODOMElement.UNDEFINED_ELEMENT_NAME, details.getElementName());

        // Test that multi element names that are the same produce that
        // element name.
        element.setName("deviceImageAsset");
        assertTrue("Element name has changed and reason is ELEMENTS.",
                details.setProxiedElements(elements.iterator(), reason));

        assertEquals("Element name should now be \"deviceImageAsset\"",
                "deviceImageAsset", details.getElementName());

        // Test that when there are no elements the name is NULL_ELEMENT_NAME
        elements.clear();
        assertTrue("Element name has changed and reason is ELEMENTS.",
                details.setProxiedElements(elements.iterator(), reason));
        assertEquals("Element name should now be \"" +
                ODOMElement.NULL_ELEMENT_NAME + "\"",
                ODOMElement.NULL_ELEMENT_NAME, details.getElementName());

    }

    /**
     * Test that getElementNamespace() returns the expected value.
     */
    public void testGetElementNamespace() {
        PolicyProxyElementDetails details =
                new PolicyProxyElementDetails("imageComponent");
        Namespace expected = Namespace.getNamespace("lpdm",
                PolicySchemas.MARLIN_LPDM_2006_02.getNamespaceURL());
        assertEquals(expected, details.getElementNamespace());
    }

    /**
     * Test that getAttributeNames() works as expected.
     */
    public void testGetAttributeNames() {
        PolicyAttributesDetails attrDetails =
                new PolicyAttributesDetails("imageComponent", true);
        PolicyProxyElementDetails pped =
                new PolicyProxyElementDetails("imageComponent");

        String [] expected = attrDetails.getAttributes();
        String [] actual = pped.getAttributeNames();

        assertEquals("Wrong number of attributes.", expected.length,
                actual.length);

        // The attributes do not have to be in the same order.
        for(int i=0; i<expected.length; i++) {
            boolean found = false;
            for(int i2=0; i<actual.length && !found; i2++) {
                found = expected[i].equals(actual[i2]);
            }
            assertTrue("Attribute \"" + expected[i] + "\" not found.", found);
        }
    }

    /**
     * Test isAttributeName() using a valid attribute name.
     */
    public void testIsAttributeNamePositive() {
        PolicyAttributesDetails attrDetails =
                new PolicyAttributesDetails("imageComponent", true);
        PolicyProxyElementDetails pped =
                new PolicyProxyElementDetails("imageComponent");

        String [] attrs = attrDetails.getAttributes();

        for(int i=0; i<attrs.length; i++) {
            assertTrue("\"" + attrs[i] + "\" is a valid attribute name.",
                    pped.isAttributeName(attrs[i]));
        }
    }

    /**
     * Test isAttributeName() using in invalid attribute name.
     */
    public void testIsAttributeNameNegative() {
        PolicyProxyElementDetails pped =
                new PolicyProxyElementDetails("imageComponent");

        assertFalse("\"hello\" is not a valid attribute name for an" +
                "imageComponent.", pped.isAttributeName("hello"));
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 13-Nov-05	9896/1	geoff	VBM:2005101906 Avoid using JDOM in MCS at runtime: rewrite runtime XML device repository

 11-Oct-05	9729/2	geoff	VBM:2005100507 Mariner Export fails with NPE

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 28-Jun-04	4764/3	matthew	VBM:2003121802 remove ProxyElementDetails.GENERAL

 28-Jun-04	4764/1	matthew	VBM:2003121802 remove ProxyElementDetails.GENERAL

 19-Dec-03	2267/1	byron	VBM:2003121804 Upgrade to Eclipse v2.1.2 has broken deletion of assets

 16-Dec-03	2213/1	allan	VBM:2003121401 More editors and fixes for presentable values.

 12-Dec-03	2123/2	allan	VBM:2003102005 Candidate ImageComponentEditor - no validation.

 ===========================================================================
*/
