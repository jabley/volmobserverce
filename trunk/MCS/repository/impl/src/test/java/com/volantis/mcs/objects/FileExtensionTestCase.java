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
package com.volantis.mcs.objects;

import com.volantis.mcs.layouts.LayoutSchemaType;
import com.volantis.mcs.policies.PolicyType;
import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * Testcase for FileExtension typesafe enumerator.
 */
public class FileExtensionTestCase extends TestCaseAbstract {
    private static final String [] TYPE_ARRAY = {
        "assetGroup",
        "audioComponent",
        "buttonImageComponent",
        "chartComponent",
        "dynamicVisualComponent",
        "imageComponent",
        LayoutSchemaType.LAYOUT.getName(),
        "linkComponent",
//        "resourceAssetTemplate",
        "rolloverImageComponent",
        "scriptComponent",
        "textComponent",
        "theme"
    };

    /**
     * Test that getFileExtensionForExtension() is case insensitive.
     */
    public void testGetFileExtensionForExtensionCaseInsensitive() {
        String upper = "MDPR";
        String lower = upper.toLowerCase();
        FileExtension upperExtension =
                FileExtension.getFileExtensionForExtension(upper);
        FileExtension lowerExtension =
                FileExtension.getFileExtensionForExtension(lower);

        assertNotNull(upperExtension);
        assertNotNull(lowerExtension);

        assertSame("Expected same FileExtensions for upper and " +
                "lower case mdpr", upperExtension, lowerExtension);
    }

    /**
     * Test that file extensions are as expected. This involves hardcoding
     * the literal extensions here.
     */
    public void testFileExtensions() {
        assertEquals("Invalid assetGroup extension.",
                "mgrp", FileExtension.ASSET_GROUP.getExtension());
        assertEquals("Invalid audioComponent extension.",
                "mauc", FileExtension.AUDIO_COMPONENT.getExtension());
        assertEquals("Invalid buttonImageComponent extension.",
                "mbtn", FileExtension.BUTTON_IMAGE_COMPONENT.getExtension());
        assertEquals("Invalid chartComponent extension.",
                "mcht", FileExtension.CHART_COMPONENT.getExtension());
        assertEquals("Invalid dynamicVisualComponent extension.",
                "mdyv", FileExtension.DYNVIS_COMPONENT.getExtension());
        assertEquals("Invalid imageComponent extension.",
                "mimg", FileExtension.IMAGE_COMPONENT.getExtension());
        assertEquals("Invalid layout extension.",
                "mlyt", FileExtension.LAYOUT.getExtension());
        assertEquals("Invalid linkComponent extension.",
                "mlnk", FileExtension.LINK_COMPONENT.getExtension());
//        assertEquals("Invalid resourceAssetTemplate extension.",
//                "mrat", FileExtension.RESOURCE_ASSET_TEMPLATE.getExtension());
        assertEquals("Invalid rolloverImageComponent extension.",
                "mrlv", FileExtension.ROLLOVER_IMAGE_COMPONENT.getExtension());
        assertEquals("Invalid scriptComponent extension.",
                "mscr", FileExtension.SCRIPT_COMPONENT.getExtension());
        assertEquals("Invalid textComponent extension.",
                "mtxt", FileExtension.TEXT_COMPONENT.getExtension());
        assertEquals("Invalid theme extension.",
                "mthm", FileExtension.THEME.getExtension());
        assertEquals("Invalid device repository extension.",
                "mdpr", FileExtension.DEVICE_REPOSITORY.getExtension());
    }

    /**
     * Test that all the available types have an associated extension.
     */
    public void testPolicyTypes() {
        for(int i=0; i<TYPE_ARRAY.length; i++) {
            assertNotNull("Expected an extension for policy type: " +
                    TYPE_ARRAY[i] + ".",
                    FileExtension.
                    getFileExtensionForPolicyType(TYPE_ARRAY[i]));
        }
    }
    /**
     * Test finding FileExtension instance by string
     * representation of file extension
     */
    public void testGetFileExtensionForExtension() {
        // Test querying for existing extension
        assertEquals(FileExtension.THEME,
                FileExtension.getFileExtensionForExtension("mthm"));
        // Test querying for non-existing extension
        assertNull(FileExtension.getFileExtensionForExtension("non-existing"));
        // Test querying for an empty string
        assertNull(FileExtension.getFileExtensionForExtension(""));
        // Test querying for null
        assertNull(FileExtension.getFileExtensionForExtension(null));
    }

    /**
     * Test finding FileExtension instance by PolicyType instance
     */
    public void testGetFileExtensionForPolicyType() {
        // Test querying for existing policyType
        assertEquals(FileExtension.THEME,
                FileExtension.getFileExtensionForPolicyType(PolicyType.THEME));
        // Test querying for null (returns Device Repository)
        assertEquals(FileExtension.DEVICE_REPOSITORY,
                FileExtension.getFileExtensionForPolicyType((PolicyType)null));

    }

    /**
     * Test finding FileExtension instance from
     * policy filename
     */
    public void testGetFileExtensionForLocalPolicy() {
        // Test querying for existing extension
        assertEquals(FileExtension.THEME,
                FileExtension.getFileExtensionForLocalPolicy("/path/to/devices.mthm"));
        // Test querying for non-existing extension
        assertNull(FileExtension.getFileExtensionForLocalPolicy("/path/to/non.existing.file"));
        // Test querying for an empty string
        assertNull(FileExtension.getFileExtensionForLocalPolicy(""));
        // Test querying for null
        assertNull(FileExtension.getFileExtensionForLocalPolicy(null));
    }

    /**
     * Test extension matching
     */
    public void testMaches() {
        // Test case-insensitive matching
        assertTrue(FileExtension.THEME.matches("MtHm"));
        assertFalse(FileExtension.THEME.matches("mdpr"));
        // Test empty string
        assertFalse(FileExtension.THEME.matches("mdpr"));
        // Test null
        assertFalse(FileExtension.THEME.matches(null));
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 07-Jul-05	8967/1	pduffin	VBM:2005070702 Refactored resolving of expressions into component identities

 08-Feb-05	6910/1	allan	VBM:2005020702 New Resource Asset Template wizard

 06-Jan-05	6474/1	allan	VBM:2004121302 Migration framework

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 16-Feb-04	3044/1	allan	VBM:2004021604 Ensure that MCSProjectBuilder only builds policies.

 12-Feb-04	2962/3	allan	VBM:2004021113 Replace old 3 char file extensions with new 4 char ones.

 28-Jan-04	2752/1	byron	VBM:2004012602 Address issues from review

 29-Dec-03	2258/1	allan	VBM:2003121725 Make Layout editor and wizard conform to new layout schema

 27-Nov-03	2013/1	allan	VBM:2003112501 Candidate commit for AttributesComposite redesign.

 ===========================================================================
*/
