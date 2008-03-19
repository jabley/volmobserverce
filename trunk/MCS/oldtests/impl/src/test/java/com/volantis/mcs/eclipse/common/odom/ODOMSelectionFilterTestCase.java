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

package com.volantis.mcs.eclipse.common.odom;

import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.mcs.xml.xpath.XPath;
import com.volantis.mcs.xml.xpath.XPathException;
import org.jdom.input.JDOMFactory;

/**
 * Test the ODOMSelectionFilter class.
 */
public class ODOMSelectionFilterTestCase
        extends TestCaseAbstract {

    /**
     * ODOMfactory for factoring ODOMObservables
     */
    private JDOMFactory factory = new ODOMFactory();

    /**
     * Test the constructor.
     */
    public void testODOMSelectionFilter() throws Exception {
        String filterNames[] = {"name1", "name2"};
        ODOMSelectionFilter filter;
        filter = new ODOMSelectionFilter(new XPath("."), null);
        assertNull(filter.getFilteredNames());

        filter = new ODOMSelectionFilter(new XPath("."), filterNames);
        assertTrue(filterNames != filter.getFilteredNames());

        filterNames[0] = null;
        try {
            filter = new ODOMSelectionFilter(new XPath("."), filterNames);
            fail("Filter array may not contain a null value");
        } catch (IllegalArgumentException e) {
        }
    }

    /**
     * Test the filter.
     */
    public void testInclude() throws Exception {
        String filterNames[] = {"name1", "name2"};

        ODOMSelectionFilter filter;
        final XPath resolver = new XPath(".");
        filter = new ODOMSelectionFilter(resolver, filterNames);
        boolean result = filter.include((ODOMElement)factory.element("name1"));
        assertFalse(result);

        result = filter.include((ODOMElement)factory.element("name2"));
        assertFalse(result);

        result = filter.include((ODOMElement)factory.element("NAME2"));
        assertTrue(result);

        result = filter.include((ODOMElement)factory.element("notinlist"));
        assertTrue(result);

        filterNames = null;
        filter = new ODOMSelectionFilter(resolver, filterNames);
        result = filter.include((ODOMElement)factory.element("name1"));
        assertTrue(result);

        filter = new ODOMSelectionFilter(resolver, filterNames);
        result = filter.include((ODOMElement)factory.element("name2"));
        assertTrue(result);
    }

    /**
     * Test that this method returns a clone of the filter names.
     */
    public void testGetFilteredNames() throws Exception {
        final XPath resolver = new XPath(".");
        String filterNames[] = null;
        ODOMSelectionFilter filter = new ODOMSelectionFilter(resolver, filterNames);
        assertNull(filter.getFilteredNames());

        filterNames = new String[] { "test" };
        filter = new ODOMSelectionFilter(resolver, filterNames);
        assertNotNull(filter.getFilteredNames());
        assertNotEquals(filter.getFilteredNames(), filterNames);

        for (int i = 0; i < filterNames.length; i++) {
            String filterName = filterNames[i];
            assertEquals("Filter name should match", filterName,
                    filter.getFilteredNames()[i]);
        }
    }

    /**
     * Test the resolve method.
     */
    public void testResolve() throws Exception {
        XPath resolver = new XPath(".");
        String filterNames[] = null;
        ODOMSelectionFilter filter = new ODOMSelectionFilter(resolver, filterNames);
        ODOMElement result = filter.resolve((ODOMElement)factory.element("myName"));
        assertNotNull(result);
        assertEquals("Element name should match", "myName", result.getName());

        resolver = new XPath("cd");
        filter = new ODOMSelectionFilter(resolver, filterNames);
        result = filter.resolve((ODOMElement)factory.element("catalog"));
        assertNull("Result should be null", result);

        resolver = new XPath("cd");
        filter = new ODOMSelectionFilter(resolver, filterNames);
        ODOMElement catalog = (ODOMElement)factory.element("catalog");
        ODOMElement cd = (ODOMElement)factory.element("cd");
        cd.setAttribute("title", "titleName");
        catalog.addContent(cd);
        result = filter.resolve(catalog);

        assertNotNull("Result shouldn't be null.", result);
        assertEquals("Result should match", "cd", result.getName());

        catalog.addContent((ODOMElement)factory.element("cd"));
        try {
            result = filter.resolve(catalog);
            fail("Expected an XPathException.");
        } catch (XPathException e) {
        }

        resolver = new XPath("cd/@title");
        filter = new ODOMSelectionFilter(resolver, filterNames);
        try {
            result = filter.resolve(catalog);
            fail("Expected an XPathException.");
        } catch (XPathException e) {
        }
    }

    /**
     * Test the equals method for reflexive, symmetric, transitive and null
     * comparisons (amongst others).
     */
    public void testEquals() throws Exception {
        String filterNames[] = {"name1", "name2"};
        ODOMSelectionFilter filter1;
        ODOMSelectionFilter filter3;
        ODOMSelectionFilter filter4;
        ODOMSelectionFilter filter2;
        filter1 = new ODOMSelectionFilter(new XPath("."), filterNames);
        filter2 = new ODOMSelectionFilter(new XPath("."), filterNames);
        filter3 = new ODOMSelectionFilter(new XPath("."), filterNames);
        filter4 = new ODOMSelectionFilter(new XPath("catalog"), filterNames);

        // Reflexive.
        assertTrue(filter1.equals(filter1));

        // Symmetric
        assertTrue(filter1.equals(filter2));
        assertTrue(filter2.equals(filter1));

        // Transitive
        assertTrue(filter1.equals(filter2));
        assertTrue(filter2.equals(filter3));
        assertTrue(filter1.equals(filter3));

        // Null value.
        assertFalse(filter1.equals(null));

        // Not equals
        assertFalse(filter1.equals(filter4));
        assertFalse(filter4.equals(filter1));
        assertFalse(filter2.equals(filter4));
    }

    /**
     * Test the hashCode.
     */
    public void testHashCode() throws Exception {
        String filter[] = {"name1", "name2"};
        ODOMSelectionFilter filter1;
        ODOMSelectionFilter filter2;
        ODOMSelectionFilter filter3;

        filter1 = new ODOMSelectionFilter(new XPath("."), filter);
        filter2 = new ODOMSelectionFilter(new XPath("."), filter);
        filter3 = new ODOMSelectionFilter(new XPath("catalog"), filter);

        assertEquals("Hashcode should match", filter1.hashCode(),
                filter2.hashCode());
        // Thanks to synergetics fudge of the assertXXX methods I have to use
        // the assertTrue here.
        assertTrue("Hashcode should not match",
                filter1.hashCode() != filter3.hashCode());

        // Hashcodes of xpath1 and xpath2 match => equals should match too.
        assertTrue(filter1.equals(filter2));
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 21-Dec-04	6524/1	allan	VBM:2004112610 Move xpath and xml validation out of eclipse

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 09-Jul-04	4841/1	adrianj	VBM:2004010802 Removal of ODOMValidatable infrastructure

 15-Dec-03	2160/1	doug	VBM:2003120702 Modified ODOMObservables so that they cab validate themesevles.

 27-Nov-03	2036/3	byron	VBM:2003111902 Element Selection implementation - renamed testFilter testcase to testInclude

 27-Nov-03	2036/1	byron	VBM:2003111902 Element Selection implementation - added testcase and fixed bugs

 ===========================================================================
*/
