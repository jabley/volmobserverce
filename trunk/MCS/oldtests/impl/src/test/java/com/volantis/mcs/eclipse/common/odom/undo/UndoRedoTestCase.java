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
package com.volantis.mcs.eclipse.common.odom.undo;

import com.volantis.mcs.eclipse.common.odom.ODOMAttribute;
import com.volantis.mcs.eclipse.common.odom.ODOMElement;
import com.volantis.mcs.eclipse.common.odom.ODOMFactory;
import com.volantis.mcs.xml.xpath.XPath;
import com.volantis.synergetics.testtools.TestCaseAbstract;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Text;
import org.jdom.input.SAXBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Tests the {@link UndoRedoManager} when different maipulations are
 * programmatically applied to a Document
 */
public class UndoRedoTestCase extends TestCaseAbstract {

    final static String[] elems1_2_3 = new String[]{"elem01",
                                                    "elem02",
                                                    "elem03"};

    final static String[] initialContentOfElem03 = {"text03",
                                                    "elem031",
                                                    "more_text03",
                                                    "elem032",
                                                    "more_more_text03",
                                                    "elem033"};

    private static final String XPATH_ATTR0 = "/ns0:elem0/@attr0";
    private static final String XPATH_ATTR01 = "/ns0:elem0/elem01/@attr01";
    private static final String XPATH_TEXT01 = "/ns0:elem0/elem01/text()";
    private static final String XPATH_ELEM03 = "/ns0:elem0/elem03";
    private static final String XPATH_ELEM033 = XPATH_ELEM03 + "/elem033";

    private Document document;
    private ODOMElement rootElement;
    private UndoRedoManager undoRedoManager;
    private MockUndoRedoMementoOriginator undoRedoMementoOriginator;
    private ODOMFactory odomFactory;


    public UndoRedoTestCase(String string) {
        super(string);
    }


    protected void setUp() throws Exception {
        super.setUp();
        SAXBuilder builder = new SAXBuilder();
        odomFactory = new ODOMFactory();
        builder.setFactory(odomFactory);

        document =
        builder.build(getClass().
                      getResourceAsStream("UndoRedoTestCase01.xml"));
        rootElement = (ODOMElement) document.getRootElement();

        undoRedoMementoOriginator = new MockUndoRedoMementoOriginator();

        undoRedoManager =
        new UndoRedoManager(rootElement, undoRedoMementoOriginator);
    }


    protected void tearDown() throws Exception {
        undoRedoManager.dispose();
        super.tearDown();
    }


    /**
     * testcase self-sanity check
     *
     * @throws Exception
     */
    public void testDocumentAsExpectedByTestCase() throws Exception {
        assertEquals("elem0", rootElement.getName());
        assertEquals("urn:UndoRedoTestCase:0",
                     rootElement.getNamespaceURI());
        assertEquals("value0", rootElement.getAttributeValue("attr0"));
        assertChildrenElementNamesEquals(elems1_2_3,
                                         rootElement.getChildren());
    }


    public void testManagerInitialStateCanUndoRedo()
            throws Exception {
        testDocumentAsExpectedByTestCase();

        //now let's test initial state of manager
        assertFalse(undoRedoManager.canUndo());
        assertFalse(undoRedoManager.canRedo());

        rootElement.getAttribute("attr0").setValue("value0*");
        assertEquals("value0*", rootElement.getAttributeValue("attr0"));

        assertTrue(undoRedoManager.canUndo());
        assertFalse(undoRedoManager.canRedo());
    }


    public void testChangeAttributeValueUndoRedo() throws Exception {
        //1st change
        rootElement.getAttribute("attr0").setValue("value0*");

        { //I'm using a block to give xpaths local scope
            undoRedoManager.undo();
            UndoRedoInfo undoRedoInfo = undoRedoMementoOriginator.getLastUndoRedoInfoAndClear();
            assertEquals("value0", rootElement.getAttributeValue("attr0"));
            assertFalse(undoRedoManager.canUndo());
            assertTrue(undoRedoManager.canRedo());
            assertXPathsEquals(new String[]{XPATH_ATTR0},
                               undoRedoInfo.getChangedNodesXPaths());

            List expectedChangedElements = new ArrayList();
            expectedChangedElements.add(rootElement);
            assertEquals(expectedChangedElements,
                         undoRedoInfo.getChangedElements(document));
        }

        {
            undoRedoManager.redo();
            UndoRedoInfo undoRedoInfo = undoRedoMementoOriginator.getLastUndoRedoInfoAndClear();
            assertEquals("value0*", rootElement.getAttributeValue("attr0"));
            assertTrue(undoRedoManager.canUndo());
            assertFalse(undoRedoManager.canRedo());

            assertXPathsEquals(new String[]{XPATH_ATTR0}, undoRedoInfo.getChangedNodesXPaths());
        }

        //2nd change
        {
            undoRedoManager.demarcateUOW();
            assertTrue(undoRedoManager.canUndo());
            assertFalse(undoRedoManager.canRedo());
            rootElement.getChild("elem01").getAttribute("attr01").setValue(
                    "value01*");
            assertTrue(undoRedoManager.canUndo());
            assertFalse(undoRedoManager.canRedo());
            undoRedoManager.undo();
            assertEquals("value01",
                         rootElement.getChild("elem01").getAttribute("attr01")
                         .getValue());
            assertTrue(undoRedoManager.canUndo());
            assertTrue(undoRedoManager.canRedo());

            UndoRedoInfo undoRedoInfo = undoRedoMementoOriginator.getLastUndoRedoInfoAndClear();

            assertXPathsEquals(new String[]{XPATH_ATTR01},
                               undoRedoInfo.getChangedNodesXPaths());

            List expectedChangedElements = new ArrayList();
            expectedChangedElements.add(rootElement.getChild("elem01"));
            assertEquals(expectedChangedElements,
                         undoRedoInfo.getChangedElements(document));
        }

        {
            undoRedoManager.redo();
            UndoRedoInfo undoRedoInfo = undoRedoMementoOriginator.getLastUndoRedoInfoAndClear();
            List xpaths = undoRedoInfo.getChangedNodesXPaths();
            assertEquals("value01*",
                         rootElement.getChild("elem01").getAttribute("attr01")
                         .getValue());
            assertXPathsEquals(new String[]{XPATH_ATTR01}, xpaths);
        }

    }


    public void testUndoRedoStatusListener() {
        TestUndoRedoStatusListener myListener = new TestUndoRedoStatusListener();
        undoRedoManager.addUndoRedoListener(myListener);

        //first change to the doc should enable undo
        rootElement.getAttribute("attr0").setValue("value0*");
        assertTrue(myListener.checkNotifiedSinceLastCheck());
        assertFalse("sanity check of 'checkNotifiedSinceLastCheck'",
                    myListener.checkNotifiedSinceLastCheck());
        assertTrue(myListener.lastEvent.canUndo());
        assertFalse(myListener.lastEvent.canRedo());

        //another change goes into the same UOW - no status change
        rootElement.getAttribute("attr0").setValue("value0**");
        assertFalse(myListener.checkNotifiedSinceLastCheck());

        //undo enables the redo
        undoRedoManager.undo();
        assertTrue(myListener.checkNotifiedSinceLastCheck());
        assertFalse(myListener.lastEvent.canUndo());
        assertTrue(myListener.lastEvent.canRedo());

        //demarcation does not change status
        undoRedoManager.demarcateUOW();
        assertFalse(myListener.checkNotifiedSinceLastCheck());
        assertFalse(myListener.lastEvent.canUndo());
        assertTrue(myListener.lastEvent.canRedo());

        //document changed after demarcation - status changed: undo enabled and redo disabled
        rootElement.getAttribute("attr0").setValue("value0***");
        assertTrue(myListener.checkNotifiedSinceLastCheck());
        assertTrue(myListener.lastEvent.canUndo());
        assertFalse(myListener.lastEvent.canRedo());
    }


    public void testChangeTextUndoRedo() throws Exception {
        //1st change
        rootElement.getChild("elem01").setText("text01*");

        assertTrue(undoRedoManager.canUndo());
        assertFalse(undoRedoManager.canRedo());

        {
            undoRedoManager.undo();
            UndoRedoInfo undoRedoInfo = undoRedoMementoOriginator.getLastUndoRedoInfoAndClear();

            assertEquals("text01",
                         rootElement.getChild("elem01").getTextTrim());
            assertFalse(undoRedoManager.canUndo());
            assertTrue(undoRedoManager.canRedo());
            //it's the result of a remove and an add
            assertXPathsEquals(new String[]{XPATH_TEXT01}, undoRedoInfo.getChangedNodesXPaths());
        }

        {
            undoRedoManager.redo();
            UndoRedoInfo undoRedoInfo = undoRedoMementoOriginator.getLastUndoRedoInfoAndClear();
            assertEquals("text01*",
                         rootElement.getChild("elem01").getTextTrim());
            assertTrue(undoRedoManager.canUndo());
            assertFalse(undoRedoManager.canRedo());
            assertXPathsEquals(new String[]{XPATH_TEXT01}, undoRedoInfo.getChangedNodesXPaths());
        }

        //2nd change
        {
            undoRedoManager.demarcateUOW();
            assertTrue(undoRedoManager.canUndo());
            assertFalse(undoRedoManager.canRedo());
            rootElement.getChild("elem01").setText("text01**");  //2stars in the text
            assertTrue(undoRedoManager.canUndo());
            assertFalse(undoRedoManager.canRedo());

            undoRedoManager.undo();
            UndoRedoInfo undoRedoInfo = undoRedoMementoOriginator.getLastUndoRedoInfoAndClear();

            assertEquals("text01*",
                         rootElement.getChild("elem01").getTextTrim());
            assertTrue(undoRedoManager.canUndo());
            assertTrue(undoRedoManager.canRedo());
            assertXPathsEquals(new String[]{XPATH_TEXT01}, undoRedoInfo.getChangedNodesXPaths());
        }

        {
            undoRedoManager.redo();
            UndoRedoInfo undoRedoInfo = undoRedoMementoOriginator.getLastUndoRedoInfoAndClear();

            assertEquals("text01**",
                         rootElement.getChild("elem01").getTextTrim());
            assertXPathsEquals(new String[]{XPATH_TEXT01}, undoRedoInfo.getChangedNodesXPaths());
        }
    }


    public void testRedoShouldDoNothing() throws Exception {

        {
            rootElement.getChild("elem01").setText("text01*");
            undoRedoManager.undo();
            UndoRedoInfo undoRedoInfo = undoRedoMementoOriginator.getLastUndoRedoInfoAndClear();

            assertXPathsEquals(new String[]{XPATH_TEXT01}, undoRedoInfo.getChangedNodesXPaths());
        }

        {
            //another change after undo -> redo should be a no-op
            rootElement.getChild("elem01").setText("text01**");
            assertFalse(undoRedoManager.canRedo());

            undoRedoManager.redo();
            assertNull("originator must not be called again", undoRedoMementoOriginator.getLastUndoRedoInfoAndClear());
            assertEquals("text01**",
                         rootElement.getChild("elem01").getTextTrim());
        }
    }

    public void testUndoShouldDoNothing() throws Exception {
        {
            rootElement.getChild("elem01").setText("text01*");
            undoRedoManager.undo();
            UndoRedoInfo undoRedoInfo = undoRedoMementoOriginator.getLastUndoRedoInfoAndClear();

            assertXPathsEquals(new String[]{XPATH_TEXT01}, undoRedoInfo.getChangedNodesXPaths());
        }

        {
            //another undo when the undo stack dhould be empty -> undo should be a no-op
            assertFalse(undoRedoManager.canUndo());
            undoRedoManager.undo();
            assertNull("originator must not be called again", undoRedoMementoOriginator.getLastUndoRedoInfoAndClear());
            assertEquals("text01",
                         rootElement.getChild("elem01").getTextTrim());
        }
    }


    public void testAddElementUndoRedo() throws Exception {
        final String[] elementsExpectedAfterAdd = new String[]{"elem01",
                                                               "elem01b",
                                                               "elem02",
                                                               "elem03"};

        List children = rootElement.getChildren();
        Element odomElement = odomFactory.element("elem01b");
        children.add(1, odomElement);

        assertChildrenElementNamesEquals(elementsExpectedAfterAdd,
                                         rootElement.getChildren());

        {
            undoRedoManager.undo();
            UndoRedoInfo undoRedoInfo = undoRedoMementoOriginator.getLastUndoRedoInfoAndClear();

            assertChildrenElementNamesEquals(elems1_2_3,
                                             rootElement.getChildren());
            assertXPathsEquals(new String[]{}, undoRedoInfo.getChangedNodesXPaths()); //undo removed a node

        }

        {
            undoRedoManager.redo();
            UndoRedoInfo undoRedoInfo = undoRedoMementoOriginator.getLastUndoRedoInfoAndClear();

            assertChildrenElementNamesEquals(elementsExpectedAfterAdd,
                                             rootElement.getChildren());
            //redo re-added the node
            assertXPathsEquals(new String[]{"/ns0:elem0/elem01b"},
                               undoRedoInfo.getChangedNodesXPaths());

            List expectedChangedElements = new ArrayList();
            expectedChangedElements.add(odomElement);
            assertEquals(expectedChangedElements,
                         undoRedoInfo.getChangedElements(document));
        }
    }


    public void testRemoveElementUndoRedo() throws Exception {
        final String[] elementsExpectedAfterRemove = new String[]{"elem01",
                                                                  "elem03"};
        rootElement.removeChild("elem02");
        assertChildrenElementNamesEquals(elementsExpectedAfterRemove,
                                         rootElement.getChildren());

        {
            undoRedoManager.undo();
            UndoRedoInfo undoRedoInfo = undoRedoMementoOriginator.getLastUndoRedoInfoAndClear();

            assertChildrenElementNamesEquals(elems1_2_3,
                                             rootElement.getChildren());
            //redo re-added the node
            assertXPathsEquals(new String[]{"/ns0:elem0/elem02"}, undoRedoInfo.getChangedNodesXPaths());
        }

        {
            undoRedoManager.redo();
            UndoRedoInfo undoRedoInfo = undoRedoMementoOriginator.getLastUndoRedoInfoAndClear();

            assertChildrenElementNamesEquals(elementsExpectedAfterRemove,
                                             rootElement.getChildren());
            assertXPathsEquals(new String[]{}, undoRedoInfo.getChangedNodesXPaths()); //removed a node
        }
    }


    public void testRemoveElementFromChildrenUndoRedo() throws Exception {
        final String[] elementsExpectedAfterRemove = new String[]{"elem01",
                                                                  "elem03"};
        rootElement.getChildren().remove(1);
        assertChildrenElementNamesEquals(elementsExpectedAfterRemove,
                                         rootElement.getChildren());

        {
            undoRedoManager.undo();
            UndoRedoInfo undoRedoInfo = undoRedoMementoOriginator.getLastUndoRedoInfoAndClear();

            assertChildrenElementNamesEquals(elems1_2_3,
                                             rootElement.getChildren());
            //redo re-added the node
            assertXPathsEquals(new String[]{"/ns0:elem0/elem02"},undoRedoInfo.getChangedNodesXPaths());
        }

        {
            undoRedoManager.redo();
            UndoRedoInfo undoRedoInfo = undoRedoMementoOriginator.getLastUndoRedoInfoAndClear();

            assertChildrenElementNamesEquals(elementsExpectedAfterRemove,
                                             rootElement.getChildren());
            assertXPathsEquals(new String[]{}, undoRedoInfo.getChangedNodesXPaths()); //removed a node
        }
    }


    /**
     * This test shows a quirk in the current implememtation.
     * Adding and removing the same node causes the undo to return
     * an xpath to an element that does not exist
     */
    public void testAddAndRemoveTheSameNodeInTheSameUOW() {
        Element odomElement = odomFactory.element("elem04");
        rootElement.addContent(odomElement);
        assertTrue(rootElement.removeContent(odomElement));

        {
            undoRedoManager.undo();
            UndoRedoInfo undoRedoInfo = undoRedoMementoOriginator.getLastUndoRedoInfoAndClear();

            assertXPathsEquals(new String[]{"/ns0:elem0/elem04"}, undoRedoInfo.getChangedNodesXPaths());
        }

        {
            undoRedoManager.redo();
            UndoRedoInfo undoRedoInfo = undoRedoMementoOriginator.getLastUndoRedoInfoAndClear();

            assertXPathsEquals(new String[]{"/ns0:elem0/elem04"}, undoRedoInfo.getChangedNodesXPaths());
        }
    }


    public void testAddAttributeUndoRedo() throws Exception {
        Map expectedAttributesBeforeAdd = new HashMap();
        expectedAttributesBeforeAdd.put("attr01", "value01");

        Map expectedAttributesAfterAdd = new HashMap();
        expectedAttributesAfterAdd.put("attr01", "value01");
        expectedAttributesAfterAdd.put("attr01_1", "value01_1");

        rootElement.getChild("elem01").getAttributes().add(
                new ODOMAttribute("attr01_1", "value01_1"));

        assertAttributesEquals(expectedAttributesAfterAdd,
                               rootElement.getChild("elem01").getAttributes());

        {
            undoRedoManager.undo();
            UndoRedoInfo undoRedoInfo = undoRedoMementoOriginator.getLastUndoRedoInfoAndClear();

            assertAttributesEquals(expectedAttributesBeforeAdd,
                                   rootElement.getChild("elem01")
                                   .getAttributes());
            assertXPathsEquals(new String[]{}, undoRedoInfo.getChangedNodesXPaths()); //removed a node
        }

        {
            undoRedoManager.redo();
            UndoRedoInfo undoRedoInfo = undoRedoMementoOriginator.getLastUndoRedoInfoAndClear();

            assertAttributesEquals(expectedAttributesAfterAdd,
                                   rootElement.getChild("elem01")
                                   .getAttributes());
            final String xpathAttr01_1 = XPATH_ATTR01 + "_1";
            assertXPathsEquals(new String[]{xpathAttr01_1}, undoRedoInfo.getChangedNodesXPaths()); //added a node
        }
    }


    public void testRemoveAttributeUndoRedo() throws Exception {
        Map expectedAttributesBeforeRemove = new HashMap();
        expectedAttributesBeforeRemove.put("attr02", "value02");

        Map expectedAttributesAfterRemove = new HashMap();

        rootElement.getChild("elem02").getAttributes().remove(0);

        assertAttributesEquals(expectedAttributesAfterRemove,
                               rootElement.getChild("elem02").getAttributes());

        {
            undoRedoManager.undo();
            UndoRedoInfo undoRedoInfo = undoRedoMementoOriginator.getLastUndoRedoInfoAndClear();

            assertAttributesEquals(expectedAttributesBeforeRemove,
                                   rootElement.getChild("elem02")
                                   .getAttributes());
            assertXPathsEquals(new String[]{"/ns0:elem0/elem02/@attr02"},
                               undoRedoInfo.getChangedNodesXPaths());
        }

        {
            undoRedoManager.redo();
            UndoRedoInfo undoRedoInfo = undoRedoMementoOriginator.getLastUndoRedoInfoAndClear();

            assertAttributesEquals(expectedAttributesAfterRemove,
                                   rootElement.getChild("elem02")
                                   .getAttributes());
            assertXPathsEquals(new String[]{}, undoRedoInfo.getChangedNodesXPaths());
        }
    }


    public void testAddMixedContentUndoRedo() throws Exception {

        final String[] expectedContent0 = new String[0];
        final String[] expectedContent1 = new String[]{"text04-1",
                                                       "elem04-1",
                                                       "text04-2",
                                                       "elem04-2"};
        final String[] expectedContent2 = new String[]{"text04-1",
                                                       "elem04-1",
                                                       "text04-2",
                                                       "elem04-2",
                                                       "text04-3"};

        Element elem033 = rootElement.getChild("elem03").getChild("elem033");

        assertMixedContentsMatches(expectedContent0, elem033.getContent());

        elem033.addContent(odomFactory.text("text04-1"));
        elem033.addContent(odomFactory.element("elem04-1"));
        elem033.addContent(odomFactory.text("text04-2"));
        elem033.addContent(odomFactory.element("elem04-2"));

        {
            undoRedoManager.undo();
            UndoRedoInfo undoRedoInfo = undoRedoMementoOriginator.getLastUndoRedoInfoAndClear();

            assertMixedContentsMatches(expectedContent0,
                                       elem033.getContent());
            assertXPathsEquals(new String[]{}, undoRedoInfo.getChangedNodesXPaths()); //only removals
        }

        {
            undoRedoManager.redo();
            UndoRedoInfo undoRedoInfo = undoRedoMementoOriginator.getLastUndoRedoInfoAndClear();

            assertMixedContentsMatches(expectedContent1,
                                       elem033.getContent());
            assertXPathsEquals(new String[]{XPATH_ELEM033 + "/text()",
                                            XPATH_ELEM033 + "/elem04-1",
                                            XPATH_ELEM033 + "/text()[2]",
                                            XPATH_ELEM033 + "/elem04-2"},
                               undoRedoInfo.getChangedNodesXPaths());
        }

        {
            //this change goes into a new unit of work
            elem033.addContent(odomFactory.text("text04-3"));
            undoRedoManager.undo();
            UndoRedoInfo undoRedoInfo = undoRedoMementoOriginator.getLastUndoRedoInfoAndClear();

            assertMixedContentsMatches(expectedContent1,
                                       elem033.getContent());
            assertXPathsEquals(new String[]{}, undoRedoInfo.getChangedNodesXPaths()); //removal
        }

        {
            undoRedoManager.redo();
            UndoRedoInfo undoRedoInfo = undoRedoMementoOriginator.getLastUndoRedoInfoAndClear();

            assertMixedContentsMatches(expectedContent2,
                                       elem033.getContent());
            assertXPathsEquals(new String[]{XPATH_ELEM033 + "/text()[3]"},
                               undoRedoInfo.getChangedNodesXPaths());
        }
    }


    public void testRemoveMixedContentUndoRedo() throws Exception {

        final String[] expectedContentAfterFirstUOW = {"text03",
                                                       "elem031",
                                                       "more_text03",
                                                       "more_more_text03",
                                                       "elem033"};

        Element elem03 = rootElement.getChild("elem03");
        elem03.getContent().remove(3);  //elem032
        elem03.removeAttribute("attr03");

        undoRedoManager.demarcateUOW();

        elem03.getContent().remove(0); //text03
        elem03.removeChild("elem031");
        elem03.getContent().remove(0); //more_text03
        elem03.removeChild("elem033");
        elem03.getContent().remove(0); //more_more_text03

        assertTrue(elem03.getContent().isEmpty());
        assertTrue(elem03.getAttributes().isEmpty());

        {
            undoRedoManager.undo();
            UndoRedoInfo undoRedoInfo = undoRedoMementoOriginator.getLastUndoRedoInfoAndClear();

            assertMixedContentsMatches(expectedContentAfterFirstUOW,
                                       elem03.getContent());
            assertTrue(elem03.getAttributes().isEmpty());

            assertXPathsEquals(new String[]{XPATH_ELEM03 + "/text()",
                                            XPATH_ELEM033,
                                            XPATH_ELEM03 + "/text()[1]",
                                            XPATH_ELEM03 + "/elem031"},
                               undoRedoInfo.getChangedNodesXPaths());
        }

        {
            undoRedoManager.undo();
            UndoRedoInfo undoRedoInfo = undoRedoMementoOriginator.getLastUndoRedoInfoAndClear();

            assertMixedContentsMatches(initialContentOfElem03,
                                       elem03.getContent());
            assertEquals(1, elem03.getAttributes().size());
            assertXPathsEquals(new String[]{XPATH_ELEM03 + "/@attr03",
                                            XPATH_ELEM03 + "/elem032"},
                               undoRedoInfo.getChangedNodesXPaths());
        }

        {
            undoRedoManager.redo();
            UndoRedoInfo undoRedoInfo = undoRedoMementoOriginator.getLastUndoRedoInfoAndClear();

            assertMixedContentsMatches(expectedContentAfterFirstUOW,
                                       elem03.getContent());
            assertXPathsEquals(new String[]{}, undoRedoInfo.getChangedNodesXPaths());
        }

        {
            undoRedoManager.redo();
            UndoRedoInfo undoRedoInfo = undoRedoMementoOriginator.getLastUndoRedoInfoAndClear();

            assertTrue(elem03.getContent().isEmpty());
            assertXPathsEquals(new String[]{}, undoRedoInfo.getChangedNodesXPaths());
        }
    }


    public void testBatchRemovalsClearContentUndoRedo() throws Exception {
        final String[] elementsExpectedAfterRemove = new String[]{"elem01",
                                                                  "elem03"};
        rootElement.removeChild("elem02");
        undoRedoManager.demarcateUOW();
        rootElement.getContent().clear();

        {
            undoRedoManager.undo();
            UndoRedoInfo undoRedoInfo = undoRedoMementoOriginator.getLastUndoRedoInfoAndClear();

            assertChildrenElementNamesEquals(elementsExpectedAfterRemove,
                                             rootElement.getChildren());
        }

        {
            undoRedoManager.undo();
            UndoRedoInfo undoRedoInfo = undoRedoMementoOriginator.getLastUndoRedoInfoAndClear();

            assertChildrenElementNamesEquals(elems1_2_3,
                                             rootElement.getChildren());
            assertXPathsEquals(new String[]{"/ns0:elem0/elem02"}, undoRedoInfo.getChangedNodesXPaths());
        }
    }


    public void testBatchRemovalSetMixedContentUndoRedo() throws Exception {

        final String[] expectedContentAfterSet = {"elem03-1",
                                                  "elem03-2",
                                                  "text03-2",
                                                  "elem03-3"};
        Element elem03 = rootElement.getChild("elem03");

        List new03Content = new ArrayList();
        new03Content.add(odomFactory.element("elem03-1"));
        new03Content.add(odomFactory.element("elem03-2"));
        new03Content.add(odomFactory.text("text03-2"));
        new03Content.add(odomFactory.element("elem03-3"));

        elem03.setContent(new03Content);
        assertMixedContentsMatches(expectedContentAfterSet,
                                   elem03.getContent());

        {
            undoRedoManager.undo();
            UndoRedoInfo undoRedoInfo = undoRedoMementoOriginator.getLastUndoRedoInfoAndClear();

            assertMixedContentsMatches(initialContentOfElem03,
                                       elem03.getContent());
        }

        {
            undoRedoManager.redo();
            UndoRedoInfo undoRedoInfo = undoRedoMementoOriginator.getLastUndoRedoInfoAndClear();

            assertMixedContentsMatches(expectedContentAfterSet,
                                       elem03.getContent());
            assertEquals("redo expected to re-add the 4 new content nodes",
                         4,
                         undoRedoInfo.getChangedNodesXPaths().size());
        }
    }


    public void testUndoRedoReplaceElementWithFragmentContainingElement()
            throws Exception {
        Element elem02 = rootElement.getChild("elem02");
        elem02.detach();

        Element newElem02 = odomFactory.element("newElem02");
        newElem02.addContent(elem02);
        rootElement.getChildren().add(1, newElem02);
        assertNotNull("elem02 in new pos",
                      rootElement.getChild("newElem02").getChild("elem02"));

        {
            undoRedoManager.undo();
            UndoRedoInfo undoRedoInfo = undoRedoMementoOriginator.getLastUndoRedoInfoAndClear();

            assertChildrenElementNamesEquals(elems1_2_3,
                                             rootElement.getChildren());
            assertXPathsEquals(new String[]{"/ns0:elem0/elem02"}, undoRedoInfo.getChangedNodesXPaths());
        }

        {
            undoRedoManager.redo();
            UndoRedoInfo undoRedoInfo = undoRedoMementoOriginator.getLastUndoRedoInfoAndClear();

            assertChildrenElementNamesEquals(new String[]{"elem01",
                                                          "newElem02",
                                                          "elem03"},
                                             rootElement.getChildren());
            assertXPathsEquals(new String[]{"/ns0:elem0/newElem02"}, undoRedoInfo.getChangedNodesXPaths());
            assertNotNull("elem02 in new pos after redo",
                          rootElement.getChild("newElem02").getChild("elem02"));
        }

    }


//=============================================================================
    /**
     * mock UndoRedoStatusListener implementation
     */
    private class TestUndoRedoStatusListener
            implements UndoRedoStatusListener {
        UndoRedoStatusEvent lastEvent = null;
        private UndoRedoStatusEvent lastEventForCheckMethod = null;


        synchronized public void undoStatusChanged(UndoRedoStatusEvent event) {
            lastEvent = event;
        }


        synchronized boolean checkNotifiedSinceLastCheck() {
            boolean result = (lastEventForCheckMethod != lastEvent);
            lastEventForCheckMethod = lastEvent;
            return result;
        }
    }


    /**
     * helper method for checking mixed content
     *
     * @param expectedContent - when a string contains 'text' the corresponding
     *                        node must be Text, else must be an Element
     * @param content
     */
    private static void assertMixedContentsMatches(String[] expectedContent,
                                                   List content) {
        assertEquals("size", expectedContent.length, content.size());

        for (int i = 0; i < expectedContent.length; i++) {
            String s = expectedContent[i];
            Object o = content.get(i);
            if (s.indexOf("text") >= 0) {
                assertEquals(s, ((Text) o).getText().trim());
            } else {
                assertEquals(s, ((Element) o).getName());
            }
        }
    }


    /**
     * helper method for checking attributes (unordered)
     *
     * @param expectedNameValuePairs
     * @param actualAttributes
     */
    private static void assertAttributesEquals(Map expectedNameValuePairs,
                                               List actualAttributes) {
        assertEquals("size of list",
                     expectedNameValuePairs.size(),
                     actualAttributes.size());
        for (int i = 0; i < actualAttributes.size(); i++) {
            Attribute attribute = (Attribute) actualAttributes.get(i);
            assertTrue("attribute name not found",
                       expectedNameValuePairs.containsKey(attribute.getName()));
            assertEquals("attribute values do not match",
                         attribute.getValue(),
                         expectedNameValuePairs.get(attribute.getName()));
        }
    }


    /**
     * helper method for checking children element names
     *
     * @param expectedNames
     * @param children
     */
    private static void assertChildrenElementNamesEquals(
            String[] expectedNames, List children) {
        assertEquals(expectedNames.length, children.size());
        for (int i = 0; i < expectedNames.length; i++) {
            assertEquals("at index=" + i, expectedNames[i],
                         ((ODOMElement) children.get(i)).getName());

        }
    }


    /**
     * helper method for checking a list of xpaths
     *
     * @param expectedXPathsAsStrings
     * @param actualXPathsObjects
     */
    private static void assertXPathsEquals(String[] expectedXPathsAsStrings,
                                           List actualXPathsObjects) {
        assertEquals(expectedXPathsAsStrings.length,
                     actualXPathsObjects.size());
        for (int i = 0; i < expectedXPathsAsStrings.length; i++) {
            assertEquals("at index=" + i,
                         expectedXPathsAsStrings[i],
                         ((XPath) actualXPathsObjects.get(i)).getExternalForm());

        }

    }


    private class MockUndoRedoMementoOriginator
            implements UndoRedoMementoOriginator {

        public MockUndoRedoMementoOriginator() {
        }


        UndoRedoInfo undoRedoInfo;


        public UndoRedoMemento takeSnapshot() {
            return UndoRedoMemento.NULLOBJ;
        }


        public void restoreSnapshot(UndoRedoInfo undoRedoInfo) {
            assertNotNull(undoRedoInfo);
            this.undoRedoInfo = undoRedoInfo;
        }

        //we clear the field so that tests invoking the originator more than
        //once will only succeed if a restore has been called with fresh info
        public UndoRedoInfo getLastUndoRedoInfoAndClear() {
            UndoRedoInfo result = undoRedoInfo ;
            undoRedoInfo=null;
            return result;
        }

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

 17-Feb-04	2988/2	eduardo	VBM:2004020908 undo/redo reafctoring for multi-page editor

 09-Feb-04	2800/4	eduardo	VBM:2004012802 undo redo works from outline view

 05-Feb-04	2800/2	eduardo	VBM:2004012802 undo redo hooked in eclipse with demarcation. Designed just for single page editors

 29-Jan-04	2689/4	eduardo	VBM:2003112407 undo/redo manager for ODOM

 ===========================================================================
*/
