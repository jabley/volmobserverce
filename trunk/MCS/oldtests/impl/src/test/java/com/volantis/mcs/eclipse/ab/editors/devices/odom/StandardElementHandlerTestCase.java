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
package com.volantis.mcs.eclipse.ab.editors.devices.odom;

import com.volantis.mcs.eclipse.common.odom.ODOMElement;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.JDOMFactory;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

/**
 * Test case for the {@link com.volantis.mcs.eclipse.ab.editors.devices.odom.StandardElementHandler} class
 */
public class StandardElementHandlerTestCase extends TestCaseAbstract {

    /**
     * ODOMFactory
     */
    private static final JDOMFactory ODOM_FACTORY =
            new DeviceODOMElementFactory();

    /**
     * Will be used to generate XML markup from DOM nodes
     */
    private static final XMLOutputter XML_OUTPUTTER = new XMLOutputter();

    /**
     * Used to build DOM nodes from XML markup
     */
    private static final SAXBuilder SAX_BUILDER = new SAXBuilder();

    /**
     * Creates a ODOM hierarchy from some XML markup
     * @param xml the xml markup
     * @return an ODOMElement
     * @throws java.lang.Exception if the xml is badly formed.
     */
    private ODOMElement createElement(String xml) throws Exception {
        SAX_BUILDER.setFactory(ODOM_FACTORY);
        // we need to add dummy root element as  ODOMElement throw
        // a wobbly if a root element is detached from it's Document.
        Document document = SAX_BUILDER.build(new StringReader(
                    "<root>" + xml + "</root>"));
        ODOMElement root = (ODOMElement) document.getRootElement();
        root.getChildren();
        ODOMElement realRoot = (ODOMElement) root.getChildren().get(0);
        realRoot.detach();
        return realRoot;
    }

    /**
     * Checks that the expected XML string matches the element
     * @param expected the expected XML as a string
     * @param actual the actual XML as an element
     * @throws java.lang.Exception if an error occurs
     */
    private void assertElementEquals(String expected, Element actual)
                throws Exception{
        // create a couple of writers
        Writer expectedWriter = new StringWriter();
        Writer actualWriter = new StringWriter();

        // output both the expect and actual elements to the writers
        XML_OUTPUTTER.output(createElement(expected), expectedWriter);
        XML_OUTPUTTER.output(actual, actualWriter);

        // ensure the expected and actual element strings match.
        assertEquals("Element was not as expected: ",
                     expectedWriter.toString(),
                     actualWriter.toString());
    }

    /**
     * This test will ensure that an empty standard element is inserted when
     * an element is created for the first time.
     */
    public void testElementAddition() throws Exception {
        ODOMElement parent = createElement("<parent/>");

        new StandardElementHandler(parent, "a");

        ODOMElement a = createElement("<child name=\"a\"/>");

        // this should result in a standard element being created
        parent.addContent(a);

        // there should be an empty standard element
        assertElementEquals(
                    "<parent>" +
                        "<child name=\"a\">" +
                            "<standard/>" +
                         "</child>" +
                    "</parent>",
                    parent);
    }

    /**
     * This test will ensure that an empty standard element is not inserted when
     * an element is created for the first time but is not the "Managed"
     * element
     */
    public void testNonManagedElementAddition() throws Exception {
        ODOMElement parent = createElement("<parent/>");

        new StandardElementHandler(parent, "a");

        ODOMElement b = createElement("<child name=\"b\"/>");

        // this should result in a standard element being created
        parent.addContent(b);

        // there should be an empty standard element
        assertElementEquals(
                    "<parent>" +
                        "<child name=\"b\"/>" +
                    "</parent>",
                    parent);
    }

    /**
     * This test will ensure that an empty standard element is inserted when
     * an element that has siblings is created for the fist time.
     */
    public void testElementAdditionMultipleChildren() throws Exception {
        ODOMElement parent = createElement(
                    "<parent>" +
                        "<child name=\"b\"/>" +
                        "<child name=\"c\"/>" +
                    "</parent>");

        new StandardElementHandler(parent, "a");

        ODOMElement a = createElement("<child name=\"a\"/>");

        // this should result in a standard element being created
        parent.addContent(a);

        // there should be an empty standard element
        assertElementEquals(
                    "<parent>" +
                        "<child name=\"b\"/>" +
                        "<child name=\"c\"/>" +
                        "<child name=\"a\">" +
                            "<standard/>" +
                        "</child>" +
                    "</parent>",
                    parent);
    }

    /**
     * This test will ensure that an empty standard element is inserted when
     * an element is created for the first time.
     */
    public void testElementUpdate() throws Exception {
        // create a dom element that we will use for testing
        ODOMElement parent = createElement("<parent><child name=\"a\" " +
                                           "value=\"value1\"/></parent>");

        // add an element handler to this
        new StandardElementHandler(parent, "a");

        // get hold of the child elements value attriubes
        Attribute valueAtt = parent.getChild("child").getAttribute("value");
        valueAtt.setValue("value100");

        // ok there should be a standard element
        assertElementEquals(
                    "<parent>" +
                        "<child name=\"a\" value=\"value100\">" +
                            "<standard>" +
                                "<child name=\"a\" value=\"value1\"/>" +
                            "</standard>" +
                        "</child>" +
                    "</parent>",
                    parent);
    }

    /**
     * This test will ensure that an standard element is inserted when
     * the managed element has one of it's attributes deleted
     */
    public void testElementAttributeDeletion() throws Exception {
        // create a dom element that we will use for testing
        ODOMElement parent = createElement("<parent><child name=\"a\" " +
                                           "value=\"value1\"/></parent>");

        // add an element handler to this
        new StandardElementHandler(parent, "a");

        // remove the child elements value attriubes
        parent.getChild("child").removeAttribute("value");

        // ok there should be a standard element
        assertElementEquals(
                    "<parent>" +
                        "<child name=\"a\">" +
                            "<standard>" +
                                "<child name=\"a\" value=\"value1\"/>" +
                            "</standard>" +
                        "</child>" +
                    "</parent>",
                    parent);
    }

    /**
     * This test will ensure that an standard element is inserted when
     * the managed element has one of it's child elements deleted
     */
    public void testElementChildDeletion() throws Exception {
        // create a dom element that we will use for testing
        ODOMElement parent = createElement("<parent>" +
                                             "<child name=\"a\" value=\"1\">" +
                                               "<grandchild/>" +
                                             "</child>" +
                                           "</parent>");

        // add an element handler to this
        new StandardElementHandler(parent, "a");

        // remove the grandchild element
        parent.getChild("child").removeChild("grandchild");

        // ok there should be a standard element
        assertElementEquals(
                    "<parent>" +
                        "<child name=\"a\" value=\"1\">" +
                            "<standard>" +
                                "<child name=\"a\" value=\"1\">" +
                                    "<grandchild/>" +
                                "</child>" +
                            "</standard>" +
                        "</child>" +
                    "</parent>",
                    parent);
    }

    /**
     * This test will ensure that an empty standard element is inserted when
     * an element is created for the first time.
     */
    public void testElementUpdateWithComplexChild() throws Exception {
        // create a dom element that we will use for testing
        ODOMElement parent = createElement(
                    "<parent>" +
                        "<child name=\"a\" value=\"value1\">" +
                            "<grandchild/>" +
                        "</child>" +
                    "</parent>");

        // add an element handler to this
        new StandardElementHandler(parent, "a");

        // get hold of the child elements value attriubes
        Attribute valueAtt = parent.getChild("child").getAttribute("value");
        valueAtt.setValue("value100");

        // ok there should be a standard element
        assertElementEquals(
                    "<parent>" +
                        "<child name=\"a\" value=\"value100\">" +
                            "<grandchild/>" +
                            "<standard>" +
                                "<child name=\"a\" value=\"value1\">" +
                                    "<grandchild/>" +
                                "</child>" +
                            "</standard>" +
                        "</child>" +
                    "</parent>",
                    parent);
    }

    /**
     * Tests the restore method when the element originally did not exist
     * @throws java.lang.Exception
     */
    public void  testRestoreWhenStandardIsEmpty() throws Exception {
        // create an element that has an empty standard element
        ODOMElement parent = createElement(
                    "<parent>" +
                        "<child name=\"z\" value=\"value2\"/>" +
                        "<child name=\"a\" value=\"value100\">" +
                            "<grandchild/>" +
                            "<standard/>" +
                        "</child>" +
                        "<child name=\"b\" value=\"value5\"/>" +
                    "</parent>");

        // register a handler with the parent of the managed element
        StandardElementHandler handler =
                    new StandardElementHandler(parent, "a");

        // restore the element back to the original
        handler.restore();
        // check that the element has been restored
        // ok there should be a standard element
        assertElementEquals(
                    "<parent>" +
                        "<child name=\"z\" value=\"value2\"/>" +
                        "<child name=\"b\" value=\"value5\"/>" +
                    "</parent>",
                    parent);
    }

    /**
     * Test the restore method when the managed element originally existed
     * @throws java.lang.Exception
     */
    public void  testRestoreWhenStandardIsNotEmpty() throws Exception {
        // create an element that has an empty standard element
        ODOMElement parent = createElement(
                    "<parent>" +
                        "<child name=\"z\" value=\"value2\"/>" +
                        "<child name=\"a\" value=\"value100\">" +
                            "<grandchild value=\"fred\"/>" +
                            "<standard>" +
                                "<child name=\"a\" value=\"value3\">" +
                                    "<grandchild value=\"bob\"/>" +
                                "</child>" +
                            "</standard>" +
                        "</child>" +
                        "<child name=\"b\" value=\"value4\"/>" +
                    "</parent>");

        // register a handler with the parent of the managed element
        StandardElementHandler handler =
                    new StandardElementHandler(parent, "a");

        // restore the element back to the original
        handler.restore();
        // check that the element has been restored
        // ok there should be a standard element
        assertElementEquals(
                    "<parent>" +
                        "<child name=\"z\" value=\"value2\"/>" +
                        "<child name=\"a\" value=\"value3\">" +
                            "<grandchild value=\"bob\"/>" +
                        "</child>" +
                        "<child name=\"b\" value=\"value4\"/>" +
                    "</parent>",
                    parent);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 16-Nov-04	4394/4	allan	VBM:2004051018 Undo/Redo in device editor.

 17-May-04	4394/2	allan	VBM:2004051018 StandardElement handler re-write. Undo/redo nearly working.

 14-May-04	4301/1	byron	VBM:2004051018 CC/PP section does not handle undo/redo

 14-Apr-04	3693/5	doug	VBM:2004031001 fixed problem with event identification

 13-Apr-04	3693/3	doug	VBM:2004031001 Fixed bug where a standard element was not being created when the managed element was created for the first time

 05-Apr-04	3693/1	doug	VBM:2004031001 MCS

 ===========================================================================
*/
