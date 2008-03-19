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

package com.volantis.mcs.eclipse.ab.core;

import com.volantis.mcs.eclipse.common.ControlType;
import com.volantis.mcs.eclipse.common.Filter;
import com.volantis.mcs.eclipse.common.PolicyAttributesDetails;
import com.volantis.synergetics.testtools.TestCaseAbstract;

import java.util.ArrayList;
import java.util.List;

/**
 * Test the building of attribute composite controls.
 */
public class PolicyAttributesDetailsTestCase
        extends TestCaseAbstract {

    /**
     * Test getPresentableValue() where there are specific presentable
     * values available.
     */
    public void testGetPresentableValuePositive() {
        PolicyAttributesDetails attributesDetails =
                new PolicyAttributesDetails("imageComponent", true);

        assertEquals("Incorrect presentable value", "GIF",
                attributesDetails.getPresentableValue("encoding", "gif"));
        assertEquals("Incorrect presentable value", "BMP",
                attributesDetails.getPresentableValue("encoding", "bmp"));
        assertEquals("Incorrect presentable value", "JPEG",
                attributesDetails.getPresentableValue("encoding", "jpeg"));
        assertEquals("Incorrect presentable value", "WBMP",
                attributesDetails.getPresentableValue("encoding", "wbmp"));
        assertEquals("Incorrect presentable value", "PJPEG",
                attributesDetails.getPresentableValue("encoding", "pjpeg"));
        assertEquals("Incorrect presentable value", "TIFF",
                attributesDetails.getPresentableValue("encoding", "tiff"));
        assertEquals("Incorrect presentable value", "VIDEOTEX",
                attributesDetails.getPresentableValue("encoding", "videotex"));
    }

    /**
     * Test that getPresentableValue() returns value when there is no
     * specific presentable value available.
     */
    public void testGetPresentableValueNegative() {
        PolicyAttributesDetails attributesDetails =
                new PolicyAttributesDetails("imageComponent", true);
        assertEquals("Incorrect presentable value",
                "24", attributesDetails.getPresentableValue("pixelDepth",
                        "24"));
    }

    /**
     * Test that a normal PolicyAttributesDetails with no attribute
     * filtering and no use of child elements contains the right
     * attributes.
     */
    public void testNormalAttributes() {
        List properties = new ArrayList();
        properties.add("prefixURL");
        properties.add("locationType");

        checkReturnedAttributes(properties, "assetGroup", false);
    }

    /**
     * Test that a normal PolicyAttributes with no attribute filtering
     * but using the child elements contains the right attributes, using
     * the imageComponent element.
     */
    public void testNormalChildAttributes() {
        List properties = new ArrayList();
        properties.add("assetGroupName");
        properties.add("deviceName");
        properties.add("encoding");
        properties.add("pixelDepth");
        properties.add("pixelsX");
        properties.add("pixelsY");
        properties.add("rendering");
        properties.add("value");
        properties.add("widthHint");
        properties.add("sequence");
        properties.add("sequenceSize");
        properties.add("preserveLeft");
        properties.add("preserveRight");
        checkReturnedAttributes(properties, "imageComponent", true);
    }

    /**
     * Ensures that the expected attributes are retrieved for a given
     * element name.
     */
    private void checkReturnedAttributes(List expected, String elementName,
                                         boolean childAttrs) {
        PolicyAttributesDetails attributesDetails =
                new PolicyAttributesDetails(elementName, childAttrs);

        String[] result = attributesDetails.getAttributes();

        assertNotNull(result);
        assertEquals("There should be " + expected.size() + " attributes.",
                expected.size(), result.length);

        for (int i = 0; i < result.length; i++) {
            String name = result[i];
            assertTrue("Attribute not found in attributes: " + name,
                    expected.contains(name));
        }
    }

    /**
     * Test that attributes filtered to be included are included and are
     * the only attributes in the PolicyAttributesDetails.
     */
    public void testFilterInclusion() {
        String[] include = {
            "assetGroupName",
            "deviceName",
            "encoding",
        };

        PolicyAttributesDetails attributesDetails =
                new PolicyAttributesDetails("imageComponent",
                        new Filter(include, Filter.INCLUDE), true);

        String[] result = attributesDetails.getAttributes();

        assertNotNull(result);
        assertEquals("There should be 3 attributes.", 3,
                result.length);

        List properties = new ArrayList();
        properties.add("assetGroupName");
        properties.add("deviceName");
        properties.add("encoding");

        for (int i = 0; i < result.length; i++) {
            String name = result[i];
            assertTrue("Attribute not found in attributes: " + name,
                    properties.contains(name));
        }
    }


    /**
     * Test that attributes filtered to be excluded are excluded.
     */
    public void testFilterExclusion() {
        String[] exclude = {
            "assetGroupName",
            "deviceName",
            "encoding",
            "project"
        };

        PolicyAttributesDetails attributesDetails =
                new PolicyAttributesDetails("imageComponent",
                        new Filter(exclude, Filter.EXCLUDE), true);

        String[] result = attributesDetails.getAttributes();

        assertNotNull(result);
        assertEquals("There should be 10 attributes.", 10,
                result.length);

        List properties = new ArrayList();
        properties.add("pixelDepth");
        properties.add("pixelsX");
        properties.add("pixelsY");
        properties.add("rendering");
        properties.add("sequence");
        properties.add("sequenceSize");
        properties.add("value");
        properties.add("widthHint");
        properties.add("sequence");
        properties.add("sequenceSize");
        properties.add("preserveLeft");
        properties.add("preserveRight");      

        for (int i = 0; i < result.length; i++) {
            String name = result[i];
            assertTrue("Attribute not found in attributes: " + name,
                    properties.contains(name));
        }
    }

    /**
     * Test that getAttributeValueSelection returns the expected values
     * selection for a sample attribute - in this case the encoding
     * attribute of ImageAsset. In this case the selection is based on
     * a child attribute.
     */
    public void testGetChildAttributeValueSelectionPositive() {
        PolicyAttributesDetails attributesDetails =
                new PolicyAttributesDetails("imageComponent", true);

        Object[] encodings =
                attributesDetails.getAttributeValueSelection("encoding");

        assertNotNull(encodings);
        assertEquals("There should be 8 encodings.", 8, encodings.length);


        List properties = new ArrayList();
        properties.add("bmp");
        properties.add("gif");
        properties.add("jpeg");
        properties.add("pjpeg");
        properties.add("png");
        properties.add("wbmp");
        properties.add("tiff");
        properties.add("videotex");

        for (int i = 0; i < encodings.length; i++) {
            String name = encodings[i].toString();
            assertTrue("Encoding not found in encodings: " + name,
                    properties.contains(name));
        }
    }


    /**
     * Test that getAttributeValueSelection returns the expected values
     * selection for a sample attribute - in this case the location
     * attribute of AssetGroup. In this case the selection is based on
     * the element itself i.e. AssetGroup.
     */
    public void testGetAttributeValueSelectionPositive() {
        PolicyAttributesDetails attributesDetails =
                new PolicyAttributesDetails("assetGroup", false);

        Object[] locations =
                attributesDetails.getAttributeValueSelection("locationType");

        assertNotNull(locations);
        assertEquals("There should be 2 locations.", 2, locations.length);


        List properties = new ArrayList();
        properties.add("server");
        properties.add("device");

        for (int i = 0; i < locations.length; i++) {
            String name = locations[i].toString();
            assertTrue("Location not found in locations: " + name,
                    properties.contains(name));
        }
    }

    /**
     * Test that getAttributeValueSelection returns null when there
     * is no associated value selection.
     */
    public void testGetAttributeValueSelectionNegative() {
        PolicyAttributesDetails attributesDetails =
                new PolicyAttributesDetails("imageComponent", false);

        assertNull(attributesDetails.
                getAttributeValueSelection("fallbackTextComponent"));
    }

    /**
     * Test that getAttributeControlType works as expected with the only
     * policy that currently has a value for it i.e. AudioAsset.encoding.
     */
    public void testGetAttributeControlTypePositive() {
        PolicyAttributesDetails attributesDetails =
                new PolicyAttributesDetails("audioComponent", true);

        ControlType controlType =
                attributesDetails.getAttributeControlType("encoding");

        assertNotNull(controlType);
        assertEquals("Expected ControlType.READ_ONLY_COMBO_VIEWER",
                ControlType.READ_ONLY_COMBO_VIEWER, controlType);
    }


    /**
     * Test that getAttributeControlType returns null when there is no
     * known control type for a given atttribute
     */
    public void testGetAttributeControlTypeNegative() {
        PolicyAttributesDetails attributesDetails =
                new PolicyAttributesDetails("audioComponent", false);

        ControlType controlType =
                attributesDetails.getAttributeControlType("encoding");

        assertNull(controlType);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 07-Nov-05	10168/1	ianw	VBM:2005102504 port forward web clipping

 07-Nov-05	10170/1	ianw	VBM:2005102504 port forward web clipping

 04-Nov-05	9999/3	pszul	VBM:2005102504 preserver area implemented in ConvertibleImageAsset

 04-Nov-05	9999/1	pszul	VBM:2005102504 preserver area implemented in ConvertibleImageAsset

 11-Mar-05	7308/5	tom	VBM:2005030702 Added XHTMLSmartClient and support for image sequences

 09-Mar-05	7315/3	allan	VBM:2005030711 Add sequences of image assets.

 09-Mar-05	7315/1	allan	VBM:2005030711 Add sequences of image assets.

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 24-Sep-04	5627/1	adrianj	VBM:2004090812 Fix for device theme overview page

 27-May-04	4532/1	byron	VBM:2004052104 ASCII-Art Image Asset Encoding

 01-Feb-04	2821/1	mat	VBM:2004012701 Change tests and generate scripts for Projects

 30-Jan-04	2767/1	claire	VBM:2004012701 Add project

 06-Jan-04	2323/1	doug	VBM:2003120701 Added better validation error messages

 17-Dec-03	2213/4	allan	VBM:2003121401 Basic editor support for all policies. Some bugs remain.

 16-Dec-03	2213/2	allan	VBM:2003121401 More editors and fixes for presentable values.

 15-Dec-03	2208/4	allan	VBM:2003121201 Include ImageAssetDetails in this fix.

 13-Dec-03	2208/2	allan	VBM:2003121201 Use PresentableItems for presenting attribute values.

 12-Dec-03	2123/1	allan	VBM:2003102005 Candidate ImageComponentEditor - no validation.

 27-Nov-03	2013/1	allan	VBM:2003112501 Candidate commit for AttributesComposite redesign.

 ===========================================================================
*/
