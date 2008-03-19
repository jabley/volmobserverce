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
package com.volantis.mcs.eclipse.ab.actions.layout;

import com.volantis.mcs.eclipse.common.odom.MCSNamespace;
import com.volantis.mcs.xml.xpath.XPath;
import com.volantis.synergetics.testtools.TestCaseAbstract;

import java.io.StringReader;

import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;

/**
 * Tests {@link ActionSupport}.
 */
public class ActionSupportTestCase extends TestCaseAbstract {

    /**
     * Namespace for use in XPaths. The xpaths must use the "action" prefix
     */
    protected static final Namespace[] namespace = new Namespace[]{
        Namespace.getNamespace("action", MCSNamespace.LPDM.getURI())
    };

    /**
     * Test haveCommonLayout.
     */
    public void testHaveCommonLayout() throws Exception {
        // Forces predicate removal
        String xml =
                "<layout>" +
                "    <canvasLayout>" +
                "        <paneFormat name='other'/>" +
                "    </canvasLayout>" +
                "    <canvasLayout>" +
                "        <gridFormat>" +
                "            <gridFormatRow>" +
                "                <paneFormat name='pane'/>" +
                "            </gridFormatRow>" +
                "        </gridFormat>" +
                "    </canvasLayout>" +
                "</layout>";


        SAXBuilder builder = new SAXBuilder();

        // Default the namespace to LPDM
        builder.setFactory(FormatPrototype.factory);

        Element root = builder.build(new StringReader(xml)).getRootElement();


         XPath gridXPath = new XPath("/action:layout/" +
                "action:canvasLayout[2]/" +
                "action:gridFormat", namespace);

         XPath paneXPath = new XPath("/action:layout/" +
                "action:canvasLayout[2]/" +
                "action:gridFormat/" +
                "action:gridFormatRow/" +
                "action:paneFormat", namespace);

        Element grid = gridXPath.selectSingleElement(root);
        Element pane = paneXPath.selectSingleElement(root);

        assertTrue("pane and grid do not have a common layout",
                ActionSupport.haveCommonLayout(grid, pane));

        XPath otherPaneXPath = new XPath("/action:layout/" +
                "action:canvasLayout[1]/" +
                "action:paneFormat", namespace);

        Element otherPane = otherPaneXPath.selectSingleElement(root);
        assertNotNull(otherPane);
        assertFalse("otherPane and pane do have a common layout",
                ActionSupport.haveCommonLayout(pane, otherPane));
    }

    /**
     * Test isDescendent.
     */
    public void testIsDescendent() throws Exception {
        // Forces predicate removal
        String xml =
                "<layout>" +
                "    <canvasLayout>" +
                "        <paneFormat name='other'/>" +
                "    </canvasLayout>" +
                "    <canvasLayout>" +
                "        <gridFormat>" +
                "            <gridFormatRow>" +
                "                <paneFormat name='pane'/>" +
                "            </gridFormatRow>" +
                "        </gridFormat>" +
                "    </canvasLayout>" +
                "</layout>";


        SAXBuilder builder = new SAXBuilder();

        // Default the namespace to LPDM
        builder.setFactory(FormatPrototype.factory);

        Element root = builder.build(new StringReader(xml)).getRootElement();


         XPath gridXPath = new XPath("/action:layout/" +
                "action:canvasLayout[2]/" +
                "action:gridFormat", namespace);

         XPath paneXPath = new XPath("/action:layout/" +
                "action:canvasLayout[2]/" +
                "action:gridFormat/" +
                "action:gridFormatRow/" +
                "action:paneFormat", namespace);

        Element grid = gridXPath.selectSingleElement(root);
        Element pane = paneXPath.selectSingleElement(root);

        assertTrue("pane is not a descendent of grid",
                ActionSupport.isDescendent(grid, pane));

        XPath otherPaneXPath = new XPath("/action:layout/" +
                "action:canvasLayout[1]/" +
                "action:paneFormat", namespace);

        Element otherPane = otherPaneXPath.selectSingleElement(root);
        assertNotNull(otherPane);
        assertFalse("otherPane is a descendent of grid",
                ActionSupport.isDescendent(grid, otherPane));
    }

    /**
     * Also implicitly tests {@link ActionSupport#equivalentElement} since this
     * is used in the {@link ActionSupport#cloneContainingDeviceLayout} method.
     */
    public void testCloneContainingDeviceLayoutCanvasFound() throws Exception {
        // Forces predicate removal
        Element element = buildDOM(
                "<layout>" +
                "    <canvasLayout>" +
                "        <paneFormat name='other'/>" +
                "    </canvasLayout>" +
                "    <canvasLayout>" +
                "        <gridFormat>" +
                "            <gridFormatRow>" +
                "                <paneFormat name='pane'/>" +
                "            </gridFormatRow>" +
                "        </gridFormat>" +
                "    </canvasLayout>" +
                "</layout>",
                new XPath("/action:layout/" +
                "action:canvasLayout[2]/" +
                "action:gridFormat/" +
                "action:gridFormatRow/" +
                "action:paneFormat",
                        namespace));

        Element clone = ActionSupport.cloneContainingDeviceLayout(
                element);

        assertNotNull("A non-null clone should have been returned",
                clone);

        assertNotSame("The clone should be a different instance",
                element, clone);

        assertEquals("The clone should have the name attribute",
                "pane",
                clone.getAttributeValue("name"));
    }

    public void testCloneContainingDeviceLayoutNotFound()
            throws Exception {
        Element element = buildDOM(
                "<layout>" +
                "    <canvasLayout>" +
                "        <gridFormat>" +
                "            <gridFormatRow>" +
                "                <paneFormat name='pane'/>" +
                "            </gridFormatRow>" +
                "        </gridFormat>" +
                "    </canvasLayout>" +
                "    <canvasLayout>" +
                "        <paneFormat name='other'/>" +
                "    </canvasLayout>" +
                "    <montageLayout>" +
                "        <paneFormat name='other'/>" +
                "    </montageLayout>" +
                "</layout>",
                new XPath("/action:layout/" +
                "action:canvasLayout[1]",
                        namespace));

        Element clone = ActionSupport.cloneContainingDeviceLayout(
                element);

        assertNull("A null clone should have been returned",
                clone);
    }

    /**
     * Also implicitly tests {@link ActionSupport#equivalentElement} since this
     * is used in the {@link ActionSupport#cloneContainingDeviceLayout} method.
     */
    public void testCloneContainingDeviceLayoutMontageFound() throws Exception {
        // No predicate removal needed as there is only one montage
        Element element = buildDOM(
                "<layout>" +
                "    <montageLayout>" +
                "        <segmentFormat name='betty'/>" +
                "    </montageLayout>" +
                "    <canvasLayout>" +
                "        <paneFormat name='other'/>" +
                "    </canvasLayout>" +
                "</layout>",
                new XPath("/action:layout/" +
                "action:montageLayout/" +
                "action:segmentFormat",
                        namespace));

        Element clone = ActionSupport.cloneContainingDeviceLayout(
                element);

        assertNotNull("A non-null clone should have been returned",
                clone);

        assertNotSame("The clone should be a different instance",
                element, clone);

        assertEquals("The clone should have the name attribute",
                "betty",
                clone.getAttributeValue("name"));
    }

    /**
     * Dummy
     */
    public void testEquivalentElement() throws Exception {
        // The target method is implicitly tested by other test cases
    }

    /**
     * Creates a document from the given XML string and returns an element in
     * that document identified by the given XPath. The element must exist.
     *
     * @param xml       the XML string to build the DOM from
     * @param selection the XPath that identifies an element that really
     *                  exists. The path must use the LPDM namespace
     * @return the required element
     */
    protected Element buildDOM(String xml,
                               XPath selection) throws Exception {
        SAXBuilder builder = new SAXBuilder();

        // Default the namespace to LPDM
        builder.setFactory(FormatPrototype.factory);

        return selection.selectSingleElement(
                builder.build(new StringReader(xml)).getRootElement());
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

 19-Aug-04	5264/1	allan	VBM:2004081008 Remove invalid plugin dependencies

 15-Jul-04	4886/1	allan	VBM:2004052812 Tidied some more and added some more tests.

 26-May-04	4470/1	matthew	VBM:2004041406 Reduce flicker in Layout Designer

 06-May-04	4068/1	allan	VBM:2004032103 Structure page policies section.

 21-Jan-04	2635/1	philws	VBM:2003121513 Implement the New Grid and non-Grid Action Commands

 15-Jan-04	2583/1	philws	VBM:2003121512 Add layout constraints

 ===========================================================================
*/
