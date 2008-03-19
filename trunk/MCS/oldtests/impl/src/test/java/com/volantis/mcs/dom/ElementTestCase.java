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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/dom/ElementTestCase.java,v 1.4 2003/01/28 15:22:48 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 20-Aug-02    Byron           VBM:2002081311 - A JUnit test case to test the
 *                              Element object. Currently the copy() and
 *                              copyAttributes() methods are targeted for tests
 * 30-Dec-02    Byron           VBM:2002110807 - Moved from tests package to
 *                              this package and renamed class name to end with
 *                              TestCase. Updated comments and code style.
 * 14-Jan-03    Chris W         VBM:2002111508 - Add tests for promote()
 * 27-Jan-03    Geoff           VBM:2003012302 - Split all the tests for 
 *                              promote in two to check behaviour of new
 *                              trimEmptyNodes parameter.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.dom;

import junit.framework.TestCase;
import com.volantis.mcs.dom.debug.DOMUtilities;

/**
 * Unit tests for the Element object.
 * @author byron
 */
public class ElementTestCase extends TestCase {

    /*static
    {
        org.apache.log4j.BasicConfigurator.configure();
    }*/

    /**
     * Elements used in the promote tests
     */
    private Element topLevel, e1, e2, e3, e4;
    
    /**
     * Text used in the promote tests
     */
    private Text t1, t2, w1, w2;

    /**
     * Factory to use to create DOM objects.
     */
    private DOMFactory domFactory = DOMFactory.getDefaultInstance();

    /**
     * Creates new TestDOMElement
     */
    public ElementTestCase(String name) {
        super(name);
    }

    /**
     * Create some test nodes and set up their content.
     * @see junit.framework.TestCase#setUp()
     */
    public void setUp()
    {
        // Some elements
        topLevel = domFactory.createElement();   // Top level element for all tests
        e1 = domFactory.createElement();
        e2 = domFactory.createElement();
        e3 = domFactory.createElement();
        e4 = domFactory.createElement();
        topLevel.setName("topLevel");        
        e1.setName("e1");
        e2.setName("e2");
        e3.setName("e3");
        e4.setName("e4");
        
        // Some text
        t1 = domFactory.createText();
        t2 = domFactory.createText();
        t1.append("t1");
        t2.append("t2");
        
        // Some whitespace
        w1 = domFactory.createText();
        w2 = domFactory.createText();
        w1.append(" ");
        w2.append(" ");
    }

    /**
     * Utility method to test whether two strings are exactly the same
     *
     * @param  s1   a string requiring comparison
     * @param  s2   a string requiring comparison
     * @return      true if s1 is exactly the same as s2, false otherwise
     */
    private boolean stringMatch(String s1, String s2) {
        if (s1 == s2) {
            return true;
        } else if (s1 == null || s2 == null) {
            return false;
        }
        return s1.equals(s2);
    }

    /**
     * Test to see if the attribute list starting at the given item a1 matches
     * the list starting with a2. The method should return false if the one
     * list is empty, and the other not, or if the list differs in length, or
     * each item (name and value) is not the same in the list
     *
     * @param  a1   attribute handle to first item in a list, may be null
     * @param  a2   attribute handle to first item in a list, may be null
     * @return      true if attribute list a1 is equal to attributes list a2,
     *              false otherwise
     */
    private boolean attributesEqual(Attribute a1, Attribute a2) {
        // Both null or both 'point' to same non-null object
        if (a1 == a2) {
            return true;
            // One is null, other isn't
        } else if (a1 == null || a2 == null) {
            return false;
        }
        // If we get here, both a1 and a2 are not null
        boolean hasNext = true;
        Attribute attribute1 = a1;
        Attribute attribute2 = a2;
        while (hasNext) {
            if (!stringMatch(attribute1.getName(), attribute2.getName()) ||
                    !stringMatch(attribute1.getValue(), attribute2.getValue())) {
                return false;
            }
            attribute1 = attribute1.getNext();
            attribute2 = attribute2.getNext();
            if (attribute1 == null && attribute2 != null) {
                return false;
            } else if (attribute1 != null && attribute2 == null) {
                return false;
            }
            hasNext = (attribute1 != null && attribute2 != null);
        }
        return true;
    }

    /**
     * Two element are equal if they are both null, or they are both not null
     * and have tbe same name and attributes Otherwise they are not equal
     * <p>
     * Note: perhaps an equals method should be placed into the Element class
     *
     * @param  e1   element handle to be matched with e2, may be null
     * @param  e2   element handle to be matched with e1, may be null
     * @return      true if element e1 is equal to element e2, false otherwise
     */
    private boolean elementsEqual(Element e1, Element e2) {
        // Both null or both 'point' to same non-null object
        if (e1 == e2) {
            return true;
            // One is null, other isn't
        } else if (e1 == null || e2 == null) {
            return false;
            // Names should match
        } else if (!stringMatch(e1.getName(), e2.getName())) {
            return false;
            // All attributes should match too
        } else if (!attributesEqual(e1.getAttributes(), e2.getAttributes())) {
            return false;
        }
        return true;
    }

    /**
     * Test a simple copy:
     * <ul>
     * <li>empty elements (no attributes or name)</li>
     * <li>source element has one attribute</li>
     * <li>source element has > 1 attribute</li>
     * </ul>
     */
    public void testSimpleCopying() {
        Element e1 = domFactory.createElement();
        Element e2 = domFactory.createElement();
        // Two 'empty' elements
        assertTrue(elementsEqual(e1, e2));

        e1.copy(e2);
        assertTrue(elementsEqual(e1, e2));

        // One element has attributes
        e2.setAttribute("Attribute 1", "Value 1");
        assertTrue(elementsEqual(e1, e2) == false);

        // Copy non-empty element into empty element
        e1.copy(e2);
        assertTrue(elementsEqual(e1, e2));

        // Create several more attributes for e2
        for (int i = 0; i < 10; i++) {
            e2.setAttribute("List attribute " + i, "List value " + i);
        }
        assertTrue(elementsEqual(e1, e2) == false);

        // Copy non-empty e2 element into non-empty element e1
        e1.copy(e2);
        assertTrue(elementsEqual(e1, e2));

        e1 = null;
        e2 = null;
    }

    /**
     * Slightly more complex:
     * <ul>
     * <li>source and dest elements have names that differ</li>
     * <li>source is empty, dest is non-empty (has attributes)</li>
     * <li>source and dest are the same object</li>
     * <li>source is null</li>
     * </ul>
     */
    public void testComplexCopying() {
        Element e1 = domFactory.createElement();
        Element e2 = domFactory.createElement();
        e1.setName("e1");
        e2.setName("e2");

        // Names differ
        assertTrue(elementsEqual(e1, e2) == false);

        // Make names the same
        e1.copy(e2);
        assertTrue(elementsEqual(e1, e2));

        // Create several more attributes for e2
        for (int i = 0; i < 10; i++) {
            e2.setAttribute("List attribute " + i, "List value " + i);
        }
        assertTrue(elementsEqual(e1, e2) == false);

        // Copy empty element into non-empty element
        assertTrue(!e1.hasAttributes() && e2.hasAttributes());
        e2.copy(e1);
        assertTrue(elementsEqual(e1, e2));

        // Copy this element into itself
        e2.copy(e2);
        assertTrue(elementsEqual(e2, e2));

        try {
            e2.copy(null);
            fail("Should throw an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
        try {
            e2.copyAttributes(null);
            fail("Should throw an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
    }

    public void testPromoteOnlyChild() throws Exception {
        checkPromoteOnlyChild(false);
    }

    public void testPromoteOnlyChildTrim() throws Exception {
        checkPromoteOnlyChild(true);
    }
    
    /**
     * Tests that we when promote e2 in the
     * <topLevel><e1><e2/></e1></topLevel>
     * that we get
     * <topLevel><e2/></topLevel>
     * i.e. As the node is the only child of this element then this element
     * is replaced by the node.
     * @throws java.lang.Exception
     */
    private void checkPromoteOnlyChild(boolean trim) throws Exception
    {
        topLevel.addHead(e1);
        e1.addHead(e2);                
        e2.promote(trim);
        
        String expected = "<topLevel><e2/></topLevel>";
        assertEquals("Wrong dom returned", expected, 
                    DOMUtilities.toString(topLevel));
    }

    public void testPromoteFirstChild1() throws Exception {
        checkPromoteFirstChild1(false);
    }
    
    public void testPromoteFirstChild1Trim() throws Exception {
        checkPromoteFirstChild1(true);
    }

    /**
     * Tests that we when promote e2 in the
     * <topLevel><e1><e2/><e3/></e1></topLevel>
     * that we get
     * <topLevel><e2/><e1><e3/></e1></topLevel>
     * i.e. As the node is the first child of this element then it is
     * inserted before this element.
     * @throws java.lang.Exception
     */
    private void checkPromoteFirstChild1(boolean trim) throws Exception
    {
        topLevel.addHead(e1);
        e1.addTail(e2);
        e1.addTail(e3);                
        e2.promote(trim);
        
        String expected = "<topLevel><e2/><e1><e3/></e1></topLevel>";
        assertEquals("Wrong dom returned", expected, 
                    DOMUtilities.toString(topLevel));
    }

    public void testPromoteFirstChild2() throws Exception {
        checkPromoteFirstChild2(false);
    }
    
    public void testPromoteFirstChild2Trim() throws Exception {
        checkPromoteFirstChild2(true);
    }

    /**
     * Tests that we when promote e2 in the
     * <topLevel><e1><e2/>t1</e1></topLevel>
     * that we get
     * <topLevel><e2/><e1>t1</e1></topLevel>
     * i.e. As the node is the first child of this element then it is
     * inserted before this element.
     * @throws java.lang.Exception
     */
    private void checkPromoteFirstChild2(boolean trim) throws Exception
    {
        topLevel.addHead(e1);
        e1.addTail(e2);
        e1.addTail(t1);                
        e2.promote(trim);
        
        String expected = "<topLevel><e2/><e1>t1</e1></topLevel>";
        assertEquals("Wrong dom returned", expected, 
                    DOMUtilities.toString(topLevel));
    }

    public void testPromoteFirstChild3() throws Exception {
        checkPromoteFirstChild3(false);
    }

    public void testPromoteFirstChild3Trim() throws Exception {
        checkPromoteFirstChild3(true);
    }
    
    /**
     * Tests that we when promote e2 in the
     * <topLevel><e1><e2/> </e1></topLevel>
     * that we get
     * <topLevel><e2/><e1> </e1></topLevel>
     * i.e. As the node is the first child of this element then it is
     * inserted before this element.
     * @throws java.lang.Exception
     */
    private void checkPromoteFirstChild3(boolean trim) throws Exception
    {
        topLevel.addHead(e1);
        e1.addTail(e2);
        e1.addTail(w1);                
        e2.promote(trim);
        
        String expected = "<topLevel><e2/><e1> </e1></topLevel>";
        assertEquals("Wrong dom returned", expected, 
                    DOMUtilities.toString(topLevel));
    }

    public void testPromoteLastChild1() throws Exception {
        checkPromoteLastChild1(false);
    }
    
    public void testPromoteLastChild1Trim() throws Exception {
        checkPromoteLastChild1(true);
    }
    
    /**
     * Tests that we when promote e3 in the
     * <topLevel><e1><e2/><e3/></e1></topLevel>
     * that we get
     * <topLevel><e1><e2/></e1><e3/></topLevel>
     * i.e. As the node is the last child of this element then it is
     * inserted after this element.
     * @throws java.lang.Exception
     */
    private void checkPromoteLastChild1(boolean trim) throws Exception
    {
        topLevel.addHead(e1);        
        e1.addTail(e2);
        e1.addTail(e3);                
        e3.promote(trim);
        
        String expected = "<topLevel><e1><e2/></e1><e3/></topLevel>";
        assertEquals("Wrong dom returned", expected, 
                    DOMUtilities.toString(topLevel));
    }

    public void testPromoteLastChild2() throws Exception {
        checkPromoteLastChild2(false);
    }

    public void testPromoteLastChild2Trim() throws Exception {
        checkPromoteLastChild2(true);
    }

    /**
     * Tests that we when promote e3 in the
     * <topLevel><e1>t1<e3/></e1></topLevel>
     * that we get
     * <topLevel><e1>t1</e1><e3/></topLevel>
     * i.e. As the node is the last child of this element then it is
     * inserted after this element.
     * @throws java.lang.Exception
     */
    private void checkPromoteLastChild2(boolean trim) throws Exception
    {
        topLevel.addHead(e1);        
        e1.addTail(t1);
        e1.addTail(e3);                
        e3.promote(trim);
        
        String expected = "<topLevel><e1>t1</e1><e3/></topLevel>";
        assertEquals("Wrong dom returned", expected, 
                    DOMUtilities.toString(topLevel));
    }

    public void testPromoteLastChild3() throws Exception {
        checkPromoteLastChild3(false);
    }
    
    public void testPromoteLastChild3Trime() throws Exception {
        checkPromoteLastChild3(true);
    }
    
    /**
     * Tests that we when promote e3 in the
     * <topLevel><e1> <e3/></e1></topLevel>
     * that we get
     * <topLevel><e1> </e1><e3/></topLevel>
     * i.e. As the node is the last child of this element then it is
     * inserted after this element.
     * @throws java.lang.Exception
     */
    private void checkPromoteLastChild3(boolean trim) throws Exception
    {
        topLevel.addHead(e1);        
        e1.addTail(w1);
        e1.addTail(e3);                
        e3.promote(trim);
        
        String expected = "<topLevel><e1> </e1><e3/></topLevel>";
        assertEquals("Wrong dom returned", expected, 
                    DOMUtilities.toString(topLevel));
    }

    public void testPromoteMid1() throws Exception {
        checkPromoteMid1(false);
    }
    
    public void testPromoteMid1Trim() throws Exception {
        checkPromoteMid1(true);
    }

    /**
     * Tests that we when promote e3 in the
     * <topLevel><e1><e2/><e3/><e4/></e1></topLevel>
     * that we get
     * <topLevel><e1><e2/></e1><e3/><e1><e4/></e1></topLevel>
     * i.e. the element is split into two, the first of which contains
     * all the children which precede the node and the second of which contains
     * all the children which follow the node. The node is inserted between the
     * two elements.
     * @throws java.lang.Exception
     */
    private void checkPromoteMid1(boolean trim) throws Exception
    {
        topLevel.addHead(e1);        
        e1.addTail(e2);
        e1.addTail(e3);
        e1.addTail(e4);                
        e3.promote(trim);
        
        String expected = "<topLevel><e1><e2/></e1><e3/><e1><e4/></e1></topLevel>";
        assertEquals("Wrong dom returned", expected, 
                    DOMUtilities.toString(topLevel));
    }

    public void testPromoteMid2() throws Exception {
        checkPromoteMid2(false);
    }

    public void testPromoteMid2Trim() throws Exception {
        checkPromoteMid2(true);
    }
    
    /**
     * Tests that we when promote e3 in the
     * <topLevel><e1>t1<e3/><e4/></e1></topLevel>
     * that we get
     * <topLevel><e1>t1</e1><e3/><e1><e4/></e1></topLevel>
     * i.e. the element is split into two, the first of which contains
     * all the children which precede the node and the second of which contains
     * all the children which follow the node. The node is inserted between the
     * two elements.
     * @throws java.lang.Exception
     */
    private void checkPromoteMid2(boolean trim) throws Exception
    {
        topLevel.addHead(e1);        
        e1.addTail(t1);
        e1.addTail(e3);
        e1.addTail(e4);                
        e3.promote(trim);
        
        String expected = "<topLevel><e1>t1</e1><e3/><e1><e4/></e1></topLevel>";
        assertEquals("Wrong dom returned", expected, 
                    DOMUtilities.toString(topLevel));
    }

    public void testPromoteMid3() throws Exception {
        checkPromoteMid3(false);
    }
    
    public void testPromoteMid3Trim() throws Exception {
        checkPromoteMid3(true);
    }
    
    /**
     * Tests that we when promote e3 in the
     * <topLevel><e1> <e3/><e4/></e1></topLevel>
     * that we get
     * <topLevel>[<e1> </e1>]<e3/><e1><e4/></e1></topLevel>
     * i.e. the element is split into two, the first of which contains
     * all the children which precede the node and the second of which contains
     * all the children which follow the node. The node is inserted between the
     * two elements.
     * @throws java.lang.Exception
     */
    private void checkPromoteMid3(boolean trim) throws Exception
    {
        topLevel.addHead(e1);        
        e1.addTail(w1);
        e1.addTail(e3);
        e1.addTail(e4);                
        e3.promote(trim);
        
        String expected =
                "<topLevel>" + 
                (trim ? "" : "<e1> </e1>") +
                "<e3/><e1><e4/></e1></topLevel>";
        assertEquals("Wrong dom returned", expected, 
                    DOMUtilities.toString(topLevel));
    }

    public void testPromoteMid4() throws Exception {
        checkPromoteMid4(false);
    }

    public void testPromoteMid4Trim() throws Exception {
        checkPromoteMid4(true);
    }

    /**
     * Tests that we when promote e3 in the
     * <topLevel><e1><e2/><e3/>t1</e1></topLevel>
     * that we get
     * <topLevel><e1><e2/></e1><e3/><e1>t1</e1></topLevel>
     * i.e. the element is split into two, the first of which contains
     * all the children which precede the node and the second of which contains
     * all the children which follow the node. The node is inserted between the
     * two elements.
     * @throws java.lang.Exception
     */
    private void checkPromoteMid4(boolean trim) throws Exception
    {
        topLevel.addHead(e1);        
        e1.addTail(e2);
        e1.addTail(e3);
        e1.addTail(t1);                
        e3.promote(trim);
        
        String expected = "<topLevel><e1><e2/></e1><e3/><e1>t1</e1></topLevel>";
        assertEquals("Wrong dom returned", expected, 
                    DOMUtilities.toString(topLevel));
    }
    
    public void testPromoteMid5() throws Exception {
        checkPromoteMid5(false);
    }

    public void testPromoteMid5Trim() throws Exception {
        checkPromoteMid5(true);
    }

    /**
     * Tests that we when promote e3 in the
     * <topLevel><e1>t1<e3/>t2</e1></topLevel>
     * that we get
     * <topLevel><e1>t1</e1><e3/><e1>t2</e1></topLevel>
     * i.e. the element is split into two, the first of which contains
     * all the children which precede the node and the second of which contains
     * all the children which follow the node. The node is inserted between the
     * two elements.
     * @throws java.lang.Exception
     */
    private void checkPromoteMid5(boolean trim) throws Exception
    {
        topLevel.addHead(e1);        
        e1.addTail(t1);
        e1.addTail(e3);
        e1.addTail(t2);                
        e3.promote(trim);
        
        String expected = "<topLevel><e1>t1</e1><e3/><e1>t2</e1></topLevel>";
        assertEquals("Wrong dom returned", expected, 
                    DOMUtilities.toString(topLevel));
    }
    
    public void testPromoteMid6() throws Exception {
        checkPromoteMid6(false);
    }
    
    public void testPromoteMid6Trim() throws Exception {
        checkPromoteMid6(true);
    }
    
    /**
     * Tests that we when promote e3 in the
     * <topLevel><e1> <e3/>t2</e1></topLevel>
     * that we get
     * <topLevel>[<e1> </e1>]<e3/><e1>t2</e1></topLevel>
     * i.e. the element is split into two, the first of which contains
     * all the children which precede the node and the second of which contains
     * all the children which follow the node. The node is inserted between the
     * two elements.
     * @throws java.lang.Exception
     */
    private void checkPromoteMid6(boolean trim) throws Exception
    {
        topLevel.addHead(e1);        
        e1.addTail(w1);
        e1.addTail(e3);
        e1.addTail(t2);                
        e3.promote(trim);
        
        String expected =
                "<topLevel>" + 
                (trim ? "" : "<e1> </e1>") + 
                "<e3/><e1>t2</e1></topLevel>";
        assertEquals("Wrong dom returned", expected, 
                    DOMUtilities.toString(topLevel));
    }

    public void testPromoteMid7() throws Exception {
        checkPromoteMid7(false);
    }

    public void testPromoteMid7Trim() throws Exception {
        checkPromoteMid7(true);
    }

    /**
     * Tests that we when promote e3 in the
     * <topLevel><e1><e2/><e3/> </e1></topLevel>
     * that we get
     * <topLevel><e1><e2/></e1><e3/>[<e1> </e1>]</topLevel>
     * i.e. the element is split into two, the first of which contains
     * all the children which precede the node and the second of which contains
     * all the children which follow the node. The node is inserted between the
     * two elements.
     * @throws java.lang.Exception
     */
    private void checkPromoteMid7(boolean trim) throws Exception
    {
        topLevel.addHead(e1);        
        e1.addTail(e2);
        e1.addTail(e3);
        e1.addTail(w1);                
        e3.promote(trim);
        
        String expected = "<topLevel><e1><e2/></e1><e3/>" +
                (trim ? "" : "<e1> </e1>") + 
                "</topLevel>";
        assertEquals("Wrong dom returned", expected, 
                    DOMUtilities.toString(topLevel));
    }

    public void testPromoteMid8() throws Exception {
        checkPromoteMid8(false);
    }
    
    public void testPromoteMid8Trim() throws Exception {
        checkPromoteMid8(true);
    }
    
    /**
     * Tests that we when promote e3 in the
     * <topLevel><e1>t1<e3/> </e1></topLevel>
     * that we get
     * <topLevel><e1>t1</e1><e3/>[<e1> </e1>]</topLevel>
     * i.e. the element is split into two, the first of which contains
     * all the children which precede the node and the second of which contains
     * all the children which follow the node. The node is inserted between the
     * two elements.
     * @throws java.lang.Exception
     */
    private void checkPromoteMid8(boolean trim) throws Exception
    {
        topLevel.addHead(e1);        
        e1.addTail(t1);
        e1.addTail(e3);
        e1.addTail(w1);                
        e3.promote(trim);
        
        String expected =
                "<topLevel><e1>t1</e1><e3/>" + 
                (trim ? "" : "<e1> </e1>") + 
                "</topLevel>";
        assertEquals("Wrong dom returned", expected, 
                    DOMUtilities.toString(topLevel));
    }

    public void testPromoteMid9() throws Exception {
        checkPromoteMid9(false);
    }
    
    public void testPromoteMid9Trim() throws Exception {
        checkPromoteMid9(true);
    }
    
    /**
     * Tests that we when promote e3 in the
     * <topLevel><e1> <e3/> </e1></topLevel>
     * that we get
     * <topLevel>[<e1> </e1>]<e3/>[<e1> </e1>]</topLevel>
     * i.e. the element is split into two, the first of which contains
     * all the children which precede the node and the second of which contains
     * all the children which follow the node. The node is inserted between the
     * two elements.
     * @throws java.lang.Exception
     */
    private void checkPromoteMid9(boolean trim) throws Exception
    {
        topLevel.addHead(e1);        
        e1.addTail(w1);
        e1.addTail(e3);
        e1.addTail(w2);                
        e3.promote(trim);
        
        String expected =
                "<topLevel>" + 
                (trim ? "" : "<e1> </e1>") + 
                "<e3/>" + 
                (trim ? "" : "<e1> </e1>") + 
                "</topLevel>";
        assertEquals("Wrong dom returned", expected, 
                    DOMUtilities.toString(topLevel));
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Jun-05	8483/1	emma	VBM:2005052410 Migrate styling to use the new styling support framework (still using CSSEmulator to style underneath)

 05-May-05	8005/3	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 15-Oct-04	5798/1	adrianj	VBM:2004082515 Device Theme Cascade: Find matching rules

 12-Oct-04	5778/1	adrianj	VBM:2004083106 Provide styling engine API

 30-Jun-04	4781/1	adrianj	VBM:2002111405 VolantisProtocol.defaultMimeType() and DOMProtocol made abstract

 20-Aug-03	1207/1	adrian	VBM:2003032804 removed suite and main methods from testcase classes

 ===========================================================================
*/
