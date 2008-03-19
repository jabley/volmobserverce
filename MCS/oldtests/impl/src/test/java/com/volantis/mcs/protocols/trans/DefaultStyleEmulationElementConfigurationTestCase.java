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
package com.volantis.mcs.protocols.trans;

import com.volantis.synergetics.testtools.TestCaseAbstract;

import java.util.Map;
import java.util.Set;
import java.util.HashSet;

import junitx.util.PrivateAccessor;

/**
 * Test the default style emulation element configuration.
 */
public class DefaultStyleEmulationElementConfigurationTestCase
        extends TestCaseAbstract {

    protected DefaultStyleEmulationElementConfiguration cfg;
    protected final HashSet permittedChildren = new HashSet();

    // javadoc inherited.
    protected void setUp() throws Exception {
        super.setUp();
        cfg = new DefaultStyleEmulationElementConfiguration();
        permittedChildren.add("child");
    }

    // javadoc inherited.
    protected void tearDown() throws Exception {
        cfg = null;
        super.tearDown();
    }

    /**
     * Test the method: associateStylisticAndAntiElements
     */
    public void testAssociateStylisticElement() throws Exception {
        try {
            cfg.associateStylisticAndAntiElements(null, null, null);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
        // code block
        {
            String[] children = null;
            createSet(children);
            cfg.associateStylisticAndAntiElements("a", null, permittedChildren);
            verifyStylisticAssociations(1, 1, children);
        }
        // code block
        {
            String[] children = { "x" };
            createSet(children);
            cfg.associateStylisticAndAntiElements("a", null, permittedChildren);
            verifyStylisticAssociations(1, 1, children);
        }
        // code block
        {
            String[] children = { "x", "y" };
            createSet(children);
            cfg.associateStylisticAndAntiElements("a", null, permittedChildren);
            verifyStylisticAssociations(1, 2, children);
        }
        // code block
        {
            String[] children = { "y", "zzz" };
            createSet(children);
            String[] expectedChildren =  { "x", "y", "zzz" };
            cfg.associateStylisticAndAntiElements("a", null, permittedChildren);
            verifyStylisticAssociations(1, 3, expectedChildren);
        }
    }

    /**
     * Reset {@link #permittedChildren} so that it contains only the strings
     * defined in the given string array.
     *
     * @param children  string array to which to reset permittedChildren
     * @return Set the reset class variable permittedChildren
     */
    protected Set createSet(String[] children) {
        permittedChildren.clear();
        assertEquals(0, permittedChildren.size());
        if (children != null) {
            for (int i = 0; children != null && i< children.length; i++) {
                permittedChildren.add(children[i]);
            }
            assertEquals(children.length, permittedChildren.size());
        }
        return permittedChildren;
    }

    /**
     * Helper method.
     */
    private void verifyStylisticAssociations(int keyCount,
                                             int permitteChildrenCount,
                                             String[] permittedChildren)
            throws Exception {
        Map stylistElementMap = (Map)PrivateAccessor.getField(
                cfg, "stylisticElementMap");
        final Set keys = stylistElementMap.keySet();
        final Set children = (Set)stylistElementMap.get("a");

        assertEquals(keyCount, keys.size());
        if (permittedChildren == null) {
            assertNull("No children expected", children);
        } else {
            assertEquals(permitteChildrenCount, children.size());
            for (int i = 0; (permittedChildren != null) &&
                    (i < permittedChildren.length); i++) {
                String s = permittedChildren[i];
                assertTrue("Expected child '" + s + "' to be in the permitted " +
                           "children set.", children.contains(s));
            }
        }
    }

    /**
     * Test the method: isAntiElement
     */
    public void testIsAntiElement() throws Exception {
        assertFalse(cfg.isAntiElement("anti-test"));
        cfg.associateStylisticAndAntiElements("test", "anti-test", null);
        assertFalse(cfg.isAntiElement("test"));
        assertTrue(cfg.isAntiElement("anti-test"));

        cfg.associateStylisticAndAntiElements("bold", "anti-bold", null);
        assertFalse(cfg.isAntiElement("bold"));
        assertFalse(cfg.isAntiElement("ANTI-BOLD"));
        assertTrue(cfg.isAntiElement("anti-test"));
        assertTrue(cfg.isAntiElement("anti-bold"));
    }

    /**
     * Test the method: isIndivisibleElement
     */
    public void testIsIndivisibleElement() throws Exception {
        assertTrue(cfg.isIndivisibleElement("test"));
        assertTrue(cfg.isIndivisibleElement("d1"));
        assertTrue(cfg.isIndivisibleElement("ind1"));
        assertTrue(cfg.isIndivisibleElement("ind2"));

        cfg.associateStylisticAndAntiElements("anti-test", "test",
                permittedChildren);
        cfg.addDivisibleElementsThatPermitStyles(new String[] {"d1"});
        cfg.addIndivisibleElementsThatPermitStyles(new String[] {"ind1", "ind2"});

        assertTrue(cfg.isIndivisibleElement("ind1"));
        assertTrue(cfg.isIndivisibleElement("ind2"));
        assertTrue(cfg.isIndivisibleElement("not-in-any-set"));
        assertTrue(cfg.isIndivisibleElement("child"));
        assertTrue(cfg.isIndivisibleElement("TEST"));
        assertTrue(cfg.isIndivisibleElement("D1"));

        assertFalse(cfg.isIndivisibleElement("test"));
        assertFalse(cfg.isIndivisibleElement("d1"));
        assertFalse(cfg.isIndivisibleElement("anti-test"));
    }

    /**
     * Test the method: isMergeableElement
     */
    public void testIsMergeableElement() throws Exception {
        assertFalse(cfg.isMergeableElement("font"));
        cfg.addMergeableElement("font");
        assertTrue(cfg.isMergeableElement("font"));

        cfg.addMergeableElement("mergeable");
        assertTrue(cfg.isMergeableElement("font"));
        assertTrue(cfg.isMergeableElement("mergeable"));
    }

    /**
     * Test the method: isStylisticElement
     */
    public void testIsStylisticElement() throws Exception {
        assertFalse(cfg.isStylisticElement("test"));
        cfg.associateStylisticAndAntiElements("test", null, permittedChildren);
        cfg.associateStylisticAndAntiElements("stylistic", null,
                permittedChildren);
        assertTrue(cfg.isStylisticElement("test"));
        assertTrue(cfg.isStylisticElement("stylistic"));
        assertFalse(cfg.isStylisticElement("child"));
    }

    /**
     * Test the method: isDivisibleStyleElement
     */
    public void testIsDivisibleStyleElement() throws Exception {
        assertFalse(cfg.isDivisibleStyleElement("child"));
        cfg.addDivisibleElementsThatPermitStyles(new String[] {"child", null});
        assertFalse(cfg.isDivisibleStyleElement("test"));
        assertFalse(cfg.isDivisibleStyleElement("stylistic"));
        assertTrue(cfg.isDivisibleStyleElement("child"));
        assertTrue(cfg.isDivisibleStyleElement(null));
    }

    /**
     * Test the method: isStylePermittedInElement
     */
    public void testIsStylePermittedInElement() throws Exception {
        assertFalse(cfg.isStylePermittedInElement("test"));
        assertFalse(cfg.isStylePermittedInElement("d1"));
        assertFalse(cfg.isStylePermittedInElement("ind1"));
        assertFalse(cfg.isStylePermittedInElement("ind2"));

        cfg.associateStylisticAndAntiElements("test", "anti-test",
                permittedChildren);
        cfg.addDivisibleElementsThatPermitStyles(new String[] {"d1"});
        cfg.addIndivisibleElementsThatPermitStyles(
                new String[] {"ind1", "ind2"});

        assertTrue(cfg.isStylePermittedInElement("test"));
        assertTrue(cfg.isStylePermittedInElement("d1"));
        assertTrue(cfg.isStylePermittedInElement("ind1"));
        assertTrue(cfg.isStylePermittedInElement("ind2"));

        assertFalse(cfg.isStylePermittedInElement("anti-test"));
        assertFalse(cfg.isStylePermittedInElement("child"));
        assertFalse(cfg.isStylePermittedInElement("TEST"));
        assertFalse(cfg.isStylePermittedInElement("D1"));
    }

    /**
     * Test the method: addDivisibleElementsThatPermitStyles
     */
    public void testAddDivisibleElementsThatPermitStyles() throws Exception {
        String[] elements = { "a", "b", "c" };
        cfg.addDivisibleElementsThatPermitStyles(elements);

        String[] moreElements = { "c", "d" };
        cfg.addDivisibleElementsThatPermitStyles(moreElements);

        Set set = (Set)PrivateAccessor.getField(
                cfg, "divisibleElementsThatPermitStyles");
        assertEquals("Should have added and merged all elements:",
                     4, set.size());

        for (int i = 0; i < elements.length; i++) {
            assertTrue("Should be in list: ", set.contains(elements[i]));
        }
        for (int i = 0; i < moreElements.length; i++) {
            assertTrue("Should be in list: ", set.contains(moreElements[i]));
        }
    }

    /**
     * Test the method: addIndivisibleElementsThatPermitStyles
     */
    public void testAddIndivisibleElementsThatPermitStyles() throws Exception {
        String[] elements = { "a", "b", "c" };
        cfg.addIndivisibleElementsThatPermitStyles(elements);

        String[] moreElements = { "c", "d" };
        cfg.addIndivisibleElementsThatPermitStyles(moreElements);

        Set set = (Set)PrivateAccessor.getField(
                cfg, "indivisibleElementsThatPermitStyles");
        assertEquals("Should have added and merged all elements:",
                     6, set.size());

        for (int i = 0; i < elements.length; i++) {
            assertTrue("Should be in list: ", set.contains(elements[i]));
        }
        for (int i = 0; i < moreElements.length; i++) {
            assertTrue("Should be in list: ", set.contains(moreElements[i]));
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Oct-04	5877/5	byron	VBM:2004101911 Style Emulation: fix definition and implementation of Atomic Elements - rework issues

 28-Oct-04	5877/3	byron	VBM:2004101911 Style Emulation: fix definition and implementation of Atomic Elements - rework issues

 27-Oct-04	5877/1	byron	VBM:2004101911 Style Emulation: fix definition and implementation of Atomic Elements

 ===========================================================================
*/
