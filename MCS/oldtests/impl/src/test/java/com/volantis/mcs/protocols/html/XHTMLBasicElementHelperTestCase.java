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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/protocols/html/XHTMLBasicElementHelperTestCase.java,v 1.3 2002/10/15 11:13:14 philws Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 25-Sep-02    Phil W-S        VBM:2002091901 - Created. 
 * 11-Oct-02    Phil W-S        VBM:2002100804 - Updated to test getCells.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.html;

import com.volantis.mcs.dom.DOMFactory;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.protocols.trans.ElementHelper;
import junit.framework.TestCase;

/**
 * This is the unit test for the XHTMLBasicElementHelper class and the
 * ElementHelper interface.
 *
 * @author <a href="mailto:phil.weighill-smith@volantis.com">Phil W-S</a>
 */
public class XHTMLBasicElementHelperTestCase extends TestCase {

    /**
     * Factory to use to create DOM objects.
     */
    protected DOMFactory domFactory = DOMFactory.getDefaultInstance();

    public XHTMLBasicElementHelperTestCase(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Ensures that the singleton mechanism works.
     */
    public void testGetInstance() {
        assertNotNull("The singleton should exist but is null",
            XHTMLBasicElementHelper.getInstance());
        assertTrue("The singleton should implement ElementHelper but does not",
            XHTMLBasicElementHelper.getInstance() instanceof ElementHelper);
    }

    /**
     * Simple method test
     */
    public void testIsTable() {
        ElementHelper helper = XHTMLBasicElementHelper.getInstance();
        Element table = domFactory.createElement();
        Element span = domFactory.createElement();
        table.setName("table");
        span.setName("span");

        assertTrue("table not indicated as isTable",
                   helper.isTable(table));
        assertTrue("span is indicated as isTable",
                   !helper.isTable(span));
    }

    /**
     * Simple method test
     */
    public void testIsRow() {
        ElementHelper helper = XHTMLBasicElementHelper.getInstance();
        Element row = domFactory.createElement();
        Element span = domFactory.createElement();
        row.setName("tr");
        span.setName("span");

        assertTrue("tr not indicated as isRow",
                   helper.isRow(row));
        assertTrue("span is indicated as isRow",
                   !helper.isRow(span));
    }

    /**
     * Simple method test
     */
    public void testIsCell() {
        ElementHelper helper = XHTMLBasicElementHelper.getInstance();
        Element cell = domFactory.createElement();
        Element span = domFactory.createElement();
        Element th = domFactory.createElement();
        cell.setName("td");
        span.setName("span");
        th.setName("th");

        assertTrue("td not indicated as isCell",
                   helper.isCell(cell));
        assertTrue("span is indicated as isCell",
                   !helper.isCell(span));
        assertTrue("th not indicated as isCell",
                   helper.isCell(th));
    }

    /**
     * Simple method test
     */
    public void testGetTable() {
        ElementHelper helper = XHTMLBasicElementHelper.getInstance();

        assertEquals("getTable failed",
                     helper.getTable(),
                     "table");
    }

    /**
     * Simple method test
     */
    public void testGetRow() {
        ElementHelper helper = XHTMLBasicElementHelper.getInstance();

        assertEquals("getRow failed",
                     helper.getRow(),
                     "tr");
    }

    /**
     * Simple method test
     */
    public void testGetCell() {
        ElementHelper helper = XHTMLBasicElementHelper.getInstance();

        assertEquals("getCell failed",
                     helper.getCell(),
                     "td");
    }

    /**
     * Simple method test
     */
    public void testGetCells() {
        ElementHelper helper = XHTMLBasicElementHelper.getInstance();
        String[] cells = { "td", "th" };
        String[] actuals = helper.getCells();

        assertEquals("getCells failed to return the expected number of " +
                     "options",
                     actuals.length,
                     cells.length);

        for (int i = 0;
             i < cells.length;
             i++) {
            boolean found = false;

            for (int j = 0;
                 !found && (j < actuals.length);
                 j++) {
                found = (cells[i] == actuals[j]);
            }

            if (!found) {
                fail("getCells did not return " + cells[i]);
            }
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
