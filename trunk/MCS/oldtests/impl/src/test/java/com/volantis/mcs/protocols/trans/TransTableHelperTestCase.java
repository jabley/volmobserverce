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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/protocols/trans/TransTableHelperTestCase.java,v 1.2 2003/01/15 12:42:10 philws Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 14-Jan-03    Phil W-S        VBM:2002110402 - Created. Tests associated
 *                              class.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols.trans;

import com.volantis.mcs.dom.DOMFactory;
import com.volantis.mcs.dom.debug.DOMUtilities;
import com.volantis.mcs.dom.Document;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.protocols.html.XHTMLBasicConfiguration;
import com.volantis.mcs.protocols.html.XHTMLBasicTransFactory;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import org.xml.sax.XMLReader;

/**
 * Tests the TransVisitor behaviour in conjunction with its superclasses.
 */
public class TransTableHelperTestCase extends TestCaseAbstract {

    /**
     * Factory to use to create DOM objects.
     */
    protected DOMFactory domFactory = DOMFactory.getDefaultInstance();

    /**
     * Protocol specific configuration.
     */
    protected XHTMLBasicConfiguration configuration =
            new XHTMLBasicConfiguration();

    /**
     * This method tests the constructors for
     * the com.volantis.mcs.protocols.trans.TransTableHelper class.
     */
    public void notestConstructors() {
    }

    /**
     * This method tests the method public TransTableHelper getInstance()
     * for the com.volantis.mcs.protocols.trans.TransTableHelper class.
     */
    public void testGetInstance()
        throws Exception {
        TransTableHelper instance = getInstance();

        assertEquals("unexpected class for singleton",
                     getHelperClass().getName(),
                     instance.getClass().getName());

        assertSame("singleton unexpectedly changed",
                   instance,
                   getInstance());
    }

    protected TransTableHelper getInstance() {
        return TransTableHelper.getInstance();
    }

    protected Class getHelperClass() {
        return TransTableHelper.class;
    }

    /**
     * This method tests the method public boolean isZero(String)
     * for the com.volantis.mcs.protocols.trans.TransTableHelper class.
     */
    public void testIsZero()
        throws Exception {
        TransTableHelper helper = TransTableHelper.getInstance();

        assertTrue("isZero(\"0\") failed",
                   helper.isZero("0"));
        assertTrue("isZero(null) failed",
                   helper.isZero(null));
        assertTrue("!isZero(\"1\") failed",
                   !helper.isZero("1"));
    }

    /**
     * This method tests the method public boolean tableHasId(Element,TransFactory)
     * for the com.volantis.mcs.protocols.trans.TransTableHelper class.
     */
    public void testTableHasId()
        throws Exception {
        TransTableHelper helper = TransTableHelper.getInstance();

        String tableWithoutId =
            "<root>" +
            "<table>" +
              "<thead>" +
                "<tr>" +
                  "<th><div id=\"fred\">Cell</div></th>" +
                  "<th>Cell</th>" +
                "</tr>" +
              "</thead>" +
              "<tbody>" +
                "<tr>" +
                  "<td><div id=\"fred\">Cell</div></td>" +
                  "<td>Cell</td>" +
                "</tr>" +
              "</tbody>" +
            "</table>" +
            "</root>";

        String tableWithId =
            "<root>" +
            "<table>" +
              "<thead>" +
                "<tr>" +
                  "<th><div id=\"fred\">Cell</div></th>" +
                  "<th>Cell</th>" +
                "</tr>" +
              "</thead>" +
              "<tbody>" +
                "<tr>" +
                  "<td><div id=\"fred\">Cell</div></td>" +
                  "<td id=\"tdid\">Cell</td>" +
                "</tr>" +
              "</tbody>" +
            "</table>" +
            "</root>";

        Document dom = generateDOM(tableWithoutId);

        assertFalse("tableWithoutId failed", helper.tableHasId(
                dom.getRootElement(),
                new XHTMLBasicTransFactory(configuration), true));

        dom = generateDOM(tableWithId);

        assertTrue("tableWithId failed", helper.tableHasId(
                dom.getRootElement(), 
                new XHTMLBasicTransFactory(configuration), true));
    }

    /**
     * This method tests the method public boolean
     * tableHasAttribute()
     * for the com.volantis.mcs.protocols.trans.TransTableHelper class.
     */
    public void testTableHasAttribute()
        throws Exception {
        TransTableHelper helper = TransTableHelper.getInstance();

        String tableWithoutBGColor =
            "<root>" +
            "<table>" +
              "<thead>" +
                "<tr>" +
                  "<th><div id=\"fred\">Cell</div></th>" +
                  "<th>Cell</th>" +
                "</tr>" +
              "</thead>" +
              "<tbody>" +
                "<tr>" +
                  "<td><div id=\"fred\">Cell</div></td>" +
                  "<td>Cell</td>" +
                "</tr>" +
              "</tbody>" +
            "</table>" +
            "</root>";

        String tableWithBGColor =
            "<root>" +
            "<table>" +
              "<thead>" +
                "<tr>" +
                  "<th><div id=\"fred\">Cell</div></th>" +
                  "<th>Cell</th>" +
                "</tr>" +
              "</thead>" +
              "<tbody>" +
                "<tr>" +
                  "<td><div id=\"fred\">Cell</div></td>" +
                  "<td bgcolor=\"bgcolor\">Cell</td>" +
                "</tr>" +
              "</tbody>" +
            "</table>" +
            "</root>";

        Document dom = generateDOM(tableWithoutBGColor);

        assertTrue("tableWithoutAttribute failed",
                !helper.tableHasAttribute(dom.getRootElement(),
                        new XHTMLBasicTransFactory(configuration),
                        "bgcolor", true));

        dom = generateDOM(tableWithBGColor);

        assertTrue("tableWithAttribute failed",
                   helper.tableHasAttribute(dom.getRootElement(),
                           new XHTMLBasicTransFactory(configuration),
                           "bgcolor", true));
    }

    /**
     * This method tests the method public boolean tableContains(Element,TransFactory,String,boolean)
     * for the com.volantis.mcs.protocols.trans.TransTableHelper class.
     */
    public void testTableContains()
        throws Exception {
        TransTableHelper helper = TransTableHelper.getInstance();

        String tableWithoutOneventCell =
            "<root>" +
            "<table>" +
              "<thead>" +
                "<tr>" +
                  "<th><div onevent=\"fred\">Cell</div></th>" +
                  "<th>Cell</th>" +
                "</tr>" +
              "</thead>" +
              "<tbody>" +
                "<tr>" +
                  "<td>" +
                    "<div id=\"fred\">" +
                      "<onevent type=\"xyz\"/>" +
                      "Cell" +
                    "</div>" +
                  "</td>" +
                  "<td>Cell</td>" +
                "</tr>" +
              "</tbody>" +
            "</table>" +
            "</root>";

        String tableWithOneventCell =
            "<root>" +
            "<table>" +
              "<thead>" +
                "<tr>" +
                  "<th><div onevent=\"fred\">Cell</div></th>" +
                  "<th>Cell</th>" +
                "</tr>" +
              "</thead>" +
              "<tbody>" +
                "<tr>" +
                  "<td>" +
                    "<onevent type=\"xyz\"/>" +
                  "</td>" +
                  "<td>Cell</td>" +
                "</tr>" +
              "</tbody>" +
            "</table>" +
            "</root>";

        String tableWithOneventRow =
            "<root>" +
            "<table>" +
              "<thead>" +
                "<tr>" +
                  "<th><div onevent=\"fred\">Cell</div></th>" +
                  "<th>Cell</th>" +
                "</tr>" +
              "</thead>" +
              "<tbody>" +
                "<tr>" +
                  "<td>Cell</td>" +
                  "<onevent type=\"xyz\"/>" +
                  "<td>Cell</td>" +
                "</tr>" +
              "</tbody>" +
            "</table>" +
            "</root>";

        Document dom = generateDOM(tableWithoutOneventCell);
        XHTMLBasicTransFactory transFactory =
                new XHTMLBasicTransFactory(configuration);

        assertTrue("tableWithoutOneventCell(cells) failed",
                   !helper.tableContains(dom.getRootElement(), transFactory,
                           "onevent", true));

        assertTrue("tableWithoutOneventCell(rows) failed",
                   !helper.tableContains(dom.getRootElement(), transFactory,
                           "onevent", false));

        dom = generateDOM(tableWithOneventCell);

        assertTrue("tableWithOneventCell(cells) failed",
                   helper.tableContains(dom.getRootElement(), transFactory,
                           "onevent", true));

        assertTrue("tableWithOneventCell(rows) failed",
                   !helper.tableContains(dom.getRootElement(), transFactory,
                           "onevent", false));

        dom = generateDOM(tableWithOneventRow);

        assertTrue("tableWithOneventRow(cells) failed",
                   helper.tableContains(dom.getRootElement(), transFactory,
                           "onevent", true));

        assertTrue("tableWithOneventRow(rows) failed",
                   helper.tableContains(dom.getRootElement(), transFactory,
                           "onevent", false));
    }

    /**
     * This method tests the method public boolean match(Element,Element,String)
     * for the com.volantis.mcs.protocols.trans.TransTableHelper class.
     */
    public void testMatch()
        throws Exception {
        TransTableHelper helper = TransTableHelper.getInstance();

        Element table1 = domFactory.createElement();
        Element table2 = domFactory.createElement();

        table1.setName("table");
        table2.setName("table");

        table1.setAttribute("bgcolor", "blue");
        table2.setAttribute("bgcolor", "green");

        table1.setAttribute("class", "classy");
        table2.setAttribute("class", "classy");

        table1.setAttribute("id", "fred");

        table2.setAttribute("cellspacing", "10");

        assertTrue("cellpadding failed",
                   helper.match(table1, table2, "cellpadding"));

        assertTrue("bgcolor failed",
                   !helper.match(table1, table2, "bgcolor"));

        assertTrue("class failed",
                   helper.match(table1, table2, "class"));

        assertTrue("id failed",
                   !helper.match(table1, table2, "id"));

        assertTrue("cellspacing failed",
                   !helper.match(table1, table2, "cellspacing"));
    }

    protected Document generateDOM(String xml) throws Exception {
        XMLReader reader = DOMUtilities.getReader();
        Document dom = DOMUtilities.read(reader, xml);

        return dom;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 01-Sep-05	9375/1	geoff	VBM:2005082301 XDIMECP: clean up protocol creation

 22-Aug-05	9363/1	emma	VBM:2005080405 Remove the 'supports multi class' properties

 22-Aug-05	9223/5	emma	VBM:2005080403 Remove style class from within protocols and transformers

 19-Aug-05	9289/3	emma	VBM:2005081602 Refactoring UnabridgedTransformers so they're not singletons

 17-Aug-05	9289/1	emma	VBM:2005081602 Refactoring TransMapper and TransFactory so that they're not singletons

 02-Jun-05	8005/4	pduffin	VBM:2005050404 Moved dom to its own subsystem

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 11-May-05	8167/1	allan	VBM:2005040701 Prevent iMode table optimization when td has style

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 30-Jun-04	4781/1	adrianj	VBM:2002111405 VolantisProtocol.defaultMimeType() and DOMProtocol made abstract

 20-Aug-03	1207/1	adrian	VBM:2003032804 removed suite and main methods from testcase classes

 ===========================================================================
*/
